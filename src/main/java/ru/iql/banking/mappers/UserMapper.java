package ru.iql.banking.mappers;

import org.mapstruct.Mapper;
import ru.iql.banking.dtos.UserDto;
import ru.iql.banking.models.BankingUser;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, EmailMapper.class, PhoneMapper.class})
public interface UserMapper {

    UserDto userToUserDto(BankingUser bankingUser);

    BankingUser userDtoToUser(UserDto userDto);
}