package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;

/**
 * Created by scott.hopfensperger on 12/7/2014.
 */
public class MTNotesView extends LinearLayout {
    private MTEditText mNotes;

    private String mPreviousNotesText = null;

    public MTNotesView(Activity activity) {
        super(activity);

        activity.setTheme(android.R.style.Theme_Holo_Light);

        LayoutInflater.from(activity).inflate(R.layout.view_notes, this);

        this.mNotes = (MTEditText) findViewById(R.id.itemDetailNotes);
        this.mNotes.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

//        this.mNotes.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                mPreviousNotesText = charSequence.toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 256) {
//                    mNotes.setText(mPreviousNotesText);
//                }
//            };
//        });
    }

    public String getNotes() {
        Editable notes = this.mNotes.getText();
        return notes.toString();
    }

    public void setNotes(String notes) {
        this.mNotes.setText(notes);
    }

}
