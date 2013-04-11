package dk.cream.team;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date: 29/03/13
 */
public class DateUtil {

    public static final String DEFAULT_DATE_PATTERN = "%1$td/%1$tm/%1$tY %1$tH:%1$tM";
    public static final String WHOLE_DATE_PATTERN = "%1$ty%1$tm%1$td%1$tH%1$tM";
    public static final String TIME_ONLY_PATTERN = "%1$tH:%1$tM";
    public static final String DATE_STRING_PATTERN = "dd.MM.yyy";
    public static final String DATE_PATTERN = "%1$te";
    public static final String WEEK_PATTERN = "ww";
    public static final String DAY_IN_WEEK_PATTERN = "E";
    public static final String Month_Pattern = "MMM";
    public static final String SHORT_TIMELESS_DATE_PATTERN = "%1$td.%1$tm.%1$ty";

    public static String formatDateToString(java.util.Date someDate, String format) {
        if (someDate == null) {
            return "-";
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(someDate);
        return formatCalendarToString(cal, format);
    }

    public static String formatCalendarToString(java.util.Calendar cal, String format) {
        if (cal == null) {
            return "";
        }
        return String.format(format, cal);
    }

    public static String formatCalendarWeek(Calendar calendar) {
        DateFormat df = new SimpleDateFormat(DateUtil.WEEK_PATTERN);
        String format = df.format(calendar.getTime());
        return " Uge " + format;
    }

    public static String formatCalendarWeekNo(Calendar calendar) {
        DateFormat df = new SimpleDateFormat(DateUtil.WEEK_PATTERN);
        return df.format(calendar.getTime());
    }

    public static String formatCalendarDay(Calendar calendar) {
        DateFormat df = new SimpleDateFormat(DateUtil.DAY_IN_WEEK_PATTERN);
        String format = df.format(calendar.getTime());
        return Days.valueOf(format).danish;
    }

    public static String formatCalendarMonth(Calendar calendar) {
        DateFormat df = new SimpleDateFormat(DateUtil.Month_Pattern);
        return df.format(calendar.getTime());
    }

    public static Date formatStringToDate(String stringDate) {
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_STRING_PATTERN);
        Date toDate = new Date();
        try {
            toDate = df.parse(stringDate);
        } catch (ParseException e) {
            return toDate;
        }
        return toDate;
    }

    public static Integer formatDateAndTimeToIntegerValue(Date date) {
        return Integer.parseInt(formatDateToString(date, WHOLE_DATE_PATTERN));
    }

    @SuppressWarnings("UnusedDeclaration")
    private static enum Days {
        Mon("Mandag"),
        Tue("Tirsdag"),
        Wed("Onsdag"),
        Thu("Torsdag"),
        Fri("Fredag"),
        Sat("L�rdag"),
        Sun("S�ndag");

        private String danish;

        private Days(String danish) {
            this.danish = danish;
        }
    }
}
