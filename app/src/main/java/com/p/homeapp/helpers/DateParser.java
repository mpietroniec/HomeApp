package com.p.homeapp.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParser {
    static String dateTemplate = "yyyy-M-dd";
    static SimpleDateFormat formatter = new SimpleDateFormat(dateTemplate, Locale.ENGLISH);

    public static Date stringToDateParser(String date) {
        Date localDate = null;
        try {
            localDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("123456: " + localDate);
        return localDate;
    }

    public static String dateToStringParser(Date date){
        String dateText = formatter.format(date);
        return dateText;
    }
}
