package com.gabriel.payroll.controller;

import com.gabriel.payroll.controller.advisor.PayrollUserExceptionHandler;
import com.gabriel.payroll.exception.EmailExistException;
import com.gabriel.payroll.exception.UserNotFoundException;
import com.gabriel.payroll.exception.UsernameExistException;
import com.gabriel.payroll.model.PayrollUser;
import com.gabriel.payroll.service.PayrollUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/", "/user" })
@RequiredArgsConstructor
public class PayrollUserController extends PayrollUserExceptionHandler {

  private final PayrollUserService payrollUserService;
  private final AuthenticationManager authenticationManager;

  @GetMapping("/home")
  public String showUser() {
    return "application works";
  }

  @GetMapping
  public List<PayrollUser> getAll() {
    return payrollUserService.getPayrollUsers();
  }

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestBody PayrollUser user) throws UsernameNotFoundException {
    authenticate(user.getUsername(), user.getPassword());
    PayrollUser loginUser = payrollUserService.getByUsername(user.getUsername());
    HttpHeaders jwtHeader = payrollUserService.getJwtHeader(loginUser);
    return new ResponseEntity<>(loginUser, jwtHeader, OK);
  }

  @PostMapping("/register")
  public ResponseEntity<PayrollUser> register(@RequestBody PayrollUser user)
    throws UserNotFoundException, EmailExistException, UsernameExistException {

    PayrollUser newUser = payrollUserService.register(user);
    return new ResponseEntity<>(newUser, OK);
  }

  private void authenticate(String username, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }

}
