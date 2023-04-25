package com.zfsoft.service.util.office;

import com.zfsoft.service.dbaccess.data.sxSys.BaseBean;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * zip工具类
 *
 * @author yuy
 * @date 2021-2-9
 */
public class ZipCompressorByAnt {

    private File zipFile;

    /**
     * 压缩文件构造函数
     *
     * @param finalFile
     *            最终压缩生成的压缩文件：目录+压缩文件名.zip
     */
    public ZipCompressorByAnt(String finalFile) {
        zipFile = new File(finalFile);
    }

    /**
     * 执行压缩操作
     *
     * @param srcPathName
     *            需要被压缩的文件/文件夹
     */
    public void compressExe(String srcPathName) {
        File srcdir = new File(srcPathName);
        if (!srcdir.exists()) {
            throw new RuntimeException(srcPathName + "不存在！");
        }
        if (zipFile.exists()) {
            zipFile.delete();
        }

        Project prj = new Project();
        Zip zip = new Zip();
        zip.setEncoding("UTF-8");
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcdir);
        zip.addFileset(fileSet);
        zip.execute();
    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
     *
     * @param zipPath
     *            待解压缩的ZIP文件名
     * @param descDir
     *            目标目录
     */
    public static List<File> upzipFile(String zipPath, String descDir) {
        return upzipFile(new File(zipPath), descDir);
    }

    /**
     * 对.zip文件进行解压缩
     *
     * @param zipFile
     *            解压缩文件
     * @param descDir
     *            压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<File> upzipFile(File zipFile, String descDir) {
        List<File> list = new ArrayList<File>();
        try {
            ZipFile zipFile1 = new ZipFile(zipFile, "GBK");
            for (Enumeration entries = zipFile1.getEntries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                File fil1 = new File(descDir + File.separator + entry.getName());
                if (entry.isDirectory()) {
                    fil1.mkdirs();
                } else {
                    File parent = fil1.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    InputStream in = zipFile1.getInputStream(entry);
                    OutputStream out = new FileOutputStream(fil1);
                    int len = 0;
                    byte[] byte1 = new byte[1024];
                    while ((len = in.read(byte1)) > 0) {
                        out.write(byte1, 0, len);
                    }
                    in.close();
                    out.flush();
                    out.close();
                    list.add(fil1);
                }
            }
        } catch (IOException e) {
        }
        return list;
    }

    /**
     * 对.zip文件进行解压缩
     *
     * @param zipPath
     * @return
     * @throws IOException
     */
    public static List<CompressFileBean> getFilesByZipCompress(String zipPath) throws Exception {
        File zipFile = new File(zipPath);
        return getFilesByZipCompress(zipFile);
    }

    /**
     * 对.zip文件进行解压缩
     *
     * @param zipFile
     *            解压缩文件
     * @return
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static List<CompressFileBean> getFilesByZipCompress(File zipFile) throws Exception {
        List<CompressFileBean> list = new ArrayList<CompressFileBean>();
        ZipFile zipFile1 = new ZipFile(zipFile, "GBK");
        for (Enumeration entries = zipFile1.getEntries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            CompressFileBean cfb = new CompressFileBean(entry.getName(), entry.isDirectory());
            list.add(cfb);
        }
        return list;
    }

    /**
     * 根据压缩文件路径获取文件集合
     *
     * @param is
     * @return 压缩文件集合
     */
    public static List<CompressFileBean> getFilesByCompress(InputStream is) throws Exception {
        return getFilesByCompress(is);
    }

    public static void main(String[] args) {
        // upzipFile("E:\\资料\\colorbox-master.zip", null);
        try {
//            getFilesByCompress("E:\\资料\\全国行政区划代码表.rar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 压缩文件信息
 *
 * @author yuy
 * @date 2021-2-9
 */
class CompressFileBean extends BaseBean {

    private static final long serialVersionUID = -6633981183235266285L;

    private String fileName;
    private boolean directory;

    public CompressFileBean(String fileName, boolean directory) {
        super();
        this.fileName = fileName;
        this.directory = directory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

}
