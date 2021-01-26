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

public class SellerLoginActivity extends AppCompatActivity {
    private EditText passwordInput, emailInput;
    private Button loginBtn;
    FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seller_login);
        loginBtn = findViewById(R.id.seller_login_btn);
        passwordInput = findViewById(R.id.login_SellerPassword);
        emailInput = findViewById(R.id.login_SellerEmail);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginSeller();

            }
        });


    }

    private void loginSeller() {

        final String password = passwordInput.getText().toString();
        final String email = emailInput.getText().toString();
        if (  password.equals("") || email.equals("") )
            Toast.makeText(this, "Please, fill all fields", Toast.LENGTH_SHORT).show();
        else {
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait, while checking your credentials ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
//                        loadingBar.dismiss();
                        Toast.makeText(SellerLoginActivity.this, " Logged in Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }
    }
}