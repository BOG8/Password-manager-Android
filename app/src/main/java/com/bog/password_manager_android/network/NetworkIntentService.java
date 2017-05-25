package com.bog.password_manager_android.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;


public class NetworkIntentService extends IntentService {

    public final static String NETWORK_ACTION = "network_action";
    public final static String REGISTRATE_ACTION = "registrate";
    public final static String DOWNLOAD_ACTION = "download";
    public final static String UPLOAD_ACTION = "upload";
    public final static String REGISTRATE_SUCCESS = "registrate_success";
    public final static String REGISTRATE_FAILED = "registrate_failed";
    public final static String DOWNLOAD_SUCCESS = "DOWNLOAD_success";
    public final static String DOWNLOAD_FAILED = "DOWNLOAD_failed";
    public static final String UPLOAD_SUCCESS = "upload_success";
    public static final String UPLOAD_FAILED = "upload_failed";


    public final static String EXTRA_ACTION = "extra_action";
    public final static String EXTRA_USERNAME = "extra_username";
    public final static String EXTRA_PASSWORD = "extra_password";
    public final static String EXTRA_SUCCESS = "extra_SUCCESS";
    public static final String EXTRA_IV = "extra_iv";
    public static final String EXTRA_DATA = "extra_data";

    public NetworkIntentService() {
        super("NetworIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
        if (action.equals(REGISTRATE_ACTION)) {
            registrate(intent);
        }
        if (action.equals(DOWNLOAD_ACTION)) {
            download(intent);
        }
        if (action.equals(UPLOAD_ACTION)) {
            upload(intent);
        }
    }

    void download(Intent intent) {
        String username = intent.getStringExtra(EXTRA_USERNAME);
        String password = intent.getStringExtra(EXTRA_PASSWORD);

        NetworkManager manager = NetworkManager.getInstance();
        boolean result = manager.downloadData(username, password);

        final Intent intentBroadcast = new Intent(NETWORK_ACTION);
        intentBroadcast.putExtra(EXTRA_SUCCESS, result ? DOWNLOAD_SUCCESS : DOWNLOAD_FAILED);
        intentBroadcast.putExtra(EXTRA_ACTION, DOWNLOAD_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
    };

    void upload(Intent intent) {
        String username = intent.getStringExtra(EXTRA_USERNAME);
        String password = intent.getStringExtra(EXTRA_PASSWORD);
        String data = intent.getStringExtra(EXTRA_DATA);
        String iv = intent.getStringExtra(EXTRA_IV);

        NetworkManager manager = NetworkManager.getInstance();
        boolean result = manager.uploadData(username, password, data, iv);

        final Intent intentBroadcast = new Intent(NETWORK_ACTION);
        intentBroadcast.putExtra(EXTRA_SUCCESS, result ? UPLOAD_SUCCESS : UPLOAD_FAILED);
        intentBroadcast.putExtra(EXTRA_ACTION, UPLOAD_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
    };


    void registrate(Intent intent) {
        String username = intent.getStringExtra(EXTRA_USERNAME);
        String password = intent.getStringExtra(EXTRA_PASSWORD);

        NetworkManager manager = NetworkManager.getInstance();
        boolean result = manager.registrate(username, password);

        final Intent intentBroadcast = new Intent(NETWORK_ACTION);
        intentBroadcast.putExtra(EXTRA_SUCCESS, result ? REGISTRATE_SUCCESS : REGISTRATE_FAILED);
        intentBroadcast.putExtra(EXTRA_ACTION, REGISTRATE_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
    }
}
