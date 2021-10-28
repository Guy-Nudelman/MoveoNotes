package com.example.moveonotes.ViewModel;

import android.Manifest;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moveonotes.Repo.Repository;
import com.example.moveonotes.Utils.Enums.Validation;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.example.moveonotes.Utils.InputValidation;
import com.example.moveonotes.View.LoginActivity;

public class LoginViewModel extends AndroidViewModel {

    //Variables
    private MutableLiveData<String> mutableLiveDataLoggedUserResponse = new MutableLiveData<>();
    private Repository repository;

    //Constructor
    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(GlobalApplicationContext.getContext());
    }

    //ViewModel Methods
    public Validation.ERROR_INPUT isValid(String email, String password) {
        return InputValidation.isLoginValid(email,password);
    }

    public LiveData<String> getMutableLiveDataCreateUserResponse() {
        return mutableLiveDataLoggedUserResponse;

    }


    public void loginUser(String email, String password, LoginActivity loginActivity) {
     repository.signIn(email,password,loginActivity);
    }

    public void requestPermissions(LoginActivity loginActivity) {
        ActivityCompat.requestPermissions(loginActivity, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN},
                101);
    }



    public LiveData<Boolean> resetPassword(String email) {
        return repository.forgetPassword(email);
    }
}
