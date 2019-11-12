package ru.bolgar.kiko.rental.utilities;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * Remove time from source date
     *
     * @param date date
     * @return calendar with date without time (00:00:00.000)
     */
    public static Calendar trunc(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
