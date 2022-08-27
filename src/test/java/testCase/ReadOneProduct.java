package testCase;


	
	

	import org.testng.Assert;
	import org.testng.annotations.Test;

	import io.restassured.path.json.JsonPath;
	import io.restassured.response.Response;

	import static io.restassured.RestAssured.*;

	import java.util.concurrent.TimeUnit;

	public class ReadOneProduct {
		
		String baseUri = "https://techfios.com/api-prod/api/product";

		@Test
		public void readOneProduct() {
			
			/*
			 given: all input details(baseURI,Headers,Authorization,Payload/Body,QueryParameters)
			 when: submit api requests(Http method,Endpoint/Resource)
			 then: validate response (status code, Headers, responseTime, Payload/Body)
			 https://techfios.com/api-prod/api/product/read_one.php
			  
			  
			  
			
			 */
			
			
			

		Response response =

		given()
			.baseUri(baseUri)
			.header("Content-Type", "application/json")
			.auth().preemptive().basic("demo@techfios.com", "abc123")
			.queryParam("id", "5211").
			
		when()
			.get("read_one.php").
		then()
			.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code" + statusCode);
		Assert.assertEquals(statusCode, 200);

			long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
			System.out.println("Response Time" + responseTime);

			if (responseTime <= 2000) {
				System.out.println("Response time is within the range");

			} else {
				System.out.println("Response time is out of range");
			}

			String responseHeader = response.getHeader("Content-Type");
			System.out.println("Response Header ContentType:" + responseHeader);
			Assert.assertEquals(responseHeader, "application/json");

			String responseBody = response.getBody().asString();
			// System.out.println("Response Body" + responseBody);
			
			/*
	{
    "id": "5211",
    "name": "Amazing Automation Pillow 2.0 By",
    "description": "Please dont delete this As it is for my project The best pillow only for Aasif.",
    "price": "999",
    "category_id": "2",
    "category_name": "Electronics"
}
}
			 */
			
			

			JsonPath jsonPath = new JsonPath(responseBody);

			String productName = jsonPath.getString("name");
			System.out.println("Product Name:" + productName);
			Assert.assertEquals(productName, "Amazing Automation Pillow 2.0 By");
			
			
			String productDescription = jsonPath.getString("description");
			System.out.println("Product Description:" + productDescription);
			Assert.assertEquals(productDescription, "Please dont delete this As it is for my project The best pillow only for Aasif.");
			
			String productPrice = jsonPath.getString("price");
			System.out.println("Product Price:" + productPrice);
			Assert.assertEquals(productPrice, "999");
			
			
		}

	}

			//You can use soft assert as well instead of hard assert. 
			//it will still execute without failing even if it doesnt match
			//with hard assertion it will not pass execution or move more forward if it doesnt match
			//in soft assertion you need to end it with assert All in order to execute and pass each and every line 
			//Hard assertion- if it fails will not execute rest of the code
			//Soft assertion - if it fails will execute rest of the code
			//during soft assertion you will have to AssertAll at the end by creating a soft assert class and by creating an object
			// for eg SoftAssert softassert = new SoftAssert then you can use softassert.assertequals class
			//you can use soft assert if you want to fail but still want all the lines to execute


