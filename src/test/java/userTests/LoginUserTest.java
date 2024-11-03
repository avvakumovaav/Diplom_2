package userTests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserChecks;
import praktikum.user.UserClient;

public class LoginUserTest {
    String accessToken;
    private User user;
    UserClient userClient = new UserClient();
    UserChecks userChecks = new UserChecks();
    private Faker faker;

    @Before
    public void init() {
        user = User.createUser();
        ValidatableResponse registerResponse = userClient.createUser(user);
        accessToken = registerResponse.extract().path("accessToken");

        faker = new Faker();
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse response = userClient.deleteUser(accessToken);
            userChecks.checkDeleted(response);
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginExistUserTest() {
        ValidatableResponse response = userClient.loginUser(user);
        userChecks.checkLoggedIn(response, user);
    }

    @Test
    @DisplayName("Логин пользователя с неверным логином")
    public void loginUserWithInvalidEmailTest() {
        User invalidEmailUser = new User(faker.internet().emailAddress(), user.getPassword(), user.getName());
        ValidatableResponse response = userClient.loginUser(invalidEmailUser);
        userChecks.checkLoginWithNotExistedUserFailed(response);
    }

    @Test
    @DisplayName("Логин пользователя с неверным паролем")
    public void loginUserWithInvalidPasswordTest() {
        User invalidPasswordUser = new User(user.getEmail(), faker.internet().password(), user.getName());
        ValidatableResponse response = userClient.loginUser(invalidPasswordUser);
        userChecks.checkLoginWithNotExistedUserFailed(response);
    }
}
