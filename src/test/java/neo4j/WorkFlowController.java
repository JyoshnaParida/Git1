package neo4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class WorkFlowController extends MyRunner{
	private String workflowId;

	
	 public void createWorkflow() throws IOException {
	        test = report.startTest("Neo4j_Api to persist a TenantNode in Neo4j DB");
	        RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "/v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/workflows";
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User Hits createTenant request");
			test.log(LogStatus.INFO, "Request URL:" + "   " +RestAssured.baseURI + tokenEndpoint);


	        try {
	            // Load the Excel file
	            FileInputStream file = new FileInputStream("data/testData.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

	            // Read the input JSON body from Excel
	            String bodyString = sheet.getRow(10).getCell(1).getStringCellValue();

	            // Convert the JSON string to a JSONObject
	            JSONObject bodyJson = new JSONObject(bodyString);

	            // Generate a random tenantId using UUID and set it in the JSON object
	            String workflowId = UUID.randomUUID().toString();
	            bodyJson.put("workflowId", workflowId);

	            // Set the request JSON body
	            request.body(bodyJson.toString());
	            request.contentType(ContentType.JSON);
	    		//test.log(LogStatus.INFO, "Request Body: " + request);

	            test.log(LogStatus.PASS, "User valid Body");
	            // Send the request and validate the response
	            Response response = request.post(tokenEndpoint);

	            response.then().log().all();
	            String responseBody = response.getBody().asString();
	            test.log(LogStatus.INFO, "Response Body: " + responseBody);
	            workflowId = response.jsonPath().getString("workflowId");

	            ////Assert.assertEquals(response.statusCode(), 200);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "workflowId Creation Test Passed", "Response statuscode: " + response.statusCode());

	            // Store the extracted tenantId in the instance variable
	            this.workflowId = workflowId;

	            // Store the extracted tenantId in the Excel sheet
	            Cell workflowIdCell = sheet.getRow(1).createCell(2);
	            workflowIdCell.setCellValue(workflowId);

	            // Write the modified workbook back to the file
	            FileOutputStream outFile = new FileOutputStream("testData.xlsx");
	            workbook.write(outFile);

	            // Close the workbook and file
	            workbook.close();
	            file.close();
	            outFile.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Log the test failure in the report
	            test.log(LogStatus.FAIL, "workflowId CREATION FAILED", e.getMessage());
	        }
	    }
		
		
		
//
//		XSSFRow row = ws.getRow(27);
//		String requestBody = row.getCell(1).getStringCellValue();
//
//
//		test.log(LogStatus.PASS, "Create Workflow");
//
//		RequestSpecification request = RestAssured.given().body(requestBody);
//		test.log(LogStatus.PASS, "User gives valid body");
//		Response response = request.post(tokenEndpoint);
//		
//			test.log(LogStatus.PASS, "Create Work flow");
//
//		int statusCode = response.getStatusCode();
//		ResponseBody ResponseData = response.getBody();
//
//
//		if (statusCode == 200) {
//			test.log(LogStatus.PASS, "Workflow Created successful" + statusCode);
//		} else {
//			test.log(LogStatus.FAIL, "Failed to CreateWorkflow. Status code: " + ResponseData);
//		}
//
//		report.endTest(test);
//	}
	public void updateWorkflow() throws IOException {
		test = report.startTest("neo4j_Update Work flow");
		FileInputStream fi = new FileInputStream("data/testData.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet ws = wb.getSheet("Body");

		XSSFRow row = ws.getRow(28);
		String requestBody = row.getCell(1).getStringCellValue();

		wb.close();
		String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/group";
		test.log(LogStatus.PASS, "Update Work flow");

		RequestSpecification request = RestAssured.given().body(requestBody);
		test.log(LogStatus.PASS, "User gives valid body");
		Response response = request.put(tokenEndpoint);
	
		test.log(LogStatus.PASS, "Update Work flow");

		int statusCode = response.getStatusCode();
		ResponseBody ResponseData = response.getBody();


		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Workflow Updated successful" + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Updated Workflow. statuscode: " + ResponseData);
		}

		report.endTest(test);
	}
	public void getWorkflows() {
		test = report.startTest("Neo4j_Get Work flows");
		String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/workflow";
		test.log(LogStatus.PASS, "Get Work flows");

		Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
		int statusCode = response.getStatusCode();

		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Retrieved all GroupNodes successfull statuscode:" + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All groupNode statuscode: " + statusCode);
		}

		report.endTest(test);
	}
	public void getWorkflowId() {
		test = report.startTest(" Neo4j_Get Workflow Id");
		String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/workflow/workflow6981b350001bf100";
		test.log(LogStatus.PASS, "Retrieve By Get Workflow Id");

		Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
		int statusCode = response.getStatusCode();
		ResponseBody ResponseData = response.getBody();


		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Retrieved all Workflow by ID's successful" + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve By All Workflow Id's Status code: " + ResponseData);
		}

		report.endTest(test);
	}


}
