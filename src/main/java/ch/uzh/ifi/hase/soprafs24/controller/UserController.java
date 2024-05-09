package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserLoginPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserLogoutPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @GetMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable Long id) throws ResponseStatusException {
    User user = userService.getUserById(id);

    if (user == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested user with id %s does not exist.", id)
      );
    }
    else {
      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }


  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @PutMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO updateUser(@RequestBody UserPutDTO userPutDTO, @PathVariable Long id) {
    User user = userService.getUserById(id);

    if (user == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested user with id %s does not exist.", id)
      );
    }
    else {
      User updatedUser = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

      user.setUsername(updatedUser.getUsername());
      user.setEmail(updatedUser.getEmail());

      // only set the password if it is included in the DTO
      if (userPutDTO.getPassword() != null && !userPutDTO.getPassword().isEmpty()) {
        user.setPassword(userPutDTO.getPassword());
      }

      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userService.updateUser(user));

    }
  }

  @DeleteMapping("/users/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void deleteUser(@PathVariable Long id) {
    User user = userService.getUserById(id);

    if (user == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested user with id %s does not exist.", id)
      );
    }
    else {
      userService.deleteUser(user);
    }
  }

  @GetMapping("/users/username/{username}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserByUsername(@PathVariable String username) throws ResponseStatusException {
    User user = userService.getUserByUsername(username);

    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO loginUser(@RequestBody UserLoginPostDTO userLoginPostDTO) {
    User authenticatedUser = userService.loginUser(userLoginPostDTO.getUsername(), userLoginPostDTO.getPassword());

    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(authenticatedUser);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  public void logoutUser(@RequestBody UserLogoutPostDTO userLogoutPostDTO) {

    userService.logoutUser(userLogoutPostDTO.getUsername(), userLogoutPostDTO.getToken());
  }

}
