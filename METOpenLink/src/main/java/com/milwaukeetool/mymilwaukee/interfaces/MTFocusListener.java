package com.milwaukeetool.mymilwaukee.interfaces;

import android.widget.EditText;

/**
 * Created by cent146 on 11/21/14.
 */
public interface MTFocusListener {
    public void didChangeFocus(boolean hasFocus, EditText editText, int listItemNumber);
}
