package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.EmailMessageDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.ZoneId;
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
  private static Space testSpace;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private SpaceRepository spaceRepository;
  @Autowired
  private SpaceService spaceService;

// removed @BeforeAll and moved the content to @BeforeEach
// as it caused some problems with my tests

  @BeforeEach
  public void setup() {
    plantRepository.deleteAll();
    userRepository.deleteAll();
    spaceRepository.deleteAll();

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
    testPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));

    anotherTestPlant = new Plant();
    anotherTestPlant.setPlantName("Another Test Plant");
    anotherTestPlant.setSpecies("One-Two tree");
    anotherTestPlant.setOwner(owner);
    anotherTestPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
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

    //plantRepository.delete(createdPlant);
    plantService.deletePlant(createdPlant);

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


  /*@Test
  public void verifyIfUserIsOwner_success() {
    
    Plant createdPlant = plantService.createPlant(testPlant);
    User owner = userService.getUserById(createdPlant.getOwner().getId());

    assertDoesNotThrow(() -> plantService.verifyIfUserIsOwner(owner.getId(), createdPlant.getPlantId()));
  }

  @Test
  public void verifyIfUserIsOwner_withNonExistantPlant_ShoudlThrowException() {

    Long nonExistantId = 99L;
    Plant createdPlant = plantService.createPlant(testPlant);
    User owner = userService.getUserById(createdPlant.getOwner().getId());

    Exception exception = assertThrows(RuntimeException.class, () -> plantService.verifyIfUserIsOwner(owner.getId(), nonExistantId));
    assertEquals("No plant with " + nonExistantId + " found.", exception.getMessage());
  }

  @Test
  public void verifyIfUserIsOwner_withNonOwnerUser_ShouldThrowException() {

    Long wrongUserId = 999L;
    Plant createdPlant = plantService.createPlant(testPlant);

    Exception exception = assertThrows(RuntimeException.class, () -> plantService.verifyIfUserIsOwner(wrongUserId, createdPlant.getPlantId()));
    assertEquals("The current user is not the owner of this plant.", exception.getMessage());
  }*/

  @Test
  public void validateUser_success() {

    User actualUser = plantService.validateUser(testUser.getId());
    assertNotNull(actualUser);
    assertEquals(testUser.getId(), actualUser.getId());
  }

  @Test
  public void validateUser_UserDoesNotExist_ThrowsException() {

    Long wrongUserId = 999L;

    Exception exception = assertThrows(UserNotFoundException.class, () -> {
      plantService.validateUser(wrongUserId);
    });

    assertEquals("User with userId " + wrongUserId + " not found", exception.getMessage());
  }

  @Test
  public void validatePlant_success() {
    Plant createdPlant = plantService.createPlant(testPlant);
    Plant actualPlant = plantService.validatePlant(createdPlant.getPlantId());
    assertNotNull(actualPlant);
    assertEquals(createdPlant.getPlantId(), actualPlant.getPlantId());
  }

  @Test
  public void validatePlant_PlantDoesNotExist_ThrowsException() {

    Long wrongPlantID = 999L;
    Exception exception = assertThrows(RuntimeException.class, () -> {
      plantService.validatePlant(wrongPlantID);
    });

    assertEquals("No plant with plantId " + wrongPlantID + " found.", exception.getMessage());
  }

  @Test
  public void validateSpace_success() {
    testSpace = new Space();
    testSpace.setSpaceName("Test Space");
    testSpace.setSpaceOwner(testUser);

    Space createdSpace = spaceService.createSpace(testSpace);
    Space actualSpace = plantService.validateSpace(testSpace.getSpaceId());
    assertNotNull(actualSpace);
    assertEquals(createdSpace.getSpaceId(), actualSpace.getSpaceId());
  } 

  @Test
  public void validateSpace_SpaceDoesNotExist_ThrowsException() {

    Long wrongSpaceId = 999L;
    Exception exception = assertThrows(RuntimeException.class, () -> {
      plantService.validateSpace(wrongSpaceId);
    });

    assertEquals("No space with spaceId " + wrongSpaceId + " found.", exception.getMessage());
  }

  @Test
  @Transactional
  public void addCaretakerToPlant_success() {
  
    User newCaretaker = new User();
    newCaretaker.setId(2L);
    newCaretaker.setEmail("newCaretaker@email.com");
    newCaretaker.setUsername("newCaretakerUsername");
    newCaretaker.setPassword("password");
    newCaretaker.setToken("token999");
    User NewCaretaker = userService.createUser(newCaretaker);
    Plant createdPlant = plantService.createPlant(testPlant);

    assertDoesNotThrow(() -> plantService.addCaretakerToPlant(NewCaretaker.getId(), createdPlant.getPlantId()));
   
    // Refetch the plant to get the most updated state
    Plant updatedPlant = plantRepository.findById(createdPlant.getPlantId()).orElseThrow(() -> new RuntimeException("Plant not found"));
   
    assertTrue(updatedPlant.getCaretakers().contains(NewCaretaker));
  }

  /*@Test
  public void addCaretakerToPlant_CaretakerIsOwner_ShouldThrowException() {

    Plant createdPlant = plantService.createPlant(testPlant);

    Exception exception = assertThrows(RuntimeException.class, () -> plantService.addCaretakerToPlant(testUser.getId(), testUser.getId(), createdPlant.getPlantId()));
    assertEquals("Owner cannot add himself as caretaker.", exception.getMessage());
  }*/

  @Test
  @Transactional
  public void deleteCaretakerFromPlant_success() {
  
    Plant createdPlant = plantService.createPlant(testPlant);

    assertDoesNotThrow(() -> plantService.deleteCaretakerFromPlant(testCaretaker.getId(), createdPlant.getPlantId()));

    // Refetch the plant to get the most updated state
    Plant updatedPlant = plantRepository.findById(createdPlant.getPlantId()).orElseThrow(() -> new RuntimeException("Plant not found"));
   
    assertFalse(updatedPlant.getCaretakers().contains(testCaretaker));
  }

  @Test
  public void deleteCaretakerFromPlant_CaretakerNotInCaretakerList_ThrowsException() {
  
    User newCaretaker = new User();
    newCaretaker.setId(2L);
    newCaretaker.setEmail("newCaretaker@email.com");
    newCaretaker.setUsername("newCaretakerUsername");
    newCaretaker.setPassword("password");
    newCaretaker.setToken("token999");
    User NewCaretaker = userService.createUser(newCaretaker);
    Plant createdPlant = plantService.createPlant(testPlant);


    Exception exception = assertThrows(RuntimeException.class, () -> plantService.deleteCaretakerFromPlant(NewCaretaker.getId(), createdPlant.getPlantId()));
    assertEquals("Cannot delete non-existing caretaker", exception.getMessage());
  }

  @Test
  public void testGetOverduePlants_OnePlantOverdue() {

    Plant createdPlant = plantService.createPlant(testPlant);
    anotherTestPlant.setNextWateringDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    Plant anotherCreatedPlant = plantService.createPlant(anotherTestPlant);


    List<EmailMessageDTO> results = plantService.generateEmailMessagesForOverduePlants();
    assertEquals(1, results.size());
    assertEquals(testUser.getEmail(), results.get(0).getTo().get(0).get("Email"));
    assertEquals("Your plant " + testPlant.getPlantName() + " needs watering.", results.get(0).getTextPart());
  }

  @Test
  public void testGetOverduePlants_AllPlantsOkay() {

    testPlant.setNextWateringDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    Plant createdPlant = plantService.createPlant(testPlant);
    anotherTestPlant.setNextWateringDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    Plant anotherCreatedPlant = plantService.createPlant(anotherTestPlant);


    List<EmailMessageDTO> results = plantService.generateEmailMessagesForOverduePlants();
    assertTrue(plantService.generateEmailMessagesForOverduePlants().isEmpty());
  }

  @Test
  public void testGetOverduePlants_NoPlantsAvailable() {
      assertTrue(plantService.generateEmailMessagesForOverduePlants().isEmpty());
  }

  @Transactional
  @Test
  public void waterThisPlant_fieldsUpdated() {
    User myUser = new User();
    myUser.setUsername("Elton");
    myUser.setEmail("Elton@RTL.de");
    myUser.setPassword("contra7");

    myUser = userService.createUser(myUser);

    Calendar today = Calendar.getInstance();
    // Clean up the current time
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);

    Plant myPlant = new Plant();
    myPlant.setPlantName("thirsty plant");
    myPlant.setOwner(myUser);
    Date lastWateringDate = today.getTime();
    myPlant.setLastWateringDate(lastWateringDate);
    myPlant.setWateringInterval(3);

    myPlant = plantService.createPlant(myPlant);

    assertNull(myPlant.getNextWateringDate());

    myPlant = plantService.waterThisPlant(myPlant);

    today.add(Calendar.DATE, myPlant.getWateringInterval());
    Date calculatedNextWateringDate = today.getTime();

    assertEquals(
            myPlant.getNextWateringDate(),
            calculatedNextWateringDate);

    plantService.deletePlant(myPlant);
    userService.deleteUser(myUser);
  }

  @Transactional
  @Test
  public void careForThisPlant_fieldsUpdated() {
    User myUser = new User();
    myUser.setUsername("Elton");
    myUser.setEmail("Elton@RTL.de");
    myUser.setPassword("contra7");

    myUser = userService.createUser(myUser);

    Calendar today = Calendar.getInstance();
    // Clean up the current time
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);

    Plant myPlant = new Plant();
    myPlant.setPlantName("neglected plant");
    myPlant.setOwner(myUser);
    Date lastCaringDate = today.getTime();
    myPlant.setLastCaringDate(lastCaringDate);
    myPlant.setCaringInterval(3);

    myPlant = plantService.createPlant(myPlant);

    assertNull(myPlant.getNextCaringDate());

    myPlant = plantService.careForThisPlant(myPlant);

    today.add(Calendar.DATE, myPlant.getCaringInterval());
    Date calculatedNextCaringDate = today.getTime();

    assertEquals(
            myPlant.getNextCaringDate(),
            calculatedNextCaringDate
    );

    plantService.deletePlant(myPlant);
    userService.deleteUser(myUser);
  }

  @Test
  @Transactional
  public void assignPlantToSpace_success() {

    testSpace = new Space();
    testSpace.setSpaceName("Test Space");
    testSpace.setSpaceOwner(testUser);


    Space assignedSpace = spaceService.createSpace(testSpace);
    Plant newPlant = plantService.createPlant(testPlant);
    // ensure no space has been assigned to plant
    assertEquals(null, newPlant.getSpace());

    plantService.assignPlantToSpace(newPlant.getPlantId(),assignedSpace.getSpaceId());
    // refetch plant
    Plant updatedPlant = plantRepository.findById(newPlant.getPlantId()).orElseThrow(() -> new RuntimeException("Plant not found"));
    assertEquals(assignedSpace, updatedPlant.getSpace());
    assertTrue(assignedSpace.getPlantsContained().contains(newPlant));
  }



  @Test
  @Transactional
  public void removePlantFromSpace_success() {

    testSpace = new Space();
    testSpace.setSpaceName("Test Space");
    testSpace.setSpaceOwner(testUser);
    testSpace.setPlantsContained(new ArrayList<>(Arrays.asList(testPlant)));
    testPlant.setSpace(testSpace);


    Space assignedSpace = spaceService.createSpace(testSpace);
    Plant newPlant = plantService.createPlant(testPlant);
    // ensure correct space has been assigned to plant
    assertEquals(assignedSpace, newPlant.getSpace());

    plantService.removePlantFromSpace(newPlant.getPlantId(),assignedSpace.getSpaceId());
    // refetch plant
    Plant updatedPlant = plantRepository.findById(newPlant.getPlantId()).orElseThrow(() -> new RuntimeException("Plant not found"));
    assertEquals(null, updatedPlant.getSpace());
    assertFalse(assignedSpace.getPlantsContained().contains(newPlant));
  }
}
