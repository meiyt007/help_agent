package com.zfsoft.service.util;


import com.alibaba.fastjson.JSONArray;

import java.io.*;
import java.util.Map;


public class FileSysUtil {
    /**
     * 文件下载
     * @param pathsrc
     * @param fileName
     * @param descPath
     * @return
     * @author wangxl
     * @date 2020-04-13
     */
    public static boolean downloadFile(String pathsrc,String fileName,String descPath) {
        boolean flag =false;
        InputStream is = null;
        OutputStream os = null;
        try{
        	 is = FileInterfaceUtil.downLoadFile(pathsrc+fileName);
        	if(null == is ){
        		return flag;
        	}
        	 File pathFile = new File(descPath);
             if (!pathFile.exists()) {
                 pathFile.mkdirs();
             }
             os = new BufferedOutputStream(new FileOutputStream(new File(descPath + "/" + fileName)));
             byte[] b = new byte[1024*10];
             int len=0;
             while((len = is.read(b))!=-1){
            	 os.write(b,0,len);
             }
             os.flush();
             os.close();
             is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        	try {
	        		if(null != os){
						os.close();
	        		}
	        		if(null != is){
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
			}
			
		}
        return flag;
    }
    
    /**
     * 
     * @param fileName
     * @param savePath
     * @param originalFileName
     * @param inputStream
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2020-04-13
     */
    public static boolean TYWJSave(Map<String, String> paramMap, InputStream inputStream)
			throws Exception {
		boolean flag = false;
		String msgJson = FileInterfaceUtil.upLoadFile(paramMap, inputStream);
		Map<String, Object> map2 = JSONArray.parseObject(msgJson);
//		JsonParser jp = new JsonParser();
//		// 将json字符串转化成json对象
//		JsonObject jo = jp.parse(msgJson).getAsJsonObject();
		// 获取success对应的值
		String message = map2.get("success").toString();
		if (message.equals("true")) {
			flag = true;
		}
		return flag;
	}
    
    /**
     * 删除文件
     * @param requestData 文件主键 或 相对路径
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2020-05-29
     */
    public static boolean delFile(String requestData)throws Exception {
    	return FileInterfaceUtil.delFile(requestData);
    }
    
    /**
     * 判断文件是否存在
     * @param requestData 文件主键 或 相对路径
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2020-05-29
     */
    public static boolean existFile(String requestData)throws Exception {
    	return FileInterfaceUtil.existFile(requestData);
    }
}
