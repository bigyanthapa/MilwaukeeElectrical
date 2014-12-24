package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.AnalyticUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

/**
 * Created by scott.hopfensperger on 12/7/2014.
 */
public class MTNotesView extends LinearLayout {
    private MTEditText mNotes;

    public MTNotesView(Activity activity) {
        super(activity);

        AnalyticUtils.logScreenView(activity, MiscUtils.getString(R.string.mt_screen_name_add_notes));

        //activity.setTheme(android.R.style.Theme_Holo_Light);

        LayoutInflater.from(activity).inflate(R.layout.view_notes, this);

        this.mNotes = (MTEditText) findViewById(R.id.itemDetailNotes);
        this.mNotes.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        this.mNotes.setImeOptions(EditorInfo.IME_ACTION_NONE);
    }

    public String getNotes() {
        Editable notes = this.mNotes.getText();
        return notes.toString();
    }

    public void setNotes(String notes) {
        this.mNotes.setText(notes);
    }

}
