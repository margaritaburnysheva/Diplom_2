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
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class LoginInvalidDataTest {
    private UserClient userClient;
    private String token;
    public String name;
    public String email;
    public String password;

    public LoginInvalidDataTest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    @Parameterized.Parameters(name="Тестовые данные: {0}, {1}, {2}")
    public static Object[][] data() {
        return new Object[][]{
                {UserDataGenerator.randomName,"", UserDataGenerator.randomPassword},
                {UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.randomPassword},
                {UserDataGenerator.randomName, UserDataGenerator.randomEmail,""},
                {UserDataGenerator.randomName, UserDataGenerator.randomEmail, UserDataGenerator.secondRandomPassword},
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
    @DisplayName("Login with invalid user data")
    @Description("Check login with invalid user data return 401 and 'email or password are incorrect'")
    public void loginWithInvalidUserData(){
        User user = new User(UserDataGenerator.randomName, UserDataGenerator.randomEmail, UserDataGenerator.randomPassword);
        userClient.createUser(user);
        token=userClient.loginUser(UserCredentials.from(user))
                .assertThat()
                .body("accessToken",notNullValue())
                .extract().path("accessToken");
        User secondUser=new User(name,email,password);
        userClient.loginUser(UserCredentials.from(secondUser))
                 .assertThat()
                 .statusCode(SC_UNAUTHORIZED)
                 .and()
                 .assertThat()
                 .body("message",is("email or password are incorrect"));
    }
}
