package com.bank.credit_system.functions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFunction {
    private DateFunction() {
    }

    private static final String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";

    public static String getDateFromMillis(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Date resp = new Date(date);
        return sdf.format(resp);
    }
}
