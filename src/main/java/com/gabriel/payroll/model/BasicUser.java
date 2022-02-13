package com.gabriel.payroll.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BasicUser extends AbstractEntity {

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true)
  private String cpf;

  @Column(nullable = false, unique = true)
  private String email;

  @Temporal(TemporalType.DATE)
  private Date birthday;
}
