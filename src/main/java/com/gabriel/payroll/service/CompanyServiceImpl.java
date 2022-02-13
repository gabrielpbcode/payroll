package com.gabriel.payroll.service;

import com.gabriel.payroll.dto.CompanyDto;
import com.gabriel.payroll.dto.EmployeeDto;
import com.gabriel.payroll.exception.DataAlreadyExistsException;
import com.gabriel.payroll.exception.DataNotFoundException;
import com.gabriel.payroll.exception.MissingDataException;
import com.gabriel.payroll.exception.PatternNotMatchException;
import com.gabriel.payroll.model.Company;
import com.gabriel.payroll.model.Employee;
import com.gabriel.payroll.model.PayrollUser;
import com.gabriel.payroll.repository.CompanyRepository;
import com.gabriel.payroll.repository.PayrollUserRepository;
import com.gabriel.payroll.validator.CompanyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository companyRepository;
  private final PayrollUserRepository payrollUserRepository;
  private final CompanyValidator companyValidator;

  @Override
  public CompanyDto getById(Long id) throws DataNotFoundException {
    Company e = companyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Company not found"));
    CompanyDto eDto = new CompanyDto(e.getId(),
      e.getName(),
      e.getFantasyName(),
      e.getEmail(),
      e.getCnpj(),
      e.getAccountBalance(),
      e.getOwnerId());

    eDto.setEmployees(convertToEmployeesDto(e));

    return eDto;
  }

  @Override
  @Transactional
  public void register(CompanyDto companyDto)
    throws PatternNotMatchException, DataAlreadyExistsException, DataNotFoundException, MissingDataException {
    if (companyValidator.validateCompanyRegister(companyDto)) {
      if (companyRepository.existsByCnpj(companyDto.getCnpj())) {
        throw new DataAlreadyExistsException("CNPJ already exists");
      }

      Company newCompany = companyRepository.save(setFields(companyDto));
      PayrollUser pu = payrollUserRepository.findById(companyDto.getPayrollUserId())
        .orElseThrow(() -> new DataNotFoundException("User not found"));

      pu.getCompanies().add(newCompany);
    } else {
      throw new PatternNotMatchException("CNPJ pattern does not match with the requirements");
    }
  }

  private Company setFields(CompanyDto dto) {
    Company company = new Company(dto.getName(), dto.getFantasyName(), dto.getCnpj(), dto.getEmail(), dto.getPayrollUserId());
    company.setAccountBalance(dto.getAccountBalance());

    return company;
  }

  @Override
  public Double getBalanceById(Long companyId) throws DataNotFoundException {
    return companyRepository.findById(companyId)
      .map(Company::getAccountBalance)
      .orElseThrow(() -> new DataNotFoundException("It was not possible to find the company"));
  }

  @Override
  public List<CompanyDto> getAllCompanies() {
    return companyRepository.findAll().stream()
      .map(ent -> {
        CompanyDto enDto = new CompanyDto(
          ent.getId(),
          ent.getName(),
          ent.getFantasyName(),
          ent.getEmail(),
          ent.getCnpj(),
          ent.getAccountBalance(),
          ent.getOwnerId());

        enDto.setEmployees(convertToEmployeesDto(ent));

        return enDto;
      }).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void processPayroll(Long companyId) throws DataNotFoundException {
    Optional<Company> companyOpt = companyRepository.findById(companyId);

    if (companyOpt.isPresent()) {
      Company company = companyOpt.get();
      Set<Employee> employees = company.getEmployees();

      Double totalWage = employees.stream().map(Employee::getWage).reduce(0.0, Double::sum);
      Double reducedBalance = totalWage + (totalWage * 0.038);
      Double balance = company.getAccountBalance();

      company.setAccountBalance(balance - reducedBalance);

      for (Employee emp : employees) {
        PayrollUser pu = payrollUserRepository.findByCpf(emp.getCpf())
          .orElseThrow(() -> new DataNotFoundException("User not found"));

        Double actualBalance = pu.getAccount().getBalance();
        pu.getAccount().setBalance(actualBalance + emp.getWage());
      }
    } else {
      throw new DataNotFoundException("It was not possible to find the company. The payroll will not be processed");
    }
  }

  public Set<EmployeeDto> convertToEmployeesDto(Company ent) {
    return ent.getEmployees().stream()
      .map(emp ->
        new EmployeeDto(
          emp.getId(),
          emp.getFirstName(),
          emp.getLastName(),
          emp.getCpf(),
          emp.getBirthday(),
          emp.getEmail(),
          emp.getReferenceAccount(),
          emp.getReferenceAgency(),
          emp.getWage(),
          ent.getId())
      ).collect(Collectors.toSet());
  }
}
