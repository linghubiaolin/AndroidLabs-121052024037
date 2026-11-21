package com.fjnu.lab3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 实验点 5 的核心：RecyclerView.Adapter + ViewHolder。
 * onCreateViewHolder 只做布局实例化，onBindViewHolder 只做数据绑定，
 * 因此复用 convertView 时不会出现错位（数据与视图一一对应）。
 */
public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, Animal animal);
    }

    private final List<Animal> data;
    private final OnItemClickListener listener;

    public AnimalAdapter(List<Animal> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = data.get(position);
        holder.tvName.setText(animal.getName());
        holder.ivPhoto.setImageResource(animal.getPhotoRes());
        holder.tvIndex.setText("No." + (position + 1));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currentPosition, data.get(currentPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /** 追加一条数据并做「局部刷新」，避免 notifyDataSetChanged 造成的整表重绘。 */
    public void addItem(Animal animal) {
        data.add(0, animal);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, data.size());
    }

    static class AnimalViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPhoto;
        final TextView tvName;
        final TextView tvIndex;

        AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvName = itemView.findViewById(R.id.tv_name);
            tvIndex = itemView.findViewById(R.id.tv_index);
        }
    }
}
