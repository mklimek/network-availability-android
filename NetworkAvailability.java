package com.foo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.NonNull;

public class NetworkAvailability {

    /* TODO: CHANGE THE ACTION NAME TO BE RELEVANT TO YOUR PROJECT */
    public static final String NETWORK_AVAILABILITY_ACTION = "com.foo.NETWORK_AVAILABILITY_ACTION";

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    private static NetworkAvailability instance;

    private NetworkAvailability() {
    }

    public static NetworkAvailability getInstance(){
        if(instance == null){
            instance = new NetworkAvailability();
        }
        return instance;
    }

    public static boolean isAvailable(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void registerNetworkAvailability(final Context context, BroadcastReceiver networkAvailabilityReceiver) {

        context.registerReceiver(networkAvailabilityReceiver, new IntentFilter(NETWORK_AVAILABILITY_ACTION));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context.registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } else{
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    context.sendBroadcast(getNetworkAvailabilityIntent(true));
                }

                @Override
                public void onLost(Network network) {
                    context.sendBroadcast(getNetworkAvailabilityIntent(false));
                }
            };
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
            if(isAvailable(context)){
                context.sendBroadcast(getNetworkAvailabilityIntent(true));
            } else{
                context.sendBroadcast(getNetworkAvailabilityIntent(false));
            }
        }
    }

    public void unregisterNetworkAvailability(Context context, BroadcastReceiver networkAvailabilityReceiver){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context.unregisterReceiver(connectivityChangeReceiver);
        } else{
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
        context.unregisterReceiver(networkAvailabilityReceiver);
    }

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                context.sendBroadcast(getNetworkAvailabilityIntent(false));
            } else {
                context.sendBroadcast(getNetworkAvailabilityIntent(true));
            }
        }
    };

    @NonNull
    private Intent getNetworkAvailabilityIntent(boolean isNetworkAvailable) {
        Intent intent = new Intent(NETWORK_AVAILABILITY_ACTION);
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, !isNetworkAvailable);
        return intent;
    }
}
