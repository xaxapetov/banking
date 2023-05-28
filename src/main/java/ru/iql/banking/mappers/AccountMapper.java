package ru.iql.banking.mappers;

import org.mapstruct.Mapper;
import ru.iql.banking.dtos.AccountDto;
import ru.iql.banking.models.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto accountToAccountDto(Account account);

    Account accountDtoToAccount(AccountDto accountDto);
}
