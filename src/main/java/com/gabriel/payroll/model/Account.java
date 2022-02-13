package com.gabriel.payroll.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Account extends AbstractEntity {

  @Column(unique = true, nullable = false)
  private String accountNumber;

  @Column(nullable = false)
  private String agencyNumber;

  private Double balance;

  @OneToOne(mappedBy = "account")
  private PayrollUser payrollUser;
}
