package com.bog.password_manager_android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
        setHasOptionsMenu(true);
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

        view.findViewById(R.id.add_resource)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceActivity.onAddResourceClick();
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

    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        inflater.inflate(R.menu.password_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.download_resources:
                resourceActivity.onDownloadResourcesClick();
                return true;
            case R.id.upload_resources:
                resourceActivity.onUploadResourcesClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}