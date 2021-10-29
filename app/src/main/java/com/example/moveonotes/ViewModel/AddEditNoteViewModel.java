package com.example.moveonotes.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moveonotes.Model.Note;
import com.example.moveonotes.Repo.Repository;
import com.example.moveonotes.Utils.GlobalApplicationContext;

public class AddEditNoteViewModel extends AndroidViewModel {

    //Variables
    private Repository repository;
    private MutableLiveData<Boolean> _noteResult;


    //Constructor
    public AddEditNoteViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(GlobalApplicationContext.getContext());
        _noteResult = new MutableLiveData<>();

    }


    //ViewModel Methods
    public MutableLiveData<Boolean> get_noteResult() {
        return _noteResult;
    }

    public void updateNote(Note note) {
         repository.updateNote(note, _noteResult);
    }

    public void saveNote(Note note) {
        repository.saveNote(note, _noteResult);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

}
