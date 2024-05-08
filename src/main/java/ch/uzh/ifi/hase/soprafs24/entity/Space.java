package ch.uzh.ifi.hase.soprafs24.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SPACE")
public class Space {
  private static final long serialVersionUID = 1L;

  /**
   * Fields
   */

  @Id
  @GeneratedValue
  private Long spaceId;

  @Column(nullable = false)
  private String spaceName;


  /**
   * Relations
   */

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User spaceOwner;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "space", orphanRemoval = false, cascade = CascadeType.ALL)
  private List<Plant> plantsContained = new ArrayList<>();


  /**
   * getters and setters
   */

  public Long getSpaceId() {
    return spaceId;
  }

  public void setSpaceId(Long spaceId) {
    this.spaceId = spaceId;
  }

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
