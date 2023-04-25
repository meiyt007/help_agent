package com.zfsoft.platform.utils.fileUtil;


import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;

import java.io.*;
import java.text.DecimalFormat;

/**
 * @Description:  文件管理工具类
 * @Author: wuxx
 * @Date: 2020/4/25 10:11
 **/
public class FileUtil {
	/**
	 * 文件拷贝
	 */
	public static int copyStream(InputStream in, OutputStream out) throws IOException {
		int result = 0;

		byte[] buf = new byte[4096];
		int numRead;
		while ((numRead = in.read(buf)) != -1) {
			out.write(buf, 0, numRead);
			result += numRead;
		}
		out.flush();
		return result;
	}

	public static byte[] getStreamBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyStream(in, out);
		return out.toByteArray();
	}

	/**
	 * 创建目录
	 */
	public static void createDir(String dirName) {
		String[] dirList = dirName.split("[//\\\\]");
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < dirList.length; i++) {
			tmp.append(dirList[i] + "/");
			File fp = new File(tmp.toString());
			if ((!fp.isDirectory()) && (!fp.isFile())) {
				fp.mkdir();
			}
		}
	}

	/**
	 * 删除目录
	 */
	public static void deleteDir(String dirName) {
		File fp = new File(dirName);
		File[] aa = fp.listFiles();
		for (int i = 0; i < aa.length; i++) {
			if (aa[i].isFile()) {
				aa[i].delete();
			} else if (aa[i].isDirectory()) {
				deleteDir(aa[i].getPath());
			}
		}
		fp.delete();
	}

	/**
	 * 删除文件
	 */
	public static void delFile(String fileName) {
		File fp = new File(fileName);
		if (fp.isFile()){
			fp.delete();
		}
	}

	/**
	 * 批量删除文件
	 */
	public static void delFile(String[] fileName) {
		if (fileName == null) {
			return;
		}
		for (int i = 0; i < fileName.length; i++){
			delFile(fileName[i]);
		}
	}

	public static int copyFile(String fromFile, String toFile) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(fromFile);
		File toF = new File(toFile);
		File parentFile = toF.getParentFile();
		// 当文件夹不存在时，自行创建
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		OutputStream os = new FileOutputStream(toFile);
		int ret = copyStream(is, os);
		is.close();
		os.close();
		return ret;
	}

	public static int copyFile(InputStream is, String toFile) throws FileNotFoundException, IOException {
		File toF = new File(toFile);
		File parentFile = toF.getParentFile();
		// 当文件夹不存在时，自行创建
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		OutputStream os = new FileOutputStream(toFile);
		int ret = copyStream(is, os);
		is.close();
		os.close();
		return ret;
	}

	public static int copyToFile(InputStream in, String toFile) throws FileNotFoundException, IOException {
		OutputStream os = new FileOutputStream(toFile);
		int ret = copyStream(in, os);
		os.close();
		return ret;
	}

	/**
	 * 计算文件大小
	 */
	static String convertFileSize(long size) {
		int divisor = 1;
		String unit = "bytes";
		long mb = 1048576L;
		long kb = 1024L;
		if (size >= mb) {
			divisor = 1048576;
			unit = "MB";
		} else if (size >= kb) {
			divisor = 1024;
			unit = "KB";
		}
		if (divisor == 1){
			return size / divisor + " " + unit;
		}
		String aftercomma = String.valueOf(100L * (size % divisor) / divisor);
		if (aftercomma.length() == 1){
			aftercomma = "0" + aftercomma;
		}
		return size / divisor + "." + aftercomma + " " + unit;
	}

	public static String fileToString(String filePath, String fileName) throws Exception {
		String fileAddress = filePath + fileName;
		File dirFile = new File(fileAddress);
		if (!dirFile.exists()) {
			return "NO";
		}
		String line;
		FileInputStream in = new FileInputStream(dirFile);
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		line = reader.readLine();
		while (line != null) {
			buffer.append(line);
			line = reader.readLine();
		}
		in.close();
		return buffer.toString();
	}

	/**
	 * @description 根据路径和name 获取项目根目录
	 * @author wuxx
	 * @date 20200309
	 */
	public static String getFileName(String path, String name) {
		if (StrUtil.isEmpty(path) && StrUtil.isEmpty(name)) return "";
		if (StrUtil.isEmpty(path)) {
			return name;
		}
		if (StrUtil.isEmpty(name)) {
			return path;
		}
		if (!path.endsWith("/") && !name.startsWith("/")) {
			path += "/";
		}
		return path + name;
	}

	/**
	 * 根据上传方式获取文件流信息
	 * @author luzw
	 * @date 2018年9月25日
	 * @param fileDir 文件目录
	 * @param fileName 文件名
	 * @return
	 * @throws Exception
	 */
	public static InputStream getInputStreamByUploadType(String fileDir, String fileName,String fastDFSUploadUrl) throws Exception{
		InputStream ins = null;
		if (BaseStaticParameter.UPLOAD_TYPE.FTP.equals(FileUploadParam.uploadType())) {
			//FTP中的文件
			ins = FtpUtil.getFileInputInputStrem(FileUploadParam.uploadFtpIp(),
					Integer.parseInt(FileUploadParam.uploadFtpPort()),
					FileUploadParam.uploadFtpUsername(), FileUploadParam.uploadFtpPassword(), fileDir, fileName);
		} else if (BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType())) {
			//文件服务器中的文件
			if(StrUtil.isNotEmpty(fastDFSUploadUrl)){
				ins = FastDFSUtil.getFastDFSFileStream(fastDFSUploadUrl);
			}else{
				//磁盘中的文件
				ins = new FileInputStream(new File(fileDir + fileName));
			}
		} else {
			//磁盘中的文件
			ins = new FileInputStream(new File(fileDir + fileName));
		}
		return ins;
	}

	/**
	 * 根据上传方式删除文件
	 * @author luzw
	 * @date 2018年9月27日
	 * @param fileDir 文件目录
	 * @param fileName 文件名
	 * @return
	 */
	public static boolean deleteFileByUploadType(String fileDir, String fileName,String fastDFSUploadUrl) {
		boolean flag = false;
		if (BaseStaticParameter.UPLOAD_TYPE.FTP.equals(FileUploadParam.uploadType())) {
			//删除文件
			flag = FtpUtil.deleteFile(FileUploadParam.uploadFtpIp(),
					Integer.parseInt(FileUploadParam.uploadFtpPort()),
					FileUploadParam.uploadFtpUsername(), FileUploadParam.uploadFtpPassword(), fileDir, fileName);
		} else if (BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType())) {
			//删除文件
			if(StrUtil.isNotEmpty(fastDFSUploadUrl)){
				FastDFSUtil.deleteFile(fastDFSUploadUrl);
				flag = true;
			}
		} else {
			File tempFile = new File(FileUploadParam.uploadPath() + "/" +fileDir, fileName);
			flag = tempFile.delete();
		}
		return flag;
	}

	/**
	 * @description:  转换文件大小
	 * @param fileS 文件的大小
	 * @author: wuxx
	 * @Date: 2020/10/24 14:00
	 **/
	public static String formatFileSize(long fileS) {
		if(0L==fileS){
			return "";
		}
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}
}
