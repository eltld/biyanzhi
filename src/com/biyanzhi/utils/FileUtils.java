package com.biyanzhi.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import android.os.Environment;

public class FileUtils {
	/**
	 * 使用当前时间戳拼接一个文件名
	 * 
	 * @param format
	 * @return
	 */
	public static String getFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}

	/**
	 * 获取根目�?
	 */
	public static String getRootDir() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 获取拍照路径
	 */
	public static String getCameraPath() {
		String sdpath = getRootDir();
		File destDir = new File(sdpath + "/biyanzhi/camera");
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
		createNoMediaFile();
		return destDir.getAbsolutePath();

	}

	private static void createNoMediaFile() {
		File file = new File(getRootDir() + "/biyanzhi/.nomedia");
		if (!file.exists()) {// 判断文件是否存在（不存在则创建这个文件）
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 得到绝对路径
	 * 
	 * @param dir
	 * @return
	 */
	public static String getgetAbsoluteDir(String dir) {
		return getRootDir() + dir;

	}
}
