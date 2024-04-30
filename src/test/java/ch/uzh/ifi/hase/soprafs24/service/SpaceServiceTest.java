package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

public class SpaceServiceTest {

  @Mock
  private SpaceRepository spaceRepository;

  @Mock 
  private UserRepository userRepository;

  @Mock 
  private PlantRepository plantRepository;

  @InjectMocks
  private SpaceService spaceService;

  private Space testSpace;
  private User testUser;
  private Plant testPlant;
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
    testPlant.setCaretakers(new ArrayList<>(Arrays.asList(testCaretaker)));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));

    testSpace = new Space();
    testSpace.setSpaceName("Test Space");
    testSpace.setSpaceOwner(testUser);
    testSpace.setPlantsContained(new ArrayList<>(Arrays.asList(testPlant)));
    
    Mockito.when(spaceRepository.save(Mockito.any())).thenReturn(testSpace);
  }
    
  @Test 
  public void createSpace_validInputs_success() {
    // when 
    Space createdSpace = spaceService.createSpace(testSpace);

    // then 
    Mockito.verify(spaceRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testSpace.getSpaceId(), createdSpace.getSpaceId());
    assertEquals(testSpace.getSpaceName(), createdSpace.getSpaceName());
    assertEquals(testSpace.getPlantsContained(), createdSpace.getPlantsContained());
    assertEquals(testSpace.getSpaceOwner(), createdSpace.getSpaceOwner());
  }

  @Test
  public void getSpaceById_SpaceExists_success() {
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    Space result = spaceService.getSpaceById(testSpace.getSpaceId());

    assertNotNull(result);
    assertEquals(testSpace.getSpaceName(), result.getSpaceName());
  }

  @Test
  public void getSpaceById_SpaceDoesNotExist_ReturnNull() {
    Mockito.when(spaceRepository.findBySpaceId(99L)).thenReturn(null);

    assertNull(spaceService.getSpaceById(99L));
  }

  @Test
  public void saveSpace_NoOwnerSet_ThrowsException() {
    Space someSpace = new Space();
    someSpace.setSpaceName("incompleteSpace");

    assertThrows(RuntimeException.class, () -> spaceService.createSpace(someSpace));
  }

}
