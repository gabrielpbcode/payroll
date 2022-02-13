package com.gabriel.payroll.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class AccountDto {
  private final String accountNumber;

  private final String agency;

  private final Double accountBalance;

}
