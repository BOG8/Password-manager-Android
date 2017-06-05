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
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
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
        adapter = new PasswordRecyclerViewAdapter(password, getContext());
        if (recyclerView != null) {
            recyclerView.swapAdapter(adapter, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.passwordFields);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (adapter != null) {
            recyclerView.swapAdapter(adapter, false);
        } else {
            setContent(null);
        }

        view.findViewById(R.id.edit_resource)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceActivity.onResourceEditClick(resourceIndex);
                    }
                });

        return view;
    }
}
