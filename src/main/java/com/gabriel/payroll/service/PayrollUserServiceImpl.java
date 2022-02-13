package com.gabriel.payroll.service;

import com.gabriel.payroll.exception.EmailExistException;
import com.gabriel.payroll.exception.EmailNotFoundException;
import com.gabriel.payroll.exception.UserNotFoundException;
import com.gabriel.payroll.exception.UsernameExistException;
import com.gabriel.payroll.model.PayrollUser;
import com.gabriel.payroll.repository.PayrollUserRepository;
import com.gabriel.payroll.security.tokenprovider.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.gabriel.payroll.security.SecurityConstants.JWT_TOKEN_HEADER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@RequiredArgsConstructor
public class PayrollUserServiceImpl implements PayrollUserService {

  public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
  public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
  public static final String NO_USER_FOUND_BY_USERNAME = "No user found by username: ";
  public static final String NO_USER_FOUND_BY_EMAIL = "No user found for email: ";

  private Logger LOGGER = LoggerFactory.getLogger(getClass());

  private final PayrollUserRepository payrollUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTTokenProvider tokenProvider;

  public List<PayrollUser> getPayrollUsers() {
    return payrollUserRepository.findAll();
  }

  @Override
  public PayrollUser getById(Long id) {
    return payrollUserRepository.findById(id)
      .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username: %s not found")));
  }

  @Override
  @Transactional(rollbackOn = { UsernameNotFoundException.class, EmailExistException.class, UsernameExistException.class })
  public PayrollUser register(PayrollUser user) throws UserNotFoundException, EmailExistException, UsernameExistException {
    validateNewUsernameAndEmail(EMPTY, user.getUsername(), user.getEmail());

    PayrollUser newUser = new PayrollUser();
    newUser.setFirstName(user.getFirstName());
    newUser.setLastName(user.getLastName());
    newUser.setUsername(user.getUsername());
    newUser.setEmail(user.getEmail());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setActive(true);

    LOGGER.info(String.format("New user password: %s", newUser.getPassword()));

    return newUser;
  }

  @Override
  public PayrollUser getByUsername(String username) {
    return payrollUserRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username: %s, not found", username)));
  }

  @Override
  public PayrollUser getByCpf(String cpf) {
    return payrollUserRepository.findByCpf(cpf)
      .orElseThrow(() -> new UsernameNotFoundException(String.format("User with cpf: %s, not found", cpf)));
  }

  @Override
  @Transactional(rollbackOn = { UsernameNotFoundException.class, EmailExistException.class, UsernameExistException.class })
  public PayrollUser addNewPayrollUser(PayrollUser user) throws UserNotFoundException, EmailExistException, UsernameExistException {
    validateNewUsernameAndEmail(EMPTY, user.getUsername(), user.getEmail());

    PayrollUser newUser = new PayrollUser();
    newUser.setFirstName(user.getFirstName());
    newUser.setLastName(user.getLastName());
    newUser.setUsername(user.getUsername());
    newUser.setEmail(user.getEmail());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setActive(true);

    return newUser;
  }

  @Override
  @Transactional(rollbackOn = { UsernameNotFoundException.class, EmailExistException.class, UsernameExistException.class })
  public PayrollUser updatePayrollUser(PayrollUser user) throws UserNotFoundException, EmailExistException, UsernameExistException {
    PayrollUser currentUser = validateNewUsernameAndEmail(EMPTY, user.getUsername(), user.getEmail());

    currentUser.setFirstName(user.getFirstName());
    currentUser.setLastName(user.getLastName());
    currentUser.setUsername(user.getUsername());
    currentUser.setEmail(user.getEmail());
    currentUser.setActive(user.isActive());

    return currentUser;
  }

  @Override
  public void resetPassword(String email) throws EmailNotFoundException {
    Optional<PayrollUser> user = payrollUserRepository.findByEmail(email);
    
    if (!user.isPresent()) {
      throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
    }

    String password = generatePassword();
    user.get().setPassword(passwordEncoder.encode(password));
    LOGGER.info("New user password: " + password);

    payrollUserRepository.save(user.get());
  }

  @Override
  public HttpHeaders getJwtHeader(PayrollUser loginUser) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(JWT_TOKEN_HEADER, tokenProvider.generateJwtToken(loginUser));
    return headers;
  }

  private String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(10);
  }

  private PayrollUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
    throws UserNotFoundException, UsernameExistException, EmailExistException {

    Optional<PayrollUser> userByNewUsername = payrollUserRepository.findByUsername(newUsername);
    Optional<PayrollUser> userByNewEmail = payrollUserRepository.findByEmail(newEmail);

    if (StringUtils.isNotBlank(currentUsername)) {
      Optional<PayrollUser> currentUser = payrollUserRepository.findByUsername(currentUsername);

      if (!currentUser.isPresent()) {
        throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
      }

      if (userByNewUsername.isPresent() && !currentUser.get().getId().equals(userByNewUsername.get().getId())) {
        throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
      }

      if (userByNewEmail.isPresent() && !currentUser.get().getId().equals(userByNewEmail.get().getId())) {
        throw new EmailExistException(EMAIL_ALREADY_EXISTS);
      }

      return currentUser.get();
    } else {
      if (userByNewUsername.isPresent()) {
        throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
      }

      if (userByNewEmail.isPresent()) {
        throw new EmailExistException(EMAIL_ALREADY_EXISTS);
      }

      return null;
    }
  }
}
