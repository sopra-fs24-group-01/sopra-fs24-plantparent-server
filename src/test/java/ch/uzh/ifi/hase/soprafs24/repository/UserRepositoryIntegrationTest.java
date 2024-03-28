package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.PersistenceException;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByUserName_success() {
    // given
    User user = new User();
    user.setEmail("testUser@email.com");
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");
    user.setToken("1");

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByUsername(user.getUsername());

    // then
    assertNotNull(found.getId());
    assertEquals(found.getEmail(), user.getEmail());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
  }

  @Test
  public void saveUser_withNullUsername_throwsException() {
    User user = new User();
    user.setEmail("testUser@email.com");
    user.setPassword("testPassword");
    user.setUsername(null);
    user.setToken("1");

    assertThrows(PersistenceException.class, () -> {
      entityManager.persistAndFlush(user);
    });
  }

  @Test
  public void saveUser_withNullEmail_throwsException() {
    User user = new User();
    user.setEmail(null);
    user.setPassword("testPassword");
    user.setUsername("testUsername");
    user.setToken("1");

    assertThrows(PersistenceException.class, () -> {
      entityManager.persistAndFlush(user);
    });
  }

  @Test
  public void saveUser_withNullPassword_throwsException() {
    User user = new User();
    user.setEmail("testUser@email.com");
    user.setPassword(null);
    user.setUsername("testUsername");
    user.setToken("1");

    assertThrows(PersistenceException.class, () -> {
      entityManager.persistAndFlush(user);
    });
  }

  @Test
  public void saveUser_withNullToken_throwsException() {
    User user = new User();
    user.setEmail("testUser@email.com");
    user.setPassword("password");
    user.setUsername("testUsername");
    user.setToken(null);

    assertThrows(PersistenceException.class, () -> {
      entityManager.persistAndFlush(user);
    });
  }

  @Test
  public void saveUser_withDuplicateUsername_throwsException() {
    User user1 = new User();
    user1.setUsername("uniqueUser");
    user1.setEmail("user1@example.com");
    user1.setPassword("password1");
    user1.setToken("1");
    userRepository.save(user1);

    User user2 = new User();
    user2.setUsername("uniqueUser"); // same username as user1
    user2.setEmail("user2@example.com");
    user2.setPassword("password2");
    user2.setToken("2");

    assertThrows(DataIntegrityViolationException.class, () -> {
      userRepository.saveAndFlush(user2);
    });
  }

  @Test
  public void saveUser_withDuplicateEmail_throwsException() {
    User user1 = new User();
    user1.setUsername("uniqueUser1");
    user1.setEmail("user1@example.com");
    user1.setPassword("password1");
    user1.setToken("1");
    userRepository.save(user1);

    User user2 = new User();
    user2.setUsername("uniqueUser2"); 
    user2.setEmail("user1@example.com"); // same email as user1
    user2.setPassword("password2");
    user2.setToken("2");

    assertThrows(DataIntegrityViolationException.class, () -> {
      userRepository.saveAndFlush(user2);
    });
  }

  @Test
  public void saveUser_withDuplicateToken_throwsException() {
    User user1 = new User();
    user1.setUsername("uniqueUser1");
    user1.setEmail("user1@example.com");
    user1.setPassword("password1");
    user1.setToken("1");
    userRepository.save(user1);

    User user2 = new User();
    user2.setUsername("uniqueUser2"); 
    user2.setEmail("user2@example.com");
    user2.setPassword("password2"); // same token as user1
    user2.setToken("1");

    assertThrows(DataIntegrityViolationException.class, () -> {
      userRepository.saveAndFlush(user2);
    });
  }

}
