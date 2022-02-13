package com.gabriel.payroll.repository;

import com.gabriel.payroll.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findEmployeeByFirstName(String firstName);

  @Query("SELECT DISTINCT e FROM Employee e WHERE e.companyId = :companyId")
  Set<Employee> findallProjectedByEnterpriseId(@Param("companyId") Long companyId);

  boolean existsByCpf(String cpf);

  boolean existsByEmail(String email);
}
