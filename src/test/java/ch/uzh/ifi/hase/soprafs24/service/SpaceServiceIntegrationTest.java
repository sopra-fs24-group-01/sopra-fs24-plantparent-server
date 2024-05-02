package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

@WebAppConfiguration
@SpringBootTest
public class SpaceServiceIntegrationTest {
    
  @Qualifier("spaceRepository")
  @Autowired
  private SpaceRepository spaceRepository;

  @Autowired
  private SpaceService spaceService;

  private Space testSpace;
  private Plant testPlant;
  private static User testUser;
  private static User testCaretaker;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PlantService plantService;
  @Autowired
  private PlantRepository plantRepository;

  @BeforeEach
  public void setup() {
    spaceRepository.deleteAll();
    userRepository.deleteAll();
    plantRepository.deleteAll();

    testUser = new User();
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setToken("token");

    testCaretaker = new User();
    testCaretaker.setEmail("testCaretaker@email.com");
    testCaretaker.setUsername("testCaretakerUsername");
    testCaretaker.setPassword("password");
    testCaretaker.setToken("token2");

    User owner = userService.createUser(testUser);
    User caretaker = userService.createUser(testCaretaker);

    testSpace = new Space();
    testSpace.setSpaceName("Test Space");
    testSpace.setSpaceOwner(testUser);
    testSpace.setPlantsContained(new ArrayList<>(Arrays.asList(testPlant)));

    testPlant = new Plant();
    testPlant.setPlantName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(owner);
    testPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));

  }

  @Test
  public void createSpace_validInputs_success() {
    assertNull(spaceRepository.findBySpaceId(1L));

    Space createdSpace = spaceService.createSpace(testSpace);
    User owner = userService.getUserById(createdSpace.getSpaceOwner().getId());

    assertEquals(createdSpace.getSpaceId(), testSpace.getSpaceId());
    assertEquals(createdSpace.getPlantsContained(), testSpace.getPlantsContained());
    assertEquals(createdSpace.getSpaceOwner().getId(), owner.getId());
    assertEquals(createdSpace.getSpaceName(), testSpace.getSpaceName());
  }


  @Test
  public void deleteSpace_validInput_success() {
    User newUser = new User();
    newUser.setUsername("asdf");
    newUser.setEmail("asdf@asdf.ch");
    newUser.setPassword("asdf");
    User deletableOwner = userService.createUser(newUser);

    Space deletableSpace = new Space();
    deletableSpace.setSpaceName("Soon deleted )=");
    deletableSpace.setSpaceOwner(deletableOwner);


    Space createdSpace = spaceService.createSpace(deletableSpace);
    Space foundSpace = spaceRepository.findBySpaceId(createdSpace.getSpaceId());
    assertEquals(foundSpace.getSpaceId(), createdSpace.getSpaceId());

    //spaceRepository.delete(createdSpace);
    spaceService.deleteSpace(createdSpace);

    assertNull(spaceRepository.findBySpaceId(createdSpace.getSpaceId()));
    userService.deleteUser(deletableOwner);
  }

  @Test
  public void deleteSpace_nonExistingSpace_throwsException() {
    Long spaceId = 98L;
    assertNull(spaceRepository.findBySpaceId(spaceId));

    assertThrows(RuntimeException.class, () -> spaceService.deleteSpace(testSpace));
  }

  @Test
  public void updateSpace_validInput_success() {
    Long spaceId = 98L;
    assertNull(spaceRepository.findBySpaceId(spaceId));

    Space createdSpace = spaceService.createSpace(testSpace);

    createdSpace.setSpaceName("new name");

    Space updatedPlant = spaceService.updateSpace(createdSpace);

    assertEquals(createdSpace.getSpaceName(), updatedPlant.getSpaceName());
  }

  @Test
  public void getSpaces_success() {
    assertTrue(spaceService.getSpaces().isEmpty());

    Space newSpace = spaceService.createSpace(testSpace);

    List<Space> allSpaces = spaceService.getSpaces();

    Space firstSpace = allSpaces.get(0);
    assertEquals(firstSpace.getSpaceName(), testSpace.getSpaceName());
    assertEquals(firstSpace.getSpaceOwner().getId(), testSpace.getSpaceOwner().getId());

    assertThrows(RuntimeException.class, () -> allSpaces.get(2));
  }

}
