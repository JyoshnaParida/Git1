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

public class GroupNodeController extends MyRunner{
	
	public void createGroupNode() throws IOException {
		 test = report.startTest("Neo4j_create GroupNode");
		 RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/groups?requesterType=TENANT";
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User hits create Group Node request");

	     // ...
	     // ...

	        try {
	            // Load the Excel file
	            FileInputStream file = new FileInputStream("data/testData.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

	            // Read the input JSON body from Excel
	            String bodyString = sheet.getRow(6).getCell(1).getStringCellValue();

	            // Convert the JSON string to a JSONObject
	            JSONObject bodyJson = new JSONObject(bodyString);

	            // Generate a random tenantId using UUID and set it in the JSON object
	            String groupId = UUID.randomUUID().toString();
	            bodyJson.put("groupId", groupId);

	            // Set the request JSON body
	            request.body(bodyJson.toString());
	            request.contentType(ContentType.JSON);

	            test.log(LogStatus.PASS, "User valid Body");
	            // Send the request and validate the response
	            Response response = request.post(tokenEndpoint);

	            response.then().log().all();
	            Assert.assertEquals(response.statusCode(), 201);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "Group creation test passed", "Response statuscode: " + response.statusCode());

	            // Parse the response JSON to extract the tenantId
	            groupId = response.jsonPath().getString("groupId");

	            // Store the extracted tenantId in the Excel sheet
	            Cell groupIdCell = sheet.getRow(6).createCell(2);
	            groupIdCell.setCellValue(groupId);

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
	            test.log(LogStatus.FAIL, "Group CREATION FAILED", e.getMessage());
	        }
}
	
	public void retrieveaGroupNodefromNeo4jDBbyGroupId() { 
		test = report.startTest("Neo4j_Retrieve a Group Node from Neo4jDB by GroupId");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/618b6fdef5dacc0001a6b1b0/groups/6347d79b7b7aa2000110866e?requesterType=TENANT";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "Retrieve a Group Node from Neo4jDB by GroupId");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "Retrieve a Group Node from Neo4jDB by GroupId successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to retrieved a Group Node from Neo4jDB by GroupId statuscode: " +statuscode);
	    }
	    report.endTest(test);

	}
	
	public void updateaGroupNodeinNeo4jDB() throws IOException {
		 test = report.startTest("Neo4j_update a Group Node in Neo4jDB");
		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(12);
		    String requestBody = row.getCell(1).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "/v1.0/neo4j/node/group";

		    test.log(LogStatus.PASS, "User hits update a GroupNode in Neo4jDB request");
		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);
		    test.log(LogStatus.INFO, "Request Body: " + requestBody);

		    RequestSpecification request = RestAssured.given().body(requestBody);
		    test.log(LogStatus.PASS, "User gives valid request body");

		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

		    if (statusCode == 200) {
		        test.log(LogStatus.PASS, "Update a Group Node in Neo4jDB Successfull statuscode: " + statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to update a Group Node in Neo4jDB statuscode: " + statusCode);
		    }

		    report.endTest(test);
		}
	
	public void retrieveallGroupNode() { 
		
		test = report.startTest("Neo4j_Retrieve all GroupNode");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/618b6fdef5dacc0001a6b1b0/groups?requesterType=TENANT";
	    
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "Retrieve all GroupNode");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "Retrieve all GroupNode successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to retrieveallGroupNode statuscode: " +statuscode);
	    }
	    report.endTest(test);

	}
}



