package com.ctsi.ssdc.util;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * 结合前端webuploader插件实现文件断点续传功能
 * @author lym
 *
 */

public class WebUploaderUtil {
	/**
	 * 保存分片
	 * @param request 请求
	 * @param response 响应
	 * @param savePath 保存路径
	 * @throws ServletException 
	 * @throws IOException
	 */
	//@RequestMapping(value="/saveChunk")
	

	public static void saveChunk(HttpServletRequest request, HttpServletResponse response, String savePath)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());

		// 1.创建DiskFileItemFactory对象，配置缓存用
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

		// 2. 创建 ServletFileUpload对象
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

		// 3. 设置文件名称编码
		servletFileUpload.setHeaderEncoding("utf-8");

		// 4. 开始解析文件
		// 文件md5获取的字符串
		String fileMd5 = null;
		// 文件的索引
		String chunk = null;
		try {
			
			StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request; // 遍历普通参数（即formData的fileName和fileSize） Enumeration<String> names = req.getParameterNames(); while (names.hasMoreElements()) { String key = names.nextElement(); String val = req.getParameter(key); System.out.println("FormField：k=" + key + "v=" + val); }
			// 遍历普通参数（即formData的fileName和fileSize） 
			Enumeration<String> names = req.getParameterNames(); 
			while (names.hasMoreElements()) { 
				String key = names.nextElement(); 
				String val = req.getParameter(key); 
				if ("info".equals(key)) {
					System.out.println("info:" + val);
				}
				if ("fileMd5".equals(key)) {
					fileMd5 = val;
					System.out.println("fileMd5:" + val);
				}
				if ("chunk".equals(key)) {
					chunk = val;
					System.out.println("chunk:" + val);
				} 
			}
			Iterator<String> iterator = req.getFileNames(); 
			while (iterator.hasNext()) { 
			
				File file = new File(savePath + "/" + fileMd5);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 保存文件
				File chunkFile = new File(savePath + "/" + fileMd5 + "/" + chunk);
				MultipartFile multipartFile = req.getFile(iterator.next());
				FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), chunkFile);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 检查分片是否已上传，合并分片
	 * @param request 请求
	 * @param response 响应
	 * @param savePath 保存路径
	 * @throws ServletException
	 * @throws IOException
	 */
	//@RequestMapping(value="/checkOrMergeChunk")
	

//	public static void checkOrMergeChunk(HttpServletRequest request, HttpServletResponse response, String savePath)
//			throws ServletException, IOException {
//		response.setContentType("text/html;charset=utf-8");
//		String action = request.getParameter("action");
//		if ("mergeChunks".equals(action)) {
//			// 获得需要合并的目录
//			String fileMd5 = request.getParameter("fileMd5");
//			String fileName = request.getParameter("fileName");
//
//			// 读取目录所有文件
//			File f = new File(savePath + "/" + fileMd5);
//			File[] fileArray = f.listFiles(new FileFilter() {
//
//				// 排除目录，只要文件
//				@Override
//				public boolean accept(File pathname) {
//					if (pathname.isDirectory()) {
//						return false;
//					}
//					return true;
//				}
//			});
//
//			// 转成集合，便于排序
//			List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
//			// 从小到大排序
//			Collections.sort(fileList, new Comparator<File>() {
//
//				@Override
//				public int compare(File o1, File o2) {
//					if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
//						return -1;
//					}
//					return 1;
//				}
//
//			});
//
//			//获取文件后缀名
//			int lastIndex = fileName.lastIndexOf(".");
//			String suffix = "";
//			if (lastIndex > -1 && lastIndex < (fileName.length() - 1)) {
//				suffix = "." + fileName.substring(lastIndex + 1);
//			}
//
//			// 新建保存文件
//			File outputFile = new File(savePath + "/" + fileMd5 + suffix);
//
//			// 创建文件
//			outputFile.createNewFile();
//
//			// 输出流
//			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
//			FileChannel outChannel = fileOutputStream.getChannel();
//
//			// 合并
//			FileChannel inChannel;
//			for (File file : fileList) {
//				inChannel = new FileInputStream(file).getChannel();
//				inChannel.transferTo(0, inChannel.size(), outChannel);
//				inChannel.close();
//
//				// 删除分片
//				file.delete();
//			}
//
//			// 关闭流
//			fileOutputStream.close();
//			outChannel.close();
//
//			// 清除文件加
//			File tempFile = new File(savePath + "/" + fileMd5);
//			if (tempFile.isDirectory() && tempFile.exists()) {
//				tempFile.delete();
//			}
//
//			System.out.println("合并文件成功");
//			response.getWriter().write(fileMd5+suffix);
//
//		} else if ("checkChunk".equals(action)) {
//			// 校验文件是否已经上传并返回结果给前端
//
//			// 文件唯一表示
//			String fileMd5 = request.getParameter("fileMd5");
//			// 当前分块下标
//			String chunk = request.getParameter("chunk");
//			// 当前分块大小
//			String chunkSize = request.getParameter("chunkSize");
//
//			// 找到分块文件
//			File checkFile = new File(savePath + "/" + fileMd5 + "/" + chunk);
//
//			// 检查文件是否存在，且大小一致
//			if (checkFile.exists() && checkFile.length() == Integer.parseInt((chunkSize))) {
//				response.getWriter().write("{\"ifExist\":1}");
//			} else {
//				response.getWriter().write("{\"ifExist\":0}");
//			}
//		}
//	}
	
}
