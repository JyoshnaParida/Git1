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

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;

public class UserNodeController extends MyRunner{
	public void createUser() throws IOException {
		test = report.startTest("Neo4j_Create User");
        RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
        String tokenEndpoint = "/v1.0/neo4j/node/user";
        RequestSpecification request = RestAssured.given();
        test.log(LogStatus.PASS, "User Hits Create User request");
        try {
            // Load the Excel file
            FileInputStream file = new FileInputStream("data/testData.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

            // Read the input JSON body from Excel
            String bodyString = sheet.getRow(2).getCell(1).getStringCellValue();

            // Convert the JSON string to a JSONObject
            JSONObject bodyJson = new JSONObject(bodyString);

            // Generate a random tenantId using UUID and set it in the JSON object
            String userId = UUID.randomUUID().toString();
            bodyJson.put("userId", userId);

            // Set the request JSON body
            request.body(bodyJson.toString());
            request.contentType(ContentType.JSON);

            test.log(LogStatus.PASS, "User valid Body");
            // Send the request and validate the response
            Response response = request.post(tokenEndpoint);

            response.then().log().all();
            Assert.assertEquals(response.statusCode(), 201);

            // Log the test result in the report
            test.log(LogStatus.PASS, "User Creation Test Passed", "Response statuscode: " + response.statusCode());

            // Parse the response JSON to extract the tenantId
            userId = response.jsonPath().getString("userId");

            // Store the extracted tenantId in the Excel sheet
            Cell userIdCell = sheet.getRow(2).createCell(2);
            userIdCell.setCellValue(userId);

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
            test.log(LogStatus.FAIL, "User creation failed", e.getMessage());
        }
}

	
	

//	public void createUser() throws IOException {
//		 test = report.startTest("Neo4j_create User");
//		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
//		    XSSFWorkbook wb = new XSSFWorkbook(fi);
//		    XSSFSheet ws = wb.getSheet("Body");
//
//		    XSSFRow row = ws.getRow(1);
//		    String requestBody = row.getCell(1).getStringCellValue();
//
//		    wb.close();
//
//		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
//		    String tokenEndpoint = "/v1.0/neo4j/node/user";
//
//		    test.log(LogStatus.PASS, "User hits create User request");
//		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);
//
//		    RequestSpecification request = RestAssured.given().body(requestBody);
//		    test.log(LogStatus.PASS, "User gives valid requestBody");
//		    test.log(LogStatus.INFO, "Request Body: " + requestBody);
//
//		    Response response = request.post(tokenEndpoint);
//		    int statusCode = response.getStatusCode();
//
//		    String responseBody = response.getBody().asString();
//		    test.log(LogStatus.INFO, "Response Body: " + responseBody);
//
//		    if (statusCode == 200) {
//		        test.log(LogStatus.PASS, "create User Successfully. statuscode: " + statusCode);
//		    } else {
//		        test.log(LogStatus.FAIL, "Failed to create User. statuscode: " + statusCode);
//		    }
//
//		    report.endTest(test);
//		}
	public void getAllUserNodes() { 
		test = report.startTest("Neo4j_get All UserNodes");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/user";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "Retrieved All User Nodes");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "Retrieved AllUserNodes successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Retrieved AllUserNodes statuscode: " +statuscode);
	    }
	}
	public void  retrieveaUserNodefromNeo4jDBbyid() { 
		test = report.startTest("Neo4j_retrieve a UserNode fromNeo4j DB by id");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/user/0001";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "retrieve a UserNode fromNeo4j DB by id");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "retrieve a UserNode fromNeo4j DB by id successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Retrieved retrieve a UserNode fromNeo4j DB by id statuscode: " +statuscode);
	    }
	}

	public void updateUserNode() throws IOException {
		 test = report.startTest("Neo4j_update UserNode");
		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(7);
		    String requestBody = row.getCell(1).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "/v1.0/neo4j/node/user";

		    test.log(LogStatus.PASS, "User hits update UserNode request");
		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);

		    RequestSpecification request = RestAssured.given().body(requestBody);
		    test.log(LogStatus.PASS, "User gives valid requestBody");
		    test.log(LogStatus.INFO, "Request Body: " + requestBody);


		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

		    if (statusCode == 200) {
		        test.log(LogStatus.PASS, "update UserNode Successfully statuscode: " + statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to update UserNode statuscode: " + statusCode);
		    }

		    report.endTest(test);
		}
	
	public void contructAccessiblity() throws IOException {
		 test = report.startTest("Neo4j_construct Accessiblity");
		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(8);
		    String requestBody = row.getCell(1).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "/v1.0/neo4j/node/user";

		    test.log(LogStatus.PASS, "User hits construct Accessiblity request");
		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);

		    RequestSpecification request = RestAssured.given().body(requestBody);
		    test.log(LogStatus.PASS, "User gives valid requestBody");
		    test.log(LogStatus.INFO, "Request Body: " + requestBody);


		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

		    if (statusCode == 200) {
		        test.log(LogStatus.PASS, "construct Accessiblity Successfully. statuscode: " + statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to construct Accessiblity statuscode: " + statusCode);
		    }

		    report.endTest(test);
		}
	

}
