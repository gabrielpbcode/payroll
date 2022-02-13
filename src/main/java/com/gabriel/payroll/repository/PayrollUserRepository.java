package com.gabriel.payroll.repository;

import com.gabriel.payroll.model.PayrollUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollUserRepository extends JpaRepository<PayrollUser, Long> {

  Optional<PayrollUser> findByUsername(String username);

  Optional<PayrollUser> findByEmail(String email);

  Optional<PayrollUser> findByCpf(String cpf);
}
