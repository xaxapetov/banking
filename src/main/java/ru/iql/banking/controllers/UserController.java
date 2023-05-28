package ru.iql.banking.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.iql.banking.dtos.PhoneDto;
import ru.iql.banking.services.impl.JwtServiceImpl;
import ru.iql.banking.dtos.EmailDto;
import ru.iql.banking.dtos.UserDto;
import ru.iql.banking.services.UserService;
import ru.iql.banking.validators.UsersValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.iql.banking.utils.Constants.PHONE_PATTERN;


@RestController
@RequestMapping("/banking/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final JwtServiceImpl jwtServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить всех пользователей", security = @SecurityRequirement(name = "basicAuth"))
    public Page<UserDto> getUsers(@RequestParam(name = "dateOfBirth", required = false) LocalDate localDate,
                                  @RequestParam(name = "phone", required = false) @Pattern(regexp = PHONE_PATTERN) String phone,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "email", required = false) @Email String email,
                                  @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {

        UsersValidator.requestParamsOneOrMoreNonNull(localDate, phone, name, email);
        return userService.getUsers(localDate, phone, name, email, pageable);
    }


    @PostMapping("/email")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить email", security = @SecurityRequirement(name = "basicAuth"))
    public EmailDto addEmail(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token,
                             @RequestParam(name = "email") @Email String email) {

        Long userId = getUserIdByToken(token);
        return userService.addEmail(userId, email);
    }

    @PatchMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить email по заданному id", security = @SecurityRequirement(name = "basicAuth"))
    public EmailDto changeEmail(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token,
                                @RequestParam(name = "emailId") Long emailId,
                                @RequestParam(name = "newEmail") @Email String newEmail) {

        Long userId = getUserIdByToken(token);
        return userService.changeEmail(userId,emailId, newEmail);
    }

    @DeleteMapping("/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить email по заданному id", security = @SecurityRequirement(name = "basicAuth"))
    public void deleteEmail(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token,
                            @RequestParam(name = "emailId") Long emailId) {

        Long userId = getUserIdByToken(token);
        userService.deleteEmail(userId, emailId);
    }

    @PostMapping("/phone")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить номер телефона", security = @SecurityRequirement(name = "basicAuth"))
    public PhoneDto addPhone(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token,
                             @RequestParam(name = "phone") String phone) {

        Long userId = getUserIdByToken(token);
        return userService.addPhone(userId, phone);
    }

    @PatchMapping("/phone")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить номер телефона по заданному id", security = @SecurityRequirement(name = "basicAuth"))
    public PhoneDto changePhone(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token,
                                @RequestParam(name = "phoneId") Long phoneId,
                                @RequestParam(name = "newPhone") String newPhone) {

        Long userId = getUserIdByToken(token);
        return userService.changePhone(userId, phoneId, newPhone);
    }

    @DeleteMapping("/phone")
    @Operation(summary = "Удалить номер телефона по заданному id", security = @SecurityRequirement(name = "basicAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhone(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token,
                            @RequestParam(name = "phoneId") Long phoneId) {

        Long userId = getUserIdByToken(token);
        userService.deletePhone(userId, phoneId);
    }

    @PostMapping("/money-transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Перевод денег от одного пользователя другому", security = @SecurityRequirement(name = "basicAuth"))
    public void moneyTransfer(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String token,
                                @RequestParam("transfer_to") @NonNull Long transferTo,
                                @RequestParam(name = "value") @NonNull BigDecimal value) {

        Long transferFrom = getUserIdByToken(token);
        userService.moneyTransfer(transferFrom, transferTo, value);
    }

    private Long getUserIdByToken(String token){
        String userId = jwtServiceImpl.extractUserId(token.substring(7));
        return Long.parseLong(userId);
    }
}
