package neo4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class ContextController extends MyRunner{
	
	public void addContextNodeByTenant() throws IOException {
		test = report.startTest("Neo4J_Add Context Node");


	      RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "/v1.0/Tenant_Lakshmi/contexts?requesterType=TENANT";
	        RequestSpecification request = RestAssured.given();
	        test.log(LogStatus.PASS, "User Hits create Context request");

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
	            String contextId = UUID.randomUUID().toString();
	            bodyJson.put("contextId", contextId);

	            // Set the request JSON body
	            request.body(bodyJson.toString());
	            request.contentType(ContentType.JSON);

	            test.log(LogStatus.PASS, "User valid Body");
	            // Send the request and validate the response
	            Response response = request.post(tokenEndpoint);

	            response.then().log().all();
	            Assert.assertEquals(response.statusCode(), 201);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "context creation Test Passed", "Response statuscode: " + response.statusCode());

	            // Parse the response JSON to extract the tenantId
	            contextId = response.jsonPath().getString("contextId");

	            // Store the extracted tenantId in the Excel sheet
	            Cell contextIdCell = sheet.getRow(6).createCell(2);
	            contextIdCell.setCellValue(contextId);

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
	            test.log(LogStatus.FAIL, "context CREATION FAILED", e.getMessage());
	        }
}
	

	// Update Context Node by Tenant in RequesterType

	public void updateContextNodeByTenant() throws IOException {
		test = report.startTest("NEO4J_Update Context Node");
		FileInputStream fi = new FileInputStream("data/testData.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet ws = wb.getSheet("Body");

		XSSFRow row = ws.getRow(26);
		String requestBody = row.getCell(1).getStringCellValue();

		wb.close();

		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/649459e72cc1a20001cbb68c/contexts/context6981b350001";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT").body(requestBody);
		Response response = request.put(tokenEndpoint);
		test.log(LogStatus.PASS, "User hits update Context API by Tenant");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);

		test.log(LogStatus.INFO, "User gives valid Request Body" + " " + requestBody);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 201) {
			test.log(LogStatus.PASS, "Update context node successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody" + responseBody);

		} else {
			test.log(LogStatus.FAIL, "Failed to Update context node. Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody" + responseBody);

		}

		report.endTest(test);

	}

	// Retrieve All contexts
	public void retrieveAllContexts() throws IOException {
		test = report.startTest("NEO4J_Get All ContextNode");

		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/649459e72cc1a20001cbb68c/contexts";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "User hits get all context api by Tenant");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);

		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();

		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Get all contexts successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody" + responseBody);

		} else {
			test.log(LogStatus.FAIL, "Failed to get all contexts . Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody" + responseBody);
		}

		report.endTest(test);

	}

	// retrieve by context Id
	public void retrieveContextId() throws IOException {
		test = report.startTest("NEO4J_Retrieve By ContextId");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/649459e72cc1a20001cbb68c/contexts/context6981b350001";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "User hits get by contextId api by Tenant");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS, "Get by contextId successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL, "Failed to get by contextId . Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);
	}

	// search controller
	// getAllUniversesEitherCreatedbyTenantorpublicUniversesbasedonownedOnly
	public static void getAllUniversesEitherCreatedbyTenantorpublicUniversesbasedonownedOnly() {
		test = report.startTest("Neo4j GetAllUniverses by Tenant or Public");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/tenants/618b6fdef5dacc0001a6b1b0/universes";
		RequestSpecification request = RestAssured.given().params("ownedOnly", "true").param("page", "1")
				.param("size", "0").contentType("application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS,
				"User hits get all universes either created by Tenant or public Universe based on owned Only api");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Get All Universes either created by tenant or public universe based on owned only successfully Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to get all universes either created by tenant or public universe based on owned only . Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);

	}

	// aPI_to_get_all_constructs_attached_to_the_given_universeId
	public static void getAllConstructsAttachedToTheGivenUniverseId() {

		test = report.startTest("Neo4j GetAllConstructs Attached to UniverseId");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/tenants/618b6fdef5dacc0001a6b1b0/universes/542156854";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "User hits get all constructs attached to the given univeseId");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Get all constructs attached to the given universeId successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to get all constructs attached to the given universeId . Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);
	}

	// Apitogetalltenantsattachedtoanalliancedconstruct
	public void getalltenantsattachedtoanalliancedconstruct() {
		test = report.startTest("API to Get all Tenants Attached to an Allianced Construct");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v1.0/neo4j/node/allianced/tenants";
		RequestSpecification request = RestAssured.given().param("appId", "5421").param("construct", "GROUP")
				.param("nodeId", "98654").param("tenantId", "9854").contentType("application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "User hits get all tenants attached to an allianced construct");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Get all tenants attached to an allianced construct successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to get all tenants attached to an allianced construct . Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);

	}

	// ApitogetallconstructsattachedtothegivenconstructId
	public void getallconstructsattachedtothegivenconstructId() {
		test = report.startTest("API to Get all Constructs Attached to the Given Construct Id");

		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v1.0/neo4j/node/tenants/65214565";
		RequestSpecification request = RestAssured.given().param("construct", "GROUP").param("nodeId", "5421")
				.contentType("application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "User hits get all Constructs Attached to the Given Construct Id");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Get all constructs attached to the given construct Id successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to get all constructs attached to the given construct Id . Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);

	}

	// APItoretrievealltheconstructsattachedtothetenantId
	public void retrievealltheconstructsattachedtothetenantId() {

		test = report.startTest("Retrieve All the Constructs Attached to the TenantId");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v1.0/neo4j/node";
		RequestSpecification request = RestAssured.given().param("tenantId", "618b6fdef5dacc0001a6b1b0")
				.contentType("application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "Retrieve all the Constructs attached to the tenantId hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Retrieve all the Constructs attached to the tenantId successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to retrieve all the Constructs attached to the tenantId. Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);
	}

	// APItoChecktheAccessibiliTyoftheTenanttoaParticularConstruct
	public void checktheAccessibiliTyoftheTenanttoaParticularConstruct() {

		test = report.startTest("API to Check the Accessibility of the Tenant to a ParticularConstruct");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "neo4j/node/accessiblity";
		RequestSpecification request = RestAssured.given().param("appId", "652").param("construct", "GROUP")
				.param("nodeId", "854").param("tenantId", "618b6fdef5dacc0001a6b1b0").contentType("application/json")
				.contentType("application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "Retrieve all the Constructs attached to the tenantId hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Check the accessibility of the tenant to a particular construct successfully Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to check the accessibility of the tenant to a particular construct,  Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);

	}

	// Api to check the accessibility of the tenant/user to a particular construct
	// for particular accessType
	public void checkAccessiblityOfTheTenantorusertoaParticularConstructForPaticularAccessType() {
		test = report.startTest(
				"check the accessibility of the tenant/user to a particular construct for particular accessType");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v1.0/neo4j/node/tf-web/accessiblity/accesstype";
		RequestSpecification request = RestAssured.given().param("accessType", "READ").param("construct", "GROUP")
				.param("nodeId", "854").param("tenantId", "618b6fdef5dacc0001a6b1b0")
				.header("Accept", "application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS,
				"Check the accessibility of the tenant/user to a particular construct for particular accessType hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"check the accessibility of the tenant/user to a particular construct for particular accessType Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to check the accessibility of the tenant/user to a particular construct for particular accessType,  Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);

	}

	// Api to check the accessibility of the tenant_User to a particular construct
	// for particular accessType
	public void checktheaccissiblityofthetenant_UsertoaparticularconstructforpaticularaccessType() {
		test = report.startTest(
				"Api to check the accessibility of the tenant/user to a particular construct for particular accessType");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v1.0/neo4j/node/tf-web/accessiblity/tenant-user/accesstype";
		RequestSpecification request = RestAssured.given().param("construct", "GROUP").param("nodeId", "854")
				.param("userAccessType", "READ").param("userId", "618b6fdef5dacc0001a6b1b0")
				.header("Accept", "application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS,
				"Check the accessibility of the tenant/user to a particular construct for particular accessType hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"check the accessibility of the tenant/user to a particular construct for particular accessType successfully Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to check the accessibility of the tenant/user to a particular construct for particular accessType,  Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);
	}

	// Api to retrieve all the constructs attached to the tenantId or UserId
	public void retrieveAllTheConstructsAttachedToTheTenantIdOrUserId() {
		test = report.startTest("Api to retrieve all the constructs attached to the tenantId or UserId");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "/v2.0/requesters";
		RequestSpecification request = RestAssured.given().param("construct", "GROUP")
				.param("requesterId", "8754142578").param("requesterType", "TENANT")
				.header("Accept", "application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS,
				"Check the accessibility of the tenant/user to a particular construct for particular accessType hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Retrieve all the constructs attached to the tenantId or UserId successfully Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to retrieve all the constructs attached to the tenantId or UserId,  Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}

		report.endTest(test);

	}

//	Api to get all universes either created by Tenant/User or public universes based on ownedOnly

	public void getAllUniversesEitherCreatedByTenantorUserorPublicUniversesBasedonOwnedOnly() {
		test = report.startTest(
				"Api to get all universes either created by Tenant/User or public universes based on ownedOnly");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v2.0/requesters/845125687541/universes";
		RequestSpecification request = RestAssured.given().param("ownedOnly", "true").header("Accept",
				"application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS,
				"Get all universes either created by Tenant/User or public universes based on ownedOnly hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Get all universes either created by Tenant/User or public universes based on ownedOnly successfully Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to get all universes either created by Tenant/User or public universes based on ownedOnly,  Status code: "
							+ statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}
		report.endTest(test);
	}

	// Api to get all constructs attached to the given universeId
	public void getAllConstructsAttachedToGivenUniverseId() {
		test = report.startTest("Api to get all constructs attached to the given universeId");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v2.0/requesters/6854213685421/universes/1247865320145";
		RequestSpecification request = RestAssured.given().param("requesterType", "TENANT").header("Accept",
				"application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "Get all constructs attached to the given universeId hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"Get all constructs attached to the given universeId successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to get all constructs attached to the given universeId,  Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}
		report.endTest(test);

	}
	
	//Api to check the accessibility of the tenant/user to a particular construct for particular accessType	
	public void checkTheAccessiblityofTheTenantorusertoParticularConstructforParticularAccessType() {
		test = report.startTest("Api to check the accessiblity of the tenant/user to a particular construct for particular accessType");
		RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
		String tokenEndpoint = "v2.0/tf-web/accessiblity/accesstype";
		RequestSpecification request = RestAssured.given().param("accessType", "READ").param("construct", "GROUP").param("nodeId", "987541").param("requesterId", "8995421").param("requesterType", "TENANT").header("Accept","application/json");
		Response response = request.get(tokenEndpoint);
		test.log(LogStatus.PASS, "check the accessiblity of the tenant/user to a particular construct for particular accessType hit successfully");
		test.log(LogStatus.INFO, "Request URL :  " + "  " + RestAssured.baseURI + tokenEndpoint);
		int statusCode = response.getStatusCode();
		String responseBody = response.getBody().asString();
		if (statusCode == 200) {
			test.log(LogStatus.PASS,
					"check the accessiblity of the tenant/user to a particular construct for particular accessType successfully Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		} else {
			test.log(LogStatus.FAIL,
					"Failed to check the accessiblity of the tenant/user to a particular construct for particular accessType,  Status code: " + statusCode);
			test.log(LogStatus.INFO, "ResponseBody  " + responseBody);
		}
		report.endTest(test);
		
		
	}
}


