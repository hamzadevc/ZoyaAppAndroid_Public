package com.grappetite.zoya.utils;


import androidx.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    private static final String TAG = "DateUtils";
    public static final long ONE_DAY = 24*60*60*1000;

    public static String getDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
    }

    @NonNull
    public static Date getDateOnly(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String sd = sdf.format(date);
        try {
            return sdf.parse(sd);
        } catch (ParseException e) {
            Log.e(TAG,e.toString());
            //noinspection ConstantConditions
            return null;
        }
    }

    public static String toServerDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }


    public static Date toServerDate(String inputFormat, String date) {
        try {
            return new SimpleDateFormat(inputFormat,Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            Log.e(TAG,e.toString());
            return null;
        }
    }

    public static String toServerFormat(int day, int month, int year) {
        return String.format(Locale.getDefault(),"%d-%d-%d",year,month,day);
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    public static long daysBetween(Date startDate, Date endDate) {
        long diff = getDateOnly(endDate).getTime() - getDateOnly(startDate).getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
