package ch.uzh.ifi.hase.soprafs24.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.SpaceRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

@Component
public class DatabaseLoader implements CommandLineRunner{

    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
  private final SpaceRepository spaceRepository;

    @Autowired
    public DatabaseLoader(UserRepository userRepository, PlantRepository plantRepository, SpaceRepository spaceRepository) {
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
      this.spaceRepository = spaceRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) { // Check if data needs to be initialized
            User initialUser = new User();
          initialUser.setEmail("initialUser@muell.icu");
            initialUser.setUsername("initialUser");
            initialUser.setPassword("password");
            userRepository.save(initialUser);

            User secondUser = new User();
          secondUser.setEmail("secondUser@muell.icu");
            secondUser.setUsername("secondlUser");
            secondUser.setPassword("password2");
            userRepository.save(secondUser);

            User thirdUser = new User();
          thirdUser.setEmail("thirdUser@muell.icu");
            thirdUser.setUsername("thirdUser");
            thirdUser.setPassword("password3");
            userRepository.save(thirdUser);

            User fourthUser = new User();
          fourthUser.setEmail("fourthUser@muell.icu");
            fourthUser.setUsername("fourthlUser");
            fourthUser.setPassword("password4");
            userRepository.save(fourthUser);

            User fifthUser = new User();
          fifthUser.setEmail("fifthUser@muell.icu");
            fifthUser.setUsername("fifthUser");
            fifthUser.setPassword("password5");
            userRepository.save(fifthUser);


            // if we add further stuff here, tests need to be adjusted
            // i had to hardcode the plantId in the test,
            // as we don't have a method findByName for plants because the names don't have to be unique

            Plant initialPlant = new Plant();
            initialPlant.setPlantName("initialPlant");
            initialPlant.setSpecies("initialSpecies");
            initialPlant.setOwner(initialUser);
            initialPlant.setCaretakers(new ArrayList<>(Arrays.asList(thirdUser)));
            initialPlant.setCareInstructions("Only water at night.");
            initialPlant.setLastWateringDate(new Date(124,3,26));
            initialPlant.setWateringInterval(3);
            initialPlant.setNextWateringDate(new Date(124,3,29));
            initialPlant.setLastCaringDate(new Date(124,3,23));
            initialPlant.setCaringInterval(7);
            initialPlant.setNextCaringDate(new Date(124,3,30));
          initialPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
            plantRepository.save(initialPlant);


            Plant secondPlant = new Plant();
            secondPlant.setPlantName("second plant");
            secondPlant.setSpecies("desert plant");
            secondPlant.setOwner(initialUser);
            secondPlant.setCaretakers(new ArrayList<>(Arrays.asList(secondUser, thirdUser)));
            secondPlant.setCareInstructions("Don't overwater.");
            secondPlant.setLastWateringDate(new Date(124,1,1));
            secondPlant.setWateringInterval(365);
            secondPlant.setNextWateringDate(new Date(125,1,1));
            secondPlant.setLastCaringDate(new Date(124,1,1));
            secondPlant.setCaringInterval(730);
            secondPlant.setNextCaringDate(new Date(125,1,1));
          secondPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
            plantRepository.save(secondPlant);

            Plant thirdPlant = new Plant();
            thirdPlant.setPlantName("thirsty plant");
            thirdPlant.setSpecies("water plant");
            thirdPlant.setOwner(fourthUser);
            thirdPlant.setCaretakers(new ArrayList<>(Arrays.asList(initialUser, secondUser, thirdUser, fifthUser)));
            thirdPlant.setCareInstructions("Drown it!");
            thirdPlant.setLastWateringDate(new Date(124,3,18));
            thirdPlant.setWateringInterval(1);
            thirdPlant.setNextWateringDate(new Date(124,3,19));
            thirdPlant.setLastCaringDate(new Date(124,3,10));
            thirdPlant.setCaringInterval(10);
            thirdPlant.setNextCaringDate(new Date(124,3,20));
          thirdPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
            plantRepository.save(thirdPlant);


            Plant fourthPlant = new Plant();
            fourthPlant.setPlantName("regular plant");
            fourthPlant.setSpecies("normal plant");
            fourthPlant.setOwner(secondUser);
            fourthPlant.setCaretakers(new ArrayList<>(Arrays.asList(initialUser, thirdUser, fifthUser)));
            fourthPlant.setCareInstructions("Put it in the sunlight every once in a while");
            fourthPlant.setLastWateringDate(new Date(124,3,18));
            fourthPlant.setWateringInterval(10);
            fourthPlant.setNextWateringDate(new Date(124,3,28));
            fourthPlant.setLastCaringDate(new Date(124,2,1));
            fourthPlant.setCaringInterval(25);
            fourthPlant.setNextCaringDate(new Date(124,2,25));
          fourthPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
            plantRepository.save(fourthPlant);

            Plant fifthPlant = new Plant();
            fifthPlant.setPlantName("boring plant");
            fifthPlant.setSpecies("normal plant");
            fifthPlant.setOwner(secondUser);
            fifthPlant.setCaretakers(new ArrayList<>(Arrays.asList(thirdUser, fifthUser)));
            fifthPlant.setCareInstructions("A cup of water is enough");
            fifthPlant.setLastWateringDate(new Date(124,4,18));
            fifthPlant.setWateringInterval(8);
            fifthPlant.setNextWateringDate(new Date(124,4,26));
            fifthPlant.setLastCaringDate(new Date(124,4,10));
            fifthPlant.setCaringInterval(15);
            fifthPlant.setNextCaringDate(new Date(124,4,25));
          fifthPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
            plantRepository.save(fifthPlant);

            initialUser.getPlantsCaredFor().add(thirdPlant);
            initialUser.getPlantsCaredFor().add(fourthPlant);
            userRepository.save(initialUser);

            secondUser.getPlantsCaredFor().add(secondPlant);
            secondUser.getPlantsCaredFor().add(thirdPlant);
            userRepository.save(secondUser);

            thirdUser.getPlantsCaredFor().add(initialPlant);
            thirdUser.getPlantsCaredFor().add(secondPlant);
            thirdUser.getPlantsCaredFor().add(thirdPlant);
            thirdUser.getPlantsCaredFor().add(fourthPlant);
            thirdUser.getPlantsCaredFor().add(fifthPlant);
            userRepository.save(thirdUser);

            fifthUser.getPlantsCaredFor().add(thirdPlant);
            fifthUser.getPlantsCaredFor().add(fourthPlant);
            fifthUser.getPlantsCaredFor().add(fifthPlant);
            userRepository.save(fifthUser);

          Space livingRoom = new Space();
          livingRoom.setSpaceName("living room");
          livingRoom.setSpaceOwner(initialUser);
          initialUser.setSpacesOwned(new ArrayList<>(Arrays.asList(livingRoom)));
          //livingRoom.setPlantsContained(new ArrayList<>(Arrays.asList(initialPlant, secondPlant)));
          initialPlant.setSpace(livingRoom);
          secondPlant.setSpace(livingRoom);
          spaceRepository.save(livingRoom);
          userRepository.save(initialUser);
          plantRepository.save(initialPlant);
          plantRepository.save(secondPlant);


          Space bedroom = new Space();
          bedroom.setSpaceName("bedroom");
          bedroom.setSpaceOwner(secondUser);
          secondUser.setSpacesOwned(new ArrayList<>(Arrays.asList(bedroom)));
          bedroom.setPlantsContained(new ArrayList<>(Arrays.asList(fourthPlant)));
          fourthPlant.setSpace(bedroom);
          spaceRepository.save(bedroom);
          userRepository.save(secondUser);
          plantRepository.save(fourthPlant);


          Space hallway = new Space();
          hallway.setSpaceName("hallway");
          hallway.setSpaceOwner(secondUser);
          secondUser.getSpacesOwned().add(bedroom);
          hallway.setPlantsContained(new ArrayList<>(Arrays.asList(fifthPlant)));
          fifthPlant.setSpace(hallway);
          spaceRepository.save(hallway);
          userRepository.save(secondUser);
          plantRepository.save(fifthPlant);
        }
    }
}
