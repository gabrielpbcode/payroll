package com.gabriel.payroll.service;

import com.gabriel.payroll.model.PayrollUser;
import com.gabriel.payroll.repository.PayrollUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

  private Logger LOGGER = LoggerFactory.getLogger(getClass());
  private final PayrollUserRepository payrollUserRepository;

  @Override
  @Transactional(rollbackOn = Exception.class)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    PayrollUser payrollUser = payrollUserRepository.findByUsername(username)
      .orElseThrow(() -> {
        String message = "It was not possible to find this user";
        LOGGER.error(message);
        throw new UsernameNotFoundException(message);
      });

    payrollUser.setLastLoginDateDisplay(payrollUser.getLastLoginDate());
    payrollUser.setLastLoginDate(new Date());

    List<GrantedAuthority> authorities = payrollUser.getAuthorities().stream()
      .map(auth -> new SimpleGrantedAuthority(auth.getName()))
      .collect(Collectors.toList());

    UserDetails user = new User(username,
      payrollUser.getPassword(),
      payrollUser.isActive(),
      true,
      true,
      !payrollUser.isBlocked(),
      authorities);

    LOGGER.info(String.format("Returning found user by username: %s", username));

    return user;
  }
}
