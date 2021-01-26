package com.example.shoppingapp.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Adapters.CartAdapter;
import com.example.shoppingapp.Interfaces.OnItemListener;
import com.example.shoppingapp.Model.Cart;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements OnItemListener {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;
    private DatabaseReference ProductsRef;
    private DatabaseReference ProductsRef2;


    ArrayList<Cart> cartsList = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

            recyclerView = findViewById(R.id.cart_list);
        txtMsg1 = findViewById(R.id.msg1);
        NextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount = findViewById(R.id.total_price);
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("total", txtTotalAmount.getText().toString());
                startActivity(intent);
            }
        });
        ProductsRef2 = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products");

        ProductsRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child(Prevalent.currentOnlineUser.getPhone())
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


        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                double overTotalPrice = 0;
                cartsList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Cart products = snapshot1.getValue(Cart.class);
                    cartsList.add(products);
                    // counting Total Price
                    double productPrice = (Double.parseDouble(cartsList.get(i).getPrice())) * (Double.parseDouble(cartsList.get(i).getQuantity()));
                    overTotalPrice += productPrice;
                    i++;
                }

                adapter.notifyDataSetChanged();
                txtTotalAmount.setText(" Total = "+String.valueOf(overTotalPrice));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });
        CheckOrderState();
    }



    // check if order shipped or not
    private void CheckOrderState() {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();
                    if (shippingState.equals("Shipped")) {
                        recyclerView.setVisibility(View.GONE);
                        NextProcessBtn.setVisibility(View.GONE);
                        txtTotalAmount.setText("You can purchase more products once you received your last order");
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulation Your final order has been Shipped Successfully. Soon you will receive your order at your door step");

                    } else if (shippingState.equals("not Shipped")) {
                        recyclerView.setVisibility(View.GONE);
                        NextProcessBtn.setVisibility(View.GONE);
                        txtTotalAmount.setText("Attention : You can purchase more products once you received your last order");
                        txtMsg1.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

                 // open details
    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
        intent.putExtra("PID", cartsList.get(position).getPid());
        intent.putExtra("pname", cartsList.get(position).getpName());
        intent.putExtra("description", cartsList.get(position).getDescription());
        intent.putExtra("price", cartsList.get(position).getPrice());
        intent.putExtra("image", cartsList.get(position).getImage());
        startActivity(intent);
    }

                // delete Item
    @Override
    public void onDeleteClicked(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
//                        cartsList.remove(position);
//                        adapter.notifyItemRemoved(position);
//                        adapter.notifyItemRangeChanged(position, cartsList.size());
                        ProductsRef.child(cartsList.get(position).getPid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {

                                            Toast.makeText(CartActivity.this, "Error, Check internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        ProductsRef2.child(cartsList.get(position).getPid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(CartActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        break;

                }

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}