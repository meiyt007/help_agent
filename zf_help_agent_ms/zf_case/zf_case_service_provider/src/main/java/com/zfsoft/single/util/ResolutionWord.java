package com.zfsoft.single.util;

import com.zfsoft.single.data.ywbl.vo.WordParamVo;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class ResolutionWord {

    public static final String RUN_NODE_NAME = "w:r";
    public static final String TEXT_NODE_NAME = "w:t";
    public static final String BOOKMARK_START_TAG = "w:bookmarkStart";
    public static final String BOOKMARK_END_TAG = "w:bookmarkEnd";
    public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
    public static final String STYLE_NODE_NAME = "w:rPr";
    // 给生成的表格设置样式
    public static void setCellWitchAndAlign(XWPFTableCell cell, String width, STVerticalJc.Enum typeEnum, STJc.Enum align){
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(typeEnum);
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(align);
        CTTblWidth ctTblWidth = (ctPr != null && ctPr.isSetTcW() && ctPr.getTcW()!=null &&ctPr.getTcW().getW()!=null) ? ctPr.getTcW(): ctPr.addNewTcW();
        if(StringUtils.isNotBlank(width)){
            ctTblWidth.setW(new BigInteger(width));
            ctTblWidth.setType(STTblWidth.DXA);

        }
    }

    //文件必须为07版以上的docx的文件，否则会报can't not spec 问题

    /**
     *
     * @param response
     * @param list 表格替换字符
     * @param map 文本段落替换
     * @param file 文件模板位置
     * @param outPath 输出位置
     *   twocode 生成二维码信息
     * @return
     */
    public static String DownloadWord1(HttpServletResponse response, List<WordParamVo> list, Map<String,Object> twocode, Map<String, String> map, String file, String outPath){
        //创建返回文件
        if(outPath !=null) {
            File fileInfo = new File(outPath);
            if (!fileInfo.exists()) {
                fileInfo.mkdirs();// 能创建多级目录
            }
        }
        XWPFDocument document = null;
        ByteArrayOutputStream ostream = null;
        //添加表格
        try {
            ostream = new ByteArrayOutputStream();
            //读取word模板
            document = new XWPFDocument(POIXMLDocument.openPackage(file));// 生成word文档并读取模板

            //段落
            //1. 替换段落中的指定文字
            Iterator itPara = document.getParagraphsIterator();
            String text;
            Set set = map.keySet();
            List<XWPFRun> run=null;
            while (itPara.hasNext()) {
                XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                run = paragraph.getRuns();
                Iterator iterator = set.iterator();
                if(run.size()>0 && set.size()>0){
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    for (int i = 0, runSie = run.size(); i < runSie; i++) {
                        text = run.get(i).getText(run.get(i).getTextPosition());
                        if(text != null && text!="" && text.contains(key)) {
                            String temText=text.replace(key,map.get(key));
                            run.get(i).setText(temText,0);
                        }
                    }
                }
                }
            }

            Iterator<XWPFTable> it = document.getTablesIterator();
            //附表格
           // XWPFTable ComTable = document.createTable();
            while(it.hasNext()){
                XWPFTable ComTable = it.next();
                // 注意：这边是列数较少固定的，如果列数不固定可循环创建上面的列数
                for (int i=0;i < list.size();i++) {
                    XWPFTableRow comTableRow = ComTable.createRow();
                    // 表格内容的填充
                    WordParamVo vo = list.get(i);
                    comTableRow.getCell(0).setText(((Integer)(i+1)).toString());
                    comTableRow.getCell(1).setText(vo.getMaterialName());
                    // comTableRow.getCell(2).setText(vo.getMaterialType());
                    comTableRow.getCell(2).setText(vo.getMaterialFormat());
                    comTableRow.getCell(3).setText(vo.getNum());
                    // 表格内容剧中+单元格大小设置
                    setCellWitchAndAlign(comTableRow.getCell(0),"700",STVerticalJc.CENTER,STJc.CENTER);
                    setCellWitchAndAlign(comTableRow.getCell(1),"3600",STVerticalJc.CENTER,STJc.CENTER);
                    // setCellWitchAndAlign(comTableRow.getCell(2),"1200",STVerticalJc.CENTER,STJc.LEFT);
                    setCellWitchAndAlign(comTableRow.getCell(2),"3600",STVerticalJc.CENTER,STJc.LEFT);
                    setCellWitchAndAlign(comTableRow.getCell(3),"700",STVerticalJc.CENTER,STJc.LEFT);
                }
            }
            String fileName = ""+System.currentTimeMillis();
            fileName += ".doc";
            FileOutputStream out = new FileOutputStream(outPath+"/"+fileName,true);
            document.write(ostream);
            //输出字节流
            out.write(ostream.toByteArray());
            return outPath+"/"+fileName;
        } catch (Exception e1) {
            e1.printStackTrace();
        }finally{
            try{
                if(ostream != null){
                    ostream.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }


    //书签替换文字
    public static void refreshBooksWord(XWPFDocument doc, Map<String, String> dataMap) {
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph xwpfParagraph : paragraphs) {
            CTP ctp = xwpfParagraph.getCTP();

            for (int dwI = 0; dwI < ctp.sizeOfBookmarkStartArray(); dwI++) {
                CTBookmark bookmark = ctp.getBookmarkStartArray(dwI);

                String data = dataMap.get(bookmark.getName());
                if (data != null) {
                    XWPFRun run = xwpfParagraph.createRun();
                    run.setText(data);

                    Node firstNode = bookmark.getDomNode();
                    Node nextNode = firstNode.getNextSibling();
                    while (nextNode != null) {
                        String nodeName = nextNode.getNodeName();
                        if (nodeName.equals(BOOKMARK_END_TAG)) {
                            break;
                        }
                        // 删除中间的非结束节点，即删除原书签内容
                        Node delNode = nextNode;
                        nextNode = nextNode.getNextSibling();

                        ctp.getDomNode().removeChild(delNode);
                    }

                    if (nextNode == null) {
                        // 始终找不到结束标识的，就在书签前面添加
                        ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), firstNode);
                    } else {
                        // 找到结束符，将新内容添加到结束符之前，即内容写入bookmark中间
                        ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
                    }
                }
            }
        }

    }

    //书签替换图片
    public static void refreshBooksImg(XWPFDocument doc, Map<String, InputStream> dataMap) throws IOException, InvalidFormatException {
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph xwpfParagraph : paragraphs) {
            CTP ctp = xwpfParagraph.getCTP();

            for (int dwI = 0; dwI < ctp.sizeOfBookmarkStartArray(); dwI++) {
                CTBookmark bookmark = ctp.getBookmarkStartArray(dwI);

                System.out.println(bookmark.getName()+"-----");
                InputStream picIs = dataMap.get(bookmark.getName());
                if(picIs != null){
                    XWPFRun run = xwpfParagraph.createRun();
                    byte [] picbytes = IOUtils.toByteArray(picIs);
                    doc.addPictureData(picIs,XWPFDocument.PICTURE_TYPE_PNG);
                    //bus.png为鼠标在word里选择图片时，图片显示的名字，400，400则为像素单元，根据实际需要的大小进行调整即可。
                   // run.addPicture(picbytes,XWPFDocument.PICTURE_TYPE_PNG,"1630301834(1)", Units.toEMU(68), Units.toEMU(68));
                }
            }
        }

    }

    public static String DownloadWord(HttpServletResponse response, List<WordParamVo> list,Map<String,Object> twocode, Map<String, String> map, String file,String outPath){
        try {
            FileInputStream is = new FileInputStream("D:/templates/gzs.docx");
            XWPFDocument doc = new XWPFDocument(is);

            Map<String,InputStream> dataMap = new HashMap<>();
            dataMap.put("PO_matrixc1",new FileInputStream("E:/002.png"));
            refreshBooksImg(doc,dataMap);
           /* Map<String,String> dataMap = new HashMap<>();
            dataMap.put("PO_windowNo","大白");
            refreshBooksWord(doc,dataMap);*/

            doc.write(new FileOutputStream("e:/test01.docx"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
