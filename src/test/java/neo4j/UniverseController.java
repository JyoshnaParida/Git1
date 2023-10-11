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

public class UniverseController extends MyRunner{

	public void createUniverse() throws IOException {
        test = report.startTest("Neo4j_create universe");

	      RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/universes?requesterType=TENANT";
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User Hits create universe request");

	     // ...
	     // ...

	        try {
	            // Load the Excel file
	            FileInputStream file = new FileInputStream("data/testData.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

	            // Read the input JSON body from Excel
	            String bodyString = sheet.getRow(3).getCell(1).getStringCellValue();

	            // Convert the JSON string to a JSONObject
	            JSONObject bodyJson = new JSONObject(bodyString);

	            // Generate a random tenantId using UUID and set it in the JSON object
	            String universeId = UUID.randomUUID().toString();
	            bodyJson.put("universeId", universeId);

	            // Set the request JSON body
	            request.body(bodyJson.toString());
	            request.contentType(ContentType.JSON);

	            test.log(LogStatus.PASS, "User valid Body");
	            // Send the request and validate the response
	            Response response = request.post(tokenEndpoint);

	            response.then().log().all();
	            Assert.assertEquals(response.statusCode(), 201);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "universe creation Test Passed", "Response statuscode: " + response.statusCode());

	            // Parse the response JSON to extract the tenantId
	            universeId = response.jsonPath().getString("universeId");

	            // Store the extracted tenantId in the Excel sheet
	            Cell tenantIdCell = sheet.getRow(3).createCell(2);
	            tenantIdCell.setCellValue(universeId);

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
	            test.log(LogStatus.FAIL, "universe CREATION FAILED", e.getMessage());
	        }
}
	
//		 test = report.startTest("Neo4j_create Universe");
//		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
//		    XSSFWorkbook wb = new XSSFWorkbook(fi);
//		    XSSFSheet ws = wb.getSheet("Body");
//
//		    XSSFRow row = ws.getRow(2);
//		    String requestBody = row.getCell(1).getStringCellValue();
//
//		    wb.close();
//
//		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
//		    String tokenEndpoint = "/v1.0/001/universes?requesterType=USER";
//
//		    test.log(LogStatus.PASS, "User hits create Universe request");
//		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);
//
//		    RequestSpecification request = RestAssured.given().body(requestBody);
//		    test.log(LogStatus.PASS, "User gives valid requestbody");
//		    test.log(LogStatus.INFO, "Request Body: " + requestBody);
//
//
//		    Response response = request.post(tokenEndpoint);
//		    int statusCode = response.getStatusCode();
//
//		    String responseBody = response.getBody().asString();
//		    test.log(LogStatus.INFO, "Response Body: " + responseBody);
//
//		    if (statusCode == 200) {
//		        test.log(LogStatus.PASS, "create Universe Successfully statuscode: " + statusCode);
//		    } else {
//		        test.log(LogStatus.FAIL, "Failed to create Universe statuscode: " + statusCode);
//		    }
//
//		    report.endTest(test);
//		}
	public void updateUniverse() throws IOException { 
		test = report.startTest("Neo4j_Update Universe");
		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(3);
		    String requestBody = row.getCell(1).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "v1.0/001/universes/001?requesterType=USER";

		    test.log(LogStatus.PASS, "User hits Update Universe request");
		    test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);

		    RequestSpecification request = RestAssured.given().body(requestBody);
		    test.log(LogStatus.PASS, "User gives valid requestbody");
		    test.log(LogStatus.INFO, "Request Body: " + requestBody);


		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

		    if (statusCode == 200) {
		        test.log(LogStatus.PASS, "Update Universe Successfully statuscode: " + statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to Update Universe statuscode: " + statusCode);
		    }

		    report.endTest(test);
		}
	public void retrieveallUniverseNodefromNeo4jDB() { 
		test = report.startTest("Neo4j_retrieve all Universe NodefromNeo4jDB");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/606fe4aa2cf0760001832be0/universes?requesterType=TENANT";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "retrieve all Universe Node from Neo4jDB");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "retrieve all Universe Node from Neo4jDB successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to rretrieve all Universe Node from Neo4jDB statuscode: " +statuscode);
	    }
	}
	public void apitoretrieveaUniverseNodefromNeo4jDBbyuniverseId() { 
		test = report.startTest("Neo4j_api to retrieve a Universe Node from Neo4j DB by universeId");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/606fe4aa2cf0760001832be0/universes/64b8cd5dd12e4d0001147c21?requesterType=TENANT";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "api to retrieve a Universe Node from Neo4j DB by universeId");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "api to retrieve a Universe Node from Neo4j DB by universeId successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to api to retrieve a Universe Node from Neo4j DB by universeId  statuscode: " +statuscode);
	    }
	}
	public void allUniverseNodefromNeo4jDBalongwithdbcount() { 
		test = report.startTest("Neo4j_all Universe Nodefrom Neo4jDB along with dbcount");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v2.0/606fe4aa2cf0760001832be0/universes?requesterType=TENANT";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "all Universe Nodefrom Neo4jDB along with dbcount");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "Retrieved all Universe Nodefrom Neo4jDB along with dbcount successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Retrieved all Universe Nodefrom Neo4jDB along with dbcount statuscode: " +statuscode);
	    }
	}
	public void allUniverseNodefromNeo4jDBalongwithdbcount1() { 
		test = report.startTest("Neo4j_all Universe Node from Neo4j DB along withdb count1");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v2.1/606fe4aa2cf0760001832be0/universes?requesterType=TENANT";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "Retrieved all Universe Node from Neo4j DB along withdb count1");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "Retrieved all Universe Node from Neo4j DB along withdb count successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Retrieved all Universe Node from Neo4j DB along withdb count statuscode: " +statuscode);
	    }
	}
}

