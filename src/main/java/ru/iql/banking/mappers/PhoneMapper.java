package ru.iql.banking.mappers;

import org.mapstruct.Mapper;
import ru.iql.banking.dtos.PhoneDto;
import ru.iql.banking.models.PhoneData;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    PhoneDto phoneDataToPhoneDto(PhoneData phoneData);

    PhoneData phoneDtoToPhoneData(PhoneDto phoneDto);
}
