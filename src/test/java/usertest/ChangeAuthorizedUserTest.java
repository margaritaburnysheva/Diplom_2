package usertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;
import user.UserCredentials;
import user.UserDataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class ChangeAuthorizedUserTest {
    private UserClient userClient;
    private String token;
    public String name;
    public String email;
    public String password;
    public String expectedName;
    public String expectedEmail;

    public ChangeAuthorizedUserTest(String name, String email, String password,String expectedName,String expectedEmail) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.expectedName = expectedName;
        this.expectedEmail = expectedEmail;
    }
    @Parameterized.Parameters(name="Тестовые данные: {0}, {1}, {2}, {3}, {4}")
    public static Object[][] dataTwo() {
        return new Object[][]{
                {UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.randomPassword, UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail},
                {UserDataGenerator.secondRandomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.randomPassword, UserDataGenerator.secondRandomName, UserDataGenerator.secondRandomEmail},
                {UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.secondRandomPassword, UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail},
        };
    }
    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());
    }
    @Before
    public void setUp(){
        userClient = new UserClient();
    }
    @After
    public void clearData(){
        userClient.deleteUser(token);
    }
    @Test
    @DisplayName("Change authorized user")
    @Description("Check change authorized user return 200 and expected email and name")
    public void changeAuthorizedUser(){
        User user = new User(UserDataGenerator.randomName, UserDataGenerator.randomEmail, UserDataGenerator.randomPassword);
        userClient.createUser(user);
        token=userClient.loginUser(UserCredentials.from(user))
                .assertThat()
                .body("accessToken",notNullValue())
                .extract().path("accessToken");
        User secondUser=new User(name,email,password);
        userClient.changeUser(UserCredentials.from(secondUser),token)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success",is(true))
                .and()
                .assertThat()
                .body("user.email",is(expectedEmail))
                .and()
                .body("user.name",is(expectedName));
    }
}
