package check.regexes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Regexes {
    public static final String TIME_PATTERN = "^(([0,1][0-9])|(2[0-3])):[0-5][0-9]$";

    public static final boolean checkTime(String time) {
        return time.matches(TIME_PATTERN);
    }
}
