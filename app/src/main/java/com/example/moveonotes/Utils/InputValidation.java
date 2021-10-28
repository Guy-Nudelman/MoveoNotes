package com.example.moveonotes.Utils;

import com.example.moveonotes.Utils.Enums.Validation;

public class InputValidation {

    //Variables

    //Constructor
    public InputValidation() {
    }

    //Class Methods
    public static Validation.ERROR_INPUT isLoginValid(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) return Validation.ERROR_INPUT.MISS_FIELDS;
        else if (password.length() < 6) return Validation.ERROR_INPUT.PASS_SHORT;
        else if (email.length() < 2) return Validation.ERROR_INPUT.EMAIL_SHORT;
        return Validation.ERROR_INPUT.VALID;
    }

    public static Validation.ERROR_INPUT isRegisterValid(String email, String firstName, String lastName, String password) {
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty())
            return Validation.ERROR_INPUT.MISS_FIELDS;
        else if (password.length() < 6) return Validation.ERROR_INPUT.PASS_SHORT;
        else if (email.length() < 2) return Validation.ERROR_INPUT.EMAIL_SHORT;
        else if (firstName.length() < 2 || lastName.length()<2) return Validation.ERROR_INPUT.NAME_SHORT;
        return Validation.ERROR_INPUT.VALID;
    }

    public static Validation.ERROR_INPUT isNoteValid(String title, String body) {
        if (title.isEmpty() || body.isEmpty()) return Validation.ERROR_INPUT.MISS_FIELDS;
        else if (title.length() < 2) return Validation.ERROR_INPUT.TITLE_SHORT;
        else if (body.length() < 2) return Validation.ERROR_INPUT.BODY_SHORT;
        return Validation.ERROR_INPUT.VALID;


    }


}
