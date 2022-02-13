package com.gabriel.payroll.validator;

import com.gabriel.payroll.dto.CompanyDto;
import com.gabriel.payroll.exception.MissingDataException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CompanyValidator {
  public boolean validateCompanyRegister(CompanyDto e) throws MissingDataException {
    if (e != null) {
      List<String> errors = new ArrayList<>();

      Pattern pattern = Pattern.compile("^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$");
      Matcher matcher = pattern.matcher(e.getCnpj());

      if (!matcher.matches()) {
        errors.add("CNPJ pattern does not match with the requirements");
      }

      if (e.getName().isEmpty()) {
        errors.add("Enterprise name cannot be empty");
      }

      if (e.getFantasyName().isEmpty()) {
        errors.add("Fantasy name cannot be empty");
      }

      if (e.getCnpj().isEmpty()) {
        errors.add("CNPJ cannot be empty");
      }

      if (errors.size() > 0) {
        throw new MissingDataException(String.join("\n", errors));
      }

      return true;
    }

    return false;
  }
}
