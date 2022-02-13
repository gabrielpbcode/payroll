package com.gabriel.payroll.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends AbstractEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String fantasyName;

  @Column(unique = true, nullable = false)
  private String cnpj;

  @Column(nullable = false)
  private String email;

  @Column(columnDefinition = "real default 0.0")
  private Double accountBalance;

  @OneToMany
  private Set<Employee> employees;

  @Column(name = "payroll_user_id", insertable = false, updatable = false)
  private Long ownerId;

  public Company(String name, String fantasyName, String cnpj, String email, Long ownerId) {
    this.name = name;
    this.fantasyName = fantasyName;
    this.cnpj = cnpj;
    this.email = email;
    this.ownerId = ownerId;
  }
}
