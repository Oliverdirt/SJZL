package com.ctsi.ssdc.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * 图像工具类
 * @author ctsi
 *
 */
public class ImageUtil {
	// 图片宽度的一般
	private static int IMAGE_WIDTH = 50;
	private static int IMAGE_HEIGHT = 50;
	private static int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
	private static int FRAME_WIDTH = 2;

	// 二维码写码器
	private static MultiFormatWriter mutiWriter = new MultiFormatWriter();

	/**
	 * 
	 * @param content
	 *            二维码显示的文本
	 * @param width
	 *            二维码的宽度
	 * @param height
	 *            二维码的高度
	 * @param srcImagePath
	 *            中间嵌套的图片
	 * @param destImagePath
	 *            二维码生成的地址
	 */
	

	public static void encode(String content, int width, int height, String srcImagePath, String destImagePath) {
		try {
			// ImageIO.write 参数 1、BufferedImage 2、输出的格式 3、输出的文件
			ImageIO.write(genBarcode(content, width, height, srcImagePath), "png", new File(destImagePath));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到BufferedImage
	 * 
	 * @param content
	 *            二维码显示的文本
	 * @param width
	 *            二维码的宽度
	 * @param height
	 *            二维码的高度
	 * @param srcImagePath
	 *            中间嵌套的图片
	 * @return 图像BufferedImage
	 * @throws WriterException
	 * @throws IOException
	 */
	

	public static BufferedImage genBarcode(String content, int width, int height, String srcImagePath)
			throws WriterException, IOException {
		IMAGE_WIDTH = 50;
		IMAGE_HEIGHT = 50;
		IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
		// 读取源图像
		BufferedImage scaleImage = null;
		try {
			scaleImage = scale(srcImagePath, IMAGE_WIDTH, IMAGE_HEIGHT, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int[][] srcPixels = new int[0][0];
		if (scaleImage != null) {
			srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
			for (int i = 0; i < scaleImage.getWidth(); i++) {
				for (int j = 0; j < scaleImage.getHeight(); j++) {
					srcPixels[i][j] = scaleImage.getRGB(i, j);
				}
			}
		} else {
			IMAGE_WIDTH = 0;
			IMAGE_HEIGHT = 0;
			IMAGE_HALF_WIDTH = 0;
			FRAME_WIDTH = 0;
		}

		Hashtable<EncodeHintType, Object> hint = new Hashtable<EncodeHintType, Object>();
		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 生成二维码
		BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hint);

		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int[] pixels = new int[width * height];

		// System.out.println(matrix.getHeight());
		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 读取图片
				if (x > halfW - IMAGE_HALF_WIDTH && x < halfW + IMAGE_HALF_WIDTH && y > halfH - IMAGE_HALF_WIDTH
						&& y < halfH + IMAGE_HALF_WIDTH) {
					pixels[y * width + x] = srcPixels[x - halfW + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
				} else if (validate(x, y, halfW, halfH)) {
					pixels[y * width + x] = 0xfffffff;
				} else {
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * width + x] = matrix.get(x, y) ? 0xff000000 : 0xfffffff;
				}
			}
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, width, height, pixels);

		return image;
	}
	
	private static boolean validate(int x, int y, int halfW, int halfH) {
		// 在图片四周形成边框
		boolean flagA = x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH 
				&& x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH 
				&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH 
				&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH;
		boolean flagB = x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH 
				&& x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH 
				&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH
				&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH;
		boolean flagC = x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH 
				&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
				&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH
				&& y < halfH - IMAGE_HALF_WIDTH + FRAME_WIDTH;
		boolean flagD = x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH 
				&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
				&& y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH
				&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH;
		return flagA || flagB || flagC || flagD;	
	}

	/**
	 * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标
	 * 
	 * @param srcImageFile
	 *            源文件地址
	 * @param height
	 *            目标高度
	 * @param width
	 *            目标宽度
	 * @param hasFiller
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 * @throws IOException
	 */
	private static BufferedImage scale(String srcImageFile, int height, int width, boolean hasFiller)
			throws IOException {
		double ratio = 0.0; // 缩放比例
		File file = new File(srcImageFile);

		if (!file.exists()) {
			return null;
		}

		BufferedImage srcImage = ImageIO.read(file);
		Image destImage = srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		// 计算比例
		if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
			if (srcImage.getHeight() > srcImage.getWidth()) {
				ratio = (double) height / srcImage.getHeight();
			} else {
				ratio = (double)width / srcImage.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			destImage = op.filter(srcImage, null);
		}
		if (hasFiller) {// 补白
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if (width == destImage.getWidth(null)) {
				graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			} else {
				graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			}
			graphic.dispose();
			destImage = image;
		}
		return (BufferedImage) destImage;
	}

	/**
	 * 生成验证码图片
	 * @param code 验证码
	 * @return 验证码图片的BufferedImage
	 * @throws IOException
	 */
	

	public static BufferedImage createMatchCode(String code) throws IOException {
		BufferedImage bi = new BufferedImage(40, 16, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.clearRect(0, 0, 16, 40);
		g.setColor(Color.CYAN);
		g.drawString(code, 4, 12);
		g.dispose();
		return bi;
	}
	
	/**
	 * 图片裁剪通用接口
	 * @param src 源
	 * @param dest 目标
	 * @param x 指定的x坐标
	 * @param y 指定的y坐标
	 * @param w 矩形的宽度
	 * @param h 矩形的高度
	 * @throws IOException IO异常
	 */
	

	public static void cutImage(String src, String dest, int x, int y, int w, int h) throws IOException {
		Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		Rectangle rect = new Rectangle(x, y, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "jpg", new File(dest));

	}

	/**
	 * 图片缩放
	 * @param src 源文件
	 * @param dest 目标
	 * @param w 宽
	 * @param h 高
	 * @throws Exception 文件读写异常
	 */
	

	public static void zoomImage(String src, String dest, int w, int h) throws Exception {
		double wr = 0;
		double hr = 0;
		File srcFile = new File(src);
		File destFile = new File(dest);
		BufferedImage bufImg = ImageIO.read(srcFile);
		Image iTemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		wr = w * 1.0 / bufImg.getWidth();
		hr = h * 1.0 / bufImg.getHeight();
		AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
		iTemp = ato.filter(bufImg, null);
		try {
			ImageIO.write((BufferedImage) iTemp, dest.substring(dest.lastIndexOf(".") + 1), destFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	/**
	 * 从url下载图片
	 * @param urlstr 源URL
	 * @param path 保存路径
	 * @return 图片名
	 * @throws Exception
	 */
	

	public static String saveImageFromUrl(String urlstr, String path) throws Exception {
		String file = PrimaryKeyBuild.randomFileNameWithDoubleLongNum() + ".jpg";
		// new一个URL对象
		URL url = new URL(urlstr);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"GET"
		conn.setRequestMethod("GET");
		// 超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		// 通过输入流获取图片数据
		InputStream inStream = conn.getInputStream();
		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		// String path =
		// configFromServer.getWebFileDirectory();//ServletActionContext.getServletContext().getRealPath("/upload");
		File imageFile = new File(path + File.separatorChar + file);
		// 创建输出流
		FileOutputStream outStream = new FileOutputStream(imageFile);
		// 写入数据
		outStream.write(data);
		// 关闭输出流
		outStream.close();

		return file;
	}
	/**
	 * 将流读入byte数组
	 * @param inStream 输入流
	 * @return byte数组
	 * @throws Exception
	 */
	

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

}
