package com.bog.password_manager_android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class PasswordEditFragment extends Fragment {
    public static final String RESOURCE_INDEX = "ResourceEditIndex";
    public static final String FIELDS_STATE = "FieldsEditState";
    public static final int NONE_RESOURCE = -1;
    public static final int NEW_RESOURCE = -2;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ResourceActivity resourceActivity;
    private PasswordModel currentPassword;
    private int resourceIndex = NONE_RESOURCE;

    public PasswordEditFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            resourceIndex = savedInstanceState.getInt(RESOURCE_INDEX);
            if (savedInstanceState.containsKey(FIELDS_STATE)) {
                currentPassword = (PasswordModel) savedInstanceState.getSerializable(FIELDS_STATE);
                mAdapter = new PasswordEditRecyclerViewAdapter(currentPassword);
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
        if (resourceIndex == NONE_RESOURCE) {
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
        if (resourceIndex == NONE_RESOURCE) {
            currentPassword = null;
        } else if (resourceIndex == NEW_RESOURCE) {
            currentPassword = new PasswordModel();
            this.resourceIndex = resourceIndex;
        } else {
            currentPassword = resourceActivity.getResourcesList().get(resourceIndex).clone();
        }
        mAdapter = new PasswordEditRecyclerViewAdapter(currentPassword);
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

        Button addFieldButton = (Button) view.findViewById(R.id.add_field);
        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddFieldClick();
            }
        });

        Button removeResourceButton = (Button) view.findViewById(R.id.remove_resource);
        removeResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveResourceClick();
            }
        });

        Button saveResourceButton = (Button) view.findViewById(R.id.save_resource);
        saveResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveResourceClick();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(FIELDS_STATE, currentPassword);
    }

    private void onAddFieldClick() {
        Context context = getContext();
        View promptsView = LayoutInflater.from(context).inflate(R.layout.prompt_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.submit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                currentPassword.additionalFields.put(userInput.getText().toString(), "");
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void onRemoveResourceClick() {
        if (resourceIndex != NEW_RESOURCE) {
            resourceActivity.getResourcesList().remove(resourceIndex);
            resourceActivity.saveState();
            getActivity().getSupportFragmentManager().popBackStack(0,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void onSaveResourceClick() {
        if (resourceIndex == NEW_RESOURCE) {
            resourceActivity.getResourcesList().add(currentPassword);
            resourceActivity.saveState();
            getFragmentManager().popBackStack();
        } else {
            resourceActivity.getResourcesList().set(resourceIndex, currentPassword);
            resourceActivity.saveState();
            getFragmentManager().popBackStack();
        }
    }
}
