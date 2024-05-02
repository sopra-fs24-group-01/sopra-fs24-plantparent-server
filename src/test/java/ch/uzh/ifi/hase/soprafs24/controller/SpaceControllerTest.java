package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.SpaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpaceController.class)
public class SpaceControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SpaceService spaceService;

  @MockBean
  private DTOMapper dtoMapper;


  private static Space hallway;
  private static Space garden;
  private static List<Space> spaces;
  private static Plant testPlant;
  private static Plant anotherTestPlant;
  private static List<Plant> plants;
  private static User testUser;


  @BeforeAll
  public static void setupAll() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setEmail("spaceOwner@email.com");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setToken("token");

    testPlant = new Plant();
    testPlant.setPlantName("Tree One");
    testPlant.setSpecies("One-Two tree");
    testPlant.setOwner(testUser);

    plants = Arrays.asList(
            testPlant,
            anotherTestPlant
    );

    anotherTestPlant = new Plant();
    anotherTestPlant.setPlantName("Tree Two");
    anotherTestPlant.setSpecies("One-Two tree");
    anotherTestPlant.setOwner(testUser);

    hallway = new Space();
    hallway.setSpaceName("hallway");
    hallway.setSpaceOwner(testUser);
    hallway.setPlantsContained(Collections.singletonList(testPlant));

    garden = new Space();
    garden.setSpaceName("garden");
    garden.setSpaceOwner(testUser);
    garden.setPlantsContained(plants);

    spaces = Arrays.asList(
            hallway,
            garden
    );

  }

  @BeforeEach
  public void setupEach() {
    reset(spaceService, dtoMapper);

    Mockito.when(dtoMapper.convertEntityToSpaceGetDTO(Mockito.any(Space.class))).thenAnswer(invocation -> {
      Space space = invocation.getArgument(0);
      SpaceGetDTO dto = new SpaceGetDTO();
      dto.setSpaceId(space.getSpaceId());
      dto.setSpaceName(space.getSpaceName());
      dto.setPlantsContained(space.getPlantsContained());
      return dto;
    });
  }

  /**
   * get all spaces, 200
   */
  @Test
  public void getAllSpaces_success() throws Exception {
    Mockito.when(spaceService.getSpaces()).thenReturn(spaces);

    MockHttpServletRequestBuilder getRequest = get("/spaces")
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].spaceName", is(spaces.get(0).getSpaceName())))
            .andExpect(jsonPath("$[1].spaceName", is(spaces.get(1).getSpaceName())));
  }

  /**
   * get one space, 200
   */
  @Test
  public void givenSpaceId_whenGetSpace_thenReturnJson() throws Exception {
    Mockito.when(spaceService.getSpaceById(1L)).thenReturn(hallway);

    MockHttpServletRequestBuilder getRequest = get("/spaces/1")
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.spaceName", is(hallway.getSpaceName())));
  }

  /**
   * post new space, 201
   */
  @Test
  public void createSpace_validInput_spaceCreated() throws Exception {
    given(spaceService.createSpace(Mockito.any())).willReturn(hallway);

    SpacePostDTO spacePostDTO = new SpacePostDTO();
    spacePostDTO.setSpaceName(hallway.getSpaceName());

    MockHttpServletRequestBuilder postRequest = post("/spaces")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(spacePostDTO));

    mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.spaceName", is(hallway.getSpaceName())));
  }

  /**
   * update existing space, 204
   */
  @Test
  public void updateSpace_validInput_spaceUpdated() throws Exception {
    SpacePutDTO spacePutDTO = new SpacePutDTO();
    spacePutDTO.setSpaceName(hallway.getSpaceName());

    given(spaceService.updateSpace(Mockito.any())).willReturn(hallway);
    given(spaceService.getSpaceById(Mockito.any())).willReturn(hallway);

    MockHttpServletRequestBuilder putRequest = put("/spaces/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(spacePutDTO));

    mockMvc.perform(putRequest).andExpect(status().isNoContent());
  }

  /**
   * delete existing space, 204
   */
  @Test
  public void deleteSpace_existingSpace_spaceDeleted() throws Exception {
    given(spaceService.getSpaceById(Mockito.any())).willReturn(hallway);

    MockHttpServletRequestBuilder deleteRequest =
            delete("/spaces/987");

    mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
  }


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