package com.bog.password_manager_android;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bog.password_manager_android.network.NetworkIntentService;
import com.bog.password_manager_android.network.NetworkManager;
import com.bog.password_manager_android.network.ServiceHelper;

import java.util.List;

import static com.bog.password_manager_android.MainActivity.*;

public class ResourceActivity extends AppCompatActivity
        implements ListRecyclerViewAdapter.IResourceEntryClickListener, ServiceHelper.downloadListener, ServiceHelper.uploadListener {

    private static String FRAGMENT_TAG = "some_tag";
    public static SharedPreferences preferences;
    private List<PasswordModel> resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences(PASSWORD_MANAGER, 0);
        fillResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (prevFragment == null) {
            ListFragment newFragment = new ListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment, FRAGMENT_TAG);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, prevFragment, FRAGMENT_TAG);
            transaction.commit();
        }
    }

    @Override
    public void onResourceEntryClick(int index) {
        PasswordFragment newFragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putInt(PasswordFragment.RESOURCE_INDEX, index);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fillResources() {
        String cipherData = preferences.getString(CIPHER_DATA, null);
        String iv = preferences.getString(IV, null);
        PasswordCipher cipher = PasswordCipher.getInstance();
        String clearData = cipher.decrypt(Converter.toByte(cipherData), Converter.toByte(iv));
        resources = PasswordsStringConvertor.deserialize(clearData);
    }

    public List<PasswordModel> getResourcesList() {
        return resources;
    }

    public void onAddResourceClick() {
        PasswordEditFragment newFragment = new PasswordEditFragment();
        Bundle args = new Bundle();
        args.putInt(PasswordFragment.RESOURCE_INDEX, PasswordEditFragment.NEW_RESOURCE);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onResourceEditClick(int index) {
        PasswordEditFragment newFragment = new PasswordEditFragment();
        Bundle args = new Bundle();
        args.putInt(PasswordFragment.RESOURCE_INDEX, index);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void saveState() {
        String data = PasswordsStringConvertor.serialize(resources);
        PasswordCipher cipher = PasswordCipher.getInstance();
        cipher.encrypt(data);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CIPHER_DATA, cipher.getCipherData());
        editor.putString(IV, cipher.getIv());
        editor.apply();
        cipher.clearCipher();
    }

    public void onDownloadResourcesClick() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.input_username_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.submit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ServiceHelper helper = ServiceHelper.getInstance(ResourceActivity.this);


                                PasswordCipher cipher = PasswordCipher.getInstance();
                                String password = cipher.getPassword();
                                String username = userInput.getText().toString();
                                helper.download(ResourceActivity.this, ResourceActivity.this, username, password);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }
                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void onDataLoaded(String cipherData, String iv, int resultCode) {
        if (resultCode == 200) {
            fillResources();

            // update
            ListFragment newFragment = new ListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment, FRAGMENT_TAG);
            transaction.commit();

            Toast.makeText(this, R.string.download_succes, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.download_error) + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    public void onUploadResourcesClick() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.input_username_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.submit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ServiceHelper helper = ServiceHelper.getInstance(ResourceActivity.this);

                                String cipherData = preferences.getString(CIPHER_DATA, null);
                                String iv = preferences.getString(IV, null);
                                PasswordCipher cipher = PasswordCipher.getInstance();
                                String password = cipher.getPassword();
                                String username = userInput.getText().toString();
                                helper.upload(ResourceActivity.this, ResourceActivity.this, username, password, cipherData, iv);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }
                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    @Override
    public void onDownload (String success) {
        onDataLoaded(null, null, NetworkManager.NETWORK_SUCCESS);
    }

    @Override
    public void onUpload(String success) {
        if (!success.equals(NetworkIntentService.UPLOAD_SUCCESS)) {
            Toast.makeText(this, getString(R.string.upload_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.upload_succes, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        ServiceHelper helper = ServiceHelper.getInstance(this);
        helper.removeDownloadListener();
        helper.removeUpLoadListener();
        super.onStop();
    }
}
