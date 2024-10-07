package praktikum.api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static praktikum.configuration.Env.BASE_URL;

public class Client {
    private static final String PATH_CREATE = "/api/auth/register";
    private static final String PATH_LOGIN = "/api/auth/login";
    private static final String PATH_USER = "/api/auth/user";

    @Step("Create user")
    public ValidatableResponse createUserRequest(Users user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + PATH_CREATE)
                .then();
    }

    @Step("Login")
    public ValidatableResponse loginUserRequest(Login loginUser) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginUser)
                .when()
                .post(BASE_URL + PATH_LOGIN)
                .then();
    }

    @Step("Refresh user")
    public ValidatableResponse updateUserRequest(Users user, String token) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(BASE_URL + PATH_USER)
                .then();
    }

    @Step("Remove user")
    public ValidatableResponse deleteUserRequest(String token) {
        return given()
                .auth().oauth2(token)
                .when()
                .delete(BASE_URL + PATH_USER)
                .then();
    }
}
