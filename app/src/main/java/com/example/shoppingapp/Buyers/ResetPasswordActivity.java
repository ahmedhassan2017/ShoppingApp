package com.example.shoppingapp.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Prevalent.Prevalent;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, questionsTitle;
    private EditText phoneQuestion, heroQuestion, nickNameQuestion;
    Button verifyBtn;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        pageTitle = findViewById(R.id.page_title);
        questionsTitle = findViewById(R.id.question_title);
        phoneQuestion = findViewById(R.id.question_Phone);
        heroQuestion = findViewById(R.id.question_hero);
        nickNameQuestion = findViewById(R.id.question_nickname);
        verifyBtn = findViewById(R.id.verify);
        check = getIntent().getStringExtra("check");

        if (check.equals("settings")) {
            ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneQuestion.setVisibility(View.GONE);
// law howa gay mn el settings Act hay set el security Answers an hay Update
        if (check.equals("settings")) {
// it will display Answers if it exist
            DisplayCurrentSecurityAnswers();
            pageTitle.setText("Set Questions");
            questionsTitle.setText("Please set answers for the following security questions");
            verifyBtn.setText("Set");
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAnswers();
                }
            });

        }// law gay mn el login Act (Forget Password)
        else if (check.equals("login")) {
            phoneQuestion.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });


        }
    }


    private void DisplayCurrentSecurityAnswers() {
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nickNameValue = snapshot.child("nickName").getValue().toString();
                    String heroValue = snapshot.child("hero").getValue().toString();

                    nickNameQuestion.setText(nickNameValue);
                    heroQuestion.setText(heroValue);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateAnswers() {
        String nickNameAnswer = nickNameQuestion.getText().toString().toLowerCase();
        String heroAnswer = heroQuestion.getText().toString().toLowerCase();
        if (nickNameAnswer.equals("") || heroAnswer.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {

            HashMap<String, Object> userDataMap = new HashMap<>();
            userDataMap.put("nickName", nickNameAnswer);
            userDataMap.put("hero", heroAnswer);
            ref.child("Security Questions").updateChildren(userDataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Security Questions Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void verifyUser() {
        final String phone = phoneQuestion.getText().toString();
        final String nickNameAnswer = nickNameQuestion.getText().toString().toLowerCase();
        final String heroAnswer = heroQuestion.getText().toString().toLowerCase();
        if (phone.equals("") || nickNameAnswer.equals("") || heroAnswer.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {

            ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
//                    String userPhone = snapshot.child("phone").getValue().toString();

                        if (snapshot.hasChild("Security Questions")) {

                            String nickNameValue = snapshot.child("Security Questions").child("nickName").getValue().toString();
                            String heroValue = snapshot.child("Security Questions").child("hero").getValue().toString();
                            if (!nickNameValue.equals(nickNameAnswer)) {
                                Toast.makeText(ResetPasswordActivity.this, "your nickname answer is Wrong", Toast.LENGTH_SHORT).show();
                            } else if (!heroValue.equals(heroAnswer)) {
                                Toast.makeText(ResetPasswordActivity.this, "your hero answer is Wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write new Password ..");
                                builder.setView(newPassword);
                                builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newPassword.getText().toString().equals("")) {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }

                        } else
                            Toast.makeText(ResetPasswordActivity.this, "You haven't set Security Questions", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(ResetPasswordActivity.this, "Phone Number Not Exist", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

}