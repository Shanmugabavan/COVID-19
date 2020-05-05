package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_Login extends AppCompatActivity {
    private Button signin;
    private Button register;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__login);
        user= FirebaseAuth.getInstance().getCurrentUser();
        progressDialog=new ProgressDialog(Register_Login.this);
        progressDialog.setMessage("Make sure Internet connection");
        progressDialog.show();

        a: if(!isOnline()){

        }
        progressDialog.dismiss();

        if(user!=null){
            startActivity(new Intent(Register_Login.this,MainActivity.class));
            finish();
        }

        signin=findViewById(R.id.signin_1_bt);
        register=findViewById(R.id.register_1_bt1);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Login.this,Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Login.this,Register.class));
            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
//    public void checkConnection(){
//        if(isOnline()){
//            Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
//        }
//    }
}
