package ch.uzh.ifi.hase.soprafs24.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.SpaceNotFoundException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

@Service
@Transactional
public class SpaceService {

  private final SpaceRepository spaceRepository;
  private final UserRepository userRepository;

  @Autowired
  public SpaceService(@Qualifier("spaceRepository") SpaceRepository spaceRepository,
                      @Qualifier("userRepository") UserRepository userRepository) {
    this.spaceRepository = spaceRepository;
    this.userRepository = userRepository;
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
  public void deleteMemeberFromSpace(Long memberId, Long spaceId) {
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

}
