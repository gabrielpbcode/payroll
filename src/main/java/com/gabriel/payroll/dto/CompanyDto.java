package com.gabriel.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
@RequiredArgsConstructor
public class CompanyDto {
  private final Long id;

  private final String name;

  private final String fantasyName;

  private final String cnpj;

  private final String email;

  private final Double accountBalance;

  private final Long payrollUserId;

  private Set<EmployeeDto> employees = Set.of();

  public CompanyDto(Long id, String name, String fantasyName, String cnpj, String email, Double accountBalance, Long payrollUserId, Set<EmployeeDto> employees) {
    this.id = id;
    this.name = name;
    this.fantasyName = fantasyName;
    this.cnpj = cnpj;
    this.email = email;
    this.accountBalance = accountBalance;
    this.payrollUserId = payrollUserId;
    this.employees = employees;
  }
}
