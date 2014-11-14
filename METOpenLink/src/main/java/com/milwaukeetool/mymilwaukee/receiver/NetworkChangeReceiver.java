package com.milwaukeetool.mymilwaukee.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.milwaukeetool.mymilwaukee.model.event.MTNetworkAvailabilityEvent;
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;

import de.greenrobot.event.EventBus;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;

/**
 * Created by scott.hopfensperger on 11/13/2014.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        NetworkUtil.NetworkType type = NetworkUtil.getConnectivityStatus(context);

        switch(type) {
            case TYPE_NOT_CONNECTED:
                LOGD("NetworkChangeReceiver","Network not available");
                MTNetworkAvailabilityEvent e1 = new MTNetworkAvailabilityEvent(this, false);
                EventBus.getDefault().post(e1);
                break;
            default:
                LOGD("NetworkChangeReceiver","Network available");
                MTNetworkAvailabilityEvent e2 = new MTNetworkAvailabilityEvent(this, true);
                EventBus.getDefault().post(e2);
                break;
        }
    }
}
