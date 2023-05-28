package ru.iql.banking.validators;

import ru.iql.banking.exeptions.NoArgumentsSpecified;

import java.util.Objects;
import java.util.stream.Stream;

public class UsersValidator {

    public static void requestParamsOneOrMoreNonNull(final Object... params) throws NoArgumentsSpecified {
        boolean result = Stream.of(params).anyMatch(Objects::nonNull);
        if(!result){
            throw new NoArgumentsSpecified("No arguments specified!");
        }
    }
}
