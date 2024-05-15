package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

@WebAppConfiguration
@SpringBootTest
@Transactional
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
  private Plant anotherPlant;
  private static User testMember;
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

    testMember = new User();
    testMember.setEmail("testMember@email.com");
    testMember.setUsername("testMember");
    testMember.setPassword("pword");
    testMember.setToken("token3");

    User owner = userService.createUser(testUser);
    User caretaker = userService.createUser(testCaretaker);
    
    testSpace = new Space();
    testSpace.setSpaceName("Test Space");
    testSpace.setSpaceOwner(owner);

    testPlant = new Plant();
    testPlant.setPlantName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(owner);
    testPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    
    anotherPlant = new Plant();
    anotherPlant.setPlantName("Another Plant");
    anotherPlant.setSpecies("One-Two tree");
    anotherPlant.setOwner(owner);
    anotherPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
    anotherPlant.setCareInstructions("Only water at night.");
    anotherPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    anotherPlant.setWateringInterval(3);
    anotherPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    
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

  @Test
  public void getContainedPlantsBySpaceId_onePlant_success() {
    // initital assertions
    assertTrue(testSpace.getPlantsContained().isEmpty());
    assertTrue(testPlant.getSpace() == null);

    Space newSpace = spaceService.createSpace(testSpace);
    Plant newPlant = plantService.createPlant(testPlant);

    assertTrue(newSpace.getPlantsContained().isEmpty());
    assertTrue(newPlant.getSpace() == null);

    plantService.assignPlantToSpace(newPlant.getPlantId(), newSpace.getSpaceId());

    Plant updatedPlant = plantRepository.findById(newPlant.getPlantId()).orElseThrow(() -> new RuntimeException("Plant not found"));
    Space updatedSpace = spaceRepository.findById(newSpace.getSpaceId()).orElseThrow(() -> new RuntimeException("Space not found"));

    List<Plant> containedPlants = spaceService.getContainedPlantsBySpaceId(updatedSpace.getSpaceId());

    assertTrue(updatedSpace.getPlantsContained().contains(updatedPlant));
    assertTrue(containedPlants.contains(updatedPlant));
    assertEquals(updatedPlant.getSpace(), updatedSpace);
    assertEquals(containedPlants.size(), 1);
  }


  @Test
  public void getContainedPlantsBySpaceId_multiplePlants_success() {

    // initital assertions
    assertTrue(testSpace.getPlantsContained().isEmpty());
    assertTrue(testPlant.getSpace() == null);
    assertTrue(anotherPlant.getSpace() == null);

    Space newSpace = spaceService.createSpace(testSpace);
    Plant newPlant = plantService.createPlant(testPlant);
    Plant anotherNewPlant = plantService.createPlant(anotherPlant);

    assertTrue(newSpace.getPlantsContained().isEmpty());
    assertTrue(newPlant.getSpace() == null);
    assertTrue(anotherNewPlant.getSpace() == null);

    plantService.assignPlantToSpace(newPlant.getPlantId(), newSpace.getSpaceId());
    plantService.assignPlantToSpace(anotherNewPlant.getPlantId(), newSpace.getSpaceId());

    Plant updatedPlant = plantRepository.findById(newPlant.getPlantId()).orElseThrow(() -> new RuntimeException("Plant not found"));
    Space updatedSpace = spaceRepository.findById(newSpace.getSpaceId()).orElseThrow(() -> new RuntimeException("Space not found"));
    Plant anotherUpdatedPlant = plantRepository.findById(anotherPlant.getPlantId()).orElseThrow(() -> new RuntimeException("Plant not found"));

    List<Plant> containedPlants = spaceService.getContainedPlantsBySpaceId(updatedSpace.getSpaceId());

    assertTrue(updatedSpace.getPlantsContained().contains(updatedPlant));
    assertTrue(updatedSpace.getPlantsContained().contains(anotherUpdatedPlant));
    assertTrue(containedPlants.contains(updatedPlant));
    assertTrue(containedPlants.contains(anotherUpdatedPlant));
    assertEquals(updatedPlant.getSpace(), updatedSpace);
    assertEquals(anotherUpdatedPlant.getSpace(), updatedSpace);
    assertEquals(containedPlants.size(), 2);
  }

  @Test
  public void addMemberToSpace_success() {

    // setup
    testSpace.getPlantsContained().add(testPlant);
    testPlant.setSpace(testSpace);

    User newMember = userService.createUser(testMember);
    Space newSpace = spaceService.createSpace(testSpace);

    spaceService.addMemberToSpace(newMember.getId(), newSpace.getSpaceId());

    // refetch
    Space updatedSpace = spaceRepository.findById(newSpace.getSpaceId()).orElseThrow(() -> new RuntimeException("Space not found"));
    User updatedMember = userRepository.findById(newMember.getId()).orElseThrow(() -> new RuntimeException("User not found"));

    assertTrue(updatedSpace.getSpaceMembers().contains(updatedMember));
    assertTrue(updatedMember.getSpaceMemberships().contains(updatedSpace));
    assertTrue(testPlant.getCaretakers().contains(updatedMember));
  }

  @Test
  public void deleteMemberFromSpace_success() {

    // setup
    testSpace.getPlantsContained().add(testPlant);
    testPlant.setSpace(testSpace);
    testMember.getSpaceMemberships().add(testSpace);
    testSpace.getSpaceMembers().add(testMember);
    testPlant.getCaretakers().add(testMember);
    testMember.getPlantsCaredFor().add(testPlant);

    User newMember = userService.createUser(testMember);
    Space newSpace = spaceService.createSpace(testSpace);

    assertTrue(newSpace.getSpaceMembers().contains(newMember));

    spaceService.deleteMemeberFromSpace(newMember.getId(), newSpace.getSpaceId());

    // refetch
    Space updatedSpace = spaceRepository.findById(newSpace.getSpaceId()).orElseThrow(() -> new RuntimeException("Space not found"));
    User updatedMember = userRepository.findById(newMember.getId()).orElseThrow(() -> new RuntimeException("User not found"));

    assertFalse(updatedSpace.getSpaceMembers().contains(updatedMember));
    assertFalse(updatedMember.getSpaceMemberships().contains(updatedSpace));
    assertFalse(testPlant.getCaretakers().contains(updatedMember));
  }

  @Test
  @Transactional
  public void getOwnedSpacesByUserId_success() {
    testUser.getSpacesOwned().add(testSpace);
    userRepository.save(testUser);

    List<Space> spaces = spaceService.getOwnedSpacesByUserId(testUser.getId());

    assertEquals(1, spaces.size());
    assertEquals("Test Space", spaces.get(0).getSpaceName());
  }

  @Test
  @Transactional
  public void getAllMembershipSpacesByUserId_success() {
    Space space = spaceService.createSpace(testSpace);
    User member = userService.createUser(testMember);
    spaceService.addMemberToSpace(member.getId(), space.getSpaceId());


    List<Space> spaces = spaceService.getMembershipSpacesByUserId(member.getId());

    assertEquals(1, spaces.size());
    assertEquals("Test Space", spaces.get(0).getSpaceName());
  }

}
