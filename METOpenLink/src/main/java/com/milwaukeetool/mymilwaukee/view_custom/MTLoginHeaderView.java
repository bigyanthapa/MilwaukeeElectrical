package com.milwaukeetool.mymilwaukee.view_custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTLoginHeaderView extends RelativeLayout {

    public MTLoginHeaderView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_login_header, this);
    }
}
