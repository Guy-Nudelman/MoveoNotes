package com.example.moveonotes.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moveonotes.Model.Note;
import com.example.moveonotes.R;
import com.example.moveonotes.Utils.SharedPrefHelper;
import com.example.moveonotes.View.AddEditNoteActivity;
import com.example.moveonotes.View.MainActivity;

import pl.droidsonroids.gif.GifImageView;

public class PINDialogAdapter {

    //Variables
    private Dialog pinDialog;
    private Context currentContex;
    private EditText input_PIN;
    private int pin;
    private ImageView btn;
    private GifImageView animation;
    private boolean pinChecked;
    private AddEditNoteActivity mAddEditNoteActivity;

    public PINDialogAdapter(Context context) {

        this.currentContex = context;
    }

    public void PIN_enter(AddEditNoteActivity addEditNoteActivity) {
        mAddEditNoteActivity = addEditNoteActivity;
        this.currentContex = addEditNoteActivity;
        initPinWindow();
        input_PIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) pinChecked = checkPin(Integer.parseInt(s.toString()));
                if (pinChecked) animation.setImageResource(R.drawable.success);
                if(!pinChecked) animation.setImageResource(R.drawable.erro1);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinChecked) {
                    Log.d("pinnnn",Boolean.toString(pinChecked));
                    addEditNoteActivity.setImportantCheck(true);
                }else {
                    addEditNoteActivity.setImportantCheck(false);
                }
                pinDialog.dismiss();
            }

        });
        pinDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!pinChecked) addEditNoteActivity.setImportantCheck(false);
                if (pinChecked) addEditNoteActivity.setImportantCheck(true);

            }
        });

    }

    private boolean checkPin(int pin) {
        return pin == SharedPrefHelper.getInstance(currentContex).getUser().getPin();
    }

    private void initPinWindow() {
        pinDialog = new Dialog(currentContex);
        pinDialog.setContentView(R.layout.pin_pop_up);
        btn = pinDialog.findViewById(R.id.pin_popup_btn);
        animation = pinDialog.findViewById(R.id.pop_up_gif);
        animation.setImageResource(R.drawable.erro1);
        pinDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pinDialog.show();

        input_PIN = pinDialog.findViewById(R.id.pin_popup_input);
    }


    public boolean getPinChecked() {
        return this.pinChecked;
    }

    public void PIN_enter(MainActivity mainActivity, Note note) {
        initPinWindow();
        input_PIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) pinChecked = checkPin(Integer.parseInt(s.toString()));
                if (pinChecked) animation.setImageResource(R.drawable.success);
                if(!pinChecked) animation.setImageResource(R.drawable.erro1);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinChecked) {
                    Log.d("pinnnn",Boolean.toString(pinChecked));
                    mainActivity.deletNote(note,true);
                }else {
                    mainActivity.deletNote(note,false);
                }
                pinDialog.dismiss();
            }

        });
        pinDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!pinChecked) mainActivity.deletNote(note,false);
                if (pinChecked) mainActivity.deletNote(note,true);

            }
        });
    }
}
