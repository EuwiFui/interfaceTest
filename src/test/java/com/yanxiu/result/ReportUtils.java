package com.yanxiu.result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class ReportUtils {

	private final String templateFileName = "./report.vm";
	private final String resultFileName = "TestResult.html";
	private List<Report> result;
	private VelocityEngine velocityEngine;
	private Template vmTemplate;
	private VelocityContext context;
	private Page page;

	public void init() throws ResourceNotFoundException, ParseErrorException, Exception {
		result = new ArrayList<Report>();
		page = new Page();
		velocityEngine = new VelocityEngine();
		Properties props = new Properties();
		props.put("input.encoding", "utf-8");
		props.setProperty("resource.loader", "class");
		props.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init(props);
		vmTemplate = velocityEngine.getTemplate(templateFileName);
		context = new VelocityContext();
	}

	public void addResultToResultList(Report report) {
		result.add(report);
	}

	public void generateReport()
			throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception {

		page.setPageNum(result.size());
		context.put("objs", result);

		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format1.format(new Date());

		context.put("date", date);
		context.put("page", page);

		// Table table = new Table(result.size(), 5);
		// int row = 0;
		//
		// Cell cell;
		// for (int i = 0; i < result.size(); i++) {
		// if (i == row) {
		// row = 1;
		// for (int j = i + 1; j < result.size(); j++) {
		// if (result.get(i).getUrl().equals(result.get(j).getUrl())) {
		//
		// row++;
		//
		// }
		//
		// }
		//
		// if (row > 1) {
		// cell = new Cell(result.get(i).getUrl(), row, 1);
		// table.setCell(cell, i, 0);
		// row = i + row;
		// }
		// }
		//
		// cell = new Cell(result.get(i).getName());
		// table.setCell(cell, i, 1);
		// cell = new Cell(result.get(i).getMethod());
		// table.setCell(cell, i, 2);
		// cell = new Cell(result.get(i).getParams());
		// table.setCell(cell, i, 3);
		// cell = new Cell(result.get(i).getResult());
		// table.setCell(cell, i, 4);
		//
		// }
		//
		// context.put("table", table);

		StringWriter writer = new StringWriter();
		// 转换输出
		vmTemplate.merge(context, writer);
		File resultFile = new File(resultFileName);
		if (!resultFile.exists()) {
			resultFile.createNewFile();
		} else {
			resultFile.delete();
		}
		// FileWriter fileWritter = new FileWriter(resultFile.getName(),true);
		BufferedWriter bufferWritter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(resultFile), "UTF-8"));
		bufferWritter.write(writer.toString());
		bufferWritter.close();
		// System.out.println(writer.toString());
	}

	public void writeResultToExcel() throws IOException {

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet resultSheet = wb.createSheet("测试结果");

		resultSheet.setColumnWidth(0, 5 * 256);
		resultSheet.setColumnWidth(1, 50 * 256);
		resultSheet.setColumnWidth(2, 25 * 256);
		resultSheet.setColumnWidth(3, 8 * 256);
		resultSheet.setColumnWidth(4, 50 * 256);
		resultSheet.setColumnWidth(5, 7 * 256);
		resultSheet.setColumnWidth(6, 8 * 256);
		resultSheet.setColumnWidth(7, 80 * 256);

		String[] titles = { "编号", "接口", "用例名称", "请求方式", "参数", "返回值", "测试结果", "失败原因" };

		XSSFCellStyle commonStyle = wb.createCellStyle();

		commonStyle.setBorderLeft(BorderStyle.THIN);
		commonStyle.setBorderRight(BorderStyle.THIN);
		commonStyle.setBorderTop(BorderStyle.THIN);
		commonStyle.setBorderBottom(BorderStyle.THIN);
		commonStyle.setWrapText(true);

		XSSFCellStyle failStyle = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		font.setColor(XSSFFont.COLOR_RED);
		failStyle.setFont(font);

		XSSFCellStyle titleStyle = wb.createCellStyle();
		setTitleStyle(wb,titleStyle);


		XSSFRow titleRow = resultSheet.createRow(0);
		for (int i = 0; i < titles.length; i++) {
			XSSFCell cell = titleRow.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(titleStyle);
			// cell.setCellStyle(commonStyle);
		}

		for (int i = 0; i < result.size(); i++) {
			XSSFRow row = resultSheet.createRow(i + 1);
			String[] caseResults = { result.get(i).getUrl(), result.get(i).getName(), result.get(i).getMethod(),
					result.get(i).getParams(), String.valueOf(result.get(i).getStatusCode()), result.get(i).getResult(),
					result.get(i).getReason() };
			XSSFCell cell = row.createCell(0);
			cell.setCellValue(i + 1);
			cell.setCellStyle(commonStyle);
			for (int j = 0; j < caseResults.length; j++) {
				cell = row.createCell(j + 1);
				cell.setCellValue(caseResults[j]);
				cell.setCellStyle(commonStyle);
				if (caseResults[j].equals("FAIL")) {
					cell.setCellStyle(failStyle);
				}
			}
			
			
		}
		summarizeResult(wb);
		String file = "TestResult.xlsx";

		FileOutputStream out = new FileOutputStream(file);
		wb.write(out);
		out.close();
	}
	
	public void summarizeResult(XSSFWorkbook wb){
		XSSFSheet summarySheet = wb.createSheet("结果汇总");
		
		String[] titles = {"Total","Pass","Fail","Pass Rate"};
		XSSFCellStyle titleStyle = wb.createCellStyle();
		setTitleStyle(wb,titleStyle);
		
		XSSFRow row = summarySheet.createRow(0);
		for(int i=0;i<titles.length;i++){
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(titleStyle);
		}
		
		int passNumber = 0;
		int failNumber = 0;
		for(int i=0;i<result.size();i++){
			if(result.get(i).getResult().equals("PASS")){
				passNumber++;
			}else if(result.get(i).getResult().equals("FAIL")){
				failNumber++;
			}
		}
		
		XSSFCellStyle commonStyle = wb.createCellStyle();

		commonStyle.setBorderLeft(BorderStyle.THIN);
		commonStyle.setBorderRight(BorderStyle.THIN);
		commonStyle.setBorderTop(BorderStyle.THIN);
		commonStyle.setBorderBottom(BorderStyle.THIN);
		
		
		NumberFormat numberFormat = NumberFormat.getInstance(); 
		numberFormat.setMaximumFractionDigits(2);  
		String passRate = numberFormat.format((float)passNumber/(float)result.size()*100)+"%";
		String[] summary = {String.valueOf(result.size()),String.valueOf(passNumber),String.valueOf(failNumber),passRate};
		row = summarySheet.createRow(1);
		XSSFCell cell1;
		for(int i=0;i<summary.length;i++){
			cell1 = row.createCell(i);
		cell1.setCellValue(summary[i]);
		cell1.setCellStyle(commonStyle);

		}
		
	}
	
	public void setTitleStyle(XSSFWorkbook wb,XSSFCellStyle titleStyle){
		XSSFColor color = new XSSFColor();
		color.setARGBHex("6495ED");
		titleStyle.setFillForegroundColor(color);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderTop(BorderStyle.THIN);
		titleStyle.setBorderBottom(BorderStyle.THIN);

		XSSFFont titleFont = wb.createFont();
		XSSFColor titleColor = new XSSFColor();
		titleColor.setARGBHex("FFFFFF");
		titleFont.setColor(titleColor);
		titleStyle.setFont(titleFont);
	}
}