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
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderWithoutIngredientsTest {
    private OrderClient orderClient;
    private String token;
    private List<String> data;

    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());
    }
    @Before
    public void setUp(){
        orderClient = new OrderClient();
    }
    @After
    public void clearData(){
        UserClient userClient=new UserClient();
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("Check create order without ingredients return 400 and 'Ingredient ids must be provided'")
    public void createOrderWithoutIngredients(){
        Ingredients ingredients=new Ingredients(null);
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
        orderClient.createOrder(ingredients, token)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message",is("Ingredient ids must be provided"));
    }
}
