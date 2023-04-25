package com.zfsoft.single.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.FieldsDocumentPart;
import org.apache.poi.hwpf.usermodel.Field;
import org.apache.poi.hwpf.usermodel.Fields;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * 一件事办件通知书打印工具类
 * @author wangwg
 * @date 2020-01-26
 */
public class ReadWordUtils {

    /**
     * 实现对word读取和修改操作
     * @param filePath    word模板路径和名称
     * @param map        待填充的数据，从数据库读取
     */
    public static String readWriteWord(String filePath,String outPath, Map<String,String> map){
        //创建返回文件
        if(outPath !=null) {
            File file = new File(outPath);
            if (!file.exists()) {
                file.mkdirs();// 能创建多级目录
            }
        }
        //读取word模板
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        HWPFDocument hdt = null;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(in);
            hdt = new HWPFDocument(fs);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Fields fields = hdt.getFields();
        Iterator<Field> it = fields.getFields(FieldsDocumentPart.MAIN).iterator();
        while(it.hasNext()){
            System.out.println(it.next().getType());
        }

        //读取word文本内容
        Range range = hdt.getRange();
        System.out.println(range.text());
        //替换文本内容
        for (Map.Entry<String,String> entry: map.entrySet()) {
            range.replaceText("$" + entry.getKey() + "$",entry.getValue()==null?"":entry.getValue());
        }
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        String fileName = ""+System.currentTimeMillis();
        fileName += ".doc";
        FileOutputStream out = null;
        try {
            outPath =outPath+"/"+fileName;
            out = new FileOutputStream(outPath,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            hdt.write(ostream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //输出字节流
        try {
            out.write(ostream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outPath;
    }


//======================输出文件流下载方式：==========================　　


    /**
     * 实现对word读取和修改操作
     * @param response    响应,设置生成的文件类型,文件头编码方式和文件名,以及输出
     * @param filePath    word模板路径和名称
     * @param map        待填充的数据，从数据库读取
     */
    public static void readwriteWord(HttpServletResponse response, String filePath, Map<String, String> map){
        //读取word模板文件
        FileInputStream in;
        HWPFDocument hdt = null;
        try {
            in = new FileInputStream(new File(filePath));
            POIFSFileSystem fs = new POIFSFileSystem(in);
            hdt = new HWPFDocument(fs);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        Fields fields = hdt.getFields();
        Iterator<Field> it = fields.getFields(FieldsDocumentPart.MAIN).iterator();
        while(it.hasNext()){
            System.out.println(it.next().getType());
        }

        //替换读取到的word模板内容的指定字段
        Range range = hdt.getRange();

        for (Map.Entry<String,String> entry:map.entrySet()) {
            range.replaceText("$" + entry.getKey() + "$",entry.getValue());
        }

        //输出word内容文件流，提供下载
        response.reset();
        response.setContentType("application/x-msdownload");
        String fileName = ""+System.currentTimeMillis()+".doc";
        response.addHeader("Content-Disposition", "attachment; filename="+fileName);
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        OutputStream servletOS = null;
        try {
            servletOS = response.getOutputStream();
            hdt.write(ostream);
            servletOS.write(ostream.toByteArray());
            servletOS.flush();
            servletOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
