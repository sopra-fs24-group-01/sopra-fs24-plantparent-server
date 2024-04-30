package ch.uzh.ifi.hase.soprafs24.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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

}
