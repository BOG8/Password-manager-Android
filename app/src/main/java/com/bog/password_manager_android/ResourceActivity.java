package com.bog.password_manager_android;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import static com.bog.password_manager_android.MainActivity.*;

public class ResourceActivity extends AppCompatActivity
        implements ListRecyclerViewAdapter.IResourceEntryClickListener {

    private static String FRAGMENT_TAG = "some_tag";
    List<PasswordModel> resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        fillResources();

        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (prevFragment == null) {
            ListFragment newFragment = new ListFragment();
            newFragment.setClickListener(this);
            newFragment.setContent(resources);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.commitAllowingStateLoss();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, prevFragment, FRAGMENT_TAG);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onResourceEntryClick(int index) {
        PasswordFragment newFragment = new PasswordFragment();
        newFragment.setContent(resources.get(index));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void fillResources() {
        SharedPreferences preferences = getSharedPreferences(PASSWORD_MANAGER, 0);
        String cipherData = preferences.getString(CIPHER_DATA, null);
        String iv = preferences.getString(IV, null);
        PasswordCipher cipher = PasswordCipher.getInstance();
        String clearData = cipher.decrypt(Converter.toByte(cipherData), Converter.toByte(iv));
        resources = PasswordsStringConvertor.deserialize(clearData);
    }
}
