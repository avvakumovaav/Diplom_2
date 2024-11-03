package praktikum.order;

import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class OrderClient extends praktikum.Client {
    public static final String ORDERS_PATH = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuthUser(Order order, @Param(mode = HIDDEN) String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuthUser(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public ValidatableResponse getOrdersWithAuthUser(@Param(mode = HIDDEN) String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public ValidatableResponse getOrdersWithoutAuthUser() {
        return spec()
                .when()
                .get(ORDERS_PATH)
                .then().log().all();
    }
}
