package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;

/**
 * Created by scott.hopfensperger on 12/3/2014.
 */
public class MTInventoryResultItem extends RelativeLayout {
    private ImageView mToolImageView;
    private ImageView mAddToolImageView;
    private MTTextView mDescription;
    private MTTextView mModelNumber;

    public MTInventoryResultItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_item_search_result_list_item, this);

//        mToolImageView = (ImageView) this.findViewById(R.id.toolImageView);
//        mAddToolImageView = (ImageView) this.findViewById(R.id.addToolImageView);
//        mDescription = (MTTextView) this.findViewById(R.id.toolDescriptionTextView);
//        mModelNumber = (MTTextView) this.findViewById(R.id.toolModelNumberTextView);
//
//        mAddToolImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_searchgrey));
    }

//    public void setToolIcon(int resourceId) {
//        mToolImageView.setImageDrawable(this.getResources().getDrawable(resourceId));
//    }
//
//    public void setDescription(String description) {
//        this.mDescription.setText(description);
//    }
//
//    public void setModelNumber(String modelNumber) {
//        this.mModelNumber.setText(modelNumber);
//    }
}
