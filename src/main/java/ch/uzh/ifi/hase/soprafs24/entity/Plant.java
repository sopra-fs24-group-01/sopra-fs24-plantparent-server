package ch.uzh.ifi.hase.soprafs24.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "PLANT")
public class Plant implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Fields
   */

  @Id
  @GeneratedValue
  private Long plantId;
  @Column(nullable = false)
  private String plantName;
  @Column()
  private String species;
  @Column()
  private String careInstructions;
  @Column()
  private Date lastWateringDate;

  @Column()
  private Date nextWateringDate;

  @Column()
  private Integer wateringInterval;

  /**
   * Relations
   */

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User owner;

  @ManyToMany
  @JoinColumn(name = "plantsCaredFor")
  @JsonIgnore
  private List<User> caretakers = new ArrayList<>();

  // calculate the next watering date
  public Calendar calculateAndSetNextWateringDate() {
    Calendar nextDate = Calendar.getInstance();
    nextDate.add(Calendar.DATE, this.wateringInterval);
    // Clean up the current time
    nextDate.set(Calendar.HOUR_OF_DAY, 0);
    nextDate.set(Calendar.MINUTE, 0);
    nextDate.set(Calendar.SECOND, 0);
    nextDate.set(Calendar.MILLISECOND, 0);

    this.setNextWateringDate(nextDate.getTime());

    return nextDate;
  }

  // Getters and setters
  public Long getPlantId() {
    return plantId;
  }

  public String getPlantName() {
    return plantName;
  }

  public void setPlantName(String name) {
    this.plantName = name;
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
}
