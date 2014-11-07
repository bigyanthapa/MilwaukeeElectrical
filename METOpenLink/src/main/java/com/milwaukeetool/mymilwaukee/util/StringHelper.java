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
    private static final Pattern REGEX_UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern REGEX_LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern REGEX_NUMBER = Pattern.compile("[0-9]");

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

    public static boolean containsUppercase(String value) {
        return StringHelper.matches(REGEX_UPPERCASE, value);
    }

    public static boolean containsLowercase(String value) {
        return StringHelper.matches(REGEX_LOWERCASE, value);
    }

    public static boolean containsNumber(String value) {
        return StringHelper.matches(REGEX_NUMBER, value);
    }

    private static boolean matches(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return true;
        }

        return false;
    }
}
