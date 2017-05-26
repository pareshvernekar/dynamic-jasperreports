package test.dynamicjasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

public class ReportExporter {
	/**
 	 * The path to the file must exist.
 	 * @param jp
 	 * @param path
 	 * @throws JRException
 	 * @throws FileNotFoundException
 	 */
 	public static void exportReport(JasperPrint jp, String path) throws JRException, FileNotFoundException{
 		System.out.println("Exporing report to: " + path);
 		JRPdfExporter exporter = new JRPdfExporter();
 
 		File outputFile = new File(path);
 		File parentFile = outputFile.getParentFile();
 		if (parentFile != null)
 			parentFile.mkdirs();
 		FileOutputStream fos = new FileOutputStream(outputFile);
 
 		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
 		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
 
 		exporter.exportReport();
 
 		System.out.println("Report exported: " + path);
 	}
 
 	public static void exportReportXls(JasperPrint jp, String path) throws JRException, FileNotFoundException{
 		JRXlsExporter exporter = new JRXlsExporter();
 
 		File outputFile = new File(path);
 		File parentFile = outputFile.getParentFile();
 		if (parentFile != null)
 			parentFile.mkdirs();
 		FileOutputStream fos = new FileOutputStream(outputFile);
 
 		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
 		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
 		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
 		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
 		exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.FALSE);
 
 		exporter.exportReport();
 
 		System.out.println("XLS Report exported: " + path);
 	}
 
 	public static void exportReportHtml(JasperPrint jp, String path) throws JRException, FileNotFoundException{
 		JRHtmlExporter exporter = new JRHtmlExporter();
 		
 		File outputFile = new File(path);
 		File parentFile = outputFile.getParentFile();
 		if (parentFile != null)
 			parentFile.mkdirs();
 		FileOutputStream fos = new FileOutputStream(outputFile);
 		
 		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
 		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
 		exporter.exportReport();
 		
 		System.out.println("HTML Report exported: " + path);
 	}
 
 	public static void exportReportPlainXls(JasperPrint jp, String path) throws JRException, FileNotFoundException{
 //		JRXlsExporter exporter = new JRXlsExporter();
 		JExcelApiExporter exporter = new JExcelApiExporter();
 
 		File outputFile = new File(path);
 		File parentFile = outputFile.getParentFile();
 		if (parentFile != null)
 			parentFile.mkdirs();
 		FileOutputStream fos = new FileOutputStream(outputFile);
 
 		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
 		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
 		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
 		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
 		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
 		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
 
 
 		exporter.exportReport();
 
 		System.out.println("Report exported: " + path);
 
 	}
}
