package ch.uzh.ifi.hase.soprafs24.controller;


import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CaretakerPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpaceAssignmentPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.EmailMessageDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GCPStorageService;
import ch.uzh.ifi.hase.soprafs24.service.PlantService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
public class PlantController {
  private final PlantService plantService;
  private final GCPStorageService gcpStorageService;

  PlantController(PlantService plantService, GCPStorageService gcpStorageService) {
    this.plantService = plantService;
    this.gcpStorageService = gcpStorageService;
  }

  @GetMapping("/plants")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<PlantGetDTO> getAllPlants() {
    List<Plant> plants = plantService.getPlants();
    List<PlantGetDTO> plantGetDTOs = new ArrayList<>();

    for (Plant plant : plants) {
      plantGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPlantGetDTO(plant));
    }
    return plantGetDTOs;
  }

  @GetMapping("/plants/{plantId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public PlantGetDTO getPlant(@PathVariable Long plantId) {
    Plant plant = plantService.getPlantById(plantId);

    if (plant == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested plant with id %s does not exist.", plantId)

      );
    }
    else {
      return DTOMapper.INSTANCE.convertEntityToPlantGetDTO(plant);
    }
  }

  @PostMapping("/plants")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public PlantGetDTO createPlant(@RequestBody PlantPostDTO plantPostDTO) {
    Plant createInput = DTOMapper.INSTANCE.convertPlantPostDTOtoEntity(plantPostDTO);

    Plant createdPlant = plantService.createPlant(createInput);

    return DTOMapper.INSTANCE.convertEntityToPlantGetDTO(createdPlant);
  }

  @PutMapping("/plants/{plantId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updatePlant(@RequestBody PlantPutDTO plantPutDTO, @PathVariable Long plantId) {
    Plant plant = plantService.getPlantById(plantId);

    if (plant == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested plant with id %s does not exist.", plantId)
      );
    }
    else {
      Plant changedPlant = DTOMapper.INSTANCE.convertPlantPutDTOtoEntity(plantPutDTO);

      plant.setPlantName(changedPlant.getPlantName());
      plant.setSpecies(changedPlant.getSpecies());
      plant.setCareInstructions(changedPlant.getCareInstructions());
      plant.setLastWateringDate(changedPlant.getLastWateringDate());
      plant.setNextWateringDate(changedPlant.getNextWateringDate());
      plant.setWateringInterval(changedPlant.getWateringInterval());
      plant.setLastCaringDate(changedPlant.getLastCaringDate());
      plant.setNextCaringDate(changedPlant.getNextCaringDate());
      plant.setCaringInterval(changedPlant.getCaringInterval());
      plant.setOwner(changedPlant.getOwner());
      plant.setCaretakers(changedPlant.getCaretakers());
      plant.setPlantImageUrl(changedPlant.getPlantImageUrl());

      plantService.updatePlant(plant);

    }
  }

  @PutMapping("/plants/{plantId}/water")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updateWatering(@PathVariable Long plantId) {
    Plant plant = plantService.getPlantById(plantId);

    if (plant == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested plant with id %s does not exist.", plantId)
      );
    }
    else {
      plantService.waterPlant(plant);
    }
  }

  @PutMapping("/plants/{plantId}/care")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updateCaring(@PathVariable Long plantId) {
    Plant plant = plantService.getPlantById(plantId);

    if (plant == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested plant with id %s does not exist.", plantId)
      );
    }
    else {
      plantService.careForPlant(plant);
    }
  }

  @DeleteMapping("/plants/{plantId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void deletePlant(@PathVariable Long plantId) {
    Plant plant = plantService.getPlantById(plantId);

    if (plant == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested plant with id %s does not exist", plantId)
      );
    }
    else {
      plantService.deletePlant(plant);
    }
  }


  @GetMapping("/plants/owned")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<PlantGetDTO> getAllOwnedPlants(@RequestParam("ownerId") Long id) throws ResponseStatusException {

    List<Plant> ownedPlants = plantService.getOwnedPlantsByUserId(id);

    return ownedPlants.stream()
                        .map(DTOMapper.INSTANCE::convertEntityToPlantGetDTO)
                        .collect(Collectors.toList());
  }

  @GetMapping("/plants/caredFor")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<PlantGetDTO> getAllCaredForPlants(@RequestParam("careTakerId") Long id) throws ResponseStatusException {

    List<Plant> caredForPlants = plantService.getCaretakerPlantsByUserId(id);

    return caredForPlants.stream()
                        .map(DTOMapper.INSTANCE::convertEntityToPlantGetDTO)
                        .collect(Collectors.toList());
  }

  @PostMapping("/plants/{plantId}/caretakers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void addCaretakerToPlant(@PathVariable Long plantId, @RequestBody CaretakerPostDTO caretakerPostDTO) {
      Long caretakerId = caretakerPostDTO.getCaretakerId();

      plantService.addCaretakerToPlant(caretakerId, plantId);
  }

  @DeleteMapping("/plants/{plantId}/caretakers/{caretakerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void addCaretakerToPlant(@PathVariable Long plantId, @PathVariable Long caretakerId) {


      plantService.deleteCaretakerFromPlant(caretakerId, plantId);
  }

  @PostMapping("/checkAllWatering")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Map<String, Object> checkAllWatering() throws IOException {
    List<EmailMessageDTO> messages = plantService.generateEmailMessagesForOverduePlants();
    Map<String, Object> response = new HashMap<>();
    response.put("SandboxMode", false);
    response.put("Messages", messages);

    ObjectMapper objectMapper = new ObjectMapper();
    String jacksonData = objectMapper.writeValueAsString(response);
    String mjResponse = plantService.callMailJet(jacksonData);

    response.put("MailJetResponse", mjResponse);
    return response;
  }

  @PostMapping("/plants/{plantId}/space")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void assignPlantToSpace(@PathVariable Long plantId, @RequestBody SpaceAssignmentPostDTO spaceAssignmentPostDTO) {
    Long spaceId = spaceAssignmentPostDTO.getSpaceId();

    plantService.assignPlantToSpace(plantId, spaceId);
  }

  @DeleteMapping("/plants/{plantId}/space/{spaceId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void removePlantFromSpace(@PathVariable Long plantId, @PathVariable Long spaceId) {

    plantService.removePlantFromSpace(plantId, spaceId);
  }


  @PostMapping(path = "/plants/{plantId}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String addImage(@PathVariable Long plantId, @RequestParam("image") MultipartFile image) {

    Plant plant = plantService.getPlantById(plantId);

    if (plant == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format("Requested plant with id %s does not exist.", plantId)
      );
    }
    else {
      String gcsFileURI = gcpStorageService.uploadImage(image);
      plant.setPlantImageUrl(gcsFileURI);

      plantService.updatePlant(plant);

      return gcsFileURI;
    }
  }
}



