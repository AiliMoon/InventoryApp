package com.example.android.inventoryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder>{

    private OnItemClickListener listener;
    private List<Item> items = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Item currentItem = items.get(position);
        holder.textView_title.setText(currentItem.getTitle());
        holder.textView_price.setText(String.valueOf(currentItem.getPrice()));
        holder.textView_quantity.setText(String.valueOf(currentItem.getQuantity()));

        String image = currentItem.getImage();

        if (image != null) {
            Glide.with(holder.imageView_image.getContext())
                    .load(image)
                    .into(holder.imageView_image);
        }
        else {
            Glide.with(holder.imageView_image.getContext())
                    .load(R.drawable.image_no)
                    .into(holder.imageView_image);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Item getItemAt(int position) {
        return items.get(position);
    }

    class Holder extends ViewHolder {
        private TextView textView_title;
        private TextView textView_price;
        private TextView textView_quantity;
        private ImageView imageView_image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.text_view_title);
            textView_price = itemView.findViewById(R.id.text_view_description);
            textView_quantity = itemView.findViewById(R.id.text_view_priority);
            imageView_image = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(items.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
