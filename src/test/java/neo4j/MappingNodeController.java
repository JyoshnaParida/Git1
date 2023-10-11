package neo4j;
import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;
import com.sun.mail.imap.protocol.BODY;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class MappingNodeController extends MyRunner {

	public void createMappingNode() throws IOException {
		test = report.startTest("NEO4J_Create Mapping Node");
		FileInputStream fi = new FileInputStream("data/testData.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet ws = wb.getSheet("Body");

		XSSFRow row = ws.getRow(13);
		String requestBody = row.getCell(1).getStringCellValue();

		wb.close();
		
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/1210022/mappings?requesterType=TENANT";

		test.log(LogStatus.PASS, "User hits the Create Mapping Node controller");

		test.log(LogStatus.INFO, "Request URL:" + "   " +RestAssured.baseURI + tokenEndpoint);
	    test.log(LogStatus.PASS, "User gives valid request body");

	    test.log(LogStatus.INFO, "Request Body: " + requestBody);

		RequestSpecification request = RestAssured.given().body(requestBody);
//	    test.log(LogStatus.PASS, "User gives valid Request body");
		Response response = request.post(tokenEndpoint);
		int statusCode = response.getStatusCode();

	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);

		
		
//			test.log(LogStatus.PASS, "CreateMappingNode");
//
//		int statusCode = response.getStatusCode();
//		ResponseBody ResponseData = response.getBody();
//

		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Create Mapping Node created successful" + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to create mapping node status code: " + statusCode);
		}

		report.endTest(test);
	}
	public void getMappingNodeById() {
		test = report.startTest("Neo4j_Get MappingNode By Id");
		RequestSpecification request = RestAssured.given();
		 String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/engagement/v1.0/1455/mappings/11255?requesterType=TENANT";
//		 String tokenEndpoint = "/v1.0/1455/mappings/11255?requesterType=TENANT";
		 test.log(LogStatus.INFO, "Request URL: " + baseURL);
		 request.baseUri(baseURL);
//		 request.baseUri(baseURL).param(baseURL, "engagement6981b350001bf189");

		 Response response = request.get();
		 response.then().log().all();
		 test.log(LogStatus.PASS, "User hits get Mapping Node by Id request");
		 int statuscode = response.getStatusCode();
		 String responseBody = response.getBody().asString();
		 test.log(LogStatus.INFO, "Response Body: " + responseBody);
		 if (statuscode == 200) {
		    test.log(LogStatus.PASS, "Retrieved get Mapping Node By Id successfully statuscode:" +statuscode);
		  } else {
	     test.log(LogStatus.FAIL, "Failed to retrived get mappingNode By Id statuscode: " +statuscode);
		  }
	}
	public void updateMappingNode() throws IOException {
		 test = report.startTest("Neo4j_Update MappingNode");
		 FileInputStream fi = new FileInputStream("data/testData.xlsx");
		 XSSFWorkbook wb = new XSSFWorkbook(fi);
		 XSSFSheet ws = wb.getSheet("Body");

		 XSSFRow row = ws.getRow(14);
		 String requestBody = row.getCell(1).getStringCellValue();

		  wb.close();

		  RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		 String tokenEndpoint = "/v1.0/11155/mappings/1144?requesterType=TENANT";
		 test.log(LogStatus.PASS, "User hits update MappingNode request");
		 test.log(LogStatus.INFO, "Request URL: " + "    " + RestAssured.baseURI + tokenEndpoint);
		 test.log(LogStatus.PASS, "User gives valid request body");

		 test.log(LogStatus.INFO, "Request Body: " + requestBody);
//		 test.log(LogStatus.PASS, "User gives valid Request body");
		 RequestSpecification request = RestAssured.given().body(requestBody);

		  Response response = request.put(tokenEndpoint);
		  int statusCode = response.getStatusCode();

		 String responseBody = response.getBody().asString();
		 test.log(LogStatus.INFO, "Response Body: " + responseBody);

		 if (statusCode == 200) {
	    test.log(LogStatus.PASS, "Successfully update MappingNode statuscode: " + statusCode);
		  } else {
		     test.log(LogStatus.FAIL, "Failed to update MappingNode statuscode: " + statusCode);
		  }

	   report.endTest(test);
	}

		 public void getAllMappingNodes() {

		test = report.startTest("Neo4j_Get All MappingNodes");
		 RequestSpecification request = RestAssured.given();

		 String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/mapping";
		 test.log(LogStatus.INFO, "Request URL: " + baseURL);
		 request.baseUri(baseURL);

		 Response response = request.get();
		 response.then().log().all();

		 test.log(LogStatus.PASS, "User hits get all MappingNodes request");
		 int statuscode = response.getStatusCode();
		 String responseBody = response.getBody().asString();
		 test.log(LogStatus.INFO, "Response Body: " + responseBody);
		 
		 if (statuscode == 200) {
		 test.log(LogStatus.PASS, "Retrieved get all MappingNodes successfully statuscode:" + statuscode);
		} 
		 else 
		 {
		test.log(LogStatus.FAIL, "Failed to retrive get all MappingNodes statuscode: " + statuscode);
		 }
		  report.endTest(test);
		 }

}


