package com.example.android.inventoryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

public class Adapter extends ListAdapter<Item, Adapter.Holder> {

    private OnItemClickListener listener;

    public Adapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getPrice().equals(newItem.getPrice()) &&
                    oldItem.getQuantity().equals(newItem.getQuantity());
        }
    };

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Item currentItem = getItem(position);
        holder.textView_title.setText(currentItem.getTitle());
        holder.textView_description.setText(currentItem.getPrice());
        holder.textView_priority.setText(String.valueOf(currentItem.getQuantity()));

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

    public Item getItemAt(int position) {
        return getItem(position);
    }

    class Holder extends ViewHolder {
        private TextView textView_title;
        private TextView textView_description;
        private TextView textView_priority;
        private ImageView imageView_image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.text_view_title);
            textView_description = itemView.findViewById(R.id.text_view_description);
            textView_priority = itemView.findViewById(R.id.text_view_priority);
            imageView_image = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
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
