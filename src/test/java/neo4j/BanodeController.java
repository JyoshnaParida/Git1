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

public class BanodeController extends MyRunner{
	public void createBANodeinNeo4jDB() throws IOException 
	{
		test = report.startTest("Neo4j_create BA Node in Neo4jDB");
		FileInputStream fi = new FileInputStream("data/testData.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet ws = wb.getSheet("Body");
		XSSFRow row = ws.getRow(17);
		String requestBody = row.getCell(1).getStringCellValue();
		//wb.close();
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/649459e72cc1a20001cbb68c/bas";
		test.log(LogStatus.PASS, "User hitscreate BA Node in Neo4jDB  request");
		test.log(LogStatus.INFO, "Request URL: " + RestAssured.baseURI + tokenEndpoint);
		test.log(LogStatus.INFO, "Request Body: " + requestBody);
		RequestSpecification request = RestAssured.given().body(requestBody);
		test.log(LogStatus.PASS, "User gives valid body");
		Response response = request.post(tokenEndpoint);
		response.then().log().all();
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		test.log(LogStatus.INFO, "Response Body: " + responseBody);
		if (statusCode == 200) {
			test.log(LogStatus.PASS, "create BA Node in Neo4jDB Successfully. statuscode: " + statusCode);
		} else 
		{
			test.log(LogStatus.FAIL, "Failed to create BA Node in Neo4jDB statuscode: " + statusCode);
		}
		report.endTest(test);
		 }


	
	public void updateaBANodeinNeo4jDB() throws Throwable
	{
	test=report.startTest("Neo4J_update a BANode in Neo4jDB");
	String baseURL="https://ingress-gateway.gaiansolutions.com/neo4j-service";
	String tokenendpoint="/v1.0/neo4j/node/ba";
	test.log(LogStatus.PASS,"User hits update a BANode in Neo4jDB request"); 
	   test.log(LogStatus.INFO,"Request URL:" +RestAssured.baseURI+tokenendpoint);
	test.log(LogStatus.INFO, "Request URL :"+baseURL);
	FileInputStream file = new FileInputStream("data/testData.xlsx");
	   XSSFWorkbook workbook = new XSSFWorkbook(file); 
    XSSFSheet sheet = workbook.getSheet("Body");
    XSSFRow row=sheet.getRow(18);
    String tenantBody = row.getCell(1).getStringCellValue();
	   test.log(LogStatus.INFO, "Response Body :"+tenantBody); 
	   RequestSpecification request = RestAssured.given().body(tenantBody).log().all();
	   test.log(LogStatus.PASS, "User gives valid body");
	request.baseUri(baseURL);
	Response response=request.put(tokenendpoint);
	response.then().log().all();
	   int statusCode = response.getStatusCode();
	   String responsebody=response.getBody().asString();
	   if (statusCode == 200)
	   {
		   
		   test.log(LogStatus.PASS, "update a BANode in Neo4jDB Successfully.statuscode:"+statusCode);
	   }else
	   {
		   test.log(LogStatus.FAIL, "Failed to update a BANode in Neo4jDB statuscode: " + statusCode);
	   }
	   report.endTest(test);
    
	}

	
	
	public void retrieveallBANodefromNeo4jDB() {
		
		test=report.startTest("Neo4J_retrieve all BA Node from Neo4jDB");
		 RequestSpecification request = RestAssured.given();
		 String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/ba";
		 test.log(LogStatus.INFO, "Request URL: " + baseURL);
		 request.baseUri(baseURL);
		 Response response = request.get();
		 response.then().log().all();
		 test.log(LogStatus.PASS, "User hits requesterType");
		 int statuscode = response.getStatusCode();
		 String responseBody = response.getBody().asString();
		 test.log(LogStatus.PASS, "Response Body: " + responseBody);
		 if (statuscode == 200) {
		 test.log(LogStatus.PASS, "retrieve all BA Node from Neo4jDB successfully statuscode: " +statuscode);
		 }
		 else {
			  test.log(LogStatus.FAIL, "Failed to retrieve all BA Node from Neo4jDB type statuscode: " + statuscode); 
			}
	    report.endTest(test);
	}
	
public void retrieveallBANodefromNeo4jDBbaId() {
		
		test=report.startTest("Neo4J_retrieve all BANode from Neo4j DB baId");
		 RequestSpecification request = RestAssured.given();
		 String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/ba/ba6981b350001bf2f05";
		 test.log(LogStatus.INFO, "Request URL: " + baseURL);
		 request.baseUri(baseURL);
		 Response response = request.get();
		 response.then().log().all();
		 test.log(LogStatus.PASS, "User hits retrieve all BANode from Neo4j DB baId");
		 int statuscode = response.getStatusCode();
		 String responseBody = response.getBody().asString();
		 test.log(LogStatus.PASS, "Response Body: " + responseBody);
		 if (statuscode == 200) {
		 test.log(LogStatus.PASS, "retrieve all BANode from Neo4j DB baId successfully statuscode: " +statuscode);
		 }
		 else {
			  test.log(LogStatus.FAIL, "Failed to retrieve all BANode from Neo4j DB baId type statuscode: " + statuscode); 
			}
	    report.endTest(test);
	}

}




