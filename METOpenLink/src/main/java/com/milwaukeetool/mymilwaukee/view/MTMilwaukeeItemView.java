package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTTextView;

/**
 * Created by cent146 on 12/4/14.
 */
public class MTMilwaukeeItemView extends RelativeLayout {

    public MTMilwaukeeItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MTMilwaukeeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public MTMilwaukeeItemView(Context context) {
        super(context);
        init(context);
    }


    private void init(Context context) {

        View rootView = View.inflate(context, R.layout.view_milwaukee_item, this);

        MTTextView milwaukeeModelText = (MTTextView) rootView.findViewById(R.id.searchMilwaukeeModelText);
        MTTextView otherModelText = (MTTextView) rootView.findViewById(R.id.searchOtherModelText);

        String string = MiscUtils.getString(R.string.add_item_milwaukee_search_message);
        int index = string.indexOf(MiscUtils.getString(R.string.search_replace_wildcard_string));

        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_searchgrey);
        drawable.setBounds(0,0, UIUtils.getPixels(25), UIUtils.getPixels(25));
        ImageSpan imageSpan = new ImageSpan(drawable);

        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(imageSpan, index, index+1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        milwaukeeModelText.setText(spannableString, TextView.BufferType.SPANNABLE);

        otherModelText.setText(Html.fromHtml(MiscUtils.getString(R.string.add_item_milwaukee_select_other_message)));
    }
}
