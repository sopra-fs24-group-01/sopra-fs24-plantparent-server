package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.exceptions.AuthenticationException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;



/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User getUserById(Long id) {
    return this.userRepository.findById(id).orElse(null);
  }

  public User getUserByUsername(String username) {
    User user = this.userRepository.findByUsername(username);
    if (user == null) {
      throw new UserNotFoundException("User with username " + username + " not found");
    }
    return user;
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User updateUser(User user) {
    User existingUser = getUserById(user.getId());
    if (existingUser == null) {
      throw new RuntimeException("Can't update nonexisting user.");
    }
    else {
      userRepository.saveAndFlush(user);
      return user;
    }
  }

  public void deleteUser(User user) {
    User existingUser = getUserById(user.getId());
    if (existingUser == null) {
      throw new RuntimeException("Can't delete nonexisting user.");
    }
    else {
      userRepository.delete(user);
    }
  }

  public User loginUser(String username, String password) {
    User user = getUserByUsername(username);
    
    if (!user.getPassword().equals(password)) {
      throw new AuthenticationException("Can't authenticate user: wrong password.");
    }
    user.setToken(UUID.randomUUID().toString());
    userRepository.saveAndFlush(user);
    return user;
  }

  public void logoutUser(String username, String token) {
    User user = getUserByUsername(username);

    // check if the provided token mathces the stored token
    if (token.equals(user.getToken())) {
      user.setToken(null); // invalidate token 
      userRepository.saveAndFlush(user);
    } else {
      // if tokens don't match 
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
  }

  // Check if the database is already initialized with user data
  public boolean needsInitialization() {
    return userRepository.count() == 0;  // Assumes initialization is needed if no users are found
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the email
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByEmail != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          String.format(baseErrorMessage, "username and email", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByEmail != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "email", "is"));
    }
  }
}
