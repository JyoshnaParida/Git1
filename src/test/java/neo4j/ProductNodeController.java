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

public class ProductNodeController extends MyRunner {
	
	public void createProductNode() throws IOException {
		test = report.startTest("Neo4j_Create Product");
	    FileInputStream fi = new FileInputStream("data/testData.xlsx");
	    XSSFWorkbook wb = new XSSFWorkbook(fi);
	    XSSFSheet ws = wb.getSheet("Body");

	    XSSFRow row = ws.getRow(20);
	    String requestBody = row.getCell(1).getStringCellValue();

	    wb.close();

	    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/product";
		
	    test.log(LogStatus.PASS, "User hits Create Product request");
	    test.log(LogStatus.INFO, "Request URL:" +"    " + RestAssured.baseURI + tokenEndpoint);
	    test.log(LogStatus.INFO, "Request Body: " + requestBody);
	    RequestSpecification request = RestAssured.given().body(requestBody);
	    test.log(LogStatus.PASS, "User gives valid Request body");

	    Response response = request.post(tokenEndpoint);
	    int statusCode = response.getStatusCode();

	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);

	    if (statusCode == 200) {
	        test.log(LogStatus.PASS, "Successfully Created ProductID. statuscode: " + statusCode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Create ProductID. statuscode: " + statusCode);
	    }

	    report.endTest(test);
	}
	public void createAlliance() throws IOException {
		test = report.startTest("Neo4j_Create Alliance");
	    FileInputStream fi = new FileInputStream("data/testData.xlsx");
	    XSSFWorkbook wb = new XSSFWorkbook(fi);
	    XSSFSheet ws = wb.getSheet("Body");

	    XSSFRow row = ws.getRow(21);
	    String requestBody = row.getCell(1).getStringCellValue();

	    wb.close();

	    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/product/alliance";
		
	    test.log(LogStatus.PASS, "User hits Create Alliance request");
	    test.log(LogStatus.INFO, "Request URL:" +"    " + RestAssured.baseURI + tokenEndpoint);
	    test.log(LogStatus.INFO, "Request Body: " + requestBody);
	    RequestSpecification request = RestAssured.given().body(requestBody);
	    test.log(LogStatus.PASS, "User gives valid Request body");

	    Response response = request.post(tokenEndpoint);
	    int statusCode = response.getStatusCode();

	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);

	    if (statusCode == 200) {
	        test.log(LogStatus.PASS, "Successfully Created AllianceID. statuscode: " + statusCode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Create AllianceID. statuscode: " + statusCode);
	    }

	    report.endTest(test);
	}
	public void getAllProducts() {

		test = report.startTest("Neo4j_Get All Products");
		RequestSpecification request = RestAssured.given();

		String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/product";
		test.log(LogStatus.INFO, "Request URL: " + baseURL);
		request.baseUri(baseURL);

		Response response = request.get();
		response.then().log().all();

		test.log(LogStatus.PASS, "User hits GetAllProducts request");
		int statuscode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		test.log(LogStatus.INFO, "Response Body: " + responseBody);
		if (statuscode == 200) {
			test.log(LogStatus.PASS, "Retrieved AllProducts successfully statuscode:" + statuscode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrive AllProducts statuscode: " + statuscode);
		}
	}

	public void getProductID() {
		
		  test = report.startTest("Neo4j_Get Product ID");
	  	    RequestSpecification request = RestAssured.given();
	  	    
	  	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/product/IAMb6fdef5dacc0001a6b1b0";
	  	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	  	    request.baseUri(baseURL);
//	  	    .param("Id", "engagement6981b350001bf189");

	  	    Response response = request.get();
	  	    response.then().log().all();
	  	    
	  	    test.log(LogStatus.PASS, "User hits GetProductID request");
	  	    int statuscode = response.getStatusCode();
	  	    String responseBody = response.getBody().asString();
	  	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	  	    if (statuscode == 200) {
	  	        test.log(LogStatus.PASS, "Retrieved ProductID successfully statuscode:" +statuscode);
	  	    } else {
	  	        test.log(LogStatus.FAIL, "Failed to Retrive ProductID statuscode: " +statuscode);
	  	    }
	}


}
