package com.bog.password_manager_android;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PasswordEditRecyclerViewAdapter
        extends RecyclerView.Adapter<PasswordEditRecyclerViewAdapter.ViewHolder> {
    private final static int PASSWORD_SIZE = 10;
    private PasswordModel currentPassword;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;
        public EditText textViewValue;
        public Button buttonRemove;
        public int fieldIndex;
        private PasswordEditRecyclerViewAdapter listener;
        public ViewHolder(LinearLayout v, int index, PasswordEditRecyclerViewAdapter listener) {
            super(v);
            fieldIndex = index;
            this.listener = listener;
            textViewName = (TextView)v.findViewById(R.id.fieldEditName);
            textViewValue = (EditText)v.findViewById(R.id.fieldEditValue);
            buttonRemove = (Button) v.findViewById(R.id.fieldEditRemove);
            buttonRemove.setOnClickListener(this);
            textViewValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    ViewHolder.this.listener.onFieldChange(fieldIndex, s.toString());
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onFieldRemove(fieldIndex);
        }
    }

    public PasswordEditRecyclerViewAdapter(PasswordModel password) {
        currentPassword = password;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_edit_field_view, parent, false);

        return new ViewHolder((LinearLayout) v, -1, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fieldIndex = position;
        if (position == 0) {
            holder.textViewName.setText(R.string.field_name_label);
            holder.textViewValue.setText(currentPassword.name);
            holder.buttonRemove.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            holder.textViewName.setText(R.string.field_password_label);
            holder.textViewValue.setText(currentPassword.password);
            holder.buttonRemove.setBackgroundResource(R.drawable.ic_refresh_black_24dp);
            holder.buttonRemove.setVisibility(View.VISIBLE);
        }
        else {
            String key = (String) currentPassword.additionalFields.keySet().toArray()[position-2];
            holder.textViewName.setText(key);
            holder.textViewValue.setText(currentPassword.additionalFields.get(key));
            holder.buttonRemove.setBackgroundResource(R.drawable.ic_delete_black_24dp);
            holder.buttonRemove.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (currentPassword != null) {
            return 2 + currentPassword.additionalFields.size();
        } else {
            return 0;
        }
    }

    public void onFieldRemove(int index) {
        if (index == 1) {
            currentPassword.password = new PasswordGenerator().generatePassword(PASSWORD_SIZE);
            notifyDataSetChanged();
        } else {
            String key = (String) currentPassword.additionalFields.keySet().toArray()[index - 2];
            currentPassword.additionalFields.remove(key);
            notifyDataSetChanged();
        }
    }

    public void onFieldChange(int index, String value) {
        if (index == 0) {
            currentPassword.name = value;
        } else if (index == 1) {
            currentPassword.password = value;
        } else {
            String key = (String) currentPassword.additionalFields.keySet().toArray()[index-2];
            currentPassword.additionalFields.put(key, value);
        }
    }
}
