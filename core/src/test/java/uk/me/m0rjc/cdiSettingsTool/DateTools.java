package uk.me.m0rjc.cdiSettingsTool;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Helper methods for dates.
 * 
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
final class DateTools
{
    /** Inhibit construction. */
    private DateTools()
    {}
    
    /** Make a date in the GMT time zone.
     * @param year year, eg 2011
     * @param month month, 1 for January
     * @param day day of month starting 1
     * @param hour hour in 24 hour clock
     * @param minute minute, 0 to 59
     * @param second second, 0 to 59
     * @return {@link Calendar#getTime()}
     */
    public static Date makeDateGmt(final int year, final int month, final int day, final int hour, final int minute, final int second)
    {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        return populateCalendar(year, month, day, hour, minute, second,
                calendar);
    }

    /**
     * Populate a Calendar and return the Date.
     * @param year year, eg 2011
     * @param month month, 1 for January
     * @param day day of month starting 1
     * @param hour hour in 24 hour clock
     * @param minute minute, 0 to 59
     * @param second second, 0 to 59
     * @param calendar calendar to set up.
     * @return {@link Calendar#getTime()}
     */
    public static Date populateCalendar(final int year, final int month, final int day, final int hour,
            final int minute, final int second, final Calendar calendar)
    {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
