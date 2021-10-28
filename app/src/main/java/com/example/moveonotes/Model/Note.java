package com.example.moveonotes.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

@Entity(tableName = "NOTES_TABLE")
public class Note implements Serializable {

    //Variables
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int key;
    @ColumnInfo(name = "User Name")
    private String userName;
    @ColumnInfo(name = "Time")
    private long timeStampCreated;
    @ColumnInfo(name = "Title")
    private String noteTitle;
    @ColumnInfo(name = "Body")
    private String noteBody;
    @ColumnInfo(name = "Lat")
    private double noteLat;
    @ColumnInfo(name = "Long")
    private double noteLong;
    @ColumnInfo(name = "Email")
    private String userEmail;
    @ColumnInfo(name = "Image")
    private String image="";
    @ColumnInfo(name = "Secure")
    private boolean secure= false;

    //Constructors
    public Note() {
    }

    public Note(@NonNull int key, String userName, long timeStampCreated, String noteTitle, String noteBody, double noteLat, double noteLong, String userEmail, String image,boolean secure ) {
        this.key = key;
        this.userName = userName;
        this.timeStampCreated = timeStampCreated;
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
        this.noteLat = noteLat;
        this.noteLong = noteLong;
        this.userEmail = userEmail;
        this.image = image;
        this.secure = secure;
    }

    public Note(long timeStampCreated, String noteTitle, String noteBody, String image) {
        this.timeStampCreated = timeStampCreated;
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
        this.image = image;
        this.secure = false;
    }





    //Class Methods


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTimeStampCreated() {
        return timeStampCreated;
    }

    public void setTimeStampCreated(long timeStampCreated) {
        this.timeStampCreated = timeStampCreated;
    }


    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public double getNoteLat() {
        return noteLat;
    }

    public void setNoteLat(double noteLat) {
        this.noteLat = noteLat;
    }

    public double getNoteLong() {
        return noteLong;
    }

    public void setNoteLong(double noteLong) {
        this.noteLong = noteLong;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
