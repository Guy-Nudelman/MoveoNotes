package com.example.moveonotes.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moveonotes.Interface.CallBackFragment;
import com.example.moveonotes.R;
import com.example.moveonotes.Utils.Enums.Validation;
import com.example.moveonotes.Utils.InputValidation;
import com.example.moveonotes.ViewModel.LoginViewModel;
import com.scwang.wave.MultiWaveHeader;

public class LoginActivity extends AppCompatActivity implements CallBackFragment {

    //Variables
    private EditText EmailEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private Button registerBtn;
    private ProgressBar loginSpinner;
    private TextView emailError;
    private TextView passwordError;
    private TextView generalError;
    private TextView forgetPasswordText;
    private LoginViewModel mViewModel;
    private MultiWaveHeader waveHeader,waveFooter;
    public static final int DEFAULT_WAVE_VELOCITY = 1;
    public static final int DEFAULT_WAVE_PROGRESS = 1;
    public static final int DEFAULT_WAVE_HEIGHT = 30;
    public static final int DEFAULT_WAVE_ANGLE = 45;
    private String m_Text = "";

    //LifeCycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeVariables();
        InitializeDynamicVariables();
        mViewModel.requestPermissions(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //Init Methods
    private void InitializeVariables() {
        EmailEditText = findViewById(R.id.login_email_edtText);
        passwordEditText = findViewById(R.id.login_passWord_edtText);
        loginBtn = findViewById(R.id.login_login_btn);
        registerBtn = findViewById(R.id.login_register_btn);
        loginSpinner = findViewById(R.id.login_spinner);
        emailError = findViewById(R.id.login_name_error);
        passwordError = findViewById(R.id.login_password_error);
        generalError = findViewById(R.id.login_general_error);
        forgetPasswordText = findViewById(R.id.forgot_password_text);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        waveHeader = findViewById(R.id.login_header_Wave);
        waveFooter = findViewById(R.id.login_footer_wave);
        FillWavesUi();
    }



    private void InitializeDynamicVariables() {
        loginSpinner.setVisibility(View.INVISIBLE);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(RegisterActivity.class);
            }
        });
        emailError.setText("");
        passwordError.setText("");
        generalError.setText("");
        mViewModel.getMutableLiveDataCreateUserResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                loginSpinner.setVisibility(View.GONE);
                if (s.equals("VALID")) {
                    //   mViewModel.listenerFeed();
                    //    mListener.showActivity(MainActivity.class);
                } else {
                    loginSpinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }


    private void FillWavesUi() {
        waveHeader.setVelocity(DEFAULT_WAVE_VELOCITY);
        waveHeader.setProgress(DEFAULT_WAVE_PROGRESS);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(DEFAULT_WAVE_ANGLE);
        waveHeader.setWaveHeight(DEFAULT_WAVE_HEIGHT);
        waveHeader.setStartColor(Color.RED);
        waveHeader.setCloseColor(Color.CYAN);

        waveFooter.setVelocity(DEFAULT_WAVE_VELOCITY);
        waveFooter.setProgress(DEFAULT_WAVE_PROGRESS);
        waveFooter.isRunning();
        waveFooter.setGradientAngle(DEFAULT_WAVE_ANGLE);
        waveFooter.setWaveHeight(DEFAULT_WAVE_HEIGHT);
        waveFooter.setStartColor(Color.MAGENTA);
        waveFooter.setCloseColor(Color.YELLOW);

    }


    //Activity Methods
    private void login() {
        String email = EmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        switch (mViewModel.isValid(email, password)) {
            case VALID:
                loginSpinner.setVisibility(View.VISIBLE);
                mViewModel.loginUser(email, password, this);
                break;
            case PASS_SHORT:
                loginSpinner.setVisibility(View.GONE);
                restartErrorText();
                passwordError.setText("Password Is Too Short");
                passwordEditText.requestFocus();
                Toast.makeText(getApplicationContext(), "Fix Errors", Toast.LENGTH_SHORT).show();
                break;
            case EMAIL_SHORT:
                loginSpinner.setVisibility(View.GONE);
                restartErrorText();
                passwordError.setText("Email Is Too Short");
                EmailEditText.requestFocus();
                Toast.makeText(getApplicationContext(), "Fix Errors", Toast.LENGTH_SHORT).show();
                break;
            case MISS_FIELDS:
                loginSpinner.setVisibility(View.GONE);
                restartErrorText();
                passwordError.setText("Fill All Fields");
                passwordEditText.requestFocus();
                EmailEditText.requestFocus();
                Toast.makeText(getApplicationContext(), "Fix Errors", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void forgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Email To Reset Password");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().equals(""))
                mViewModel.resetPassword(input.getText().toString()).observe(LoginActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean emailFound) {
                        if(emailFound) Toast.makeText(LoginActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(LoginActivity.this, "Couldn't Find Email", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void restartErrorText() {
        passwordError.setText("");
        emailError.setText("");
        generalError.setText("");
    }

    public void setGeneralError(String wrong_email_or_password) {
        restartErrorText();
        generalError.setText(wrong_email_or_password);
        loginSpinner.setVisibility(View.INVISIBLE);
    }

    public void ForgotPassword(){

    }


    //Interface Methods
    @Override
    public void showActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


}