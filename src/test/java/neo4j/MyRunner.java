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

public class MyRunner {
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
					case "tenantById":
						keys.tenantById();
						break;
					case "retrieveAlltenants":
						keys.retrieveAlltenants();
						break;
					case "deleteTenant":
						keys.deleteTenant();
						break;
					case "createUser":
						keys.createUser();
						break;
					case "deleteUser":
						keys.deleteUser();
						break;
					case "createUniverse":
						keys.createUniverse();
						break;
					case "universeById":
						keys.universeById();
						break;
					case "retrieveAlluniverses":
						keys.retrieveAlluniverses();
						break;
					case "updateUniverse":
						keys.updateUniverse();
						break;
					case "deleteUniverse":
                       keys.deleteUniverse();
						break;
                    case "createSchema":
						keys.createSchema();
						break;
                    case "schemaById":
   						keys.schemaById();
   						break;
   					case "retrieveAllSchemas":
   						keys.retrieveAllSchemas();
   						break;
   					case "updateSchema":
						keys.updateSchema();
						break;
                    case "deleteschema":
                       keys.deleteschema();
						break;
                    case "createAQNode":
						keys.createAQNode();
						break;
                    case "aqById":
      					keys.aqById();
      					break;
      				case "retrieveAllAq":
      					keys.retrieveAllAq();
      					break;
      				case "updateAq":
      					keys.updateAq();
      					break;
                    case "deleteAQ":
						keys.deleteAQ();
						break;
					case "createGroupNode":
						keys.createGroupNode();
						break;
					case "GroupById":
						keys.GroupById();
						break;
					case "retrieveAllGroup":
						keys.retrieveAllGroup();
						break;
					case "updateGroup":
						keys.updateGroup();
						break;
					case"deleteGroup":
					    keys.deleteGroup();
					     break;
                    case "schemaGroup":
						keys.schemaGroup();
						break;
                    case "groupBySchemaId":
   						keys.groupBySchemaId();
   						break;
                    case "RetrieveAllGroupBySchemaId":
   						keys.RetrieveAllGroupBySchemaId();
   						break;
					case "schemaContext":
						keys.schemaContext();
						break;
					case "contextBySchemaId":
						keys.contextBySchemaId();
						break;
					case "RetrieveAllContexts":
						keys.RetrieveAllContexts();
						break;
					case "AqContext":
						keys.AqContext();
						break;
					case "contextByAqId":
						keys.contextByAqId();
						break;
					case "retrieveAllContexts":
						keys.retrieveAllContexts();
						break;
					case "updateContextAq":
						keys.updateContextAq();
						break;
					case "deleteContext":
						keys.deleteContext();
						break;
					case "createWorkflow":
						keys.createWorkflow();
						break;
					case "workflowByAqId":
						keys.workflowByAqId();
						break;
					case "retrieveAllWorkflows":
						keys.retrieveAllWorkflows();
						break;
					case "updateWorkflow":
						keys.updateWorkflow();
						break;
					case "createChartGroup":
						keys.createChartGroup();
						break;
					case "chartByGroupId":
						keys.chartByGroupId();
						break;
					case "retrieveAllCharts":
						keys.retrieveAllCharts();
						break;
					case "updateChartGroup":
						keys.updateChartGroup();
						break;
					case "createBaGroup":
						keys.createBaGroup();
						break;
					case "BaByGroupId":
						keys.BaByGroupId();
						break;
					case "retrieveAllBas":
						keys.retrieveAllBas();
						break;
					case "updateBaGroup":
						keys.updateBaGroup();
						break;
					case "createTemplateGroup":
						keys.createTemplateGroup();
						break;
					case "TemplateByGroupId":
						keys.TemplateByGroupId();
						break;
					case "retrieveAllTemplates":
						keys.retrieveAllTemplates();
						break;
					case "updateTemplateGroup":
						keys.updateTemplateGroup();
						break;
					case "createEngagementTemplate":
						keys.createEngagementTemplate();
						break;
					case "engagementByTemplateId":
						keys.engagementByTemplateId();
						break;
					case "retrieveAllEngagements":
						keys.retrieveAllEngagements();
						break;
					case "deleteworkflowId":
						keys.deleteworkflowId();
						break;
					case "deletechartId":
						keys.deletechartId();
						break;
					case "engagementId":
						keys.engagementId();
						break;
					case "deleteBa":
						keys.deleteBa();
						break;
					case "deletetemplateId":
						keys.deletetemplateId();
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

//		try {
//			SendEmail.sendTestReportEmail();
//
//		} catch (EmailException e) {
//			e.printStackTrace();
//		}

	}}
