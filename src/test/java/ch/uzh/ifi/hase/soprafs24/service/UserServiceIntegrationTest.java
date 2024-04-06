package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getEmail(), createdUser.getEmail());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the email but forget about the username
    testUser2.setEmail("testUser2@email.com");
    testUser2.setUsername("testUsername");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  @Test
  public void deleteUser_validInput_success() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    User createdUser = userService.createUser(testUser);

    userService.deleteUser(createdUser);
    assertNull(userRepository.findByUsername("testUsername"));
  }

  @Test
  public void deleteUser_nonExistentUser_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setId(1L);
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");

    assertThrows(RuntimeException.class, () -> userService.deleteUser(testUser));
  }

  @Test
  public void udpateUser_validInput_success() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    User createdUser = userService.createUser(testUser);

    createdUser.setEmail("other@mail.com");
    createdUser.setUsername("otherName");
    createdUser.setPassword("otherPass");

    User updatedUser = userService.updateUser(createdUser);

    assertEquals(createdUser.getId(), updatedUser.getId());
    assertEquals(createdUser.getEmail(), updatedUser.getEmail());
    assertEquals(createdUser.getUsername(), updatedUser.getUsername());
    assertEquals(createdUser.getPassword(), updatedUser.getPassword());

  }
}
