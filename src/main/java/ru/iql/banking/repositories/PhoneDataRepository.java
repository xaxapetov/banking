package ru.iql.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iql.banking.models.BankingUser;
import ru.iql.banking.models.PhoneData;

import java.util.Optional;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    Optional<PhoneData> findByPhone(String phone);

    Long countAllByUser(BankingUser user);
}
