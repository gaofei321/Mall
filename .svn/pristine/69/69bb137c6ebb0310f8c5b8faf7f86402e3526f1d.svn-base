package com.allroot.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.URL;

public class FileUtil {

	public static void saveFileByString(File dest, String text) throws Exception {
		BufferedReader bufferedReader;// 为字符输入流加缓冲
		FileOutputStream fileOutputStream;// 字节输出流
		OutputStreamWriter outputStreamWriter;// 将字节输出流转换成字符输出流

		// StringReader stringReader=new StringReader(text);
		byte[] bytes = text.getBytes("UTF-8");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream, "UTF-8");
		bufferedReader = new BufferedReader(inputStreamReader);
		String s;
		fileOutputStream = new FileOutputStream(dest);
		outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
		while ((s = bufferedReader.readLine()) != null) {
			outputStreamWriter.write(s);
		}

		outputStreamWriter.close();
		fileOutputStream.close();
		bufferedReader.close();
	}

	public static void writeFile(String filePathAndName, String fileContent) throws Exception {
		File f = new File(filePathAndName);
		if (!f.exists()) {
			f.createNewFile();
		}
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
		BufferedWriter writer = new BufferedWriter(write);
		// PrintWriter writer = new PrintWriter(new BufferedWriter(new
		// FileWriter(filePathAndName)));
		// PrintWriter writer = new PrintWriter(new
		// FileWriter(filePathAndName));
		writer.write(fileContent);
		writer.close();
	}
}
