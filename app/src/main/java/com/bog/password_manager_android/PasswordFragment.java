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

public class PasswordFragment extends Fragment {
    public static final String RESOURCE_INDEX = "ResourceEditIndex";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ResourceActivity resourceActivity;
    private int resourceIndex = -1;

    public PasswordFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            resourceIndex = savedInstanceState.getInt(RESOURCE_INDEX);
            setContent(resourceActivity.getResourcesList().get(resourceIndex));
        } else {
            setContent(null);
        }
        //setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            resourceIndex = args.getInt(RESOURCE_INDEX);;
            setContent(resourceActivity.getResourcesList().get(resourceIndex));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        resourceActivity = (ResourceActivity) activity;
    }


    public void setContent(PasswordModel password) {
        mAdapter = new PasswordRecyclerViewAdapter(password);
        if (mRecyclerView != null) {
            mRecyclerView.swapAdapter(mAdapter, false);
        }
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

        if (mAdapter != null) {
            mRecyclerView.swapAdapter(mAdapter, false);
        } else {
            setContent(null);
        }

        ((Button) view.findViewById(R.id.edit_resource))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceActivity.onResourceEditClick(resourceIndex);
                    }
                });

        return view;
    }
}
