package praktikum.user;

import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class UserClient extends praktikum.Client {
    public static final String REGISTER_USER_PATH = "/api/auth/register";
    public static final String USER_PATH = "/api/auth/user";
    public static final String LOGIN_USER_PATH = "/api/auth/login";

    @Step("Создание пользователя {user.email}")
    public ValidatableResponse createUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(REGISTER_USER_PATH)
                .then().log().all();
    }

    @Step("Логин пользователя {user.email}")
    public ValidatableResponse loginUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(LOGIN_USER_PATH)
                .then().log().all();
    }

    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse updateUserDataWithAccessToken(User user, @Param(mode=HIDDEN)String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_PATH)
                .then().log().all();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateUserDataWithoutAuthorization(User user) {
        return spec()
                .body(user)
                .when()
                .patch(USER_PATH)
                .then().log().all();
    }

    @Step("Удаление пользователя {user.email}")
    public ValidatableResponse deleteUser(@Param(mode=HIDDEN)String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH)
                .then().log().all();
    }
}
