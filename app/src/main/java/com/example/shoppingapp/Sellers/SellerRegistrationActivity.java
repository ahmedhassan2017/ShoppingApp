package com.example.shoppingapp.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingapp.Buyers.MainActivity;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private Button seller_haveAccount_btn, registerBtn;
    private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seller_registration);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        seller_haveAccount_btn = findViewById(R.id.seller_haveAccount_btn);
        registerBtn = findViewById(R.id.seller_Registration_btn);
        nameInput = findViewById(R.id.sellerPersonName);
        phoneInput = findViewById(R.id.sellerPhone);
        passwordInput = findViewById(R.id.sellerPassword);
        emailInput = findViewById(R.id.sellerEmail);
        addressInput = findViewById(R.id.sellerAddress);

        seller_haveAccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {
        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String password = passwordInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String address = addressInput.getText().toString();

        if (name.equals("") || phone.equals("") || password.equals("") || email.equals("") || address.equals(""))
            Toast.makeText(this, "Please, fill all fields", Toast.LENGTH_SHORT).show();
        else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait, while checking your credentials ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final DatabaseReference rootRef;
                                rootRef = FirebaseDatabase.getInstance().getReference();
                                // sID = Seller ID
                                String sID = mAuth.getCurrentUser().getUid();
                                HashMap<String, Object> sellerMap = new HashMap<>();
                                sellerMap.put("SID", sID);
                                sellerMap.put("name", name);
                                sellerMap.put("phone", phone);
                                sellerMap.put("password", password);
                                sellerMap.put("email", email);
                                sellerMap.put("address", address);
                                rootRef.child("Sellers")
                                        .child(sID)
                                        .updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                loadingBar.dismiss();
                                                Toast.makeText(SellerRegistrationActivity.this, "Account Registered Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SellerRegistrationActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                            }
                        }
                    });
        }
    }
}