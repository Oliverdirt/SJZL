package com.ctsi.ssdc.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * 文件上传与下载
 * @author ctsi
 *
 */
public class FileLoadUtil {
	/**
	 * 上传
	 * @param multipartFile 文件
	 * @param savePath 保存路径
	 * @return 上传结果，成功则返回文件名，失败则返回失败信息
	 */
	

	public static AjaxBackData upload(MultipartFile multipartFile, String savePath) {
		AjaxBackData ajax = new AjaxBackData(true, "");
		File file = new File(savePath); 
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			//获取文件后缀名
			String originalFileName = multipartFile.getOriginalFilename();
			int lastIndex = originalFileName.lastIndexOf(".");
			String suffix = "";
			if (lastIndex > -1 && lastIndex < (originalFileName.length() - 1)) {
				suffix = "." + originalFileName.substring(lastIndex + 1);
			}
			//生成文件名
			String fileName = File.separator + PrimaryKeyBuild.randomFileNameWithDoubleLongNum() + suffix;
			File ftmp = new File(savePath + fileName);
			if (!ftmp.exists()) {
				ftmp.createNewFile();
			}
			//转存文件
			multipartFile.transferTo(ftmp.getAbsoluteFile());
			//返回文件名
			ajax = new AjaxBackData(true, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			ajax = new AjaxBackData(false, "发生异常：" + e.getMessage());
		}

		return ajax;
	}
	
	/**
	 * 下载
	 * @param response 响应
	 * @param savePath 保存路径
	 * @param fileName 文件名
	 */
	
	public static void download(HttpServletResponse response, String savePath, String fileName) {
		try {
			//校验文件名
			if (fileName == null || "".equals(fileName.trim()) || fileName.indexOf("..") > -1) {
				return;
			}
			//输出文件流
			OutputStream os = response.getOutputStream();
			
			FileInputStream in = new FileInputStream(savePath + File.separator + fileName);

			byte[] b = new byte[1024];
			while (in.read(b) != -1) {
				os.write(b);
			}
			in.close();
			os.flush();
			os.close();
		} catch (Exception exception) {
			exception.printStackTrace();
			return;
		}
	}

}
