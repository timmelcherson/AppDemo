package com.example.appdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImagePickerRecyclerAdapter extends RecyclerView.Adapter<ImagePickerRecyclerAdapter.ViewHolder> {

    private List<ImageItem> mImageList;
    private OnImageItemsItemListener onImageItemsItemListener;

    public static final String TAG = "TAG";


    // Constructor
    public ImagePickerRecyclerAdapter(List<ImageItem> imageList, OnImageItemsItemListener onImageItemsItemListener) {
        this.mImageList = imageList;
        this.onImageItemsItemListener = onImageItemsItemListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mImageItemCard;
        private TextView mItemTitle;
        private ImageView mItemImage;
        private OnImageItemsItemListener onImageItemsItemListener;

        public ViewHolder(@NonNull View itemView, OnImageItemsItemListener onImageItemsItemListener) {
            super(itemView);
            mImageItemCard = itemView.findViewById(R.id.image_item_card);
            mItemTitle = itemView.findViewById(R.id.image_item_title);
            mItemImage = itemView.findViewById(R.id.image_item_src);
            this.onImageItemsItemListener = onImageItemsItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onImageItemsItemListener.onItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_card, parent, false);
        return new ViewHolder(view, onImageItemsItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ImageItem item = mImageList.get(position);
        holder.mItemImage.setImageResource(item.getImageResource());
        holder.mItemTitle.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public interface OnImageItemsItemListener {
        void onItemClick(int position);
    }
}
