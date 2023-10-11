package neo4j;

public class ReqBody {
	public static String Universe() {
		return "{\n"
				+ "    \"description\": \"string\",\n"
				+ "    \"icon\": \"string\",\n"
				+ "    \"name\": \"string\",\n"
				+ "    \"universeId\": \"Universeb6fdef5dacc000Patel11\"\n"
				+ "}";
	}
	
	public static String UpdateUniverse() {
		return "{\n"
				+ "    \"description\": \"1212 Universe  Avinash Marketplace Identity Access Management Service.\"\n"
				+ "}";
		
	}
	public static String CreateSchemaID() {
		return"{\n"
				+ "    \"name\": \"Gaian User\",\n"
				+ "    \"tenantNodeId\": \"0001\",\n"
				+ "    \"userId\": \"0001\",\n"
				+ "    \"constructs\": {}\n"
				+ "}";
	}
	public static String UpdateSchemaID() {
		return"{\n"
				+ "    \"universeNodeIds\": [\n"
				+ "        \"Universeb6fdef5dacc0001aGuru\"\n"
				+ "    ],\n"
				+ "    \"schemaId\": \"018b6fdef5dacc0001Patel\",\n"
				+ "    \"description\": \"Patel Schema Avinash Tenant description\",\n"
				+ "    \"name\": \"1415 RPtelavi Schema Tenant\",\n"
				+ "    \"schemaOwnerId\": \"1415018b6fdef5dacc0001a6b1b9\"\n"
				+ "}";
	}
	
}


