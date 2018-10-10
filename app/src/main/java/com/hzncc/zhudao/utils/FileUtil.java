package com.hzncc.zhudao.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateFormat;

public class FileUtil {
	public static final String JPG_SUFFIX = ".jpg";// JPG图片的后缀

	/**
	 * 从Assets中读取txt文件内容
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            要读取的文件名称
	 * @return 返回一个字符串
	 */
	public static String getTxtFromAssets(Context context, String fileName) {
		String Result = null;
		try {
			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";

			while ((line = bufReader.readLine()) != null)
				Result += line;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result;
	}

	/**
	 * 将一个byte数组保存位一个本地文件
	 * 
	 * @param path
	 *            文件保存使用的名称
	 * @param b
	 *            要保存的byte数组
	 * @return 返回保存好的文件对象 如果保存失败file为null
	 */
	public static File saveBytesToFile(String path, byte[] b) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(path);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 根据路径获取文件，并自动检查文件是否存在
	 * 
	 * @param path
	 *            要得到文件对象的路径
	 * @return 返回文件对象file
	 */
	public static File getNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			initPath(path);
		}
		return file;
	}

	/**
	 * 创建一个新的文件夹
	 * 
	 * @param path
	 *            要创建的文件路径
	 * @return 判断是否创建成功
	 */
	public static boolean initPath(String path) {
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.exists();
	}

	/**
	 * 获取当前系统时间并拼接之后作为文件名使用
	 * 
	 * @param suffix
	 *            需要拼接到时间字符串后面的字符串
	 * @return 返回拼接好的字符串
	 */
	public static String getDataToFileName(String suffix) {
		String name = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ suffix;
		return name;
	}

	/**
	 * 删除成功
	 */
	public static final int DEL_SUCCESS = 1;
	/**
	 * 删除失败
	 */
	public static final int DEL_FAIL = -1;
	/**
	 * 文件不存在
	 */
	public static final int FILE_IS_NULL = 0;

	/**
	 * 删除某个文件
	 * 
	 * @param path
	 *            要删除的文件路径
	 * @return 0 代表文件不存在 1 代表删除成功 -1 代表删除失败
	 */
	public static int delFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			return file.delete() ? DEL_SUCCESS : DEL_FAIL;
		} else {
			return FILE_IS_NULL;
		}
	}
}
