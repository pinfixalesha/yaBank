package ru.yandex.practicum.yaBank.accountsApplication.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    static final public DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static public boolean isAdult(LocalDate dateOfBirth) {
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18;
    }
}
