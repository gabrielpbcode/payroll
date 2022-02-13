package com.gabriel.payroll.validator;

import com.gabriel.payroll.dto.EmployeeDto;
import com.gabriel.payroll.exception.MissingDataException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeValidator {
  public boolean validateEmployeeRegister(EmployeeDto e) throws MissingDataException {
    List<String> errors = new ArrayList<>();

    if (e != null) {
      Pattern pattern = Pattern.compile("^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$");
      Matcher matcher = pattern.matcher(e.getCpf());

      if (!matcher.matches()) {
        errors.add("CPF pattern does not match with the requirements");
      }

      if (e.getFirstName().isEmpty()) {
        errors.add("Employee first name cannot be empty");
      }

      if (e.getLastName().isEmpty()) {
        errors.add("Employee last name cannot be empty");
      }

      if (e.getEmail().isEmpty()) {
        errors.add("Employee email cannot be empty");
      }

      if (e.getBirthday() == null) {
        errors.add("Birthday cannot be null");
      }

      if (e.getWage() < 0) {
        errors.add("Wage cannot be less than zero");
      }

      if (errors.size() > 0) {
        throw new MissingDataException(String.join("\n", errors));
      }

      return true;
    }

    return false;
  }
}
