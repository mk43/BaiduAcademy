package org.fitzeng.recurrentanr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

public class BroadcastANR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        Log.i("BroadcastANR", "myLooper=" + myLooper + ";   mainLooper=" + mainLooper);
        try {
            Thread.sleep(80 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
