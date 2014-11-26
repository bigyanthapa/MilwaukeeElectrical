package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.view.MTTextView;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/25/2014.
 */
public class MilwaukeeItemFragment extends Fragment {
    private static final String TAG = makeLogTag(MilwaukeeItemFragment.class);

    private static final String ARG_POSITION = "position";

    private int position;

    public static MilwaukeeItemFragment newInstance(int position) {
        MilwaukeeItemFragment f = new MilwaukeeItemFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_milwaukee_item, container, false);

        MTTextView milwaukeeModelText = (MTTextView) rootView.findViewById(R.id.searchMilwaukeeModelText);
        MTTextView otherModelText = (MTTextView) rootView.findViewById(R.id.searchOtherModelText);

        String string = "Search for a Milwaukee model number using the * icon above.";
        int index = string.indexOf("*");

        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_searchgrey);
        drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable);

        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(imageSpan, index, index+1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        milwaukeeModelText.setText(spannableString, TextView.BufferType.SPANNABLE);

        otherModelText.setText(Html.fromHtml("Select <strong>OTHER</strong> to enter an item from a different manufacturer"));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_add_item_menu, menu);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_add_item_title));
    }
}
