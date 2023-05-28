package ru.iql.banking.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.iql.banking.repositories.AccountRepository;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl {

    private final AccountRepository accountRepository;
    private static final Integer RAISE_LIMIT = 207;
    private static final Integer  COEF = 10;

    @Async
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateBalance(){
        accountRepository.updateAccountBalance(RAISE_LIMIT, COEF);
    }

}
