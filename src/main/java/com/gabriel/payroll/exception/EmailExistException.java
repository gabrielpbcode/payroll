package com.gabriel.payroll.exception;

public class EmailExistException extends Exception {
  public EmailExistException(String message) {
    super(message);
  }
}
