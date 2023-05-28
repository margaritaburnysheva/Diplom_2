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
import user.User;
import user.UserClient;
import user.UserCredentials;
import user.UserDataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ChangeUserEmailToExistingEmailTest {
        private UserClient userClient;
        private String token;
        private String secondToken;

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
            userClient.deleteUser(secondToken);
        }

        @Test
        @DisplayName("Change user email to another existing user's email")
        @Description("Check change user email to another existing user's email return 403 and 'User with such email already exists'")
        public void changeUserEmailToAnotherExistingUsersEmail(){
            User user = new User(UserDataGenerator.randomName, UserDataGenerator.randomEmail, UserDataGenerator.randomPassword);
            userClient.createUser(user);
            token=userClient.loginUser(UserCredentials.from(user))
                    .assertThat()
                    .body("accessToken",notNullValue())
                    .extract().path("accessToken");
            User secondUser=new User(UserDataGenerator.randomName, UserDataGenerator.secondRandomEmail, UserDataGenerator.randomPassword);
            userClient.createUser(secondUser);
            secondToken=userClient.loginUser(UserCredentials.from(secondUser))
                    .assertThat()
                    .body("accessToken",notNullValue())
                    .extract().path("accessToken");
            userClient.changeUser(UserCredentials.from(secondUser),token)
                    .assertThat()
                    .statusCode(SC_FORBIDDEN)
                    .and()
                    .assertThat()
                    .body("message",is("User with such email already exists"));
        }
}
