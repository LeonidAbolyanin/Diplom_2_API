import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.Client;
import praktikum.api.Login;
import praktikum.api.Users;
import praktikum.create.GenUsers;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UsersUpdateTest {
    private Client userClient;
    private Users user;
    private String token;
    private String bearerToken;
    private String newEmail;
    private String newPassword;
    private String newName;
    private final static String ERROR_MESSAGE_NOT_AUTHORISED = "You should be authorised";

    @Before
    public void beforeCreateUserTest(){
        userClient = new Client();
        user = GenUsers.getSuccessCreateUser();
        newEmail = GenUsers.getNewEmail();
        newPassword = GenUsers.getNewPassword();
        newName = GenUsers.getNewName();

        ValidatableResponse responseCreate = userClient.createUserRequest(user);
        bearerToken = responseCreate.extract().path("accessToken");
        token = bearerToken.substring(7);

        userClient.loginUserRequest(Login.from(user));
    }

    @After
    public void deleteUser() {
        if(token != null){
            userClient.deleteUserRequest(token);
        }
    }

    @Test
    @DisplayName("Check to update a user with login (Email)")
    @Description("Обновление данных пользователя с авторизацией (Email)")
    public void updateUserEmailWithLoginTest(){
        user.setEmail(newEmail);
        ValidatableResponse responseUpdate = userClient.updateUserRequest(user, token);
        int actualStatusCode = responseUpdate.extract().statusCode();
        Boolean isUserUpdated = responseUpdate.extract().path("success");
        String actualResponce = (responseUpdate.extract().path("user")).toString();
        boolean isEmailUpdated = actualResponce.contains(newEmail);
        assertEquals("StatusCode is not 200", SC_OK, actualStatusCode);
        assertTrue("User is not login", isUserUpdated);
        assertTrue("Email is not new", isEmailUpdated);
    }

    @Test
    @DisplayName("Check to update a user with login (Name)")
    @Description("Обновление данных пользователя с авторизацией (Name)")
    public void updateUserNameWithLoginTest(){
        user.setName(newName);
        ValidatableResponse responseUpdate = userClient.updateUserRequest(user, token);
        int actualStatusCode = responseUpdate.extract().statusCode();
        Boolean isUserUpdated = responseUpdate.extract().path("success");
        String actualResponce = (responseUpdate.extract().path("user")).toString();
        boolean isNameUpdated = actualResponce.contains(newName);
        assertEquals("StatusCode is not 200", SC_OK, actualStatusCode);
        assertTrue("User is not login", isUserUpdated);
        assertTrue("Name is not new", isNameUpdated);
    }

    @Test
    @DisplayName("Check to update a user with login (Password)")
    @Description("Обновление данных пользователя с авторизацией (Password)")
    public void updateUserPasswordWithLoginTest(){
        user.setPassword(newPassword);
        ValidatableResponse responseUpdate = userClient.updateUserRequest(user, token);
        int actualStatusCode = responseUpdate.extract().statusCode();
        Boolean isUserUpdated = responseUpdate.extract().path("success");
        assertEquals("StatusCode is not 200", SC_OK, actualStatusCode);
        assertTrue("User is not login", isUserUpdated);

        ValidatableResponse responseSecondLogin = userClient.loginUserRequest(Login.from(user));
        Boolean isUserSecondlogged = responseSecondLogin.extract().path("success");
        assertTrue("User is not login", isUserSecondlogged);
    }

    @Test
    @DisplayName("Check to update a user without login (Email)")
    @Description("Обвление данных пользователя без авторизации (Email)")
    public void updateUserEmailWithoutLoginTest(){
        user.setEmail(newEmail);
        ValidatableResponse responseUpdate = userClient.updateUserRequest(user, "");
        int actualStatusCode = responseUpdate.extract().statusCode();
        String actualMessage = responseUpdate.extract().path("message");
        assertEquals("StatusCode is not 403", SC_UNAUTHORIZED, actualStatusCode);
        assertEquals("Incorrect message", ERROR_MESSAGE_NOT_AUTHORISED, actualMessage);
    }

    @Test
    @DisplayName("Check to update a user without login (Name)")
    @Description("Обновление данных пользователя без авторизации (Name)")
    public void updateUserNameWithoutLoginTest(){
        user.setName(newName);
        ValidatableResponse responseUpdate = userClient.updateUserRequest(user, "");
        int actualStatusCode = responseUpdate.extract().statusCode();
        String actualMessage = responseUpdate.extract().path("message");
        assertEquals("StatusCode is not 403", SC_UNAUTHORIZED, actualStatusCode);
        assertEquals("Incorrect message", ERROR_MESSAGE_NOT_AUTHORISED, actualMessage);
    }

    @Test
    @DisplayName("Check to update a user without login (Password)")
    @Description("Обновление данных пользователя без авторизации (Password)")
    public void updateUserPasswordWithoutLoginTest(){
        user.setPassword(newPassword);
        ValidatableResponse responseUpdate = userClient.updateUserRequest(user, "");
        int actualStatusCode = responseUpdate.extract().statusCode();
        String actualMessage = responseUpdate.extract().path("message");
        assertEquals("StatusCode is not 403", SC_UNAUTHORIZED, actualStatusCode);
        assertEquals("Incorrect message", ERROR_MESSAGE_NOT_AUTHORISED, actualMessage);
    }
}