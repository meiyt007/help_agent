package com.zfsoft.single.util.poi;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/11/15 15:11
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/11/15 9:40
 */
@Slf4j
public class GetValuesFromWord {
    public static Map<String,String> getValuesFromWordTemplate(String  sourceWordPath,String targetPath){
        Map<String,String>  values = new HashMap<>();
        OutputStream os3  = null;
        try {
            //创建返回文件
/*            if(targetPath !=null) {
                File fileInfo = new File(targetPath);
                if (!fileInfo.exists()) {
                    fileInfo.mkdirs();// 能创建多级目录
                }
            }*/
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceWordPath));
            //XWPFDocument document = new XWPFDocument(in);
            List<IBodyElement> bodyElements = document.getBodyElements();// 所有对象（段落+表格）
            int templateBodySize = bodyElements.size();// 标记模板文件（段落+表格）总个数
            String regEx = "\\{.+?\\}";
            Pattern pattern = Pattern.compile(regEx);
            //Matcher matcher = pattern.matcher(xWPFParagraphText);//正则匹配字符串{****}
            for (int a = 0; a < templateBodySize; a++) {
                IBodyElement body = bodyElements.get(a);
                if (BodyElementType.TABLE.equals(body.getElementType())) {// 处理表格
                    List<XWPFTable> tables = body.getBody().getTables();

                } else if (BodyElementType.PARAGRAPH.equals(body.getElementType())) {// 处理段落
                    List<XWPFParagraph> paragraphs  = body.getBody().getParagraphs();
                    for (XWPFParagraph paragraph: paragraphs){
                        //System.out.println(paragraph.getText());
                        Matcher matcher = pattern.matcher(paragraph.getText());//正则匹配字符串{****}
                        //System.out.println("paragraph text--  "+ paragraph.getText());
                        //if(matcher.find()){
                            List<XWPFRun> lists = paragraph.getRuns();
                            for(XWPFRun xWPFRun : lists){
                                if(xWPFRun.text().contains("seat")){
                                    System.out.println("seat----  "+xWPFRun.text());
                                    values.put("seat",xWPFRun.text());
                                    //清空
                                    //xWPFRun.setText("test-------ttttt");
                                    xWPFRun.setText("",0);
                                }
                                if(xWPFRun.text().contains("seal")){
                                    xWPFRun.setText("{{@seal}}",0);
                                    System.out.println("seal----  "+xWPFRun.text());
                                }
                            }

                        //}
                    }
                }
            }
            //OutputStream os3 = new FileOutputStream("D:\\simpleWrite.docx");
            //OutputStream os3  = new FileOutputStream("E:\\poiImage\\20211115\\sltzsDel_4.docx");
            os3  = new FileOutputStream(targetPath);
            document.write(os3);
            os3.close();
            //this.close(os);
        } catch (Exception e) {
            e.printStackTrace();
            //添加  打错误日志程序。
            System.out.println("GetValuesFromWord error  "+e.getMessage());
            log.error("GetValuesFromWord error  "+e.getMessage());
        }finally {
            try {
                if(os3!=null){
                    os3.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return values;
    }
}
