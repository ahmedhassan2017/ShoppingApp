package com.example.shoppingapp.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.shoppingapp.Prevalent.Prevalent;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {


    private EditText firstName, lastName, phoneNum, address, cityName;
    private Button confirmBtn;
    private Switch sendLocation;
    private String TotalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        phoneNum = findViewById(R.id.phonenum);
        address = findViewById(R.id.address);
        cityName = findViewById(R.id.city_name);
        sendLocation = findViewById(R.id.send_location);
        confirmBtn = findViewById(R.id.confirm_final_order);
        TotalAmount = getIntent().getStringExtra("total");


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check();

            }
        });


    }

    private void check() {
        if (firstName.getText().toString().equals("")) {
            Toast.makeText(this, "Please, enter Your First Name", Toast.LENGTH_SHORT).show();
        } else if (lastName.getText().toString().equals("")) {
            Toast.makeText(this, "Please, enter Your Last Name", Toast.LENGTH_SHORT).show();
        } else if (phoneNum.getText().toString().equals("")) {
            Toast.makeText(this, "Please, enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (address.getText().toString().equals("")) {
            Toast.makeText(this, "Please, enter Your Address", Toast.LENGTH_SHORT).show();
        } else if (cityName.getText().toString().equals("")) {
            Toast.makeText(this, "Please, enter Your City Name", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {

        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("UID", Prevalent.currentOnlineUser.getPhone());
        orderMap.put("totalAmount", TotalAmount);
        orderMap.put("firstName", firstName.getText().toString());
        orderMap.put("lastName", lastName.getText().toString());
        orderMap.put("phone", phoneNum.getText().toString());
        orderMap.put("address", address.getText().toString());
        orderMap.put("city", cityName.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "not Shipped");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ConfirmFinalOrderActivity.this, "Your order placed Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

}