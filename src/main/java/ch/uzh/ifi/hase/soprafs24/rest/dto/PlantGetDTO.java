package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Space;
import ch.uzh.ifi.hase.soprafs24.entity.User;

import java.util.Date;
import java.util.List;

public class PlantGetDTO {
  private Long plantId;
  private String plantName;
  private String species;
  private String careInstructions;
  private Date lastWateringDate;
  private Date nextWateringDate;
  private Integer wateringInterval;
  private Date lastCaringDate;
  private Date nextCaringDate;
  private Integer caringInterval;
  private User owner;
  private List<User> caretakers;
  private Space space;


  public Long getPlantId() {
    return plantId;
  }

  public void setPlantId(Long plantId) {
    this.plantId = plantId;
  }

  public String getPlantName() {
    return plantName;
  }

  public void setPlantName(String plantName) {
    this.plantName = plantName;
  }

  public String getSpecies() {
    return species;
  }

  public void setSpecies(String species) {
    this.species = species;
  }

  public String getCareInstructions() {
    return careInstructions;
  }

  public void setCareInstructions(String careInstructions) {
    this.careInstructions = careInstructions;
  }

  public Date getLastWateringDate() {
    return lastWateringDate;
  }

  public void setLastWateringDate(Date lastWateringDate) {
    this.lastWateringDate = lastWateringDate;
  }

  public Date getNextWateringDate() {
    return nextWateringDate;
  }

  public void setNextWateringDate(Date nextWateringDate) {
    this.nextWateringDate = nextWateringDate;
  }

  public Integer getWateringInterval() {
    return wateringInterval;
  }

  public void setWateringInterval(Integer wateringInterval) {
    this.wateringInterval = wateringInterval;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public List<User> getCaretakers() {
    return caretakers;
  }

  public void setCaretakers(List<User> caretakers) {
    this.caretakers = caretakers;
  }

  public Space getSpace() {
    return space;
  }

  public void setSpace(Space space) {
    this.space = space;
  }

  public Date getLastCaringDate() {
    return lastCaringDate;
  }

  public void setLastCaringDate(Date lastCaringDate) {
    this.lastCaringDate = lastCaringDate;
  }

  public Date getNextCaringDate() {
    return nextCaringDate;
  }

  public void setNextCaringDate(Date nextCaringDate) {
    this.nextCaringDate = nextCaringDate;
  }

  public Integer getCaringInterval() {
    return caringInterval;
  }

  public void setCaringInterval(Integer caringInterval) {
    this.caringInterval = caringInterval;
  }
}
