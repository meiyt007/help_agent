package com.zfsoft.single.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/22 13:28
 */
public class ZipUtils {

    private static final int  BUFFER_SIZE = 2 * 1024;

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException {

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {

            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {

                try {
                    zos.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        }
    }


    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    //zos.putNextEntry(new ZipEntry(name + File.separator));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        //compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                        String value = "";
                        if(StringUtils.isEmpty(name)){
                            //value = File.separator;
                            value = "";
                        }else{
                            //value = name + File.separator;
                            value = name + "/" ;
                        }
                        compress(file, zos, value + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }

    public static void toZip(String srcDir, OutputStream out,String name ,boolean KeepDirStructure)
            throws RuntimeException {

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {

            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            //compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            compress(sourceFile, zos, name, KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {

                try {
                    zos.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 递归删除
     * 删除某个目录及目录下的所有子目录和文件
     * @param file 文件或目录
     * @return 删除结果
     */
    public static boolean delFiles(File file){
        boolean result = false;
        //目录
        if(file.isDirectory()){
            File[] childrenFiles = file.listFiles();
            for (File childFile:childrenFiles){
                result = delFiles(childFile);
                if(!result){
                    return result;
                }
            }
        }
        //删除 文件、空目录
        result = file.delete();
        return result;
    }

    /**
     * 压缩成ZIP 方法2
     * @param srcFiles 需要压缩的文件列表
     * @param out 压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //FileOutputStream fos1 = new FileOutputStream(new File("E:/compress/mytest01.zip"));
        //ZipUtils.toZip("E:/pic", fos1,true);
        //FileOutputStream fos1 = new FileOutputStream(new File("E:/compress/mytest01HasName.zip"));
        //ZipUtils.toZip("E:/pic", fos1,"超级综窗柜台-自动分类数据集",true);

//        File  del = new File("E:/compress/20210723135715980test");
//        if(del.exists()){
//            delFiles(del);
//        }

        List<File> fileList = new ArrayList<>();
        fileList.add(new File("C:\\Users\\Admin\\Desktop\\pdf\\材料二.pdf"));
        fileList.add(new File("C:\\Users\\Admin\\Desktop\\pdf\\材料六.pdf"));
        fileList.add(new File("C:\\Users\\Admin\\Desktop\\pdf\\材料七.pdf"));


        String path = System.getProperty("user.dir");
        File file2 = new File(path + "\\zf_help_agent_ms\\zf_case\\zf_case_service_provider\\src\\main\\java\\com\\zfsoft\\composeJpg");
        file2.mkdir();
        String pathZip = path + "\\zf_help_agent_ms\\zf_case\\zf_case_service_provider\\src\\main\\java\\com\\zfsoft\\composeJpg\\aa.zip";
        File fileZip = new File(pathZip);
        FileOutputStream fos2 = new FileOutputStream(fileZip);
        ZipUtils.toZip(fileList, fos2);
    }


}

