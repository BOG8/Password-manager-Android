package com.bog.password_manager_android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alex on 15.04.2017.
 */

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {
    private List<PasswordModel> dataSet;
    private IResourceEntryClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        private int resourceIndex;
        IResourceEntryClickListener onClickListener;
        public ViewHolder(LinearLayout v, int index, IResourceEntryClickListener listener) {
            super(v);
            textView = (TextView)v.findViewById(R.id.resourceName);
            resourceIndex = index;
            onClickListener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onResourceEntryClick(resourceIndex);
            }
        }
    }

    public ListRecyclerViewAdapter(List<PasswordModel> myDataset,
                                   IResourceEntryClickListener listener) {
        dataSet = myDataset;
        onClickListener = listener;
    }

    @Override
    public ListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_view, parent, false);

        return new ViewHolder((LinearLayout) v, 0, onClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.resourceIndex = position;
        holder.textView.setText(dataSet.get(position).name);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface IResourceEntryClickListener {
        void onResourceEntryClick(int index);
    }
}
