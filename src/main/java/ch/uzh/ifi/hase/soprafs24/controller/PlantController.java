package ch.uzh.ifi.hase.soprafs24.controller;


import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.PlantService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.stream.Collectors;



@RestController
public class PlantController {
  private final PlantService plantService;

  PlantController(PlantService plantService) {
    this.plantService = plantService;
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
  


