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

    @Step("Проверка неуспешного создания пользователя при отсутствии email или пароля в запросе")
    public void checkCreateWithNullEmailOrPasswordFailed(ValidatableResponse response) {
        var body = response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);

        assertEquals(false, body.get("success"));
        assertEquals( "Email, password and name are required fields", body.get("message"));
    }


    @Step("Проверка неуспешного добавления пользователя без логина или пароля")
    public void checkCreateWithoutRequiredFieldsFailed(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .body().as(Map.class);

        assertEquals("Недостаточно данных для создания учетной записи", body.get("message"));
        assertEquals(Set.of("code", "message"), body.keySet());
    }

    @Step("Проверка логина пользователя")
    public int checkLoggedIn(ValidatableResponse loginResponse) {
        int id = loginResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("id");

        assertNotEquals(0, id);

        return id;
    }

    @Step("Проверка неуспешного логина пользователя с несуществующей парой логин-пароль")
    public void checkLoginWithNotExistedUserFailed(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .extract()
                .body().as(Map.class);

        assertEquals("Учетная запись не найдена", body.get("message"));
        assertEquals(Set.of("code","message"), body.keySet());
    }


}
//public class UserChecks {
//    @Step("создался успешно")
//    public void checkCreated(ValidatableResponse response) {
//        boolean created = response
//                .assertThat()
//                .statusCode(HTTP_CREATED)
//                .extract()
//                .path("ok");
//        assertTrue(created);
//    }
//
//    @Step("создать не получилось")
//    public void checkFailed(ValidatableResponse response) {
//        var body = response
//                .assertThat()
//                .statusCode(HTTP_BAD_REQUEST)
//                .extract()
//                .body().as(Map.class);
//
//        assertEquals("Недостаточно данных для создания учетной записи", body.get("message"));
//        assertEquals(Set.of("message"), body.keySet());
//    }
//
//    @Step("залогинился")
//    public int checkLoggedIn(ValidatableResponse loginResponse) {
//        int id = loginResponse
//                .assertThat()
//                .statusCode(HTTP_OK)
//                .extract()
//                .path("id");
//
//        assertNotEquals(0, id);
//
//        return id;
//    }
//
//    public void deleted(ValidatableResponse response) {
//
//    }
//}

