package ch.uzh.ifi.hase.soprafs24.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

@Component
public class DatabaseLoader implements CommandLineRunner{
    
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    @Autowired
    public DatabaseLoader(UserRepository userRepository, PlantRepository plantRepository) {
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) { // Check if data needs to be initialized
            User initialUser = new User();
            initialUser.setEmail("initialUser@email.com");
            initialUser.setUsername("initialUser");
            initialUser.setPassword("password");
            userRepository.save(initialUser);


            // if we add further stuff here, tests need to be adjusted
            // i had to hardcode the plantId in the test, 
            // as we don't have a method findByName for plants because the names don't have to be unique

            Plant initialPlant = new Plant();
            initialPlant.setName("initialPlant");
            initialPlant.setSpecies("initialSpecies");
            initialPlant.setOwner(initialUser);
            initialPlant.setCareInstructions("Only water at night.");
            initialPlant.setLastWateringDate(new Date(10,10,10));
            initialPlant.setWateringInterval(3);
            initialPlant.setNextWateringDate(new Date(10,10,13));
            plantRepository.save(initialPlant);

        }
    }
}
