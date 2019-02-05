package home.TestData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestingData {
    private Locale locale;


    public synchronized void setLocale(Locale locale) {
        this.locale = locale;
    }

    public synchronized Locale getLocale() {
        return this.locale;
    }

    public String dateFormater (String date, String format,String toFormat) throws Exception {
        SimpleDateFormat dtf = new SimpleDateFormat(format,getLocale());
        SimpleDateFormat newFormat = new SimpleDateFormat(toFormat,getLocale());
        Date ldt = dtf.parse(date);
        return newFormat.format(ldt);
    }
}
