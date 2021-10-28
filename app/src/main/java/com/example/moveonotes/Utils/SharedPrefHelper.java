package com.example.moveonotes.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.moveonotes.Model.User;

public class SharedPrefHelper {

    //Variables
    private static final String EMAIL_FIREBASE_KEY = "EMAIL_FIREBASE_KEY";
    private static final String EMAIL_ADDRESS_KEY = "EMAIL_ADDRESS_KEY";
    private static final String NAME_KEY = "NAME_KEY";
    private static final String LAST_NAME_KEY = "LAST_NAME_KEY";
    private static final String PIN = "PIN_KEY";

    private static SharedPrefHelper instance;
    private final SharedPreferences preferences;

    //Constructor
    private SharedPrefHelper(Context context) {
        preferences = context.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);
    }

    //Singleton
    public static SharedPrefHelper getInstance(Context context) {
        if (instance == null)
            instance = new SharedPrefHelper(context);
        return instance;
    }


    //Class Methods
    private void storeEmailFirebase(String emailKey) {
        preferences.edit().putString(EMAIL_FIREBASE_KEY, emailKey).apply();
    }

    private String getEmailFirebase() {
        return preferences.getString(EMAIL_FIREBASE_KEY, null);
    }

    private void storeEmail(String email) {
        preferences.edit().putString(EMAIL_ADDRESS_KEY, email).apply();
    }

    private String getEmailAddress() {
        return preferences.getString(EMAIL_ADDRESS_KEY, null);
    }

    private void storeFirstName(String firstName) {
        preferences.edit().putString(NAME_KEY, firstName).apply();
    }

    private String getFirstName() {
        return preferences.getString(NAME_KEY, null);
    }

    private void storeLastName(String lastName) {
        preferences.edit().putString(LAST_NAME_KEY, lastName).apply();
    }


    private String getLastName() {
        return preferences.getString(LAST_NAME_KEY, null);
    }


    public void storePIN(int pin) {
         preferences.edit().putInt(PIN, pin).apply();

    }
    private int getUserPin() {
        return preferences.getInt(PIN, 1111);
    }

    public User getUser() {
        if (getEmailAddress() == null)
            return null;
        User user = new User(getEmailAddress(), getFirstName(), getLastName(),getUserPin());
        return user;
    }

    public void storeUser(User user) {
        storeEmail(user.getEmail());
        storeFirstName(user.getFirstName());
        storeLastName(user.getLastName());
        storePIN(user.getPin());
    }

    public void signOut() {
        preferences.edit().clear().apply();
    }
}