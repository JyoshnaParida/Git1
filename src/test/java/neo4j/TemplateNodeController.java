package neo4j;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TemplateNodeController extends MyRunner {
	 public void createTemplate() throws IOException {
			test = report.startTest("NEO4J_Create Template");
			FileInputStream fi = new FileInputStream("data/testData.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(fi);
			XSSFSheet ws = wb.getSheet("Body");

			XSSFRow row = ws.getRow(15);
			String requestBody = row.getCell(1).getStringCellValue();

			wb.close();
			
			RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
			String tokenEndpoint = "/v1.0/14455/templates?requesterType=TENANT";

			test.log(LogStatus.PASS, "User hits the Create Template controller");

			test.log(LogStatus.INFO, "Request URL:" + "   " +RestAssured.baseURI + tokenEndpoint);
		    test.log(LogStatus.PASS, "User gives valid Request body");

		    test.log(LogStatus.INFO, "Request Body: " + requestBody);

			RequestSpecification request = RestAssured.given().body(requestBody);
//		    test.log(LogStatus.PASS, "User gives valid Request body");
			Response response = request.post(tokenEndpoint);

		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

			
			
//				test.log(LogStatus.PASS, "CreateMappingNode");
	//
//			int statusCode = response.getStatusCode();
//			ResponseBody ResponseData = response.getBody();
	//	
		    int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;

			if (statusCode == 200) {
				test.log(LogStatus.PASS, "Create Template Created successfull" + statusCode);
			} else {
				test.log(LogStatus.FAIL, "Failed to Create Template Status code: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Template Creation Response Time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.INFO, "<font color='Maroon'>Taking more than one second creating a Template Response Time: " + seconds + " seconds</font>");

			}

			report.endTest(test);
		}
		public void getTemplateById() {
			test = report.startTest("Neo4j_Get Template By Id");
			RequestSpecification request = RestAssured.given();
			 String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
//			 String tokenEndpoint = "/v1.0/11488/templates/1155?requesterType=TENANT";
			 test.log(LogStatus.INFO, "Request URL: " + baseURL);
			 request.baseUri(baseURL);
//			 request.baseUri(baseURL).param(baseURL, "engagement6981b350001bf189");

			 Response response = request.get();
			 response.then().log().all();
			 test.log(LogStatus.PASS, "User hits Get Template By Id request");
			 String responseBody = response.getBody().asString();
			 test.log(LogStatus.INFO, "Response Body: " + responseBody);
			 
			 int statusCode = response.getStatusCode();
				long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
				
			 if (statusCode == 200) {
			    test.log(LogStatus.PASS, "Retrieved Get Template By Id successfully statuscode:" +statusCode);
			  }
			 else 
			 {
		     test.log(LogStatus.FAIL, "Failed to Get Template By Id statuscode: " +statusCode);
			  }
			 if (seconds <= 1.0){

				 test.log(LogStatus.PASS, "<font color='Green'>Plugin Creation Response Time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.INFO, "<font color='Maroon'>Taking more than one second creating a Plugin Response Time: " + seconds + " seconds</font>");

			}
		}
		public void updateTemplate() throws IOException {
			 test = report.startTest("Neo4j_Update Template");
			 FileInputStream fi = new FileInputStream("data/testData.xlsx");
			 XSSFWorkbook wb = new XSSFWorkbook(fi);
			 XSSFSheet ws = wb.getSheet("Body");

			 XSSFRow row = ws.getRow(16);
			 String requestBody = row.getCell(1).getStringCellValue();

			  wb.close();

			  RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
			 String tokenEndpoint = "/v1.0/1488/templates/1455?requesterType=TENANT";
			 test.log(LogStatus.PASS, "User hits Update Template request");
			 test.log(LogStatus.INFO, "Request URL: " + "    " + RestAssured.baseURI + tokenEndpoint);
			 test.log(LogStatus.PASS, "User gives valid Request body");

			 test.log(LogStatus.INFO, "Request Body: " + requestBody);
//			 test.log(LogStatus.PASS, "User gives valid Request body");
			 RequestSpecification request = RestAssured.given().body(requestBody);

			  Response response = request.put(tokenEndpoint);

			 String responseBody = response.getBody().asString();
			 test.log(LogStatus.INFO, "Response Body: " + responseBody);
			 int statusCode = response.getStatusCode();
			 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;

			 if (statusCode == 200) {
		    test.log(LogStatus.PASS, "Successfully Update Template statuscode: " + statusCode);
			  } else {
			     test.log(LogStatus.FAIL, "Failed to  Update Template statuscode: " + statusCode);
			  }
			 if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Plugin Creation Response Time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.INFO, "<font color='Maroon'>Taking more than one second creating a Plugin Response Time: " + seconds + " seconds</font>");

				}

		   report.endTest(test);
		}
		public void getAllTemplate() {

		test = report.startTest("Neo4j_Get All Template");
		RequestSpecification request = RestAssured.given();

		String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
//		 String tokenEndpoint = "/v1.0/1458/templates?requesterType=TENANT";
		test.log(LogStatus.INFO, "Request URL: " + baseURL);
		request.baseUri(baseURL);

		Response response = request.get();
		response.then().log().all();

		test.log(LogStatus.PASS, "User hits Get All Template request");
		int statusCode = response.getStatusCode();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		String responseBody = response.getBody().asString();
		test.log(LogStatus.INFO, "Response Body: " + responseBody);
		if (statusCode == 200) {
		test.log(LogStatus.PASS, "Retrieved Get All Template successfully statuscode:" + statusCode);
		} 
		else 
		{
			test.log(LogStatus.FAIL, "Failed to Retrive GetAllTemplate statuscode: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Plugin Creation Response Time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.INFO, "<font color='Maroon'>Taking more than one second creating a Plugin Response Time: " + seconds + " seconds</font>");

		}

		
		}
}
