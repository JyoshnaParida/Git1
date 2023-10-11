package neo4j;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ReteriveData extends MyRunner{
	
	public void tenantById() { 
		test = report.startTest("Neo4j_tenant By Id");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/tenant/606ed8616bf9930001f98fd4";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "Retrieved tenantById");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "Retrieved tenant By Id successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Retrieved tenant By Id statuscode: " +statuscode);
	    }
	}
	
	public void  retrieveaUserNodefromNeo4jDBbyid() { 
		test = report.startTest("Neo4j_retrieve a UserNode fromNeo4j DB by id");
	    RequestSpecification request = RestAssured.given();
	    
	    String baseURL = "https://ingress-gateway.gaiansolutions.com/neo4j-service/v1.0/neo4j/node/user/0001";
	    test.log(LogStatus.INFO, "Request URL: " + baseURL);
	    request.baseUri(baseURL);

	    Response response = request.get();
	    response.then().log().all();
	    
	    test.log(LogStatus.PASS, "retrieve a UserNode fromNeo4j DB by id");
	    int statuscode = response.getStatusCode();
	    String responseBody = response.getBody().asString();
	    test.log(LogStatus.INFO, "Response Body: " + responseBody);
	    if (statuscode == 200) {
	        test.log(LogStatus.PASS, "retrieve a UserNode fromNeo4j DB by id successfully statuscode:" +statuscode);
	    } else {
	        test.log(LogStatus.FAIL, "Failed to Retrieved retrieve a UserNode fromNeo4j DB by id statuscode: " +statuscode);
	    }
	}
}


