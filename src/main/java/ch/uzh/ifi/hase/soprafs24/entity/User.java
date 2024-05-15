package ch.uzh.ifi.hase.soprafs24.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "user_table")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column()
  private String token;

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  /**
   * Relations
   */

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Plant> plantsOwned = new ArrayList<>();

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "caretakers")
  private List<Plant> plantsCaredFor = new ArrayList<>();

  @JsonIgnore
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "spaceOwner", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Space> spacesOwned = new ArrayList<>();

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "spaceMembers")
  private List<Space> spaceMemberships = new ArrayList<>();

  /**
   * getters and setters
   */

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public List<Space> getSpacesOwned() {
    return spacesOwned;
  }

  public void setSpacesOwned(List<Space> spacesOwned) {
    this.spacesOwned = spacesOwned;
  }

  public List<Space> getSpaceMemberships() {
    return spaceMemberships;
  }

  public void setSpacesMemberships(List<Space> spaceMemberships) {
    this.spaceMemberships = spaceMemberships;
  }
}
