package com.basoft.core.ware.wechat.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * amr是一种音频格式的缩写，很多能录音的手机生成的录音文件均为此格式
 * 
 * @author basoft
 */
public class WeixinFileUtils {
	public static void main(String[] args) {
		File file = new File("E:\\ElBIAAznlfKVg_A7_VFC1poPEutKbKVVaAuWDoH0u1IWjNzKO7aBQpnIY4x6eEyo.amr");
		Long duration = getAmrDuration(file);
		System.out.println("duration=" + duration);
		System.out.println("file.length()=" + file.length());
	}

	/**
	 * 得到amr的时长
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static long getAmrDuration(File file) {
		long duration = 0;
		int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();// 文件的长度
			int pos = 6;// 设置初始位置
			int frameCount = 0;// 初始帧数
			int packedPos = -1;
			/////////////////////////////////////////////////////
			byte[] datas = new byte[1];// 初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			/////////////////////////////////////////////////////
			duration += frameCount * 20;// 帧数*20
			duration = (long) Math.ceil(duration / 1000.0);
		} catch (Exception e) {
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return duration;
	}
}
