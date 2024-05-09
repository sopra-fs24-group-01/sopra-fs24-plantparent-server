package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpaceGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpacePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpacePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class SpaceController {

  private final SpaceService spaceService;

  SpaceController(SpaceService spaceService) {
    this.spaceService = spaceService;
  }

  @GetMapping("/spaces")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<SpaceGetDTO> getAllSpaces() {
    List<SpaceGetDTO> spaceGetDTOs = new ArrayList<>();

    List<Space> spaces = spaceService.getSpaces();
    for (Space space : spaces) {
      spaceGetDTOs.add(DTOMapper.INSTANCE.convertEntityToSpaceGetDTO(space));
    }
    return spaceGetDTOs;
  }

  @GetMapping("/spaces/{spaceId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public SpaceGetDTO getSpace(@PathVariable Long spaceId) {
    Space space = spaceService.getSpaceById(spaceId);

    if (space == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format(
                      "Requested space with id %s does not exist.",
                      spaceId
              )
      );
    }
    else {
      return DTOMapper.INSTANCE.convertEntityToSpaceGetDTO(space);
    }
  }

  @PostMapping("/spaces")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public SpaceGetDTO createSpace(@RequestBody SpacePostDTO spacePostDTO) {
    Space spaceInput = DTOMapper.INSTANCE.convertSpacePostDTOtoEntity(spacePostDTO);
    Space createdSpace = spaceService.createSpace(spaceInput);

    return DTOMapper.INSTANCE.convertEntityToSpaceGetDTO(createdSpace);
  }

  @PutMapping("/spaces/{spaceId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updateSpace(@RequestBody SpacePutDTO spacePutDTO, @PathVariable Long spaceId) {
    Space space = spaceService.getSpaceById(spaceId);

    if (space == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format(
                      "Requested space with id %s does not exist.",
                      spaceId
              )
      );
    }
    else {
      Space updatedSpace = DTOMapper.INSTANCE.convertSpacePutDTOtoEntity(spacePutDTO);

      space.setSpaceName(updatedSpace.getSpaceName());
      space.setSpaceOwner(updatedSpace.getSpaceOwner());
      space.setPlantsContained(updatedSpace.getPlantsContained());

      spaceService.updateSpace(space);
    }
  }

  @DeleteMapping("/spaces/{spaceId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void deleteSpace(@PathVariable Long spaceId) {
    Space space = spaceService.getSpaceById(spaceId);

    if (space == null) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              String.format(
                      "Requested space with id %s does not exist.",
                      spaceId
              )
      );
    }
    else {
      spaceService.deleteSpace(space);
    }
  }

  @GetMapping("/plants/space")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<PlantGetDTO> getAllContainedPlants(@RequestParam("spaceId") Long spaceId) throws ResponseStatusException {

    List<Plant> containedPlants = spaceService.getContainedPlantsBySpaceId(spaceId);

    return containedPlants.stream()
            .map(DTOMapper.INSTANCE::convertEntityToPlantGetDTO)
            .collect(Collectors.toList());

  }
  
}
