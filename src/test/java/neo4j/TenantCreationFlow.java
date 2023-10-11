package neo4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TenantCreationFlow extends MyRunner {
	private String tenantId;
	private String userId;
	private String universeId;
	private String scheamId;
	private String aqIds;
	private String groupId;
	private String contextId;
	private String workflowId;
	private String chartId;
	private String baId;
	private String templateId;
	private String engagementId;

	public void createTenant() throws IOException {
		test = report.startTest("Neo4j_Api to Persist a TenantNode in Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service";
		String tokenEndpoint = "v1.0/tenant";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Create Tenant' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(1).getCell(1).getStringCellValue();

			// Convert the JSON string to a JSONObject
			JSONObject bodyJson = new JSONObject(bodyString);

			// Generate a random tenantId using UUID and set it in the JSON object
			String tenantId = UUID.randomUUID().toString();
			bodyJson.put("tenantId", tenantId);

			// Set the request JSON body
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			// test.log(LogStatus.INFO, "Request Body: " + request);

			// Send the request and validate the response
			Response response = request.post(tokenEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
			tenantId = response.jsonPath().getString("tenantId");

			//// Assert.assertEquals(response.statusCode(), 200);

			// Store the extracted tenantId in the instance variable
			this.tenantId = tenantId;

			 int statusCode = response.getStatusCode();
			 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
				
			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Created Tenant Successfully		 Statuscode: " + statusCode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Create Tenant		Statuscode: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Tenant creation response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a tenant Response Time: " + seconds + " seconds</font>");
				writeToExcel(tokenEndpoint, statusCode, seconds);


			}
			report.endTest(test);
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Tenant Creation Failed", e.getMessage());
		}
		
	}

	public void retrieveAlltenants() {
		test = report.startTest("Neo4j_Api to Retrieve All TenantNode From Neo4j DB");
		RequestSpecification request = RestAssured.given();

		String baseURL = "https://ig.aidtaas.com/neo4j-service/v1.0/tenant";
		//test.log(LogStatus.INFO, "Request URL: " + baseURL);
		request.baseUri(baseURL);

		Response response = request.get();
		response.then().log().all();

		//test.log(LogStatus.PASS, "User hits 'Retrieve All TenantNode' request");
		int statuscode = response.getStatusCode();
		 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		String responseBody = response.getBody().asString();
		//test.log(LogStatus.INFO, "Response Body: " + responseBody);
		if (statuscode == 200) {
			test.log(LogStatus.PASS, "Retrieve all TenantNode successfully 				Statuscode:" + statuscode);
		} else {
			test.log(LogStatus.FAIL, "Failed to retrieve all TenantNode 			Statuscode: " + statuscode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieve all tenant's response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve to all tenant's response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void tenantById() {
		test = report.startTest("Neo4j_Api to retrieve a TenantNode from Neo4j DB by TenantId");
		RequestSpecification request = RestAssured.given();

		String baseURL = "https://ig.aidtaas.com/neo4j-service/v1.0/tenant/" + tenantId;

		//test.log(LogStatus.INFO, "Request URL: " + baseURL);
		request.baseUri(baseURL);

		Response response = request.get();
		response.then().log().all();

		//test.log(LogStatus.PASS, "User hits 'Retrieve tenant by Id' request");
		int statuscode = response.getStatusCode();
		 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		String responseBody = response.getBody().asString();
		//test.log(LogStatus.INFO, "Response Body: " + responseBody);
		if (statuscode == 200) {
			test.log(LogStatus.PASS, "Retrieved Tenant by Id Successfully 			Statuscode:" + statuscode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve Tenant by Id 			Statuscode: " + statuscode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieve tenant by id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to retrieve tenant by id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void deleteTenant() throws Throwable {
		test = report.startTest("Neo4j_Delete Tenant");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/";
		String deleteEndpoint = "v1.0/tenant/" + tenantId;
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Delete Tenant' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);

			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Tenant Deletion Test Passed			Statuscode: " + response.statusCode());
			
			int statuscode = response.getStatusCode();
			 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			test.log(LogStatus.INFO, "Response Body: " + responseBody);
			if (statuscode == 200) {
				test.log(LogStatus.PASS, "Deleted Tenant by Id Successfully 			Statuscode:" + statuscode);
			} else {
				test.log(LogStatus.FAIL, "Failed to Delete Tenant by Id 			Statuscode: " + statuscode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Delete tenant by id response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to delete tenant by id response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);

		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Tenant deletion failed", e.getMessage());
		}
	}

	public void createUser() throws IOException {
		test = report.startTest("Neo4j_Create User");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/neo4j/user";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

		//test.log(LogStatus.PASS, "User hits 'Create User' request");

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
			// test.log(LogStatus.INFO, "Request Body: " + request);

			// Send the request and validate the response
			Response response = request.post(tokenEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
		//	test.log(LogStatus.INFO, "Response Body: " + responseBody);
			userId = response.jsonPath().getString("userId");

			Assert.assertEquals(response.statusCode(), 201);

			// Log the test result in the report
			test.log(LogStatus.PASS, "User Created Successfully			 Statuscode: " + response.statusCode());

			// Store the extracted tenantId in the instance variable
			this.userId = userId;

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
			int statuscode = response.getStatusCode();
			 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			 if (statuscode == 200) {
					test.log(LogStatus.PASS, "Create User by Id Successfully 			Statuscode:" + statuscode);
				} else {
					test.log(LogStatus.FAIL, "Failed to Create User by Id 			Statuscode: " + statuscode);
				}
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>User creation response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a user response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
			 
			 
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "User creation failed", e.getMessage());
		}
	}

	public void deleteUser() throws Throwable {
		test = report.startTest("Neo4j_Delete User");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String deleteEndpoint = "/neo4j/user/" + userId;
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Delete User' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);
		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();

			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Deleted User Successfully			 statuscode: " + response.statusCode());
			int statuscode = response.getStatusCode();
			 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
			if (statuscode == 200) {
				test.log(LogStatus.PASS, "Deleted User by Id Successfully 			Statuscode:" + statuscode);
			} else {
				test.log(LogStatus.FAIL, "Failed to Delete User by Id 			Statuscode: " + statuscode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Delete user by id response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to delete user by id response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Failed to Delete the User", e.getMessage());
		}
	}

	public void createUniverse() throws IOException {
		test = report.startTest("Neo4j_Create Universe");

		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/";
		String tokenEndpoint = "v1.0/universes";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Create Universe' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

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
			bodyJson.put("tenantId", tenantId);

			// Generate a random tenantId using UUID and set it in the JSON object
			String universeId = UUID.randomUUID().toString();
			bodyJson.put("universeId", universeId);

			// Set the request JSON body
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			// test.log(LogStatus.INFO, "Request Body: " + request);
			// Send the request and validate the response
			Response response = request.post(tokenEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
			universeId = response.jsonPath().getString("universeId");

			// Store the extracted tenantId in the instance variable
			this.universeId = universeId;
			int statusCode = response.getStatusCode();
			 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Universe Created Succesfully			Statuscode: " + response.getStatusCode());
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Create Universe			Statuscode: " + response.getStatusCode());
			}
			
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Universe creation response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a universe response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Universe Creation Failed", e.getMessage());
		}
	}

	public void universeById() {
		test = report.startTest("Neo4j_Api to Retrieve a UniverseNode from Neo4j DB by UniverseId");
		RequestSpecification request = RestAssured.given();

		String baseURL = "https://ig.aidtaas.com/neo4j-service/v1.0/universes/" + universeId;

		//test.log(LogStatus.INFO, "Request URL: " + baseURL);
		request.baseUri(baseURL);

		Response response = request.get();
		response.then().log().all();

		//test.log(LogStatus.PASS, "User hits 'Retrieved Universe by Id' request");
		int statuscode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		//test.log(LogStatus.INFO, "Response Body: " + responseBody);
		if (statuscode == 200) {
			test.log(LogStatus.PASS, "Retrieved Universe by Id Successfully 			Statuscode:" + statuscode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve Universe by Id				Statuscode: " + statuscode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieve universe by id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to retrieve universe by id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAlluniverses() {
		test = report.startTest("Neo4j_Api to Retrieve All Universes From Neo4j DB");

		// ?requesterId=9ed51e2a-31dd-4e72-bdb9-759615a1a677&requesterType=TENANT\n

		String BaseURL = "https://ig.aidtaas.com/neo4j-service/v1.0/universes?requesterId=9ed51e2a-31dd-4e72-bdb9-759615a1a677&requesterType=TENANT\n";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(BaseURL);
		//test.log(LogStatus.PASS, "User hits 'Retrieve All Universes' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + BaseURL);
		int statuscode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		//test.log(LogStatus.INFO, "Response Body: " + responseBody);
		 long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		if (statuscode == 200) {
			test.log(LogStatus.PASS, "Retrieved all Universes Successfully 			Statuscode:" + statuscode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All Universes 			Statuscode: " + statuscode);
		}
		
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieve all universes response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to retrieve all universes response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateUniverse() throws IOException {
		test = report.startTest("Neo4j_Api updateUniverse");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/universes";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Update Universe' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body");
			// Replace "Sheet1" with the actual sheet name
			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(15).getCell(1).getStringCellValue();
			// Convert the JSON string to a JSONObject
			JSONObject bodyJson = new JSONObject(bodyString);
			// Generate a random tenantId using UUID and set it in the JSON object
			String name = UUID.randomUUID().toString();
			bodyJson.put("name", name);
			bodyJson.put("tenantId", tenantId);
			bodyJson.put("universeId", universeId);

			// Set the request JSON body
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			// test.log(LogStatus.INFO, "Request Body: " + request);
			// Send the request and validate the response
			Response response = request.put(tokenEndpoint);
			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
			universeId = response.jsonPath().getString("universeId");

			// Store the extracted tenantId in the instance variable
			this.universeId = universeId;
			int statusCode = response.getStatusCode();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Universe Updated Successfully			Statuscode: " + statusCode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Update Universe		 Statuscode: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Updated universe response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to update universes response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Universe Updation Failed ", e.getMessage());
		}
	}

	public void deleteUniverse() throws Throwable {
		test = report.startTest("Neo4j_Delete Universe");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
//        String universeId = "v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/universes/+universeId";
		// String requesterType = "TENANT";
		// Update the URL to include the universeId as a path parameter
		String deleteEndpoint = "/universes/" + universeId;

		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		//test.log(LogStatus.PASS, "User hits 'Delete Universe' request");
		try {
			// Add the requesterType as a query parameter
			// request.queryParam( requesterType);

			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);

			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Universe Deleted Successfully 			Statuscode: " + response.statusCode());
			int statuscode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			if (statuscode == 200 || statuscode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Universe Deleted Successfully			Statuscode: " + statuscode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Delete Universe		 Statuscode: " + statuscode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Universe deleted response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to delete universe response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Universe Deletion Failed", e.getMessage());
		}
	}

	public void createSchema() throws IOException {
		test = report.startTest("Neo4j_Create Schema");

		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/schemas";
		RequestSpecification request = RestAssured.given();
//			test.log(LogStatus.INFO, "Request URL:" + "   " +RestAssured.baseURI + tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Create Schema' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

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
			bodyJson.put("tenantId", tenantId);
			// Assuming you have a valid universeId, set it in the JSON object
			JSONArray universeNodeIds = new JSONArray();
			universeNodeIds.put(universeId);  
			bodyJson.put("universeNodeIds", universeNodeIds);
			
			// Set the request JSON body
			request.body(bodyJson.toString());
			System.out.println(bodyJson);
			request.contentType(ContentType.JSON);
			// Send the request and validate the response
			Response response = request.post(tokenEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
			schemaId = response.jsonPath().getString("scheamId");
			// Store the extracted tenantId in the instance variable
			this.scheamId = schemaId;

			int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Schema created Successfully			 Statuscode: " + response.getStatusCode());
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Schema Creation Failed			 Statuscode: " + response.getStatusCode());
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Schema creation response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a schema response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Schema Creation Failed", e.getMessage());
		}
	}

	public void schemaById() {
		test = report.startTest("Neo4j_Api to retrieve a schemaNode from Neo4j DB by schemaId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/schemas/" + scheamId;
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Get by SchemaId' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Retrieved Schema by Id Successfully 			Statuscode: " + statusCode);
		
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve Schema by Id  			Statuscode: " + statusCode);
		
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved schema by id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieved schema by id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllSchemas() {
		test = report.startTest("Neo4j_Api to retrieve all schemas from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/schemas";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve All Schema' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		response.then().log().all();
		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Retrived All Schema Successfully 			Statuscode: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrive All Schema 			Statuscode: " + statusCode);
		}
		
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all schema response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to retrieve all schema response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateSchema() throws IOException {
		test = report.startTest("Neo4j_Api updateSchema");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/schemas";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Update Schema' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body");
			// Replace "Sheet1" with the actual sheet name
			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(16).getCell(1).getStringCellValue();
			// Convert the JSON string to a JSONObject
			JSONObject bodyJson = new JSONObject(bodyString);
			// Generate a random tenantId using UUID and set it in the JSON object
			String name = UUID.randomUUID().toString();
			bodyJson.put("name", name);
			bodyJson.put("schemaId", scheamId);
			bodyJson.put("tenantId", tenantId);
			// Assuming you have a valid universeId, set it in the JSON object
			JSONArray universeNodeIds = new JSONArray();
			universeNodeIds.put(universeId);  
			bodyJson.put("universeNodeIds", universeNodeIds);
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			Response response = request.put(tokenEndpoint);
			response.then().log().all();
			
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);

			int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;

			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Schema Updated Successfully 			Statuscode: " + statusCode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Update Schema 			Statuscode: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Schema updated response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second update schema response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Schema Updation Failed", e.getMessage());
		}
	}

	public void deleteschema() throws Throwable {
		test = report.startTest("Neo4j_Delete Schema");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
//      String universeId = "v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/universes/+universeId";
		String requesterType = "TENANT";

		// Update the URL to include the universeId as a path parameter
		String deleteEndpoint = "/schemas/" + scheamId;

		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Delete Schema' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Add the requesterType as a query parameter
			request.queryParam(requesterType);

			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);

			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Schema Deleted Successfully 			Statuscode: " + response.statusCode());

			int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;

			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Schema Deleted Successfully 			Statuscode: " + statusCode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to delete Schema 			Statuscode: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Schema deleted response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete schema response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Schema Deletion Failed", e.getMessage());
		}
	}

	public void createAQNode() throws IOException {
		test = report.startTest("Neo4j_Create AQ Node");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/";
		String tokenEndpoint = "v1.0/aqs";
		//https://ig.aidtaas.com/neo4j-service/v1.0/aqs

		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Create AQ Node' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

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
			String name = UUID.randomUUID().toString();
			bodyJson.put("name", name);
			JSONArray schemaNodeIds = new JSONArray();
			schemaNodeIds.put(scheamId);  
			bodyJson.put("schemaNodeIds", schemaNodeIds);

			JSONArray universeNodeIds = new JSONArray();
			universeNodeIds.put(universeId);  
			bodyJson.put("universeNodeIds", universeNodeIds);
			bodyJson.put("tenantId", tenantId);

			

			
			// Set the request JSON body
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
//	    		test.log(LogStatus.INFO, "Request Body: " + request);

			// Send the request and validate the response
			Response response = request.post(tokenEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
            // Parse the response JSON to extract the tenantId
			aqId = response.jsonPath().getString("aqId");
			this.aqIds = aqId;


			int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;

			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "AQ Created Successfully 			Statuscode: " + statusCode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "AQ Creation Failed 			Statuscode: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>AQ creation response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a AQ response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
			
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "AQ Creation Failed", e.getMessage());
		}
	}
		

	public void aqById() {
		test = report.startTest("Neo4j_Api to retrieve a AqNode from Neo4j DB by AqId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/aqs/" + aqIds;
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Get by AQ_Id' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		
		String responseBody = response.getBody().asString();
		response.then().log().all();

		if (statusCode == 200 || statusCode == 201) {
			// Log the test result as passed if status code is 201
			test.log(LogStatus.PASS, "Retrived AQ By Id Successfully 			Statuscode: " + statusCode);
		} else {
			// Log the test result as failed if status code is not 201
			test.log(LogStatus.FAIL, "Failed to Retrive AQ By Id 			Statuscode: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrived AQ by id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrive AQ by id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllAq() {
		test = report.startTest("Neo4j_Api to retrieve  all Aq from Neo4j DB by AqId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/aqs/";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve All AQ' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		String responseBody = response.getBody().asString();
		response.then().log().all();
		int statusCode = response.getStatusCode();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		
		if (statusCode == 200 || statusCode == 201) {
			// Log the test result as passed if status code is 201
			test.log(LogStatus.PASS, "Retrived AQ By Id Successfully 			Statuscode: " + statusCode);
		} else {
			// Log the test result as failed if status code is not 201
			test.log(LogStatus.FAIL, "Failed to Retrive AQ By Id 			Statuscode: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrived AQ by id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrive AQ by id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}


	public void updateAq() throws IOException {
	    test = report.startTest("Neo4j_Api updateAq");
	    RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service";
	    String tokenEndpoint = "/v1.0/aqs";
	    RequestSpecification request = RestAssured.given();
	    //test.log(LogStatus.PASS, "User hits 'Update AQ' request");
	    //test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

	    try {
	        FileInputStream file = new FileInputStream("data/testData.xlsx");
	        Workbook workbook = new XSSFWorkbook(file);
	        Sheet sheet = workbook.getSheet("Body");

	        String bodyString = sheet.getRow(18).getCell(1).getStringCellValue();

	        JSONObject bodyJson = new JSONObject(bodyString);
	        
	        // Modify the JSON properties you want to update
	        String name = UUID.randomUUID().toString();
	        bodyJson.put("name", name);
	        JSONArray schemaNodeIds = new JSONArray();
			schemaNodeIds.put(scheamId);  
			bodyJson.put("schemaNodeIds", schemaNodeIds);

			JSONArray universeNodeIds = new JSONArray();
			universeNodeIds.put(universeId);  
			bodyJson.put("universeNodeIds", universeNodeIds);
			bodyJson.put("tenantId", tenantId);
			bodyJson.put("aqId", aqIds);

	        // Set the request JSON body
	        request.body(bodyJson.toString());
	        request.contentType(ContentType.JSON);

	        // Send the request and validate the response
	        Response response = request.put(tokenEndpoint);  // Use PUT for updates

	        response.then().log().all();
	        String responseBody = response.getBody().asString();
	       // test.log(LogStatus.INFO, "Response Body: " + responseBody);

	        int statusCode = response.getStatusCode();

	        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			
			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Updated AQ Successfully 			Statuscode: " + statusCode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Update AQ			Statuscode: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Updated AQ response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second update AQ response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
	    } catch (IOException e) {
	        e.printStackTrace();
	        test.log(LogStatus.FAIL, "AQ Updation Failed", e.getMessage());
	    }
	}

	public void deleteAQ() throws Throwable {
		test = report.startTest("Neo4j_Delete AQ");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String deleteEndpoint = "/aqs/" +aqIds;
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Delete AQ' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			test.log(LogStatus.INFO, "Response Body: " + responseBody);

			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "AQ Deleted Successfully 			Statuscode: " + response.statusCode());
	        int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			
			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Deleted AQ Successfully 			Statuscode: " + statusCode);
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Delete AQ			Statuscode: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Deleted AQ response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete AQ response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Failed to Delete AQ", e.getMessage());
		}
	}

	public void createGroupNode() throws IOException {
	    test = report.startTest("Neo4j_Group Creation With SchemaId");
	    RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/";
	    String tokenEndpoint = "v1.0/groups";
	    RequestSpecification request = RestAssured.given();
	    //test.log(LogStatus.PASS, "User hits 'Create Group Node' request");
	   // test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

	    try {
	        FileInputStream file = new FileInputStream("data/testData.xlsx");
	        Workbook workbook = new XSSFWorkbook(file);
	        Sheet sheet = workbook.getSheet("Body");
	        

	        String bodyString = sheet.getRow(7).getCell(1).getStringCellValue();

	        JSONObject bodyJson = new JSONObject(bodyString);
	        JSONArray aqId = new JSONArray();
	        aqId.put(aqIds);  
	        bodyJson.put("aqId", aqId);
	        String groupId = UUID.randomUUID().toString();
	        bodyJson.put("groupId", groupId);

	    

	        JSONArray schemaId = new JSONArray();
	        schemaId.put(scheamId);  
	        bodyJson.put("schemaId", schemaId);

	        JSONArray universeNodeIds = new JSONArray();
	        universeNodeIds.put(universeId);  
	        bodyJson.put("universeNodeIds", universeNodeIds);

	        bodyJson.put("tenantId",tenantId );

	        // Set the request JSON body
	        request.body(bodyJson.toString());
	        request.contentType(ContentType.JSON);
	       // test.log(LogStatus.INFO, "Request Body: " + request);

	        // Send the request and validate the response
	        Response response = request.post(tokenEndpoint);

	        response.then().log().all();
	        String responseBody = response.getBody().asString();
	       // test.log(LogStatus.INFO, "Response Body: " + responseBody);
	        this.groupId = groupId;

	        int statusCode = response.getStatusCode();
	        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
			
	        if (statusCode == 200 || statusCode == 201) {
	            test.log(LogStatus.PASS, "Group Created Successfully 			Statuscode: " + statusCode);
	        } else {
	            test.log(LogStatus.FAIL, "Failed to Create Group			Statuscode: " + statusCode);
	        }
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Group creation response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a group response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
	    } catch (IOException e) {
	        e.printStackTrace();
	        test.log(LogStatus.FAIL, "Group Creation Failed", e.getMessage());
	    }
	}


	public void GroupById() {
		test = report.startTest("Neo4j_Api to Retrieve a GroupNode from Neo4j DB by groupId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/groups/" + groupId;
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve Group by Id' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		 if (statusCode == 200 || statusCode == 201){
			test.log(LogStatus.PASS, "Retrieved Group by Id Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve Group by Id 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved group by id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to retrieve group by id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllGroup() {
		test = report.startTest("Neo4j_Api to Retrieve All Group from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0/";
		String tokenEndpoint = "groups/";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrive All Groups' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All Groups Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrive All Groups  			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all groups response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to Retrieve all groups response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateGroup() throws IOException {
		test = report.startTest("Neo4j_Api Update Group");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/groups";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Update Group' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body");
			// Replace "Sheet1" with the actual sheet name
			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(19).getCell(1).getStringCellValue();

	        JSONObject bodyJson = new JSONObject(bodyString);
	        
	        // Modify the JSON properties you want to update
	        String name = UUID.randomUUID().toString();
	        bodyJson.put("name", name);
	        JSONArray aqId = new JSONArray();
	        aqId.put(aqIds);  
	        bodyJson.put("aqId", aqId);

	    

	        JSONArray schemaId = new JSONArray();
	        schemaId.put(scheamId);  
	        bodyJson.put("schemaId", schemaId);

	        JSONArray universeNodeIds = new JSONArray();
	        universeNodeIds.put(universeId);  
	        bodyJson.put("universeNodeIds", universeNodeIds);

	        bodyJson.put("tenantId",tenantId );

			bodyJson.put("groupId", groupId);

			
	        // Set the request JSON body
	        request.body(bodyJson.toString());
	        request.contentType(ContentType.JSON);

	        // Send the request and validate the response
	        Response response = request.put(tokenEndpoint);  // Use PUT for updates

	        response.then().log().all();
	        String responseBody = response.getBody().asString();
	       // test.log(LogStatus.INFO, "Response Body: " + responseBody);

	        int statusCode = response.getStatusCode();
	        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;
	        if (statusCode == 200 || statusCode == 201) {
	            test.log(LogStatus.PASS, "Group Updated Successfully 			Statuscode: " + statusCode);
	        } else {
	            test.log(LogStatus.FAIL, "Failed to Update Group 			Statuscode: " + statusCode);
	        }
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Group updated response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to update Group response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
	    } catch (IOException e) {
	        e.printStackTrace();
	        test.log(LogStatus.FAIL, "Group Update Failed", e.getMessage());
	    }
	}

	public void deleteGroup() throws Throwable {
		test = report.startTest("Neo4j_Delete Group");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String deleteEndpoint = "/groups/" + groupId;
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Delete GroupId' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);

			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Group Deleted Successfully 			Statuscode: " + response.statusCode());

			 int statusCode = response.getStatusCode();
		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				 double seconds = responseTimeInSeconds1 / 1000.0;
		        if (statusCode == 200 || statusCode == 201) {
		            test.log(LogStatus.PASS, "Group Deleted Successfully 			Statuscode: " + statusCode);
		        } else {
		            test.log(LogStatus.FAIL, "Failed to Delete Group 			Statuscode: " + statusCode);
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Group deleted response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second to delete Group response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Failed to Delete Group", e.getMessage());
		}
	}

	public void schemaGroup() throws IOException {
		test = report.startTest("Neo4j_Group Creation Without Schema");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/groups";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

		//test.log(LogStatus.PASS, "User hits 'Group Creation Without Schema' request");

		// ...
		// ...

		try {
	        FileInputStream file = new FileInputStream("data/testData.xlsx");
	        Workbook workbook = new XSSFWorkbook(file);
	        Sheet sheet = workbook.getSheet("Body");
	        

	        String bodyString = sheet.getRow(7).getCell(1).getStringCellValue();

	        JSONObject bodyJson = new JSONObject(bodyString);
	        JSONArray aqId = new JSONArray();
	        aqId.put(aqIds);  
	        bodyJson.put("aqId", aqId);
	        String groupId = UUID.randomUUID().toString();
	        bodyJson.put("groupId", groupId);

	        JSONArray universeNodeIds = new JSONArray();
	        universeNodeIds.put(universeId);  
	        bodyJson.put("universeNodeIds", universeNodeIds);

	        bodyJson.put("tenantId",tenantId );

	        // Set the request JSON body
	        request.body(bodyJson.toString());
	        request.contentType(ContentType.JSON);
	       // test.log(LogStatus.INFO, "Request Body: " + request);

	        // Send the request and validate the response
	        Response response = request.post(tokenEndpoint);

	        response.then().log().all();
	        String responseBody = response.getBody().asString();
	        //test.log(LogStatus.INFO, "Response Body: " + responseBody);
	        this.groupId = groupId;

	        int statusCode = response.getStatusCode();
	        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			 double seconds = responseTimeInSeconds1 / 1000.0;

	        if (statusCode == 200 || statusCode == 201) {
	            test.log(LogStatus.PASS, "Group Created without Schema Successfully 			Statuscode: " + response.getStatusCode());
	        } else {
	            test.log(LogStatus.FAIL, "Group without Schema Creation Failed 			Statuscode: " + response.getStatusCode());
	        }
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Group creation without schema response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a group without schema response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
	    } catch (IOException e) {
	        e.printStackTrace();
	        test.log(LogStatus.FAIL, "Group Creation without Schema Failed", e.getMessage());
	    }
	}

	public void groupBySchemaId() {
		test = report.startTest("Neo4j_Api to Retrieve a GroupNode from Neo4j DB by SchemaId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/groups/" + groupId;
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve Group by Id' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		 double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved Group by Schema Id Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve Group by Schema Id  			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved group by schema id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve group by  schema id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void RetrieveAllGroupBySchemaId() {
		test = report.startTest("Neo4j_Api to Retrieve All Group by SchemaId from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0/";
		String tokenEndpoint = "groups/";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve All Group by SchemaId' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All Group by SchemaId Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All Group by SchemaId  			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved group by schema id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve group by  schema id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void schemaContext() throws IOException {
		test = report.startTest("Neo4j_Context Creation With Schema");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/contexts";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Create Context with Schema' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

		// ...
		// ...

		try {
	        FileInputStream file = new FileInputStream("data/testData.xlsx");
	        Workbook workbook = new XSSFWorkbook(file);
	        Sheet sheet = workbook.getSheet("Body");
	        

	        String bodyString = sheet.getRow(9).getCell(1).getStringCellValue();

	        JSONObject bodyJson = new JSONObject(bodyString);
	        JSONArray aqId = new JSONArray();
	        aqId.put(aqIds);  
	        bodyJson.put("aqId", aqId);
	        String contextId = UUID.randomUUID().toString();
	        bodyJson.put("contextId", contextId);

	        JSONArray universeNodeIds = new JSONArray();
	        universeNodeIds.put(universeId);  
	        bodyJson.put("universeNodeIds", universeNodeIds);
	        bodyJson.put("schemaId", scheamId);
	        bodyJson.put("tenantId",tenantId );

	        // Set the request JSON body
	        request.body(bodyJson.toString());
	        request.contentType(ContentType.JSON);
	       // test.log(LogStatus.INFO, "Request Body: " + request);

	        // Send the request and validate the response
	        Response response = request.post(tokenEndpoint);
	        String responseBody = response.getBody().asString();
	        //test.log(LogStatus.INFO, "Response Body: " + responseBody);
	        this.contextId = contextId;

	        int statusCode = response.getStatusCode();
	        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;

	        if (statusCode == 200 || statusCode == 201) {
	            test.log(LogStatus.PASS, "Created Context with Schema Successfully 			Statuscode: " + response.getStatusCode());
	        } else {
	            test.log(LogStatus.FAIL, "Failed to Create Context with Schema 			Statuscode: " + response.getStatusCode());
	        }
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Context creation with schema response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a context with schema response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
	    } catch (IOException e) {
	        e.printStackTrace();
	        test.log(LogStatus.FAIL, "Context Creation with Schema Failed", e.getMessage());
	    }
	}
	public void contextBySchemaId() {
		test = report.startTest("Neo4j_Api to retrieve a ContextNode from Neo4j DB by SchemaId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/contexts/"+contextId;
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve a Context' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201){
			test.log(LogStatus.PASS, "Retrieved a Context by Schema Id Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve a Context by Schema Id 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved a context by schema id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve a context by schema id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void RetrieveAllContexts() {
		test = report.startTest("Neo4j_Api to retrieve Retrieve All Contexts from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/contexts/";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits get by contextId api by tenant");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All Contexts Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All Contexts 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all context's response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve all context's response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void AqContext() throws IOException {
		test = report.startTest("Neo4j_Context Creation With AQ");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/contexts";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Context Creation With AQ' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

		// ...
		// ...

		try {
	        FileInputStream file = new FileInputStream("data/testData.xlsx");
	        Workbook workbook = new XSSFWorkbook(file);
	        Sheet sheet = workbook.getSheet("Body");
	        

	        String bodyString = sheet.getRow(9).getCell(1).getStringCellValue();

	        JSONObject bodyJson = new JSONObject(bodyString);
	        JSONArray aqId = new JSONArray();
	        aqId.put(aqIds);  
	        bodyJson.put("aqId", aqId);
	        String contextId = UUID.randomUUID().toString();
	        bodyJson.put("contextId", contextId);

	        JSONArray universeNodeIds = new JSONArray();
	        universeNodeIds.put(universeId);  
	        bodyJson.put("universeNodeIds", universeNodeIds);
	        bodyJson.put("tenantId",tenantId );

	        // Set the request JSON body
	        request.body(bodyJson.toString());
	        request.contentType(ContentType.JSON);
	       // test.log(LogStatus.INFO, "Request Body: " + request);

	        // Send the request and validate the response
	        Response response = request.post(tokenEndpoint);
	        String responseBody = response.getBody().asString();
	        this.contextId = contextId;

	        int statusCode = response.getStatusCode();
	        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
	        if (statusCode == 200 || statusCode == 201) {
	            test.log(LogStatus.PASS, "Context Creation With AQ is Successful 			Statuscode: " + response.getStatusCode());
	        } else {
	            test.log(LogStatus.FAIL, "Context Creation with AQ Failed 			Statuscode: " + response.getStatusCode());
	        }
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Context creation with AQ response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a context with AQ response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);

	    } catch (IOException e) {
	        e.printStackTrace();
	        test.log(LogStatus.FAIL, "Context Creation with AQ Failed", e.getMessage());
	    }
	}
	public void contextByAqId() {
		test = report.startTest("Neo4j_Api to Retrieve a ContextNode from Neo4j DB by AqId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/contexts/" + contextId;
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve a Context by AQ Id' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved a Context by AQ Id Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve a Context by AQ Id 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved a context by AQ id response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve a context by AQ id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllContexts() {
		test = report.startTest("Neo4j_Api to Retrieve All Contexts from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/contexts/";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		//test.log(LogStatus.PASS, "User hits 'Retrieve All Contexts' request");
		//test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All Contexts Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All Contexts 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all context's response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve all context's id response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateContextAq() throws IOException {
		test = report.startTest("Neo4j_Api Update Context With AQ");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/contexts";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Update Context With AQ' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body");
			// Replace "Sheet1" with the actual sheet name
			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(23).getCell(1).getStringCellValue();
			// Convert the JSON string to a JSONObject
			JSONObject bodyJson = new JSONObject(bodyString);
			// Generate a random tenantId using UUID and set it in the JSON object
			String name = UUID.randomUUID().toString();
			bodyJson.put("name", name);
			// Set the request JSON body
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			// test.log(LogStatus.INFO, "Request Body: " + request);
			//test.log(LogStatus.PASS, "User Valid Body");
			// Send the request and validate the response
			Response response = request.put(tokenEndpoint);
			response.then().log().all();
			int statusCode = response.getStatusCode();

//			String responseBody = response.getBody().asString();
//			test.log(LogStatus.INFO, "Response Body: " + responseBody);
//			//// Assert.assertEquals(response.statusCode(), 200);
			// Log the test result in the report
			String responseBody = response.getBody().asString();
			response.then().log().all();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
				test.log(LogStatus.PASS, "Updated Context With AQ 			Status code: " + statusCode);
			} else {
				test.log(LogStatus.FAIL, "Failed to Update Context With AQ 			Status code: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Updated context with AQ response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second update context with AQ response time: " + seconds + " seconds</font>");

			}
		
		
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Context Updation Failed", e.getMessage());
		}
	}

	public void deleteContext() throws Throwable {
		test = report.startTest("Neo4j_Delete Context");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String deleteEndpoint = "/contexts/" +contextId;
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Delete Context' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
	        int statusCode = response.getStatusCode();
	        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
		            // Log the test result in the report for success
		            test.log(LogStatus.PASS, "Deleted Context Successfully  			Statuscode: " + response.getStatusCode());
		        } else {
		            // Log the test result in the report for failure
		            test.log(LogStatus.FAIL, "Failed to Delete Context 			Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Deleted context response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete context response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "ContextId Deletion Failed", e.getMessage());
		}
	}

	public void createWorkflow() throws IOException {
		test = report.startTest("Neo4j_Api create Workflow");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/";
		String tokenEndpoint = "v1.0/workflows";
		RequestSpecification request = RestAssured.given();
		//test.log(LogStatus.PASS, "User hits 'Create Workflow' request");
		//test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();

			 JSONObject bodyJson = new JSONObject(bodyString);
		     
		    	JSONArray schemaNodeIds = new JSONArray();
				schemaNodeIds.put(scheamId);  
				bodyJson.put("schemaIds", schemaNodeIds);
		        bodyJson.put("tenantId",tenantId );
                JSONArray universeNodeIds = new JSONArray();
			    universeNodeIds.put(universeId);  
			    bodyJson.put("universeNodeIds", universeNodeIds);
		        String workflowId = UUID.randomUUID().toString();
		        bodyJson.put("workflowId", workflowId);
		        // Set the request JSON body
		        request.body(bodyJson.toString());
		        request.contentType(ContentType.JSON);
		        // Send the request and validate the response
		        Response response = request.post(tokenEndpoint);
		        String responseBody = response.getBody().asString();
		        //test.log(LogStatus.INFO, "Response Body: " + responseBody);
		        this.workflowId = workflowId;

		        int statusCode = response.getStatusCode();
		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;

		        if (statusCode == 200 || statusCode == 201) {
		            test.log(LogStatus.PASS, "Created Workflow Successfully 			Statuscode: " + response.getStatusCode());
		        } else {
		            test.log(LogStatus.FAIL, "Failed to Create Workflow			 Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Workflow creation response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a workflow response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		    } catch (IOException e) {
		        e.printStackTrace();
		        test.log(LogStatus.FAIL, "Work creation failed", e.getMessage());
		    }
		}
	public void workflowByAqId() {
		test = report.startTest("Neo4j_Api to Retrieve a WorkflowNode from Neo4j DB by AqId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/workflows/" + workflowId;
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve a WorkflowNode' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved a WorkflowNode Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve a WorkflowNode  			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved a workflowNode response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve a workflowNode response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllWorkflows() {
		test = report.startTest("Neo4j_Api to Retrieve All Workflows from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/workflows";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve All Workflows' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All Workflows Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All Workflows 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all workflow's response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve all workflow's response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateWorkflow() throws IOException {
		test = report.startTest("Neo4j_Api updateWorkflow");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/workflows/" + workflowId;
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Update Workflow' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body");
			// Replace "Sheet1" with the actual sheet name
			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(16).getCell(1).getStringCellValue();
			// Convert the JSON string to a JSONObject
			JSONObject bodyJson = new JSONObject(bodyString);
			// Generate a random tenantId using UUID and set it in the JSON object
			String name = UUID.randomUUID().toString();
			bodyJson.put("name", name);
			bodyJson.put("schemaId", scheamId);
			bodyJson.put("tenantId", tenantId);
	        bodyJson.put("workflowId", workflowId);

			// Assuming you have a valid universeId, set it in the JSON object
			JSONArray universeNodeIds = new JSONArray();
			universeNodeIds.put(universeId);  
			bodyJson.put("universeNodeIds", universeNodeIds);
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			Response response = request.put(tokenEndpoint);
			response.then().log().all();
			
			String responseBody = response.getBody().asString();

			int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
				// Log the test result as passed if status code is 201
				test.log(LogStatus.PASS, "Updated Workflow Successfully 			Statuscode: " + response.getStatusCode());
			} else {
				// Log the test result as failed if status code is not 201
				test.log(LogStatus.FAIL, "Failed to Update Workflow 			Statuscode: " + response.getStatusCode());
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Updated workflow response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second update workflow response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Workflow Updation Failed", e.getMessage());
		}
	}
	public void createChartGroup() throws IOException {
		test = report.startTest("Neo4j_Chart Creation With Group");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/charts";
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
//
//		test.log(LogStatus.PASS, "User hits 'Chart Creation With Group' request");

		// ...
		// ...

		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();

			 JSONObject bodyJson = new JSONObject(bodyString);
			    JSONArray aqId = new JSONArray();
		        aqId.put(aqIds);  
		        bodyJson.put("aqIds", aqId);
		        bodyJson.put("tenantId",tenantId );
		        JSONArray universeNodeIds = new JSONArray();
			    universeNodeIds.put(universeId);  
			    bodyJson.put("universeNodeIds", universeNodeIds);
                JSONArray groupIds = new JSONArray();
                groupIds.put(groupId);  
			    bodyJson.put("groupIds", groupIds);
		        String chartId = UUID.randomUUID().toString();
		        bodyJson.put("chartId", chartId);
		        // Set the request JSON body
		        request.body(bodyJson.toString());
		        request.contentType(ContentType.JSON);
		        // Send the request and validate the response
		        Response response = request.post(tokenEndpoint);
		        String responseBody = response.getBody().asString();
		       // test.log(LogStatus.INFO, "Response Body: " + responseBody);
		        this.chartId = chartId;

		        int statusCode = response.getStatusCode();
		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
		        if (statusCode == 200 || statusCode == 201) {
		            test.log(LogStatus.PASS, "Chart With Group Created Successfully 			Statuscode: " + response.getStatusCode());
		        } else {
		            test.log(LogStatus.FAIL, "Failed to Create Chart With Group 			Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Chart with group creation response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a chart With group response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		    } catch (IOException e) {
		        e.printStackTrace();
		        test.log(LogStatus.FAIL, "Chart Creation with Group Failed", e.getMessage());
		    }
		}

	public void chartByGroupId() {
		test = report.startTest("Neo4j_Api to Retrieve a ChartNode from Neo4j DB by GroupId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/charts/" + chartId;
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve a ChartNode' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved a ChartNode Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieved a ChartNode  			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved a chartNode response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve a chartNode response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllCharts() {
		test = report.startTest("Neo4j_Api to Retrieve All Charts from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/charts";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve All Charts' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All Charts Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All Charts  			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all chart's response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve all chart's response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateChartGroup() throws IOException {
		test = report.startTest("Neo4j_Api Update Chart with Group");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/charts"; 
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Update Chart with Group' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();

			 JSONObject bodyJson = new JSONObject(bodyString);
			    JSONArray aqId = new JSONArray();
		        aqId.put(aqIds);  
		        bodyJson.put("aqIds", aqId);
		        bodyJson.put("tenantId",tenantId );
		        JSONArray universeNodeIds = new JSONArray();
			    universeNodeIds.put(universeId);  
			    bodyJson.put("universeNodeIds", universeNodeIds);
                JSONArray groupIds = new JSONArray();
                groupIds.put(groupId);  
			    bodyJson.put("groupIds", groupIds);
		        bodyJson.put("chartId", chartId);
		        // Set the request JSON body
		        request.body(bodyJson.toString());
		        request.contentType(ContentType.JSON);
		        // Send the request and validate the response
		        Response response = request.post(tokenEndpoint);
		        String responseBody = response.getBody().asString();
		       // test.log(LogStatus.INFO, "Response Body: " + responseBody);
		        this.chartId = chartId;

		        int statusCode = response.getStatusCode();

		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
		        if (statusCode == 200 || statusCode == 201) {
		            test.log(LogStatus.PASS, "Updated Chart with Group Successfully 			Statuscode: " + response.getStatusCode());
		        } else {
		            test.log(LogStatus.FAIL, "Failed to Update Chart with Group			 Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Updated chart with group response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second update chart with group response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		    } catch (IOException e) {
		        e.printStackTrace();
		        test.log(LogStatus.FAIL, "Chart Updation Group Failed", e.getMessage());
		    }
		}

	public void createBaGroup() throws IOException {
		test = report.startTest("Neo4j_BA with Group");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/bas";
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
//
//		test.log(LogStatus.PASS, "User hits 'BA with Group' request");

		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();

			 JSONObject bodyJson = new JSONObject(bodyString);
			    JSONArray aqId = new JSONArray();
		        aqId.put(aqIds);  
		        bodyJson.put("aqIds", aqId);
		        bodyJson.put("tenantId",tenantId );
		        JSONArray universeNodeIds = new JSONArray();
			    universeNodeIds.put(universeId);  
			    bodyJson.put("universeNodeIds", universeNodeIds);
                JSONArray groupIds = new JSONArray();
                groupIds.put(groupId);  
			    bodyJson.put("groupIds", groupIds);
		        String baId = UUID.randomUUID().toString();
		        bodyJson.put("baId", baId);
		        // Set the request JSON body
		        request.body(bodyJson.toString());
		        request.contentType(ContentType.JSON);
		        // Send the request and validate the response
		        Response response = request.post(tokenEndpoint);
		        String responseBody = response.getBody().asString();
		        //test.log(LogStatus.INFO, "Response Body: " + responseBody);
		        this.baId = baId;

		        int statusCode = response.getStatusCode();

		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
		        if (statusCode == 200 || statusCode == 201) {
		            test.log(LogStatus.PASS, "Created BA with Group Successfully 			Statuscode: " + response.getStatusCode());
		        } else {
		            test.log(LogStatus.FAIL, "Failed to Create BA with Group 			Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>BA with Group creation response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a BA with group response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		    } catch (IOException e) {
		        e.printStackTrace();
		        test.log(LogStatus.FAIL, "BA Creation with Group Failed", e.getMessage());
		    }
		}
	public void BaByGroupId() {
		test = report.startTest("Neo4j_Api to retrieve a ChartNode from Neo4j DB by GroupId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/bas/" + baId;
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'BA by Group Id' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		String responseBody = response.getBody().asString();
		response.then().log().all();
		int statusCode = response.getStatusCode();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved BA by Group Id 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve BA by Group Id 			Status code: " + statusCode);
		}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Retrieved BA by group id response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve BA by group id response time: " + seconds + " seconds</font>");

			}
		report.endTest(test);
	}

	public void retrieveAllBas() {
		test = report.startTest("Neo4j_Api to Retrieve All BA's from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/bas";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve All BAs' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All BA's Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All BA's  			Status code: " + statusCode);
		}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Retrieved all BA's response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve all BA's response time: " + seconds + " seconds</font>");

			}
		report.endTest(test);
	}

	public void updateBaGroup() {
	    test = report.startTest("Neo4j_Api Update Ba with Group");
	    RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
	    String tokenEndpoint = "/bas";
	    RequestSpecification request = RestAssured.given();

//	    test.log(LogStatus.PASS, "User hits 'Update Ba with Group' request");
//	    test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);

	    try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();

			 JSONObject bodyJson = new JSONObject(bodyString);
			 String name = UUID.randomUUID().toString();
		        bodyJson.put("name", name);
			    JSONArray aqId = new JSONArray();
		        aqId.put(aqIds);  
		        bodyJson.put("aqIds", aqId);
		        bodyJson.put("tenantId",tenantId );
		        JSONArray universeNodeIds = new JSONArray();
			    universeNodeIds.put(universeId);  
			    bodyJson.put("universeNodeIds", universeNodeIds);
                JSONArray groupIds = new JSONArray();
                groupIds.put(groupId);  
			    bodyJson.put("groupIds", groupIds);
		        bodyJson.put("baId", baId);
		        // Set the request JSON body
		        request.body(bodyJson.toString());
		        request.contentType(ContentType.JSON);
		        // Send the request and validate the response
		        Response response = request.post(tokenEndpoint);
		        String responseBody = response.getBody().asString();
		       // test.log(LogStatus.INFO, "Response Body: " + responseBody);

		        int statusCode = response.getStatusCode();

		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
	        if (statusCode == 200 || statusCode == 201) {
	            test.log(LogStatus.PASS, "Updated BA with Group Successfully 			Statuscode: " + response.getStatusCode());
	        } else {
	            test.log(LogStatus.FAIL, "Failed to Update BA with Group 			Statuscode: " + response.getStatusCode());
	        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Updated BA with group response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second updated BA with group response time: " + seconds + " seconds</font>");

				}
			report.endTest(test);
	    } catch (IOException e) {
	        test.log(LogStatus.FAIL, "BA Updation Group Failed", e.getMessage());
	    }
	}


	public void createTemplateGroup() throws IOException {
		test = report.startTest("Neo4j_Template Creation with AQ");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/templates";
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
//
//		test.log(LogStatus.PASS, "User hits 'Template Creation with AQ' request");

		try {
			// Load the Excel file
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

			// Read the input JSON body from Excel
			String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();

			 JSONObject bodyJson = new JSONObject(bodyString);
			 JSONArray aqId = new JSONArray();
		        aqId.put(aqIds);  
		        bodyJson.put("aqIds", aqId);
		        bodyJson.put("tenantId",tenantId );
		        JSONArray universeNodeIds = new JSONArray();
			    universeNodeIds.put(universeId);  
			    bodyJson.put("universeNodeIds", universeNodeIds);
                JSONArray groupIds = new JSONArray();
                groupIds.put(groupId);  
			    bodyJson.put("groupIds", groupIds);
		        String templateId = UUID.randomUUID().toString();
		        bodyJson.put("templateId", templateId);
		        // Set the request JSON body
		        request.body(bodyJson.toString());
		        request.contentType(ContentType.JSON);
		        // Send the request and validate the response
		        Response response = request.post(tokenEndpoint);
		        String responseBody = response.getBody().asString();
		       // test.log(LogStatus.INFO, "Response Body: " + responseBody);
		        this.templateId = templateId;

		        int statusCode = response.getStatusCode();

		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
		        if (statusCode == 200 || statusCode == 201) {
		            test.log(LogStatus.PASS, "Template Creation with AQ is Successful 			Statuscode: " + response.getStatusCode());
		        } else {
		            test.log(LogStatus.FAIL, "Template Creation with AQ is Failed 			Statuscode: " + response.getStatusCode());
		        }	    
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Template creation with AQ response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second creating a template with AQ response time: " + seconds + " seconds</font>");

				}
			report.endTest(test);
		    } catch (IOException e) {
		        e.printStackTrace();
		        test.log(LogStatus.FAIL, "Template Creation with AQ failed", e.getMessage());
		    }
		}

	public void TemplateByGroupId() {
		test = report.startTest("Neo4j_Api to Retrieve a TemplateNode from Neo4j DB by GroupId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/templates/" + templateId;
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve a TemplateNode' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved a TemplateNode Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve a TemplateNode  			Status code: " + statusCode);
		}    
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved a templateNode response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve a templateNode response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllTemplates() {
		test = report.startTest("Neo4j_Api to Retrieve All Templates from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/templates";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits ' Retrieve All Templates' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, " Retrieved All Templates Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to  Retrieve All Templates 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all template's response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve all template's response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateTemplateGroup() throws IOException {
		test = report.startTest("Neo4j_Api Update Template Group");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/templates";
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Update Template Group' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
// Load the Excel file        
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body");       
			String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();
			JSONObject bodyJson = new JSONObject(bodyString);
			String name = UUID.randomUUID().toString();
			bodyJson.put("name", name);
			// Set the request JSON body
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			 JSONArray aqId = new JSONArray();
		        aqId.put(aqIds);  
		        bodyJson.put("aqIds", aqId);
		        bodyJson.put("tenantId",tenantId );
		        JSONArray universeNodeIds = new JSONArray();
			    universeNodeIds.put(universeId);  
			    bodyJson.put("universeNodeIds", universeNodeIds);
               JSONArray groupIds = new JSONArray();
               groupIds.put(groupId);  
			    bodyJson.put("groupIds", groupIds);
		        String templateId = UUID.randomUUID().toString();
		        bodyJson.put("templateId", templateId);
		        // Set the request JSON body
		        request.body(bodyJson.toString());
		        request.contentType(ContentType.JSON);
		        // Send the request and validate the response
		        Response response = request.post(tokenEndpoint);
		        String responseBody = response.getBody().asString();
		       // test.log(LogStatus.INFO, "Response Body: " + responseBody);
		        this.templateId = templateId;

		        int statusCode = response.getStatusCode();

		        long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;

		        if (statusCode == 200 || statusCode == 201) {
		            test.log(LogStatus.PASS, "Updated Template Group Successfully 			Statuscode: " + response.getStatusCode());
		        } else {
		            test.log(LogStatus.FAIL, "Failed to Update Template Group 			Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Updated template group response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second update template group response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		    } catch (IOException e) {
		        e.printStackTrace();
		        test.log(LogStatus.FAIL, "Update Template Group Failed", e.getMessage());
		    }
		}

	public void createEngagementTemplate() throws IOException {
	    test = report.startTest("Neo4j_Engagement with Template");
	    RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service";
	    String tokenEndpoint = "v1.0/engagements";
	    RequestSpecification request = RestAssured.given();
//	    test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
//
//	    test.log(LogStatus.PASS, "User hits 'Engagement with Template' request");

	 // Generate a random templateId using UUID and set it in the JSON body
	 			String templateId = UUID.randomUUID().toString();
	    // Your request body as a JSON string
		String requestBody = "{\n"
				+ "    \"aqIds\": [\n"
				+ "        \"QA_qa\"\n"
				+ "    ],\n"
				+ "    \"groupIds\": [\n"
				+ "        \"QA_groupId\"\n"
				+ "    ],\n"
				+ "    \"name\": \"qatemplateId\",\n"
				+ "    \"templateId\": \""+templateId+"\",\n"
				+ "    \"tenantId\": \"QA_tenantId\",\n"
				+ "    \"universeNodeIds\": [\n"
				+ "        \"QA_universeId\"\n"
				+ "    ]\n"
				+ "}";

		// Convert the JSON string to a JSONObject
		JSONObject bodyJson = new JSONObject(requestBody);

		// Set the request JSON body
		request.body(bodyJson.toString());
		request.contentType(ContentType.JSON);

		// Send the request and validate the response
		Response response = request.post(tokenEndpoint);

		response.then().log().all();
		String responseBody = response.getBody().asString();
		//test.log(LogStatus.INFO, "Response Body: " + responseBody);
		engagementId = response.jsonPath().getString("engagementId");
        this.engagementId = engagementId;
		int statusCode = response.getStatusCode();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;

		if (statusCode == 200 || statusCode == 201) {
		    test.log(LogStatus.PASS, "Created Engagement with Template 			Statuscode: " + statusCode);
		} else {
		    test.log(LogStatus.FAIL, "Failed to Create Engagement with Template 			Statuscode: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Engagement with template Creation response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second Creating a engagement with template response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void engagementByTemplateId() {
		test = report.startTest("Neo4j_Api to Retrieve an EngagementNode from Neo4j DB by TemplateId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/engagements/" + engagementId;
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve an EngagementNode' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved an EngagementNode Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve an EngagementNode  			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved an engagementNode response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve an engagementNode response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void retrieveAllEngagements() {
		test = report.startTest("Neo4j_Api to retrieve All Engagements from Neo4j DB");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/engagements";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
//		test.log(LogStatus.PASS, "User hits 'Retrieve All Engagements' request");
//		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		response.then().log().all();
		long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
		double seconds = responseTimeInSeconds1 / 1000.0;
		if (statusCode == 200 || statusCode == 201) {
			test.log(LogStatus.PASS, "Retrieved All Engagements Successfully 			Status code: " + statusCode);
		} else {
			test.log(LogStatus.FAIL, "Failed to Retrieve All Engagements 			Status code: " + statusCode);
		}
		if (seconds <= 1.0) {

			test.log(LogStatus.PASS, "<font color='Green'>Retrieved all engagement's response time: " + seconds + " seconds</font>");
		} else {
			test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second retrieve all engagement's response time: " + seconds + " seconds</font>");

		}
		report.endTest(test);
	}

	public void updateEngagementTemplate() throws IOException {
		test = report.startTest("Neo4j_Api Update Engagement Template");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String tokenEndpoint = "/v1.0/engagements";
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Update Engagement Template' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + tokenEndpoint);
		try {
// Load the Excel file        
			FileInputStream file = new FileInputStream("data/testData.xlsx");
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheet("Body");
// Replace "Sheet1" with the actual sheet name        
// Read the input JSON body from Excel        
			String bodyString = sheet.getRow(22).getCell(1).getStringCellValue();
// Convert the JSON string to a JSONObject        
			JSONObject bodyJson = new JSONObject(bodyString);
// Generate a random tenantId using UUID and set it in the JSON object       
			String name = UUID.randomUUID().toString();
			bodyJson.put("name", name);
			// Set the request JSON body
			request.body(bodyJson.toString());
			request.contentType(ContentType.JSON);
			// test.log(LogStatus.INFO, "Request Body: " + request);
			test.log(LogStatus.PASS, "User valid Body");
			// Send the request and validate the response
			Response response = request.put(tokenEndpoint);
			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
////Assert.assertEquals(response.statusCode(), 200);        
// Log the test result in the report        
			test.log(LogStatus.PASS, "Updated Engagement Template Successfully 			Statuscode: " + response.statusCode());
			int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
				test.log(LogStatus.PASS, "Updated Engagement Template Successfully 			Status code: " + statusCode);
			} else {
				test.log(LogStatus.FAIL, "Failed to Update Engagement Template 			Status code: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Updated engagement template response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second update engagement template response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
		} catch (IOException e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Engagement Template Updation Failed", e.getMessage());
		}
	}

	public void deleteworkflowId() throws Throwable {
		test = report.startTest("Neo4j_Delete WorkflowId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0/";
		String deleteEndpoint = "workflows/" + workflowId;
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits delete workflowId request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);

			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Deleted WorkflowId Successfully 			Statuscode: " + response.statusCode());
			int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
			if (statusCode == 200 || statusCode == 201) {
				test.log(LogStatus.PASS, "Deleted Workflow Id Successfully 			Status code: " + statusCode);
			} else {
				test.log(LogStatus.FAIL, "Failed to Delete Workflow Id			Status code: " + statusCode);
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Deleted workflow id response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete workflow id response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);

		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Failed to Delete WorkflowId", e.getMessage());
		}
	}

	public void deletechartId() throws Throwable {
	    test = report.startTest("Neo4j_Delete ChartId");
	    RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
	    String deleteEndpoint = "/charts/" + chartId;
	    RequestSpecification request = RestAssured.given();
//	    test.log(LogStatus.PASS, "User hits 'Delete ChartId' request");
//	    test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

	    try {
	        // Send the request and validate the response
	        Response response = request.delete(deleteEndpoint);

	        response.then().log().all();
	        String responseBody = response.getBody().asString();
	       // test.log(LogStatus.INFO, "Response Body: " + responseBody);
	        int statusCode = response.getStatusCode();
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
			double seconds = responseTimeInSeconds1 / 1000.0;
	        // Check the response status code and determine success or failure
			if (statusCode == 200 || statusCode == 201) {
	            // Log the test result in the report for success
	            test.log(LogStatus.PASS, "Deleted ChartId Successfully 			Statuscode: " + response.getStatusCode());
	        } else {
	            // Log the test result in the report for failure
	            test.log(LogStatus.FAIL, "ChartId deletion Failed 			Statuscode: " + response.getStatusCode());
			}
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Deleted chart id response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete chart id response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Log the test failure in the report
	        test.log(LogStatus.FAIL, "chartId deletion failed", e.getMessage());
	    }
	}

	public void engagementId() throws Throwable {
		test = report.startTest("Neo4j_Delete EngagementId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0";
		String deleteEndpoint = "/engagements/" + engagementId;
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Delete EngagementId' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
	        int statusCode = response.getStatusCode(); 
			long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
				double seconds = responseTimeInSeconds1 / 1000.0;
			  // Check the response status code and determine success or failure
	        if (statusCode == 200 || statusCode == 201) {
	            // Log the test result in the report for success
	            test.log(LogStatus.PASS, "Deleted EngagementId Successfully 			Statuscode: " + response.getStatusCode());
	        } else {
	            // Log the test result in the report for failure
	            test.log(LogStatus.FAIL, "Failed to Delete EngagementId 			Statuscode: " + response.getStatusCode());
	        }
			if (seconds <= 1.0) {

				test.log(LogStatus.PASS, "<font color='Green'>Deleted engagement id response time: " + seconds + " seconds</font>");
			} else {
				test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete engagement id response time: " + seconds + " seconds</font>");

			}
			report.endTest(test);
	        


		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "EngagementId Deletion Failed", e.getMessage());
		}
	}

	public void deleteBa() throws Throwable {
		test = report.startTest("Neo4j_DeleteBA");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0/";
		String deleteEndpoint = "/bas/" + baId;
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Delete BA' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);
			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Ba Id deletion test passed", "Response statuscode: " + response.statusCode());
			 int statusCode = response.getStatusCode(); 
				long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
					double seconds = responseTimeInSeconds1 / 1000.0;
				  // Check the response status code and determine success or failure
		        if (statusCode == 200 || statusCode == 201) {
		            // Log the test result in the report for success
		            test.log(LogStatus.PASS, "Deleted BA Id Successfully 			Statuscode: " + response.getStatusCode());
		        } else {
		            // Log the test result in the report for failure
		            test.log(LogStatus.FAIL, "Failed to Delete BA Id 			Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Deleted BA id response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete BA id response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "BaId deletion failed", e.getMessage());
		}
	}

	public void deletetemplateId() throws Throwable {
		test = report.startTest("Neo4j_Delete TemplateId");
		RestAssured.baseURI = "https://ig.aidtaas.com/neo4j-service/v1.0/";
		String deleteEndpoint = "templates/" + templateId;
		RequestSpecification request = RestAssured.given();
//		test.log(LogStatus.PASS, "User hits 'Delete TemplateId' request");
//		test.log(LogStatus.INFO, "Request URL:" + "   " + RestAssured.baseURI + deleteEndpoint);

		try {
			// Send the request and validate the response
			Response response = request.delete(deleteEndpoint);

			response.then().log().all();
			String responseBody = response.getBody().asString();
			//test.log(LogStatus.INFO, "Response Body: " + responseBody);

			int statuscode = response.getStatusCode();
			// Assert.assertEquals(response.statusCode(), 200);

			// Log the test result in the report
			test.log(LogStatus.PASS, "Delete templateId test passed", "Response statuscode: " + response.statusCode());
			 int statusCode = response.getStatusCode(); 
				long responseTimeInSeconds1 = response.timeIn(TimeUnit.MILLISECONDS);
					double seconds = responseTimeInSeconds1 / 1000.0;
				  // Check the response status code and determine success or failure
		        if (statusCode == 200 || statusCode == 201) {
		            // Log the test result in the report for success
		            test.log(LogStatus.PASS, "Deleted templateId Successfully 			Statuscode: " + response.getStatusCode());
		        } else {
		            // Log the test result in the report for failure
		            test.log(LogStatus.FAIL, "Failed to Delete templateId 			Statuscode: " + response.getStatusCode());
		        }
				if (seconds <= 1.0) {

					test.log(LogStatus.PASS, "<font color='Green'>Deleted template id response time: " + seconds + " seconds</font>");
				} else {
					test.log(LogStatus.FAIL, "<font color='Maroon'>Taking more than one second delete template id response time: " + seconds + " seconds</font>");

				}
				report.endTest(test);
		} catch (Exception e) {
			e.printStackTrace();
			// Log the test failure in the report
			test.log(LogStatus.FAIL, "Templates Deletion Failed", e.getMessage());
		}
	}
	private void writeToExcel(String baseURI, int statusCode, double seconds) {
	    try {
	        String filePath = "data/testData.xlsx";
	        FileInputStream file = new FileInputStream(filePath);
	        Workbook workbook = new XSSFWorkbook(file);
	        Sheet sheet = workbook.getSheet("ResponseData");

	        // Assuming you have headers in the first row
	        Row headerRow = sheet.getRow(0);

	        // Assuming column indices for BASEURI, StatusCode, ResponseTime, and Status
	        int baseURIColumnIndex = 0;
	        int statusCodeColumnIndex = 1;
	        int responseTimeColumnIndex = 2;
	        int statusColumnIndex = 3;

	        // Create a new row for the data
	        int rowNum = sheet.getLastRowNum() + 1;
	        Row dataRow = sheet.createRow(rowNum);

	        // Write data to respective columns
	        dataRow.createCell(baseURIColumnIndex).setCellValue(baseURI);
	        dataRow.createCell(statusCodeColumnIndex).setCellValue(statusCode);
	        dataRow.createCell(responseTimeColumnIndex).setCellValue(seconds);

	        // Assume some logic to determine the status based on the statusCode
	        String status = (statusCode == 200 || statusCode == 201) ? "Pass" : "Fail";
	        dataRow.createCell(statusColumnIndex).setCellValue(status);

	        // Write the updated workbook back to the file
	        FileOutputStream outputStream = new FileOutputStream(filePath);
	        workbook.write(outputStream);
	        outputStream.close();

	        // Close the file input stream
	        file.close();

	    } catch (IOException e) {
	        // Handle the exception
	    }
	
}

}








