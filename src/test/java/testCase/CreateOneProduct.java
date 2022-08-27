package testCase;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CreateOneProduct {

	String baseUri = "https://techfios.com/api-prod/api/product";
	SoftAssert softAssert = new SoftAssert();
	String createProductRequestHeader = "application/json; charset=UTF-8";
	String createProductResponseHeader;
	String createProductActualMessage;
	String createProductExpectedMessage = "Product was created.";

	String firstProductId;
	HashMap<String, String> createPayloadMap;

	public Map<String, String> getCreatePayloadMap() {
		

		createPayloadMap = new HashMap<String, String>();

		createPayloadMap.put("name", "Aasifs Automated EyeWear");
		createPayloadMap.put("description",
				"Please dont delete this As it is for my project The best eyeglass only for Aasif.");
		createPayloadMap.put("price", "199");
		createPayloadMap.put("category_id", "1");
		createPayloadMap.put("category_name", "Fashion");

		return createPayloadMap;

	}

	@Test(priority = 1)
	public void createOneProduct() {

		/*
		 * given: all input
		 * details(baseURI,Headers,Authorization,Payload/Body,QueryParameters) when:
		 * submit api requests(Http method,Endpoint/Resource) then: validate response
		 * (status code, Headers, responseTime, Payload/Body)
		 * https://techfios.com/api-prod/api/product/read_one.php
		 */
		Response response =

				given().baseUri(baseUri).header("Content-Type", createProductRequestHeader).auth().preemptive()
						.basic("demo@techfios.com", "abc123")
						// .body(new File("src\\main\\java\\data\\CreatePayload.json")).
						.body(getCreatePayloadMap()).

						when().post("create.php").then().extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code" + statusCode);
		softAssert.assertEquals(statusCode, 201, "Status codes are not matching");

		createProductResponseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header ContentType:" + createProductResponseHeader);
		softAssert.assertEquals(createProductResponseHeader, createProductRequestHeader,
				"Response Headers are not matching!");

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		createProductActualMessage = jsonPath.getString("message");
		System.out.println("Product Message:" + createProductActualMessage);
		softAssert.assertEquals(createProductActualMessage, createProductExpectedMessage,
				"Product messages are not matching");

		softAssert.assertAll();
		
	}

	public class ReadAllProducts {

		@Test(priority = 2)
		public void readAllProducts() {

			Response response =

					given().baseUri(baseUri).header("Content-Type", "application/json; charset=UTF-8").auth()
							.preemptive().basic("demo@techfios.com", "abc123").when().get("read.php").then().extract()
							.response();

			String responseBody = response.getBody().asString();

			JsonPath jsonPath = new JsonPath(responseBody);

			firstProductId = jsonPath.getString("records[0].id");
			System.out.println("First Product Id:" + firstProductId);

		}

	}

	@Test(priority = 3)
	public void readOneProduct() {

		Response response =

				given().baseUri(baseUri).header("Content-Type", "application/json").auth().preemptive()
						.basic("demo@techfios.com", "abc123").queryParam("id", firstProductId).

						when().get("read_one.php").then().extract().response();

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		String actualProductNameFromResponse = jsonPath.getString("name");
		System.out.println("Actual Product Name:" + actualProductNameFromResponse);
		softAssert.assertEquals(actualProductNameFromResponse, getCreatePayloadMap().get("name"),
				"Product names are not matching!");

		String productDescription = jsonPath.getString("description");
		System.out.println("Product Description:" + productDescription);
		softAssert.assertEquals(productDescription, getCreatePayloadMap().get("description"),
				"Product descriptions are not matching!");

		String productPrice = jsonPath.getString("price");
		System.out.println("Product Price:" + productPrice);
		softAssert.assertEquals(productPrice, getCreatePayloadMap().get("price"), "Product prices are not matching!");

		softAssert.assertAll();
	}

}
