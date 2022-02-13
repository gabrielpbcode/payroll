package com.gabriel.payroll.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Employee extends BasicUser {

  @Column(nullable = false)
  private String referenceAccount;

  @Column(nullable = false)
  private String referenceAgency;

  @Column(nullable = false)
  private String referenceBankCode;

  @Column(columnDefinition = "int8range default 0.0")
  private Double wage;

  @ManyToOne
  private Company company;

  @Column(name = "company_id", updatable = false, insertable = false)
  private Long companyId;

  public Employee(String firstName, String lastName, String cpf, Date birthday, String email, Double wage) {
    super(firstName, lastName, cpf, email, birthday);
    this.wage = wage;
  }
}
