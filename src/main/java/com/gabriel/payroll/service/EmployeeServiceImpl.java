package com.gabriel.payroll.service;

import com.gabriel.payroll.dto.EmployeeDto;
import com.gabriel.payroll.exception.DataAlreadyExistsException;
import com.gabriel.payroll.exception.DataNotFoundException;
import com.gabriel.payroll.exception.MissingDataException;
import com.gabriel.payroll.exception.PatternNotMatchException;
import com.gabriel.payroll.model.Company;
import com.gabriel.payroll.model.Employee;
import com.gabriel.payroll.repository.CompanyRepository;
import com.gabriel.payroll.repository.EmployeeRepository;
import com.gabriel.payroll.validator.EmployeeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final PayrollUserService payrollUserService;
  private final CompanyRepository companyRepository;
  private final EmployeeValidator employeeValidator;

  @Override
  @Transactional
  public void register(EmployeeDto emp)
    throws PatternNotMatchException, DataAlreadyExistsException, MissingDataException, DataNotFoundException {
    if (employeeValidator.validateEmployeeRegister(emp)) {
      if (employeeRepository.existsByCpf(emp.getCpf())) {
        throw new DataAlreadyExistsException("CPF already exists");
      }

      if (employeeRepository.existsByEmail(emp.getEmail())) {
        throw new DataAlreadyExistsException("Email already exists");
      }

      Employee employee = employeeRepository.save(new Employee(
        emp.getFirstName(),
        emp.getLastName(),
        emp.getCpf(),
        emp.getBirthday(),
        emp.getEmail(),
        emp.getWage()));

      Company ent = companyRepository.findById(emp.getCompanyId())
        .orElseThrow(() -> new DataNotFoundException("Company not found"));

      ent.getEmployees().add(employee);
    } else {
      throw new PatternNotMatchException("CPF pattern does not match with the requirements");
    }
  }

  @Override
  public Double getBalanceByEmployeeId(Long employeeId) throws DataNotFoundException {
    Optional<Employee> employee = employeeRepository.findById(employeeId);

    return employee.isPresent()
      ? payrollUserService.getByCpf(employee.get().getCpf()).getAccount().getBalance()
      : null;
  }

  @Override
  public Set<Employee> getAllByCompanyId(Long companyId) {
    return employeeRepository.findallProjectedByEnterpriseId(companyId);
  }

  @Override
  public List<EmployeeDto> getAll() {
    return employeeRepository.findAll().stream()
      .map(e -> new EmployeeDto(
        e.getId(),
        e.getFirstName(),
        e.getLastName(),
        e.getCpf(),
        e.getBirthday(),
        e.getEmail(),
        e.getReferenceAccount(),
        e.getReferenceAgency(),
        e.getWage(),
        e.getCompanyId()))
      .collect(Collectors.toList());
  }
}
