package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Plant;
import ch.uzh.ifi.hase.soprafs24.entity.User;

import java.util.List;

public class SpacePostDTO {
  private String spaceName;
  private User spaceOwner;
  private List<Plant> plantsContained;

  /**
   * getters and setters
   */

  public String getSpaceName() {
    return spaceName;
  }

  public void setSpaceName(String spaceName) {
    this.spaceName = spaceName;
  }

  public User getSpaceOwner() {
    return spaceOwner;
  }

  public void setSpaceOwner(User spaceOwner) {
    this.spaceOwner = spaceOwner;
  }

  public List<Plant> getPlantsContained() {
    return plantsContained;
  }

  public void setPlantsContained(List<Plant> plantsContained) {
    this.plantsContained = plantsContained;
  }
}
