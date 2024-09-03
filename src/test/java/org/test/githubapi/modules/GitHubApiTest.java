package org.test.githubapi.modules;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GitHubApiTest {

    private String baseUrl = "https://api.github.com";
    private String token = "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";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
    }

    @Test
    @Description("Test Case 1: No Token Provided")
    public void testNoTokenProvided() {
        Response response = given()
                .when()
                .get("/user")
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 Unauthorized status code");
    }

    @Description("Test Case 2: Invalid Token Provided")
    @Test
    public void testInvalidTokenProvided() {
        Response response = given()
                .header("Authorization", "Bearer ghp_pabuLTVZW2FgnOeD6xfY5VuwW8ovVH0_invalid")
                .when()
                .get("/user")
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 Unauthorized status code");
    }

    @Description("Test Case 3: Forbidden Access")
    @Test
    public void testForbiddenAccess() {
        String insufficientPermissionToken = "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";

        Response response = given()
                .header("Authorization", "Bearer " + insufficientPermissionToken)
                .when()
                .get("/user")
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 403, "Expected 403 Forbidden status code");
    }

    @Description("Test Case 4: Get User With Valid Token")
    @Test
    public void testGetUserWithValidToken() {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/user")
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK status code");
        Assert.assertNotNull(response.jsonPath().getString("login"), "User login should not be null");
    }

    @Description("Test Case 5: Update User Bio With Valid Token")
    @Test
    public void testUpdateUserBioWithValidToken() {
        String newBio = "Bio Updated.";

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body("{\"bio\": \"" + newBio + "\"}")
                .when()
                .patch("/user")
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK status code");
        Assert.assertEquals(response.jsonPath().getString("bio"), newBio, "Bio should be updated");
    }
}
