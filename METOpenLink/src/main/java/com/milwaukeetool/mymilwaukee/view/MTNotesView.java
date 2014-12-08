package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.milwaukeetool.mymilwaukee.R;

/**
 * Created by scott.hopfensperger on 12/7/2014.
 */
public class MTNotesView extends LinearLayout {
    private MTEditText mNotes;

    public MTNotesView(Activity activity) {
        super(activity);

        LayoutInflater.from(activity).inflate(R.layout.view_notes, this);

        this.mNotes = (MTEditText) findViewById(R.id.itemDetailNotes);
    }

    public MTEditText getNotes() {
        return this.mNotes;
    }
}
