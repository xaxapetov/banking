package ru.iql.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import ru.iql.banking.models.Account;

import static ru.iql.banking.utils.Constants.UPDATE_BALANCE_IN_ACCOUNT_FUNCTION;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Procedure(UPDATE_BALANCE_IN_ACCOUNT_FUNCTION)
    Boolean updateAccountBalance(Integer raiseLimit, Integer coef);
}
