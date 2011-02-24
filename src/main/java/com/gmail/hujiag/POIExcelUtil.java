package com.gmail.hujiag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @className:POIExcelUtil.java
 * @classDescription:POI操作类
 * @author:xiayingjie
 * @createTime:2010-10-29
 */

public class POIExcelUtil {
	private static Log log = LogFactory.getLog(POIExcelUtil.class);

	// ------------------------写Excel-----------------------------------
	/**
	 * 创建workBook对象 xlsx(2007以上版本)
	 * 
	 * @return
	 */
	public static Workbook createWorkbook() {
		return createWorkbook(true);
	}

	/**
	 * 创建WorkBook对象
	 * 
	 * @param flag
	 *            true:xlsx(1997-2007) false:xls(2007以下)
	 * @return
	 */
	public static Workbook createWorkbook(boolean flag) {
		Workbook wb;
		if (flag) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}
		return wb;
	}

	/**
	 * 添加图片
	 * 
	 * @param wb
	 *            workBook对象
	 * @param sheet
	 *            sheet对象
	 * @param picFileName
	 *            图片文件名称（全路径）
	 * @param picType
	 *            图片类型
	 * @param row
	 *            图片所在的行
	 * @param col
	 *            图片所在的列
	 */
	public static void addPicture(Workbook wb, Sheet sheet, String picFileName,
			int picType, int row, int col) {
		InputStream is = null;
		try {
			// 读取图片
			is = new FileInputStream(picFileName);
			byte[] bytes = IOUtils.toByteArray(is);
			int pictureIdx = wb.addPicture(bytes, picType);
			is.close();
			// 写图片
			CreationHelper helper = wb.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();
			// 设置图片的位置
			anchor.setCol1(col);
			anchor.setRow1(row);
			Picture pict = drawing.createPicture(anchor, pictureIdx);

			pict.resize();
		} catch (Exception e) {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 创建Cell 默认为水平和垂直方式都是居中
	 * 
	 * @param style
	 *            CellStyle对象
	 * @param row
	 *            Row对象
	 * @param column
	 *            单元格所在的列
	 * @return
	 */
	public static Cell createCell(CellStyle style, Row row, short column) {
		return createCell(style, row, column, XSSFCellStyle.ALIGN_CENTER,
				XSSFCellStyle.ALIGN_CENTER);
	}

	/**
	 * 创建Cell并设置水平和垂直方式
	 * 
	 * @param style
	 *            CellStyle对象
	 * @param row
	 *            Row对象
	 * @param column
	 *            单元格所在的列
	 * @param halign
	 *            水平对齐方式：XSSFCellStyle.VERTICAL_CENTER.
	 * @param valign
	 *            垂直对齐方式：XSSFCellStyle.ALIGN_LEFT
	 */
	public static Cell createCell(CellStyle style, Row row, short column,
			short halign, short valign) {
		Cell cell = row.createCell(column);
		setAlign(style, halign, valign);
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            最后行
	 * @param firstCol
	 *            开始列
	 * @param lastCol
	 *            最后列
	 */
	public static void mergeCell(Sheet sheet, int firstRow, int lastRow,
			int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol,
				lastCol));
	}

	// ---------------------------------设置样式-----------------------

	/**
	 * 设置单元格对齐方式
	 * 
	 * @param style
	 * @param halign
	 * @param valign
	 * @return
	 */
	public static CellStyle setAlign(CellStyle style, short halign, short valign) {
		style.setAlignment(halign);
		style.setVerticalAlignment(valign);
		return style;
	}

	/**
	 * 设置单元格边框(四个方向的颜色一样)
	 * 
	 * @param style
	 *            style对象
	 * @param borderStyle
	 *            边框类型 ：dished-虚线 thick-加粗 double-双重 dotted-有点的
	 *            CellStyle.BORDER_THICK
	 * @param borderColor
	 *            颜色 IndexedColors.GREEN.getIndex()
	 * @return
	 */
	public static CellStyle setBorder(CellStyle style, short borderStyle,
			short borderColor) {

		// 设置底部格式（样式+颜色）
		style.setBorderBottom(borderStyle);
		style.setBottomBorderColor(borderColor);
		// 设置左边格式
		style.setBorderLeft(borderStyle);
		style.setLeftBorderColor(borderColor);
		// 设置右边格式
		style.setBorderRight(borderStyle);
		style.setRightBorderColor(borderColor);
		// 设置顶部格式
		style.setBorderTop(borderStyle);
		style.setTopBorderColor(borderColor);

		return style;
	}

	/**
	 * 自定义颜色（xssf)
	 * 
	 * @param style
	 *            xssfStyle
	 * @param red
	 *            RGB red (0-255)
	 * @param green
	 *            RGB green (0-255)
	 * @param blue
	 *            RGB blue (0-255)
	 */
	public static CellStyle setBackColorByCustom(XSSFCellStyle style, int red,
			int green, int blue) {
		// 设置前端颜色
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(red,
				green, blue)));
		// 设置填充模式
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return style;
	}

	/**
	 * 设置前景颜色
	 * 
	 * @param style
	 *            style对象
	 * @param color
	 *            ：IndexedColors.YELLOW.getIndex()
	 * @return
	 */
	public static CellStyle setBackColor(CellStyle style, short color) {

		// 设置前端颜色
		style.setFillForegroundColor(color);
		// 设置填充模式
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return style;
	}

	/**
	 * 设置背景颜色
	 * 
	 * @param style
	 *            style对象
	 * @param color
	 *            ：IndexedColors.YELLOW.getIndex()
	 * @param fillPattern
	 *            ：CellStyle.SPARSE_DOTS
	 * @return
	 */
	public static CellStyle setBackColor(CellStyle style, short backColor,
			short fillPattern) {

		// 设置背景颜色
		style.setFillBackgroundColor(backColor);

		// 设置填充模式
		style.setFillPattern(fillPattern);

		return style;
	}

	/**
	 * 
	 * 设置字体（简单的需求实现，如果复杂的字体，需要自己去实现）尽量重用
	 * 
	 * @param style
	 *            style对象
	 * @param fontSize
	 *            字体大小 shot(24)
	 * @param color
	 *            字体颜色 IndexedColors.YELLOW.getIndex()
	 * @param fontName
	 *            字体名称 "Courier New"
	 * @param
	 */
	public static CellStyle setFont(Font font, CellStyle style, short fontSize,
			short color, String fontName) {
		font.setFontHeightInPoints(color);
		font.setFontName(fontName);

		// font.setItalic(true);// 斜体
		// font.setStrikeout(true);//加干扰线

		font.setColor(color);// 设置颜色
		// Fonts are set into a style so create a new one to use.
		style.setFont(font);

		return style;

	}

	/**
	 * 
	 * @param createHelper
	 *            createHelper对象
	 * @param style
	 *            CellStyle对象
	 * @param formartData
	 *            date:"m/d/yy h:mm"; int:"#,###.0000" ,"0.0"
	 */
	public static CellStyle setDataFormat(CreationHelper createHelper,
			CellStyle style, String formartData) {

		style.setDataFormat(createHelper.createDataFormat().getFormat(
				formartData));

		return style;
	}

	/**
	 * 将Workbook写入文件
	 * 
	 * @param wb
	 *            workbook对象
	 * @param fileName
	 *            文件的全路径
	 * @return
	 */
	public static boolean createExcel(Workbook wb, String fileName) {
		boolean flag = true;
		FileOutputStream fileOut = null;
		try {
			File file = new File(fileName);
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			flag = false;
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
		return flag;
	}

	// --------------------读取Excel-----------------------
	/**
	 * 读取Excel
	 * 
	 * @param filePathName
	 * @return
	 */
	public static Workbook readExcel(String filePathName) {
		InputStream inp = null;
		Workbook wb = null;
		try {
			inp = new FileInputStream(filePathName);
			wb = WorkbookFactory.create(inp);
			inp.close();

		} catch (Exception e) {
			try {
				if (null != inp) {
					inp.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 读取Cell的值
	 * 
	 * @param sheet
	 * @return
	 */
	public static Map readCell(Sheet sheet) {
		Map map = new HashMap();
		// 遍历所有行
		for (Row row : sheet) {
			// 便利所有列
			for (Cell cell : row) {
				// 获取单元格的类型
				CellReference cellRef = new CellReference(row.getRowNum(),
						cell.getColumnIndex());
				// System.out.print(cellRef.formatAsString());
				String key = cellRef.formatAsString();
				// System.out.print(" - ");

				switch (cell.getCellType()) {
				// 字符串
				case Cell.CELL_TYPE_STRING:
					map.put(key, cell.getRichStringCellValue().getString());
					// System.out.println(cell.getRichStringCellValue()
					// .getString());
					break;
				// 数字
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						// System.out.println(cell.getDateCellValue());
						map.put(key, cell.getDateCellValue());
					} else {
						// System.out.println(cell.getNumericCellValue());
						map.put(key, cell.getNumericCellValue());
					}
					break;
				// boolean
				case Cell.CELL_TYPE_BOOLEAN:
					// System.out.println(cell.getBooleanCellValue());
					map.put(key, cell.getBooleanCellValue());
					break;
				// 方程式
				case Cell.CELL_TYPE_FORMULA:
					// System.out.println(cell.getCellFormula());
					map.put(key, cell.getCellFormula());
					break;
				// 空值
				default:
					System.out.println();
					map.put(key, "");
				}

			}
		}
		return map;

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// 在需要换行的地方加上\n
		// cell.setCellValue("Use \n with word wrap on to create a new line");
		// 设置行高 row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		// 设置列只适应宽度 sheet.autoSizeColumn((short)2);

		//
		// // -------------插入---------------------
		// Workbook wb = POIUtil.createWorkbook();
		// //创建Sheet
		// Sheet s = wb.createSheet();
		// s.autoSizeColumn(1);
		// s.autoSizeColumn(0);
		// s.autoSizeColumn(2);
		// //创建样式（真实项目中-所有样式都写在这里）
		// CellStyle style1=wb.createCellStyle();
		// CellStyle style2=wb.createCellStyle();
		// CellStyle style3=wb.createCellStyle();
		// //字体
		// //设置字体
		// Font font=wb.createFont();
		// CellStyle fontStyle
		// =setFont(font,style3,(short)30,IndexedColors.RED.getIndex() ,
		// "Courier New");
		// //合并单元格
		// //mergeCell(s,2,2,1,2);
		// //创建行
		// Row row1 = s.createRow(0);
		// row1.setHeightInPoints((2*s.getDefaultRowHeightInPoints()));
		// //-----------数字-----------
		// Cell c1=createCell(style1, row1, (short) 0);
		// c1.setCellValue(3.138);
		// //设置边框
		// setBorder(style1,CellStyle.BORDER_THIN,IndexedColors.GREEN.getIndex());
		//
		//
		// //-------------日期----------------
		// Cell c2=createCell(style2, row1, (short) 1);
		// c2.setCellValue(Calendar.getInstance());
		// CreationHelper ch=wb.getCreationHelper();
		// setDataFormat(ch,style2,"m/d/yy h:mm");
		// setBackColor(style2,IndexedColors.YELLOW.getIndex());
		//
		//
		//
		// Cell c4=createCell(style2, row1, (short) 2);
		//
		// //----------------字符串------------------
		//
		// //Cell c3=createCell(style3, row1, (short) 2);
		// Cell c3=row1.createCell((short) 3);
		// c3.setCellValue("我和你dfgd、\nfged二个如果");
		// CellStyle cs=wb.createCellStyle();
		//
		// setBackColor(style3,IndexedColors.ORANGE.getIndex());
		//
		//
		// c3.setCellStyle(style3);
		// c3.setCellStyle(cs);
		// 写入图片
		// POIUtil.addPicture(wb, s,"F://aa.gif",
		// Workbook.PICTURE_TYPE_JPEG,5,6);

		// Footer footer = s.getFooter(); //页脚
		//
		// footer.setRight( "Page " + footer.getLeft() + " of " +
		// footer.getRight());

		// s.shiftRows(5, 10,-5); //把第6-11行向上移动5行
		// s.setSelected(true); //选中sheet
		//
		// 打印
		// PrintSetup ps = s.getPrintSetup();
		//
		// sheet.setAutobreaks(true);
		//
		// ps.setFitHeight((short)1);
		// ps.setFitWidth((short)1);

		// POIUtil.createExcel(wb, "F://text.xlsx");

		// =====================读================
		Workbook wb = readExcel("D://test.xls");
		Workbook newWb = createWorkbook(false);
		Sheet sheet;
		Map<String, String> replaceTextMap = new HashMap<String, String>();
		replaceTextMap.put("販売単価", "販売単価1");
		replaceTextMap.put("TIS 應", "TIS 應1");
		;
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			sheet = wb.getSheetAt(i);
			replace(sheet, replaceTextMap);
		}

		File f = new File("D://excelReplace");
		f.mkdirs();
		createExcel(wb, "D://excelReplace//test.xls");

	}

	/**
	 * 替换文本
	 * 
	 * @param sheet
	 *            工作表
	 * @param replaceTextMap
	 *            替换文本MAP key=原字符串 value=替换字符串
	 */
	private static void replace(Sheet sheet, Map<String, String> replaceTextMap) {
		Iterator<Map.Entry<String, String>> iter = replaceTextMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			replace(sheet, key, value);
		}

	}

	/**
	 * 替换文本
	 * 
	 * @param sheet
	 *            工作表
	 * @param key
	 *            原字符串
	 * @param value
	 *            替换字符串
	 */
	private static void replace(Sheet sheet, String key, String value) {
		// 遍历所有行
		for (Row row : sheet) {
			// 便利所有列
			for (Cell cell : row) {
				// 获取单元格的类型
				CellReference cellRef = new CellReference(row.getRowNum(),
						cell.getColumnIndex());
				switch (cell.getCellType()) {
				// 字符串
				case Cell.CELL_TYPE_STRING:
					String targetText = cell.getRichStringCellValue()
							.getString();

					if (targetText != null && !targetText.trim().equals("")) {
						if (targetText.contains(key)) {
							targetText = targetText.replace(value, key);
							targetText = targetText.replace(key, value);
							cell.setCellValue(targetText);
							log.info("Sheet[" + sheet.getSheetName() + "]"
									+ "行:" + (cell.getRowIndex() + 1) + "列:"
									+ getColLetter(cell.getColumnIndex()) + " "
									+ targetText + " replace " + key + " -> "
									+ value);
						}
					}

					break;
				// 数字
				case Cell.CELL_TYPE_NUMERIC:
					break;
				// boolean
				case Cell.CELL_TYPE_BOOLEAN:
					break;
				// 方程式
				case Cell.CELL_TYPE_FORMULA:
					break;
				// 空值
				default:
				}

			}
		}

	}

	/**
	 * 将列的索引换算成ABCD字母，这个方法要在插入公式时用到。
	 * 
	 * @param colIndex
	 *            列索引。
	 * @return ABCD字母。
	 */
	private static String getColLetter(int colIndex) {
		String ch = "";
		if (colIndex < 26)
			ch = "" + (char) ((colIndex) + 65);
		else
			ch = "" + (char) ((colIndex) / 26 + 65 - 1)
					+ (char) ((colIndex) % 26 + 65);
		return ch;
	}
}
