package ru.iql.banking.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.iql.banking.dtos.EmailDto;
import ru.iql.banking.dtos.PhoneDto;
import ru.iql.banking.dtos.UserDto;
import ru.iql.banking.models.BankingUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface UserService {

    Page<UserDto> getUsers (LocalDate localDate, String phone, String name, String email, Pageable pageable);

    EmailDto addEmail(Long userId, String email);

    EmailDto changeEmail(Long userId, Long emailId, String newEmail);

    void deleteEmail(Long userId, Long emailId);

    PhoneDto addPhone(Long userId, String phone);

    PhoneDto changePhone(Long userId, Long phoneId, String newPhone);

    void deletePhone(Long userId, Long phoneId);

    BankingUser getUserByEmailOrPhone (String userData);

    List<String> getPhonesAndEmailsByUserId(String userId);

    void moneyTransfer(Long transferFrom, Long transferTo, BigDecimal value);
}
