package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("plantRepository")
public interface PlantRepository extends JpaRepository<Plant, Long> {
  Plant findByPlantId(Long plantId);

}
