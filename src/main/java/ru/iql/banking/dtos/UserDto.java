package ru.iql.banking.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    private AccountDto account;

    private List<EmailDto> emailData;

    private List<PhoneDto> phoneData;
}
