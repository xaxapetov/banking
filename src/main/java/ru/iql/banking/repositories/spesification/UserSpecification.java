package ru.iql.banking.repositories.spesification;

import org.springframework.data.jpa.domain.Specification;
import ru.iql.banking.models.BankingUser;
import ru.iql.banking.models.EmailData;
import ru.iql.banking.models.PhoneData;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import java.sql.Timestamp;
import java.time.LocalDate;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.iql.banking.utils.Constants.DATE_FUNCTION;
import static ru.iql.banking.utils.Constants.DATE_OF_BIRTH;
import static ru.iql.banking.utils.Constants.DATE_PATTERN;
import static ru.iql.banking.utils.Constants.EMAIL;
import static ru.iql.banking.utils.Constants.EMAIL_DATA;
import static ru.iql.banking.utils.Constants.PHONE;
import static ru.iql.banking.utils.Constants.NAME;
import static ru.iql.banking.utils.Constants.PHONE_DATA;
import static ru.iql.banking.utils.Constants.TO_TIMESTAMP_FUNCTION;

public class UserSpecification {

    public static Specification<BankingUser> getUserByDateOfBirth(final LocalDate dateOfBirth) {
        if(dateOfBirth == null){
            return null;
        }
        return (root, query, crBuilder) -> {
            Expression<Timestamp> timestampExpression = crBuilder.function(
                    TO_TIMESTAMP_FUNCTION,
                    Timestamp.class,
                    root.get(DATE_OF_BIRTH),
                    crBuilder.literal(DATE_PATTERN));

            Expression<LocalDate> localDateExpression = crBuilder.function(
                    DATE_FUNCTION,
                    LocalDate.class,
                    timestampExpression);

            return crBuilder.greaterThan(localDateExpression, dateOfBirth);
        };
    }

    public static Specification<BankingUser> getUserByPhone(final String phone) {
        if(phone == null){
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            Join<PhoneData, BankingUser> phones = root.join(PHONE_DATA);
            return criteriaBuilder.equal(phones.get(PHONE), phone);
        };
    }

    public static Specification<BankingUser> getUserByName(final String name) {
        if(name == null){
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(NAME), name + "%");
    }

    public static Specification<BankingUser> getUserByEmail(final String email) {
        if(email == null){
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            Join<EmailData, BankingUser> emails = root.join(EMAIL_DATA);
            return criteriaBuilder.equal(emails.get(EMAIL), email);
        };

    }

    public static Specification<BankingUser> getAllSpecificationForGetUser(final LocalDate dateOfBirth,
                                                                           final String phone,
                                                                           final String name,
                                                                           final String email) {
        return where(getUserByDateOfBirth(dateOfBirth))
                .and(getUserByPhone(phone))
                .and(getUserByName(name))
                .and(getUserByEmail(email));
    }
}
