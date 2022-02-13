package com.gabriel.payroll.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
public class PayrollUser extends BasicUser implements Serializable {

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(columnDefinition = "boolean default true")
  private boolean active;

  @Column(columnDefinition = "boolean default false")
  private boolean blocked;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastLoginDate;

  private Date lastLoginDateDisplay;

  @ManyToMany
  @Fetch(FetchMode.SUBSELECT)
  private Set<Authority> authorities = Set.of();

  @OneToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @OneToMany
  private Set<Company> companies = Set.of();

}
