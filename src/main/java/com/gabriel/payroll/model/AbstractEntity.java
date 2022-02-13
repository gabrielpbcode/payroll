package com.gabriel.payroll.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JsonIgnore
  @Version
  private Long version;

  @JsonIgnore
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @JsonIgnore
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @PrePersist
  public void createdAt() {
    Calendar c = Calendar.getInstance();
    Date time = c.getTime();

    createdAt = time;
    updatedAt = time;
  }

  @PreUpdate
  public void preUpdate() {
    Calendar c = Calendar.getInstance();
    Date time = c.getTime();

    updatedAt = time;
  }
}
