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

public class SchemaController extends MyRunner{
	
	public void createSchema() throws IOException {
		 test = report.startTest("Neo4j_create schema");

	      RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/schemas?requesterType=TENANT";
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User Hits create schema request");

	     // ...
	     // ...

	        try {
	            // Load the Excel file
	            FileInputStream file = new FileInputStream("data/testData.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

	            // Read the input JSON body from Excel
	            String bodyString = sheet.getRow(4).getCell(1).getStringCellValue();

	            // Convert the JSON string to a JSONObject
	            JSONObject bodyJson = new JSONObject(bodyString);

	            // Generate a random tenantId using UUID and set it in the JSON object
	            String schemaId = UUID.randomUUID().toString();
	            bodyJson.put("schemaId", schemaId);

	            // Set the request JSON body
	            request.body(bodyJson.toString());
	            request.contentType(ContentType.JSON);

	            test.log(LogStatus.PASS, "User valid Body");
	            // Send the request and validate the response
	            Response response = request.post(tokenEndpoint);

	            response.then().log().all();
	            Assert.assertEquals(response.statusCode(), 201);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "schema creation Test Passed", "Response statuscode: " + response.statusCode());

	            // Parse the response JSON to extract the tenantId
	            schemaId = response.jsonPath().getString("schemaId");

	            // Store the extracted tenantId in the Excel sheet
	            Cell schemaIdCell = sheet.getRow(4).createCell(2);
	            schemaIdCell.setCellValue(schemaId);

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
	            test.log(LogStatus.FAIL, "schema CREATION FAILED", e.getMessage());
	        }
}
	public void retrieveAllSchemaNode() { 
		test = report.startTest("Neo4j_retrieve all schema node");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/618b6fdef5dacc0001a6b1b0/schemas?requesterType=TENANT";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "retrieve all schema node");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "Retrieved all schemaNode successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Retrieved all schemaNode statuscode: " +statuscode);
	    }
	}
	public void  retrieveaSchemaNodefromNeo4jDBbyschemaId() { 
		test = report.startTest("Neo4j_retrieve a Schema Node from Neo4jDB by schemaId");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/647e22b3bbce5500018719b/schemas/647e22b3bbce5500018719b?requesterType=USER";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "retrieve a Schema Node from Neo4jDB by schemaId");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "retrieve a Schema Node from Neo4jDB by schemaId successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to retrieve a Schema Node from Neo4jDB by schemaId statuscode: " +statuscode);
	    }
	}
	public void updateaSchemaNodeinNeo4jDB() throws IOException {
		 test = report.startTest("Neo4j_update a schema Node in Neo4jDB");
		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(4);
		    String requestBody = row.getCell(1).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "v1.0/606ed8616bf9930001f98fd4/schemas?requesterType=TENANT";

		    test.log(LogStatus.PASS, "User hits update a schema Node in Neo4jDB request");
		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);
		    test.log(LogStatus.INFO, "Request Body: " + requestBody);

		    RequestSpecification request = RestAssured.given().body(requestBody);
		    test.log(LogStatus.PASS, "User gives valid requestbody");

		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

		    if (statusCode == 200) {
		        test.log(LogStatus.PASS, "update a schema Node in Neo4jDB Successfully. statuscode: " + statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to update a schema Node in Neo4jDB statuscode: " + statusCode);
		    }

		    report.endTest(test);
		}
}


