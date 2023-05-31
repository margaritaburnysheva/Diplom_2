package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.UserRest;

import static io.restassured.RestAssured.given;

public class OrderClient extends UserRest {
    private static final String GET_ORDER_URI = "https://stellarburgers.nomoreparties.site/api/orders";
    private static final String CREATE_ORDER_URI ="https://stellarburgers.nomoreparties.site/api/orders";
    private static final String GET_INGREDIENTS_URI="https://stellarburgers.nomoreparties.site/api/ingredients";
    @Step("Get order")
    public ValidatableResponse getUserOrder(String token){
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .when()
                .get(GET_ORDER_URI)
                .then();
    }
    @Step("Create order")
    public ValidatableResponse createOrder(Ingredients ingredients, String token){
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }
    @Step("Create order without authorization")
    public ValidatableResponse createOrderWithoutAuth(Ingredients ingredients){
        return given()
                .spec(getBaseReqSpec())
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }
    @Step("Get ingredients")
    public ValidatableResponse getIngredients(){
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(GET_INGREDIENTS_URI)
                .then();
    }
}
