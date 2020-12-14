import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    public static void main(String[] args) {
//        System.out.println(getLastFourWeek("2020-07-24"));
        System.out.println(getLastFourWeekWithWeekend("2020-07-25"));
    }

    public static String getLastFourWeek(String inputStr,String pattern){
        if(pattern == null){
            pattern = DEFAULT_PATTERN;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date inputDate = formatter.parse(inputStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(inputDate);
            cal.add(Calendar.DAY_OF_MONTH , -28);
            Date newDate = cal.getTime();
            return formatter.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getLastFourWeek(String inputStr){
        return getLastFourWeek(inputStr, DEFAULT_PATTERN);
    }

    public static String getLastFourWeekWithWeekend(String inputStr,String pattern){
        if(pattern == null){
            pattern = DEFAULT_PATTERN;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date inputDate = formatter.parse(inputStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(inputDate);
            cal.add(Calendar.DAY_OF_MONTH, -(cal.get(Calendar.DAY_OF_WEEK) + 27));
            Date newDate = cal.getTime();
            return formatter.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getLastFourWeekWithWeekend(String inputStr) {
        return getLastFourWeekWithWeekend(inputStr, DEFAULT_PATTERN);
    }
}
