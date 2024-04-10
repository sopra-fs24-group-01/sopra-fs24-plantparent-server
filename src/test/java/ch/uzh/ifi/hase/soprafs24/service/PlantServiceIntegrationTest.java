package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

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
  private static User testUser;
  private static User testCaretaker;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  public static void setupAll() {
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

  }

  @BeforeEach
  public void setup() {
    plantRepository.deleteAll();
    userRepository.deleteAll();

    User owner = userService.createUser(testUser);
    User caretaker = userService.createUser(testCaretaker);

    testPlant = new Plant();
    testPlant.setName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(owner);
    testPlant.setCaretakers(Collections.singletonList(caretaker));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));

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
    deletablePlant.setName("Soon deleted )=");
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

    createdPlant.setName("new name");
    createdPlant.calculateAndSetNextWateringDate();

    Plant updatedPlant = plantService.updatePlant(createdPlant);

    assertEquals(createdPlant.getName(), updatedPlant.getName());
    assertEquals(createdPlant.getNextWateringDate(), updatedPlant.getNextWateringDate());
  }

  @Test
  public void getPlants_success() {
    assertTrue(plantService.getPlants().isEmpty());

    Plant newPlant = plantService.createPlant(testPlant);

    List<Plant> allPlants = plantService.getPlants();

    Plant firstPlant = allPlants.get(0);
    assertEquals(firstPlant.getName(), testPlant.getName());
    assertEquals(firstPlant.getOwner().getId(), testPlant.getOwner().getId());

    assertThrows(RuntimeException.class, () -> allPlants.get(2));
  }
}
