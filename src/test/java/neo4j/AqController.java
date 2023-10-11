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
import io.restassured.specification.RequestSpecification;

public class AqController extends MyRunner{
	
	public void createAQNode() throws IOException {
		 test = report.startTest("Neo4j_create AQ Node");
		 RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "/v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/aqs?requesterType=TENANT";
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User Hits create AQ Node request");

	     // ...
	     // ...

	        try {
	            // Load the Excel file
	            FileInputStream file = new FileInputStream("data/testData.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

	            // Read the input JSON body from Excel
	            String bodyString = sheet.getRow(5).getCell(1).getStringCellValue();

	            // Convert the JSON string to a JSONObject
	            JSONObject bodyJson = new JSONObject(bodyString);

	            // Generate a random tenantId using UUID and set it in the JSON object
	            String aqId = UUID.randomUUID().toString();
	            bodyJson.put("aqId", aqId);

	            // Set the request JSON body
	            request.body(bodyJson.toString());
	            request.contentType(ContentType.JSON);

	            test.log(LogStatus.PASS, "User valid Body");
	            // Send the request and validate the response
	            Response response = request.post(tokenEndpoint);

	            response.then().log().all();
	            Assert.assertEquals(response.statusCode(), 200);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "AQ creation Test Passed", "Response statuscode: " + response.statusCode());

	            // Parse the response JSON to extract the tenantId
	            aqId = response.jsonPath().getString("aqId");

	            // Store the extracted tenantId in the Excel sheet
	            Cell aqIdCell = sheet.getRow(5).createCell(2);
	            aqIdCell.setCellValue(aqId);

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
	            test.log(LogStatus.FAIL, "aq CREATION FAILED", e.getMessage());
	        }
}
	public void retrieveaAQNodefromNeo4jDBbyaqId() { 
		test = report.startTest("Neo4j_retrieve a AQ Node from Neo4j DB by aq Id");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/node/context/618b6fdef5dacc0001a6b1b0";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "retrieve a AQ Node from Neo4j DB by aq Id");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "retrieve a AQ Node from Neo4j DB by aq Id successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to retrieve a AQ Node from Neo4j DB by aq Id statuscode: " +statuscode);
	    }
	}
	public void  retrieveallAQNodefromNeo4jDB() { 
		test = report.startTest("Neo4j_Retrieve all AQ Node from Neo4jDB");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/context";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "Retrieve all AQ Node from Neo4jDB");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "retrieve all AQ Node from Neo4jDB successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to retrieve all AQ Node from Neo4jDB statuscode: " +statuscode);
	    }
	}

	public void updateaAQNodeinNeo4jDB() throws IOException {
		 test = report.startTest("Neo4j_update a AQ Node in Neo4jDB");
		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(10);
		    String requestBody = row.getCell(1).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "v1.0/618b6fdef5dacc0001a6b1b0/aqs/649459e72cc1a20001cbb68b?requesterType=TENANT";

		    test.log(LogStatus.PASS, "User hits update a AQ Node  in Neo4jDB request");
		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);

		    RequestSpecification request = RestAssured.given().body(requestBody);
		    test.log(LogStatus.PASS, "User gives valid requestBody");
		    test.log(LogStatus.INFO, "Request Body: " + requestBody);


		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

		    if (statusCode == 200) {
		        test.log(LogStatus.PASS, "update a AQ Node in Neo4jDB Successfully. statuscode: " + statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to update a AQ Node in Neo4jDB statuscode: " + statusCode);
		    }

		    report.endTest(test);
		}
	
	public void retrieveaAQNodefromNeo4jDBbyaqId1() throws IOException {
		 test = report.startTest("Neo4j_retrieve a AQ Node from Neo4j DB by aq Id1");
		 RequestSpecification request = RestAssured.given();
		    
		    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/user/0001";
		    test.log(LogStatus.INFO, "Request URL: " + baseURL);
		    request.baseUri(baseURL);

		    Response response = request.get();
		    response.then().log().all();
		    
		    test.log(LogStatus.PASS, "retrieve a AQ Node from Neo4j DB by aq Id1");
		    int statuscode = response.getStatusCode();
		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);
		    if (statuscode == 200) {
		        test.log(LogStatus.PASS, "retrieve a AQ Node from Neo4j DB by aq Id1 successfully statuscode:" +statuscode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to retrieve a AQ Node from Neo4j DB by aq Id1 statuscode: " +statuscode);
		    }
		}

}