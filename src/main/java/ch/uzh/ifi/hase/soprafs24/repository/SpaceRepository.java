package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("spaceRepository")
public interface SpaceRepository extends JpaRepository<Space, Long> {
    Space findBySpaceId(Long spaceId);
    
} 