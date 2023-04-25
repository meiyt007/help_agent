package com.zfsoft.superwindow.util.fa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.FileImageOutputStream;
import java.io.*;

/**
 * 计算机视觉帮助方法
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
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
        if (fp.isFile()) {
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
        for (int i = 0; i < fileName.length; i++) {
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
        if (divisor == 1) {
            return size / divisor + " " + unit;
        }
        String aftercomma = String.valueOf(100L * (size % divisor) / divisor);
        if (aftercomma.length() == 1) {
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
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }

    public static void byte2image(byte[] imageBtye, String filePath) {
        if (imageBtye.length < 3 || filePath.equals("")) {
            return;// 判断输入的byte是否为空
        }
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(filePath));// 打开输入流
            imageOutput.write(imageBtye, 0, imageBtye.length);// 将byte写入硬盘
            imageOutput.close();
        } catch (Exception ex) {
            logger.error("二进制数组转文件失败", ex);
        }
    }

}
