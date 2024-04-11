package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.AuthenticationException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserLoginPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserLogoutPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  /**
   * get 200, all users
   */
  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    user.setEmail("testUser@email.com");
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", is(user.getEmail())))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())));
  }

  /**
   * get 200, one user
   */
  @Test
  public void givenUser_whenGetUser_thenReturnJson() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setEmail("testUser@email.com");
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");

    given(userService.getUserById(user.getId())).willReturn(user);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users/" + user.getId().toString())
            .contentType(MediaType.APPLICATION_JSON
            );

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(user.getId().intValue())))
            .andExpect(jsonPath("$.username", is(user.getUsername())))
            .andExpect(jsonPath("$.email", is(user.getEmail())));
  }

  /**
   * get 404, one user
   */
  @Test
  public void givenUsers_whenGetMissingUser_exceptionThrown() throws Exception {
    // when
    MockHttpServletRequestBuilder getRequest = get("/users/99")
            .contentType(MediaType.APPLICATION_JSON
            );

    // then
    mockMvc.perform(getRequest).andExpect(status().isNotFound());
  }

  /**
   * post 201
   */
  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setEmail("testUser@email.com");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setPassword("testPassword");

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setEmail("testUser@email.com");
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.email", is(user.getEmail())))
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  /**
   * post 409
   */
  @Test
  public void createUser_invalidInput_exceptionThrown() throws Exception {
    // given
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");
    userPostDTO.setUsername("testUser");
    userPostDTO.setEmail("test@plantparent.ch");
    userPostDTO.setPassword("asdf");

    given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest).andExpect(status().isConflict());
  }

  /**
   * put 204
   */
  @Test
  public void updateUser_validInput_userUpdated() throws Exception {
    // existing user
    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setUsername("testUser");
    existingUser.setEmail("asdf@plantparent.ch");
    existingUser.setPassword("asdf");

    UserPutDTO userPutDTO = new UserPutDTO();
    userPutDTO.setUsername("Fancy Username");
    userPutDTO.setEmail("ddd@plantparent.ch");
    userPutDTO.setPassword("dkdkdk");

    given(userService.updateUser(Mockito.any())).willReturn(existingUser);
    given(userService.getUserById(Mockito.any())).willReturn(existingUser);


    // when/then -> put the new user, then check the changed user
    MockHttpServletRequestBuilder putRequest = put("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userPutDTO));

    mockMvc.perform(putRequest).andExpect(status().isNoContent());

  }

  /**
   * put 404
   */
  @Test
  public void updateUser_invalidInput_exceptionThrown() throws Exception {
    // non-existent user
    UserPutDTO userPutDTO = new UserPutDTO();
    userPutDTO.setUsername("Fancy Username");
    userPutDTO.setEmail("ddd@plantparent.ch");
    userPutDTO.setPassword("dkdkdk");

    // when/then -> put the new user, then check the changed user
    MockHttpServletRequestBuilder putRequest = put("/users/15")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userPutDTO));

    mockMvc.perform(putRequest).andExpect(status().isNotFound());
  }

  /**
   * delete 204
   */
  @Test
  public void deleteUser_existingUser_userDeleted() throws Exception {
    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setUsername("testUser");
    existingUser.setEmail("asdf@plantparent.ch");
    existingUser.setPassword("asdf");

    given(userService.getUserById(Mockito.any())).willReturn(existingUser);

    MockHttpServletRequestBuilder deleteRequest = delete("/users/1");

    mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
  }

  /**
   * delete 404
   */
  @Test
  public void deleteUser_nonExistingUser_exceptionThrown() throws Exception {
    MockHttpServletRequestBuilder deleteRequest = delete("/users/1");
    mockMvc.perform(deleteRequest).andExpect(status().isNotFound());
  }

  @Test
  public void testLogin_Success() throws Exception {

    // given
    User user = new User();
    user.setId(1L);
    user.setEmail("testUser@email.com");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setPassword("testPassword");

    UserLoginPostDTO userLoginPostDTO = new UserLoginPostDTO();
    userLoginPostDTO.setUsername("testUsername");
    userLoginPostDTO.setPassword("testPassword");

    given(userService.loginUser(Mockito.anyString(), Mockito.anyString())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userLoginPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.email", is(user.getEmail())))
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

@Test
public void testLogin_Failure_IncorrectPassword() throws Exception {
    // Given
    UserLoginPostDTO userLoginPostDTO = new UserLoginPostDTO();
    userLoginPostDTO.setUsername("testUsername");
    userLoginPostDTO.setPassword("wrongPassword");

    Mockito.doThrow(new AuthenticationException("Incorrect password"))
           .when(userService).loginUser(eq("testUsername"), eq("wrongPassword"));

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userLoginPostDTO));

    // then
    mockMvc.perform(postRequest)
            .andExpect(status().isUnauthorized())
            .andExpect(content().string(containsString("Incorrect password")));
}

@Test
public void getUserByUsername_Success() throws Exception {
  
  // given
  User user = new User();
  user.setId(1L);
  user.setEmail("testUser@email.com");
  user.setUsername("testUsername");
  user.setToken("1");
  user.setPassword("testPassword");

  Mockito.when(userService.getUserByUsername("testUsername")).thenReturn(user);

  mockMvc.perform(get("/users/username/testUsername"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username", is("testUsername")))
          .andExpect(jsonPath("$.email", is("testUser@email.com")));
}

@Test
public void getUserByUsername_NotFound() throws Exception {
  Mockito.when(userService.getUserByUsername("nonExistingUsername"))
    .thenThrow(new UserNotFoundException("User with username nonExistingUsername not found"));

    mockMvc.perform(get("/users/username/nonExistingUsername"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("User with username nonExistingUsername not found")));
}

@Test
public void logoutUser_Success() throws Exception {

  // Given
  UserLogoutPostDTO userLogoutPostDTO = new UserLogoutPostDTO();
  userLogoutPostDTO.setUsername("testUsername");
  userLogoutPostDTO.setToken("testToken");

  // when/then -> do the request + validate the result
  MockHttpServletRequestBuilder postRequest = post("/logout")
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(userLogoutPostDTO));

  // then
  mockMvc.perform(postRequest)
  .andExpect(status().isOk());

  // Verify that the userService.logoutUser method was called with the correct username and token
  verify(userService, times(1)).logoutUser("testUsername", "testToken");
}

@Test
public void logoutUser_UserNotFound() throws Exception {

  // Given
  UserLogoutPostDTO userLogoutPostDTO = new UserLogoutPostDTO();
  userLogoutPostDTO.setUsername("NonExistantUsername");
  userLogoutPostDTO.setToken("testToken");

  doThrow(new UserNotFoundException("User with username NonExistantUsername not found"))
    .when(userService).logoutUser("NonExistantUsername", "testToken");

  // when/then -> do the request + validate the result
  MockHttpServletRequestBuilder postRequest = post("/logout")
  .contentType(MediaType.APPLICATION_JSON)
  .content(asJsonString(userLogoutPostDTO));

  // then
  mockMvc.perform(postRequest)
  .andExpect(status().isNotFound())
  .andExpect(content().string(containsString("User with username NonExistantUsername not found")));

  // Verify that the userService.invalidateToken method was called
  verify(userService, times(1)).logoutUser("NonExistantUsername", "testToken");
}

@Test
public void logoutUser_wrongToken() throws Exception {

  // Given
  UserLogoutPostDTO userLogoutPostDTO = new UserLogoutPostDTO();
  userLogoutPostDTO.setUsername("testUsername");
  userLogoutPostDTO.setToken("wrongToken");

  doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"))
    .when(userService).logoutUser(eq("testUsername"), eq("wrongToken"));

  // when/then -> do the request + validate the result
  MockHttpServletRequestBuilder postRequest = post("/logout")
  .contentType(MediaType.APPLICATION_JSON)
  .content(asJsonString(userLogoutPostDTO));

  // then
  mockMvc.perform(postRequest)
  .andExpect(status().isUnauthorized());

  // Verify that the userService.invalidateToken method was called
  verify(userService, times(1)).logoutUser("testUsername", "wrongToken");
}


  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              String.format("The request body could not be created.%s", e));
    }
  }
}