package com.example.moveonotes.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moveonotes.Adapter.NoteAdapter;
import com.example.moveonotes.Adapter.PINDialogAdapter;
import com.example.moveonotes.Interface.CallBackFragment;
import com.example.moveonotes.Model.Note;
import com.example.moveonotes.R;
import com.example.moveonotes.Repo.Repository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CallBackFragment {

    //Variables
    private TextView nameText;
    private FloatingActionButton addBtn;
    private Button mapBtn;
    private Button logOutBtn;
    private RecyclerView recyclerView;
    private Repository repository;
    private List<Note> notes;
    private NoteAdapter noteAdapter;
    private Dialog imageDialog;
    private Dialog logoutDialog;
    private EditText searchEditText;



    //LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        getLocalData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Init  Methods
    private void InitializeDynamicVariables() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(AddEditNoteActivity.class);
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapPopUp();
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        initRecyclerView();
        getLocalData();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

    }



    private void InitializeVariables() {
        addBtn = findViewById(R.id.main_add_btn);
        mapBtn = findViewById(R.id.main_location_btn);
        logOutBtn = findViewById(R.id.main_log_out_btn);
        recyclerView = findViewById(R.id.main_recycle_view);
        searchEditText = findViewById(R.id.main_search_text);
        notes = new ArrayList<>();
    }


    private void initRecyclerView() {
        repository = Repository.getInstance(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        defineSwipeToDelete();
        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                showEditNoteActivity(note);
            }
        });

    }

    //Activity Methods
    private void getLocalData() {
        repository.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> _notes) {
                if (_notes == null) return;
                notes=_notes;
                noteAdapter.submitList(_notes);
            }
        });

    }



    private void search(String text) {
        noteAdapter.setSearchText(text);
        ArrayList<Note> filteredList = new ArrayList<>();
        for (Note note : notes) {
            if (note.getNoteTitle().toLowerCase().contains(text.toLowerCase()) || note.getNoteBody().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(note);
            }
        }
        noteAdapter.submitList(filteredList);
        noteAdapter.notifyDataSetChanged();
    }

    private void showLogoutDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Log Out");
        alertDialog.setMessage("Are you sure you want to log out ?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        showActivity(SplashActivity.class);
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void showEditNoteActivity(Note note) {
        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getKey());
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getNoteTitle());
        intent.putExtra(AddEditNoteActivity.EXTRA_BODY, note.getNoteBody());
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getKey());
        intent.putExtra(AddEditNoteActivity.EXTRA_IMAGE_URL, note.getImage());
        intent.putExtra(AddEditNoteActivity.EXTRA_SECURED,note.isSecure());
        startActivityForResult(intent, 2);

    }

    private void defineSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(2,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Log.d("Image",noteAdapter.getNoteAt(viewHolder.getAdapterPosition()).toString());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = noteAdapter.getNoteAt(viewHolder.getAdapterPosition());
                if(note.getImage().length()>2){
                    showImagePopup(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                    noteAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(MainActivity.this, "No Image!", Toast.LENGTH_SHORT).show();
                    noteAdapter.notifyDataSetChanged();
                }

            }
        }).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = noteAdapter.getNoteAt(viewHolder.getAdapterPosition());
                if(!note.isSecure())repository.delete(note);
                else pinBeforeDelete(note);
                Toast.makeText(MainActivity.this, "Deleting Note", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

    }

    private void pinBeforeDelete(Note note) {
        PINDialogAdapter pinDialogAdapter = new PINDialogAdapter(this);
        pinDialogAdapter.PIN_enter(this ,note);
    }


    private void showMapPopUp() {
        getLocalData();
        MapFragment mapFragment = MapFragment.newInstance(notes);
        mapFragment.show(getSupportFragmentManager(),"MapFragment");



    }


    private void showImagePopup(Note noteAt) {
        imageDialog = new Dialog(MainActivity.this);
        imageDialog.setContentView(R.layout.image_pop_up);
        TextView dialogTitle;
        ImageView noteImage;
        dialogTitle = imageDialog.findViewById(R.id.pop_up_title);
        noteImage = imageDialog.findViewById(R.id.pop_up_image);
        Log.d("image",noteAt.getImage());
        Glide.with(getApplicationContext())
                .load(new File(noteAt.getImage()).getPath()) // Uri of the picture
            .into(noteImage);
        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageDialog.show();

    }


    //Interface Methods
    @Override
    public void showActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }


    public void deletNote(Note note, boolean b) {
        if (b)repository.delete(note);
        else{
            Toast.makeText(this, "Wrong PIN!", Toast.LENGTH_SHORT).show();
        }
        noteAdapter.notifyDataSetChanged();
    }
}