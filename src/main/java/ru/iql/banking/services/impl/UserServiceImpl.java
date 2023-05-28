package ru.iql.banking.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.iql.banking.dtos.EmailDto;
import ru.iql.banking.dtos.PhoneDto;
import ru.iql.banking.dtos.UserDto;
import ru.iql.banking.exeptions.BankingUserNotFoundException;
import ru.iql.banking.exeptions.EmailIsExistException;
import ru.iql.banking.exeptions.EmailNotFoundException;
import ru.iql.banking.exeptions.InvalidTransferAmountException;
import ru.iql.banking.exeptions.OnlyOneInstanceOfData;
import ru.iql.banking.exeptions.PhoneIsExistException;
import ru.iql.banking.exeptions.PhoneNotFoundException;
import ru.iql.banking.mappers.EmailMapper;
import ru.iql.banking.mappers.PhoneMapper;
import ru.iql.banking.mappers.UserMapper;
import ru.iql.banking.models.BankingUser;
import ru.iql.banking.models.EmailData;
import ru.iql.banking.models.PhoneData;
import ru.iql.banking.repositories.EmailDataRepository;
import ru.iql.banking.repositories.PhoneDataRepository;
import ru.iql.banking.repositories.UserRepository;
import ru.iql.banking.repositories.spesification.UserSpecification;
import ru.iql.banking.services.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.iql.banking.utils.Constants.PHONE_PATTERN;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final PhoneMapper phoneMapper;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Page<UserDto> getUsers (LocalDate localDate,
                                   String phone,
                                   String name,
                                   String email,
                                   Pageable pageable){
        log.debug("Start getUsers(localDate = " + localDate
                + ", phone = " + phone
                + ", name = " + name
                + ", email = " + email + ") method");
        Specification<BankingUser> userSpecification = UserSpecification
                .getAllSpecificationForGetUser(localDate, phone, name, email);
        return userRepository.findAll(userSpecification, pageable).map(userMapper::userToUserDto);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EmailDto addEmail(Long userId, String email) {
        log.debug("Start addEmail(userId = " + userId
                + ", email = " + email + ") method");
        boolean isPresentEmail = emailDataRepository.findByEmailIgnoreCase(email).isPresent();
        if(isPresentEmail){
            throw new EmailIsExistException(email + " is exist");
        }
        BankingUser user = findBankingUserById(userId);
        EmailData.builder().user(user).email(email).build();
        EmailData emailData = emailDataRepository.save(EmailData
                .builder()
                .user(user)
                .email(email.toLowerCase())
                .build());
        return emailMapper.emailDataToEmailDto(emailData);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EmailDto changeEmail(Long userId, Long emailId, String newEmail) {
        log.debug("Start changeEmail(userId = " + userId
                + ", emailId = " + emailId
                + ", newEmail = " + newEmail + ") method");
        boolean isPresentEmail = emailDataRepository.findByEmailIgnoreCase(newEmail).isPresent();
        if(isPresentEmail){
            throw new EmailIsExistException(newEmail + " is exist");
        }
        EmailData emailData = findEmailDataById(emailId);
        if(! userId.equals(emailData.getUser().getId())){
            throw new BankingUserNotFoundException("User with id=" + userId + " not found for emailId=" + emailId);
        }
        emailData.setEmail(newEmail);
        return emailMapper.emailDataToEmailDto(emailData);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteEmail(Long userId, Long emailId) {
        log.debug("Start deleteEmail(userId = " + userId
                + ", emailId = " + emailId + ") method");
        long emailsCount = emailDataRepository.countAllByUser(BankingUser
                .builder()
                .id(userId)
                .build());
        EmailData emailData = checkCountData (this::findEmailDataById, emailId, emailsCount, userId);
        emailDataRepository.delete(emailData);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PhoneDto addPhone(Long userId, String phone) {
        log.debug("Start addPhone(userId = " + userId
                + ", phone = " + phone + ") method");
        boolean isPresentPhone = phoneDataRepository.findByPhone(phone.toLowerCase()).isPresent();
        if(isPresentPhone){
            throw new PhoneIsExistException(phone + " is exist");
        }
        BankingUser user = findBankingUserById(userId);
        PhoneData.builder().user(user).phone(phone).build();
        PhoneData phoneData = phoneDataRepository.save(PhoneData
                .builder()
                .user(user)
                .phone(phone)
                .build());
        return phoneMapper.phoneDataToPhoneDto(phoneData);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PhoneDto changePhone(Long userId, Long phoneId, String newPhone) {
        log.debug("Start changePhone(userId = " + userId
                + ", phoneId = " + phoneId
                + ", newPhone = " + newPhone +") method");
        boolean isPresentPhone = phoneDataRepository.findByPhone(newPhone).isPresent();
        if(isPresentPhone){
            throw new PhoneIsExistException(newPhone + " is exist");
        }
        PhoneData phoneData = findPhoneDataById(phoneId);
        if(! userId.equals(phoneData.getUser().getId())){
            throw new BankingUserNotFoundException("User with id=" + userId + " not found for emailId=" + phoneId);
        }
        phoneData.setPhone(newPhone);
        return phoneMapper.phoneDataToPhoneDto(phoneData);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deletePhone(Long userId, Long phoneId) {
        log.debug("Start deletePhone(userId = " + userId
                + ", phoneId = " + phoneId + ") method");
        long phonesCount = phoneDataRepository.countAllByUser(BankingUser
                .builder()
                .id(userId)
                .build());
        PhoneData phoneDAta = checkCountData (this::findPhoneDataById, phoneId, phonesCount, userId);
        phoneDataRepository.delete(phoneDAta);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public BankingUser getUserByEmailOrPhone (String userData) {
        log.debug("Start getUserByEmailOrPhone(userData = " + userData + ") method");
        BankingUser bankingUser;
        if(userData.matches(PHONE_PATTERN)){
            bankingUser = phoneDataRepository
                    .findByPhone(userData).orElseThrow(() -> new UsernameNotFoundException("Username by phone not found!"))
                    .getUser();
        } else{
            bankingUser = emailDataRepository
                    .findByEmailIgnoreCase(userData).orElseThrow(() -> new UsernameNotFoundException("Username by email not found!"))
                    .getUser();
        }
        return bankingUser;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<String> getPhonesAndEmailsByUserId(String userId) {
        log.debug("Start getPhonesAndEmailsByUserId(userId = " + userId + ") method");
        BankingUser bankingUser = userRepository.findById(Long.parseLong(userId)).orElse(null);
        if(bankingUser == null){
            return new ArrayList<>();
        }
        Stream<String> emailDataStream = bankingUser.getEmailData().stream().map(EmailData::getEmail);
        Stream<String> phoneDataStream = bankingUser.getPhoneData().stream().map(PhoneData::getPhone);
        return Stream.concat(emailDataStream, phoneDataStream).collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(value = {SQLException.class}, maxAttempts = 2, backoff = @Backoff(delay = 500))
    public void moneyTransfer(Long transferFrom, Long transferTo, BigDecimal value) {
        log.debug("Start moneyTransfer(transferFrom = " + transferFrom
                + ", transferTo = " + transferTo
                + ", value = " + value +  ") method");
        final BigDecimal amount = value.setScale(2, RoundingMode.FLOOR);
        BankingUser fromUser = findBankingUserById(transferFrom);
        BankingUser toUser = findBankingUserById(transferTo);
        BigDecimal fromBalance = fromUser.getAccount().getBalance();
        BigDecimal toBalance = toUser.getAccount().getBalance();

        if(fromBalance.compareTo(amount) < 0 || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidTransferAmountException("Invalid amount=" + amount);
        }
        fromUser.getAccount().setBalance(fromBalance.subtract(amount));
        toUser.getAccount().setBalance(toBalance.add(amount));

    }

    private BankingUser findBankingUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new BankingUserNotFoundException("User with id=" + userId + " not found!"));
    }

    private EmailData findEmailDataById(Long emailId) {
        return emailDataRepository.findById(emailId)
                .orElseThrow(() -> new EmailNotFoundException("Email by id=" + emailId + " not found!"));
    }

    private PhoneData findPhoneDataById(Long phoneId) {
        return phoneDataRepository.findById(phoneId)
                .orElseThrow(() -> new PhoneNotFoundException("Phone by id=" + phoneId + " not found!"));

    }

    private <T> T checkCountData (Function<Long, T> findData, Long id, long count, Long userId) {
        if(count == 1){
            throw new OnlyOneInstanceOfData("Only one email found for user=" + userId);
        }
        if(count == 0){
            throw new BankingUserNotFoundException("User with id=" + userId + " not found!");
        }
        return findData.apply(id);
    }
}
