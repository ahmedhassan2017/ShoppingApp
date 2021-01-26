package com.example.shoppingapp.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shoppingapp.Adapters.ProductsAdapter;
import com.example.shoppingapp.Interfaces.OnItemListener;
import com.example.shoppingapp.Model.Products;
import com.example.shoppingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchProductActivity extends AppCompatActivity implements OnItemListener {
    private Button searchButton;
    private EditText searchEditTxt;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String searchInput;
    DatabaseReference reference;
    ArrayList<Products> productslist = new ArrayList<>();
    ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        reference = FirebaseDatabase.getInstance().getReference().child("Products");


        searchButton = findViewById(R.id.search_btn);
        searchEditTxt = findViewById(R.id.search_edit_txt);
        recyclerView = findViewById(R.id.search_list);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductsAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(productslist);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = searchEditTxt.getText().toString();
                SearchForIt(searchInput);

    }

    private void SearchForIt(String searchInput) {
        reference.orderByChild("pname").startAt(searchInput).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productslist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Products products = snapshot1.getValue(Products.class);
                    productslist.add(products);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
        });
    }


    @Override
    public void onItemClicked(int position) {
        productslist.get(position);
        Intent intent = new Intent(SearchProductActivity.this, ProductDetailsActivity.class);
        intent.putExtra("PID", productslist.get(position).getPid());
        intent.putExtra("pname", productslist.get(position).getPname());
        intent.putExtra("description", productslist.get(position).getDescription());
        intent.putExtra("price", productslist.get(position).getPrice());
        intent.putExtra("image", productslist.get(position).getImage());
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(int position) {

    }
}