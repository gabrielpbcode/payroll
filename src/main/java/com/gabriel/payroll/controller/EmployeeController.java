package com.gabriel.payroll.controller;

import com.gabriel.payroll.controller.advisor.ObjectExceptionHandler;
import com.gabriel.payroll.dto.EmployeeDto;
import com.gabriel.payroll.exception.DataAlreadyExistsException;
import com.gabriel.payroll.exception.DataNotFoundException;
import com.gabriel.payroll.exception.MissingDataException;
import com.gabriel.payroll.exception.PatternNotMatchException;
import com.gabriel.payroll.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController extends ObjectExceptionHandler {
  private final EmployeeService employeeService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody EmployeeDto employee)
    throws DataNotFoundException, DataAlreadyExistsException, PatternNotMatchException, MissingDataException {
    employeeService.register(employee);
    return new ResponseEntity<>("Employee registered with success", HttpStatus.OK);
  }

  @GetMapping("/getBalance/{id}")
  public ResponseEntity<Double> getBalance(@PathVariable("id") Long id) throws DataNotFoundException {
    return new ResponseEntity<>(employeeService.getBalanceByEmployeeId(id), HttpStatus.OK);
  }

  @GetMapping
  public List<EmployeeDto> getAllEmployees() {
    return employeeService.getAll();
  }
}