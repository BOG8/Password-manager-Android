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
    List<PasswordModel> resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        SharedPreferences preferences = getSharedPreferences(PASSWORD_MANAGER, 0);
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
        // TODO: add save member "resources" to sharedPreferences/Server
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
        // TODO сохранить данные просто Олег
        if (resultCode != 200) {
            Toast.makeText(this, "Ошибка cкачивания данных " + resultCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Данные скачаны успешно ", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUploadResourcesClick() {
        // TODO: upload member "resources" to server
    }

}
