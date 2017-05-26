package test.dynamicjasper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJValueFormatter;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.util.SortUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperDesignViewer;
import net.sf.jasperreports.view.JasperViewer;

public class FirstSampleReport {

	protected JasperPrint jp;
	protected JasperReport jr;
	protected Map params = new HashMap();
	protected DynamicReport dr;


	public DynamicReport buildReport() throws Exception {
		
		FastReportBuilder frb = new FastReportBuilder();
		Date currDate = new Date();
		frb.addColumn("State", "state", String.class.getName(),30)
		.addColumn("Branch", "branch", String.class.getName(),30)
		.addColumn("Product Line", "productLine", String.class.getName(),50)
		.addColumn("Item", "item", String.class.getName(),50)
		.addColumn("Item Code", "id", Long.class.getName(),30,true)
		.addColumn("Quantity", "quantity", Long.class.getName(),60,true)
		.addColumn("Amount", "amount", Float.class.getName(),70,true)
		.addGroups(2)
		.setTitle("November " + getYear() + " sales report")
		.setSubtitle("This report was generated at " + currDate)
		.setPrintBackgroundOnOddRows(true)			
		.setUseFullPageWidth(true);
		

		frb.addGlobalFooterVariable(frb.getColumn(4), DJCalculation.COUNT, null, new DJValueFormatter() {

			public String getClassName() {
				return String.class.getName();
			}
			public Object evaluate(Object value, Map fields, Map variables,   Map parameters) {
				return (value == null ? "0" : value.toString()) + " Clients";
			}
		});
		dr = frb.build();
			return dr;
		}

	public Map getParams() {
		return params;
	}
	
	public void testReport() throws Exception {
		DynamicReport dr = buildReport();
		  
		  			/**
		  			 * Get a JRDataSource implementation
		  			 */
		  			JRDataSource ds = getDataSource();
		  			/**
		  			 * Creates the JasperReport object, we pass as a Parameter
		  			 * the DynamicReport, a new ClassicLayoutManager instance (this
		  			 * one does the magic) and the JRDataSource
		  			 */
		  			JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
		  			/**
		  			 * Creates the JasperPrint object, we pass as a Parameter
		  			 * the JasperReport object, and the JRDataSource
		  			 */
		  			System.out.println("Filling the report");
		  			if (ds != null)
		  				jp = JasperFillManager.fillReport(jr, params, ds);
		  			else
		  				jp = JasperFillManager.fillReport(jr, params);
		  
		  			System.out.println("Filling done!");
		  			System.out.println("Exporting the report (pdf, xls, etc)");
		              exportReport();
		              System.out.println("test finished");
		  
		  	}
		  
		 	protected LayoutManager getLayoutManager() {
		 		return new ClassicLayoutManager();
		 	}
		 
		 	protected void exportReport() throws Exception {
		 		ReportExporter.exportReport(jp, System.getProperty("user.dir")+ "/target/reports/" + this.getClass().getName() + ".pdf");
		 		exportToJRXML();
		 	}
		 	
		 	protected void exportToJRXML() throws Exception {
		 		if (this.jr != null){
		 			DynamicJasperHelper.generateJRXML(this.jr, "UTF-8",System.getProperty("user.dir")+ "/target/reports/" + this.getClass().getName() + ".jrxml");
		 			
		 		} else {
		 			DynamicJasperHelper.generateJRXML(this.dr, this.getLayoutManager(), this.params, "UTF-8",System.getProperty("user.dir")+ "/target/reports/" + this.getClass().getName() + ".jrxml");
		 		}
		 	}	
		 
		 	protected void exportToHTML() throws Exception {
		 		ReportExporter.exportReportHtml(this.jp, System.getProperty("user.dir")+ "/target/reports/" + this.getClass().getName() + ".html");
		 	}	
		 
		 	/**
		 	 * @return JRDataSource
		 	 */
		 	protected JRDataSource getDataSource() {
		 		Collection dummyCollection = TestRepositoryProducts.getDummyCollection();
		 		dummyCollection = SortUtils.sortCollection(dummyCollection,dr.getColumns());
		 
		 		JRDataSource ds = new JRBeanCollectionDataSource(dummyCollection);		//Create a JRDataSource, the Collection used
		 																				//here contains dummy hardcoded objects...
		 		return ds;
		 	}
		 
		 	public Collection getDummyCollectionSorted(List columnlist) {
		 		Collection dummyCollection = TestRepositoryProducts.getDummyCollection();
		 		return SortUtils.sortCollection(dummyCollection,columnlist);
		 
		 	}
		 
		 	public DynamicReport getDynamicReport() {
		 		return dr;
		 	}
		 
		 	/**
		 	 * Uses a non blocking HSQL DB. Also uses HSQL default test data
		 	 * @return a Connection
		 	 * @throws Exception
		 	 */
		 	public static Connection createSQLConnection() throws Exception {
		 		Connection con = null;
		 		     Class.forName("org.hsqldb.jdbcDriver" );
		 			 con = DriverManager.getConnection("jdbc:hsqldb:file:target/test-classes/hsql/test_dj_db", "sa", "");
		 
		         return con;
		 	}
		 
		     public int getYear(){
		         return Calendar.getInstance().get(Calendar.YEAR);
		     }
		 

	public static void main(String[] argv) {
		FirstSampleReport fsr = new FirstSampleReport();
		try {
			fsr.testReport();
			fsr.exportToJRXML();
			JasperViewer.viewReport(fsr.jp);	//finally display the report report
			JasperDesignViewer.viewReportDesign(fsr.jr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
