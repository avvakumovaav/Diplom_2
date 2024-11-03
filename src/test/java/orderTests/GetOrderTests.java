package orderTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.ingredients.IngredientsClient;
import praktikum.order.Order;
import praktikum.order.OrderChecks;
import praktikum.order.OrderClient;
import praktikum.user.User;
import praktikum.user.UserChecks;
import praktikum.user.UserClient;

import java.util.List;

public class GetOrderTests {
    String accessToken;
    User user;
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    IngredientsClient ingredientsClient = new IngredientsClient();
    OrderChecks orderChecks = new OrderChecks();
    UserChecks userChecks = new UserChecks();

    @Before
    public void init() {
        user = User.createUser();
        ValidatableResponse registerResponse = userClient.createUser(user);
        accessToken = registerResponse.extract().path("accessToken");

        ValidatableResponse ingredientResponse = ingredientsClient.getIngredients();
        List<String> ingredients = ingredientResponse.extract().path("data._id");
        Order order = new Order(ingredients.subList(0, 1));
        ValidatableResponse orderResponse = orderClient.createOrderWithAuthUser(order, accessToken);
        orderChecks.checkCreatedWithAuthAndIngredients(orderResponse);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse response = userClient.deleteUser(accessToken);
            userChecks.checkDeleted(response);
        }
    }

    @Test
    @DisplayName("Получение заказа авторизанного пользователя")
    public void getOrderWithAuthTest() {
        ValidatableResponse response = orderClient.getOrdersWithAuthUser(accessToken);
        orderChecks.checkGetOrderWithAuthUser(response);
    }

    @Test
    @DisplayName("Получение заказа неавторизанного пользователя")
    public void getOrderWithoutAuthTest() {
        ValidatableResponse response = orderClient.getOrdersWithoutAuthUser();
        orderChecks.checkGetOrderWithoutAuthUser(response);
    }
}
