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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegister;
    private EditText editTextEmail,editTextPassword,editTextname,editTextage,editTextphone,editTextaddress;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextname = findViewById(R.id.EditTextName);
        editTextage = findViewById(R.id.EditTextAge);
        editTextphone = findViewById(R.id.EditTextPhone);
        editTextaddress = findViewById(R.id.EditTextAddress);
        editTextEmail = findViewById(R.id.EditTextEmail);
        editTextPassword = findViewById(R.id.EditTextPassword);

        buttonRegister =findViewById(R.id.buttonRegister);

        textViewSignin = findViewById(R.id.signIn);

        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(getApplicationContext(), profile.class ));
        }

        progressDialog = new ProgressDialog(this);




        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextPassword.getText().toString().trim();
                if (code.isEmpty() || code.length() < 8) {
                    editTextPassword.setError("Password is Too short");
                    editTextPassword.requestFocus();
                    return;
                }

                registerUser();
            }
        });

    }

// Log In/Sign UP

    private void registerUser(){
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

        progressDialog.setMessage("Registering User....");
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            AddData();
                            startActivity(new Intent(getApplicationContext(), profile.class ));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed / Already Registerd.",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }
    // Log In/Sign UP

    //Add Data On Database

    private void AddData() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("User").child(mAuth.getUid());



        String name = editTextname.getText().toString().trim();
        String age = editTextage.getText().toString().trim();
        String phone = editTextphone.getText().toString().trim();
        String address = editTextaddress.getText().toString().trim();


        Getprofile_Data User= new Getprofile_Data(name, age, phone, address);
        databaseReference.setValue(User);

    }

    //Add Data On Database

    public void SignUp(View view) {
        if(view == buttonRegister){

        }
    }

    public void SignIn(View view) {
        if(view == textViewSignin){
            startActivity(new Intent(this,login_page.class));

        }
    }
}
