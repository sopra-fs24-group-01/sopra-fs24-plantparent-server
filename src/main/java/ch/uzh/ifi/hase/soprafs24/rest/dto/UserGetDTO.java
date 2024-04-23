package ch.uzh.ifi.hase.soprafs24.rest.dto;


import ch.uzh.ifi.hase.soprafs24.entity.Plant;

import java.util.List;

public class UserGetDTO {

  private Long id;
  private String email;
  private String username;
  private String token;
  private List<Plant> plantsOwned;
  private List<Plant> plantsCaredFor;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public List<Plant> getPlantsOwned() {
    return plantsOwned;
  }

  public void setPlantsOwned(List<Plant> plantsOwned) {
    this.plantsOwned = plantsOwned;
  }

  public List<Plant> getPlantsCaredFor() {
    return plantsCaredFor;
  }

  public void setPlantsCaredFor(List<Plant> plantsCaredFor) {
    this.plantsCaredFor = plantsCaredFor;
  }
}
