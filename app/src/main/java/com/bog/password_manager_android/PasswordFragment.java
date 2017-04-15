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


public class PasswordFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ListRecyclerViewAdapter.IResourceEntryClickListener onResourceEntryClickListener;

    public PasswordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setContent(PasswordModel password) {
        mAdapter = new PasswordRecyclerViewAdapter(password);
        if (mRecyclerView != null)
            mRecyclerView.swapAdapter(mAdapter, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.passwordFields);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mAdapter != null)
            mRecyclerView.swapAdapter(mAdapter, false);
        else
            setContent(null);

        return view;
    }
}
