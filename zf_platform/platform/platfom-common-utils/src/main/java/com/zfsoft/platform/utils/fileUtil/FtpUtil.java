package com.zfsoft.platform.utils.fileUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * @author FTP管理类
 */
public class FtpUtil {

    /**
     * 上传文件
     *
     * @param hostname    FTP服务器地址
     * @param port        FTP服务器端口号
     * @param username    FTP登录帐号
     * @param password    FTP登录密码
     * @param pathname    FTP服务器保存目录
     * @param fileName    上传到FTP服务器后的文件名称
     * @param inputStream 输入文件流
     * @return
     */
    public static boolean uploadFile(String hostname, int port, String username, String password, String pathname,
                                     String fileName, InputStream inputStream) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            if (SystemUtil.getOsInfo().isWindows()) {
                ftpClient.enterLocalActiveMode();// 设置传输协议 ,主动传输;
            } else {
                ftpClient.enterLocalPassiveMode();// 设置传输协议 ,被动传输;
            }
            // 是否成功登录FTP服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }

            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            createPath(ftpClient, pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 创建FTP路径
     *
     * @param ftpClient
     * @param pathname
     * @throws IOException
     */
    private static void createPath(FTPClient ftpClient, String pathname) throws IOException {
        if (StrUtil.isEmpty(pathname)) {
            return;
        }
        String[] paths = pathname.split("/");
        for (String path : paths) {
            if (!StrUtil.isEmpty(path)) {
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(path);
            }
        }
    }

    /**
     * 上传文件（可对文件进行重命名）
     *
     * @param hostname       FTP服务器地址
     * @param port           FTP服务器端口号
     * @param username       FTP登录帐号
     * @param password       FTP登录密码
     * @param pathname       FTP服务器保存目录
     * @param filename       上传到FTP服务器后的文件名称
     * @param originfilename 待上传文件的名称（绝对地址）
     * @return
     */
    public static boolean uploadFileFromProduction(String hostname, int port, String username, String password,
                                                   String pathname, String filename, String originfilename) {
        boolean flag = false;
        try {
            InputStream inputStream = new FileInputStream(new File(originfilename));
            flag = uploadFile(hostname, port, username, password, pathname, filename, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件（不可以进行文件的重命名操作）
     *
     * @param hostname       FTP服务器地址
     * @param port           FTP服务器端口号
     * @param username       FTP登录帐号
     * @param password       FTP登录密码
     * @param pathname       FTP服务器保存目录
     * @param originfilename 待上传文件的名称（绝对地址）
     * @return
     */
    public static boolean uploadFileFromProduction(String hostname, int port, String username, String password,
                                                   String pathname, String originfilename) {
        boolean flag = false;
        try {
            String fileName = new File(originfilename).getName();
            InputStream inputStream = new FileInputStream(new File(originfilename));
            flag = uploadFile(hostname, port, username, password, pathname, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param hostname FTP服务器地址
     * @param port     FTP服务器端口号
     * @param username FTP登录帐号
     * @param password FTP登录密码
     * @param pathname FTP服务器保存目录
     * @param filename 要删除的文件名称
     * @return
     */
    public static boolean deleteFile(String hostname, int port, String username, String password, String pathname,
                                     String filename) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            if (SystemUtil.getOsInfo().isWindows()) {
                ftpClient.enterLocalActiveMode();// 设置传输协议 ,主动传输;
            } else {
                ftpClient.enterLocalPassiveMode();// 设置传输协议 ,被动传输;
            }
            // 验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            // 切换FTP目录
            createPath(ftpClient, pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                } catch (IOException e) {

                }
            }
        }
        return flag;
    }

    /**
     * 下载文件
     *
     * @param hostname  FTP服务器地址
     * @param port      FTP服务器端口号
     * @param username  FTP登录帐号
     * @param password  FTP登录密码
     * @param pathname  FTP服务器文件目录
     * @param filename  文件名称
     * @param localpath 下载后的文件路径
     * @return
     */
    public static boolean downloadFile(String hostname, int port, String username, String password, String pathname,
                                       String filename, String localpath) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            if (SystemUtil.getOsInfo().isWindows()) {
                ftpClient.enterLocalActiveMode();// 设置传输协议 ,主动传输;
            } else {
                ftpClient.enterLocalPassiveMode();// 设置传输协议 ,被动传输;
            }
            // 验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            // 切换FTP目录
            createPath(ftpClient, pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    File pathFile = new File(localpath);
                    if (!pathFile.exists()) {
                        pathFile.mkdirs();
                    }
                    File localFile = new File(localpath + "/" + file.getName());
                    if (localFile.exists()) {
                        break;
                    }
                    OutputStream os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                    break;
                }
            }
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                } catch (IOException e) {

                }
            }
        }
        return flag;
    }

    /**
     * 根据文件路径，判断FTP上是否存在该文件
     *
     * @param hostname
     * @param port
     * @param username
     * @param password
     * @param fileDir
     * @param fileName
     * @return
     * @author luzw
     * @date 2018年9月25日
     */
    public static boolean isFTPFileExist(String hostname, int port, String username, String password, String fileDir,
                                         String fileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            if (SystemUtil.getOsInfo().isWindows()) {
                ftpClient.enterLocalActiveMode();// 设置传输协议 ,主动传输;
            } else {
                ftpClient.enterLocalPassiveMode();// 设置传输协议 ,被动传输;
            }
            // 验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return false;
            }
            // 设置文件类型为二进制，与ASCII有区别
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置编码格式
            ftpClient.setControlEncoding("GBK");
            // 进入文件所在目录，注意编码格式，以能够正确识别中文目录
            ftpClient.changeWorkingDirectory(new String(fileDir.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
            // 检验文件是否存在
            InputStream is = ftpClient
                    .retrieveFileStream(new String(fileName.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
            if (is == null || ftpClient.getReplyCode() == FTPReply.FILE_UNAVAILABLE) {
                return false;
            }
            if (is != null) {
                is.close();
                ftpClient.completePendingCommand();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 获取文件流
     *
     * @param hostname
     * @param port
     * @param username
     * @param password
     * @param fileDir  文件目录
     * @param fileName 文件名
     * @return
     * @author luzw
     * @date 2018年9月25日
     */
    public static InputStream getFileInputInputStrem(String hostname, int port, String username, String password,
                                                     String fileDir, String fileName) {
        if (StrUtil.isEmpty(fileDir) || StrUtil.isEmpty(fileName)) {
            return null;
        }
        FTPClient ftpClient = new FTPClient();
        InputStream in = null;
        try {
            // 连接FTP服务器
            ftpClient.connect(hostname, port);
            // 登录FTP服务器
            ftpClient.login(username, password);
            if (SystemUtil.getOsInfo().isWindows()) {
                ftpClient.enterLocalActiveMode();// 设置传输协议 ,主动传输;
            } else {
                ftpClient.enterLocalPassiveMode();// 设置传输协议 ,被动传输;
            }
            // 验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.changeWorkingDirectory(new String(fileDir.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
                ftpClient.setBufferSize(1024 * 1024 * 10);//设置缓冲区 10M
                in = ftpClient.retrieveFileStream(new String(fileName.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return in;
    }
}
