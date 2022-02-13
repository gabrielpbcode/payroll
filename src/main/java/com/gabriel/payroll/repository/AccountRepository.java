package com.gabriel.payroll.repository;

import com.gabriel.payroll.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findByAccountNumberAndAgencyNumber(String accountNumber, String agencyNumber);
}
