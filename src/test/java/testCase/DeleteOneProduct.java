package testCase;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteOneProduct {

	String baseUri = "https://techfios.com/api-prod/api/product";
	SoftAssert softAssert = new SoftAssert();
	String deleteProductRequestHeader = "application/json; charset=UTF-8";
	String deleteProductResponseHeader;
	String deleteProductExpectedMessage = "Product was deleted.";

	HashMap<String, String> deletePayloadMap;

	public Map<String, String> getDeletePayloadMap() {

		deletePayloadMap = new HashMap<String, String>();
		deletePayloadMap.put("id", "5299");

		return deletePayloadMap;

	}

	public void deleteOneProduct() {

		Response response =

				given().baseUri(baseUri).header("Content-Type", deleteProductRequestHeader).auth().preemptive()
						.basic("demo@techfios.com", "abc123")
						// .body(new File("src\\main\\java\\data\\CreatePayload.json")).
						.body(getDeletePayloadMap()).

						when().delete("/delete.php").then().extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code" + statusCode);
		softAssert.assertEquals(statusCode, 200, "Status codes are not matching");

		deleteProductResponseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header ContentType:" + deleteProductResponseHeader);
		softAssert.assertEquals(deleteProductResponseHeader, deleteProductRequestHeader,
				"Response Headers are not matching!");

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		String deleteProductActualMessage = jsonPath.getString("message");
		System.out.println("Product Message:" + deleteProductActualMessage);
		softAssert.assertEquals(deleteProductActualMessage, deleteProductExpectedMessage,
				"Product messages are not matching");

		softAssert.assertAll();

	}

	@Test(priority = 2)
	public void readOneProduct() {

		Response response =

				given().baseUri(baseUri).header("Content-Type", "application/json").auth().preemptive()
						.basic("demo@techfios.com", "abc123").queryParam("id", getDeletePayloadMap().get("id")).

						when().get("read_one.php").then().extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code" + statusCode);
		softAssert.assertEquals(statusCode, 404, "Status codes are not matching");

		String responseBody = response.getBody().asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		String actualProductMessage = jsonPath.getString("message");
		System.out.println("Product Message:" + actualProductMessage);
		softAssert.assertEquals(actualProductMessage, "Product does not exist.", "Product messages are not matching");
		softAssert.assertAll();
	}

}
