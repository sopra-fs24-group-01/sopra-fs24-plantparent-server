package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.PlantService;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * PlantControllerTest
 * This is a WebMvcTest which allows to test the PlantController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the PlantController works.
 */

@WebMvcTest(PlantController.class)
public class PlantControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PlantService plantService;

  @MockBean
  private DTOMapper dtoMapper;

  private static List<Plant> plants;
  private static Plant testPlant;
  private static Plant anotherTestPlant;
  private static User testUser;
  private static User testCaretaker;

  @BeforeAll
  public static void setupAll() {
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
    testPlant.setName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(testUser);
    testPlant.setCaretakers(Collections.singletonList(testCaretaker));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));

    anotherTestPlant = new Plant();
    anotherTestPlant.setName("Another Test Plant");
    anotherTestPlant.setSpecies("One-Two tree");
    anotherTestPlant.setOwner(testUser);
    anotherTestPlant.setCaretakers(Collections.singletonList(testCaretaker));
    anotherTestPlant.setCareInstructions("Only water at night.");
    anotherTestPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    anotherTestPlant.setWateringInterval(3);
    anotherTestPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
  
    plants = Arrays.asList(
        testPlant,
        anotherTestPlant
    );
  }

  @BeforeEach
  public void setupEach() {
    reset(plantService, dtoMapper);

    Mockito.when(plantService.getOwnedPlantsByUserId(eq(testUser.getId()))).thenReturn(plants);
    Mockito.when(plantService.getCaretakerPlantsByUserId(eq(testCaretaker.getId()))).thenReturn(plants);
    Mockito.when(dtoMapper.convertEntityToPlantGetDTO(Mockito.any(Plant.class))).thenAnswer(invocation -> {
        Plant plant = invocation.getArgument(0);
        PlantGetDTO dto = new PlantGetDTO();
        dto.setPlantId(plant.getPlantId());
        dto.setName(plant.getName());
        dto.setSpecies(plant.getSpecies());
        dto.setCareInstructions(plant.getCareInstructions());
        dto.setLastWateringDate(plant.getLastWateringDate());
        dto.setNextWateringDate(plant.getNextWateringDate());
        dto.setWateringInterval(plant.getWateringInterval());
        dto.setOwner(plant.getOwner());
        dto.setCaretakers(plant.getCaretakers());
        return dto;
    });
  }

  @Test
  public void getAllOwnedPlants_success() throws Exception {
    // given 
    Long userId = testUser.getId();
    
    mockMvc.perform(get("/plants/owned?ownerId=" + userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("Test Plant")))
            .andExpect(jsonPath("$[1].name", is("Another Test Plant")));

    verify(plantService).getOwnedPlantsByUserId(userId);
  }

  @Test
  public void getAllOwnedPlants_UserNotFound_ShouldReturn404() throws Exception {
    Mockito.when(plantService.getOwnedPlantsByUserId(testUser.getId()))
        .thenThrow(new UserNotFoundException("User with userId " + testUser.getId() + " not found"));

    mockMvc.perform(get("/plants/owned?ownerId=" + testUser.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
            .andExpect(result -> assertEquals("User with userId " + testUser.getId() + " not found", 
                                            result.getResolvedException().getMessage()));
  }

  @Test
  public void getAllCaredForPlants_success() throws Exception {
    // given 
    Long userId = testCaretaker.getId();
    
    mockMvc.perform(get("/plants/caredFor?careTakerId=" + userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("Test Plant")))
            .andExpect(jsonPath("$[1].name", is("Another Test Plant")));

    verify(plantService).getCaretakerPlantsByUserId(userId);
  }

  @Test
  public void getAllCaredForPlants_UserNotFound_ShouldReturn404() throws Exception {
    Mockito.when(plantService.getCaretakerPlantsByUserId(testUser.getId()))
        .thenThrow(new UserNotFoundException("User with userId " + testUser.getId() + " not found"));

    mockMvc.perform(get("/plants/caredFor?careTakerId=" + testUser.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
            .andExpect(result -> assertEquals("User with userId " + testUser.getId() + " not found", 
                                            result.getResolvedException().getMessage()));
  }
}
