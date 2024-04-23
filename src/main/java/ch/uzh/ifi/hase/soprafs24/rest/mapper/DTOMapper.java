package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "plantsOwned", target = "plantsOwned")
  @Mapping(source = "plantsCaredFor", target = "plantsCaredFor")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

  @Mapping(source = "plantName", target = "plantName")
  @Mapping(source = "species", target = "species")
  @Mapping(source = "careInstructions", target = "careInstructions")
  @Mapping(source = "lastWateringDate", target = "lastWateringDate")
  @Mapping(source = "wateringInterval", target = "wateringInterval")
  @Mapping(source = "owner", target = "owner")
  @Mapping(source = "caretakers", target = "caretakers")
  Plant convertPlantPostDTOtoEntity(PlantPostDTO plantPostDTO);

  @Mapping(source = "plantId", target = "plantId")
  @Mapping(source = "plantName", target = "plantName")
  @Mapping(source = "species", target = "species")
  @Mapping(source = "careInstructions", target = "careInstructions")
  @Mapping(source = "lastWateringDate", target = "lastWateringDate")
  @Mapping(source = "wateringInterval", target = "wateringInterval")
  @Mapping(source = "owner", target = "owner")
  @Mapping(source = "caretakers", target = "caretakers")
  @Mapping(source = "space", target = "space")
  PlantGetDTO convertEntityToPlantGetDTO(Plant plant);

  @Mapping(source = "plantName", target = "plantName")
  @Mapping(source = "species", target = "species")
  @Mapping(source = "careInstructions", target = "careInstructions")
  @Mapping(source = "lastWateringDate", target = "lastWateringDate")
  @Mapping(source = "wateringInterval", target = "wateringInterval")
  @Mapping(source = "owner", target = "owner")
  @Mapping(source = "caretakers", target = "caretakers")
  Plant convertPlantPutDTOtoEntity(PlantPutDTO plantPutDTO);
}
