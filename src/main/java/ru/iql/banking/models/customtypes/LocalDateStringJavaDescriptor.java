package ru.iql.banking.models.customtypes;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateStringJavaDescriptor extends AbstractTypeDescriptor<LocalDate> {

    public static final LocalDateStringJavaDescriptor INSTANCE = new LocalDateStringJavaDescriptor();
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @SuppressWarnings("unchecked")
    public LocalDateStringJavaDescriptor() {
        super(LocalDate.class, ImmutableMutabilityPlan.INSTANCE);
    }

    @Override
    public LocalDate fromString(String s) {
        return LocalDate.parse(s, FORMATTER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(LocalDate localDate, Class<X> aClass, WrapperOptions wrapperOptions) {
        if (localDate == null)
            return null;

        if (String.class.isAssignableFrom(aClass))
            return (X) FORMATTER.format(localDate);

        throw unknownUnwrap(aClass);
    }

    @Override
    public <X> LocalDate wrap(X aClass, WrapperOptions wrapperOptions) {
        if (aClass == null)
            return null;

        if(aClass instanceof String) {
            return LocalDate.from(FORMATTER.parse((CharSequence) aClass));
        }
        throw unknownWrap(aClass.getClass());
    }
}
