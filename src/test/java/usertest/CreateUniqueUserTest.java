package usertest;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CreateUniqueUserTest {
    private UserClient userClient;
    private String token;

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
    @DisplayName("Create unique user")
    @Description("Check create unique user return 200 and success: true")
    public void createUniqueUser(){
        Faker faker = new Faker();
        User user = User.builder()
                .name(faker.name().name())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(6,10))
                .build();
        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success",is(true));
        token=userClient.loginUser(UserCredentials.from(user))
                .assertThat()
                .body("accessToken",notNullValue())
                .extract().path("accessToken");
    }
}
