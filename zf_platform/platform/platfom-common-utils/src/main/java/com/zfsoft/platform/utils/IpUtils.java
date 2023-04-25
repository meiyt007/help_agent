package com.zfsoft.platform.utils;

/**
 * @ClassName IpUtils
 * @Description
 * @Author
 * @Date2020-09-29 17:36
 * @Version V1.0
 **/

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 获取IP工具类
 */
public class IpUtils {

    public static final String IP_UNKNOWN = "unknown ip";

    /**
     * 获取本机IP
     * @return 本机IP
     */
    public static String getLocalIp() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win") ? getLocalIpFromWindows() : getLocalIpFromLinux();
    }

    public static String getMacAddr() {
        String MacAddress = "";
        StringBuilder str = new StringBuilder();

        try {
            NetworkInterface nic = NetworkInterface.getByName("eth0");
            byte[] buf = nic.getHardwareAddress();
            int length = buf.length;

            for (int i = 0; i < length; ++i) {
                byte aBuf = buf[i];
                str.append(byteHEX(aBuf));
            }

            MacAddress = str.toString().toUpperCase();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return MacAddress;
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLocalIpFromLinux(){
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        return ip;
    }

    private static String byteHEX(byte ib) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[]{Digit[ib >>> 4 & 15], Digit[ib & 15]};
        return new String(ob);
    }

    /**
     * 获取Windows服务器的本地IP
     * @return IP
     */
    private static String getLocalIpFromWindows() {
        String localIp;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            localIp = IP_UNKNOWN;
        }

        return localIp;
    }

    /**
     * 获取主机名
     * @return 主机名
     */
    public static String getHostName() {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            hostName = "hostname";
        }
        return hostName;
    }

    /**
     * 获取客户端IP
     * @param request ServerHttpRequest
     * @return 客户端IP
     */
    public static String getClientIp(@NonNull ServerHttpRequest request) {
        String result = null;
        HttpHeaders headers = request.getHeaders();
        if (headers.getFirst("X-Forwarded-For") != null) {
            result = headers.getFirst("X-Forwarded-For");
        } else if (headers.getFirst("Proxy-Client-IP") != null) {
            result = headers.getFirst("Proxy-Client-IP");
        } else if (headers.getFirst("WL-Proxy-Client-IP") != null) {
            result = headers.getFirst("WL-Proxy-Client-IP");
        } else if (headers.getFirst("HTTP_CLIENT_IP") != null) {
            result = headers.getFirst("HTTP_CLIENT_IP");
        } else if (headers.getFirst("X-Real-IP") != null) {
            result = headers.getFirst("X-Real-IP");
        }
        if (StringUtils.isBlank(result) || "unknown".equalsIgnoreCase(result)) {
            result = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        return result;
    }

    /**
     * 获取IP+主机名
     * @return [IP+,+主机名]
     */
    public static String getIpWithBracketWrap() {
        return wrapStringWithBracket(getLocalIp() + "," + getHostName());
    }

    /**
     * 将字符串用中括号括起来
     * @param s 字符串
     * @return [s]
     */
    static String wrapStringWithBracket(String s) {
        return "[" + s + "] ";
    }

}
