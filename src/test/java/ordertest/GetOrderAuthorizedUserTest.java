package ordertest;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import order.Ingredients;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderAuthorizedUserTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private String token;
    private List<String> data;

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
    @DisplayName("Get order authorized user")
    @Description("Check get order authorized user return 200 and order number")
    public void getOrderAuthorizedUser(){
        data=orderClient.getIngredients().extract().path("data._id");
        Ingredients ingredients=new Ingredients(data);
        Faker faker = new Faker();
        User user = User.builder()
                .name(faker.name().name())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(6,10))
                .build();
        UserClient userClient=new UserClient();
        userClient.createUser(user);
        userClient.loginUser(UserCredentials.from(user));
        token=userClient.loginUser(UserCredentials.from(user))
                .extract().path("accessToken");
        orderClient.createOrder(ingredients,token);
        orderClient.getUserOrder(token)
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("total",notNullValue())
                .and()
                .body("orders.number",notNullValue());
    }
}
