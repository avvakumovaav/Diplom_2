package praktikum.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Map;
import java.util.Set;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class UserChecks {

    @Step("Проверка успешного создания пользователя {user.email} ")
    public void checkCreated(ValidatableResponse response, User user) {
        response.assertThat()
                .statusCode(HTTP_OK)
                .and().body("success", equalTo(true))
                .and().body("accessToken", notNullValue())
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(user.getEmail()))
                .and().body("user.name", equalTo(user.getName()));
    }

    @Step("Проверка удаления пользователя")
    public void checkDeleted(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HTTP_ACCEPTED);
    }

    @Step("Проверка неуспешного добавления пользователя с повторяющимся email")
    public void checkDuplicateEmailFailed(ValidatableResponse response) {
        var body = response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);

        assertEquals(false, body.get("success"));
        assertEquals( "User already exists", body.get("message"));
    }

    @Step("Проверка неуспешного создания пользователя при отсутствии email, пароля или имени в запросе")
    public void checkCreateWithNullEmailOrPasswordOrNameFailed(ValidatableResponse response) {
        var body = response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);

        assertEquals(false, body.get("success"));
        assertEquals( "Email, password and name are required fields", body.get("message"));
    }

    @Step("Проверка логина пользователя")
    public void checkLoggedIn(ValidatableResponse loginResponse, User user) {
        loginResponse.assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Проверка неуспешного логина пользователя с несуществующим логином/паролем")
    public void checkLoginWithNotExistedUserFailed(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка успешного обновления имени авторизованного пользователя")
    public void checkUpdateUserNameWithAuth(ValidatableResponse response, User user, String newName) {
        response.assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName))
                .body("user.email", equalTo(user.getEmail()));
    }

    @Step("Проверка успешного обновления email авторизованного пользователя")
    public void checkUpdateUserEmailWithAuth(ValidatableResponse response, User user, String newEmail) {
        response.assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Проверка успешного обновления пароля авторизованного пользователя")
    public void checkUpdateUserPasswordWithAuth(ValidatableResponse response, User user) {
        response.assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()));
    }

    @Step("Проверка неуспешного обновления неавторизованного пользователя")
    public void checkUpdateUserWithoutAuth(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
