package ru.iql.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.iql.banking.models.BankingUser;


@Repository
public interface UserRepository extends JpaRepository<BankingUser, Long>, JpaSpecificationExecutor<BankingUser> {
}
