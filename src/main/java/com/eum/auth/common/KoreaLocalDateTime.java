package com.eum.auth.common;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
@Component
public class KoreaLocalDateTime {
    public static String localDateTimeToKoreaZoned(LocalDateTime dateTime){
        LocalDateTime dateTimeUTC = LocalDateTime.parse(dateTime.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime koreaZoneTime = dateTimeUTC.atZone(ZoneId.of("Asia/Seoul"));
        String formattedKoreaZoneTime = koreaZoneTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        return formattedKoreaZoneTime;
    }
    public static String dateToKoreaZone(Date date){
        Instant instant = date.toInstant();
        LocalDateTime dateTimeUTC =  instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        ZonedDateTime koreaZoneTime = dateTimeUTC.atZone(ZoneId.of("Asia/Seoul"));
        String formattedKoreaZoneTime = koreaZoneTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        return formattedKoreaZoneTime;
    }


}
