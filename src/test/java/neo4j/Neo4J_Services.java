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

public class Neo4J_Services extends MyRunner{
		 public void usernode() throws IOException {
			    test = report.startTest("Neo4J_Usernode");
		        RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		        String tokenEndpoint = "/v1.0/neo4j/node/tenant";
		        RequestSpecification request = RestAssured.given();
		        test.log(LogStatus.PASS, "User Hits createusernode request");

		     // ...
		     // ...

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
		            Assert.assertEquals(response.statusCode(), 200);

		            // Log the test result in the report
		            test.log(LogStatus.PASS, "User Creation Test Passed", "Response Status Code: " + response.statusCode());

		            // Parse the response JSON to extract the tenantId
		            userId = response.jsonPath().getString("userId");

		            // Store the extracted tenantId in the Excel sheet
		            Cell tenantIdCell = sheet.getRow(2).createCell(2);
		            tenantIdCell.setCellValue(userId);

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
		            test.log(LogStatus.FAIL, "Tenant CREATION FAILED", e.getMessage());
		        }
	}
		 public void allUsernode() throws IOException {
		     test = report.startTest("Neo4J_AllUsernode");

		     String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		     String tokenEndpoint = "/v1.0/neo4j/node/user";
		     test.log(LogStatus.PASS, "User hits AllUsernode Request");

		     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
		     int statusCode = response.getStatusCode();

		     if (statusCode == 200) {
		         test.log(LogStatus.PASS, "User retrieve AllUsernode successful"+ statusCode);
		     } else {
		         test.log(LogStatus.FAIL, "Failed to retrieve AllUsernode. Status code: " + statusCode);
		     }

		     report.endTest(test);
		 }

	 
	 public void userId() {
	     test = report.startTest("Neo4J_UserId");
	     String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	     String tokenEndpoint = "/v1.0/neo4j/node/user/018b6fdef5dacc0001a6b001";
	     test.log(LogStatus.PASS, "User hits UserId Request");
	     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
	     int statusCode = response.getStatusCode();

	     if (statusCode == 200) {
	         test.log(LogStatus.PASS, "User retrieve UserId request successful"+ statusCode);
	     } else {
	         test.log(LogStatus.FAIL, "Failed to retrieve UserId. Status code: " + statusCode);
	     }

	     report.endTest(test);
	 }

	 public void constructsByAccessType() {
	     test = report.startTest("Neo4J_ConstructsByAccessType");
	     String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	     String tokenEndpoint = "v1.0/neo4j/node/user/018b6fdef5dacc0001a6b001/constructs?accessType=ALL";
	     test.log(LogStatus.PASS, "ConstructsByAccessType");


	     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
	     int statusCode = response.getStatusCode();

	     if (statusCode == 200) {
	         test.log(LogStatus.PASS, "AllUsernode request successful"+ statusCode);
	     } else {
	         test.log(LogStatus.FAIL, "Failed to retrieve AllUsernode. Status code: " + statusCode);
	     }

	     report.endTest(test);
	 }
	 public void universe() throws IOException {
	     test = report.startTest("Neo4J_CreateUniverse");

		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(1);
		    String requestBody = row.getCell(2).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "/v1.0/neo4j/node/user";
		    
	        test.log(LogStatus.PASS, "User hits Universe Request");

		    RequestSpecification request = RestAssured.given().body(requestBody);
	        test.log(LogStatus.PASS, "User gives valid body");


		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    if (statusCode == 201) {
		        test.log(LogStatus.PASS, "Universe created successfully"+ statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to create Universe. Status code: " + statusCode);
		    }

		    report.endTest(test);
		}


		public void getByIdUniverse() {
			test = report.startTest("Neo4J_GetByIdUniverse");
		     String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		     String tokenEndpoint = "v1.0/63722f09ef3502000174ef7d/universes/648ad96627e42d0001aa5ec0";
		     test.log(LogStatus.PASS, "GetByIdUniverse");


		     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
		     int statusCode = response.getStatusCode();

		     if (statusCode == 200) {
		         test.log(LogStatus.PASS, "GetByIdUniverse request successful"+ statusCode);
		     } else {
		         test.log(LogStatus.FAIL, "Failed to retrieve GetByIdUniverse. Status code: " + statusCode);
		     }

		     report.endTest(test);
		 }

			

		public void getAllUniverse() {
			test = report.startTest("Neo4J_GetAllUniverse");
			 String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		     String tokenEndpoint = "/v1.0/0001/universes/0001";
		     test.log(LogStatus.PASS, "GetAllUniverse");
		     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
		     int statusCode = response.getStatusCode();

		     if (statusCode == 200) {
		         test.log(LogStatus.PASS, "GetByIdUniverse request successful"+ statusCode);
		     } else {
		         test.log(LogStatus.FAIL, "Failed to retrieve GetByIdUniverse. Status code: " + statusCode);
		     }

		     report.endTest(test);
		 }

			

		public void updateUniverse() throws IOException {
			test = report.startTest("Neo4J_Universe");

		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(1);
		    String requestBody = row.getCell(3).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		    String tokenEndpoint = "/v1.0/0001/universes/0001";

		    RequestSpecification request = RestAssured.given().body(requestBody);
	        test.log(LogStatus.PASS, "User gives valid body");


		    Response response = request.post(tokenEndpoint);
		    int statusCode = response.getStatusCode();

		    if (statusCode == 200) {
		        test.log(LogStatus.PASS, "UpdateUniverse  successfully"+ statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to UpdateUniverse. Status code: " + statusCode);
		    }

		    report.endTest(test);
		} 

			


//				         Assert.assertEquals(response.statusCode(),201);
//				         test.log(LogStatus.PASS, "Post call Hit Successfully");
//		   
		

		public void deleteUniverse() {
			test = report.startTest("Neo4J_DeleteUniverse");
			 String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		     String tokenEndpoint = "v1.0/0001/universes/0001";
		     test.log(LogStatus.PASS, "DeleteUniverse");
		     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
		     int statusCode = response.getStatusCode();

		     if (statusCode == 200) {
		         test.log(LogStatus.PASS, "DeleteUniverse request successful"+ statusCode);
		     } else {
		         test.log(LogStatus.FAIL, "Failed to retrieve DeleteUniverse. Status code: " + statusCode);
		     }

		     report.endTest(test);
		 }

			
		public void schema() throws IOException {
			 test = report.startTest("Neo4J_schema");

			    FileInputStream fi = new FileInputStream("data/testData.xlsx");
			    XSSFWorkbook wb = new XSSFWorkbook(fi);
			    XSSFSheet ws = wb.getSheet("Body");

			    XSSFRow row = ws.getRow(1);
			    String requestBody = row.getCell(5).getStringCellValue();

			    wb.close();

			    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";

			    String tokenEndpoint = "v1.0/neo4j/node/schema";
		        test.log(LogStatus.PASS, "User hits schema Request");

			    RequestSpecification request = RestAssured.given().body(requestBody);
		        test.log(LogStatus.PASS, "User gives valid body");


			    Response response = request.post(tokenEndpoint);
			    int statusCode = response.getStatusCode();

			    if (statusCode == 201) {
			        test.log(LogStatus.PASS, "schema created successfully"+ statusCode);
			    } else {
			        test.log(LogStatus.FAIL, "Failed to create schema. Status code: " + statusCode);
			    }

			    report.endTest(test);
			}
		
//			 Response response=RestAssured.given().log().all()
//						.baseUri("https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/schema")
//						.contentType(ContentType.JSON)
//						.body(ReqBody.CreateSchemaID())
//						.post()
//						.then().log().all().extract().response();
////				         Assert.assertEquals(response.statusCode(),201);
//				         test.log(LogStatus.PASS, "schema Hit Successfully");
//		}
		public void getBySchemaId() {
			 test = report.startTest("Neo4J_GetBySchemaId");

			Response res=RestAssured.given().log().all()
					.baseUri("https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/schema/018b6fdef5dacc0001Pat")
					.get().then().extract().response();
	         test.log(LogStatus.PASS, "GetBySchemaId");
	         
	         int statusCode = res.getStatusCode();

			    if (statusCode == 200) {
			        test.log(LogStatus.PASS, "User retrieve BySchemaId successfully"+ statusCode);
			    } else {
			        test.log(LogStatus.FAIL, "Failed to GetBySchemaId. Status code: " + statusCode);
			    }

			    report.endTest(test);
			}

		
		public void updateSchemaId() {
			 test = report.startTest("Neo4J_updateSchemaId");

		    Response respon=RestAssured.given().log().all()
					.baseUri("https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/schema")
					 .contentType(ContentType.JSON)
					 .body(ReqBody.UpdateSchemaID())
					 .put()
					 .then().log().all().extract().response();
	         test.log(LogStatus.PASS, "updateSchemaId");
	         int statusCode = respon.getStatusCode();

			    if (statusCode == 200) {
			        test.log(LogStatus.PASS, "updateSchemaId successfully"+ statusCode);
			    } else {
			        test.log(LogStatus.FAIL, "Failed to updateSchemaId. Status code: " + statusCode);
			    }

			    report.endTest(test);
			}

		
		public void getallSchemaNode() {
			test = report.startTest("Neo4J_GetallSchemaNode");
			   String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
			     String tokenEndpoint = "/v1.0/neo4j/node/schema";
			     test.log(LogStatus.PASS, "GetallSchemaNode");

			     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
			     int statusCode = response.getStatusCode();

			     if (statusCode == 200) {
			         test.log(LogStatus.PASS, "GetallSchemaNode request successful"+statusCode);
			     } else {
			         test.log(LogStatus.FAIL, "Failed to retrieve GetallSchemaNode. Status code: " + statusCode);
			     }

			     report.endTest(test);
			 }
			
	/*	public void CreateTenant() throws IOException {
			   test = report.startTest("Neo4J_CreateTenant");

			    FileInputStream fi = new FileInputStream("data/testData.xlsx");
			    XSSFWorkbook wb = new XSSFWorkbook(fi);
			    XSSFSheet ws = wb.getSheet("Body");

			    XSSFRow row = ws.getRow(1);
			    String requestBody = row.getCell(6).getStringCellValue();

			    wb.close();

			    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";

			    String tokenEndpoint = "/v1.0/neo4j/node/tenant";
		        test.log(LogStatus.PASS, "User hits CreateTenant Request");
		        RequestSpecification request = RestAssured.given().body(requestBody);
		        test.log(LogStatus.PASS, "User gives valid body");


			    Response response = request.post(tokenEndpoint);
			    int statusCode = response.getStatusCode();

			    if (statusCode == 201) {
			        test.log(LogStatus.PASS, "CreateTenant successfully"+statusCode);
			    } else {
			        test.log(LogStatus.FAIL, "Failed to CreateTenant. Status code: " + statusCode);
			    }

			    report.endTest(test);
			}*/
	/*	public void createTenant() throws IOException {
		    test = report.startTest("Neo4J_CreateTenant");

		    FileInputStream fi = new FileInputStream("data/testData.xlsx");
		    XSSFWorkbook wb = new XSSFWorkbook(fi);
		    XSSFSheet ws = wb.getSheet("Body");

		    XSSFRow row = ws.getRow(1);
		    String requestBody = row.getCell(6).getStringCellValue();

		    wb.close();

		    RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";

		    String tokenEndpoint = "/v1.0/neo4j/node/tenant";
		    test.log(LogStatus.PASS, "User hits CreateTenant Request");

		    // Print the request body before sending the request
		    System.out.println("Request Body: " + requestBody);

		    RequestSpecification request = RestAssured.given().body(requestBody);

		    // Log the request details
		    test.log(LogStatus.INFO, "Request Details: " + request.log().all());

		    test.log(LogStatus.PASS, "User gives a valid body");

		    Response response = request.post(tokenEndpoint);

		    int statusCode = response.getStatusCode();

		    // Print the response body after receiving the response
		    String responseBody = response.getBody().asString();
		    System.out.println("Response Body: " + responseBody);

		    if (statusCode == 201) {
		        test.log(LogStatus.PASS, "CreateTenant successfully" + statusCode);
		    } else {
		        test.log(LogStatus.FAIL, "Failed to CreateTenant. Status code: " + statusCode);
		    }

		    report.endTest(test);
		}
*/
			
			
	    
		
		public void getAllTenant() {
			   test = report.startTest("Neo4J_getAllTenant");
			   String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
			     String tokenEndpoint = "/v1.0/neo4j/node/tenant";
			     test.log(LogStatus.PASS, "getAllTenant");

			     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
			     int statusCode = response.getStatusCode();

			     if (statusCode == 200) {
			         test.log(LogStatus.PASS, "getAllTenant request successful"+statusCode);
			     } else {
			         test.log(LogStatus.FAIL, "Failed to retrieve getAllTenant. Status code: " + statusCode);
			     }

			     report.endTest(test);
			 }
		
		public void getTenantById() {
			   test = report.startTest("Neo4J_getTenantById");
			   String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
			     String tokenEndpoint = "/v1.0/neo4j/node/tenant/649459e72cc1a20001cbb68c";
			     test.log(LogStatus.PASS, "getTenantById");

			     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
			     int statusCode = response.getStatusCode();

			     if (statusCode == 200) {
			         test.log(LogStatus.PASS, "getTenantById request successful"+statusCode);
			     } else {
			         test.log(LogStatus.FAIL, "Failed to retrieve getTenantById. Status code: " + statusCode);
			     }

			     report.endTest(test);
			 }
			

		 public void deleteTenantById() {
			 test = report.startTest("Neo4J_deleteTenantById");
			   String baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
			     String tokenEndpoint = "/v1.0/neo4j/node/tenant/649459e72cc1a20001cbb68c";
			     test.log(LogStatus.PASS, "deleteTenantById");

			     Response response = RestAssured.given().log().all().baseUri(baseURI).get(tokenEndpoint);
			     int statusCode = response.getStatusCode();

			     if (statusCode == 200) {
			         test.log(LogStatus.PASS, "deleteTenantById request successful"+statusCode);
			     } else {
			         test.log(LogStatus.FAIL, "Failed to retrieve deleteTenantById. Status code: " + statusCode);
			     }

			     report.endTest(test);
			 }
			
}
		
		
	
		 
	
	 

	 










