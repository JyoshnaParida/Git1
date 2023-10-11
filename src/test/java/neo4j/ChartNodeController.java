package neo4j;
import static io.restassured.RestAssured.given;

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
import com.sun.mail.imap.protocol.BODY;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class ChartNodeController extends MyRunner{
	private String chartId;

	
	public void createChartGroup() throws IOException {
		 test = report.startTest("Neo4j_Chart With Group");
		 RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
	        String tokenEndpoint = "/v1.0/b4218e25-b2b0-4789-93f6-382b02516dbc/charts?requesterType=TENANT";
	        RequestSpecification request = RestAssured.given();
		test.log(LogStatus.INFO, "Request URL:" + "   " +RestAssured.baseURI + tokenEndpoint);

	        test.log(LogStatus.PASS, "User hits create Chart Group request");

	     // ...
	     // ...

	        try {
	            // Load the Excel file
	            FileInputStream file = new FileInputStream("data/testData.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name

	            // Read the input JSON body from Excel
	            String bodyString = sheet.getRow(11).getCell(1).getStringCellValue();

	            // Convert the JSON string to a JSONObject
	            JSONObject bodyJson = new JSONObject(bodyString);

	            // Generate a random tenantId using UUID and set it in the JSON object
	            String chartId = UUID.randomUUID().toString();
	            bodyJson.put("chartId", chartId);

	            // Set the request JSON body
	            request.body(bodyJson.toString());
	            request.contentType(ContentType.JSON);
  		//test.log(LogStatus.INFO, "Request Body: " + request);

	            test.log(LogStatus.PASS, "User gives valid Body");
	            // Send the request and validate the response
	            Response response = request.post(tokenEndpoint);

	            response.then().log().all();
	         String responseBody = response.getBody().asString();
		    test.log(LogStatus.INFO, "Response Body: " + responseBody);

	            Assert.assertEquals(response.statusCode(), 201);

	            // Log the test result in the report
	            test.log(LogStatus.PASS, "Chart creation test passed", "Response statuscode: " + response.statusCode());
	         // Store the extracted tenantId in the instance variable
          this.chartId = chartId;
	            // Parse the response JSON to extract the tenantId
          chartId = response.jsonPath().getString("chartId");

	            // Store the extracted tenantId in the Excel sheet
	            Cell chartIdCell = sheet.getRow(1).createCell(2);
	            chartIdCell.setCellValue(chartId);

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
	            test.log(LogStatus.FAIL, "Chart creation failed", e.getMessage());
	        }
} 
}
