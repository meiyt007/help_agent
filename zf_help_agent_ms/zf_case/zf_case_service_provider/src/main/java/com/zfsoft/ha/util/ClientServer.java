package com.zfsoft.ha.util;

import com.zfsoft.ha.http.HttpHeader;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: wangyh
 * @Date: 2022年8月22日16:48:36
 **/
public class ClientServer {
	/**
	 * 发送post请求
	 * @param url  路径
	 * @param jsonObject  参数(json类型)
	 * @param encoding 编码格式
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String sendPostXd(String url, String jsonObject, String encoding, Map<String,Object> headerMap) throws ParseException, IOException {
		String body = "";
		//创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		//装填参数
		if(!jsonObject.isEmpty()){
			StringEntity s = new StringEntity(jsonObject, "utf-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			//设置参数到请求对象中
			httpPost.setEntity(s);
		}
//        System.out.println("请求参数："+nvps.toString());
		//设置header信息
		//指定报文头【Content-type】、【User-Agent】
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("appid", (String) headerMap.get("appid"));
		httpPost.setHeader("apiname", (String) headerMap.get("apiname"));
		httpPost.setHeader("signature", (String) headerMap.get("signature"));
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		//执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		//获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			//按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		//释放链接
		response.close();
		return body;
	}
	/**
	 * 发送post请求
	 *
	 * @param requestUrl       请求url
	 * @param requestHeader    请求头
	 * @param formTexts        表单数据
	 * @param files            上传文件
	 * @param requestEncoding  请求编码
	 * @param responseEncoding 响应编码
	 * @return 页面响应html
	 */
	public static String sendPostInter(String requestUrl, Map<String, String> requestHeader, Map<String, Object> formTexts, Map<String, MultipartFile> files, String requestEncoding, String responseEncoding) {
		OutputStream out = null;
		BufferedReader reader = null;
		String result = "";
		try {
			if (requestUrl == null || requestUrl.isEmpty()) {
				return result;
			}
			URL realUrl = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("accept", "text/html, application/xhtml+xml, image/jxr, */*");
			connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
			if (requestHeader != null && requestHeader.size() > 0) {
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			if (requestEncoding == null || requestEncoding.isEmpty()) {
				requestEncoding = "UTF-8";
			}
			if (responseEncoding == null || responseEncoding.isEmpty()) {
				responseEncoding = "UTF-8";
			}
			if (requestHeader != null && requestHeader.size() > 0) {
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			if (files == null || files.size() == 0) {
				connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
				out = new DataOutputStream(connection.getOutputStream());
				if (formTexts != null && formTexts.size() > 0) {
					String formData = "";
					for (Map.Entry<String, Object> entry : formTexts.entrySet()) {
						formData += entry.getKey() + "=" + entry.getValue() + "&";
					}
					formData = formData.substring(0, formData.length() - 1);
					out.write(formData.toString().getBytes(requestEncoding));
				}
			} else {
				String boundary = "-----------------------------" + String.valueOf(new Date().getTime());
				connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
				out = new DataOutputStream(connection.getOutputStream());
				if (formTexts != null && formTexts.size() > 0) {
					StringBuilder sbFormData = new StringBuilder();
					for (Map.Entry<String, Object> entry : formTexts.entrySet()) {
						sbFormData.append("--" + boundary + "\r\n");
						sbFormData.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
						sbFormData.append(entry.getValue() + "\r\n");
					}
					out.write(sbFormData.toString().getBytes(requestEncoding));
				}
				for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
					String fileName = entry.getKey();
					MultipartFile multipartFile  = (MultipartFile) entry.getValue();
					if (fileName == null || fileName.isEmpty()) {
						continue;
					}
					out.write(("--" + boundary + "\r\n").getBytes(requestEncoding));
					out.write(("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + fileName + "\"\r\n").getBytes(requestEncoding));
					out.write(("Content-Type: application/x-msdownload\r\n\r\n").getBytes(requestEncoding));
//					MultipartFile multipartFile = FileUtils.tranFile("https://hf.zhuofansoft.com:12102/group1/M00/C0/34/rKj6BmJTv9OAS0yzAABtX1yr6Bs882.jpg");
					DataInputStream in = new DataInputStream(multipartFile.getInputStream());
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
					out.write(("\r\n").getBytes(requestEncoding));
				}
				out.write(("--" + boundary + "--").getBytes(requestEncoding));
			}
			out.flush();
			out.close();
			out = null;
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), responseEncoding));
			String line;
			while ((line = reader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！");
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 发送post请求
	 *
	 * @param requestUrl       请求url
	 * @param requestHeader    请求头
	 * @param formTexts        表单数据
	 * @param files            上传文件
	 * @param requestEncoding  请求编码
	 * @param responseEncoding 响应编码
	 * @return 页面响应html
	 */
	public static String sendPost(String requestUrl, Map<String, String> requestHeader, Map<String, Object> formTexts, Map<String, MultipartFile> files, String requestEncoding, String responseEncoding) {
		OutputStream out = null;
		BufferedReader reader = null;
		String result = "";
		try {
			if (requestUrl == null || requestUrl.isEmpty()) {
				return result;
			}
			URL realUrl = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestProperty("accept", "text/html, application/xhtml+xml, image/jxr, */*");
			connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
			if (requestHeader != null && requestHeader.size() > 0) {
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			if (requestEncoding == null || requestEncoding.isEmpty()) {
				requestEncoding = "UTF-8";
			}
			if (responseEncoding == null || responseEncoding.isEmpty()) {
				responseEncoding = "UTF-8";
			}
			if (requestHeader != null && requestHeader.size() > 0) {
				for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			if (files == null || files.size() == 0) {
				connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
				out = new DataOutputStream(connection.getOutputStream());
				if (formTexts != null && formTexts.size() > 0) {
					String formData = "";
					for (Map.Entry<String, Object> entry : formTexts.entrySet()) {
						formData += entry.getKey() + "=" + entry.getValue() + "&";
					}
					formData = formData.substring(0, formData.length() - 1);
					out.write(formData.toString().getBytes(requestEncoding));
				}
			} else {
				String boundary = "-----------------------------" + String.valueOf(new Date().getTime());
				connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
				out = new DataOutputStream(connection.getOutputStream());
				if (formTexts != null && formTexts.size() > 0) {
					StringBuilder sbFormData = new StringBuilder();
					for (Map.Entry<String, Object> entry : formTexts.entrySet()) {
						sbFormData.append("--" + boundary + "\r\n");
						sbFormData.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
						sbFormData.append(entry.getValue() + "\r\n");
					}
					out.write(sbFormData.toString().getBytes(requestEncoding));
				}
				for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
					String fileName = entry.getKey();
					MultipartFile multipartFile  = (MultipartFile) entry.getValue();
					if (fileName == null || fileName.isEmpty()) {
						continue;
					}
					out.write(("--" + boundary + "\r\n").getBytes(requestEncoding));
					out.write(("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + fileName + "\"\r\n").getBytes(requestEncoding));
					out.write(("Content-Type: application/x-msdownload\r\n\r\n").getBytes(requestEncoding));
//					MultipartFile multipartFile = FileUtils.tranFile("https://hf.zhuofansoft.com:12102/group1/M00/C0/34/rKj6BmJTv9OAS0yzAABtX1yr6Bs882.jpg");
					DataInputStream in = new DataInputStream(multipartFile.getInputStream());
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
					out.write(("\r\n").getBytes(requestEncoding));
				}
				out.write(("--" + boundary + "--").getBytes(requestEncoding));
			}
			out.flush();
			out.close();
			out = null;
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), responseEncoding));
			String line;
			while ((line = reader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！");
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

     /**
	 * 发送post请求
     * @param url  路径
     * @param jsonObject  参数(json类型)
     * @param encoding 编码格式
     * @return
	 * @throws ParseException
     * @throws IOException
     */
	public static String send(String url, String jsonObject,String encoding) throws ParseException, IOException {
		String body = "";
		//创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		//装填参数
		StringEntity s = new StringEntity(jsonObject, "utf-8");
		s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json"));
		//设置参数到请求对象中
		httpPost.setEntity(s);
		System.out.println("请求地址："+url);
//        System.out.println("请求参数："+nvps.toString());

		//设置header信息
		//指定报文头【Content-type】、【User-Agent】
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		//执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		//获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			//按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		//释放链接
		response.close();
		return body;
	}

	public static String sendPost(String json,String monitorUrl) {
		String param = "json="+json;
		StringBuffer sb = new StringBuffer();
		try {
			URL realUrl = new URL(monitorUrl);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			// 定义BufferedReader输入流来读取URL的响应]
			try (PrintWriter out = new PrintWriter(conn.getOutputStream())) {
				out.print(param);
				out.flush();
				sb.append(IOUtils.toString(conn.getInputStream()));
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		return sb.toString();
	}

}
