package ordertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import order.Ingredients;
import order.OrderClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderWithoutAuthorizationTest {
    private OrderClient orderClient;
    private List<String> data;

    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());
    }
    @Before
    public void setUp(){
         orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Create order without authorization")
    @Description("Check create order without user authorization return 200 and order number")
    public void createOrderWithoutAuthorization(){
        data=orderClient.getIngredients().extract().path("data._id");
        Ingredients ingredients=new Ingredients(data);
        orderClient.createOrderWithoutAuth(ingredients)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("order.number",notNullValue());
    }
}
