package com.example.worldnews.ui.SignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.worldnews.Model.User;
import com.example.worldnews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    EditText ed_FullName, ed_Age, ed_Email ,ed_Password;
    Button btn_registration;
    ProgressBar progressBar;
    ImageView back;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        ed_FullName =findViewById(R.id.RG_FullName);
        ed_Age =findViewById(R.id.RG_Age);
        ed_Email =findViewById(R.id.RG_Email);
        ed_Password =findViewById(R.id.RG_password);
        progressBar =findViewById(R.id.progressBar);
        back =findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
            }
        });

        btn_registration=findViewById(R.id.RG_registerBtn);
        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    public void registerUser(){
        String fullName = ed_FullName.getText().toString().trim();
        String age = ed_Age.getText().toString().trim();
        String password = ed_Password.getText().toString().trim();
        String email = ed_Email.getText().toString().trim();


        if (fullName.isEmpty()){
            ed_FullName.setError(" Full Name Is Empty! ");
            ed_FullName.requestFocus();
            return;
        }

        if (age.isEmpty()){
            ed_Age.setError(" Age Is Empty! ");
            ed_Age.requestFocus();
            return;
        }

        if (email.isEmpty()){
            ed_Email.setError(" email Is Empty! ");
            ed_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ed_Email.setError(" Please Provide valid email! ");
            ed_Email.requestFocus();
            return;
        }

        if (password.isEmpty()){
            ed_Password.setError(" Password Is Empty! ");
            ed_Password.requestFocus();
            return;
        }
        if (password.length() < 6){
            ed_Password.setError(" Password Min 6 characters! ");
            ed_Password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user=new User(fullName,age,email);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(Registration.this, Login.class));
                                Toast.makeText(Registration.this, "User has been registered successfully! ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(Registration.this, "Failed to register! Try again! ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else {
                    Toast.makeText(Registration.this, "Failed to register! ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

}