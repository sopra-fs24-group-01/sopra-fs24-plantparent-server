package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlantService {

  private final PlantRepository plantRepository;

  @Autowired
  public PlantService(@Qualifier("plantRepository") PlantRepository plantRepository) {
    this.plantRepository = plantRepository;
  }

  public List<Plant> getPlants() {
    return this.plantRepository.findAll();
  }

  public Plant getPlantById(Long id) {
    return this.plantRepository.findById(id).orElse(null);
  }

  /*
  public List<Plant> getPlantsForUserId(Long userId) {
    List<Plant> plants = this.plantRepository.findby

    return plants;
  }*/

  public Plant createPlant(Plant newPlant) {
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
