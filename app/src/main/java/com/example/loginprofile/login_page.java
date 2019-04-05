package com.example.loginprofile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_page extends AppCompatActivity {

    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(getApplicationContext(), profile.class ));
        }

        buttonSignin = findViewById(R.id.buttonSignIn);

        editTextEmail = findViewById(R.id.EditTextEmail);
        editTextPassword = findViewById(R.id.EditTextPassword);

        textViewSignup = findViewById(R.id.signUp);

        progressDialog = new ProgressDialog(this);

    }

    private void userlogin(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Signing in User....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), profile.class ));
                        } else {

                            Toast.makeText(login_page.this, "Login failed(Chack Your Email/Password)",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    public void Login(View view) {
        if(view == buttonSignin){
            userlogin();
        }
    }

    public void CreatAccount(View view) {
        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
