package com.milwaukeetool.mymilwaukee.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
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

        String string = MiscUtils.getString(R.string.add_item_milwaukee_search_message);
        int index = string.indexOf(MiscUtils.getString(R.string.search_replace_wildcard_string));

        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_searchgrey);
        drawable.setBounds(0,0, UIUtils.getPixels(25), UIUtils.getPixels(25));
        ImageSpan imageSpan = new ImageSpan(drawable);

        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(imageSpan, index, index+1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        milwaukeeModelText.setText(spannableString, TextView.BufferType.SPANNABLE);

        otherModelText.setText(Html.fromHtml(MiscUtils.getString(R.string.add_item_milwaukee_select_other_message)));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inventory_add_item_menu, menu);

        ActionBar actionBar = this.getActivity().getActionBar();
        actionBar.setTitle(this.getResources().getString(R.string.main_add_item_title));

        SearchManager searchManager =
                (SearchManager) this.getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.inventory_search).getActionView();
        ComponentName a = this.getActivity().getComponentName();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(this.getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                return true;

            }

        });
    }
}
