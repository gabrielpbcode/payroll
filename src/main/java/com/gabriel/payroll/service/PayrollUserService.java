package com.gabriel.payroll.service;

import com.gabriel.payroll.exception.EmailExistException;
import com.gabriel.payroll.exception.EmailNotFoundException;
import com.gabriel.payroll.exception.UserNotFoundException;
import com.gabriel.payroll.exception.UsernameExistException;
import com.gabriel.payroll.model.PayrollUser;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface PayrollUserService {
  List<PayrollUser> getPayrollUsers();

  PayrollUser getById(Long id);

  PayrollUser register(PayrollUser user) throws UserNotFoundException, EmailExistException, UsernameExistException;

  PayrollUser getByUsername(String username);

  PayrollUser getByCpf(String cpf);

  PayrollUser addNewPayrollUser(PayrollUser user) throws UserNotFoundException, EmailExistException, UsernameExistException;

  PayrollUser updatePayrollUser(PayrollUser user) throws UserNotFoundException, EmailExistException, UsernameExistException;

  void resetPassword(String email) throws EmailNotFoundException;

  HttpHeaders getJwtHeader(PayrollUser user);
}
