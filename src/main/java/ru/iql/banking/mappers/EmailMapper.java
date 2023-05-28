package ru.iql.banking.mappers;

import org.mapstruct.Mapper;
import ru.iql.banking.dtos.EmailDto;
import ru.iql.banking.models.EmailData;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    EmailDto emailDataToEmailDto(EmailData emailData);

    EmailData emailDtoToEmailData(EmailDto emailDto);
}
