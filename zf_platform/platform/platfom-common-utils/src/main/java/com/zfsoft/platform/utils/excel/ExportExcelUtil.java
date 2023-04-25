package com.zfsoft.platform.utils.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:  Excel导出工具类
 * @Author: wuxx
 * @Date: 2020/9/10 11:19
 **/
public class ExportExcelUtil {

	/**
	 * 导出数据
	 */
	public void export(HttpServletResponse response,String title, String[] rowName, List<Object[]> dataList){
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
				cellRowName.setCellType(CellType.STRING);
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
					HSSFCell cell = null;
					if (j == 0) {
						cell = row.createCell(j, CellType.NUMERIC);
						cell.setCellValue(i + 1);
					} else {
						cell = row.createCell(j, CellType.STRING);
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
						currentCell.setCellType(CellType.STRING);
						if (currentCell.getCellType() == CellType.STRING) {
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
					sheet.setColumnWidth(colNum, ((columnWidth + 4) > 255 ? 255 : (columnWidth + 4)) * 256);
				}
			}

			if (workbook != null) {
				try {
					String fileName = "Excel-" + title + String.valueOf(System.currentTimeMillis()).substring(4, 13)
							+ ".xls";
					fileName = URLEncoder.encode(fileName, "utf-8");
					String headStr = "attachment; filename=\"" +  fileName + "\"";
					response.setContentType("APPLICATION/OCTET-STREAM;charset=utf-8");
					response.setHeader("Content-Disposition", headStr);
					OutputStream out = response.getOutputStream();
					workbook.write(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 导出数据-hutool工具类
	 */
	public void exportHutool(HttpServletResponse response, String title, String[] rowName, List<Object[]> dataList) {
		try {
			ArrayList<Map<String, Object>> rowsList = new ArrayList<>();
			for(int i = 0 ;i<dataList.size();i++){
				Object [] rowData = dataList.get(i);
				Map<String, Object> map = new LinkedHashMap<>();
				for(int j = 0 ;j<rowName.length;j++){
					map.put(rowName[j],rowData[j]);
				}
				rowsList.add(map);
			}
			String fileName = "Excel-" + title + String.valueOf(System.currentTimeMillis()).substring(4, 13)
					+ ".xls";
			fileName = URLEncoder.encode(fileName, "utf-8");
			//通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter();
			//通过构造方法创建writer
			//ExcelWriter writer = new ExcelWriter("d:/writeTest.xls");
			//跳过当前行，既第一行，非必须，在此演示用
			writer.passCurrentRow();
			//合并单元格后的标题行，使用默认标题样式
			writer.merge(rowName.length - 1, title);
			if (writer != null) {
				try {
					String headStr = "attachment; filename=\"" +  fileName + "\"";
					response.setContentType("APPLICATION/OCTET-STREAM;charset=utf-8");
					response.setHeader("Content-Disposition", headStr);
					OutputStream out = response.getOutputStream();
					//一次性写出内容，强制输出标题
					writer.write(rowsList, true);
					writer.flush(out, true);
					// 关闭writer，释放内存
					writer.close();
					//此处记得关闭输出Servlet流
					IoUtil.close(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 列头单元格样式
	 */
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBold(true);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(BorderStyle.THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(IndexedColors.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(BorderStyle.THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(IndexedColors.BLACK.index);
		// 设置右边框;
		style.setBorderRight(BorderStyle.THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(IndexedColors.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(BorderStyle.THIN);
		// 设置顶边框颜色;
		Color black = new Color(0,0,0);
		style.setTopBorderColor(IndexedColors.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HorizontalAlignment.CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	/**
	 * 列数据信息单元格样式
	 */
	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		//font.setFontHeightInPoints((short)10);
		// 字体加粗
		//font.setBold(true);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(BorderStyle.THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(IndexedColors.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(BorderStyle.THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(IndexedColors.BLACK.index);
		// 设置右边框;
		style.setBorderRight(BorderStyle.THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(IndexedColors.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(BorderStyle.THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(IndexedColors.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HorizontalAlignment.CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}


}
