package com.quad.xpress.utills.helpers;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import java.util.regex.Pattern;

public class FieldsValidator {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "[0-9+ ]{7,16}";

    // Error Messages
   // No excuses for forgetting your name. (Except if you are Jason Bourne, then it's OK.)

    private static final String REQUIRED_MSG = "Give a name for that feeling of yours!";
    private static final String EMAIL_MSG = "Oops! Invalid Email ID!";
    private static final String PHONE_MSG = "Clue: It's numeric. Between 10-13 digits. Try again!";
    private static final String USER_NAME = "No excuses for forgetting your name. (Except if you are Jason Bourne, then it's OK.)";
    private static final String HAS_TEXT = "Minimum 3 characters length";

    // call this method when you need to check email validation
    public static boolean isEmailAddressOK(AutoCompleteTextView editText, boolean required) {

        return isValidemail(editText, EMAIL_REGEX, EMAIL_MSG, required);

    }
    public static boolean isValidemail(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasTextEmail(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(EMAIL_MSG);
            return false;
        };

        return true;
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValidPhone(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValidPhone(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasTextPhone(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(PHONE_MSG);
            return false;
        };

        return true;
    }

    public static boolean isPhoneNumberString(String editText, boolean required) {
        return isValidPhoneString(editText, PHONE_REGEX, PHONE_MSG, required);
    }
    public static boolean isValidPhoneString(String editText, String regex, String errMsg, boolean required) {

        String text = editText;
        // clearing the error, if it was previously set by some other values

        // text required and editText is blank, so return false
        if ( required && !hasTextPhoneString(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {

            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasTextEmail(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() <= 5) {
            editText.setError(EMAIL_MSG);
            return false;
        }

        return true;
    }


    public static boolean hasTextTags(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError("Required");
            return false;
        }

        return true;
    }


    public static boolean hasTextTitle(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError("Title Required");
            return false;
        }

        return true;
    }

    public static boolean hasTextUserName(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(USER_NAME);
            return false;
        }

        return true;
    }
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0 || text.length() < 3) {
            editText.setError(HAS_TEXT);
            return false;
        }

        return true;
    }

    public static boolean hasTextPhone(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() <= 11) {
            editText.setError(PHONE_MSG);
            return false;
        }
        if (text.length() >= 17) {
            editText.setError(PHONE_MSG);
            return false;
        }

        return true;
    }
    public static boolean hasTextPhoneString(String editText) {

        String text = editText;

        // length 0 means there is no text
        if (text.length() <= 11) {

            return false;
        }
        if (text.length() >= 17) {

            return false;
        }

        return true;
    }
}