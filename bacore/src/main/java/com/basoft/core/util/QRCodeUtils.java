package com.basoft.core.util;
  
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

  
/**
 * Base google zxing
 * 
 * @author mentor
 * @version 1.0
 */
public class QRCodeUtils {  
   
    /**
     * 生成图像[写入OutputStream] 
     * 
     * @param content QRCode content
     * @param width 图像宽度  
     * @param height 图像高度  
     * @param imgType 图片类型
     * 
     * @throws WriterException
     * @throws IOException
     */
	@Test
	public static void generateImgQRCode(String content, int width, int height, String imgType, OutputStream stream)
			throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// 生成矩阵
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

		MatrixToImageWriter.writeToStream(bitMatrix, imgType, stream);
	}
    
    /**
     * 生成图像[file]
     * 
     * @param content QRCode content
     * @param width 图像宽度  
     * @param height 图像高度  
     * @param imgType 图片类型
     */
	
	@Test@SuppressWarnings("deprecation")
	public static File generateImgQRCodeFile(String content, int width, int height, String imgType)
			throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// 生成矩阵
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

		File file = new File("");
		MatrixToImageWriter.writeToFile(bitMatrix, imgType, file);
		return file;
	}
	
    /**
     * 生成图像【byte数组】
     * 
     * @param content QRCode content
     * @param width 图像宽度  
     * @param height 图像高度  
     * @param imgType 图片类型
     */
	@Test
	public static byte[] generateImgQRCodeByte(String content, int width, int height, String imgType)
			throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// 生成矩阵
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage bi =  MatrixToImageWriter.toBufferedImage(bitMatrix);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);
		byte[] b = baos.toByteArray();
		
		return b;
	}
}  