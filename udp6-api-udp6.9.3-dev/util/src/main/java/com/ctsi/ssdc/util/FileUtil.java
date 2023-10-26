package com.ctsi.ssdc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static com.ctsi.ssdc.consts.ComponentConstant.UTIL;

/**
 * 文件处理工具类
 * @author ctsi
 *
 */
public class FileUtil {
	
	/**
	 * 查找文件父目录
	 * @param f 子文件
	 * @return 文件的父目录
	 */
	

	public static File parent(File f) {
		String dirname = f.getParent();
		if (dirname == null) {
			if (f.isAbsolute()) {
				return new File(File.separator);
			} else {
				return new File(System.getProperty("user.dir"));
			}
		}
		return new File(dirname);
	}
	
	/**
	 * 复制文件，如果是目录，则递归复制其下的子文件
	 * @param src 源文件
	 * @param dest 目标文件
	 */
	

	public static void copy(File src, File dest) {
		try {
			FileInputStream source = null;
			FileOutputStream destination = null;
			byte[] buffer;
			int bytes_read;

			// Make sure the specified source exists and is readable.
			// System.out.println("--------------------enter copy progress ------------------");
			if (!src.exists()) {
				throw new IOException("source not found: " + src);
			}
			if (!src.canRead()) {
				throw new IOException("source is unreadable: " + src);
			}
			if (src.isFile()) {
				if (!dest.exists()) {
					File parentdir = parent(dest);
					if (!parentdir.exists()) {
						parentdir.mkdir();
					}
				} else if (dest.isDirectory()) {
					dest = new File(dest + File.separator + src);
				}
			} else if (src.isDirectory()) {
				if (dest.isFile()) {
					throw new IOException("cannot copy directory " + src + " to file " + dest);
				}
				if (!dest.exists()) {
					dest.mkdir();
				}
			}

			// The following line requires that the file already
			// exists!! Thanks to Scott Downey (downey@telestream.com)
			// for pointing this out. Someday, maybe I'll find out
			// why java.io.File.canWrite() behaves like this. Is it
			// intentional for some odd reason?

			// If we've gotten this far everything is OK and we can copy.
			if (src.isFile()) {
				try {
					source = new FileInputStream(src);
					destination = new FileOutputStream(dest);
					buffer = new byte[1024];
					while (true) {
						bytes_read = source.read(buffer);
						if (bytes_read == -1) {
							break;
						}
						destination.write(buffer, 0, bytes_read);
					}
				} finally {
					if (source != null) {
						try {
							source.close();
						} catch (IOException e) {
							;
						}
					}
					if (destination != null) {
						try {
							destination.close();
						} catch (IOException e) {
							;
						}
					}
				}
			} else if (src.isDirectory()) {
				String targetfile;
				String target;
				String targetdest;
				String[] files = src.list();

				for (int i = 0; i < files.length; i++) {
					targetfile = files[i];
					target = src + File.separator + targetfile;
					targetdest = dest + File.separator + targetfile;

					if ((new File(target)).isDirectory()) {
						copy(new File(target), new File(targetdest));
					} else {

						try {
							source = new FileInputStream(target);
							destination = new FileOutputStream(targetdest);
							buffer = new byte[1024];

							while (true) {
								bytes_read = source.read(buffer);
								if (bytes_read == -1) {
									break;
								}
								destination.write(buffer, 0, bytes_read);
							}
						} finally {
							if (source != null) {
								try {
									source.close();
								} catch (IOException e) {
									;
								}
							}
							if (destination != null) {
								try {
									destination.close();
								} catch (IOException e) {
									;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	
	/**
	 * 获取文件夹下文件列表
	 * @param fileName 文件夹名
	 * @return 文件夹下的文件列表
	 */
	

	public static File[] getAllFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			return files;
		}
		
		return null;
	}
	
	/**
	 * 通过流方式复制文件
	 * @param src 源文件路径
	 * @param dest 目标文件路径
	 * @return 是否成功
	 */
	

	public static boolean copyFile(String src, String dest) {
		File srcFile = new File(src);
		File destFile = new File(dest);
		return copyFile(srcFile, destFile);
	}

	/**
	 * 通过流方式复制文件
	 * @param src 源文件
	 * @param dest 目标文件
	 * @return 是否成功
	 */
	

	public static boolean copyFile(File src, File dest) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			FileInputStream fis = new FileInputStream(src);
			bis = new BufferedInputStream(fis);

			FileOutputStream fos = new FileOutputStream(dest);
			bos = new BufferedOutputStream(fos);
			
			//每次读取2K数据
			byte[] b = new byte[1024 * 2];
			int length;
			while ((length = bis.read(b)) != -1) {
				bos.write(b, 0, length);
			}
			bos.flush();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (bos != null) {
					bos.close();
					bos = null;
				}
				if (bis != null) {
					bis.close();
					bis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除文件
	 * @param fileName 文件路径
	 * @return 是否删除成功,仅在删除成功时返回true，其他情况均返回false
	 */
	

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return deleteFile(file);
	}
	
	/**
	 * 删除文件
	 * @param file 待删除文件
	 * @return 是否删除成功，仅在删除成功时返回true，其他情况均返回false
	 */
	

	public static boolean deleteFile(File file) {
		return file.delete();
	}
	
	/**
	 * 移动文件，先将原文件复制到目标文件，再删除原文件
	 * @param src 源文件路径
	 * @param dest 目的文件路径
	 * @return 是否成功，仅在复制源文件和删除源文件都成功的情况下返回true,其他情况返回false
	 */
	

	public static boolean moveFile(String src, String dest) {
		File srcFile = new File(src);
		File destFile = new File(dest);
		return moveFile(srcFile, destFile);
	}
	
	/**
	 * 移动文件，先将源文件复制到目标文件，再删除原文件
	 * @param src 源文件
	 * @param dest 目标文件
	 * @return 是否成功，仅在复制源文件和删除源文件都成功的情况下返回true,其他情况返回false
	 */
	

	public static boolean moveFile(File src, File dest) {
		boolean result = copyFile(src, dest);
		boolean result2 = deleteFile(src);
		if(result && result2) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 复制文件夹,及其下的所有文件夹与文件
	 * @param srcDir 源文件夹
	 * @param destDir 目标文件夹
	 */
	

	public boolean copyDirectory(String srcDir, String destDir) {
		File dir = new File(destDir);
		//目标文件夹不存在则创建
		if (!dir.exists()) {
			dir.mkdirs();
		}

		boolean result = false;
		File srcFile = new File(srcDir);
		File[] files = srcFile.listFiles();
		for (File file : files) {
			// 复制文件
			if (file.isFile()) {
				File sourceFile = file;
				File targetFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getName());
				result = copyFile(sourceFile, targetFile);
			}
			// 复制目录
			if (file.isDirectory()) {
				String src = srcDir + File.separator + file.getName();
				String dest = destDir + File.separator + file.getName();
				result = copyDirectory(src, dest);
			}
			//复制失败
			if (!result) {
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * 获取扩展名，字符串最后一个"."后的内容
	 * <pre>
	 * FileUtil.getExtension(null)               = null
	 * FileUtil.getExtension("filename")         = null
	 * FileUtil.getExtension("filename.ext")     = "ext"
	 * FileUtil.getExtension("filename.log.ext") = "ext"
	 * </pre>
	 * @param fileName 文件名字符串
	 * @return 扩展名，若不存在则返回null
	 */
	

	public String getExtension(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			int index = fileName.lastIndexOf('.');
			if(index!=-1) {
				String name = fileName.substring(index+1, fileName.length());
				return name;
			}
		}
		
		return null;
	}
	
	/**
	 * 去除文件名的扩展名，只获取文件名。获取字符串最后一个"."前的内容,若不存在"."则返回原文件名字符串
	 * <pre>
	 * FileUtil.trimExtension(null)           = null
	 * FileUtil.trimExtension("filename.ext") = "filename"
	 * FileUtil.trimExtension("filename")     = "filename"
	 * </pre>
	 * @param fileName 文件名字符串
	 * @return 去除扩展名的文件名
	 */
	

	public String trimExtension(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			int index = fileName.lastIndexOf('.');
			if(index!=-1) {
				String name = fileName.substring(0, index);
				return name;
			}else {
				return fileName;
			}
		}		
		return null;
	}
	
	/**
	 * 创建目录
	 * @param dir 目录路径
	 * @return 是否成功
	 */
	

	public boolean createDirectory(String dir) {
		File dirFile = new File(dir);
		return dirFile.mkdirs();
	}
	
	/**
	 * 创建文件
	 * @param fileName 文件路径
	 * @return 是否创建成功
	 */
	

	public boolean createFile(String fileName) {
		File file = new File(fileName);
		File parent = new File(file.getParent());
		//父路径不存在则创建
		if (!parent.exists()) {
			boolean result = createDirectory(parent.getPath());
			//创建失败
			if (!result) {
				return false;
			}
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.exists();
	}

	
}
