package com.milwaukeetool.mymilwaukee.adapter;

import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.interfaces.MTAlertDialogListener;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

/**
 * Created by scott.hopfensperger on 12/16/2014.
 */
public class MTAlertDialogAdapter implements MTAlertDialogListener{
    private MTActivity mActivity;

    public MTAlertDialogAdapter(MTActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void didTapCancel() {
        UIUtils.hideKeyboard(this.getActivity());
    }

    @Override
    public void didTapOkWithResult(Object result) {}

    public MTActivity getActivity() {
        return mActivity;
    }
}
