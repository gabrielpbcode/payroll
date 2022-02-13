package com.gabriel.payroll.service;

import com.gabriel.payroll.dto.CompanyDto;
import com.gabriel.payroll.dto.EmployeeDto;
import com.gabriel.payroll.exception.DataAlreadyExistsException;
import com.gabriel.payroll.exception.DataNotFoundException;
import com.gabriel.payroll.exception.MissingDataException;
import com.gabriel.payroll.exception.PatternNotMatchException;
import com.gabriel.payroll.model.Company;

import java.util.List;
import java.util.Set;

public interface CompanyService {
  CompanyDto getById(Long id) throws DataNotFoundException;

  void register(CompanyDto enterprise) throws PatternNotMatchException, DataAlreadyExistsException, DataNotFoundException, MissingDataException;

  Double getBalanceById(Long enterpriseId) throws DataNotFoundException;

  List<CompanyDto> getAllCompanies();

  void processPayroll(Long enterpriseId) throws DataNotFoundException;

  Set<EmployeeDto> convertToEmployeesDto(Company ent);
}
