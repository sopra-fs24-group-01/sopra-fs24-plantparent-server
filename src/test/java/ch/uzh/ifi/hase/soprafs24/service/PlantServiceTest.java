package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlantServiceTest {

  @Mock
  private PlantRepository plantRepository;

  @InjectMocks
  private PlantService plantService;

  private Plant testPlant;
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
    testPlant.setPlantId((1L));
    testPlant.setName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(testUser);
    testPlant.setCaretakers(Collections.singletonList(testCaretaker));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    //testPlant.calculateAndSetNextWateringDate();

    Mockito.when(plantRepository.save(Mockito.any())).thenReturn(testPlant);
  }

  @Test
  public void createPlant_validInputs_success() {
    // when
    Plant createdPlant = plantService.createPlant(testPlant);

    //then
    Mockito.verify(plantRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testPlant.getPlantId(), createdPlant.getPlantId());
    assertEquals(testPlant.getName(), createdPlant.getName());
    assertEquals(testPlant.getNextWateringDate(), createdPlant.getNextWateringDate());
  }

  @Test
  public void getPlantById_PlantExists_success() {
    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));

    Plant result = plantService.getPlantById(testPlant.getPlantId());

    assertNotNull(result);
    assertEquals(testPlant.getName(), result.getName());
  }

  @Test
  public void getPlantById_PlantDoesNotExist_ReturnNull() {
    Mockito.when(plantRepository.findByPlantId(99L)).thenReturn(null);

    assertNull(plantService.getPlantById(99L));
  }

  public void savePlant_NoOwnerSet_ThrowsExcpetion() {

    Plant incompletePlant = new Plant();
    incompletePlant.setPlantId(33L);
    incompletePlant.setName("incompletePlant");

    assertThrows(RuntimeException.class, () -> {
      plantRepository.saveAndFlush(incompletePlant);
    });
  }

}
