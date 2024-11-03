package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderChecks {

    @Step("Проверка успешного создания заказа с авторизацией и ингредиентами")
    public void checkCreatedWithAuthAndIngredients(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HTTP_OK)
                .and().body("success", equalTo(true));
    }

    @Step("Проверка неуспешного создания заказа с авторизацией и без ингредиентов")
    public void checkCreatedWithAuthAndWithoutIngredients(ValidatableResponse response) {
        response.assertThat().statusCode(HTTP_UNAUTHORIZED)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }
}
