package com.zfsoft.single.util.poi;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/11/15 15:09
 */

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class Demo {
    public static void main(String[] args) throws Exception{

        //String path = "1.docx";
        String path = "E:\\poiImage\\resousce\\sltzs.docx";
        //String path = "rqsltzs.docx";
        //String path = "test2.docx";
        //InputStream templateFile = Demo.class.getClassLoader().getResourceAsStream(path);

        //GetValuesFromWord.getValuesFromWordTemplate(new FileInputStream(path));
        //File picture = new File("C:/Users/Administrator/Pictures/我的图片/neusoft.png");
        //BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
        //BufferedImage sourceImg = ImageIO.read(Demo.class.getClassLoader().getResourceAsStream("test.png"));
        Map map = new HashMap();
        //map.put("pic", new PictureRenderData(120, 80, ".png", Demo.class.getClassLoader().getResourceAsStream("1.png")));
        //int  width = sourceImg.getWidth()/10;
        //int height = sourceImg.getHeight()/10;
        int  width = 76;
        int height = 73;
        //map.put("pic", new PictureRenderData(width,height, ".png", Demo.class.getClassLoader().getResourceAsStream("E:\\poiImage\\resousce\\test.png")));
        map.put("pic", new PictureRenderData(width,height, ".png", new FileInputStream("E:\\poiImage\\resousce\\test.png")));
        // 将数据整合到模板中去
        Configure.ConfigureBuilder builder = Configure.newBuilder();
        builder.supportGrammerRegexForAll();
        //builder.addPlugin('@', new MyPictureRenderPolicy());
        //builder.addPlugin('@', new MyPictureRenderPolicyUpdate());
        //InputStream templateFile2 = Demo.class.getClassLoader().getResourceAsStream(path);
        InputStream templateFile2 =  new FileInputStream(path);
        XWPFTemplate template = XWPFTemplate.compile(templateFile2, builder.build()).render(map);

        //String docPath = "C:\\Users\\csdc01\\Desktop\\out.docx";
        String docPath = "E:\\poiImage\\20211115\\sltzs3.docx";
        FileOutputStream outputStream1 = new FileOutputStream(docPath);
        template.write(outputStream1);
        outputStream1.flush();
        outputStream1.close();
    }
}


