package praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static praktikum.configuration.Env.BASE_URL;

public class IngrForOrder {
    private static final String PATH_INGREDIENTS = "/api/ingredients";

    @Step("Get ingredient")
    public ValidatableResponse getIngredientsRequest() {
        return given()
                .when()
                .get(BASE_URL + PATH_INGREDIENTS)
                .then();
    }
}