package com.yanxiu.result;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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

//		Table table = new Table(result.size(), 5);
//		int row = 0;
//	
//		Cell cell;
//		for (int i = 0; i < result.size(); i++) {
//			if (i == row) {
//				row = 1;
//				for (int j = i + 1; j < result.size(); j++) {
//					if (result.get(i).getUrl().equals(result.get(j).getUrl())) {
//						
//						row++;
//
//					}
//
//				}
//
//				if (row > 1) {
//					cell = new Cell(result.get(i).getUrl(), row, 1);
//					table.setCell(cell, i, 0);
//					row = i + row;
//				}
//			}
//
//			cell = new Cell(result.get(i).getName());
//			table.setCell(cell, i, 1);
//			cell = new Cell(result.get(i).getMethod());
//			table.setCell(cell, i, 2);
//			cell = new Cell(result.get(i).getParams());
//			table.setCell(cell, i, 3);
//			cell = new Cell(result.get(i).getResult());
//			table.setCell(cell, i, 4);
//
//		}
//
//		context.put("table", table);

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
		System.out.println(writer.toString());
	}
}
