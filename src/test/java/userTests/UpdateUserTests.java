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

public class UpdateUserTests {
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
    @DisplayName("Обновление имени авторизованного пользователя")
    public void updateUserNameWithAuthTest() {
        String newName = faker.name().firstName();
        User updateUser = new User(user.getEmail(), user.getPassword(), newName);
        ValidatableResponse updateResponse = userClient.updateUserDataWithAccessToken(updateUser, accessToken);
        userChecks.checkUpdateUserNameWithAuth(updateResponse, updateUser, newName);
    }

    @Test
    @DisplayName("Обновление email авторизованного пользователя")
    public void updateUserEmailWithAuthTest() {
        String newEmail = faker.internet().emailAddress();
        User updateUser = new User(newEmail, user.getPassword(), user.getName());
        ValidatableResponse updateResponse = userClient.updateUserDataWithAccessToken(updateUser, accessToken);
        userChecks.checkUpdateUserEmailWithAuth(updateResponse, updateUser, newEmail);
    }

    @Test
    @DisplayName("Обновление пароля авторизованного пользователя")
    public void updateUserPasswordWithAuthTest() {
        String newPassword = faker.internet().password();
        User updateUser = new User(user.getEmail(), newPassword, user.getName());
        ValidatableResponse updateResponse = userClient.updateUserDataWithAccessToken(updateUser, accessToken);
        userChecks.checkUpdateUserPasswordWithAuth(updateResponse, updateUser);
        ValidatableResponse response = userClient.loginUser(updateUser);
        userChecks.checkLoggedIn(response, user);
    }

    @Test
    @DisplayName("Обновление имени неавторизованного пользователя")
    public void updateUserNameWithoutAuthTest() {
        String newName = faker.name().firstName();
        User updateUser = new User(user.getEmail(), user.getPassword(), newName);
        ValidatableResponse updateResponse = userClient.updateUserDataWithoutAuthorization(updateUser);
        userChecks.checkUpdateUserWithoutAuth(updateResponse);
    }

    @Test
    @DisplayName("Обновление email неавторизованного пользователя")
    public void updateUserEmailWithoutAuthTest() {
        String newEmail = faker.internet().emailAddress();
        User updateUser = new User(newEmail, user.getPassword(), user.getName());
        ValidatableResponse updateResponse = userClient.updateUserDataWithoutAuthorization(updateUser);
        userChecks.checkUpdateUserWithoutAuth(updateResponse);
    }

    @Test
    @DisplayName("Обновление пароля неавторизованного пользователя")
    public void updateUserPasswordWithoutAuthTest() {
        String newPassword = faker.internet().password();
        User updateUser = new User(user.getEmail(), newPassword, user.getName());
        ValidatableResponse updateResponse = userClient.updateUserDataWithoutAuthorization(updateUser);
        userChecks.checkUpdateUserWithoutAuth(updateResponse);
    }
}
