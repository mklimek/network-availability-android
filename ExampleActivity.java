package com.foo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.foo.util.NetworkAvailability;

public class ExampleActivity extends Activity {

    private NetworkAvailability networkAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkAvailability = NetworkAvailability.getInstance();
        networkAvailability.registerNetworkAvailability(this, receiver);
    }

    @Override
    protected void onDestroy() {
        networkAvailability.unregisterNetworkAvailability(this, receiver);
        super.onDestroy();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                Toast.makeText(ExampleActivity.this, "network is not available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ExampleActivity.this, "network is available", Toast.LENGTH_LONG).show();
            }
        }
    };
}
