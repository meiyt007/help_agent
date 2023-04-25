package com.zfsoft.service.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


/**
 * @author: kkfan
 * @create: 2020-10-22 15:35:54
 * @description: UUID帮助类
 */
public class PdfUtil {


    public static  String toPdf(List<String>  dzUrl, String pdfPath) {
        try {
           String imagePath = null;
           // 输入流
            FileOutputStream fos = new FileOutputStream(pdfPath);
           // 创建文档
            Document document = new Document();
            document.setMargins(0, 0, 0, 0);
            PdfWriter.getInstance(document, fos);
            document.open();

            int len = dzUrl.size();
            for (int i = 0; i < len; i++) {
                String temp = dzUrl.get(i);
               System.out.println(temp);
                Image imgs = Image.getInstance(temp);
                imgs.setAlignment(Image.ALIGN_CENTER);
                // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                document.setPageSize(new Rectangle(imgs.getWidth(), imgs.getHeight()));
                //document.setPageSize(new Rectangle(597, 844));
                document.newPage();
                document.add(imgs);
            }
            // 关闭文档
            document.close();


            //System.out.println("存放地址"+pdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfPath;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath().replace("!",""));
        //fileName =path.getPath()+ "/templates/"+DocuTemplatEnum.getTemplateFileName(appCode,docTypeCode)+".doc";
        System.out.println("******"+path.getPath());

        //D:\Project\zf_dzcpt_ms_soft2.0\commonservice\case_service_provider\target\classes


        /*
        long time1 = System.currentTimeMillis();
        List<String> dzUrl=new ArrayList<>();
        dzUrl.add("D:/Demo/ceshi/cl.jpg");
        dzUrl.add("D:/Demo/ceshi/yyzz.jpg");
        dzUrl.add(" http://172.168.250.6:8888/group1/M00/00/23/rKj6BmDw8S6ARRTLAAUP3k072jw776.jpg");
        dzUrl.add(" http://172.168.250.6:8888/group1/M00/00/22/rKj6BWDw8TOAC-rhABs7QS-2AxA937.jpg");

        toPdf(dzUrl,"D:/Demo/pdf/hebing.pdf");

        long time2 = System.currentTimeMillis();

        int time = (int) ((time2 - time1)/1000);
        System.out.println("执行了："+time+"秒！");
*/


    }
}
