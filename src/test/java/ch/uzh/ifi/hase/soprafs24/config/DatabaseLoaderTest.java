package ch.uzh.ifi.hase.soprafs24.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

@SpringBootTest
@Transactional
public class DatabaseLoaderTest {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PlantRepository plantRepository;

    @Test
    public void testDatabaseInitialization() {
        assertEquals(1, userRepository.count());
        assertEquals(1, plantRepository.count());

        User loadedUser = userRepository.findByUsername("initialUser");
        assertNotNull(loadedUser);
        assertEquals("initialUser@email.com", loadedUser.getEmail());

        Plant loadedPlant = plantRepository.findByPlantId(2L);
        assertNotNull(loadedPlant);
        assertEquals("initialSpecies", loadedPlant.getSpecies());
        assertEquals(loadedUser.getId(), loadedPlant.getOwner().getId());
    }
}
