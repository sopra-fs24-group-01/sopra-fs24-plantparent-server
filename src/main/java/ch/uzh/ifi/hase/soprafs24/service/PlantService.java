package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.config.CredentialsLoader;
import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.PlantNotFoundException;
import ch.uzh.ifi.hase.soprafs24.exceptions.SpaceNotFoundException;
import ch.uzh.ifi.hase.soprafs24.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.EmailMessageDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlantService {

  private final PlantRepository plantRepository;
  private final UserRepository userRepository;
  private final CredentialsLoader credentialsLoader;
  private final SpaceRepository spaceRepository;

  @Autowired
  public PlantService(@Qualifier("plantRepository") PlantRepository plantRepository,
                      @Qualifier("userRepository") UserRepository userRepository,
                      @Qualifier("spaceRepository") SpaceRepository spaceRepository,
                      CredentialsLoader credentialsLoader
  ) {
    this.plantRepository = plantRepository;
    this.userRepository = userRepository;
    this.spaceRepository = spaceRepository;
    this.credentialsLoader = credentialsLoader;
  }

  public List<Plant> getPlants() {
    return this.plantRepository.findAll();
  }

  public Plant getPlantById(Long id) {
    Plant plant = this.plantRepository.findById(id).orElse(null);
    if (plant == null) {
      return null;
    }
    List<User> caretakers = new ArrayList<User>(new HashSet<User>(plant.getCaretakers()));
    plant.setCaretakers(caretakers);
    return plant;
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
      plantRepository.saveAndFlush(plant);
      return plant;
    }
  }

  public void waterPlant(Plant plant) {
    Plant existingPlant = getPlantById(plant.getPlantId());
    if (existingPlant == null) {
      throw new RuntimeException("Can't water nonexisting plant.");
    }
    else {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      existingPlant.setLastWateringDate(cal.getTime());
      existingPlant.calculateAndSetNextWateringDate();
      plantRepository.saveAndFlush(existingPlant);
    }
  }

  public void careForPlant(Plant plant) {
    Plant existingPlant = getPlantById(plant.getPlantId());
    if (existingPlant == null) {
      throw new RuntimeException("Can't care for nonexisting plant.");
    }
    else {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      existingPlant.setLastCaringDate(cal.getTime());
      existingPlant.calculateAndSetNextCaringDate();
      plantRepository.saveAndFlush(existingPlant);
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

    // check if caretaker is already caretaker
    if (plant.getCaretakers().contains(caretaker)) {
      throw new RuntimeException("This user with id " + caretakerId + " is already a caretaker");
    }

    plant.getCaretakers().add(caretaker);
    caretaker.getPlantsCaredFor().add(plant);
    plantRepository.save(plant);
    userRepository.save(caretaker);
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
    caretaker.getPlantsCaredFor().removeIf(p -> p.getPlantId().equals(plant.getPlantId()));
    plantRepository.save(plant);
    userRepository.save(caretaker);

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
      throw new PlantNotFoundException("No plant with plantId " + plantId + " found.");
    }  

    return plant;
  }

  public Space validateSpace(Long spaceId) {
    Space space = spaceRepository.findById(spaceId).orElse(null);
    if (space == null) {
      throw new SpaceNotFoundException("No space with spaceId " + spaceId + " found.");
    }

    return space;
  }


  public List<EmailMessageDTO> generateEmailMessagesForOverduePlants() {
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
            .map(entry -> createEmailMessage(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
  }

  private EmailMessageDTO createEmailMessage(User user, List<Plant> plants) {
    EmailMessageDTO message = new EmailMessageDTO();
    message.setToEmail(user.getEmail());
    message.setTextPart("Your plant" + (plants.size() > 1 ? "s " : " ")
      + plants.stream()
        .map(Plant::getPlantName)
        .collect(Collectors.joining(", ")) 
      + (plants.size() > 1 ? " need" : " needs") + " watering.");
    return message;
  }

  public Plant waterThisPlant(Plant plant) {
    Plant savedPlant = getPlantById(plant.getPlantId());
    if (savedPlant == null) {
      throw new PlantNotFoundException("No plant with id " + plant.getPlantId() + " found.");
    }

    savedPlant.calculateAndSetNextWateringDate();
    plantRepository.saveAndFlush(savedPlant);

    return savedPlant;
  }

  public Plant careForThisPlant(Plant plant) {
    Plant savedPlant = getPlantById(plant.getPlantId());
    if (savedPlant == null) {
      throw new PlantNotFoundException("No plant with id " + plant.getPlantId() + " found.");
    }

    savedPlant.calculateAndSetNextCaringDate();
    plantRepository.saveAndFlush(savedPlant);
    return savedPlant;
  }

  public String callMailJet(String requestJsonString ) {
    // set api uri
    String mailjetApiUri = "https://api.mailjet.com/v3.1/send";

    // prepare authentication header
    String basicEncoding = Base64.getEncoder().encodeToString(
            (credentialsLoader.getMjPublicKey() + ":" + credentialsLoader.getMjPrivateKey()).getBytes()
    );

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders header = new HttpHeaders();
    header.add("Authorization", "Basic " + basicEncoding);
    header.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(requestJsonString,header);
    String answer = restTemplate.postForObject(mailjetApiUri, entity, String.class);

    System.out.println("Answer from MailJet:");
    System.out.println(answer);

    return answer;
  }

  public void assignPlantToSpace(Long plantId, Long spaceId) {
    Plant plant = validatePlant(plantId);
    Space space = validateSpace(spaceId);

    // check if plant already in a space
    if (plant.getSpace() != null) {
      throw new RuntimeException("Plant with plantId " + plantId + " is already assigned to a space. Remove it from there before assining a new space");
    }
    // assign to new space
    plant.setSpace(space);
    space.getPlantsContained().add(plant); // ensure the space object's list is updated
    plantRepository.save(plant);
  }

  public void removePlantFromSpace(Long plantId, Long spaceId) {
    Plant plant = validatePlant(plantId);
    Space space = validateSpace(spaceId);

    if (plant.getSpace() == null) {
      throw new RuntimeException("Plant with plantId " + plantId + " has no space assigned. Thus it cannot be removed from a space.");
    }

    if (plant.getSpace() != space) {
      throw new RuntimeException("Cannot remove plant with plantId " + plantId + " from space with spaceId " + spaceId + " as it's not assigned to it");
    }

    // set space to null, as plants can only have 1 space
    plant.setSpace(null);
    space.getPlantsContained().remove(plant);
    plantRepository.save(plant);
  }

}
