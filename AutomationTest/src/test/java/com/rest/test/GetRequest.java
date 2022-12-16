package com.rest.test;

import org.testng.annotations.Test;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.json.simple.JSONObject;
import org.jtwig.JtwigModel;

import io.restassured.RestAssured;

public class GetRequest {
	@Test
	public void getmehtod() {

		/*
		 * RestAssured.baseURI = "http://103.130.88.60:8986/";
		 * given().when().get("api/v1/get/subcard");
		 */

		String searchQueryApi = "http://103.130.88.60:8986/api/v1/subcard";

		JsonNode body;
		try {
			body = Unirest.get(searchQueryApi).asJson().getBody();
			System.out.println(body);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	// @Test
	public void postMethodCall() {
		/*
		 * String payload = "{\"userName\":san1," + "\"password\" : \"123\"}";
		 * 
		 * RestAssured.baseURI = "http://103.130.88.60:8986/";
		 * given().header("Content -Type",
		 * "application/json;charset=utf-8").body(payload). when().post("api/v1/login").
		 * then().log() .all(); .assertThat() //
		 * .statusCode(200);/*.assertThat().body("email",
		 * equalTo("swethana.v@reqres.in"));
		 */
		JSONObject jsobj = new JSONObject();
		jsobj.put("userName", "san1");
		jsobj.put("password", "123");
		given().header("Content-Type", "application/json").body(jsobj.toJSONString()).when()
				.post("http://103.130.88.60:8986/api/v1/login").then().log().all().assertThat().statusCode(200);

	}

//@Test
	public void delmethodcall() {
		String editApi = "http://103.130.88.60:8986/api/v1/delete/notes/{NoteCode}";

		try {
			Unirest.delete(editApi).routeParam("NoteCode", "000008").asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public void editmethodcall() { String editApi =
	 * "https://localhost:4200/api/contacts/{contact_id}";
	 * 
	 * JtwigModel model = JtwigModel.newModel() .with("name", "guru") .with("email",
	 * "guru123@gmail.com");
	 * 
	 * Unirest.put(editApi) .routeParam("contact_id", "125432") .header("accept",
	 * "application/json") .header("Content-Type", "application/json")
	 * .body(template.render(model)) .asJson(); }
	 */
}
