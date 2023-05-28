package ru.iql.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iql.banking.models.BankingUser;
import ru.iql.banking.models.EmailData;

import java.util.Optional;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    Optional<EmailData> findByEmailIgnoreCase(String email);

    Long countAllByUser(BankingUser user);
}
