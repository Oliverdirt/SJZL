package com.ctsi.ssdc.gen.util;

import com.ctsi.ssdc.gen.exception.BusinessException;
import com.ctsi.ssdc.poi.excel.util.PathUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.problem.Status;

import java.io.*;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 */
public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    //删除文件夹或文件
    public static void deleteDir(String dirPath) {
        cn.hutool.core.io.FileUtil.del(PathUtil.filePathFilter(dirPath));
//        File file = new File(PathUtil.filePathFilter(dirPath));
//        File file = new File(dirPath);
//        if (!file.exists()){
//            return;
//        }
//        if(file.isFile()){
//            if(!file.delete()){
//                throw new BusinessException("删除目录失败！", Status.INTERNAL_SERVER_ERROR, "删除目录失败", "删除目录失败", null);
//            }
//        }else {
//            File[] files = file.listFiles();
//            if(files == null)
//            {
//                if(!file.delete()){
//                    throw new BusinessException("删除目录失败！", Status.INTERNAL_SERVER_ERROR, "删除目录失败", "删除目录失败", null);
//                }
//            }else{
//                for (int i = 0; i < files.length; i++)
//                {
//                    deleteDir(files[i].getAbsolutePath());
//                }
//                if(!file.delete()){
//                    throw new BusinessException("删除目录失败！", Status.INTERNAL_SERVER_ERROR, "删除目录失败", "删除目录失败", null);
//                }
//            }
//        }
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath
     *            :待压缩的文件路径
     * @param zipFilePath
     *            :压缩后存放路径
     * @param fileName
     *            :压缩后文件的名称
     * @return
     */
    public static boolean folderToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(zipFilePath+File.separator+fileName+".zip")))){
            byte[] buffer = new byte[1024];
            File sourceFile = new File(sourceFilePath);
            File[] sourceFiles = sourceFile.listFiles();

            if(sourceFiles!=null&&sourceFiles.length>0) {
                for (int i = 0; i < sourceFiles.length; i++) {
                    flag = readAndWrite(out, buffer, sourceFiles[i]);
                }
            }
        }catch (Exception e){

            log.error("压缩文件异常",e);
        }
        return flag;
    }

    private static boolean readAndWrite(ZipOutputStream out, byte[] buffer, File sourceFile){

        try(FileInputStream fis = new FileInputStream(sourceFile)) {
            out.putNextEntry(new ZipEntry(sourceFile.getName()));
            int len;
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.closeEntry();
            return true;
        }catch (Exception e){
            log.error("压缩文件异常",e);
            return false;
        }

    }

    /**
     * 针对多级目录
     * @param srcPath  源文件的绝对路径，可以为文件或文件夹
     * @param destPath 目标文件的绝对路径  /sdcard/.../file_name.zip
     * @throws Exception 失败
     */
    public static void compress(String srcPath, String destPath) {
        //参数检查
        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(destPath)) {
            throw new IllegalArgumentException("srcPath or destPath is illegal");
        }
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new BusinessException("文件不存在！", Status.INTERNAL_SERVER_ERROR, "文件不存在", "文件不存在", null);
        }
        File destFile = new File(destPath);
        if (destFile.exists()) {
            boolean delete = destFile.delete();
            if (!delete) {
                throw new IllegalArgumentException("destFile is exist and do not delete.");
            }
        }

       /* FileOutputStream fos=null;
        CheckedOutputStream cos = null;
        ZipOutputStream zos = null;*/
        try(ZipOutputStream zos= new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(destPath), new CRC32()))) {
             /*fos= new FileOutputStream(destPath);
            // 对目标文件做CRC32校验，确保压缩后的zip包含CRC32值
            cos = new CheckedOutputStream(fos, new CRC32());
            //装饰一层ZipOutputStream，使用zos写入的数据就会被压缩啦
            zos = new ZipOutputStream(cos);*/
            zos.setLevel(9);//设置压缩级别 0-9,0表示不压缩，1表示压缩速度最快，9表示压缩后文件最小；默认为6，速率和空间上得到平衡。
            if (srcFile.isFile()) {
                compressFile("", srcFile, zos);
            } else if (srcFile.isDirectory()) {
                compressFolder("", srcFile, zos);
            }
        } catch (FileNotFoundException e) {
            log.error("文件找不到",e);
        } catch (IOException e) {
            log.error("流异常",e);
        }
    }

    private static void compressFolder(String prefix, File srcFolder, ZipOutputStream zos) throws IOException {
        String newPrefix = prefix + srcFolder.getName() + "/";
        File[] files = srcFolder.listFiles();
        //支持空文件夹
        if(files!=null) {
            if (files.length == 0) {
                compressFile(prefix, srcFolder, zos);
            } else {
                for (File file : files) {
                    if (file.isFile()) {
                        compressFile(newPrefix, file, zos);
                    } else if (file.isDirectory()) {
                        compressFolder(newPrefix, file, zos);
                    }
                }
            }
        }
    }






    /**
     * 压缩文件和空目录
     *
     * @param prefix
     * @param src
     * @param zos
     * @throws IOException
     */
    private static void compressFile(String prefix, File src, ZipOutputStream zos) throws IOException {
        //若是文件,那肯定是对单个文件压缩
        //ZipOutputStream在写入流之前，需要设置一个zipEntry
        //注意这里传入参数为文件在zip压缩包中的路径，所以只需要传入文件名即可
        String relativePath = prefix + src.getName();
        if (src.isDirectory()) {
            relativePath += "/";
        }
        ZipEntry entry = new ZipEntry(relativePath);
        //写到这个zipEntry中，可以理解为一个压缩文件
        zos.putNextEntry(entry);

        try (InputStream is = new FileInputStream(src)){
            if (src.isFile()) {

                byte[] buffer = new byte[1024 << 3];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
            }
            //该文件写入结束
            zos.closeEntry();
        }
    }
    /**
     * 获取指定目录下的所有文件
     * @param filepath 路径
     * @param fileList 空list
     * @return List 返回的文件列表
     * @throws FileNotFoundException 文件找不到的路径错误
     * @throws IOException io错误
     */
    public static List readfile(String filepath, List<String> fileList) throws FileNotFoundException, IOException {
        try {
            File file = new File(PathUtil.filePathFilter(filepath));
            if (!file.isDirectory() && file.getName().endsWith(".vm")) {
                fileList.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                if(filelist != null){
                    for (int i = 0; i < filelist.length; i++) {
                        File readfile = new File(PathUtil.filePathFilter(filepath + "/" + filelist[i]));
                        if (!readfile.isDirectory() && readfile.getName().endsWith(".vm")) {
                            fileList.add(readfile.getAbsolutePath());
                        } else if (readfile.isDirectory()) {
                            readfile(filepath + "/" + filelist[i],fileList);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.error("读取模板列表失败！",e);
        }
        return fileList;
    }

}
