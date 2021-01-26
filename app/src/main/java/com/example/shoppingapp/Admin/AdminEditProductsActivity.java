package com.example.shoppingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditProductsActivity extends AppCompatActivity {

    private Button ApplyChangesBtn, DeleteProductBtn;
    private ImageView EditProductImage;
    private EditText EditProductName, EditProductDescription, EditProductPrice;
    String productID;

    DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_products);

        productID = getIntent().getStringExtra("PID");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        ApplyChangesBtn = findViewById(R.id.apply_changes_btn);
        DeleteProductBtn = findViewById(R.id.admin_delete_btn);
        EditProductImage = findViewById(R.id.edit_product_image);
        EditProductDescription = findViewById(R.id.edit_product_description);
        EditProductPrice = findViewById(R.id.edit_product_price);
        EditProductName = findViewById(R.id.edit_product_name);

        DisplayCurrentProductInfo();
        ApplyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChanges();
            }
        });

        DeleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteProduct();
            }
        });
    }

    private void DeleteProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminEditProductsActivity.this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //
    private void ApplyChanges() {
        String pName = EditProductName.getText().toString();
        String pDes = EditProductDescription.getText().toString();
        String pPrice = EditProductPrice.getText().toString();
//        String pImage = EditProductImage.getResources().toString();

        if (pName.equals("")) {
            Toast.makeText(this, "Product Name needed", Toast.LENGTH_SHORT).show();
        } else if (pDes.equals("")) {
            Toast.makeText(this, "Product Description needed", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {
            Toast.makeText(this, "Product Price needed", Toast.LENGTH_SHORT).show();
        } else {

            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("description", pDes);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);
            //            productMap.put("image", downloadImageUrl);
            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditProductsActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }


    private void DisplayCurrentProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ProductName = snapshot.child("pname").getValue().toString();
                    String ProductPrice = snapshot.child("price").getValue().toString();
                    String ProductDes = snapshot.child("description").getValue().toString();
                    String ProductImage = snapshot.child("image").getValue().toString();

                    EditProductName.setText(ProductName);
                    EditProductPrice.setText(ProductPrice);
                    EditProductDescription.setText(ProductDes);
                    Picasso.get().load(ProductImage).into(EditProductImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}