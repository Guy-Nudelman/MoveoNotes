package com.example.moveonotes.Interface;

import com.example.moveonotes.Model.Note;

public interface ICallBackFeedAdapter {
    void onItemClickListener(Note note);
    void onRefresh(Note note);
}
