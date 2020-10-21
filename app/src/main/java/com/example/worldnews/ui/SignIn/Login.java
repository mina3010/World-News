package com.example.worldnews.ui.SignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldnews.ui.ForgetPassword;
import com.example.worldnews.ui.MainActivity;
import com.example.worldnews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView forgetPasswordTxt,registeredTxt;
    EditText ed_email ;
    EditText ed_password;
    Button login;
    ProgressBar progressBar;
    CheckBox rememberMe;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Login.this);

        if(pref.getInt("reg",0)==1){
            Intent intent =new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }
        //banner =findViewById(R.id.re)
        forgetPasswordTxt =findViewById(R.id.forgetPassword);
        registeredTxt =findViewById(R.id.register_btn);
        rememberMe =findViewById(R.id.rememberMe);
        forgetPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgetPassword.class));
            }
        });
        ed_email =findViewById(R.id.userName);
        ed_password = findViewById(R.id.password);
        progressBar =findViewById(R.id.progressBar);

        registeredTxt .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });
        login=findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String password = ed_password.getText().toString().trim();
        String email = ed_email.getText().toString().trim();


        if (email.isEmpty()){
            ed_email.setError(" email Is Empty! ");
            ed_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ed_email.setError(" Please Provide valid email! ");
            ed_email.requestFocus();
            return;
        }

        if (password.isEmpty()){
            ed_password.setError(" Password Is Empty! ");
            ed_password.requestFocus();
            return;
        }
        if (password.length() < 6){
            ed_password.setError(" Password Min 6 characters! ");
            ed_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if(rememberMe.isChecked()){
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putInt("reg",1).commit();
//                    editor.putString("user",userName.getText().toString()).commit();
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }else {
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }

                }else {
                    Toast.makeText(Login.this, "Failed Login, Please check credentials!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}