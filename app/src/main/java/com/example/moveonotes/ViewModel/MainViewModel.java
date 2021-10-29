package com.example.moveonotes.ViewModel;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.moveonotes.Adapter.NoteAdapter;
import com.example.moveonotes.Model.Note;
import com.example.moveonotes.R;
import com.example.moveonotes.Repo.Repository;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.example.moveonotes.View.AddEditNoteActivity;
import com.example.moveonotes.View.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    //Variables
    private Repository repository;
    private List<Note> notes;

    //Constructor
    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(GlobalApplicationContext.getContext());
    }

    //ViewModel Methods
    public void fillEditNoteExtras(Intent intent, Note note) {
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getKey());
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getNoteTitle());
        intent.putExtra(AddEditNoteActivity.EXTRA_BODY, note.getNoteBody());
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getKey());
        intent.putExtra(AddEditNoteActivity.EXTRA_IMAGE_URL, note.getImage());
        intent.putExtra(AddEditNoteActivity.EXTRA_SECURED, note.isSecure());
    }


    public void search(String text, NoteAdapter noteAdapter, List<Note> notes) {
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

    public void showImagePopup(MainActivity mainActivity, Note noteAt) {
        Dialog imageDialog;
        imageDialog = new Dialog(mainActivity);
        imageDialog.setContentView(R.layout.image_pop_up);
        TextView dialogTitle;
        ImageView noteImage;
        dialogTitle = imageDialog.findViewById(R.id.pop_up_title);
        noteImage = imageDialog.findViewById(R.id.pop_up_image);
        Log.d("image", noteAt.getImage());
        Glide.with(getApplication())
                .load(new File(noteAt.getImage()).getPath()) // Uri of the picture
                .into(noteImage);
        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageDialog.show();
    }


    public LiveData<List<Note>> getLocalData() {
        return repository.getAllNotes();
    }

    public void delete(Note note) {
        repository.delete(note);
    }
}
