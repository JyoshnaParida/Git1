package neo4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;


public class TenantNodeController extends MyRunner {
	private String tenantId;

	
	 public void createTenant() throws IOException {
	        test = report.startTest("Neo4j_Api to persist a TenantNode in Neo4j DB");
	        RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "/v1.0/neo4j/node/tenant";
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User Hits createTenant request");
			test.log(LogStatus.INFO, "Request URL:" + "   " +RestAssured.baseURI + tokenEndpoint);


	        try {
	            // Load the Excel file
	            FileInputStream file = new FileInputStream("data/testData.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

	            // Read the input JSON body from Excel
	            String bodyString = sheet.getRow(1).getCell(1).getStringCellValue();

	            // Convert the JSON string to a JSONObject
	            JSONObject bodyJson = new JSONObject(bodyString);

	            // Generate a random tenantId using UUID and set it in the JSON object
	            String tenantId = UUID.randomUUID().toString();
	            bodyJson.put("tenantId", tenantId);

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
	            tenantId = response.jsonPath().getString("tenantId");

	            ////Assert.assertEquals(response.statusCode(), 200);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "Tenant Creation Test Passed", "Response statuscode: " + response.statusCode());

	            // Store the extracted tenantId in the instance variable
	            this.tenantId = tenantId;

	            // Store the extracted tenantId in the Excel sheet
	            Cell tenantIdCell = sheet.getRow(1).createCell(2);
	            tenantIdCell.setCellValue(tenantId);

	            // Write the modified workbook back to the file
	            FileOutputStream outFile = new FileOutputStream("testData.xlsx");
	            workbook.write(outFile);

	            // Close the workbook and file
	            workbook.close();
	            file.close();
	            outFile.close();
	            int statusCode = response.getStatusCode();
				long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
				if (statusCode == 200 || statusCode == 201) {
					test.log(LogStatus.PASS,
							"Tenant Creation Without Component Test Passed with Status Code: " + statusCode);

				} else {

					test.log(LogStatus.FAIL, "Failed to create Tenant Without Component. Status Code: " + statusCode);

				}
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='dim gray'>Tenant Creation Response Time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.INFO, "<font color='orange red'>Taking more than one second creating a Tenant Response Time: " + seconds + " seconds</font>");

				}
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Log the test failure in the report
	            test.log(LogStatus.FAIL, "Tenant CREATION FAILED", e.getMessage());
	        }
	    }

		public void retrieveAlltenants() { 
			test = report.startTest("Neo4j_Api to retrieve all TenantNode from Neo4j DB");
		    RequestSpecification request = RestAssured.given();
		    
		    String baseURL = "https://ig.aidtaas.com/neo4j-service/v1.0/neo4j/tenant";
		    test.log(LogStatus.INFO, "Request URL: " + baseURL);
		    request.baseUri(baseURL);

		    Response response = request.get();
		    response.then().log().all();
		    
		    test.log(LogStatus.PASS, "retrieve all TenantNode");
		    int statuscode = response.getStatusCode();
		    long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);
		    if (statuscode == 200) {
		        test.log(LogStatus.PASS, "retrieve all TenantNode successfully statuscode:" +statuscode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to retrieve all TenantNode statuscode: " +statuscode);
		    }
		    if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Tenant Creation Response Time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.INFO, "<font color='Maroon'>Taking more than one second creating a Tenant Response Time: " + seconds + " seconds</font>");

			}
		}
		public void tenantById() { 
			test = report.startTest("Neo4j_Api to retrieve a TenantNode from Neo4j DB by TenantId");
		    RequestSpecification request = RestAssured.given();
		    
		    String baseURL = "https://ig.aidtaas.com/neo4j-service/v1.0/neo4j/tenant/" + tenantId;;
		    test.log(LogStatus.INFO, "Request URL: " + baseURL);
		    request.baseUri(baseURL);

		    Response response = request.get();
		    response.then().log().all();
		    
		    test.log(LogStatus.PASS, "Retrieved tenantById");
		    int statuscode = response.getStatusCode();
		    long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);
		    if (statuscode == 200) {
		        test.log(LogStatus.PASS, "Retrieved tenant By Id successfully statuscode:" +statuscode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to Retrieved tenant By Id statuscode: " +statuscode);
		    }
		    if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Retrieve Tenant By Id Response Time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.INFO, "<font color='Maroon'>Taking more than one second Retrieve Tenant By Id Response Time: " + seconds + " seconds</font>");

			}
		}
	

	    public void deleteTenant() throws Throwable {
	        test = report.startTest("Neo4j_Delete Tenant");
	        RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String deleteEndpoint = "/v1.0/neo4j/node/tenant/" + tenantId;
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User Hits Delete Tenant request");
			test.log(LogStatus.INFO, "Request URL:" + "   " +RestAssured.baseURI + deleteEndpoint);

	        try {
	            // Send the request and validate the response
	            Response response = request.delete(deleteEndpoint);

	            response.then().log().all();
	            String responseBody = response.getBody().asString();
	            test.log(LogStatus.INFO, "Response Body: " + responseBody);

	            //Assert.assertEquals(response.statusCode(), 200);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "Tenant Deletion Test Passed", "Response statuscode: " + response.statusCode());
	            int statuscode = response.getStatusCode();
			    long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
			    test.log(LogStatus.INFO, "Response Body: " + responseBody);
			    if (statuscode == 200) {
			        test.log(LogStatus.PASS, "Retrieved tenant By Id successfully statuscode:" +statuscode);
			    } else {
			        test.log(LogStatus.FAIL, "Failed to Retrieved tenant By Id statuscode: " +statuscode);
			    }
			    if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Delete Tenant By Id Response Time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.INFO, "<font color='Maroon'>Taking more than one second Delete Tenant By Id Response Time: " + seconds + " seconds</font>");

				}
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Log the test failure in the report
	            test.log(LogStatus.FAIL, "Tenant deletion failed", e.getMessage());
	        }
	    }
}
	