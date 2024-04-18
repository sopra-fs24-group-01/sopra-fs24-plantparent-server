package ch.uzh.ifi.hase.soprafs24.controller;


import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.PlantService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@RestController
public class PlantController {
  private final PlantService plantService;

  PlantController(PlantService plantService) {
    this.plantService = plantService;
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
      plant.setOwner(changedPlant.getOwner());
      plant.setCaretakers(changedPlant.getCaretakers());

      plantService.updatePlant(plant);

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
  

}
  

