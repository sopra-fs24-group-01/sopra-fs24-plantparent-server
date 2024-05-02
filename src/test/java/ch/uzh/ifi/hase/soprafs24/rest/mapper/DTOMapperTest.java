package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  @Test
  public void testCreateSpace_fromSpacePostDTO_toSpace_success() {
    // create SpacePostDTO
    SpacePostDTO spacePostDTO = new SpacePostDTO();
    spacePostDTO.setSpaceName("Office 1");
    spacePostDTO.setSpaceOwner(new User());
    List<Plant> plants = new ArrayList<>();
    plants.add(new Plant());
    spacePostDTO.setPlantsContained(plants);

    // MAP -> Create user
    Space space = DTOMapper.INSTANCE.convertSpacePostDTOtoEntity(spacePostDTO);

    // check content
    assertEquals(spacePostDTO.getSpaceName(), space.getSpaceName());
    assertEquals(spacePostDTO.getSpaceOwner(), space.getSpaceOwner());
    assertEquals(spacePostDTO.getPlantsContained(), space.getPlantsContained());
  }

  @Test
  public void testGetSpace_fromSpace_toSpaceGetDTO_success() {
    // create User
    Space space = new Space();
    space.setSpaceName("hallway");
    space.setSpaceOwner(new User());
    List<Plant> plants = new ArrayList<>();
    plants.add(new Plant());
    space.setPlantsContained(plants);

    // MAP -> Create UserGetDTO
    SpaceGetDTO spaceGetDTO = DTOMapper.INSTANCE.convertEntityToSpaceGetDTO(space);

    // check content
    assertEquals(space.getSpaceName(), spaceGetDTO.getSpaceName());
    assertEquals(space.getSpaceOwner(), spaceGetDTO.getSpaceOwner());
    assertEquals(space.getPlantsContained(), spaceGetDTO.getPlantsContained());
  }

  @Test
  public void testUpdateSpace_fromSpacePutDTO_toSpace_success() {
    SpacePutDTO spacePutDTO = new SpacePutDTO();
    spacePutDTO.setSpaceName("hallway");
    spacePutDTO.setSpaceOwner(new User());
    List<Plant> plants = new ArrayList<>();
    plants.add(new Plant());
    spacePutDTO.setPlantsContained(plants);

    // MAP -> Create user
    Space space = DTOMapper.INSTANCE.convertSpacePutDTOtoEntity(spacePutDTO);

    // check content
    assertEquals(spacePutDTO.getSpaceName(), space.getSpaceName());
    assertEquals(spacePutDTO.getSpaceOwner(), space.getSpaceOwner());
    assertEquals(spacePutDTO.getPlantsContained(), space.getPlantsContained());
  }
}
