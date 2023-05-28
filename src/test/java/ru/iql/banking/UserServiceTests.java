package ru.iql.banking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.iql.banking.exeptions.BankingUserNotFoundException;
import ru.iql.banking.exeptions.InvalidTransferAmountException;
import ru.iql.banking.models.Account;
import ru.iql.banking.models.BankingUser;
import ru.iql.banking.repositories.UserRepository;
import ru.iql.banking.services.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private static BankingUser bankingUser1;
    private static BankingUser bankingUser2;
    private static BankingUser wrongUser;

    @BeforeAll
    static void init() {
        bankingUser1 = BankingUser.builder()
                .id(1L)
                .account(Account
                        .builder()
                        .balance(new BigDecimal(1000))
                        .build())
                .build();
        bankingUser2 = BankingUser.builder()
                .id(2L)
                .account(Account
                        .builder()
                        .balance(new BigDecimal(1000))
                        .build())
                .build();

        wrongUser = BankingUser.builder()
                .id(3L)
                .account(Account
                        .builder()
                        .balance(new BigDecimal(1000))
                        .build())
                .build();
    }

    @Test
    public void moneyTransfer_AddingWrongToUser_Throw_BankingUserNotFoundException() {
        assertThrows(BankingUserNotFoundException.class, () -> {
            when(userRepository.findById(bankingUser1.getId()))
                    .thenReturn(Optional.of(bankingUser1));
            when(userRepository.findById(wrongUser.getId()))
                    .thenThrow(new BankingUserNotFoundException("Test exception"));
            userService.moneyTransfer(bankingUser1.getId(), wrongUser.getId(), new BigDecimal(500));
        });
    }

    @Test
    public void moneyTransfer_AddingWrongFromUser_Throw_BankingUserNotFoundException() {
        assertThrows(BankingUserNotFoundException.class, () -> {
            when(userRepository.findById(wrongUser.getId()))
                    .thenThrow(new BankingUserNotFoundException("Test exception"));
            when(userRepository.findById(bankingUser1.getId()))
                    .thenReturn(Optional.of(bankingUser1));
            userService.moneyTransfer(wrongUser.getId(), bankingUser1.getId(), new BigDecimal(500));
        });
    }

    @Test
    public void moneyTransfer_AddingWrongValue_Throw_InvalidTransferAmountException() {
        assertThrows(InvalidTransferAmountException.class, () -> {
            when(userRepository.findById(bankingUser1.getId()))
                    .thenReturn(Optional.of(bankingUser1));
            when(userRepository.findById(bankingUser2.getId()))
                    .thenReturn(Optional.of(bankingUser2));
            userService.moneyTransfer(bankingUser1.getId(), bankingUser2.getId(), new BigDecimal(-500));
        });
    }

    @Test
    public void moneyTransfer_AddingAllData_Successfully() {
        when(userRepository.findById(bankingUser1.getId()))
                .thenReturn(Optional.of(bankingUser1));
        when(userRepository.findById(bankingUser2.getId()))
                .thenReturn(Optional.of(bankingUser2));
        userService.moneyTransfer(bankingUser1.getId(), bankingUser2.getId(), new BigDecimal(500));
        verify(userRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).findById(eq(2L));
    }
}