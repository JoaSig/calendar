package dk.cream.team.util;

/**
 * Date: 04/04/13
 */
public class DateUtil {
    public static final String DEFAULT_DATE_PATTERN = "%1$td/%1$tm/%1$tY %1$tH:%1$tM";
    public static final String TIME_ONLY_PATTERN = "%1$tH:%1$tM";
    public static final String DATE_STRING_PATTERN = "dd.MM.yyy";
    public static final String SHORT_TIMELESS_DATE_PATTERN = "%1$td.%1$tm.%1$ty";

    public static String formatDateToString(java.util.Date someDate, String format) {
        if (someDate == null) {
            return "-";
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(someDate);
        return formatCalendarToString(cal, format);

    }

    public static String formatDateToString(java.util.Date someDate) {
        if (someDate == null) {
            return "-";
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(someDate);
        return formatCalendarToString(cal, DEFAULT_DATE_PATTERN);
    }

    public static String formatCalendarToString(java.util.Calendar cal, String format) {
        if (cal == null) {
            return "";
        }
        return String.format(format, cal);
    }
}
