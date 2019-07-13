package com.example.orderfoodsystem.ViewHoler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfoodsystem.Interface.ItemClickListener;
import com.example.orderfoodsystem.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView drink_name, drink_price;
    public ImageView drink_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }


    public FoodViewHolder(View itemView) {
        super(itemView);

        drink_name = itemView.findViewById(R.id.drink_name);
        drink_price = itemView.findViewById(R.id.drink_price);
        drink_image = itemView.findViewById(R.id.drink_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(), false);

    }
}
