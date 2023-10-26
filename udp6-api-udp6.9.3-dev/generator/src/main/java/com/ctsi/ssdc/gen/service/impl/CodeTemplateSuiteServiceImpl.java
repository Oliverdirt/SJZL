package com.ctsi.ssdc.gen.service.impl;


import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.gen.domain.CodeTemplateSuite;
import com.ctsi.ssdc.gen.domain.CodeTemplateSuiteExample;
import com.ctsi.ssdc.gen.exception.BusinessException;
import com.ctsi.ssdc.gen.repository.CodeTemplateSuiteRepository;
import com.ctsi.ssdc.gen.service.CodeTemplateSuiteService;
import com.ctsi.ssdc.gen.util.FileUtil;
import com.ctsi.ssdc.poi.excel.util.PathUtil;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Status;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Service Implementation for managing CodeTemplateSuite.
 *
 * @author ctsi-biyi-generator
 */
@Service
public class CodeTemplateSuiteServiceImpl
        extends StrengthenBaseServiceImpl<CodeTemplateSuiteRepository, CodeTemplateSuite, Long, CodeTemplateSuiteExample>
        implements CodeTemplateSuiteService {
    private final Logger log = LoggerFactory.getLogger(CodeTemplateSuiteServiceImpl.class);

    private static final String TEMPLATEDIR = "/template/";

    @Value("${ctsi.upload.uploadPath}")
    private String uploadDir;

    @Autowired
    CodeTemplateSuiteRepository codeTemplateSuiteRepository;

    @Override
    public CodeTemplateSuite insert(CodeTemplateSuite codeTemplateSuite, MultipartFile multipartFile) {
        //查询对应的模板名称及版本是否已存在
        CodeTemplateSuiteExample codeTemplateSuiteExample = new CodeTemplateSuiteExample();
        codeTemplateSuiteExample.setSuiteCode((StringCriteria) new StringCriteria().setEquals(codeTemplateSuite.getSuiteCode()));
        codeTemplateSuiteExample.setSuiteVersion((StringCriteria) new StringCriteria().setEquals(codeTemplateSuite.getSuiteVersion()));
        codeTemplateSuiteExample.buildCriteria();
        List<CodeTemplateSuite> query = super.r.selectByExample(codeTemplateSuiteExample);
        if (0 != query.size()) {
            log.error("模板套件版本已存在!");
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, "模板版本已存在", "模板版本已存在", null);
        }
        saveSuiteFile(codeTemplateSuite, multipartFile);
        return super.insert(codeTemplateSuite);
    }

    @Override
    public CodeTemplateSuite update(CodeTemplateSuite codeTemplateSuite, MultipartFile multipartFile) {
        if (multipartFile != null) {
            //删除模板文件
            FileUtil.deleteDir(codeTemplateSuite.getSuitePath());
            //保存新文件
            saveSuiteFile(codeTemplateSuite, multipartFile);
        }
        return super.update(codeTemplateSuite);
    }

    @Override
    public void delete(Long p) {
        //删除模板文件
        CodeTemplateSuite codeTemplateSuite = super.findOne(p);
        FileUtil.deleteDir(codeTemplateSuite.getSuitePath());
        super.delete(p);
    }

    /**
     * 保存模板文件
     *
     * @param codeTemplateSuite
     * @param multipartFile
     */
    private void saveSuiteFile(CodeTemplateSuite codeTemplateSuite, MultipartFile multipartFile) {
        //文件类型判断
        String originalFileName = multipartFile.getOriginalFilename();
        if (!originalFileName.matches(".*.zip")) {
            log.error("模板文件类型错误!");
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, "模板文件类型错误！", "模板文件类型错误，请上传zip压缩文件", null);
        }
        String fileName = codeTemplateSuite.getSuiteCode() + "-" + codeTemplateSuite.getSuiteVersion();
        File projectPath = null;
        try {
            if (null != uploadDir) {
                projectPath = new File(PathUtil.filePathFilter(uploadDir));
            } else {
                projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
            }
        } catch (FileNotFoundException e) {
            log.error("获取根目录异常" + e);
            throw new BusinessException("获取根目录异常！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "获取根目录异常！", null);
        }
        if (!projectPath.exists()) {
            boolean mkdirs = projectPath.mkdirs();
            if (!mkdirs) {
                throw new BusinessException("创建目录失败！", Status.INTERNAL_SERVER_ERROR, "创建目录失败", "创建目录失败", null);
            }
        }
        String suitePath = projectPath + TEMPLATEDIR + fileName;
        File suiteDir = new File(PathUtil.filePathFilter(suitePath));
        if (!suiteDir.exists()) {
            boolean mkdirs = suiteDir.mkdirs();
            if (!mkdirs) {
                throw new BusinessException("创建目录失败！", Status.INTERNAL_SERVER_ERROR, "创建目录失败", "创建目录失败", null);
            }
        }
        //保存zip包
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(PathUtil.filePathFilter(suitePath), PathUtil.filePathFilter(fileName + ".zip")));
        } catch (IOException e) {
            log.error("保存zip模板文件异常" + e);
            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "保存zip模板文件异常！", null);
        }

        // 解压zip包
        try {
            ectract(suitePath + File.separator + fileName + ".zip", suitePath + File.separator);
        } catch (Exception e) {
            log.error("解压zip模板文件异常" + e);
            throw new BusinessException("解压模板出错！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "解压zip模板文件异常！", null);
        }
//        try (FileInputStream fin = new FileInputStream(suitePath+ File.separator+fileName+".zip");
//             BufferedInputStream bis = new BufferedInputStream(fin);
//             ZipInputStream zis = new ZipInputStream(bis);
//        ) {
//            ZipEntry entry;
//            while ((entry = zis.getNextEntry()) != null) {
////                write(zis, entry, suitePath);
//            }
//        } catch (IOException e) {
//            log.error("模板文件保存出错", e);
//            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "模板文件保存出错", null);
//        }
        codeTemplateSuite.setSuitePath(suitePath);
    }

//    /**
//     * zip包文件写入指定目录
//     *
//     * @param zis
//     * @param entry
//     * @param suitePath
//     */
//    private void write(ZipInputStream zis, ZipEntry entry, String suitePath) {
//        String entryName = entry.getName();
//        try (FileOutputStream fout = new FileOutputStream(suitePath + File.separator + entryName);
//             BufferedOutputStream bos = new BufferedOutputStream(fout)) {
//            int b = 0;
//            while ((b = zis.read()) != -1) {
//                bos.write(b);
//            }
//            bos.flush();
//        } catch (IOException e) {
//            log.error("模板文件保存出错", e);
//            throw new BusinessException("新增模板出错！", Status.INTERNAL_SERVER_ERROR, e.getMessage(), "模板文件保存出错", null);
//        }
//    }

    // 解压缩文件
    public static ArrayList ectract(String sZipPathFile, String sDestPath) {
        ArrayList<String> allFileName = new ArrayList<String>();
        FileInputStream fins = null;
        ZipInputStream zins = null;
        FileOutputStream fouts = null;
        try {
            fins = new FileInputStream(PathUtil.filePathFilter(sZipPathFile));
            zins = new ZipInputStream(fins);
            ZipEntry ze = null;
            byte[] ch = new byte[256];
            while ((ze = zins.getNextEntry()) != null) {
                File zfile = new File(PathUtil.filePathFilter(sDestPath + ze.getName()));
                File fpath = new File(PathUtil.filePathFilter(zfile.getParentFile().getPath()));
                if (ze.isDirectory()) {
                    if (!zfile.exists()) {
                        boolean mkdirs = zfile.mkdirs();
                        if (!mkdirs) {
                            throw new BusinessException("创建目录失败！", Status.INTERNAL_SERVER_ERROR, "创建目录失败", "创建目录失败", null);
                        }
                    }
                    zins.closeEntry();
                } else {
                    if (!fpath.exists()) {
                        boolean mkdirs = fpath.mkdirs();
                        if (!mkdirs) {
                            throw new BusinessException("创建目录失败！", Status.INTERNAL_SERVER_ERROR, "创建目录失败", "创建目录失败", null);
                        }
                    }
                    fouts = new FileOutputStream(zfile);
                    int i;
                    allFileName.add(zfile.getAbsolutePath());
                    while ((i = zins.read(ch)) != -1) {
                        fouts.write(ch, 0, i);
                    }
                    zins.closeEntry();

                }
            }

        } catch (Exception e) {
            System.err.println("Extract error:" + e.getMessage());
        } finally {
            if (fouts != null) {
                try {
                    fouts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (zins != null) {
                try {
                    zins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fins != null) {
                try {
                    fins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        return allFileName;
    }


    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 清空本地模板
        CodeTemplateSuiteExample codeTemplateSuiteExample = new CodeTemplateSuiteExample();
        codeTemplateSuiteExample.createCriteria().andIdIn(Arrays.asList(ids));
        List<CodeTemplateSuite> codeTemplateSuites = codeTemplateSuiteRepository.selectByExample(codeTemplateSuiteExample);
        codeTemplateSuites.stream().forEach((t)->{
            FileUtil.deleteDir(t.getSuitePath());
        });
        // 清理数据库
        codeTemplateSuiteRepository.deleteByIds(delList);
    }
}
