package com.bog.password_manager_android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class ResourceActivity extends AppCompatActivity
        implements ListRecyclerViewAdapter.IResourceEntryClickListener {

    private static String FRAGMENT_TAG = "some_tag";
    List<PasswordModel> resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        resources = PasswordsStringConvertor.deserialize(
            "[{\"name\": \"Yandex.ru\", \"password\": \"keks\", \"additionalFields\": {\"login\": \"abc\"}}," +
            "{\"name\": \"Rambler.ru\", \"password\": \"lol\", \"additionalFields\": {\"login\": \"ee\"}}]");

        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (prevFragment == null) {
            ListFragment newFragment = new ListFragment();
            newFragment.setClickListener(this);
            newFragment.setContent(resources);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.commitAllowingStateLoss();
        }
        else {
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
}
