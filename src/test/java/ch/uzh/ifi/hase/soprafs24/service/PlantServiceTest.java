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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class PlantServiceTest {

  @Mock
  private PlantRepository plantRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SpaceRepository spaceRepository;

  @InjectMocks
  private PlantService plantService;

  private Plant testPlant;
  private Plant anotherTestPlant;
  private User testUser;
  private User testCaretaker;
  private Space testSpace;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId(1L);
    testUser.setEmail("testUser@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setToken("token");

    testCaretaker = new User();
    testCaretaker.setId(2L);
    testCaretaker.setEmail("testCaretaker@email.com");
    testCaretaker.setUsername("testCaretakerUsername");
    testCaretaker.setPassword("password");
    testCaretaker.setToken("token2");

    testPlant = new Plant();
    testPlant.setPlantName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(testUser);
    testPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    //testPlant.calculateAndSetNextWateringDate();

    anotherTestPlant = new Plant();
    anotherTestPlant.setPlantName("Another Test Plant");
    anotherTestPlant.setSpecies("One-Two tree");
    anotherTestPlant.setOwner(testUser);
    anotherTestPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
    anotherTestPlant.setCareInstructions("Only water at night.");
    anotherTestPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    anotherTestPlant.setWateringInterval(3);
    anotherTestPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    //anotherTestPlant.calculateAndSetNextWateringDate();

    testSpace = new Space();
    testSpace.setSpaceId(10L);
    testSpace.setSpaceName("Test Space");
    testSpace.setSpaceOwner(testUser);
    testSpace.setPlantsContained(new ArrayList<>(Arrays.asList(testPlant)));

    Mockito.when(plantRepository.save(Mockito.any())).thenReturn(testPlant);
  }

  @Test
  public void createPlant_validInputs_success() {
    // when
    Plant createdPlant = plantService.createPlant(testPlant);

    //then
    Mockito.verify(plantRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testPlant.getPlantId(), createdPlant.getPlantId());
    assertEquals(testPlant.getPlantName(), createdPlant.getPlantName());
    assertEquals(testPlant.getNextWateringDate(), createdPlant.getNextWateringDate());
  }

  @Test
  public void getPlantById_PlantExists_success() {
    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));

    Plant result = plantService.getPlantById(testPlant.getPlantId());

    assertNotNull(result);
    assertEquals(testPlant.getPlantName(), result.getPlantName());
  }

  @Test
  public void getPlantById_PlantDoesNotExist_ReturnNull() {
    Mockito.when(plantRepository.findByPlantId(99L)).thenReturn(null);

    assertNull(plantService.getPlantById(99L));
  }

  @Test
  public void savePlant_NoOwnerSet_ThrowsException() {
    Plant somePlant = new Plant();
    somePlant.setPlantName("incompletePlant");

    assertThrows(RuntimeException.class, () -> plantService.createPlant(somePlant));
  }

  @Test
  public void getOwnedPlantsByUserId_success() {
    testUser.getPlantsOwned().add(testPlant);
    testUser.getPlantsOwned().add(anotherTestPlant);

    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
  
    List<Plant> plants = plantService.getOwnedPlantsByUserId(testUser.getId());
    
    assertNotNull(plants);
    assertEquals(2, plants.size());
    assertEquals("Test Plant", plants.get(0).getPlantName());
    assertEquals("Another Test Plant", plants.get(1).getPlantName());
    verify(userRepository).findById(testUser.getId());
  }

  @Test
  public void getOwnedPlantsByUserId_ShouldThrowExceptionWhenUserNotFound() {

    Long nonExistantId = 99L;

    Mockito.when(userRepository.findById(nonExistantId)).thenReturn(Optional.empty());
    
    Exception exception = assertThrows(UserNotFoundException.class, () -> {
      plantService.getOwnedPlantsByUserId(nonExistantId);
    });

    assertEquals("User with userId " + nonExistantId + " not found", exception.getMessage());
  }


  @Test
  public void getPlantsCaredForByUserId_success() {
    testUser.getPlantsCaredFor().add(testPlant);
    testUser.getPlantsCaredFor().add(anotherTestPlant);

    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
  
    List<Plant> plants = plantService.getCaretakerPlantsByUserId(testUser.getId());
    
    assertNotNull(plants);
    assertEquals(2, plants.size());
    assertEquals("Test Plant", plants.get(0).getPlantName());
    assertEquals("Another Test Plant", plants.get(1).getPlantName());
    verify(userRepository).findById(testUser.getId());
  }

  @Test
  public void getPlantsCaredForByUserId_ShouldThrowExceptionWhenUserNotFound() {

    Long nonExistantId = 99L;

    Mockito.when(userRepository.findById(nonExistantId)).thenReturn(Optional.empty());
    
    Exception exception = assertThrows(UserNotFoundException.class, () -> {
      plantService.getCaretakerPlantsByUserId(nonExistantId);
    });

    assertEquals("User with userId " + nonExistantId + " not found", exception.getMessage());
  }



  @Test
  public void validateUser_success() {

    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

    User actualUser = plantService.validateUser(testUser.getId());
    assertNotNull(actualUser);
    assertEquals(testUser.getId(), actualUser.getId());
  }

  @Test
  public void validateUser_UserDoesNotExist_ThrowsException() {

    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

    Exception exception = assertThrows(UserNotFoundException.class, () -> {
      plantService.validateUser(testUser.getId());
    });

    assertEquals("User with userId " + testUser.getId() + " not found", exception.getMessage());
  }

  @Test
  public void validatePlant_success() {

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));

    Plant actualPlant = plantService.validatePlant(testPlant.getPlantId());
    assertNotNull(actualPlant);
    assertEquals(testPlant.getPlantId(), actualPlant.getPlantId());
  }

  @Test
  public void validatePlant_PlantDoesNotExist_ThrowsException() {

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> {
      plantService.validatePlant(testPlant.getPlantId());
    });

    assertEquals("No plant with plantId " + testPlant.getPlantId() + " found.", exception.getMessage());
  }

  @Test
  public void validateSpace_success() {

    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    Space actualSpace = plantService.validateSpace(testSpace.getSpaceId());
    assertNotNull(actualSpace);
    assertEquals(testSpace.getSpaceId(), actualSpace.getSpaceId());
  }

  @Test
  public void validateSpace_SpaceDoesNotExist_ThrowsException() {

    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> {
      plantService.validateSpace(testSpace.getSpaceId());
    });

    assertEquals("No space with spaceId " + testSpace.getSpaceId() + " found.", exception.getMessage());
  }

  /*@Test
  public void verifyIfUserIsOwner_success() {

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));

    assertDoesNotThrow(() -> plantService.verifyIfUserIsOwner(testUser.getId(), testPlant.getPlantId()));
  }
  @Test
  public void verifyIfUserIsOwner_withNonExistantPlant_ShoudlThrowException() {

    Long nonExistantId = 99L;

    Mockito.when(userRepository.findById(nonExistantId)).thenReturn(Optional.empty());
    Exception exception = assertThrows(RuntimeException.class, () -> plantService.verifyIfUserIsOwner(testUser.getId(), nonExistantId));
    assertEquals("No plant with " + nonExistantId + " found.", exception.getMessage());
  }

  @Test
  public void verifyIfUserIsOwner_withNonOwnerUser_ShouldThrowException() {

    Long wrongUserId = 999L;

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));

    Exception exception = assertThrows(RuntimeException.class, () -> plantService.verifyIfUserIsOwner(wrongUserId, testPlant.getPlantId()));
    assertEquals("The current user is not the owner of this plant.", exception.getMessage());
  }*/

  @Test
  public void addCaretakerToPlant_success() {
  
    User newCaretaker = new User();
    newCaretaker.setId(2L);
    newCaretaker.setEmail("newCaretaker@email.com");
    newCaretaker.setUsername("newCaretakerUsername");
    newCaretaker.setPassword("password");
    newCaretaker.setToken("token999");

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
    Mockito.when(userRepository.findById(newCaretaker.getId())).thenReturn(Optional.of(newCaretaker));

    assertDoesNotThrow(() -> plantService.addCaretakerToPlant(newCaretaker.getId(), testPlant.getPlantId()));
    assertTrue(testPlant.getCaretakers().contains(newCaretaker));
    // check that caretaker appears only once in list
    assertTrue(testPlant.getCaretakers().indexOf(newCaretaker) == testPlant.getCaretakers().lastIndexOf(newCaretaker));
    verify(plantRepository).save(testPlant);
  }

  @Test
  public void addCaretakerToPlant_userAlreadyCaretaker_ShouldThrow() {

    User newCaretaker = new User();
    newCaretaker.setId(2L);
    newCaretaker.setEmail("newCaretaker@email.com");
    newCaretaker.setUsername("newCaretakerUsername");
    newCaretaker.setPassword("password");
    newCaretaker.setToken("token999");

    testPlant.getCaretakers().add(newCaretaker);

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
    Mockito.when(userRepository.findById(newCaretaker.getId())).thenReturn(Optional.of(newCaretaker));

    Exception exception = assertThrows(RuntimeException.class, () -> plantService.addCaretakerToPlant(newCaretaker.getId(), testPlant.getPlantId()));
    assertEquals("This user with id " + newCaretaker.getId() + " is already a caretaker", exception.getMessage());
  }
  
  
  /*@Test
  public void addCaretakerToPlant_CaretakerIsOwner_ShouldThrow() {

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

    Exception exception = assertThrows(RuntimeException.class, () -> plantService.addCaretakerToPlant(testUser.getId(), testUser.getId(), testPlant.getPlantId()));
    assertEquals("Owner cannot add himself as caretaker.", exception.getMessage());
  }*/

  @Test
  public void deleteCaretakerFromPlant_success() {
  
    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
    Mockito.when(userRepository.findById(testCaretaker.getId())).thenReturn(Optional.of(testCaretaker));

    assertDoesNotThrow(() -> plantService.deleteCaretakerFromPlant(testCaretaker.getId(), testPlant.getPlantId()));
    assertFalse(testPlant.getCaretakers().contains(testCaretaker));
    verify(plantRepository).save(testPlant);
  }

  @Test
  public void deleteCaretakerFromPlant_CaretakerNotInCaretakerList_ThrowsException() {
  
    User newCaretaker = new User();
    newCaretaker.setId(2L);
    newCaretaker.setEmail("newCaretaker@email.com");
    newCaretaker.setUsername("newCaretakerUsername");
    newCaretaker.setPassword("password");
    newCaretaker.setToken("token999");

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
    Mockito.when(userRepository.findById(newCaretaker.getId())).thenReturn(Optional.of(newCaretaker));

    Exception exception = assertThrows(RuntimeException.class, () -> plantService.deleteCaretakerFromPlant(newCaretaker.getId(), testPlant.getPlantId()));
    assertEquals("Cannot delete non-existing caretaker", exception.getMessage());
  }
  
  @Test 
  public void testGetOverduePlants_NoPlantsAvailable() {
      Mockito.when(plantRepository.findAll()).thenReturn(new ArrayList<>());
      assertTrue(plantService.generateEmailMessagesForOverduePlants().isEmpty());
  }

  @Test
  public void testGetOverduePlants_AllPlantsUpToDate() {
      List<Plant> plants = Arrays.asList(testPlant, anotherTestPlant);
      
      // adjust plant watering dates to today 
      testPlant.setNextWateringDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
      anotherTestPlant.setNextWateringDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
      
      Mockito.when(plantRepository.findAll()).thenReturn(plants);
      assertTrue(plantService.generateEmailMessagesForOverduePlants().isEmpty());
  }

  @Test
  public void testGetOverduePlants_OnePlantsOverdue() {
      List<Plant> plants = Arrays.asList(testPlant, anotherTestPlant);

      // adjust plant watering dates to today 

      // one overdue 
      testPlant.setNextWateringDate(Date.from(LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      // one okay
      anotherTestPlant.setNextWateringDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

      Mockito.when(plantRepository.findAll()).thenReturn(plants);
      List<EmailMessageDTO> results = plantService.generateEmailMessagesForOverduePlants();

      // one plant gets returned
      assertEquals(1, results.size());
      // check email
      assertEquals(testPlant.getOwner().getEmail(), results.get(0).getTo().get(0).get("Email"));
      // check message
      assertEquals("Your plant " + testPlant.getPlantName() + " needs watering.", results.get(0).getTextPart());
  }

  @Test
  public void testGetOverduePlants_TwoPlantsOverdue() {
      List<Plant> plants = Arrays.asList(testPlant, anotherTestPlant);

      // adjust plant watering dates to today 

      // one overdue 
      testPlant.setNextWateringDate(Date.from(LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      // one okay
      anotherTestPlant.setNextWateringDate(Date.from(LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));

      Mockito.when(plantRepository.findAll()).thenReturn(plants);
      List<EmailMessageDTO> results = plantService.generateEmailMessagesForOverduePlants();

      // one plant gets returned
      assertEquals(1, results.size());
      // check email
      assertEquals(testPlant.getOwner().getEmail(), results.get(0).getTo().get(0).get("Email"));
      assertEquals(anotherTestPlant.getOwner().getEmail(), results.get(0).getTo().get(0).get("Email"));
      // check message
      assertEquals("Your plants " + testPlant.getPlantName() + ", " + anotherTestPlant.getPlantName() + " need watering.", results.get(0).getTextPart());
  }


  @Test
  public void testGetOverduePlants_multipleOwners() {

      User newUser = new User();
      newUser.setId(2L);
      newUser.setEmail("newUser@email.com");
      newUser.setUsername("newUserUsername");
      newUser.setPassword("password");
      newUser.setToken("token88");
      Plant oneMorePlant = new Plant();
      oneMorePlant.setPlantName("My Test Plant");
      oneMorePlant.setSpecies("One-Two tree");
      oneMorePlant.setOwner(newUser);
      oneMorePlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
      oneMorePlant.setCareInstructions("Only water at night.");
      oneMorePlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
      oneMorePlant.setWateringInterval(3);
      oneMorePlant.setNextWateringDate(Date.from(LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));

      List<Plant> plants = Arrays.asList(testPlant, anotherTestPlant, oneMorePlant);

      // adjust plant watering dates to today 

      // one overdue 
      testPlant.setNextWateringDate(Date.from(LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      // one okay
      anotherTestPlant.setNextWateringDate(Date.from(LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));

      Mockito.when(plantRepository.findAll()).thenReturn(plants);
      List<EmailMessageDTO> results = plantService.generateEmailMessagesForOverduePlants();

      // one plant gets returned
      assertEquals(2, results.size());

      // as return is not always ordered the same, we need to collect the results
      // first and just check if the expected emails/messages are contained
      List<String> expectedEmails = Arrays.asList(testPlant.getOwner().getEmail(),
        anotherTestPlant.getOwner().getEmail(), oneMorePlant.getOwner().getEmail());
      List<String> actualEmails = results.stream()
        .flatMap(dto -> dto.getTo().stream())
        .map(map -> map.get("Email"))
        .collect(Collectors.toList());

      assertTrue(actualEmails.containsAll(expectedEmails));

      List<String> expectedMessages = Arrays.asList("Your plants " + 
        testPlant.getPlantName() + ", " + anotherTestPlant.getPlantName() 
        + " need watering.", "Your plant " + oneMorePlant.getPlantName() 
        + " needs watering.");
      List<String> actualMessages = results.stream().map(EmailMessageDTO::getTextPart)
        .collect(Collectors.toList());

      assertTrue(actualMessages.containsAll(expectedMessages));
  }

  @Test
  public void assignPlantToSpace_success() {

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    plantService.assignPlantToSpace(testPlant.getPlantId(), testSpace.getSpaceId());

    verify(plantRepository).save(testPlant);
    assertEquals(testSpace, testPlant.getSpace());
    assertTrue(testSpace.getPlantsContained().contains(testPlant));
  }

  @Test
  public void removePlantFromSpace_success() {

    testPlant.setSpace(testSpace);

    // check if correctly assigned
    assertTrue(testPlant.getSpace() == testSpace);

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    plantService.removePlantFromSpace(testPlant.getPlantId(), testSpace.getSpaceId());

    verify(plantRepository).save(testPlant);
    assertEquals(null, testPlant.getSpace());
    assertFalse(testSpace.getPlantsContained().contains(testPlant));
  }

  @Test
  public void removePlantFromWrongSpace_ShouldThrow() {

    Space wrongSpace = new Space();
    wrongSpace.setSpaceId(999L);
    wrongSpace.setSpaceName("Wrong Space");
    wrongSpace.setSpaceOwner(testUser);
    wrongSpace.setPlantsContained(new ArrayList<>(Arrays.asList(anotherTestPlant)));

    testPlant.setSpace(testSpace);
    testSpace.setPlantsContained(new ArrayList<>(Arrays.asList(testPlant)));

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));
    Mockito.when(spaceRepository.findById(wrongSpace.getSpaceId())).thenReturn(Optional.of(wrongSpace));


    Exception exception = assertThrows(RuntimeException.class, () -> {
      plantService.removePlantFromSpace(testPlant.getPlantId(), wrongSpace.getSpaceId());
    });

    assertTrue(exception.getMessage().contains("Cannot remove plant with plantId " + testPlant.getPlantId() +
            " from space with spaceId " + wrongSpace.getSpaceId() + " as it's not assigned to it"));
  }

  @Test
  public void removePlantFromNoSpace_ShouldThrow() {

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));


    Exception exception = assertThrows(RuntimeException.class, () -> {
      plantService.removePlantFromSpace(testPlant.getPlantId(), testSpace.getSpaceId());
    });

    assertTrue(exception.getMessage().contains("Plant with plantId " + testPlant.getPlantId() + " has no space assigned. Thus it cannot be removed from a space."));
  }

  @Test
  public void assignePlantToSpace_alreadyAssigned_ShouldThrow() {

    Space anotherSpace = new Space();
    anotherSpace.setSpaceId(999L);
    anotherSpace.setSpaceName("Another Space");
    anotherSpace.setSpaceOwner(testUser);
    anotherSpace.setPlantsContained(new ArrayList<>(Arrays.asList(testPlant)));

    testPlant.setSpace(anotherSpace);
    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));


    Exception exception = assertThrows(RuntimeException.class, () -> {
      plantService.assignPlantToSpace(testPlant.getPlantId(), testSpace.getSpaceId());
    });

    assertTrue(exception.getMessage().contains("Plant with plantId " + testPlant.getPlantId() + " is already assigned to a space. Remove it from there before assining a new space"));
  }

}
