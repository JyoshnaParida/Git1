package neo4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class MyRunner1 {
	public static String report1;

	@Test
	public void Usernode() throws Throwable {
		TenantCreationFlow keys =new TenantCreationFlow();

		// Specify the Keywords file location
		FileInputStream fi = new FileInputStream("data/testData.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet ws = wb.getSheet("Runner");

		// Find the number of rows
		int rowCount = ws.getLastRowNum();
		for (int i = 1; i <= rowCount; i++) {
			XSSFRow row = ws.getRow(i);

			// Read the run mode
			String runMode = "";
			if (row.getCell(4) != null) {
				runMode = row.getCell(4).getStringCellValue();
				System.out.println(runMode);
			}

			if ("Y".equals(runMode)) {
				// Read the step description
				String keyWord = "";
				if (row.getCell(3) != null) {
					keyWord = row.getCell(3).getStringCellValue();
					System.out.println(keyWord);
				}

				switch (keyWord) {
				case "createTenant":
					keys.createTenant();
					break;
				case "createUser":
					keys.createUser();
					break;
				case "createUniverse":
					keys.createUniverse();
					break;
				case "createSchema":
					keys.createSchema();
					break;
				case "createAQNode":
					keys.createAQNode();
					break;
				case "createGroupNode":
					keys.createGroupNode();
					break;
				case "schemaGroup":
					keys.schemaGroup();
					break;
				case "schemaContext":
					keys.schemaContext();
					break;
				case "AqContext":
					keys.AqContext();
					break;
	}

}
		}

	

// Close the workbook
wb.close();
}

static ExtentTest test;
static ExtentReports report;

@BeforeClass
public static void startTest() {
Date d = new Date();
String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
report1 = "Neo4j Api Automation test report_" + fileName;
System.out.println(report1);
report = new ExtentReports(System.getProperty("user.dir") + "" + report1);

report = new ExtentReports(System.getProperty("user.dir") + "/" + report1);

}

@AfterClass
public static void endTest() throws IOException {
	// End the test and generate the report
	report.endTest(test);
	report.flush();

//	try {
//		SendEmail.sendTestReportEmail();
//
//	} catch (EmailException e) {
//		e.printStackTrace();
//	}

}
}
