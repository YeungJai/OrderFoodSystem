package com.example.orderfoodsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.orderfoodsystem.Common.Common;
import com.example.orderfoodsystem.Model.Request;
import com.example.orderfoodsystem.ViewHoler.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    //Firebase Database
    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //I change the value from
        loadOrders(Common.currentUser.getName());
        loadOrders(Common.currentUser.getPhone());
    }

    //loadOrders() method
    private void loadOrders(String name) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("name").equalTo(name)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
            }
        };
        Toast.makeText(this, "Value: ", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Placed";
        }
        else if(status.equals("1")){
            return "On my way";
        }
        else {
            return "Shipped";
        }
    }
}
