package praktikum.ingredients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class IngredientsClient extends praktikum.Client{
    public static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получение списка всех ингредиентов")
    public ValidatableResponse getIngredients() {
        return spec()
                .when()
                .get(INGREDIENTS_PATH)
                .then().log().all();
    }
}
