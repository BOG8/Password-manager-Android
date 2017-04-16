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


public class PasswordEditFragment extends Fragment {
    public static final String RESOURCE_INDEX = "ResourceIndex";
    public static final String FIELDS_STATE = "FieldsState";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ResourceActivity resourceActivity;
    private PasswordModel currentPassword;
    private int resourceIndex = -1;

    public PasswordEditFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            resourceIndex = savedInstanceState.getInt(RESOURCE_INDEX);
            if (savedInstanceState.containsKey(FIELDS_STATE)) {
                currentPassword = (PasswordModel) savedInstanceState.getSerializable(FIELDS_STATE);
                mAdapter = new PasswordEditRecyclerViewAdapter(currentPassword, resourceIndex);
            } else {
                setContent(resourceIndex);
            }
        } else {
            setContent(-1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (resourceIndex == -1) {
            Bundle args = getArguments();
            if (args != null) {
                resourceIndex = args.getInt(RESOURCE_INDEX);
                setContent(resourceIndex);
            } else {
                setContent(-1);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        resourceActivity = (ResourceActivity) activity;
    }

    private void setContent(int resourceIndex) {
        if (resourceIndex != -1) {
            PasswordModel temp = resourceActivity.getResourcesList().get(resourceIndex);
            currentPassword = temp.clone();
        }
        else
            currentPassword = null;
        mAdapter = new PasswordEditRecyclerViewAdapter(currentPassword, resourceIndex);
        if (mRecyclerView != null) {
            mRecyclerView.swapAdapter(mAdapter, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_edit, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.passwordEditFields);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mAdapter != null) {
            mRecyclerView.swapAdapter(mAdapter, false);
        } else {
            setContent(-1);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(FIELDS_STATE, currentPassword);
    }
}
