package com.gabriel.payroll.service;

import com.gabriel.payroll.dto.EmployeeDto;
import com.gabriel.payroll.exception.DataAlreadyExistsException;
import com.gabriel.payroll.exception.DataNotFoundException;
import com.gabriel.payroll.exception.MissingDataException;
import com.gabriel.payroll.exception.PatternNotMatchException;
import com.gabriel.payroll.model.Employee;

import java.util.List;
import java.util.Set;

public interface EmployeeService {
  void register(EmployeeDto employee) throws PatternNotMatchException, DataAlreadyExistsException, DataNotFoundException, MissingDataException;

  Double getBalanceByEmployeeId(Long employeeId) throws DataNotFoundException;

  Set<Employee> getAllByCompanyId(Long companyId);

  List<EmployeeDto> getAll();
}
