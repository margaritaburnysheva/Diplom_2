package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends UserRest {
    private static final String CREATE_USER_URI = "https://stellarburgers.nomoreparties.site/api/auth/register";
    private static final String LOGIN_USER_URI = "https://stellarburgers.nomoreparties.site/api/auth/login";
    private static final String DELETE_AND_PATCH_USER_URI = "https://stellarburgers.nomoreparties.site/api/auth/user";

    @Step("Create user")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(CREATE_USER_URI)
                .then();
    }
    @Step("Login with user credentials")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(userCredentials)
                .when()
                .post(LOGIN_USER_URI)
                .then();
    }
    @Step("Delete user")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .when()
                .delete(DELETE_AND_PATCH_USER_URI)
                .then();
    }
    @Step("Change user data")
    public ValidatableResponse changeUser(UserCredentials userCredentials, String token) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .body(userCredentials)
                .when()
                .patch(DELETE_AND_PATCH_USER_URI)
                .then();
    }
}