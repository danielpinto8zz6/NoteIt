package danielpinto8zz6.github.io.noteitdesktop;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getCurrentDateTime() {
        Date currentTime = Calendar.getInstance().getTime();
        return getDateTime(currentTime);

    }
}
