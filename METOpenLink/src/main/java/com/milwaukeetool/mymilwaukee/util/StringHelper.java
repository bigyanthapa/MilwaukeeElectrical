package com.milwaukeetool.mymilwaukee.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cent146 on 10/24/14.
 */
public class StringHelper {

    /**
     * Regex to search validating email.
     */
    private static final Pattern REGEX_EMAIL = Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        CharSequence inputStr = email;
        Matcher matcher = REGEX_EMAIL.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
