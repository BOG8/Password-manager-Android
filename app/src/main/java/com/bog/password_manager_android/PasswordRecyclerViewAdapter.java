package com.bog.password_manager_android;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 15.04.2017.
 */

public class PasswordRecyclerViewAdapter extends RecyclerView.Adapter<PasswordRecyclerViewAdapter.ViewHolder> {
    private List<Pair<String, String>> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextViewName;
        public TextView mTextViewValue;
        public ViewHolder(LinearLayout v) {
            super(v);
            v.setOnClickListener(this);
            mTextViewName = (TextView)v.findViewById(R.id.fieldName);
            mTextViewValue = (TextView)v.findViewById(R.id.fieldValue);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public PasswordRecyclerViewAdapter(PasswordModel password) {
        mDataset = new ArrayList<Pair<String, String>>();
        if (password != null) {
            mDataset.add(Pair.create("name", password.name));
            mDataset.add(Pair.create("password", password.password));
            for (Map.Entry<String, String> e : password.additionalFields.entrySet())
                mDataset.add(Pair.create(e.getKey(), e.getValue()));
        }
    }

    @Override
    public PasswordRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_field_view, parent, false);

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextViewName.setText(mDataset.get(position).first);
        holder.mTextViewValue.setText(mDataset.get(position).second);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
