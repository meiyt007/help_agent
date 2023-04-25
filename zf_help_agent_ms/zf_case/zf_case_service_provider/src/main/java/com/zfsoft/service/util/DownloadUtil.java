package com.zfsoft.service.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 下载文件帮助类
 * 
 * @author wangxl
 * @date 2020-10-27
 *
 */
public class DownloadUtil {

	/**
	 * 根据文件路径，名称下载文件
	 * 
	 * @param displayFileName
	 *            文件名称
	 * @param filePath
	 *            文件路径
	 * @param request
	 *            http对象
	 * @throws Exception
	 */
	public static void downloadFile(String displayFileName, String filePath, String fileName,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userAgent = request.getHeader("User-Agent"); 
		// 针对IE或者以IE为内核的浏览器：  
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
        	displayFileName = URLEncoder.encode(displayFileName, "UTF-8");  
        } else {  
            // 非IE浏览器的处理：  
        	displayFileName = new String(displayFileName.getBytes("UTF-8"), "ISO-8859-1");  
        }  
		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=\"" + displayFileName + "\"");
		String savePath = "";
		if(UploadUtil.getRealSavePath(request).lastIndexOf("/")>0){
			savePath = UploadUtil.getRealSavePath(request);
		}else{
			savePath = UploadUtil.getRealSavePath(request) + "/";
		}
		String downloadFilePath = savePath + filePath + fileName;
			//下载
			String tempFileName = request.getSession().getServletContext().getRealPath("/") + "temp" + "/" + filePath;
			FileSysUtil.downloadFile(filePath, fileName,tempFileName);
			downloadFilePath = tempFileName + "/" + fileName;
			

		response.setContentLength((int) new File(downloadFilePath).length());
		InputStream inputStream = new FileInputStream(new File(downloadFilePath));

		OutputStream os = response.getOutputStream();
		byte[] b = new byte[2048];
		int length;
		while ((length = inputStream.read(b)) > 0) {
			os.write(b, 0, length);
		}

		// 这里主要关闭。
		os.close();

		inputStream.close();
	}
}
