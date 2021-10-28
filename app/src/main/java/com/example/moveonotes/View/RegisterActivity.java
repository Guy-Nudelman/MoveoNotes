package com.example.moveonotes.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.moveonotes.ViewModel.RegisterViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.wave.MultiWaveHeader;

public class RegisterActivity extends AppCompatActivity implements CallBackFragment {

    //Variables
    private EditText EmailEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText pinEditText;
    private TextView generalErrorText;
    private TextView emailErrorText;
    private TextView firstNameErrorText;
    private TextView lastNameErrorText;
    private TextView passwordErrorText;
    private ProgressBar registerSpinner;
    private Button registerBtn;
    private Button backBtn;
    private RegisterViewModel mViewModel;
    private MultiWaveHeader waveHeader, waveFooter;
    private FloatingActionButton emailBtn;
    public static final int DEFAULT_WAVE_VELOCITY = 1;
    public static final int DEFAULT_WAVE_PROGRESS = 1;
    public static final int DEFAULT_WAVE_HEIGHT = 30;
    public static final int DEFAULT_WAVE_ANGLE = 45;


    //LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitializeVariables();
        InitializeDynamicVariables();
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
    private void InitializeDynamicVariables() {
        registerSpinner.setVisibility(View.INVISIBLE);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(LoginActivity.class);
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.sendEmail(RegisterActivity.this);
            }
        });
        firstNameErrorText.setText("");
        lastNameErrorText.setText("");
        passwordErrorText.setText("");
        emailErrorText.setText("");
        generalErrorText.setText("");
    }


    private void InitializeVariables() {
        EmailEditText = findViewById(R.id.register_email_editText);
        passwordEditText = findViewById(R.id.register_password_editText);
        firstNameEditText = findViewById(R.id.register_firstName_editText);
        lastNameEditText = findViewById(R.id.register_lastName_editText);
        pinEditText = findViewById(R.id.register_pin_input);
        registerSpinner = findViewById(R.id.register_spinner);
        registerBtn = findViewById(R.id.register_register_btn);
        firstNameErrorText = findViewById(R.id.register_firstName_error);
        lastNameErrorText = findViewById(R.id.register_lastName_error);
        passwordErrorText = findViewById(R.id.register_password_error);
        emailErrorText = findViewById(R.id.register_email_error);
        generalErrorText = findViewById(R.id.register_general_error);
        backBtn = findViewById(R.id.register_back_btn);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
       // waveHeader = findViewById(R.id.register_wave_header);
        waveFooter = findViewById(R.id.register_wave_footer);
        emailBtn = findViewById(R.id.register_email_btn);

        FillWavesUi();

    }


    private void FillWavesUi() {
//        waveHeader.setVelocity(DEFAULT_WAVE_VELOCITY);
//        waveHeader.setProgress(DEFAULT_WAVE_PROGRESS);
//        waveHeader.isRunning();
//        waveHeader.setGradientAngle(DEFAULT_WAVE_ANGLE);
//        waveHeader.setWaveHeight(DEFAULT_WAVE_HEIGHT);
//        waveHeader.setStartColor(Color.RED);
//        waveHeader.setCloseColor(Color.CYAN);

        waveFooter.setVelocity(DEFAULT_WAVE_VELOCITY);
        waveFooter.setProgress(DEFAULT_WAVE_PROGRESS);
        waveFooter.isRunning();
        waveFooter.setGradientAngle(DEFAULT_WAVE_ANGLE);
        waveFooter.setWaveHeight(DEFAULT_WAVE_HEIGHT);
        waveFooter.setStartColor(Color.MAGENTA);
        waveFooter.setCloseColor(Color.YELLOW);

    }


    //Activity Methods
    private void register() {
        String email = EmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        int pin;
        String pinInput = pinEditText.getText().toString();
        if (!pinInput.equals(""))  pin = Integer.parseInt(pinInput);
        else pin = 1111;
        Validation.ERROR_INPUT input = mViewModel.isValid(email, firstName, lastName, password);
        switch (input) {
            case VALID:
                registerSpinner.setVisibility(View.VISIBLE);
                restartErrorText();
                mViewModel.createUser(email, firstName, lastName, password,pin ,this);
                break;
            case PASS_SHORT:
                registerSpinner.setVisibility(View.GONE);
                restartErrorText();
                passwordErrorText.setText("Password Is Too Short");
                Toast.makeText(getApplicationContext(), "Fix Errors", Toast.LENGTH_SHORT).show();
                break;
            case EMAIL_SHORT:
                registerSpinner.setVisibility(View.GONE);
                restartErrorText();
                emailErrorText.setText("Email Is Too Short");
                Toast.makeText(getApplicationContext(), "Fix Errors", Toast.LENGTH_SHORT).show();
                break;
            case MISS_FIELDS:
                registerSpinner.setVisibility(View.GONE);
                restartErrorText();
                generalErrorText.setText("Fill All Fields");
                Toast.makeText(getApplicationContext(), "Fix Errors", Toast.LENGTH_SHORT).show();
                break;
            case NAME_SHORT:
                registerSpinner.setVisibility(View.GONE);
                restartErrorText();
                firstNameErrorText.setText("Name Is Too Short");


        }
    }

    public void restartErrorText() {
        passwordErrorText.setText("");
        emailErrorText.setText("");
        firstNameErrorText.setText("");
        lastNameErrorText.setText("");
        generalErrorText.setText("");
    }

    //Interface Methods
    @Override
    public void showActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void setGeneralError(String error) {
        restartErrorText();
        generalErrorText.setText(error);
        registerSpinner.setVisibility(View.INVISIBLE);

    }
}
