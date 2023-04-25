package com.zfsoft.service.util.office;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Excel导出工具类
 * @author yuy
 * @date 2021-2-9
 */
public class ExportExcelUtil {

   /* private HSSFWorkbook workbook = new HSSFWorkbook(); //工作簿对象

    *//**
     * 如果是多个文件压缩导出,则需要提前配置此文件
     *//*
    private String filepath = "";

    //如果是单个文件导出,则需要添加导出文件名
    private String fileName = "";


    *//**
     * 导出数据
     *//*
    public void export(HttpServletResponse response, String title, String[] rowName, List<Object[]> dataList){
        try {
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建工作表
            HSSFSheet sheet = workbook.createSheet(title);
            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);

            // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
            // 获取列头样式对象
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
            // 单元格样式对象
            HSSFCellStyle style = this.getStyle(workbook);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(title);

            // 定义所需列数
            int columnNum = rowName.length;
            // 在索引2的位置创建行(最顶端的行开始的第二行)
            HSSFRow rowRowName = sheet.createRow(2);

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                // 创建列头对应个数的单元格
                HSSFCell cellRowName = rowRowName.createCell(n);
                // 设置列头单元格的数据类型
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);
                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
                // 设置列头单元格的值
                cellRowName.setCellValue(text);
                // 设置列头单元格样式
                cellRowName.setCellStyle(columnTopStyle);
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {
                // 遍历每个对象
                Object[] obj = dataList.get(i);
                // 创建所需的行数
                HSSFRow row = sheet.createRow(i + 3);
                for (int j = 0; j < obj.length; j++) {
                    // 设置单元格的数据类型
                    HSSFCell cell;
                    if (j == 0) {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(i + 1);
                    } else {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        if (!"".equals(obj[j]) && obj[j] != null) {
                            // 设置单元格的值
                            cell.setCellValue(obj[j].toString());
                        }
                    }
                    // 设置单元格样式
                    cell.setCellStyle(style);
                }
            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
            }
            try {
                String fileName;
                if (StrUtil.isNotBlank(title)){
                    fileName =title + ".xls";
                }else {
                    fileName = "Excel-" + title + String.valueOf(System.currentTimeMillis()).substring(4, 13)
                            + ".xls";
                }
                String headStr = "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8");
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                OutputStream out = response.getOutputStream();
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    *//**
     * 为工作簿添加一个页面
     * @param SheetName 页面名
     * @param rowName   标题
     * @param dataList  数据集
     * @param isLine  是否列显示
     * @throws Exception
     *//*
    public void setHSSFSheet(String SheetName,String[] rowName, List<Object[]> dataList,boolean isLine) throws Exception {//添加工作表
        if (isLine){ //数据是否列显示
            this.verticalAssembleData(SheetName,rowName,dataList);
        }else{
            this.assembleData(SheetName,rowName,dataList);
        }
    }
    //标题为列形式
    private void verticalAssembleData(String SheetName,String[] rowName, List<Object[]> dataList) throws Exception {
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet(SheetName);
        // 获取列头样式对象
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
        columnTopStyle.setWrapText(true);// 设置自动换行
        // 单元格样式对象
        HSSFCellStyle style = this.getStyle(workbook);
        style.setWrapText(true);// 设置自动换行

        // 定义所需列数
        int columnNum = rowName.length;
        // 将查询出的数据设置到sheet对应的单元格中
        HSSFRow row[] = new HSSFRow[columnNum];//设置一共多少行
        for (int i = 0; i < rowName.length-1; i++){//先将标题和序号加上
            row[i] = sheet.createRow(i);// 获取这个一行
            HSSFCell cell = row[i].createCell(1, HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(rowName[i+1]);//标题
            cell.setCellStyle(columnTopStyle); ///设置头标题样式
            cell = row[i].createCell(0, HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(i+1);//序号
            cell.setCellStyle(style);    // 设置单元格样式
        }
        for (int i = 0; i < dataList.size(); i++) { //循环值
            Object[] obj = dataList.get(i);
            for (int j = 0; j < obj.length-1; j++) { //循环值的参数
                HSSFCell cell = row[j].createCell(i+2, HSSFCell.CELL_TYPE_NUMERIC);
                if (!"".equals(obj[j+1]) && obj[j+1] != null) {
                    cell.setCellValue(obj[j+1].toString());   // 设置单元格的值
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(style);    // 设置单元格样
            }
        }
        // 让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                            if(columnWidth>254){
                                columnWidth = 54;
                            }
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
    }
    //标题为行形式
    private void assembleData(String SheetName,String[] rowName, List<Object[]> dataList) throws Exception {
        try {
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet(SheetName);
        // 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
       // HSSFCell cellTiltle = rowm.createCell(0);

        // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
        // 获取列头样式对象
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
        // 单元格样式对象
        HSSFCellStyle style = this.getStyle(workbook);
      *//*  sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));*//*
       *//* cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(SheetName);*//*

        // 定义所需列数
        int columnNum = rowName.length;
        // 在索引2的位置创建行(最顶端的行开始的第一行)
        HSSFRow rowRowName = sheet.createRow(0);

        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            // 创建列头对应个数的单元格
            HSSFCell cellRowName = rowRowName.createCell(n);
            // 设置列头单元格的数据类型
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);
            HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
            // 设置列头单元格的值
            cellRowName.setCellValue(text);
            // 设置列头单元格样式
            cellRowName.setCellStyle(columnTopStyle);
        }

        // 将查询出的数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {
            // 遍历每个对象
            Object[] obj = dataList.get(i);
            // 创建所需的行数
            HSSFRow row = sheet.createRow(i + 1);
            for (int j = 0; j < obj.length; j++) {
                // 设置单元格的数据类型
                HSSFCell cell = null;
                if (j == 0) {
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(i + 1);
                } else {
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    if (!"".equals(obj[j]) && obj[j] != null) {
                        // 设置单元格的值
                        cell.setCellValue(obj[j].toString());
                    }else{
                        cell.setCellValue("");
                    }
                }
                // 设置单元格样式
                cell.setCellStyle(style);
            }
        }
        // 让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    }

    *//**
     *  为工作簿添加一个页面(支持多行标题和 单元格合并)
     * @param SheetName 表名
     * @param rowsList  标题集合
     * @param dataList  数据集合
     * @param mergeList 单元格合并数据 List<Integer[]>：Integer[]中有四个参数，分别为：//起始行，结束行，起始列，结束列
     * @throws Exception
     *//*
    public void assembleDataAndMerge(String SheetName, List<String[]> rowsList, List<Object[]> dataList,List<Integer[]> mergeList) throws Exception {
        try {
            // 创建工作表
            HSSFSheet sheet = workbook.createSheet(SheetName);
            // 获取列头样式对象
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
            // 单元格样式对象
            HSSFCellStyle style = this.getStyle(workbook);
            // 定义所需列数
            int columnNum = dataList.get(0).length;
            Integer lineNumber = 0;//标记当前数据用到第几行
            //处理标题数据
            for (int i = 0; i < rowsList.size(); i++) {
                String[] titles = rowsList.get(i);
                HSSFRow titleRow = sheet.createRow(lineNumber);
                for (int j = 0; j < titles.length; j++) {
                    HSSFCell cell = titleRow.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(titles[j]);
                    // 设置单元格样式
                    cell.setCellStyle(columnTopStyle);
                }
                lineNumber++;
            }
            lineNumber--;
            //定义单元格合并规则
            for (int i = 0; i < mergeList.size(); i++) {
                Integer[] merges = mergeList.get(i);
                if(merges.length != 4){
                    continue;
                }
                CellRangeAddress region = new CellRangeAddress(merges[0],merges[1], merges[2], merges[3]);
                sheet.addMergedRegion(region);
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = lineNumber; i < dataList.size()+lineNumber; i++) {
                // 遍历每个对象
                Object[] obj = dataList.get(i-lineNumber);
                // 创建所需的行数
                HSSFRow row = sheet.createRow(i + 1);
                for (int j = 0; j < obj.length; j++) {
                    // 设置单元格的数据类型
                    HSSFCell cell = null;
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    if (!"".equals(obj[j]) && obj[j] != null) {
                        // 设置单元格的值
                        cell.setCellValue(obj[j].toString());
                    }else{
                        cell.setCellValue("");
                    }
                    // 设置单元格样式
                    cell.setCellStyle(style);
                }
            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    *//**
     * 导出 excel 文件
     * @param response
     * @param excelName 文件名
     *//*
    public  void exportExcelForMap(HttpServletResponse response, String excelName){
        if (workbook != null &&  !("".equals(fileName) || fileName == null)) {
            try {
                String fileName2;
                if (fileName.isEmpty()){
                     fileName2=System.currentTimeMillis()+".xls";
                }else {
                    fileName2 = fileName+".xls";
                }
                String headStr = "attachment; filename=\"" + new String(fileName2.getBytes("UTF-8"),"ISO-8859-1") + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                response.setCharacterEncoding("UTF-8");
                OutputStream out = response.getOutputStream();
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (workbook != null &&  !("".equals(filepath) || filepath == null)) {
           ZipCompressorByAnt zip = new ZipCompressorByAnt(filepath+"/"+excelName+".zip");
           zip.compressExe(filepath);
            try {
                downloadFile(excelName+".zip",filepath+"/"+excelName+".zip",response);
                FileUtil.deleteDir(filepath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    *//**
     * 添加打包用的文件夹路径(没有就创建)
     * @param folderPath
     *//*
    public void exportExcelZipFolder(String path,String folderPath){
        if ( "".equals(filepath) || filepath == null) {
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// now为获取当前系统时间
            FileUtil.createDir2(path + folderPath + "-" + time);
            filepath = path + folderPath + "-" + time;
        }
    }

    *//**
     * 添加一个要打包的文件 到打包文件件
     * @param excelName
     *//*
    public void exportExcelZipForMap(String excelName){
        if (workbook != null && !"".equals(filepath) && filepath != null) {//保存目录不允许为null
            try {
                FileOutputStream output=new FileOutputStream(filepath+"/"+excelName);
                workbook.write(output);//写入磁盘
                output.close();
                workbook = new HSSFWorkbook();//保存完后,重置对象,防止影响下一次保存
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    *//**
     * 列头单元格样式
     *//*
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    *//**
     * 列数据信息单元格样式
     *//*
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }


    *//**
     * 根据文件路径，名称下载文件
     *
     * @param fileName
     *            文件名称
     * @param filePath
     *            文件路径
     * @param response
     *            http对象
     * @throws Exception
     *//*
    public static void downloadFile(String fileName, String filePath, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
        InputStream inputStream = new FileInputStream(new File(filePath));
        OutputStream os = response.getOutputStream();
        byte[] b = new byte[2048];
        int length;
        while ((length = inputStream.read(b)) > 0) {
            os.write(b, 0, length);
        }
        // 这里主要关闭。
        os.close();
        inputStream.close();
    }


    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
*/

}
