package ch.uzh.ifi.hase.soprafs24.config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.PlantService;
import ch.uzh.ifi.hase.soprafs24.service.SpaceService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

@Component
public class DatabaseLoader implements CommandLineRunner{


    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseLoader.class);
    private final UserService userService;
    private final PlantService plantService;
    private final SpaceService spaceService;

    @Autowired
    public DatabaseLoader(UserService userService, PlantService plantService, SpaceService spaceService) {
        this.userService = userService;
        this.plantService = plantService;
        this.spaceService = spaceService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
      
        if (userService.needsInitialization()) {
          LOGGER.info("Database is empty so it is initialized.");
          User initialUser = new User();
          initialUser.setEmail("initialUser@muell.icu");
          initialUser.setUsername("initialUser");
          initialUser.setPassword("password");
          initialUser = userService.createUser(initialUser);

          User secondUser = new User();
          secondUser.setEmail("secondUser@muell.icu");
          secondUser.setUsername("secondlUser");
          secondUser.setPassword("password2");
          secondUser = userService.createUser(secondUser);

          User thirdUser = new User();
          thirdUser.setEmail("thirdUser@muell.icu");
          thirdUser.setUsername("thirdUser");
          thirdUser.setPassword("password3");
          thirdUser = userService.createUser(thirdUser);

          User fourthUser = new User();
          fourthUser.setEmail("fourthUser@muell.icu");
          fourthUser.setUsername("fourthlUser");
          fourthUser.setPassword("password4");
          fourthUser = userService.createUser(fourthUser);

          User fifthUser = new User();
          fifthUser.setEmail("fifthUser@muell.icu");
          fifthUser.setUsername("fifthUser");
          fifthUser.setPassword("password5");
          fifthUser = userService.createUser(fifthUser);

          Plant initialPlant = new Plant();
          initialPlant.setPlantName("initialPlant");
          initialPlant.setSpecies("initialSpecies");
          initialPlant.setOwner(initialUser);
          initialPlant.setCareInstructions("Only water at night.");
          initialPlant.setLastWateringDate(new Date(124,3,26));
          initialPlant.setWateringInterval(3);
          initialPlant.setNextWateringDate(new Date(124,3,29));
          initialPlant.setLastCaringDate(new Date(124,3,23));
          initialPlant.setCaringInterval(7);
          initialPlant.setNextCaringDate(new Date(124,3,30));
          initialPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
          initialPlant = plantService.createPlant(initialPlant);
          plantService.addCaretakerToPlant(thirdUser.getId(), initialPlant.getPlantId());

          Plant secondPlant = new Plant();
          secondPlant.setPlantName("second plant");
          secondPlant.setSpecies("desert plant");
          secondPlant.setOwner(initialUser);
          secondPlant.setCareInstructions("Don't overwater.");
          secondPlant.setLastWateringDate(new Date(124,1,1));
          secondPlant.setWateringInterval(365);
          secondPlant.setNextWateringDate(new Date(125,1,1));
          secondPlant.setLastCaringDate(new Date(124,1,1));
          secondPlant.setCaringInterval(730);
          secondPlant.setNextCaringDate(new Date(125,1,1));
          secondPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
          secondPlant = plantService.createPlant(secondPlant);
          plantService.addCaretakerToPlant(secondUser.getId(), secondPlant.getPlantId());
          plantService.addCaretakerToPlant(thirdUser.getId(), secondPlant.getPlantId());

          Plant thirdPlant = new Plant();
          thirdPlant.setPlantName("thirsty plant");
          thirdPlant.setSpecies("water plant");
          thirdPlant.setOwner(fourthUser);
          thirdPlant.setCareInstructions("Drown it!");
          thirdPlant.setLastWateringDate(new Date(124,3,18));
          thirdPlant.setWateringInterval(1);
          thirdPlant.setNextWateringDate(new Date(124,3,19));
          thirdPlant.setLastCaringDate(new Date(124,3,10));
          thirdPlant.setCaringInterval(10);
          thirdPlant.setNextCaringDate(new Date(124,3,20));
          thirdPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
          thirdPlant = plantService.createPlant(thirdPlant);
          plantService.addCaretakerToPlant(initialUser.getId(), thirdPlant.getPlantId());
          plantService.addCaretakerToPlant(secondUser.getId(), thirdPlant.getPlantId());
          plantService.addCaretakerToPlant(thirdUser.getId(), thirdPlant.getPlantId());
          plantService.addCaretakerToPlant(fifthUser.getId(), thirdPlant.getPlantId());

          Plant fourthPlant = new Plant();
          fourthPlant.setPlantName("regular plant");
          fourthPlant.setSpecies("normal plant");
          fourthPlant.setOwner(secondUser);
          fourthPlant.setCareInstructions("Put it in the sunlight every once in a while");
          fourthPlant.setLastWateringDate(new Date(124,3,18));
          fourthPlant.setWateringInterval(10);
          fourthPlant.setNextWateringDate(new Date(124,3,28));
          fourthPlant.setLastCaringDate(new Date(124,2,1));
          fourthPlant.setCaringInterval(25);
          fourthPlant.setNextCaringDate(new Date(124,2,25));
          fourthPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
          fourthPlant = plantService.createPlant(fourthPlant);
          plantService.addCaretakerToPlant(initialUser.getId(), fourthPlant.getPlantId());
          plantService.addCaretakerToPlant(thirdUser.getId(), fourthPlant.getPlantId());
          plantService.addCaretakerToPlant(fifthUser.getId(), fourthPlant.getPlantId());
        
          Plant fifthPlant = new Plant();
          fifthPlant.setPlantName("boring plant");
          fifthPlant.setSpecies("normal plant");
          fifthPlant.setOwner(secondUser);
          fifthPlant.setCareInstructions("A cup of water is enough");
          fifthPlant.setLastWateringDate(new Date(124,4,18));
          fifthPlant.setWateringInterval(8);
          fifthPlant.setNextWateringDate(new Date(124,4,26));
          fifthPlant.setLastCaringDate(new Date(124,4,10));
          fifthPlant.setCaringInterval(15);
          fifthPlant.setNextCaringDate(new Date(124,4,25));
          fifthPlant.setPlantImageUrl("https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/plant.jpg");
          fifthPlant = plantService.createPlant(fifthPlant);
          plantService.addCaretakerToPlant(thirdUser.getId(), fifthPlant.getPlantId());
          plantService.addCaretakerToPlant(fifthUser.getId(), fifthPlant.getPlantId());

          Space livingRoom = new Space();
          livingRoom.setSpaceName("living room");
          livingRoom.setSpaceOwner(initialUser);
          livingRoom = spaceService.createSpace(livingRoom);
          plantService.assignPlantToSpace(initialPlant.getPlantId(), livingRoom.getSpaceId());
          plantService.assignPlantToSpace(secondPlant.getPlantId(), livingRoom.getSpaceId());

          Space bedroom = new Space();
          bedroom.setSpaceName("bedroom");
          bedroom.setSpaceOwner(secondUser);
          bedroom = spaceService.createSpace(bedroom);
          plantService.assignPlantToSpace(fourthPlant.getPlantId(), bedroom.getSpaceId());
          spaceService.addMemberToSpace(fifthUser.getId(), bedroom.getSpaceId());

          Space hallway = new Space();
          hallway.setSpaceName("hallway");
          hallway.setSpaceOwner(secondUser);
          hallway = spaceService.createSpace(hallway);
          plantService.assignPlantToSpace(fifthPlant.getPlantId(), hallway.getSpaceId());
          spaceService.addMemberToSpace(fifthUser.getId(), hallway.getSpaceId());
          spaceService.addMemberToSpace(initialUser.getId(), hallway.getSpaceId());
        }
        else {
          LOGGER.info("Database is not empty. No Data is initialized.");
        }
    }
}
