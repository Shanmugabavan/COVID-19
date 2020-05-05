package com.example.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;

public class Register extends AppCompatActivity {
    private boolean state=false;
    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth auth;
    private String username;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email_reg_in);
        password=findViewById(R.id.password_reg_in);
        register=findViewById(R.id.register_reg_bt);
        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                username=txt_email.split("@")[0];

                if (TextUtils.isEmpty(txt_email)||(TextUtils.isEmpty(txt_password))){
                    Toast.makeText(Register.this,"Invalid Input",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog = new ProgressDialog(Register.this);
                    progressDialog.setMessage("Registering...(Make sure Internet connection)");
                    progressDialog.show();
                    register_user(txt_email,txt_password);
                }


            }
        });
    }
    private void register_user(final String txt_email, final String txt_password) {
        auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(Register.this,"Wait until register",Toast.LENGTH_LONG).show();
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(username).child("Email").setValue(txt_email);
                    FirebaseDatabase.getInstance().getReference().child("users").child(username).child("country").setValue("Sri Lanka");
                    Toast.makeText(Register.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(Register.this,MainActivity.class));
                    finish();
                }else{
//                    Toast.makeText(Register.this,"UNSUCCESS",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
