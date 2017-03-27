package com.treefrogapps.googlecontactsyncapp.common;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import io.reactivex.Observable;

public final class RxUtils {

    public static Observable<Intent> RxBroadcastReceiver(Context context, IntentFilter filter, boolean localBroadcast){
        return Observable.create(e -> {

            final BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override public void onReceive(Context context, Intent intent) {
                    e.onNext(intent);
                }
            };

            if(localBroadcast){
                LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
                e.setCancellable(() -> LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver));
            } else {
                context.registerReceiver(receiver, filter);
                e.setCancellable(() -> context.unregisterReceiver(receiver));
            }
        });
    }
}
