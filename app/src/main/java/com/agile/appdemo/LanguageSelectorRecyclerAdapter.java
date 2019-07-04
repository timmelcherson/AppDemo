package com.agile.appdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LanguageSelectorRecyclerAdapter extends
        RecyclerView.Adapter<LanguageSelectorRecyclerAdapter.ViewHolder> {

    private OnLanguageSelectListener mLanguageSelectListener;
    private List<LanguageSelectItem> mLanguageList;

    // Constructor
    public LanguageSelectorRecyclerAdapter(List<LanguageSelectItem> list, OnLanguageSelectListener listener) {
        this.mLanguageList = list;
        this.mLanguageSelectListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mCardIcon;
        private TextView mCardTitle;
        private OnLanguageSelectListener mLanguageSelectListener;


        public ViewHolder(@NonNull View itemView, OnLanguageSelectListener listener) {
            super(itemView);
            mCardIcon = itemView.findViewById(R.id.flag_icon);
            mCardTitle = itemView.findViewById(R.id.language_text);
            this.mLanguageSelectListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mLanguageSelectListener.onItemClick(getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_select_card, parent, false);
        return new ViewHolder(view, mLanguageSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        LanguageSelectItem item = mLanguageList.get(position);
        holder.mCardIcon.setImageResource(item.getFlagIcon());
        holder.mCardTitle.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return mLanguageList.size();
    }

    public interface OnLanguageSelectListener {
        void onItemClick(int position);
    }
}