package com.zfsoft.single.util.poi;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.zfsoft.single.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/11/16 9:38
 */
@Slf4j
public class PoiTlUtils {

    public  static String poiTl(String targetWordPath,String sourceWordPath,String imgPath,String noPstPath){
        FileOutputStream outputStream1 = null;
        InputStream templateFile2 = null;
        InputStream imageInput = null;
        InputStream imageInput2 = null;
        boolean isSuccess  = true;
        try {
            //创建返回文件
/*            if(targetWordPath !=null) {
                File fileInfo = new File(targetWordPath);
                if (!fileInfo.exists()) {
                    fileInfo.mkdirs();// 能创建多级目录
                }
            }*/
            String fileName=System.currentTimeMillis()+".docx";
            //String path = "1.docx";
            //String path = "E:\\poiImage\\resousce\\sltzs.docx";
            //String path = "rqsltzs.docx";
            //String path = "test2.docx";
            //GetValuesFromWord.getValuesFromWordTemplate(new FileInputStream(path));
            Map<String,String>  pstValueMp =  GetValuesFromWord.getValuesFromWordTemplate(sourceWordPath,noPstPath);
            String  pstValue =  pstValueMp.get("seat");
            if(StringUtils.isEmpty(pstValue)){
                isSuccess  = false;
                return sourceWordPath;
            }
            String[]  pstValuesArray = pstValue.replaceAll("\\{","").replaceAll("\\}","").split("\\|");
            //读取word文档里面的pst即一些参数，将印章图片替换。并且删除pst数据。示例数据 {pst|22|44|123}， 第一位，是positionH 偏移量，第二位 是positionV偏移量，
            //第三位 是  印章图片高。
            long positionH = Long.parseLong(pstValuesArray[1]);
            long positionV = Long.parseLong(pstValuesArray[2]);
            int high =  Integer.parseInt(pstValuesArray[3]);
            //File picture = new File("C:/Users/Administrator/Pictures/我的图片/neusoft.png");
            //BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
            imageInput = Demo.class.getClassLoader().getResourceAsStream("templates"+File.separator+"test.png");
            //BufferedImage sourceImg = ImageIO.read(Demo.class.getClassLoader().getResourceAsStream("templates\\test.png"));
            BufferedImage sourceImg = ImageIO.read(imageInput);
            Map map = new HashMap();
            //map.put("pic", new PictureRenderData(120, 80, ".png", Demo.class.getClassLoader().getResourceAsStream("1.png")));
            //int  width = sourceImg.getWidth()/10;
            //int height = sourceImg.getHeight()/10;

            int height = high;

            int width  = sourceImg.getWidth() * height / sourceImg.getHeight();

            System.out.println("width == "+width + "height == "+height);
            //int  width = 76;
            //int height = 73;
            //map.put("pic", new PictureRenderData(width,height, ".png", Demo.class.getClassLoader().getResourceAsStream("E:\\poiImage\\resousce\\test.png")));
            //map.put("pic", new PictureRenderData(width,height, ".png", new FileInputStream("E:\\poiImage\\resousce\\test.png")));
            System.out.println();
            //imageInput2 = Demo.class.getClassLoader().getResourceAsStream("/templates/test.png");
            //System.out.println(Demo.class.getClass().getResource("/test.png"));
            imageInput2 = Demo.class.getClassLoader().getResourceAsStream("templates"+File.separator+"test.png");
            map.put("seal", new PictureRenderData(width,height, ".png",imageInput2));
            // 将数据整合到模板中去
            Configure.ConfigureBuilder builder = Configure.newBuilder();
            builder.supportGrammerRegexForAll();
            //builder.addPlugin('@', new MyPictureRenderPolicy());
            //builder.addPlugin('@', new MyPictureRenderPolicyUpdate());
            builder.addPlugin('@', new MyPictureRenderPolicyUpdate(positionH,positionV));
            //InputStream templateFile2 = Demo.class.getClassLoader().getResourceAsStream(path);
            //InputStream templateFile2 =  new FileInputStream(sourceWordPath);
            templateFile2 =  new FileInputStream(noPstPath);
            XWPFTemplate template = XWPFTemplate.compile(templateFile2, builder.build()).render(map);

            //String docPath = "C:\\Users\\csdc01\\Desktop\\out.docx";
            //String docPath = "E:\\poiImage\\20211115\\sltzs3.docx";
            //FileOutputStream outputStream1 = new FileOutputStream(docPath);
            outputStream1 = new FileOutputStream(targetWordPath);
            template.write(outputStream1);
            outputStream1.flush();
            //outputStream1.close();
        }catch (Exception e){
            System.out.println("poiTl error "+ e.getMessage());
            log.error("poiTl error "+ e.getMessage());
        }finally {
            try {
                if(outputStream1!=null){
                    outputStream1.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(templateFile2!=null){
                    templateFile2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(imageInput!=null){
                    imageInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(imageInput2!=null){
                    imageInput2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!isSuccess){
            return sourceWordPath;
        }
        return targetWordPath;
    }

    public static void main(String[] args) {
        //String sourceWordPath  = "E:\\\\poiImage\\\\resousce\\\\sltzs.docx";
        //String sourceWordPath  = "E:\\\\poiImage\\\\resousce\\\\sltzs20211118_2.docx";
        //String sourceWordPath  = "E:\\poiImage\\resousce\\20211118\\sltzs.docx";
        String sourceWordPath  = "E:\\poiImage\\resousce\\20211118\\1637213896978.docx";
        //String sourceWordPath  = "E:\\\\poiImage\\\\resousce\\\\sltzs_zdl.docx";
        //String sourceWordPath  = "E:\\\\poiImage\\\\resousce\\\\sltzs_sky.docx";
        //String targetWordPath  = "E:\\poiImage\\20211116\\sltzs_2.docx";
        //String targetWordPath  = "E:\\poiImage\\20211116\\sltzs_zdl.docx";
        String targetWordPath  = "E:\\poiImage\\20211116\\sltzs_sky.docx";
        //String noPstPath = "E:\\poiImage\\20211116\\noPstPath\\sltzs_zdl.docx";
        String noPstPath = "E:\\poiImage\\20211116\\noPstPath\\sltzs_sky.docx";
        String result = poiTl(targetWordPath,sourceWordPath,"",noPstPath);
        System.out.println("result ------ "+result);
    }

}
