package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.*;

public class OrderChecks {

    @Step("Проверка успешного создания заказа с авторизацией и ингредиентами")
    public void checkCreatedWithAuthAndIngredients(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HTTP_OK)
                .and().body("success", equalTo(true));
    }

    @Step("Проверка неуспешного создания заказа без ингредиентов")
    public void checkCreatedWithAuthAndWithoutIngredients(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_BAD_REQUEST)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка неуспешного создания заказа с неверным хешем ингредиентов")
    public void checkCreatedWithInvalidIngredientHash(ValidatableResponse response) {
        response.assertThat().statusCode(500)
                .and()
                .body(containsString("Internal Server Error"));
    }

    @Step("Проверка успешного создания заказа c ингредиентами без авторизации")
    public void checkCreatedWithIngredientsWithoutAuthUser(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }
}
