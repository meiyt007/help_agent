package com.zfsoft.ha.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.zip.GZIPInputStream;


public class HttpClient {


	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String CONTENT_ENCODING_GZIP = "gzip";

	public static final String CONTENT_FORM = "application/x-www-form-urlencoded;charset=UTF-8";
	public static final String CONTENT_MULTIPART = "multipart/form-data;charset=UTF-8; boundary=";
	public static final String APPLICATION_JSON = "application/json";

	public static final int RENNECT_TIMES = 2;

	private static final int CONNECT_TIMEOUT = 15000;
	private static final int READ_TIMEOUT = 30000;


	private final int connectTimeout;
	private final int readTimeout;
	private final Optional<Proxy> proxy;

	public HttpClient(Optional<Proxy> proxy) {
		super();
		this.connectTimeout = CONNECT_TIMEOUT;
		this.readTimeout = READ_TIMEOUT;
		this.proxy = proxy;
	}

	public HttpClient(int connectTimeout, int readTimeout, Optional<Proxy> proxy) {
		super();
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.proxy = proxy;
	}

	public String doService(String url, HttpParamers paramers, HttpHeader header) throws Exception {
		HttpMethod httpMethod = paramers.getHttpMethod();
		switch (httpMethod) {
		case POST:
			return doPost(url, paramers, header);
		case GET:
			return doGet(url, paramers, header);
		default:
			return null;
		}
	}

	/**
	 * @param url
	 * @param
	 * @param header
	 * @return
	 * @throws IOException
	 */
	public String doPost(String url, HttpParamers paramers, HttpHeader header) throws Exception {

		HttpURLConnection conn = null;
		OutputStream out = null;
		String boundary = null;
		byte[] content = null;
		// 网络连接失败重试
		for(int i = 0; i <= RENNECT_TIMES; i++) {

			try {
				conn = HttpConnection.getConnection(new URL(url), HttpMethod.POST, header,proxy);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
				if (paramers.isMultipart()) {
					boundary = "----sdkboundary" + StringUtils.random(6); // 随机分隔线
					conn.setRequestProperty("Content-Type", CONTENT_MULTIPART + boundary);
					out = conn.getOutputStream();
				} else {
					String query = paramers.getQueryString(DEFAULT_CHARSET);
					content = query == null ? ArrayUtils.EMPTY_BYTE_ARRAY : query.getBytes(DEFAULT_CHARSET);
					conn.setRequestProperty("Content-Type", CONTENT_FORM);
					out = conn.getOutputStream();
				}
				break;
			} catch (Exception e) {
				IOUtils.safeClose(out);
				HttpConnection.close(conn);
				if(i == RENNECT_TIMES) {
					throw e;
				}
			}

		}

		try {
			if (paramers.isMultipart()) {
				writeMutiContent(boundary, paramers, out);
			} else {
				out.write(content);
			}
			return getResponseAsString(conn);
		} finally {
			IOUtils.safeClose(out);
			HttpConnection.close(conn);
		}
	}

	public HttpURLConnection doPostReturnConnection(String url, HttpParamers paramers, HttpHeader header) throws Exception {

		HttpURLConnection conn = null;
		OutputStream out = null;
		String boundary = null;
		byte[] content = null;
		// 网络连接失败重试
		for(int i = 0; i <= RENNECT_TIMES; i++) {

			try {
				conn = HttpConnection.getConnection(new URL(url), HttpMethod.POST, header,proxy);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
				if (paramers.isMultipart()) {
					boundary = "----sdkboundary" + StringUtils.random(6); // 随机分隔线
					conn.setRequestProperty("Content-Type", CONTENT_MULTIPART + boundary);
					out = conn.getOutputStream();
				} else {
					String query = paramers.getQueryString(DEFAULT_CHARSET);
					content = query == null ? ArrayUtils.EMPTY_BYTE_ARRAY : query.getBytes(DEFAULT_CHARSET);
					conn.setRequestProperty("Content-Type", CONTENT_FORM);
					out = conn.getOutputStream();
				}
				break;
			} catch (Exception e) {
				IOUtils.safeClose(out);
				HttpConnection.close(conn);
				if(i == RENNECT_TIMES) {
					throw e;
				}
			}

		}

		try {
			if (paramers.isMultipart()) {
				writeMutiContent(boundary, paramers, out);
			} else {
				out.write(content);
			}
			return conn;
		} finally {
			IOUtils.safeClose(out);
			HttpConnection.close(conn);
		}
	}

	/**
	 * @param url
	 * @param content
	 * @param header
	 * @return
	 * @throws IOException
	 */
	public String doPost(String url, byte[] content, HttpHeader header) throws Exception {
		HttpURLConnection conn = null;
		OutputStream out = null;
		// 网络连接失败重试
		for(int i = 0; i <= RENNECT_TIMES; i++) {
			try {
				conn = HttpConnection.getConnection(new URL(url), HttpMethod.POST, header,proxy);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
				conn.setRequestProperty("Content-Type", "application/json");
				out = conn.getOutputStream();
				break;
			} catch (Exception e) {
				IOUtils.safeClose(out);
				HttpConnection.close(conn);
				if(i == RENNECT_TIMES) {
					throw e;
				}
			}
		}
		try {
			out.write(content);
			return getResponseAsString(conn);
		} finally {
			IOUtils.safeClose(out);
			HttpConnection.close(conn);
		}
	}

	/**
	 * 写入multipart参数
	 *
	 * @param boundary
	 * @param paramers
	 * @param out
	 * @throws IOException
	 */
	protected static void writeMutiContent(String boundary, HttpParamers paramers, OutputStream out) throws IOException {
		byte[] boundaryBytes = ("\r\n--" + boundary + "\r\n").getBytes(DEFAULT_CHARSET);

		// 写入文本请求参数`
		Set<Entry<String, String>> textEntrySet = paramers.getParams().entrySet();
		boolean firstParam = true;
		for (Entry<String, String> textEntry : textEntrySet) {
			byte[] textBytes = getTextEntry(textEntry.getKey(), textEntry.getValue(), DEFAULT_CHARSET);
			if(firstParam) {
				out.write(("--" + boundary + "\r\n").getBytes(DEFAULT_CHARSET));
				firstParam = false;
			} else {
				out.write(boundaryBytes);
			}
			out.write(textBytes);
		}

		// 写入文件请求参数
		if(!paramers.getFiles().isEmpty()){
			Set<Entry<String, FileItem>> fileEntrySet = paramers.getFiles().entrySet();
			for (Entry<String, FileItem> fileEntry : fileEntrySet) {
				FileItem fileItem = fileEntry.getValue();
				if (!fileItem.isValid()) {
					throw new IOException("FileItem is invalid");
				}
				byte[] fileBytes = getFileEntry(fileEntry.getKey(), fileItem.getFileName(), fileItem.getMimeType(),
						DEFAULT_CHARSET);
				out.write(boundaryBytes);
				out.write(fileBytes);
				fileItem.write(out);
			}
		}

		//写入批量文件请求
		if(!paramers.getListFiles().isEmpty()){
			Set<Entry<String, List<FileItem>>> listEntrySet = paramers.getListFiles().entrySet();
			for(Entry<String, List<FileItem>> listEntry:listEntrySet){
				List<FileItem> items =listEntry.getValue();
				for(FileItem item :items){
					if (!item.isValid()) {
						throw new IOException("FileItem is invalid");
					}
					byte[] fileBytes = getFileEntry(listEntry.getKey(), item.getFileName(), item.getMimeType(),
							DEFAULT_CHARSET);
					out.write(boundaryBytes);
					out.write(fileBytes);
					item.write(out);
				}
			}
		}

		// 添加请求结束标志
		byte[] endBoundary = ("\r\n--" + boundary + "--\r\n").getBytes(DEFAULT_CHARSET);
		out.write(endBoundary);
	}

	private static byte[] getTextEntry(String fieldName, String fieldValue, String charset) throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
		entry.append(fieldValue);
		return entry.toString().getBytes(charset);
	}

	private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset)
			throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\";filename=\"");
		entry.append(fileName);
		entry.append("\"\r\nContent-Type:");
		entry.append(mimeType);
		entry.append("\r\n\r\n");
		return entry.toString().getBytes(charset);
	}

	/**
	 * 执行HTTP GET请求。
	 *
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param
	 * @param
	 * @return 响应字符串
	 */
	public String doGet(String url, HttpParamers params, HttpHeader header) throws Exception {
		HttpURLConnection conn = null;
		String query = params.getQueryString(DEFAULT_CHARSET);
		URL getUrl = buildGetUrl(url, query);
		int responseCode = 200;
		// 网络连接失败重试
		for(int i = 0; i <= RENNECT_TIMES; i++) {
			try {
				conn = HttpConnection.getConnection(getUrl, HttpMethod.GET, header,proxy);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
				responseCode = conn.getResponseCode();
				break;
			} catch(Exception e) {
				HttpConnection.close(conn);
				if(i == RENNECT_TIMES) {
					throw e;
				}
			}
		}

		try {
			return getResponseAsString(conn, responseCode);
		} finally {
			HttpConnection.close(conn);
		}
	}

	/**
	 * 执行下载请求
	 * @param url
	 * @param params
	 * @param header
	 * @param outputStream
	 * @return
	 * @throws Exception
	 */
	public void doDownload(String url, HttpParamers params, HttpHeader header,OutputStream outputStream) throws Exception
	{
		HttpURLConnection conn = null;
		String query = params.getQueryString(DEFAULT_CHARSET);
		URL getUrl = buildGetUrl(url, query);
		int responseCode = 200;
		// 网络连接失败重试
		for(int i = 0; i <= RENNECT_TIMES; i++) {

			try {
				conn = HttpConnection.getConnection(getUrl, HttpMethod.GET, header,proxy);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
				responseCode = conn.getResponseCode();
				break;
			} catch(Exception e) {
				HttpConnection.close(conn);
				if(i == RENNECT_TIMES) {
					throw e;
				}
			}
		}
		try {
			getResponseAsOutputStream(conn, outputStream, responseCode);
		} finally {
			HttpConnection.close(conn);
		}
	}

	protected static URL buildGetUrl(String url, String query) throws IOException {
		if (StringUtils.isEmpty(query)) {
			return new URL(url);
		}
		StringBuilder newUrl = new StringBuilder(url);
		boolean hasQuery = url.contains("?");
		boolean hasPrepend = url.endsWith("?") || url.endsWith("&");

		if (!hasPrepend) {
			if (hasQuery) {
				newUrl.append("&");
			} else {
				newUrl.append("?");
				hasQuery = true;
			}
		}
		newUrl.append(query);
		hasPrepend = false;
		return new URL(newUrl.toString());
	}

	protected static String getResponseAsString(HttpURLConnection conn, int responseCode) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		if (responseCode < 400) {
			String contentEncoding = conn.getContentEncoding();
			if (CONTENT_ENCODING_GZIP.equalsIgnoreCase(contentEncoding)) {
				return getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset);
			} else {
				return getStreamAsString(conn.getInputStream(), charset);
			}
		} else {// Client Error 4xx and Server Error 5xx
			String errorStr = "";
			InputStream inputStream = conn.getErrorStream();
			try {
				if (inputStream != null) {
					byte[] bs = new byte[inputStream.available()];
					if (bs.length > 0) {
						inputStream.read(bs);
						errorStr = new String(bs, "UTF-8");
					}
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage() + " " + errorStr);
		}
	}

	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		if (conn.getResponseCode() < 400) {
			String contentEncoding = conn.getContentEncoding();
			if (CONTENT_ENCODING_GZIP.equalsIgnoreCase(contentEncoding)) {
				return getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset);
			} else {
				return getStreamAsString(conn.getInputStream(), charset);
			}
		} else {// Client Error 4xx and Server Error 5xx
			String errorStr = "";
			InputStream inputStream = conn.getErrorStream();
			try {
				if (inputStream != null) {
					byte[] bs = new byte[inputStream.available()];
					if (bs.length > 0) {
						inputStream.read(bs);
						errorStr = new String(bs, "UTF-8");
					}
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage() + " " + errorStr);
		}
	}

	private static void getResponseAsOutputStream(HttpURLConnection conn,OutputStream outputStream, int responseCode) throws IOException {
		if (responseCode < 400) {
			outputStream.write(IOUtils.readStreamAsByteArray(conn.getInputStream()));
			outputStream.close();
		} else {// Client Error 4xx and Server Error 5xx
			String errorStr = "";
			InputStream inputStream = conn.getErrorStream();
			try {
				if (inputStream != null) {
//					Thread.sleep(2000);
					byte[] bs = new byte[1024];
					if (bs.length > 0) {
						inputStream.read(bs);
						errorStr = new String(bs, "UTF-8");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage() + " " + errorStr);
		}
	}

	public static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			Reader reader = new InputStreamReader(stream, charset);
			StringBuilder response = new StringBuilder();

			final char[] buff = new char[1024];
			int read = 0;
			while ((read = reader.read(buff)) > 0) {
				response.append(buff, 0, read);
			}

			return response.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	public static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;

		if (!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

	public String doPost(String url, Map<String, String> params) throws Exception {
		HttpParamers httpParamers = new HttpParamers(HttpMethod.POST);
		httpParamers.addParams(params);
		String result = doPost(url, httpParamers, HttpHeader.DEFAULT);
		return result;
	}

	public String doServiceWithJson(String url, String json, HttpHeader header) throws Exception {
		HttpURLConnection conn = null;
		OutputStream out = null;
		byte[] content = null;
		// 网络连接失败重试
		for(int i = 0; i <= RENNECT_TIMES; i++) {

			try {
				conn = HttpConnection.getConnection(new URL(url), HttpMethod.POST, header,proxy);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
				content = json == null ? ArrayUtils.EMPTY_BYTE_ARRAY : json.getBytes(DEFAULT_CHARSET);
				conn.setRequestProperty("Content-Type", APPLICATION_JSON);
				out = conn.getOutputStream();
				break;
			} catch (Exception e) {
				IOUtils.safeClose(out);
				HttpConnection.close(conn);
				if(i == RENNECT_TIMES) {
					throw e;
				}
			}

		}

		try {
			out.write(content);
			return getResponseAsString(conn);
		} finally {
			IOUtils.safeClose(out);
			HttpConnection.close(conn);
		}
	}
}
