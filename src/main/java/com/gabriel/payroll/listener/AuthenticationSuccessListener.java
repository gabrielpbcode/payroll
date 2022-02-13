package com.gabriel.payroll.listener;

import com.gabriel.payroll.model.PayrollUser;
import com.gabriel.payroll.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener {

  private final LoginAttemptService loginAttemptService;

  @EventListener
  public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
    Object principal = event.getAuthentication().getPrincipal();

    if (principal instanceof PayrollUser) {
      PayrollUser user = (PayrollUser) event.getAuthentication().getPrincipal();
      loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
    }
  }
}
