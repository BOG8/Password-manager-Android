package com.bog.password_manager_android.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class ServiceHelper {
    private static ServiceHelper instance =  null;

    public interface registrationListener {
        void onRegistrationResult(final String result);
    }

    public interface uploadListener {
        void onUpload(final String result);
    }

    public interface downloadListener {
        void onDownload(final String result);
    }

    private registrationListener regListener;
    private uploadListener upListener;
    private downloadListener downListener;

    private ServiceHelper() {

    }

    synchronized static public ServiceHelper getInstance(final Context context) {
        if (instance == null) {
            instance = new ServiceHelper();
            instance.initBroadcastReceiver(context);
        }
        return instance;
    }

    private void initBroadcastReceiver(Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(NetworkIntentService.NETWORK_ACTION);
        //filter.addAction(NetworkIntentService.REGISTRATE_SUCCESS);

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (regListener != null || upListener != null || downListener != null) {
                    final String result = intent.getStringExtra(NetworkIntentService.EXTRA_SUCCESS);
                    final String action = intent.getStringExtra(NetworkIntentService.EXTRA_ACTION);
                    if (action.equals(NetworkIntentService.REGISTRATE_ACTION)) {
                        regListener.onRegistrationResult(result);
                    }
                    if (action.equals(NetworkIntentService.DOWNLOAD_ACTION)) {
                        downListener.onDownload(result);
                    }
                    if (action.equals(NetworkIntentService.UPLOAD_ACTION)) {
                        upListener.onUpload(result);
                    }
                }
            }
        }, filter);
    }

    public void registrate(final Context context, final registrationListener listener, final String username, final String password) {
        regListener = listener;

        Intent intent = new Intent(context, NetworkIntentService.class);
        intent.putExtra(NetworkIntentService.EXTRA_ACTION, NetworkIntentService.REGISTRATE_ACTION);
        intent.putExtra(NetworkIntentService.EXTRA_USERNAME, username);
        intent.putExtra(NetworkIntentService.EXTRA_PASSWORD, password);
        context.startService(intent);
    }

    public void upload(final Context context, final uploadListener listener,final String username,
                       final String password, final String cipherData, final String iv) {
        upListener = listener;

        Intent intent = new Intent(context, NetworkIntentService.class);
        intent.putExtra(NetworkIntentService.EXTRA_ACTION, NetworkIntentService.UPLOAD_ACTION);
        intent.putExtra(NetworkIntentService.EXTRA_USERNAME, username);
        intent.putExtra(NetworkIntentService.EXTRA_PASSWORD, password);
        intent.putExtra(NetworkIntentService.EXTRA_DATA, cipherData);
        intent.putExtra(NetworkIntentService.EXTRA_IV, iv);
        context.startService(intent);
    }

    public void download(final Context context, final downloadListener listener, final String username, final String password) {
        downListener = listener;

        Intent intent = new Intent(context, NetworkIntentService.class);
        intent.putExtra(NetworkIntentService.EXTRA_ACTION, NetworkIntentService.DOWNLOAD_ACTION);
        intent.putExtra(NetworkIntentService.EXTRA_USERNAME, username);
        intent.putExtra(NetworkIntentService.EXTRA_PASSWORD, password);
        context.startService(intent);

    }


    public void removeRegListener() {
        regListener = null;
    }

    public void removeUpLoadListener() {
        upListener = null;
    }

    public void removeDownloadListener() {
        downListener = null;
    }



}
