package com.zfsoft.microservice.platform.util;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * @description: 注册授权发送服务
 * @author: wuxx
 * @Date: 2020/11/3 10:54
 **/
@Component
public class ClientServer {

	private static String monitorUrl;

	@Value("${zfsoft.monitor.url:#{null}")
	private String url;

	public static String sendPost(String json) {
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

	public void setUrl(String url) {
		this.monitorUrl = url;
		this.url = url;
	}
}
