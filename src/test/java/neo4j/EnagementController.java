package neo4j;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EnagementController extends MyRunner{
	
	public void createEnagement() throws IOException {
		test = report.startTest("Neo4j_CreateEngagement");
	    FileInputStream fi = new FileInputStream("data/testData.xlsx");
	    XSSFWorkbook wb = new XSSFWorkbook(fi);
	    XSSFSheet ws = wb.getSheet("Body");

	    XSSFRow row = ws.getRow(19);
	    String requestBody = row.getCell(1).getStringCellValue();

	    wb.close();

	    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v1.0/neo4j/node/engagement";
		
	    test.log(LogStatus.PASS, "User hits create Engagement request");
	    test.log(LogStatus.INFO, "Request URL:" +"    " + RestAssured.baseURI + tokenEndpoint);
	    test.log(LogStatus.INFO, "Request Body: " + requestBody);
	    RequestSpecification request = RestAssured.given().body(requestBody);
	    test.log(LogStatus.PASS, "User gives valid Request body");

	    Response response = request.post(tokenEndpoint);
	    int statusCode = response.getStatusCode();

	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);

	    if (statusCode == 200) {
	        test.log(LogStatus.PASS, "Successfully created EnagagementID statuscode: " + statusCode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to create EngagementID statuscode: " + statusCode);
	    }

	    report.endTest(test);
	}
	public void updateEnagement() throws IOException {
		test = report.startTest("Neo4j_UpdateEngagement");
	    FileInputStream fi = new FileInputStream("data/testData.xlsx");
	    XSSFWorkbook wb = new XSSFWorkbook(fi);
	    XSSFSheet ws = wb.getSheet("Body");

	    XSSFRow row = ws.getRow(20);
	    String requestBody = row.getCell(1).getStringCellValue();

	    wb.close();

	    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/engagement";
		
	    test.log(LogStatus.PASS, "User hits update engagement request");
	    test.log(LogStatus.INFO, "Request URL:" +"    " + RestAssured.baseURI + tokenEndpoint);
	    test.log(LogStatus.INFO, "Request Body: " + requestBody);
	    
	    test.log(LogStatus.PASS, "User gives valid request body");
	    RequestSpecification request = RestAssured.given().body(requestBody);

	    Response response = request.put(tokenEndpoint);
	    int statusCode = response.getStatusCode();

	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);

	    if (statusCode == 200) {
	        test.log(LogStatus.PASS, "Successfully update EngagementID statuscode: " + statusCode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to update EngagementID statuscode: " + statusCode);
	    }

	    report.endTest(test);
	}

	public void getAllEngagements() {

		test = report.startTest("Neo4j_GetAllEngagements");
		RequestSpecification request = RestAssured.given();

		String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/engagement";
		test.log(LogStatus.INFO, "Request URL: " + baseURL);
		request.baseUri(baseURL);

		Response response = request.get();
		response.then().log().all();

		test.log(LogStatus.PASS, "User hits GetAllEngagements request");
		int statuscode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		test.log(LogStatus.INFO, "Response Body: " + responseBody);
		if (statuscode == 200) {
			test.log(LogStatus.PASS, "Retrieved AllEngagements successfully statuscode: " + statuscode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrive AllEnagements statuscode: " + statuscode);
		}
	}
	public void getEngagementID() {
		
		  test = report.startTest("Neo4j_Get Engagements ID");
	  	    RequestSpecification request = RestAssured.given();
	  	    
	  	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/engagement/engagement6981b350001bf189";
	  	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	  	    request.baseUri(baseURL);
//	  	    .param("Id", "engagement6981b350001bf189");

	  	    Response response = request.get();
	  	    response.then().log().all();
	  	    
	  	    test.log(LogStatus.PASS, "User hits get Engagements ID request");
	  	    int statuscode = response.getStatusCode();
	  	    String responseBody = response.getBody().asString();
	  	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	  	    if (statuscode == 200) {
	  	        test.log(LogStatus.PASS, "Retrieved EngagementID successfully statuscode:" +statuscode);
	  	    } else {
	  	        test.log(LogStatus.FAIL, "Failed to retrive EnagementID statuscode: " +statuscode);
	  	    }
	}
}