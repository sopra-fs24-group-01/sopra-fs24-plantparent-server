package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.PlantNotFoundException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPlantDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    User owner = validateUser(userId);
    List<Plant> plants = owner.getPlantsOwned();

    return plants;
  }

  
  // gets plants where user is caretaker
  public List<Plant> getCaretakerPlantsByUserId(Long userId) {

    User caretaker = validateUser(userId);
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
      //TODO: check if current user is owner
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
      // Get Users that have this plant
      User owner = plant.getOwner();
      List<Plant> plantsOwned = owner.getPlantsOwned();
      plantsOwned.remove(plant);
      owner.setPlantsOwned(plantsOwned);

      userRepository.saveAndFlush(owner);

      plantRepository.delete(plant);
      plantRepository.flush();

    }
  }


  /*public void verifyIfUserIsOwner(Long userId, Long plantId) {

    Plant plant = getPlantById(plantId);
    if (plant == null) {
      throw new PlantNotFoundException("No plant with " + plantId + " found.");
    }

    User owner = plant.getOwner();
    if (owner == null) {
      // How should we handle such expections? Do they automatically return a BAD Request HTTP code?
      throw new RuntimeException("Plant with ID " + plantId + " does not have an owner");
    }

    if (!owner.getId().equals(userId)) {
      throw new RuntimeException("The current user is not the owner of this plant.");
    }
  }*/

  public void addCaretakerToPlant(Long caretakerId, Long plantId) {

    // checks
    //User owner = validateUser(ownerId);
    User caretaker = validateUser(caretakerId);
    Plant plant = validatePlant(plantId);

    /*if (ownerId.equals(caretakerId)) {
      throw new RuntimeException("Owner cannot add himself as caretaker.");
    }*/
    // check if currentUser has rights to add caretaker to plants
    //verifyIfUserIsOwner(ownerId, plantId);

    plant.getCaretakers().add(caretaker);
    plantRepository.save(plant);
  }

  public void deleteCaretakerFromPlant(Long caretakerId, Long plantId) {

    // checks
    //User owner = validateUser(ownerId);
    User caretaker = validateUser(caretakerId);
    Plant plant = validatePlant(plantId);

   if (!plant.getCaretakers().contains(caretaker)) {
      throw new RuntimeException("Cannot delete non-existing caretaker");
    }

    // check if currentUser has rights to delete caretaker from plants
    //verifyIfUserIsOwner(ownerId, plantId);

    plant.getCaretakers().removeIf(user -> user.getId().equals(caretaker.getId()));
    plantRepository.save(plant);

  }


  // helpers to check if plants / users exists in database
  public User validateUser(Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw new UserNotFoundException("User with userId " + userId + " not found");
    }
    return user;
  }

  public Plant validatePlant(Long plantId) {    
    Plant plant = getPlantById(plantId);
    if (plant == null) {
      throw new PlantNotFoundException("No plant with " + plantId + " found.");
    }  
    return plant;
  }

  public List<UserPlantDTO> getOverduePlants() {
    List<Plant> allPlants = plantRepository.findAll();
    Map<User, List<Plant>> overduePlantsByUser = new HashMap<>();
    LocalDate today = LocalDate.now();

    for (Plant plant : allPlants) {
      LocalDate nextWateringDate = plant.getNextWateringDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      if (nextWateringDate.plusDays(2).isBefore(today)) {
        overduePlantsByUser.computeIfAbsent(plant.getOwner(), k -> new ArrayList<>()).add(plant);
      }
    }

    return overduePlantsByUser.entrySet().stream()
            .map(entry -> {
              UserPlantDTO dto = new UserPlantDTO();
              String message = generateMessage(entry.getValue());
              dto.setUserEmail(entry.getKey().getEmail());
              dto.setMessage(message);
              return dto;
            })
            .collect(Collectors.toList());
  }

  private String generateMessage(List<Plant> plants) {
    String plantNames = plants.stream()
                              .map(Plant::getPlantName)
                              .collect(Collectors.joining(", "));
    String message = "Your plant" + (plants.size() > 1 ? "s " : " ") + plantNames + (plants.size() > 1 ? " need" : " needs") + " watering.";
    return message;                           
  }
}
