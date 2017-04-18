package com.bog.password_manager_android;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import static com.bog.password_manager_android.MainActivity.*;

public class ResourceActivity extends AppCompatActivity
        implements ListRecyclerViewAdapter.IResourceEntryClickListener {

    private static String FRAGMENT_TAG = "some_tag";
    SharedPreferences preferences;
    List<PasswordModel> resources;

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
//        PasswordEditFragment newFragment = new PasswordEditFragment();
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
        NetworkManager manager = NetworkManager.getInstance();

        manager.setDownloadCallback(new NetworkManager.DownloadCallback() {
            @Override
            public void onDownLoaded(String cipherData, String iv, int resultCode) {
                onDataLoaded(cipherData, iv, resultCode);
            }
        });

        PasswordCipher cipher = PasswordCipher.getInstance();
        String password = cipher.getPassword();
        String username = "lenovo"; //testing

        manager.downloadData(username, password);
    }

    private void onDataLoaded(String cipherData, String iv, int resultCode) {
        if (resultCode == 200) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CIPHER_DATA, cipherData);
            editor.putString(IV, iv);
            editor.apply();
            Toast.makeText(this, R.string.download_succes, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.download_error + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    public void onUploadResourcesClick() {
        String cipherData = preferences.getString(CIPHER_DATA, null);
        String iv = preferences.getString(IV, null);
        // TODO: upload member "resources" to server
        PasswordCipher cipher = PasswordCipher.getInstance();
        String password = cipher.getPassword();
        String username = "fizkek"; //testing

        NetworkManager manager = NetworkManager.getInstance();
        manager.setUploadCallback(new NetworkManager.UploadCallback() {
            @Override
            public void onUploaded(Integer resultCode) {
                onDataUploaded(resultCode);
            }
        });
        manager.uploadData(username, password, cipherData, iv);
    }

    public void onDataUploaded(Integer resultCode) {
        if (resultCode != 200) {
            Toast.makeText(this, R.string.upload_error + resultCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.upload_succes, Toast.LENGTH_SHORT).show();
        }
    }

}
