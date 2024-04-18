package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class PlantServiceIntegrationTest {

  @Qualifier("plantRepository")
  @Autowired
  private PlantRepository plantRepository;

  @Autowired
  private PlantService plantService;

  private Plant testPlant;
  private Plant anotherTestPlant;
  private static User testUser;
  private static User testCaretaker;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;

// removed @BeforeAll and moved the content to @BeforeEach
// as it caused some problems with my tests

  @BeforeEach
  public void setup() {
    plantRepository.deleteAll();
    userRepository.deleteAll();


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

    testPlant = new Plant();
    testPlant.setPlantName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(owner);
    testPlant.setCaretakers(Collections.singletonList(caretaker));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));

    anotherTestPlant = new Plant();
    anotherTestPlant.setPlantName("Another Test Plant");
    anotherTestPlant.setSpecies("One-Two tree");
    anotherTestPlant.setOwner(owner);
    anotherTestPlant.setCaretakers(Collections.singletonList(caretaker));
    anotherTestPlant.setCareInstructions("Only water at night.");
    anotherTestPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    anotherTestPlant.setWateringInterval(3);
    anotherTestPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
  }

  @Test
  public void createPlant_validInputs_success() {
    assertNull(plantRepository.findByPlantId(1L));

    Plant createdPlant = plantService.createPlant(testPlant);
    User owner = userService.getUserById(createdPlant.getOwner().getId());

    assertEquals(createdPlant.getPlantId(), testPlant.getPlantId());
    assertEquals(createdPlant.getLastWateringDate(), testPlant.getLastWateringDate());
    assertEquals(createdPlant.getOwner().getId(), owner.getId());

  }

  @Test
  public void deletePlant_validInput_success() {
    User newUser = new User();
    newUser.setUsername("asdf");
    newUser.setEmail("asdf@asdf.ch");
    newUser.setPassword("asdf");
    User deletableOwner = userService.createUser(newUser);

    Plant deletablePlant = new Plant();
    deletablePlant.setPlantName("Soon deleted )=");
    deletablePlant.setOwner(deletableOwner);


    Plant createdPlant = plantService.createPlant(deletablePlant);
    Plant foundPlant = plantRepository.findByPlantId(createdPlant.getPlantId());
    assertEquals(foundPlant.getPlantId(), createdPlant.getPlantId());

    plantRepository.delete(createdPlant);

    assertNull(plantRepository.findByPlantId(createdPlant.getPlantId()));
    userService.deleteUser(deletableOwner);
  }

  @Test
  public void deletePlant_nonExistingPlant_throwsException() {
    Long plantId = 98L;
    assertNull(plantRepository.findByPlantId(plantId));

    assertThrows(RuntimeException.class, () -> plantService.deletePlant(testPlant));

  }

  @Test
  public void updatePlant_validInput_success() {
    Long plantId = 98L;
    assertNull(plantRepository.findByPlantId(plantId));

    Plant createdPlant = plantService.createPlant(testPlant);

    createdPlant.setPlantName("new name");
    createdPlant.calculateAndSetNextWateringDate();

    Plant updatedPlant = plantService.updatePlant(createdPlant);

    assertEquals(createdPlant.getPlantName(), updatedPlant.getPlantName());
    assertEquals(createdPlant.getNextWateringDate(), updatedPlant.getNextWateringDate());
  }

  @Test
  public void getPlants_success() {
    assertTrue(plantService.getPlants().isEmpty());

    Plant newPlant = plantService.createPlant(testPlant);

    List<Plant> allPlants = plantService.getPlants();

    Plant firstPlant = allPlants.get(0);
    assertEquals(firstPlant.getPlantName(), testPlant.getPlantName());
    assertEquals(firstPlant.getOwner().getId(), testPlant.getOwner().getId());

    assertThrows(RuntimeException.class, () -> allPlants.get(2));
  }

  @Test
  @Transactional
  public void getOwnedPlantsByUserId_success() {
    testUser = new User();
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setToken("token");
    testUser.getPlantsOwned().add(testPlant);
    testUser.getPlantsOwned().add(anotherTestPlant);
    userRepository.save(testUser);

    List<Plant> plants = plantService.getOwnedPlantsByUserId(testUser.getId());

    assertEquals(2, plants.size());
    assertEquals("Test Plant", plants.get(0).getPlantName());
    assertEquals("Another Test Plant", plants.get(1).getPlantName());

  }

  @Test
  public void getOwnedPlantsByUserId_throwsUserNotFoundException() {
    Long nonExistantID = 999L;

    Exception exception = assertThrows(UserNotFoundException.class, () -> {
      plantService.getOwnedPlantsByUserId(nonExistantID);
    });

    assertTrue(exception.getMessage().contains("User with userId " + nonExistantID + " not found"));
  }

  @Test
  @Transactional
  public void getCaredForPlantsByUserId_success() {
    testUser = new User();
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setToken("token");
    testUser.getPlantsCaredFor().add(testPlant);
    testUser.getPlantsCaredFor().add(anotherTestPlant);
    userRepository.save(testUser);

    List<Plant> plants = plantService.getCaretakerPlantsByUserId(testUser.getId());

    assertEquals(2, plants.size());
    assertEquals("Test Plant", plants.get(0).getPlantName());
    assertEquals("Another Test Plant", plants.get(1).getPlantName());

  }

  @Test
  public void getCaredForPlantsByUserId_throwsUserNotFoundException() {
    Long nonExistantID = 999L;

    Exception exception = assertThrows(UserNotFoundException.class, () -> {
      plantService.getCaretakerPlantsByUserId(nonExistantID);
    });

    assertTrue(exception.getMessage().contains("User with userId " + nonExistantID + " not found"));
  }
}
