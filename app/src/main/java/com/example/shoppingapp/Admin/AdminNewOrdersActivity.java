package com.example.shoppingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.shoppingapp.Adapters.AdminOrdersAdapter;
import com.example.shoppingapp.Interfaces.OnItemListener;
import com.example.shoppingapp.Model.AdminOrders;
import com.example.shoppingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNewOrdersActivity extends AppCompatActivity implements OnItemListener {
    private RecyclerView OrdersRecyclerView;
    private DatabaseReference ordersRef;
    ArrayList<AdminOrders> orderslist = new ArrayList<>();
    AdminOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        OrdersRecyclerView = findViewById(R.id.orders_list);
        OrdersRecyclerView.setHasFixedSize(true);
        OrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminOrdersAdapter(this);
        OrdersRecyclerView.setAdapter(adapter);
        adapter.setList(orderslist);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderslist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    AdminOrders orders = snapshot1.getValue(AdminOrders.class);
                    orderslist.add(orders);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }




    private void RemoveOrder(String userID) {
        ordersRef.child(userID).removeValue();
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
        intent.putExtra("UID", orderslist.get(position).getUID());
        intent.putExtra("total", orderslist.get(position).getTotalAmount());
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(int position) {
        final String UID = orderslist.get(position).getUID();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        RemoveOrder(UID);

                        break;

                }

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}