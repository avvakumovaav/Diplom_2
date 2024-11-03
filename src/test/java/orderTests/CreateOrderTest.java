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

public class CreateOrderTest {
    String accessToken;
    private User user;
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
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse response = userClient.deleteUser(accessToken);
            userChecks.checkDeleted(response);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthUserAndIngredientsTest() {
        ValidatableResponse ingredientResponse = ingredientsClient.getIngredients();
        List<String> ingredients = ingredientResponse.extract().path("data._id");
        Order order = new Order(ingredients.subList(0, 1));
        ValidatableResponse orderResponse = orderClient.createOrderWithAuthUser(order, accessToken);
        orderChecks.checkCreatedWithAuthAndIngredients(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthUserAndWithoutIngredientsTest() {
        Order order = new Order(List.of());
        ValidatableResponse orderResponse = orderClient.createOrderWithAuthUser(order, accessToken);
        orderChecks.checkCreatedWithAuthAndWithoutIngredients(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с неверным хэшем ингредиента")
    public void createOrderWithAuthUserAndInvalidIngredientHashTest() {
        ValidatableResponse ingredientResponse = ingredientsClient.getIngredients();
        List<String> ingredients = ingredientResponse.extract().path("data._id");
        String invalidIngredientHash = ingredients.get(1) + "invalid";
        Order order = new Order(List.of(ingredients.get(0), invalidIngredientHash));
        ValidatableResponse orderResponse = orderClient.createOrderWithAuthUser(order, accessToken);
        orderChecks.checkCreatedWithInvalidIngredientHash(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с ингредиентами")
    public void createOrderWithoutAuthUserTest() {
        ValidatableResponse ingredientResponse = ingredientsClient.getIngredients();
        List<String> ingredients = ingredientResponse.extract().path("data._id");
        Order order = new Order(ingredients.subList(0, 1));
        ValidatableResponse orderResponse = orderClient.createOrderWithoutAuthUser(order);
        orderChecks.checkCreatedWithIngredientsWithoutAuthUser(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthUserAndWithoutIngredientsTest() {
        Order order = new Order(List.of());
        ValidatableResponse orderResponse = orderClient.createOrderWithoutAuthUser(order);
        orderChecks.checkCreatedWithAuthAndWithoutIngredients(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с неверным хэшем ингредиента")
    public void createOrderWithoutAuthUserAndInvalidIngredientHashTest() {
        ValidatableResponse ingredientResponse = ingredientsClient.getIngredients();
        List<String> ingredients = ingredientResponse.extract().path("data._id");
        String invalidIngredientHash = ingredients.get(1) + "invalid";
        Order order = new Order(List.of(ingredients.get(0), invalidIngredientHash));
        ValidatableResponse orderResponse = orderClient.createOrderWithoutAuthUser(order);
        orderChecks.checkCreatedWithInvalidIngredientHash(orderResponse);
    }
}
