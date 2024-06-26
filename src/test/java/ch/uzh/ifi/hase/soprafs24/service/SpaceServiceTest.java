package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.cloud.storage.Option;

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
  private User testMember;

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

    testMember = new User();
    testMember.setId(22L);
    testMember.setEmail("testMember@email.com");
    testMember.setUsername("testMember");
    testMember.setPassword("pword");
    testMember.setToken("token3");

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
    testSpace.setSpaceMembers(new ArrayList<>());
    
    Mockito.when(spaceRepository.save(Mockito.any())).thenReturn(testSpace);
  }
    
  @Test 
  public void createSpace_validInputs_success() {
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
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

  @Test
  public void getContainedPlantsBySpaceId_onePlant_success() {
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    List<Plant> containedPlants = spaceService.getContainedPlantsBySpaceId(testSpace.getSpaceId());

    assertTrue(containedPlants.contains(testPlant));
  }

  @Test
  public void getContainedPlantsBySpaceId_noPlant_success() {
    testSpace.setPlantsContained(new ArrayList<>(Arrays.asList()));

    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    List<Plant> containedPlants = spaceService.getContainedPlantsBySpaceId(testSpace.getSpaceId());

    assertTrue(containedPlants.isEmpty());
  }

  @Test
  public void addMemberToSpace_success() {

    Mockito.when(userRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    spaceService.addMemberToSpace(testMember.getId(), testSpace.getSpaceId());

    assertTrue(testSpace.getSpaceMembers().contains(testMember));
    assertTrue(testMember.getSpaceMemberships().contains(testSpace));
    assertTrue(testPlant.getCaretakers().contains(testMember));

    verify(spaceRepository).save(testSpace);
    verify(userRepository).save(testMember);
  }

  @Test
  public void addMemberToSpace_userAlreadyMember_ThrowsException() {
    testSpace.getSpaceMembers().add(testMember);
    Mockito.when(userRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    assertThrows(RuntimeException.class, () -> spaceService.addMemberToSpace(testMember.getId(), testSpace.getSpaceId()));
  }

  @Test
  public void addMemberToSpace_userIsOwner_ThrowsException() {
  
    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    assertThrows(RuntimeException.class, () -> spaceService.addMemberToSpace(testUser.getId(), testSpace.getSpaceId()));
  }

  @Test
  public void deleteMemberFromSpace() {

    // setup testMemeber as member in testSpace
    testSpace.getSpaceMembers().add(testMember);
    testMember.getSpaceMemberships().add(testSpace);
    testPlant.getCaretakers().add(testMember);
    testMember.getPlantsCaredFor().add(testPlant);


    Mockito.when(userRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    spaceService.deleteMemberFromSpace(testMember.getId(), testSpace.getSpaceId());

    assertFalse(testSpace.getSpaceMembers().contains(testMember));
    assertFalse(testMember.getSpaceMemberships().contains(testSpace));
    assertFalse(testPlant.getCaretakers().contains(testMember));
    assertFalse(testMember.getPlantsCaredFor().contains(testPlant));

    verify(spaceRepository).save(testSpace);
    verify(userRepository).save(testMember);
  }

  @Test
  public void deleteMemberToSpace_UserNotAMember_ThrowsException() {

    Mockito.when(userRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    assertThrows(RuntimeException.class, () -> spaceService.deleteMemberFromSpace(testMember.getId(), testSpace.getSpaceId()));
  }

  @Test 
  public void getOwnedSpacesByUserId_succes() {
    testUser.getSpacesOwned().add(testSpace);

    Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

    List<Space> spaces = spaceService.getOwnedSpacesByUserId(testUser.getId());

    assertNotNull(spaces);
    assertEquals(1, spaces.size());
    assertEquals("Test Space", spaces.get(0).getSpaceName());
    verify(userRepository).findById(testUser.getId());
  }

  @Test
  public void getAllMembershipSpacesById_success() {
    testCaretaker.getSpaceMemberships().add(testSpace);
    
    Mockito.when(userRepository.findById(testCaretaker.getId())).thenReturn(Optional.of(testCaretaker));

    List<Space> spaces = spaceService.getMembershipSpacesByUserId(testCaretaker.getId());

    assertNotNull(spaces);
    assertEquals(1, spaces.size());
    assertEquals("Test Space", spaces.get(0).getSpaceName());
    verify(userRepository).findById(testCaretaker.getId());
  }

  @Test
  public void addPlantToSpace_success() {

    Plant newPlant = new Plant();
    newPlant.setOwner(testUser);
    newPlant.setPlantName("newPlant");

    Mockito.when(plantRepository.findById(newPlant.getPlantId())).thenReturn(Optional.of(newPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    spaceService.addPlantToSpace(newPlant.getPlantId(), testSpace.getSpaceId());

    verify(plantRepository).save(newPlant);
    assertEquals(testSpace, newPlant.getSpace());
    assertTrue(testSpace.getPlantsContained().contains(newPlant));
  }

  @Test
  public void addPlantToSpace_spaceOwnerAddsPlant_assignsCaretakersCorrectly() {
    // set up a member in testspace
    testSpace.getSpaceMembers().add(testCaretaker);
    testCaretaker.getSpaceMemberships().add(testSpace);

    Plant newPlant = new Plant();
    newPlant.setOwner(testUser);
    newPlant.setPlantName("newPlant");

    assertFalse(testCaretaker.getPlantsCaredFor().contains(newPlant));

    Mockito.when(plantRepository.findById(newPlant.getPlantId())).thenReturn(Optional.of(newPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    spaceService.addPlantToSpace(newPlant.getPlantId(), testSpace.getSpaceId());

    verify(plantRepository).save(newPlant);
    assertEquals(testSpace, newPlant.getSpace());
    assertTrue(testSpace.getPlantsContained().contains(newPlant));
    assertTrue(newPlant.getCaretakers().contains(testCaretaker));
    assertFalse(newPlant.getCaretakers().contains(testSpace.getSpaceOwner()));
    assertFalse(testSpace.getSpaceOwner().getPlantsCaredFor().contains(newPlant));
    assertTrue(testCaretaker.getPlantsCaredFor().contains(newPlant));
  }


  @Test
  public void addPlantToSpace_MemberAddsPlant_assignsCaretakersCorrectly() {
    // set up a member in testspace
    testSpace.getSpaceMembers().add(testCaretaker);
    testCaretaker.getSpaceMemberships().add(testSpace);

    User member2 = new User();
    member2.setId(22L);
    member2.setUsername("member2");
    testSpace.getSpaceMembers().add(member2);
    member2.getSpaceMemberships().add(testSpace);

    Plant newPlant = new Plant();
    newPlant.setOwner(testCaretaker);
    newPlant.setPlantName("newPlant");

    assertFalse(testCaretaker.getPlantsCaredFor().contains(newPlant));
    assertFalse(member2.getPlantsCaredFor().contains(newPlant));
    assertFalse(testUser.getPlantsCaredFor().contains(newPlant));

    Mockito.when(plantRepository.findById(newPlant.getPlantId())).thenReturn(Optional.of(newPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    spaceService.addPlantToSpace(newPlant.getPlantId(), testSpace.getSpaceId());

    verify(plantRepository).save(newPlant);
    assertEquals(testSpace, newPlant.getSpace());
    assertTrue(testSpace.getPlantsContained().contains(newPlant));

    // member gets added as caretaker
    assertTrue(newPlant.getCaretakers().contains(member2));
    assertTrue(member2.getPlantsCaredFor().contains(newPlant));
    // spaceOwner gets added as caretaker
    assertTrue(newPlant.getCaretakers().contains(testSpace.getSpaceOwner()));
    assertTrue(testUser.getPlantsCaredFor().contains(newPlant));
    // owner of plant is not added as caretaker
    assertFalse(testCaretaker.getPlantsCaredFor().contains(newPlant));
    assertFalse(newPlant.getCaretakers().contains(testCaretaker));
  }

  @Test
  public void deletePlantFromSpace_success() {

    testPlant.setSpace(testSpace);

    // check if correctly assigned
    assertTrue(testPlant.getSpace() == testSpace);

    Mockito.when(plantRepository.findById(testPlant.getPlantId())).thenReturn(Optional.of(testPlant));
    Mockito.when(spaceRepository.findById(testSpace.getSpaceId())).thenReturn(Optional.of(testSpace));

    spaceService.deletePlantFromSpace(testPlant.getPlantId(), testSpace.getSpaceId());

    verify(plantRepository).save(testPlant);
    assertEquals(null, testPlant.getSpace());
    assertFalse(testSpace.getPlantsContained().contains(testPlant));
  }
  
}
