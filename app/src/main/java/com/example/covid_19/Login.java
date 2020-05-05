package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email_lg_in);
        password=findViewById(R.id.password_lg_in);
        login=findViewById(R.id.login_lg_bt);
        auth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt=email.getText().toString();
                String password_txt=password.getText().toString();
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Logging in...(Make sure Internet connection)");
                progressDialog.show();
                login_user(email_txt,password_txt);
            }
        });


    }
    private void login_user(String email_txt, String password_txt) {
        auth.signInWithEmailAndPassword(email_txt,password_txt).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Login.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,MainActivity.class));
                finish();
            }
        });
    }
}
