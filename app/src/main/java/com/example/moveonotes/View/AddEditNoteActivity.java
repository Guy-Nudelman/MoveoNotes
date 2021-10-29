package com.example.moveonotes.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.moveonotes.Adapter.PINDialogAdapter;
import com.example.moveonotes.Interface.CallBackFragment;
import com.example.moveonotes.Model.Note;
import com.example.moveonotes.R;
import com.example.moveonotes.Repo.Repository;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.example.moveonotes.Utils.InputValidation;
import com.example.moveonotes.ViewModel.AddEditNoteViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.DateFormat;

public class AddEditNoteActivity extends AppCompatActivity implements CallBackFragment {

    //Variables
    private Button addBtn;
    private Button backBtn;
    private FloatingActionButton deleteBtn;
    private TextView titleText;
    private EditText titleInput;
    private EditText bodyInput;
    private EditText dateInput;
    private Repository repository;
    private Note note;
    private TextView errorText;
    private CheckBox importantNoteCheck;
    private String imagePostUri = "";
    private ImageView imageView;
    private AddEditNoteViewModel mViewModel;

    public static final String EXTRA_TITLE = "com.example.moveonotes.View.EXTRA_TITLE";
    public static final String EXTRA_BODY = "com.example.moveonotes.View.EXTRA_BODY";
    public static final String EXTRA_ID = "com.example.moveonotes.View.EXTRA_ID";
    public static final String EXTRA_IMAGE_URL = "com.example.moveonotes.View.EXTRA_IMAGE_URL";
    public static final String EXTRA_SECURED = "com.example.moveonotes.View.EXTRA_SEQURED";
    private static final int IMAGE_GALLERY_PROFILE_REQUEST = 1;
    FusedLocationProviderClient fusedLocationProviderClient;


    //LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        InitializeVariables();
        InitializeDynamicVariables();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        checkStatus();
        dateInput.setText(DateFormat.getDateTimeInstance().format(System.currentTimeMillis()));
        dateInput.setEnabled(false);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                errorText.setText("");
                String title = titleInput.getText().toString();
                String body = bodyInput.getText().toString();
                Log.d("body", body);
                switch (InputValidation.isNoteValid(title, body)) {
                    case VALID:
                        note.setNoteTitle(title);
                        note.setNoteBody(body);
                        note.setTimeStampCreated(System.currentTimeMillis());
                        saveNote();
                        break;
                    case TITLE_SHORT:
                        errorText.setText("Title Is Too Short");
                        break;
                    case BODY_SHORT:
                        errorText.setText("Body Is To Short");
                        break;
                    case MISS_FIELDS:
                        errorText.setText("Fill All Fields");
                        break;
                }

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note != null) {
                    int id = getIntent().getIntExtra(EXTRA_ID, -1);
                    if (id != -1) {
                        note.setKey(id);
                        String imageUrlCheck = getIntent().getStringExtra(EXTRA_IMAGE_URL);
                        if (imagePostUri == "")
                            note.setImage(imageUrlCheck);
                    }
                    mViewModel.delete(note);
                    finish();
                }


            }
        });
        errorText.setText("");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        importantNoteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PINDialogAdapter pinDialogAdapter = new PINDialogAdapter(AddEditNoteActivity.this);
                    pinDialogAdapter.PIN_enter(AddEditNoteActivity.this);
                    boolean pinCheck = pinDialogAdapter.getPinChecked();
                }
            }
        });
    }


    private void InitializeVariables() {
        addBtn = findViewById(R.id.add_add_Btn);
        backBtn = findViewById(R.id.add_back_btn);
        titleInput = findViewById(R.id.add_title_edtText);
        bodyInput = findViewById(R.id.add_body_edtText);
        titleText = findViewById(R.id.add_title_text);
        dateInput = findViewById(R.id.add_date_edtText);
        errorText = findViewById(R.id.add_error_text);
        imageView = findViewById(R.id.add_image);
        deleteBtn = findViewById(R.id.add_delete_btn);
        importantNoteCheck = findViewById(R.id.add_secure_check_box);
        deleteBtn.setVisibility(View.INVISIBLE);
        note = new Note();
        repository = Repository.getInstance(GlobalApplicationContext.getContext());
        mViewModel = new ViewModelProvider(this).get(AddEditNoteViewModel.class);
        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    //Activity Methods
    private void saveNote() {
        Toast.makeText(this, "Saving Note...", Toast.LENGTH_SHORT).show();
        getLocationAndSaveNote();

    }


    private void getLocationAndSaveNote() {
        //Check Permissions
        if (ActivityCompat.checkSelfPermission(AddEditNoteActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When Permission Granted
            //getLocation()
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initilize Location
                    Location location = task.getResult();
                    if (location != null) {
                        note.setNoteLat(location.getLatitude());
                        note.setNoteLong(location.getLongitude());
                    }
                    saveActualNote();
                }
            });
        } else {
            //When Permission denied
            ActivityCompat.requestPermissions(AddEditNoteActivity.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }
    }

    private void saveActualNote() {
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (importantNoteCheck.isChecked()) note.setSecure(true);
        else note.setSecure(false);
        if (id != -1) {
            note.setKey(id);
            String imageUrlCheck = getIntent().getStringExtra(EXTRA_IMAGE_URL);
            if (imagePostUri == "")
                note.setImage(imageUrlCheck);
            mViewModel.get_noteResult().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean noteUpdateResult) {
                    if (noteUpdateResult) showActivity(MainActivity.class);
                    else showError("FAILED TO UPDATE NOTE");
                }
            });
            mViewModel.updateNote(note);

        } else {
            Log.d(" note laong", Double.toString(note.getNoteLat()));
            mViewModel.get_noteResult().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean noteUpdateResult) {
                    if (noteUpdateResult) showActivity(MainActivity.class);
                    else showError("FAILED TO SAVE NOTE");
                }
            });
            mViewModel.saveNote(note);

        }
    }

    public void showError(String message) {
        errorText.setText(message);
    }

    private void checkStatus() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            titleText.setText("Edit Note");
            deleteBtn.setVisibility(View.VISIBLE);
            titleInput.setText(intent.getStringExtra(EXTRA_TITLE));
            bodyInput.setText(intent.getStringExtra(EXTRA_BODY));
            importantNoteCheck.setChecked(intent.getBooleanExtra(EXTRA_SECURED, false));
            String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
            if (imageUrl.length() > 2) {
                Glide.with(getApplicationContext())
                        .load(new File(intent.getStringExtra(EXTRA_IMAGE_URL)).getPath()) // Uri of the picture
                        .into(imageView);
            }

        } else {
            titleText.setText("Add Note");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            imagePostUri = data.getData().toString();
            Glide.with(getApplicationContext()).load(imagePostUri).into(imageView);
            note.setImage(imagePostUri);
        }
    }

    private void getImage() {
        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_GALLERY_PROFILE_REQUEST);
    }

    //Interface Methods
    @Override
    public void showActivity(Class<? extends AppCompatActivity> activity) {
//        Intent intent = new Intent(this, activity);
//        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void setImportantCheck(boolean b) {
        importantNoteCheck.setChecked(b);
    }
}