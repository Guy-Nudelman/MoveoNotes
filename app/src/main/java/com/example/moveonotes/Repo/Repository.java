package com.example.moveonotes.Repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.moveonotes.Data.Database;
import com.example.moveonotes.Data.DbInterface;
import com.example.moveonotes.Model.Note;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.example.moveonotes.Utils.SharedPrefHelper;
import com.example.moveonotes.View.AddEditNoteActivity;
import com.example.moveonotes.View.LoginActivity;
import com.example.moveonotes.View.MainActivity;
import com.example.moveonotes.View.RegisterActivity;

import java.io.File;
import java.util.List;

public class Repository {

    //Variables
    private static Repository instance;
    private final SharedPrefHelper prefHelper;
    private final NetworkCore networkCore;
    private Database database;
    private DbInterface dbInterface;
    private Context context;
    private static final String TAG = "Repository";


    //Singleton
    public static Repository getInstance(Context context) {
        if (instance == null)
            instance = new Repository(context);
        return instance;
    }

    //Constructor
    private Repository(Context context) {
        networkCore = new NetworkCore();
        prefHelper = SharedPrefHelper.getInstance(context);
        context = GlobalApplicationContext.getContext();
        database = Database.getInstance(context);
        dbInterface = database.dbInterface();

    }

    //Repo Methods

    public void createUser(String email, String firstName, String lastName, String password,int pin, RegisterActivity registerActivity) {
        networkCore.createUser(email, firstName, lastName, password,pin, registerActivity);
    }

    public void signIn(String email, String password, LoginActivity loginActivity) {
        networkCore.signIn(email, password, loginActivity);
    }

    public void saveNote(Note note, AddEditNoteActivity addNoteActivity) {
        database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("lat!!@#@!#@!#",Double.toString(note.getNoteLat()));
                    note.setUserEmail(SharedPrefHelper.getInstance(GlobalApplicationContext.getContext()).getUser().getEmail());
                    dbInterface.save_note(note);
                    if(note.getImage()!="")
                        Log.d("img",note.getImage());
                    Log.d(TAG, "run:SAVED NOTE :=>" + note.getNoteTitle());
                    addNoteActivity.showActivity(MainActivity.class);
                } catch (Exception e) {
                    Log.d(TAG, "run:FAILED TO SAVE NOTE :=>" + note.getNoteTitle() + " " + "because : " + e.getMessage());
                    addNoteActivity.showError(e.getMessage());
                }
            }
        });
    }

    public LiveData<List<Note>> getAllNotes() {
        return dbInterface.getAllNotes(SharedPrefHelper.getInstance(GlobalApplicationContext.getContext()).getUser().getEmail());
    }

    public void delete(Note noteAt) {
        database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dbInterface.delete(noteAt);
                    //TODO:TO FILE HANDLER
                    //context.getFileStreamPath(noteAt.getImage()).delete();
                    Log.d(TAG, "run:Deleted NOTE : =>" + noteAt.getNoteTitle());
                } catch (Exception e) {
                    Log.d(TAG, "run:FAILED TO Delete NOTE : =>" + noteAt.getNoteTitle() + " " + "because : " + e.getMessage());
                }
            }
        });
    }

    public void updateNote(Note note, AddEditNoteActivity addEditNoteActivity) {
        database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dbInterface.update_note(note.getKey(),note.getTimeStampCreated(),note.getNoteBody(),note.getNoteTitle(),note.getImage(),note.getNoteLat(),note.getNoteLong(),note.isSecure());

                    Log.d(TAG, "run:UPDATE NOTE : =>" + note.getNoteTitle());
                    addEditNoteActivity.showActivity(MainActivity.class);
                } catch (Exception e) {
                    Log.d(TAG, "run:FAILED TO UPDATE NOTE : =>" + note.getNoteTitle() + " " + "because : " + e.getMessage());
                    addEditNoteActivity.showError(e.getMessage());
                }
            }
        });
    }

    public LiveData<Boolean> forgetPassword(String email) {
        return networkCore.resetPassword(email);
    }

}
