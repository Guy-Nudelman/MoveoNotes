package com.example.moveonotes.ViewModel;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moveonotes.R;
import com.example.moveonotes.Repo.Repository;
import com.example.moveonotes.Utils.Enums.Validation;
import com.example.moveonotes.Utils.InputValidation;
import com.example.moveonotes.View.RegisterActivity;

public class RegisterViewModel extends AndroidViewModel {

    //Variables
    private Repository repositoryApp;


    //Constructor
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        InputValidation inputValidation = new InputValidation();
        repositoryApp = Repository.getInstance(application);

    }

    //ViewModel Methods
    public Validation.ERROR_INPUT isValid(String email, String firstName, String lastName, String password) {
        return InputValidation.isRegisterValid(email,firstName,lastName,password);
    }



    public void createUser(String email, String firstName, String lastName, String password, int pin, RegisterActivity registerActivity) {
        repositoryApp.createUser(email,firstName,lastName,password,pin ,registerActivity);
    }

    public void sendEmail(RegisterActivity registerActivity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"Guy.Nudelman@aol.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
        intent.putExtra(Intent.EXTRA_TEXT, "Wanted to ...");
        registerActivity.startActivity(Intent.createChooser(intent, ""));
    }
}
