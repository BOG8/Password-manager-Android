package com.bog.password_manager_android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment
        implements ListRecyclerViewAdapter.IResourceEntryClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ListRecyclerViewAdapter.IResourceEntryClickListener onResourceEntryClickListener;
    private ResourceActivity resourceActivity;

    public ListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListRecyclerViewAdapter(resourceActivity.getResourcesList(), this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        resourceActivity = (ResourceActivity) activity;
        onResourceEntryClickListener = resourceActivity;
    }

    public void setContent(List<PasswordModel> passwords) {
        adapter = new ListRecyclerViewAdapter(passwords, this);
        if (recyclerView != null) {
            recyclerView.swapAdapter(adapter, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.passwordsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (adapter != null) {
            recyclerView.swapAdapter(adapter, false);
        } else {
            setContent(new ArrayList<PasswordModel>());
        }

        ((Button) view.findViewById(R.id.add_resource))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceActivity.onAddResourceClick();
                    }
                });

        ((Button) view.findViewById(R.id.download_resources))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceActivity.onDownloadResourcesClick();
                    }
                });

        ((Button) view.findViewById(R.id.upload_resources))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceActivity.onUploadResourcesClick();
                    }
                });

        return view;
    }

    @Override
    public void onResourceEntryClick(int index) {
        if (onResourceEntryClickListener != null) {
            onResourceEntryClickListener.onResourceEntryClick(index);
        }
    }
}