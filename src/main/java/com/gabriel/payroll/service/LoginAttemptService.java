package com.gabriel.payroll.service;

public interface LoginAttemptService {

  void evictUserFromLoginAttemptCache(String username);

  void addUserToLoginAttemptCache(String username);

  boolean hasExceededMaxAttempts(String username);
}
