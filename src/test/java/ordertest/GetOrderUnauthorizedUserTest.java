package ordertest;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import order.OrderClient;
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

public class GetOrderUnauthorizedUserTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private String token;

    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
        orderClient = new OrderClient();
    }
    @After
    public void clearData(){
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Get order unauthorized user")
    @Description("Check get order unauthorized user return 401 and 'You should be authorised'")
    public void getOrderUnauthorizedUser(){
        Faker faker=new Faker();
        User user = User.builder()
                .name(faker.name().name())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(6,10))
                .build();
        userClient.createUser(user);
        orderClient.getUserOrder("")
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message",is("You should be authorised"));
        token=userClient.loginUser(UserCredentials.from(user))
                .assertThat()
                .body("accessToken",notNullValue())
                .extract().path("accessToken");
    }
}
