package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.AuthenticationException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setToken("token");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getEmail(), createdUser.getEmail());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
  }

  @Test
  public void getUserByUsername_UserExists_success() {

    // Setup
    Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

    // Execute
    User result = userService.getUserByUsername(testUser.getUsername());

    // Verify
    assertNotNull(result);
    assertEquals(testUser.getUsername(), result.getUsername());

  }

  @Test
  public void getUserByUsername_UserDoesNotExist_ThrowsException() {
    
    //Setup
    String username = "nonExistingUser";
    Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

    //Execute & Verify
    assertThrows(UserNotFoundException.class, () -> {
      userService.getUserByUsername(username);
    });
  }

  @Test 
  public void testLoginUser_success() {
    String initialToken = testUser.getToken();
    Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);

    User result = userService.loginUser(testUser.getUsername(), testUser.getPassword());

    assertNotNull(result.getToken());
    assertNotEquals(initialToken, result.getToken(), "Token should be updated upon login.");
  }

  @Test
  public void testLoginUser_IncorrectPassword() {
    String wrongPassword = "wrongPassword";

    Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);

    assertThrows(AuthenticationException.class, () -> {
      userService.loginUser(testUser.getUsername(), wrongPassword);
    });
  }

  @Test 
  public void testLogoutUser_success() {
    
    // Setup
    Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

    // When
    userService.logoutUser(testUser.getUsername(), testUser.getToken());

    // Then
    assertNull(testUser.getToken());
    verify(userRepository).saveAndFlush(testUser);
  }

  @Test
  public void logoutUser_wrongToken_ThrowsException() {

    // Setup
    Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

    // When
    Exception exception = assertThrows(RuntimeException.class, () -> {
      userService.logoutUser(testUser.getUsername(), "wrongToken");
    });

    // Then
    String expectedMessage = "Invalid token";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

/*
  @Test
  public void createUser_duplicateEmail_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void createUser_duplicateInputs_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }
*/
}
