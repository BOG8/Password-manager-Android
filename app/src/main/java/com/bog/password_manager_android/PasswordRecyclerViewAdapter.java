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
    private List<Pair<String, String>> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewValue;
        public ViewHolder(LinearLayout v) {
            super(v);
            textViewName = (TextView)v.findViewById(R.id.fieldName);
            textViewValue = (TextView)v.findViewById(R.id.fieldValue);
        }
    }

    public PasswordRecyclerViewAdapter(PasswordModel password) {
        dataSet = new ArrayList<>();
        if (password != null) {
            dataSet.add(Pair.create("name", password.name));
            dataSet.add(Pair.create("password", password.password));
            for (Map.Entry<String, String> e : password.additionalFields.entrySet()) {
                dataSet.add(Pair.create(e.getKey(), e.getValue()));
            }
        }
    }

    @Override
    public PasswordRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_field_view, parent, false);

        return new ViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(dataSet.get(position).first);
        holder.textViewValue.setText(dataSet.get(position).second);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
