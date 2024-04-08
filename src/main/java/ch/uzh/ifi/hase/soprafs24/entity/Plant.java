package ch.uzh.ifi.hase.soprafs24.entity;


import javax.persistence.*;
import java.io.Serializable;
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

  @Id
  @GeneratedValue
  private Long plantId;

  @Column(nullable = false)
  private String name;

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

  @OneToOne(mappedBy = "id")
  private User owner;

  @ManyToMany(mappedBy = "id")
  private List<User> caretakers;


  public Long getPlantId() {
    return plantId;
  }

  public void setPlantId(Long plantId) {
    this.plantId = plantId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
