package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlantService {

  private final PlantRepository plantRepository;
  private final UserRepository userRepository;

  @Autowired
  public PlantService(@Qualifier("plantRepository") PlantRepository plantRepository, 
                      @Qualifier("userRepository") UserRepository userRepository) {
    this.plantRepository = plantRepository;
    this.userRepository = userRepository;
  }

  public List<Plant> getPlants() {
    return this.plantRepository.findAll();
  }

  public Plant getPlantById(Long id) {
    return this.plantRepository.findById(id).orElse(null);
  }


  // gets just the owned plants
  public List<Plant> getOwnedPlantsByUserId(Long userId) {

    User owner = userRepository.findById(userId).orElse(null);

    if (owner == null) {
      throw new UserNotFoundException("User with userId " + userId + " not found");
    }
    // Ensuring that the user's plants are loaded within the session context to prevent lazy loading issues.
    // Maybe there are better solutions ? 
    Hibernate.initialize(owner.getPlantsOwned());
    List<Plant> plants = owner.getPlantsOwned();

    return plants;
  }

  
  // gets plants where user is caretaker
  public List<Plant> getCaretakerPlantsByUserId(Long userId) {

    User caretaker = userRepository.findById(userId).orElse(null);

    if (caretaker == null) {
      throw new UserNotFoundException("User with userId " + userId + " not found");
    }
    // Ensuring that the user's plants are loaded within the session context to prevent lazy loading issues.
    // Maybe there are better solutions ? 
    Hibernate.initialize(caretaker.getPlantsCaredFor());
    List<Plant> plants = caretaker.getPlantsCaredFor();

    return plants;
  }

  public Plant createPlant(Plant newPlant) {
    if (newPlant.getOwner() == null) {
      throw new RuntimeException("Can't update create plant. No Owner assigned.");
    }
    newPlant = plantRepository.save(newPlant);
    plantRepository.flush();
    return newPlant;
  }

  public Plant updatePlant(Plant plant) {
    Plant existingPlant = getPlantById(plant.getPlantId());
    if (existingPlant == null) {
      throw new RuntimeException("Can't update nonexisting plant.");
    }
    else {
      plantRepository.saveAndFlush(plant);
      return plant;
    }
  }

  public void deletePlant(Plant plant) {
    Plant existingPlant = getPlantById(plant.getPlantId());
    if (existingPlant == null) {
      throw new RuntimeException("Can't delete nonexisting plant.");
    }
    else {
      // TODO: check if current user is owner.
      plantRepository.delete(plant);
    }
  }


}
