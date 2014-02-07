package com.litaook.lottery.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 复制文件<br/>
 * 非调用系统命令方式，是通过nio库读取源文件，然后再写新文件。<br/>
 * 返回复制文件的大小(bytes)
 * 
 * @author litao
 * 
 */
public class CopyFile {
	static Logger log = LogManager.getLogger("CopyFile");
	
	/**
	 * 复制文件
	 * @param srcFile
	 * @param dstDir
	 * @param newFileName
	 * @return
	 * @throws IOException
	 */
	public static long copy(String srcFile, String dstFile)
			throws IOException {
		log.debug("copy() - 复制文件 - start");
		long copySizes = 0;

		FileChannel fcin = null;
		FileChannel fcout = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			log.debug("读取文件...");
			fis = new FileInputStream(srcFile);
			fcin = fis.getChannel();
			fos = new FileOutputStream(dstFile);
			fcout = fos.getChannel();
			long size = fcin.size();
			log.debug("写新文件...");
			fcin.transferTo(0, fcin.size(), fcout);
			copySizes = size;
			log.debug(new StringBuffer("写入 ").append(copySizes).append(" bytes."));

		} finally {
			try {
				fcout.close();
				fos.close();
				fcin.close();
				fis.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
		log.debug("copy() - end");
		return copySizes;
	}

	public static void main(String[] args) {
		
		String srcFile = "/Users/litao/Downloads/swf_flv_player.dmg";
		String dstFile = "/Users/litao/Desktop/aa.dmg";

		log.entry();
		
		long rest = 0L;
		try {
			rest = CopyFile.copy(srcFile, dstFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.exit();
		System.out.println("result : " + rest);
	}

}
