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
    private List<PasswordModel> mDataset;
    private IResourceEntryClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        private int resourceIndex;
        IResourceEntryClickListener onClickListener;
        public ViewHolder(LinearLayout v, int index, IResourceEntryClickListener listener) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.resourceName);
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
        mDataset = myDataset;
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
        holder.mTextView.setText(mDataset.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface IResourceEntryClickListener {
        void onResourceEntryClick(int index);
    }
}
