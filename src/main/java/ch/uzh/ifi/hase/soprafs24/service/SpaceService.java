package ch.uzh.ifi.hase.soprafs24.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.PlantNotFoundException;
import ch.uzh.ifi.hase.soprafs24.exceptions.SpaceNotFoundException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

@Service
@Transactional
public class SpaceService {

  private final SpaceRepository spaceRepository;
  private final UserRepository userRepository;
  private final PlantRepository plantRepository;

  @Autowired
  public SpaceService(@Qualifier("spaceRepository") SpaceRepository spaceRepository,
                      @Qualifier("userRepository") UserRepository userRepository,
                      @Qualifier("plantRepository") PlantRepository plantRepository){
    this.spaceRepository = spaceRepository;
    this.userRepository = userRepository;
    this.plantRepository = plantRepository;
  }

  public List<Space> getSpaces() {
    return this.spaceRepository.findAll();
  }

  public Space getSpaceById(Long id) {
    return this.spaceRepository.findById(id).orElse(null);
  }

  public Space createSpace(Space newSpace) {
    if (newSpace.getSpaceOwner() == null) {
      throw new RuntimeException("Can't update create space. No owner assigned.");
    }
    newSpace = spaceRepository.save(newSpace);
    spaceRepository.flush();
    return newSpace;
  }

  public Space updateSpace(Space space) {
    Space existingSpace = getSpaceById(space.getSpaceId());
    if (existingSpace == null) {
      throw new RuntimeException("Can't update nonexisting space.");
    }
    else {
      spaceRepository.saveAndFlush(space);
      return space;
    }
  }

  public void deleteSpace(Space space) {
    Space existingSpace = getSpaceById(space.getSpaceId());
    if (existingSpace == null) {
      throw new RuntimeException("Can't delete nonexisting space.");
    }
    else {
      User owner = space.getSpaceOwner();
      List<Space> spacesOwned = owner.getSpacesOwned();
      spacesOwned.remove(space);
      owner.setSpacesOwned(spacesOwned);

      userRepository.saveAndFlush(owner);

      spaceRepository.delete(space);
      spaceRepository.flush();
    }
  }

  @Transactional(readOnly = true)
  public List<Plant> getContainedPlantsBySpaceId(Long spaceId) {

    Space space = validateSpace(spaceId);
    List<Plant> plantsContained = new ArrayList<>(space.getPlantsContained());

    return plantsContained;
  }

  public Space validateSpace(Long spaceId) {
    Space space = spaceRepository.findById(spaceId).orElse(null);
    if (space == null) {
      throw new SpaceNotFoundException("No space with spaceId " + spaceId + " found.");
    }
    return space;
  }
  public User validateUser(Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw new UserNotFoundException("User with userId " + userId + " not found");
    }
    return user;
  }

  public Plant validatePlant(Long plantId) {
    Plant plant = plantRepository.findById(plantId).orElse(null);
    if (plant == null) {
      throw new PlantNotFoundException("No plant with plantId " + plantId + " found.");
    }
    return plant;
  }

  @Transactional
  public void addMemberToSpace(Long memberId, Long spaceId) {
    // checks
    User member = validateUser(memberId);
    Space space = validateSpace(spaceId);

    // check if member already is member or is owner
    if (space.getSpaceMembers().contains(member)) {
      throw new RuntimeException("This user with id " + memberId + " is already member of this space with id " + spaceId);
    }
    if (space.getSpaceOwner().equals(member)) {
      throw new RuntimeException("This user with id " + memberId + " is the owner of the space with id " + spaceId + ". Thus cannot be added as member");
    }

    // add user to member list of space
    space.getSpaceMembers().add(member);
    member.getSpaceMemberships().add(space);

    // assign new member as caretaker to all plants in space
    space.getPlantsContained().stream()
      .filter(plant -> !plant.getCaretakers().contains(member))
      .forEach(plant -> {
        plant.getCaretakers().add(member);
        member.getPlantsCaredFor().add(plant);
      });

    // save the updated space and user entities
    spaceRepository.save(space);
    userRepository.save(member);
  }

  @Transactional
  public void deleteMemberFromSpace(Long memberId, Long spaceId) {
    // checks
    User member = validateUser(memberId);
    Space space = validateSpace(spaceId);

    // check if the user is actually member in this space
    if (!space.getSpaceMembers().contains(member)) {
      throw new RuntimeException("Cannot delete user from space where they are not member");
    }

    // remove user as caretaker from each plant
    space.getPlantsContained().forEach(plant -> {
      plant.getCaretakers().remove(member);
      member.getPlantsCaredFor().remove(plant);
    });

    // remove user from space
    space.getSpaceMembers().remove(member);
    member.getSpaceMemberships().remove(space);

    // save the updated space and user entities
    spaceRepository.save(space);
    userRepository.save(member);
  }

  @Transactional
  public void addPlantToSpace(Long plantId, Long spaceId) {
    // checks
    Plant plant = validatePlant(plantId);
    Space space = validateSpace(spaceId);

    // check if plant is already assigned to a space
    if (plant.getSpace() != null) {
      throw new RuntimeException("This plant with id " + plantId + " is already assigned to a space.");
    }
    // check if plant is already in the space
    if (space.getPlantsContained().contains(plant)) {
      throw new RuntimeException("This plant with id " + plantId + " is already added to this space with id " + spaceId);
    }

    // add user to member list of space
    space.getPlantsContained().add(plant);
    plant.setSpace(space);

    // save the updated space and plant entities
    spaceRepository.save(space);
    plantRepository.save(plant);
  }

  @Transactional
  public void deletePlantFromSpace(Long plantId, Long spaceId) {
    // checks
    Plant plant = validatePlant(plantId);
    Space space = validateSpace(spaceId);

    // check if the plant is actually part of this space
    if (!space.getPlantsContained().contains(plant)) {
      throw new RuntimeException("Cannot remove plant from a space which does not contain it.");
    }

    // remove user from space
    space.getPlantsContained().remove(plant);
    plant.setSpace(null);

    // save the updated space and plant entities
    spaceRepository.save(space);
    plantRepository.save(plant);
  }

  // get all spaces where user is owner
  public List<Space> getOwnedSpacesByUserId(Long userId) {

    User owner = validateUser(userId);
    List<Space> spaces = owner.getSpacesOwned();

    return spaces.stream().distinct().collect(Collectors.toList());
  }

  // get all spaces where user is member
  public List<Space> getMembershipSpacesByUserId(Long userId) {

    User member = validateUser(userId);
    List<Space> spaces = member.getSpaceMemberships();

    return spaces.stream().distinct().collect(Collectors.toList());
  }

}
