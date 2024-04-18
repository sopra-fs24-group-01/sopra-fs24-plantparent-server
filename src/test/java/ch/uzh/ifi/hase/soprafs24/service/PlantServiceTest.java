package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class PlantServiceTest {

  @Mock
  private PlantRepository plantRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private PlantService plantService;

  private Plant testPlant;
  private Plant anotherTestPlant;
  private User testUser;
  private User testCaretaker;

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
    testPlant.setCaretakers(Collections.singletonList(testCaretaker));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    //testPlant.calculateAndSetNextWateringDate();

    anotherTestPlant = new Plant();
    anotherTestPlant.setPlantName("Another Test Plant");
    anotherTestPlant.setSpecies("One-Two tree");
    anotherTestPlant.setOwner(testUser);
    anotherTestPlant.setCaretakers(Collections.singletonList(testCaretaker));
    anotherTestPlant.setCareInstructions("Only water at night.");
    anotherTestPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    anotherTestPlant.setWateringInterval(3);
    anotherTestPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    //anotherTestPlant.calculateAndSetNextWateringDate();

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
  }

}
