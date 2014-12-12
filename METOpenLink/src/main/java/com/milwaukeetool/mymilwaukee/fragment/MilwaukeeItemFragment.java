package com.milwaukeetool.mymilwaukee.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.AddItemActivity;
import com.milwaukeetool.mymilwaukee.interfaces.FirstPageFragmentListener;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/25/2014.
 */
public class MilwaukeeItemFragment extends MTFragment {
    private static final String TAG = makeLogTag(MilwaukeeItemFragment.class);

    private static final String ARG_POSITION = "position";

    private int position;

    private MenuItem mSearchMenuItem;

    static FirstPageFragmentListener mFirstPageListener;

    private AddItemActivity mAddItemActivity = null;

    public static MilwaukeeItemFragment newInstance(int position, FirstPageFragmentListener listener) {
        MilwaukeeItemFragment f = new MilwaukeeItemFragment(listener);
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    public MilwaukeeItemFragment(FirstPageFragmentListener listener) {

        mFirstPageListener = listener;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof AddItemActivity) {
            mAddItemActivity = (AddItemActivity)activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected String getScreenName() {
        return MiscUtils.getString(R.string.mt_screen_name_add_milwaukee_item);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.view_milwaukee_item, container, false);
//
//        MTTextView milwaukeeModelText = (MTTextView) rootView.findViewById(R.id.searchMilwaukeeModelText);
//        MTTextView otherModelText = (MTTextView) rootView.findViewById(R.id.searchOtherModelText);
//
//        String string = MiscUtils.getString(R.string.add_item_milwaukee_search_message);
//        int index = string.indexOf(MiscUtils.getString(R.string.search_replace_wildcard_string));
//
//        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_searchgrey);
//        drawable.setBounds(0,0, UIUtils.getPixels(25), UIUtils.getPixels(25));
//        ImageSpan imageSpan = new ImageSpan(drawable);
//
//        SpannableString spannableString = new SpannableString(string);
//        spannableString.setSpan(imageSpan, index, index+1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//        milwaukeeModelText.setText(spannableString, TextView.BufferType.SPANNABLE);
//
//        otherModelText.setText(Html.fromHtml(MiscUtils.getString(R.string.add_item_milwaukee_select_other_message)));
//        return rootView;
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.inventory_add_item_menu, menu);
//
//        final ActionBar actionBar = this.getActivity().getActionBar();
//        actionBar.setTitle(this.getResources().getString(R.string.main_add_item_title));
//
//        final SearchView searchView =
//                (SearchView) menu.findItem(R.id.inventory_search).getActionView();
//
//        mSearchMenuItem = menu.findItem(R.id.inventory_search);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                LOGD(TAG, "Submitting search query: " + s);
//
//                // Hide the keyboard
//                UIUtils.hideKeyboard(MilwaukeeItemFragment.this.getActivity());
//
//                // Remove the search from actionbar
//                //mSearchMenuItem.collapseActionView();
//
//                // Replace the fragment
//                mFirstPageListener.onSwitchToNextFragment();
//
//                // Make request to server
//                if (mAddItemActivity != null && !TextUtils.isEmpty(s)) {
//                    mAddItemActivity.performSearchRequest(s, 0);
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//
//                return true;
//
//            }
//
//        });
//
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//
//                // Remove the search from actionbar
//                mSearchMenuItem.collapseActionView();
//
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item == mSearchMenuItem) {
//            mSearchMenuItem.expandActionView();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void backPressed() {
//        mFirstPageListener.onSwitchToNextFragment();
//    }


}
