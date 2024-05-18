package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.exceptions.SpaceAlreadyExistsException;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlantGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpaceGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpacePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.SpacePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.PlantService;
import ch.uzh.ifi.hase.soprafs24.service.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class SpaceController {

  private final SpaceService spaceService;
  private final PlantService plantService;

  SpaceController(SpaceService spaceService, PlantService plantService) {
    this.spaceService = spaceService;
    this.plantService = plantService;
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
    try {
      Space spaceInput = DTOMapper.INSTANCE.convertSpacePostDTOtoEntity(spacePostDTO);
      Space createdSpace = spaceService.createSpace(spaceInput);

      return DTOMapper.INSTANCE.convertEntityToSpaceGetDTO(createdSpace);
    } catch (SpaceAlreadyExistsException e) {
      throw new ResponseStatusException(
              HttpStatus.CONFLICT,
              e.getMessage(),
              e
      );
    }
  }

  @PutMapping("/spaces/{spaceId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public SpaceGetDTO updateSpace(@RequestBody SpacePutDTO spacePutDTO, @PathVariable Long spaceId) {
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

      // Update each plant in the space without changing the owner
      List<Plant> updatedPlants = updatedSpace.getPlantsContained().stream()
              .map(updatedPlant -> plantService.getPlantById(updatedPlant.getPlantId()))
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

      space.setPlantsContained(updatedPlants);

      Space uSpace = spaceService.updateSpace(space);

      return DTOMapper.INSTANCE.convertEntityToSpaceGetDTO(uSpace);
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

  @PostMapping("/spaces/{spaceId}/members/{memberId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void addMemberToSpace(@PathVariable Long spaceId, @PathVariable Long memberId) {
    spaceService.addMemberToSpace(memberId, spaceId);
  }

  @DeleteMapping("/spaces/{spaceId}/members/{memberId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deleteMemberFromSpace(@PathVariable Long spaceId, @PathVariable Long memberId) {
    spaceService.deleteMemberFromSpace(memberId, spaceId);
  }

  @PostMapping("/spaces/{spaceId}/plants/{plantId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void addPlantToSpace(@PathVariable Long spaceId, @PathVariable Long plantId) {

    spaceService.addPlantToSpace(plantId, spaceId);
  }

  @DeleteMapping("/spaces/{spaceId}/plants/{plantId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deletePlantFromSpace(@PathVariable Long spaceId, @PathVariable Long plantId) {
    spaceService.deletePlantFromSpace(plantId, spaceId);
  }

  @GetMapping("/spaces/owned")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<SpaceGetDTO> getAllOwnedSpaces(@RequestParam("ownerId") Long id) throws ResponseStatusException {

    List<Space> ownedSpaces = spaceService.getOwnedSpacesByUserId(id);

    return ownedSpaces.stream()
            .map(DTOMapper.INSTANCE::convertEntityToSpaceGetDTO)
            .collect(Collectors.toList());
  }

  @GetMapping("spaces/member")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<SpaceGetDTO> getAllMembershipSpaces(@RequestParam("memberId") Long id) throws ResponseStatusException {

    List<Space> memberSpaces = spaceService.getMembershipSpacesByUserId(id);

    return memberSpaces.stream()
            .map(DTOMapper.INSTANCE::convertEntityToSpaceGetDTO)
            .collect(Collectors.toList());
  }


}
