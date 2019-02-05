package home.TestData;

import home.env.ThreadEnv;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SystemDate {
    private String year;
    private String month;
    private String day;

    public SystemDate () {
        ZoneId zoneId = ZoneOffset.UTC;
        ZonedDateTime now = ZonedDateTime.now( zoneId );
        year=Integer.toString(now.getYear());
        month=Integer.toString(now.getMonth().getValue());
        day=Integer.toString(now.getDayOfMonth());
    }

    public SystemDate(int years, int months, int days) {
        ZoneId zoneId = ZoneOffset.UTC;
        ZonedDateTime now = ZonedDateTime.now( zoneId ).plusDays(days);
        year=Integer.toString(now.plusYears(years).getYear());
        month = Integer.toString(now.plusMonths(months).getMonth().getValue());
        day=Integer.toString(now.getDayOfMonth());
    }

    public String getYear () {
        return year;
    }
    public String getMonth () {
        return month;
    }
    public String getDay() {
        return day;
    }

    public String getMonthString (String locale) {
        ThreadEnv.getByThread().getTestingData().setLocale(new Locale(locale));
        DateFormat formatter = new SimpleDateFormat("MMMM", Locale.forLanguageTag(locale));
        String month = "";
        try{
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Integer.parseInt(getYear()),Integer.parseInt(getMonth())-1,Integer.parseInt(getDay()));
            return formatter.format(calendar.getTime());
        }
        catch (Exception e){
            throw e;
        }
    }

}
