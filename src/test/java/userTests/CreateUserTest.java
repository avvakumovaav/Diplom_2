package userTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserChecks;
import praktikum.user.UserClient;

public class CreateUserTest {
    String accessToken;
    private User user;
    UserClient userClient = new UserClient();
    UserChecks userChecks = new UserChecks();

    @Before
    public void init() {
        user = User.createUser();
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse response = userClient.deleteUser(accessToken);
            userChecks.checkDeleted(response);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        ValidatableResponse createResponse = userClient.createUser(user);
        userChecks.checkCreated(createResponse, user);

        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUserWithExistingEmailTest() {
        ValidatableResponse responseFirstUser = userClient.createUser(user);
        accessToken = responseFirstUser.extract().path("accessToken");

        ValidatableResponse responseSecondUser = userClient.createUser(user);
        userChecks.checkDuplicateEmailFailed(responseSecondUser);
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailTest() {
        User noEmailUser = new User(null, user.getPassword(), user.getName());
        ValidatableResponse response = userClient.createUser(noEmailUser);
        userChecks.checkCreateWithNullEmailOrPasswordFailed(response);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        User noPasswordUser = new User(user.getEmail(), null, user.getName());
        ValidatableResponse response = userClient.createUser(noPasswordUser);
        userChecks.checkCreateWithNullEmailOrPasswordFailed(response);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        User noNameUser = new User(null, user.getPassword(), null);
        ValidatableResponse response = userClient.createUser(noNameUser);
        userChecks.checkCreateWithNullEmailOrPasswordFailed(response);
    }

}
