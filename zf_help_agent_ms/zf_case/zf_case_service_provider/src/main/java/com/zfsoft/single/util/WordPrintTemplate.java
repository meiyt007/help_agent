package com.zfsoft.single.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zhuozhengsoft.base64.Base64;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对docx文件中的文本及表格中的内容进行替换 --模板仅支持对 {key} 标签的替换
 *
 * @Author zhujiajian
 * @Description:
 * word模板注意页边距的问题，存在问题：比如页边距默认为3cm，画表格时，仍然可以通过
 * 拖拽，把表格边框拖动到看起来就像页边距只有1cm的样子，但是实际上此时页边距还是3cm，生成的
 * word报表的页边距还是会按照3cm来生成。解决办法，在word文件里，设置好页边距，如果需要表格
 * 两边页边距很窄，需要在word里设置页边距窄一点，而不是直接拖动表格边框来实现。
 * @Date: 2019年8月4日 下午3:25:56
 * @Copyright 上海卓繁信息技术股份有限公司版权所有 .
 */
public class WordPrintTemplate {
    private static Logger logger = LoggerFactory.getLogger(WordPrintTemplate.class);

    private XWPFDocument document;

    public XWPFDocument getDocument() {
        return document;
    }

    public void setDocument(XWPFDocument document) {
        this.document = document;
    }

    /**
     * 初始化模板内容
     *
     * @param inputStream 模板的读取流(docx文件)
     * @date 2019年8月4日 下午3:59:22
     */
    public WordPrintTemplate(InputStream inputStream) throws IOException {
        document = new XWPFDocument(inputStream);
    }

    /**
     * 将处理后的内容写入到输出流中
     *
     * @param outputStream
     */
    public void write(OutputStream outputStream) throws IOException {
        document.write(outputStream);
    }


    /**
     * 根据dataMap对word文件中的标签进行替换; <br><br>
     * ！！！！***需要注意dataMap的数据格式***！！！！ <br><br>
     * 对于需要替换的普通标签数据标签（不需要循环）-----直接将数据添加到dataMap中，
     * 来存储这些不需要循环生成的数据，比如：表头信息，日期，制表人等。 <br><br>
     * 对于需要循环生成的表格数据------key自定义，value为 --ArrayList<Map<String, String>>
     *
     * @param dataMap
     * @date 2019年8月4日 下午3:29:27
     */
    public void replaceDocument(Map<String, Object> dataMap) throws Exception {

        List<IBodyElement> bodyElements = document.getBodyElements();// 所有对象（段落+表格）
        int templateBodySize = bodyElements.size();// 标记模板文件（段落+表格）总个数

        int curT = 0;// 当前操作表格对象的索引
        int curP = 0;// 当前操作段落对象的索引
        for (int a = 0; a < templateBodySize; a++) {
            IBodyElement body = bodyElements.get(a);
            if (BodyElementType.TABLE.equals(body.getElementType())) {// 处理表格
                XWPFTable table = body.getBody().getTableArray(curT);

                List<XWPFTable> tables = body.getBody().getTables();
                table = tables.get(curT);
                if (table != null) {

                    // 处理表格
                    List<XWPFTableCell> tableCells = table.getRows().get(0).getTableCells();// 获取到模板表格第一行，用来判断表格类型
                    String tableText = table.getText();// 表格中的所有文本
                    List<Map<String, Object>> tableDataList = (List<Map<String, Object>>) dataMap.get("volist");
                    addTableInDocFooter(table, tableDataList, dataMap, 2);

                    /*if (tableText.indexOf("##{foreach") > -1) {
                        // 查找到##{foreach标签，该表格需要处理循环
                        if (tableCells.size() != 2
                                || tableCells.get(0).getText().indexOf("##{foreach") < 0
                                || tableCells.get(0).getText().trim().length() == 0) {
                            logger.info("文档中第{}}个表格模板错误,模板表格第一行需要设置2个单元格，第一个单元格存储表格类型(##{foreachTable}## 或者 ##{foreachTableRow}##)，第二个单元格定义数据源。", (curT + 1));
                            return;
                        }

                        String tableType = tableCells.get(0).getText();
                        String dataSource = tableCells.get(1).getText();
                        if (!dataMap.containsKey(dataSource)) {
                            logger.info("文档中第{}个表格模板数据源缺失", (curT + 1));
                            return;
                        }
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> tableDataList = (List<Map<String, Object>>) dataMap
                                .get(dataSource);
                        if ("##{foreachTable}##".equals(tableType)) {
                            addTableInDocFooter(table, tableDataList, dataMap, 1);

                        } else if ("##{foreachTableRow}##".equals(tableType)) {
                            addTableInDocFooter(table, tableDataList, dataMap, 2);
                        }

                    } else if (tableText.indexOf("{") > -1) {
                        // 没有查找到##{foreach标签，查找到了普通替换数据的{}标签，该表格只需要简单替换
                        addTableInDocFooter(table, null, dataMap, 3);
                    } else {
                        // 没有查找到任何标签，该表格是一个静态表格，仅需要复制一个即可。
                        addTableInDocFooter(table, null, null, 0);
                    }*/
                    curT++;

                }
            } else if (BodyElementType.PARAGRAPH.equals(body.getElementType())) {// 处理段落
                XWPFParagraph ph = body.getBody().getParagraphArray(curP);
                if (ph != null) {
                    // htmlText = htmlText+readParagraphX(ph);
                    addParagraphInDocFooter(ph, null, dataMap, 0);

                    curP++;
                }
            }

        }
        // 处理完毕模板，删除文本中的模板内容
        for (int a = 0; a < templateBodySize; a++) {
            document.removeBodyElement(0);
        }

    }

    /**
     * 根据 模板表格 和 数据list 在word文档末尾生成表格
     *
     * @param templateTable 模板表格
     * @param list          循环数据集
     * @param parametersMap 不循环数据集
     * @param flag          (0为静态表格，1为表格整体循环，2为表格内部行循环，3为表格不循环仅简单替换标签即可)
     * @date 2019年8月4日 上午10:12:05
     */
    public void addTableInDocFooter(XWPFTable templateTable, List<Map<String, Object>> list,
                                    Map<String, Object> parametersMap, int flag) throws Exception {

        if (flag == 1) {// 表格整体循环
            for (Map<String, Object> map : list) {
                List<XWPFTableRow> templateTableRows = templateTable.getRows();// 获取模板表格所有行
                CTTblGrid grid = templateTable.getCTTbl().getTblGrid();
                XWPFTable newCreateTable = document.createTable();// 创建新表格,默认一行一列
                //newCreateTable.getCTTbl().addNewTblGrid().
                for (int i = 1; i < templateTableRows.size(); i++) {
                    XWPFTableRow newCreateRow = newCreateTable.createRow();
                    CopyTableRow(newCreateRow, templateTableRows.get(i));// 复制模板行文本和样式到新行
                }
                newCreateTable.removeRow(0);// 移除多出来的第一行
                document.createParagraph();// 添加回车换行
                replaceTable(newCreateTable, map);//替换标签
            }

        } else if (flag == 2) {// 表格表格内部行循环
            XWPFTable newCreateTable = document.createTable();// 创建新表格,默认一行一列
            List<XWPFTableRow> TempTableRows = templateTable.getRows();// 获取模板表格所有行
            int tagRowsIndex = 0;// 标签行indexs
            /*for (int i = 0, size = TempTableRows.size(); i < size; i++) {
                String rowText = TempTableRows.get(i).getCell(0).getText();// 获取到表格行的第一个单元格
                if (rowText.indexOf("##{foreachRows}##") > -1) {
                    tagRowsIndex = i;
                    break;
                }
            }*/

            /* 复制模板行和标签行之前的行 */
            /*for (int i = 1; i < tagRowsIndex; i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, TempTableRows.get(i));// 复制行
                replaceTableRow(newCreateRow, parametersMap);// 处理不循环标签的替换
            }*/

            /* 循环生成模板行 */
            XWPFTableRow tempRow = TempTableRows.get(tagRowsIndex + 1);// 获取到模板行
            for (int i = 0; i < list.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, tempRow);// 复制模板行
                replaceTableRow(newCreateRow, list.get(i));// 处理标签替换
            }

            /* 复制模板行和标签行之后的行 */
            /*for (int i = tagRowsIndex + 2; i < TempTableRows.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, TempTableRows.get(i));// 复制行
                replaceTableRow(newCreateRow, parametersMap);// 处理不循环标签的替换
            }*/
            newCreateTable.removeRow(0);// 移除多出来的第一行
            document.createParagraph();// 添加回车换行

        } else if (flag == 3) {
            //表格不循环仅简单替换标签
            List<XWPFTableRow> templateTableRows = templateTable.getRows();// 获取模板表格所有行
            CTTblGrid tempGrid = templateTable.getCTTbl().getTblGrid();
            XWPFTable newCreateTable = document.createTable();// 创建新表格,默认一行一列
            CTTblGrid grid = newCreateTable.getCTTbl().addNewTblGrid();
            //grid.
            for (int i = 0; i < templateTableRows.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, templateTableRows.get(i));// 复制模板行文本和样式到新行
            }
            newCreateTable.removeRow(0);// 移除多出来的第一行
            document.createParagraph();// 添加回车换行
            replaceTable(newCreateTable, parametersMap);

        } else if (flag == 0) {
            List<XWPFTableRow> templateTableRows = templateTable.getRows();// 获取模板表格所有行
            XWPFTable newCreateTable = document.createTable();// 创建新表格,默认一行一列
            for (int i = 0; i < templateTableRows.size(); i++) {
                XWPFTableRow newCreateRow = newCreateTable.createRow();
                CopyTableRow(newCreateRow, templateTableRows.get(i));// 复制模板行文本和样式到新行
            }
            newCreateTable.removeRow(0);// 移除多出来的第一行
            document.createParagraph();// 添加回车换行
        }

    }


    /**
     * 根据 模板段落 和 数据 在文档末尾生成段落
     *
     * @param templateParagraph 模板段落
     * @param list              循环数据集
     * @param parametersMap     不循环数据集
     * @param flag              (0为不循环替换，1为循环替换)
     * @date 2019年8月4日 上午11:49:42
     */
    public void addParagraphInDocFooter(XWPFParagraph templateParagraph,
                                        List<Map<String, String>> list, Map<String, Object> parametersMap, int flag) throws Exception {

        if (flag == 0) {
            //分页符，在最后位置插入分页符
            if (templateParagraph.getRuns().size() == 1) {
                XWPFRun xwpfRun = templateParagraph.getRuns().get(0);
                if ("\n".equals(xwpfRun.text())) {
                    document.setParagraph(templateParagraph, document.getParagraphs().size() - 1);
                    return;
                    //createParagraph.setPageBreak(true);
                }
            }
            XWPFParagraph createParagraph = document.createParagraph();

            // 设置段落样式
            createParagraph.getCTP().setPPr(templateParagraph.getCTP().getPPr());
            // 移除原始内容
            for (int pos = 0; pos < createParagraph.getRuns().size(); pos++) {
                createParagraph.removeRun(pos);
            }
            // 添加Run标签
            for (XWPFRun s : templateParagraph.getRuns()) {
                XWPFRun targetrun = createParagraph.createRun();
                CopyRun(targetrun, s);
            }

            replaceParagraph(createParagraph, parametersMap);

        } else if (flag == 1) {
            // 暂无实现
        }

    }


    /**
     * 根据map替换段落元素内的{**}标签
     *
     * @param xWPFParagraph
     * @param parametersMap
     * @date 2019年8月4日 下午3:09:00
     */
    public void replaceParagraph(XWPFParagraph xWPFParagraph, Map<String, Object> parametersMap) throws Exception {
        List<XWPFRun> runs = xWPFParagraph.getRuns();
        String xWPFParagraphText = xWPFParagraph.getText();
        String regEx = "\\{.+?\\}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(xWPFParagraphText);//正则匹配字符串{****}

        if (matcher.find()) {
            // 查找到有标签才执行替换
            int beginRunIndex = xWPFParagraph.searchText("{", new PositionInParagraph()).getBeginRun();// 标签开始run位置
            int endRunIndex = xWPFParagraph.searchText("}", new PositionInParagraph()).getEndRun();// 结束标签
            StringBuffer key = new StringBuffer();

            if (beginRunIndex == endRunIndex) {
                // {**}在一个run标签内
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();

                int beginIndex = beginRunText.indexOf("{");
                int endIndex = beginRunText.indexOf("}");
                int length = beginRunText.length();

                if (beginIndex == 0 && endIndex == length - 1) {
                    // 该run标签只有{**}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    key.append(beginRunText.substring(1, endIndex));
                    //insertNewRun.setText(getValueBykey(key.toString(), parametersMap));
                    runReplcaeHandle(key.toString(), insertNewRun, parametersMap, null);

                    xWPFParagraph.removeRun(beginRunIndex + 1);
                } else {
                    // 该run标签为**{**}** 或者 **{**} 或者{**}**，替换key后，还需要加上原始key前后的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    key.append(beginRunText.substring(beginRunText.indexOf("{") + 1, beginRunText.indexOf("}")));

                    runReplcaeHandle(key.toString(), insertNewRun, parametersMap, beginRunText);

                    xWPFParagraph.removeRun(beginRunIndex + 1);
                }

            } else {
                // {**}被分成多个run

                //先处理起始run标签,取得第一个{key}值
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();
                int beginIndex = beginRunText.indexOf("{");
                if (beginRunText.length() > 1) {
                    key.append(beginRunText.substring(beginIndex + 1));
                }
                ArrayList<Integer> removeRunList = new ArrayList<>();//需要移除的run
                //处理中间的run
                for (int i = beginRunIndex + 1; i < endRunIndex; i++) {
                    XWPFRun run = runs.get(i);
                    String runText = run.text();
                    key.append(runText);
                    removeRunList.add(i);
                }

                // 获取endRun中的key值
                XWPFRun endRun = runs.get(endRunIndex);
                String endRunText = endRun.text();
                int endIndex = endRunText.indexOf("}");
                //run中**}或者**}**
                if (endRunText.length() > 1 && endIndex != 0) {
                    key.append(endRunText.substring(0, endIndex));
                }


                //*******************************************************************
                //取得key值后替换标签

                //先处理开始标签
                if (beginRunText.length() == 2) {
                    // run标签内文本{
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    //insertNewRun.setText(getValueBykey(key.toString(), parametersMap));
                    runReplcaeHandle(key.toString(), insertNewRun, parametersMap, null);
                    xWPFParagraph.removeRun(beginRunIndex + 1);//移除原始的run
                } else {
                    // 该run标签为**{**或者 {** ，替换key后，还需要加上原始key前的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());

                    runReplcaeHandle(key.toString(), insertNewRun, parametersMap, beginRunText);
                    xWPFParagraph.removeRun(beginRunIndex + 1);//移除原始的run
                }

                //处理结束标签
                if (endRunText.length() == 1) {
                    // run标签内文本只有}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // 设置文本
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(endRunIndex + 1);//移除原始的run

                } else {
                    // 该run标签为**}**或者 }** 或者**}，替换key后，还需要加上原始key后的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // 设置文本
                    String textString = endRunText.substring(endRunText.indexOf("}") + 1);
                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(endRunIndex + 1);//移除原始的run
                }

                //处理中间的run标签
                for (int i = 0; i < removeRunList.size(); i++) {
                    XWPFRun xWPFRun = runs.get(removeRunList.get(i));//原始run
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(removeRunList.get(i));
                    insertNewRun.getCTR().setRPr(xWPFRun.getCTR().getRPr());
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(removeRunList.get(i) + 1);//移除原始的run
                }

            }// 处理${**}被分成多个run
            //del digui
            replaceParagraph(xWPFParagraph, parametersMap);

        }//if 有标签

    }

    /**
     * 文本替换
     *
     * @param key           取值key
     * @param insertNewRun  新的文本段落
     * @param parametersMap 替换值
     * @param beginRunText  内部文本值
     */
    private void runReplcaeHandle(String key, XWPFRun insertNewRun, Map<String, Object> parametersMap, String beginRunText) throws Exception {
        if(key.indexOf("sign:") == 0) {
            // 设置文本
            if(StrUtil.isNotBlank(beginRunText) && beginRunText.indexOf("{") > 0) {
                String textString = beginRunText.substring(0, beginRunText.indexOf("{"));
                insertNewRun.setText(textString);
            }

            ByteArrayInputStream stream = null;
            byte[] bytes1 = cn.hutool.core.codec.Base64.decode(String.valueOf(getValueBykey("sign:Base64", parametersMap)));
            stream = new ByteArrayInputStream(bytes1);

            String widthHeight = key.split("[:]")[1];
            int signWidth = 0, signHeight= 40;
            if(StrUtil.isNotBlank(widthHeight)) {
                String[] wh = widthHeight.split("[/*]");
                if(wh.length == 2) {
                    signWidth = Integer.parseInt(wh[0]);
                    signHeight = Integer.parseInt(wh[1]);
                } else {
                    signHeight = Integer.parseInt(wh[0]);
                }
            }
            if(signWidth == 0) {
                BufferedImage sourceImg = ImageIO.read(new ByteArrayInputStream(bytes1));
                //按照比例设置图片的宽高
                if(sourceImg!=null){
                    signWidth = sourceImg.getWidth() * signHeight / sourceImg.getHeight();
                }
            }

            insertNewRun.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "sign.png", Units.toEMU(signWidth), Units.toEMU(signHeight));
            stream.close();

            // 设置文本
            /*if(StrUtil.isNotBlank(beginRunText) && beginRunText.indexOf("}") > 0) {
                String textString = beginRunText.substring(beginRunText.indexOf("}") + 1);
                if(textString!="" && textString.length()!=0){
                    insertNewRun.setText(textString, insertNewRun.text().length());
                }
            }*/
        } else if(key.indexOf("img-") == 0) {
            //img-GongMinShenFenHaoMa-tiaoxingma-268-98
            String[] arrImaInfo = key.split("-");
//            String realKey = parametersMap.get(arrImaInfo[1]) == null ? "" : parametersMap.get(arrImaInfo[1]).toString();
            String imgType = arrImaInfo[2];
            Integer width = Integer.valueOf(arrImaInfo[3]);
            Integer height = Integer.valueOf(arrImaInfo[4]);
            String realValue = getValueBykey(arrImaInfo[1], parametersMap);
            if(StrUtil.isBlank(realValue)) {
                return;
            }
            String imgBase = "";
            if("tiaoxingma".equals(imgType)) {
               // imgBase = BarCodeUtil.encode(realValue, width, height);
            } else if("erweima".equals(imgType)) {
                BitMatrix bitMatrix = QrCodeUtil.encode(realValue, width, height);
                BufferedImage image= MatrixToImageWriter.toBufferedImage(bitMatrix);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png",outputStream);
                imgBase = Base64.encodeBase64String(outputStream.toByteArray());
                outputStream.close();
            }
            ByteArrayInputStream stream = null;
            byte[] bytes1 = cn.hutool.core.codec.Base64.decode(imgBase);
            stream = new ByteArrayInputStream(bytes1);
            insertNewRun.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, IdUtil.fastUUID() +".png", Units.toEMU(width), Units.toEMU(height));
            stream.close();
        }else if(key.indexOf("copyQueue:") == 0){
            String[] arrImaInfo = key.split(":");
            // 设置文本
            List<String> list= (List<String>) parametersMap.get(arrImaInfo[1]);
            XWPFParagraph targetPar=insertNewRun.getParagraph();
            if (list != null && list.size() > 0) {
                XWPFRun xwpfRun = null;
                for (int i = 0; i <list.size(); i++) {
                    XWPFRun run = targetPar.createRun();
                    run.setText(list.get(i));
                    //换行
                    run.addBreak();
                    //缩进
                    run.addTab();
                }
            }
        }else if(key.contains("seal")){
            // 设置文本
            StringBuffer textString = new StringBuffer();
            //textString.append(getValueBykey(key, parametersMap));
            //textString.append("{");
            textString.append(key.replaceAll("\\{","").replaceAll("\\}",""));
            //textString.append("}}");
            insertNewRun.setText(textString.toString(),0);
        }else if(key.indexOf("seat|") == 0){
            // 设置文本
            StringBuffer textString = new StringBuffer();
            //textString.append("{{");
            textString.append(key);
            //textString.append("}}");
            insertNewRun.setText(textString.toString(),0);
        }
        else {
            // 设置文本
            StringBuffer textString = new StringBuffer();
            if (StrUtil.isNotBlank(beginRunText) && beginRunText.indexOf("{") > 0) {
                textString.append(beginRunText.substring(0, beginRunText.indexOf("{")));
            }
            textString.append(getValueBykey(key, parametersMap));
            if (StrUtil.isNotBlank(beginRunText) && beginRunText.indexOf("}") > 0) {
                textString.append(beginRunText.substring(beginRunText.indexOf("}") + 1));
            }
            insertNewRun.setText(textString.toString());
        }
    }

    /**
     * 复制表格行XWPFTableRow格式
     *
     * @param target 待修改格式的XWPFTableRow
     * @param source 模板XWPFTableRow
     */
    private void CopyTableRow(XWPFTableRow target, XWPFTableRow source) {

        int tempRowCellsize = source.getTableCells().size();// 模板行的列数
        for (int i = 0; i < tempRowCellsize - 1; i++) {
            target.addNewTableCell();// 为新添加的行添加与模板表格对应行行相同个数的单元格
        }
        // 复制样式
        target.getCtRow().setTrPr(source.getCtRow().getTrPr());
        // 复制单元格
        for (int i = 0; i < target.getTableCells().size(); i++) {
            copyTableCell(target.getCell(i), source.getCell(i));
        }
    }


    /**
     * 复制单元格XWPFTableCell格式
     *
     * @param newTableCell      新创建的的单元格
     * @param templateTableCell 模板单元格
     * @date 2019年8月4日 下午3:41:02
     */
    private void copyTableCell(XWPFTableCell newTableCell, XWPFTableCell templateTableCell) {
        // 列属性
        newTableCell.getCTTc().setTcPr(templateTableCell.getCTTc().getTcPr());
        // 删除目标 targetCell 所有文本段落
        for (int pos = 0; pos < newTableCell.getParagraphs().size(); pos++) {
            newTableCell.removeParagraph(pos);
        }
        // 添加新文本段落
        for (XWPFParagraph sp : templateTableCell.getParagraphs()) {
            XWPFParagraph targetP = newTableCell.addParagraph();
            copyParagraph(targetP, sp);
        }
    }

    /**
     * 复制文本段落XWPFParagraph格式
     *
     * @param newParagraph      新创建的的段落
     * @param templateParagraph 模板段落
     * @date 2019年8月4日 下午3:43:08
     */
    private void copyParagraph(XWPFParagraph newParagraph, XWPFParagraph templateParagraph) {
        // 设置段落样式
        newParagraph.getCTP().setPPr(templateParagraph.getCTP().getPPr());
        // 添加Run标签
        for (int pos = 0; pos < newParagraph.getRuns().size(); pos++) {
            newParagraph.removeRun(pos);

        }
        for (XWPFRun s : templateParagraph.getRuns()) {
            XWPFRun targetrun = newParagraph.createRun();
            CopyRun(targetrun, s);
        }

    }

    /**
     * 复制文本节点run
     *
     * @param newRun      新创建的的文本节点
     * @param templateRun 模板文本节点
     * @date 2019年8月4日 下午3:47:17
     */
    private void CopyRun(XWPFRun newRun, XWPFRun templateRun) {
        newRun.getCTR().setRPr(templateRun.getCTR().getRPr());
        // 设置文本
        newRun.setText(templateRun.text());


    }


    /**
     * 根据参数parametersMap对表格的一行进行标签的替换
     *
     * @param tableRow      表格行
     * @param parametersMap 参数map
     * @date 2019年8月4日 下午2:09:24
     */
    public void replaceTableRow(XWPFTableRow tableRow, Map<String, Object> parametersMap) throws Exception {

        List<XWPFTableCell> tableCells = tableRow.getTableCells();
        for (XWPFTableCell xWPFTableCell : tableCells) {
            List<XWPFParagraph> paragraphs = xWPFTableCell.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphs) {

                replaceParagraph(xwpfParagraph, parametersMap);
            }
        }

    }


    /**
     * 根据map替换表格中的{key}标签
     *
     * @param xwpfTable
     * @param parametersMap
     * @date 2019年8月4日 下午2:47:36
     */
    public void replaceTable(XWPFTable xwpfTable, Map<String, Object> parametersMap) throws Exception {
        List<XWPFTableRow> rows = xwpfTable.getRows();
        for (XWPFTableRow xWPFTableRow : rows) {
            List<XWPFTableCell> tableCells = xWPFTableRow.getTableCells();
            for (XWPFTableCell xWPFTableCell : tableCells) {
                List<XWPFParagraph> paragraphs2 = xWPFTableCell.getParagraphs();
                for (XWPFParagraph xWPFParagraph : paragraphs2) {
                    replaceParagraph(xWPFParagraph, parametersMap);
                }
            }
        }

    }

    private String getValueBykey(String key, Map<String, Object> map) {
        String returnValue = "";
        if (key != null) {
            try {
                key = key.trim();
                if (key.indexOf("check:") == 0) {
                    //选择框
                    String checkKey = key.substring(6, key.length()).trim();
                    String checkValue = map.get(checkKey) != null ? map.get(checkKey).toString() : "";
                    if (StrUtil.isBlank(checkValue)) {
                        returnValue = "□";
                    } else {
                        returnValue = "√";
                    }
                } else if (key.indexOf("selected:") == 0) {
                    //选择框
                    String checkKey = key.substring(9, key.length()).trim();
                    String checkValue = map.get(checkKey) != null ? map.get(checkKey).toString() : "";
                    if (StrUtil.isBlank(checkValue)) {
                        returnValue = "□";
                    } else {
                        returnValue = "√";
                    }
                } else {
                    returnValue = map.get(key) != null ? map.get(key).toString() : "";
                }
            } catch (Exception e) {
                returnValue = "";
            }

        }
        return returnValue;
    }



    /**
     * 根据Word模板以及数据生成Word文档
     * @Author dusd
     * @Date 2019/11/14 18:28
     * @return
     */
    public static String genWordByTemplate(String targetWordPath, Map<String, Object> dataMap,String sourceWordPath) throws Exception {
        FileInputStream fileInputStream = null;
        FileOutputStream fos = null;
        try {
            //创建返回文件
            if(targetWordPath !=null) {
                File fileInfo = new File(targetWordPath);
                if (!fileInfo.exists()) {
                    fileInfo.mkdirs();// 能创建多级目录
                }
            }
            // 读取word模板
            WordPrintTemplate template = new WordPrintTemplate( new FileInputStream(sourceWordPath));
            // 替换数据
            template.replaceDocument(dataMap);

            String fileName=System.currentTimeMillis()+".docx";
            // 生成文件
            fos = new FileOutputStream(new File(targetWordPath+"/"+fileName));
            template.getDocument().write(fos);

            return targetWordPath+"/"+fileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream != null) {
                fileInputStream.close();
            }
            if(fos != null) {
                fos.flush();
                fos.close();
            }
        }
        return null;
    }


    /**
     * byte[] 转 FileInputStream
     * @Author dusd
     * @Date 2019/11/15 11:06
     * @param
     * @return
     */
    public static FileInputStream byteToFile(byte[] bytes, String fileName) {
        File file = new File(fileName);
        FileInputStream fileInputStream = null;
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            fileInputStream = new FileInputStream(file);
            file.deleteOnExit();
            return fileInputStream;
        } catch (Exception e) {
            logger.error("字节数组转文件失败", e);
        } finally {
            if(output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileInputStream;
    }

}
