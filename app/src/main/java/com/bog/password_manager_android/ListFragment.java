package com.bog.password_manager_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment
        implements ListRecyclerViewAdapter.IResourceEntryClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ListRecyclerViewAdapter.IResourceEntryClickListener onResourceEntryClickListener;

    public ListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContent(List<PasswordModel> passwords) {
        mAdapter = new ListRecyclerViewAdapter(passwords, this);
        if (mRecyclerView != null) {
            mRecyclerView.swapAdapter(mAdapter, false);
        }
    }

    public void setClickListener(ListRecyclerViewAdapter.IResourceEntryClickListener listener) {
        onResourceEntryClickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.passwordsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mAdapter != null) {
            mRecyclerView.swapAdapter(mAdapter, false);
        } else {
            setContent(new ArrayList<PasswordModel>());
        }
        return view;
    }

    @Override
    public void onResourceEntryClick(int index) {
        if (onResourceEntryClickListener != null) {
            onResourceEntryClickListener.onResourceEntryClick(index);
        }
    }
}