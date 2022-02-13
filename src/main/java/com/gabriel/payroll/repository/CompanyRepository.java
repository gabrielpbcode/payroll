package com.gabriel.payroll.repository;

import com.gabriel.payroll.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

  @Query(
    "SELECT new com.gabriel.payroll.dto.CompanyDto(e.id, e.name, e.fantasyName, e.email, e.cnpj, e.accountBalance, e.ownerId) " +
    "FROM Company e")
  List<Company> findAllByProjectedDto();

  boolean existsByCnpj(String cnpj);

  Optional<Company> findByCnpj(String cnpj);
}
