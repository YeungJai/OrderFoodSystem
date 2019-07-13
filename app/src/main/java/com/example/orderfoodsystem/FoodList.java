package com.example.orderfoodsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfoodsystem.Common.Common;
import com.example.orderfoodsystem.Interface.ItemClickListener;
import com.example.orderfoodsystem.Model.Category;
import com.example.orderfoodsystem.Model.Drink;
import com.example.orderfoodsystem.ViewHoler.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference drinkList;

    String categoryId="";

    FirebaseRecyclerAdapter<Drink, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        drinkList = database.getReference("Drink");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_drink);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null)
        {
            loadListDrink(categoryId);
        }

    }

    private void loadListDrink(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Drink, FoodViewHolder>(Drink.class,
                R.layout.food_item,
                FoodViewHolder.class,
                drinkList.orderByChild("MenuId").equalTo(categoryId)
                ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Drink drink, int i) {
                viewHolder.drink_name.setText(drink.getName());
                viewHolder.drink_price.setText(drink.getPrice());
                Picasso.with(getBaseContext()).load(drink.getImage())
                        .into(viewHolder.drink_image);

                final Drink local = drink;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        //Set Adapter
        Log.d("TAG",""+adapter.getItemCount());
        recyclerView.setAdapter(adapter);

    }


}
 