package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setEmail("testUser@email.com");
    userPostDTO.setUsername("username");
    userPostDTO.setPassword("password");

    // MAP -> Create user
    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    assertEquals(userPostDTO.getEmail(), user.getEmail());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
    assertEquals(userPostDTO.getPassword(), user.getPassword());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    user.setEmail("testUser@email.com");
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");
    user.setToken("1");

    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getEmail(), userGetDTO.getEmail());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
  }

  @Test
  public void testUpdateUser_fromUserPutDTO_toUser_success() {
    // create UserPutDTO
    UserPutDTO userPutDTO = new UserPutDTO();
    userPutDTO.setEmail("testUser@email.com");
    userPutDTO.setUsername("username");
    userPutDTO.setPassword("password");

    // MAP -> Create user
    User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

    // check content
    assertEquals(userPutDTO.getEmail(), user.getEmail());
    assertEquals(userPutDTO.getUsername(), user.getUsername());
    assertEquals(userPutDTO.getPassword(), user.getPassword());
  }
}
