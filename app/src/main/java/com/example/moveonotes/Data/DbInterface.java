package com.example.moveonotes.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.moveonotes.Model.Note;

import java.util.List;

@Dao
public interface DbInterface {

    @Insert
    void save_note(Note note);

    @Query("SELECT * FROM NOTES_TABLE WHERE Email =:email  Order by Time DESC  ")
    LiveData<List<Note>> getAllNotes(String email);

    @Delete
    void delete(Note note);

    @Query("UPDATE NOTES_TABLE SET Body = :body, Time = :timeStampCreated, Title = :title ,Image=:imageUrl , Lat =:noteLat , Long =:noteLong , Secure=:secured  WHERE `key` = :id")
    void update_note(
            int id,
            long timeStampCreated,
            String body,
            String title,
            String imageUrl,
            double noteLat,
            double noteLong,
            boolean secured
    );

}
