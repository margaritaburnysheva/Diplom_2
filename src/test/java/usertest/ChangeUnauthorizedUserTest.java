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
public class ChangeUnauthorizedUserTest {
    private UserClient userClient;
    private String token;
    public String name;
    public String email;
    public String password;
    public int statusCode;
    public String errorMessage;

    public ChangeUnauthorizedUserTest(String name, String email, String password,int statusCode,String errorMessage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.statusCode =statusCode;
        this.errorMessage = errorMessage;
    }
    @Parameterized.Parameters(name="Тестовые данные: {0}, {1}, {2}, {3}, {4}")
    public static Object[][] data() {
        return new Object[][]{
                {UserDataGenerator.secondRandomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.randomPassword,SC_UNAUTHORIZED,"You should be authorised"},
                {UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.randomPassword,SC_UNAUTHORIZED,"You should be authorised"},
                {UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.secondRandomPassword,SC_UNAUTHORIZED,"You should be authorised"},
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
    @DisplayName("Change unauthorized user")
    @Description("Check change unauthorized user return 401 and 'You should be authorised'")
    public void changeUnauthorizedUser(){
        User user = new User(UserDataGenerator.randomName, UserDataGenerator.randomEmail, UserDataGenerator.randomPassword);
        userClient.createUser(user);
        User secondUser=new User(name,email,password);
        userClient.changeUser(UserCredentials.from(secondUser),"")
                .statusCode(statusCode)
                .and()
                .assertThat()
                .body("message",is(errorMessage));
        token=userClient.loginUser(UserCredentials.from(user))
                .assertThat()
                .body("accessToken",notNullValue())
                .extract().path("accessToken");
    }
}
