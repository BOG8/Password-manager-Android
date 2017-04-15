package com.bog.password_manager_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ResourceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        List<PasswordModel> res = PasswordsStringConvertor.deserialize(
                "[{\"name\": \"Yandex.ru\", \"password\": \"keks\", \"additionalFields\": {\"login\": \"abc\"}}," +
                        "{\"name\": \"Rambler.ru\", \"password\": \"lol\", \"additionalFields\": {\"login\": \"ee\"}}]");

        mRecyclerView = (RecyclerView) findViewById(R.id.passwordsList);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(res);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }






}
