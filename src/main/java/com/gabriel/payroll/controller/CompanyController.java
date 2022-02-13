package com.gabriel.payroll.controller;

import com.gabriel.payroll.controller.advisor.ObjectExceptionHandler;
import com.gabriel.payroll.dto.CompanyDto;
import com.gabriel.payroll.exception.DataAlreadyExistsException;
import com.gabriel.payroll.exception.DataNotFoundException;
import com.gabriel.payroll.exception.MissingDataException;
import com.gabriel.payroll.exception.PatternNotMatchException;
import com.gabriel.payroll.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController extends ObjectExceptionHandler {
  private final CompanyService companyService;

  @GetMapping
  public List<CompanyDto> getAllEnterprises() {
    return companyService.getAllCompanies();
  }

  @PutMapping("/processPayroll/{id}")
  public ResponseEntity<Object> processPayroll(@PathVariable("id") Long companyId) throws DataNotFoundException {
    companyService.processPayroll(companyId);
    return new ResponseEntity<>("Payroll processed with success", HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody CompanyDto company)
    throws DataNotFoundException, DataAlreadyExistsException, PatternNotMatchException, MissingDataException {
    companyService.register(company);
    return new ResponseEntity<>("Company registered with success", HttpStatus.OK);
  }

  @GetMapping("/getBalance/{id}")
  public ResponseEntity<Double> getBalance(@PathVariable("id") Long companyId) throws DataNotFoundException {
    return new ResponseEntity<>(companyService.getBalanceById(companyId), HttpStatus.OK);
  }

  @GetMapping("/getCompany/{id}")
  public ResponseEntity<CompanyDto> getById(@PathVariable("id") Long companyId) throws DataNotFoundException {
    return new ResponseEntity<>(companyService.getById(companyId), HttpStatus.OK);
  }
}
