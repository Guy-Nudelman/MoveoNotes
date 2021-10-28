package com.example.moveonotes.Repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moveonotes.Model.User;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.example.moveonotes.Utils.SharedPrefHelper;
import com.example.moveonotes.View.LoginActivity;
import com.example.moveonotes.View.RegisterActivity;
import com.example.moveonotes.View.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NetworkCore {

    //Variables
    private DatabaseReference mRef;
    MutableLiveData<Boolean> NetworkStatus;

    //Constructor
    public NetworkCore() {
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    //Class Methods
    public void createUser(String email, String firstName, String lastName, String password,int pin, RegisterActivity registerActivity) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(email,firstName,lastName,pin);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                SharedPrefHelper.getInstance(GlobalApplicationContext.getContext()).storeUser(user);
                                registerActivity.showActivity(WelcomeActivity.class);
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Error","Failed To Register");
                registerActivity.restartErrorText();
                registerActivity.setGeneralError(e.getMessage());
            }
        });

    }

    public void signIn(String email, String password, LoginActivity loginActivity) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mRef = FirebaseDatabase.getInstance().getReference("Users");
                    String UserId=auth.getCurrentUser().getUid();
                    mRef.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if(user!=null){
                                Log.d("here","here");
                                SharedPrefHelper.getInstance(GlobalApplicationContext.getContext()).storeUser(user);
                                loginActivity.showActivity(WelcomeActivity.class);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    loginActivity.setGeneralError("Wrong Email or Password");
                }
            }
        });
    }


    public LiveData<Boolean> resetPassword(String email) {
        NetworkStatus = new MutableLiveData<>();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            NetworkStatus.postValue(true);
                        }else{
                            NetworkStatus.postValue(false);
                        }
                    }

                });
        return NetworkStatus;
    }

}
