package com.bog.password_manager_android;

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
    private PasswordModel currentPassword;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextViewName;
        public EditText mTextViewValue;
        public Button mButtonRemove;
        public int fieldIndex;
        private PasswordEditRecyclerViewAdapter mlistener;
        public ViewHolder(LinearLayout v, int index, PasswordEditRecyclerViewAdapter listener) {
            super(v);
            fieldIndex = index;
            mlistener = listener;
            mTextViewName = (TextView)v.findViewById(R.id.fieldEditName);
            mTextViewValue = (EditText)v.findViewById(R.id.fieldEditValue);
            mButtonRemove = (Button) v.findViewById(R.id.fieldEditRemove);
            mButtonRemove.setOnClickListener(this);
            mTextViewValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mlistener.onFieldChange(fieldIndex, s.toString());
                }
            });
        }

        @Override
        public void onClick(View v) {
            mlistener.onFieldRemove(fieldIndex);
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
            holder.mTextViewName.setText("name");
            holder.mTextViewValue.setText(currentPassword.name);
            holder.mButtonRemove.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            holder.mTextViewName.setText("password");
            holder.mTextViewValue.setText(currentPassword.password);
            holder.mButtonRemove.setVisibility(View.INVISIBLE);
        }
        else {
            String key = (String) currentPassword.additionalFields.keySet().toArray()[position-2];
            holder.mTextViewName.setText(key);
            holder.mTextViewValue.setText(currentPassword.additionalFields.get(key));
            holder.mButtonRemove.setVisibility(View.VISIBLE);
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
        String key = (String) currentPassword.additionalFields.keySet().toArray()[index-2];
        currentPassword.additionalFields.remove(key);
        notifyDataSetChanged();
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
