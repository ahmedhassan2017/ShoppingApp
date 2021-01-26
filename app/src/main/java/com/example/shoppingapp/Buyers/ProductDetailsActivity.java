package com.example.shoppingapp.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCart;
    private ImageView productImage, imggg;
    ElegantNumberButton numberButton;
    private TextView productName, productPrice, productDescription;
    String productID, state = "normal";
    String imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_product_details);

        addToCart = findViewById(R.id.add_product_to_cart);
        productDescription = findViewById(R.id.product_description_details);
        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productPrice = findViewById(R.id.product_price_details);
        numberButton = findViewById(R.id.elegant_btn);

        productID = getIntent().getStringExtra("PID");
        productName.setText(getIntent().getStringExtra("pname"));
        productDescription.setText(getIntent().getStringExtra("description"));
        productPrice.setText(getIntent().getStringExtra("price"));
        imageUri = getIntent().getStringExtra("image");
        Picasso.get().load(imageUri).into(productImage);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(productImage);
        pAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        pAttacher.update();





        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("order placed") || state.equals("order shipped")) {
                    Toast.makeText(ProductDetailsActivity.this, " you can add products once your last order shipped or confirmed", Toast.LENGTH_LONG).show();

                } else {
                    AddToCartList();
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void AddToCartList() {

        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("PID", productID);
        cartMap.put("pName", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("description", productDescription.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("image", imageUri);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");
        cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products")
                .child(productID)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    cartListRef.child("Admin View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .child("Products")
                            .child(productID)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailsActivity.this, "Successfully Added To Cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

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

                        state = "order shipped";
                    } else if (shippingState.equals("not Shipped")) {
                        state = "order placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //    public void showImage() {
//        Dialog builder = new Dialog(this);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(this);
//        Picasso.get().load(imageUri).into(imageView);
//        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        builder.show();
//    }
}