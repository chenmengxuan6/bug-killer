package com.webtest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import com.webtest.core.*;

public class Zip {

	private static final int BUFFER_SIZE = 2 * 1024;

	/**
	 * 23 ѹ����ZIP ����1 24
	 * 
	 * @param srcDir
	 *            ѹ���ļ���·�� 25
	 * @param out
	 *            ѹ���ļ������ 26
	 * @param KeepDirStructure
	 *            �Ƿ���ԭ����Ŀ¼�ṹ,true:����Ŀ¼�ṹ; 27
	 *            false:�����ļ��ܵ�ѹ������Ŀ¼��(ע�⣺������Ŀ¼�ṹ���ܻ����ͬ���ļ�,��ѹ��ʧ��) 28
	 * @throws RuntimeException
	 *             ѹ��ʧ�ܻ��׳�����ʱ�쳣 29
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
			throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
			long end = System.currentTimeMillis();
			System.out.println("ѹ����ɣ���ʱ��" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 56 ѹ����ZIP ����2 57
	 * 
	 * @param srcFiles
	 *            ��Ҫѹ�����ļ��б� 58
	 * @param out
	 *            ѹ���ļ������ 59
	 * @throws RuntimeException
	 *             ѹ��ʧ�ܻ��׳�����ʱ�쳣 60
	 */

	public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			System.out.println("ѹ����ɣ���ʱ��" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 94 �ݹ�ѹ������ 95
	 * 
	 * @param sourceFile
	 *            Դ�ļ� 96
	 * @param zos
	 *            zip����� 97
	 * @param name
	 *            ѹ��������� 98
	 * @param KeepDirStructure
	 *            �Ƿ���ԭ����Ŀ¼�ṹ,true:����Ŀ¼�ṹ; 99
	 *            false:�����ļ��ܵ�ѹ������Ŀ¼��(ע�⣺������Ŀ¼�ṹ���ܻ����ͬ���ļ�,��ѹ��ʧ��) 100
	 * @throws Exception
	 *             101
	 */

	private static void compress(File sourceFile, ZipOutputStream zos, String name,
			boolean KeepDirStructure) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			// ��zip����������һ��zipʵ�壬��������nameΪzipʵ����ļ�������
			zos.putNextEntry(new ZipEntry(name));
			// copy�ļ���zip�������
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// ��Ҫ����ԭ�����ļ��ṹʱ,��Ҫ�Կ��ļ��н��д���
				if (KeepDirStructure) {
					// ���ļ��еĴ���
					zos.putNextEntry(new ZipEntry(name + "/"));
					// û���ļ�������Ҫ�ļ���copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// �ж��Ƿ���Ҫ����ԭ�����ļ��ṹ
					if (KeepDirStructure) {
						// ע�⣺file.getName()ǰ����Ҫ���ϸ��ļ��е����ּ�һб��,
						// ��Ȼ���ѹ�����оͲ��ܱ���ԭ�����ļ��ṹ,���������ļ����ܵ�ѹ������Ŀ¼����
						compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
					} else {
						compress(file, zos, file.getName(), KeepDirStructure);
					}
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		String filePath = "D:\\F study\\�����\\����\\elipse\\TestNGDemo\\test-output\\html\\";
		String zipPath = "D:\\admin\\ng.zip";
		FileOutputStream fos1 = new FileOutputStream(new File(zipPath));
		Zip.toZip(filePath, fos1, true);
		SendFile fj = new SendFile();
		fj.sendmail(zipPath);
	}

}
