package com.milwaukeetool.mymilwaukee.view_custom;

import android.content.Context;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;

import com.milwaukeetool.mymilwaukee.R;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTCreateAccountHeaderView extends RelativeLayout {

    public MTCreateAccountHeaderView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_create_account_header, this);
    }
}
