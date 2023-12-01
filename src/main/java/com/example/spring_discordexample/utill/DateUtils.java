package com.example.spring_discordexample.utill;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateUtils {


    public static LocalDate stringToLocalDate(String dateString) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            return LocalDate.parse(dateString, formatter);
        }catch (DateTimeParseException e){
            log.info("DateTimeParseError = {}", e.getMessage());
            throw e;
        }

    }
}
