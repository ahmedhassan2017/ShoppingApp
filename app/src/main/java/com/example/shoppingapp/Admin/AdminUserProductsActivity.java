package com.example.shoppingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Adapters.CartAdapter;
import com.example.shoppingapp.Interfaces.OnItemListener;
import com.example.shoppingapp.Model.Cart;
import com.example.shoppingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminUserProductsActivity extends AppCompatActivity implements OnItemListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productsRef;
    private String UserID,TotalPrice;
    ArrayList<Cart> cartsList = new ArrayList<>();
    CartAdapter adapter;
    private TextView txtTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);
        UserID = getIntent().getStringExtra("UID");
        TotalPrice = getIntent().getStringExtra("total");

        recyclerView = findViewById(R.id.admin_products_list);
        txtTotalAmount = findViewById(R.id.admin_total_price);
        txtTotalAmount.setText((TotalPrice));

        productsRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(UserID)
                .child("Products");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CartAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(cartsList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                cartsList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Cart products = snapshot1.getValue(Cart.class);
                    cartsList.add(products);

                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminUserProductsActivity.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onDeleteClicked(int position) {

    }
}