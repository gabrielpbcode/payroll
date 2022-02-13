package com.gabriel.payroll.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "authorityNameAndDescription", columnNames = { "Name", "Description" }))
public class Authority extends AbstractEntity implements GrantedAuthority {

  private String name;

  private String description;

  @Override
  public String getAuthority() {
    return name;
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", description, name);
  }
}
