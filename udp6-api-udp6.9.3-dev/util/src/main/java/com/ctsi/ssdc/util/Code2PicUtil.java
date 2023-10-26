package com.ctsi.ssdc.util;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成与解析二维码
 * @author ctsi
 *
 */
public class Code2PicUtil {
	/**
	 * 将字符串转化成二维码
	 * @param content 内容
	 * @return  二维码
	 */
	

	public static BufferedImage generateCode2Pic(String content) {
		try {
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

			Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints);

			BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);
			return bi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取二维码内容
	 * @param filePath 二维码图片路径
	 * @return 二维码内容
	 */
	

	public static String generateStringFromCode2Pic(String filePath) {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			File file = new File(filePath);
			BufferedImage image = ImageIO.read(file);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = formatReader.decode(binaryBitmap, hints);

			System.out.println("result = " + result.toString());
			System.out.println("resultFormat = " + result.getBarcodeFormat());
			System.out.println("resultText = " + result.getText());
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
