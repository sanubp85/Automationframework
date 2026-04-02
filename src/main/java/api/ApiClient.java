package api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ApiClient {

    private Response response;

    public void get(String url) {
        response = given()
            .header("Content-Type", "application/json")
            .when()
            .get(url)
            .then()
            .extract()
            .response();
    }

    public void getWithToken(String url, String token) {
        response = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .when()
            .get(url)
            .then()
            .extract()
            .response();
    }

    public void post(String url, String body, String token) {
        response = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token)
            .body(body)
            .when()
            .post(url)
            .then()
            .extract()
            .response();
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public String getBody() {
        return response.getBody().asString();
    }
}
