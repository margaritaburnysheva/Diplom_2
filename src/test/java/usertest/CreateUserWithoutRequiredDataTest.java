package usertest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;
import user.UserDataGeneratorWithNull;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class CreateUserWithoutRequiredDataTest {
    private UserClient userClient;
    private final User userGeneration;
    private final int statusCode;
    private final String errorMessage;
    public CreateUserWithoutRequiredDataTest(User userGeneration, int statusCode, String errorMessage) {
        this.userGeneration=userGeneration;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
    @Parameterized.Parameters(name="Тестовые данные: {0}, {1}, {2}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {UserDataGeneratorWithNull.getDataWithoutName(),SC_FORBIDDEN,"Email, password and name are required fields"},
                {UserDataGeneratorWithNull.getDataWithoutEmail(),SC_FORBIDDEN,"Email, password and name are required fields"},
                {UserDataGeneratorWithNull.getDataWithoutPassword(),SC_FORBIDDEN,"Email, password and name are required fields"},
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

    @Test
    @DisplayName("Create user without required data")
    @Description("Check create user without required data return 403 and 'Email, password and name are required fields'")
    public void createUserWithoutRequiredData() {
        User user=userGeneration;
        userClient.createUser(user)
                .assertThat()
                .statusCode(statusCode)
                .and()
                .assertThat()
                .body("message", is(errorMessage));
    }
}