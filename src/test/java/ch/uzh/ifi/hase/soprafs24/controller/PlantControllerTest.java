package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CaretakerPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.EmailMessageDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpaceAssignmentPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GCPStorageService;
import ch.uzh.ifi.hase.soprafs24.service.PlantService;
import ch.uzh.ifi.hase.soprafs24.service.SpaceService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.checkerframework.checker.units.qual.t;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.util.*;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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
  private SpaceService spaceService;

  @MockBean
  private GCPStorageService gcpStorageService;

  @MockBean
  private DTOMapper dtoMapper;

  private static List<Plant> plants;
  private static Plant testPlant;
  private static Plant anotherTestPlant;
  private static User testUser;
  private static User testCaretaker;
  private static Space hallway;



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
    testPlant.setPlantName("Test Plant");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(testUser);
    testPlant.setCaretakers(Collections.singletonList(testCaretaker));
    testPlant.setCareInstructions("Only water at night.");
    testPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    testPlant.setWateringInterval(3);
    testPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    testPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");

    anotherTestPlant = new Plant();
    anotherTestPlant.setPlantName("Another Test Plant");
    anotherTestPlant.setSpecies("One-Two tree");
    anotherTestPlant.setOwner(testUser);
    anotherTestPlant.setCaretakers(Collections.singletonList(testCaretaker));
    anotherTestPlant.setCareInstructions("Only water at night.");
    anotherTestPlant.setLastWateringDate(new Date(10, Calendar.NOVEMBER, 10));
    anotherTestPlant.setWateringInterval(3);
    anotherTestPlant.setNextWateringDate(new Date(10, Calendar.NOVEMBER, 13));
    anotherTestPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");

    hallway = new Space();
    hallway.setSpaceName("hallway");
    hallway.setSpaceOwner(testUser);

    plants = Arrays.asList(
        testPlant,
        anotherTestPlant
    );
  }

  @BeforeEach
  public void setupEach() {
    reset(plantService, dtoMapper);

    // always return testPlant and anotherTestPlant for the testUsers id.
    Mockito.when(plantService.getOwnedPlantsByUserId(eq(testUser.getId()))).thenReturn(plants);
    // always return testPlant and anotherTestPlant as caretaker attribute
    Mockito.when(plantService.getCaretakerPlantsByUserId(eq(testCaretaker.getId()))).thenReturn(plants);
    // extract info from invoked plant and return new dto from that plant.
    Mockito.when(dtoMapper.convertEntityToPlantGetDTO(Mockito.any(Plant.class))).thenAnswer(invocation -> {
        Plant plant = invocation.getArgument(0);
        PlantGetDTO dto = new PlantGetDTO();
        dto.setPlantId(plant.getPlantId());
      dto.setPlantName(plant.getPlantName());
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

  /**
   * get all plants, 200
   */
  @Test
  public void getAllPlants_success() throws Exception {
    // always return testPlant and anotherTestPlant for all plants.
    Mockito.when(plantService.getPlants()).thenReturn(plants);

    MockHttpServletRequestBuilder getRequest = get("/plants")
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].plantName", is(plants.get(0).getPlantName())))
            .andExpect(jsonPath("$[1].plantName", is(plants.get(1).getPlantName())));
  }

  /**
   * get one plant, 200
   */
  @Test
  public void givenPlantId_whenGetPlant_thenReturnJson() throws Exception {
    Mockito.when(plantService.getPlantById(1L)).thenReturn(testPlant);

    MockHttpServletRequestBuilder getRequest = get("/plants/1")
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.plantName", is(testPlant.getPlantName())));
  }

  /**
   * get non-existing plant, 404
   */
  @Test
  public void givenNonExistingPlantId_whenGetPlant_exceptionThrown() throws Exception {
    MockHttpServletRequestBuilder getRequest = get("/plants/1234")
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isNotFound());
  }

  /**
   * post new plant, 201
   */
  @Test
  public void createPlant_validInput_plantCreated() throws Exception {
    given(plantService.createPlant(Mockito.any())).willReturn(testPlant);

    PlantPostDTO plantPostDTO = new PlantPostDTO();
    plantPostDTO.setPlantName(testPlant.getPlantName());

    MockHttpServletRequestBuilder postRequest = post("/plants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(plantPostDTO));

    mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.plantName", is(testPlant.getPlantName())));
  }

  /**
   * post new plant, 201
   */
  @Test
  public void createPlant_withinSpace_plantCreated() throws Exception {
    given(plantService.createPlant(Mockito.any())).willReturn(testPlant);
    doNothing().when(spaceService).addPlantToSpace(Mockito.any(), Mockito.any());

    PlantPostDTO plantPostDTO = new PlantPostDTO();
    plantPostDTO.setPlantName(testPlant.getPlantName());
    Long spaceId = 55L;

    MockHttpServletRequestBuilder postRequest = post("/plants")
            .param("spaceId", spaceId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(plantPostDTO));

    mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.plantName", is(testPlant.getPlantName())));

    verify(spaceService).addPlantToSpace(testPlant.getPlantId(), spaceId);
  }

  /**
   * update existing plant, 204
   */
  @Test
  public void updatePlant_validInput_plantUpdated() throws Exception {
    PlantPutDTO plantPutDTO = new PlantPutDTO();
    plantPutDTO.setPlantName(testPlant.getPlantName());

    given(plantService.updatePlant(Mockito.any())).willReturn(testPlant);
    given(plantService.getPlantById(Mockito.any())).willReturn(testPlant);

    MockHttpServletRequestBuilder putRequest = put("/plants/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(plantPutDTO));

    mockMvc.perform(putRequest).andExpect(status().isNoContent());
  }

  /**
   * update non-existing plant, 404
   */
  @Test
  public void updatePlant_nonExistantPlant_exceptionThrown() throws Exception {
    PlantPutDTO plantPutDTO = new PlantPutDTO();
    plantPutDTO.setPlantName(testPlant.getPlantName());

    given(plantService.updatePlant(Mockito.any())).willReturn(testPlant);

    MockHttpServletRequestBuilder putRequest = put("/plants/599")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(plantPutDTO));

    mockMvc.perform(putRequest).andExpect(status().isNotFound());
  }

  /**
   * delete existing plant, 204
   */
  @Test
  public void deletePlant_existingPlant_userDeleted() throws Exception {
    given(plantService.getPlantById(Mockito.any())).willReturn(testPlant);

    MockHttpServletRequestBuilder deleteRequest =
            delete("/plants/588");

    mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
  }

  /**
   * delete non-existing plant, 404
   */
  @Test
  public void deletePlant_nonExistingPlant_exceptionThrown() throws Exception {
    MockHttpServletRequestBuilder deleteRequest =
            delete("/plants/588");
    mockMvc.perform(deleteRequest).andExpect(status().isNotFound());
  }


  @Test
  public void addCaretakerToPlant_success() throws Exception {
    Long userId = 10L;
    Long plantId = 50L;
    CaretakerPostDTO caretakerPostDTO = new CaretakerPostDTO();
    caretakerPostDTO.setCaretakerId(userId);

    doNothing().when(plantService).addCaretakerToPlant(userId, plantId);

    mockMvc.perform(post("/plants/{plantId}/caretakers", plantId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(caretakerPostDTO)))
                .andExpect(status().isNoContent());

    verify(plantService).addCaretakerToPlant(userId, plantId);
  }

  @Test
  public void deleteCaretakerFromPlant_success() throws Exception {
    Long userId = 10L;
    Long plantId = 50L;

    doNothing().when(plantService).addCaretakerToPlant(userId, plantId);

    mockMvc.perform(delete("/plants/{plantId}/caretakers/{caretakerId}", plantId, userId))
                .andExpect(status().isNoContent());

    verify(plantService).deleteCaretakerFromPlant(userId, plantId);

  }


  @Test
  public void getAllOwnedPlants_success() throws Exception {
    // given 
    Long userId = testUser.getId();
    
    mockMvc.perform(get("/plants/owned?ownerId=" + userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].plantName", is("Test Plant")))
            .andExpect(jsonPath("$[1].plantName", is("Another Test Plant")));

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
            .andExpect(jsonPath("$[0].plantName", is("Test Plant")))
            .andExpect(jsonPath("$[1].plantName", is("Another Test Plant")));

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


  @Test
  public void assignPlantToSpace() throws Exception {
    Long plantId = 10L;
    Long spaceId = 50L;
    SpaceAssignmentPostDTO dto = new SpaceAssignmentPostDTO();
    dto.setSpaceId(spaceId);

    // Setup the mock behavior
    doNothing().when(plantService).assignPlantToSpace(plantId, spaceId);

    mockMvc.perform(post("/plants/{plantId}/space", plantId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(dto)))
            .andExpect(status().isOk());

    verify(plantService).assignPlantToSpace(plantId, spaceId);
  }

  @Test
  public void testRemovePlantFromSpace() throws Exception {
    Long plantId = 10L;
    Long spaceId = 50L;

    // Setup the mock behavior
    doNothing().when(plantService).removePlantFromSpace(plantId, spaceId);

    // Perform the request and check assertions
    mockMvc.perform(delete("/plants/{plantId}/space/{spaceId}", plantId, spaceId))
            .andExpect(status().isOk());

    // Verify that the service method was called correctly
    verify(plantService).removePlantFromSpace(plantId, spaceId);

  }

  /**
   * post new plantImage, 201
   */
  @Test
  public void postImage_callGCPStorageService_newUrlReturned() throws Exception {
    given(plantService.getPlantById(Mockito.any())).willReturn(testPlant);
    given(gcpStorageService.uploadImage(Mockito.any())).willReturn(testPlant.getPlantImageUrl());

    FileInputStream imageStream = new FileInputStream("src/test/test-resources/mock-image.jpg");
    MockMultipartFile mockImage = new MockMultipartFile(
            "image",
            "mock-image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageStream
    );

    mockMvc.perform(
                    multipart("/plants/123/image").file(mockImage))
            .andExpect(status().isOk());

    verify(gcpStorageService, times(1)).uploadImage(Mockito.any());
  }

  /**
   * post new plantImage for non-existing plant, 400
   */
  @Test
  public void postImage_wrongMultipartFileName_exceptionThrown() throws Exception {
    given(plantService.getPlantById(Mockito.any())).willReturn(testPlant);
    given(gcpStorageService.uploadImage(Mockito.any())).willReturn(testPlant.getPlantImageUrl());

    FileInputStream imageStream = new FileInputStream("src/test/test-resources/mock-image.jpg");
    MockMultipartFile mockImage = new MockMultipartFile(
            "problematic-name-not-image",
            "mock-image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageStream
    );

    mockMvc.perform(multipart("/plants/123/image").file(mockImage))
            .andExpect(status().isBadRequest());
  }
@Test
public void updateWatering_Success() throws Exception {
  Long plantId = 11L;
  given(plantService.getPlantById(Mockito.any())).willReturn(testPlant);

  MockHttpServletRequestBuilder putRequest = put("/plants/{plantId}/water", plantId);

    mockMvc.perform(putRequest)
            .andExpect(status().isNoContent());
  
  verify(plantService, times(1)).getPlantById(plantId);
  verify(plantService, times(1)).waterPlant(testPlant);
}

@Test
public void updateWatering_NotFound() throws Exception {
    Long plantId = 1L;
    when(plantService.getPlantById(Mockito.any())).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.put("/plants/{plantId}/water", plantId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

    verify(plantService, times(1)).getPlantById(plantId);
    verifyNoMoreInteractions(plantService);
}

@Test
public void updateCaring_Success() throws Exception {
  Long plantId = 11L;
  given(plantService.getPlantById(Mockito.any())).willReturn(testPlant);

  MockHttpServletRequestBuilder putRequest = put("/plants/{plantId}/care", plantId);

    mockMvc.perform(putRequest)
            .andExpect(status().isNoContent());
  
  verify(plantService, times(1)).getPlantById(plantId);
  verify(plantService, times(1)).careForPlant(testPlant);
}

@Test
public void updateCaring_NotFound() throws Exception {
    Long plantId = 2L;
    when(plantService.getPlantById(Mockito.any())).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.put("/plants/{plantId}/care", plantId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

    verify(plantService, times(1)).getPlantById(plantId);
    verifyNoMoreInteractions(plantService);
}

 @Test
    public void testCheckAllWatering() throws Exception {
        // Setup mock behavior
        EmailMessageDTO dto = new EmailMessageDTO();
        dto.setToEmail("example@domain.com");
        dto.setTextPart("Water your plant!");

        List<EmailMessageDTO> mockMessages = Arrays.asList(dto);
        when(plantService.generateEmailMessagesForOverduePlants()).thenReturn(mockMessages);
        when(plantService.callMailJet(anyString())).thenReturn("Success");

        // Perform the request
        mockMvc.perform(post("/checkAllWatering")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.SandboxMode").value(false))
                .andExpect(jsonPath("$.Messages[0].To[0].Email").value("example@domain.com"))
                .andExpect(jsonPath("$.MailJetResponse").value("Success"));

        // Verify interactions
        verify(plantService, times(1)).generateEmailMessagesForOverduePlants();
        verify(plantService, times(1)).callMailJet(anyString());
    }
  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   *
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    }
    catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              String.format("The request body could not be created.%s", e));
    }
  }
}
