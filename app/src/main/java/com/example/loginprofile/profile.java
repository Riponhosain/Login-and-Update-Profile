package com.example.loginprofile;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private TextView textViewUserEmail;
    private TextView textViewVerified;
    private Button Logout;

    TextView textViewname,textViewage,textViewphone,textViewaddress;

    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;
    FirebaseDatabase database;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUserEmail = findViewById(R.id.textviewUserEmail);
        Logout = findViewById(R.id.Logout);



// Display the Data
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        textViewname = findViewById(R.id.textviewUserName);
        textViewage = findViewById(R.id.textviewUserAge);
        textViewphone = findViewById(R.id.textviewUserPhone);
        textViewaddress = findViewById(R.id.textviewUserAddress);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (TextUtils.isEmpty(firebaseAuth.getUid())) {

                    startActivity(new Intent(profile.this, MainActivity.class));
                } else {


                    String name = dataSnapshot.child(uid).child("name").getValue().toString();
                    String age = dataSnapshot.child(uid).child("age").getValue().toString();
                    String phone = dataSnapshot.child(uid).child("phone").getValue().toString();
                    String address = dataSnapshot.child(uid).child("address").getValue().toString();


                    textViewname.setText(name);
                    textViewage.setText(age);
                    textViewphone.setText(phone);
                    textViewaddress.setText(address);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

// Display the Data

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() ==null){
            finish();
            startActivity(new Intent(this, login_page.class ));
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();


// For varification

        textViewVerified = findViewById(R.id.textViewVerefired);


        if(user.isEmailVerified()){

            textViewVerified.setText("Email Verified");

        }else{
            textViewVerified.setText("Email Not Verified (Click To Verify)");
            textViewVerified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(profile.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            });
        }

        textViewUserEmail.setText(user.getEmail()); // display user email

    }



    private void showUpdateDeleteDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_delete, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.EditTextName);
        final EditText editTextage = (EditText) dialogView.findViewById(R.id.EditTextAge);
        final EditText editTextphone = (EditText) dialogView.findViewById(R.id.EditTextPhone);
        final EditText editTextaddress = (EditText) dialogView.findViewById(R.id.EditTextAddress);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.delete);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.update);


        dialogBuilder.setTitle("Updating...");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String name = editTextName.getText().toString();
                String age = editTextage.getText().toString();
                String phone = editTextphone.getText().toString();
                String address = editTextaddress.getText().toString();

                if (!TextUtils.isEmpty(name)) {
                    editTextName.setError("Name Requird");
                    editTextage.setError("Age Requird");
                    editTextphone.setError("Phone Requird");
                    editTextaddress.setError("Address Requird");
                }

                updateProfile(name);
                updateProfile2(age);
                updateProfile3(phone);
                updateProfile4(address);

                b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteInformation(firebaseAuth.getUid());
                b.dismiss();
            }
        });
    }


    private void deleteInformation(String uid) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dR = firebaseDatabase.getReference("User").child(firebaseAuth.getUid());

        dR.removeValue();
        firebaseAuth.signOut();
        finish();
        startActivity( new Intent(profile.this, MainActivity.class));

        Toast.makeText(getApplicationContext(), "Profile Deleted", Toast.LENGTH_LONG).show();

    }

    private boolean updateProfile(String name) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dR = firebaseDatabase.getReference("User").child(firebaseAuth.getUid()).child("name");

        if(!name.isEmpty()){
            dR.setValue(name);
        }


        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean updateProfile2(String age) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dR = firebaseDatabase.getReference("User").child(firebaseAuth.getUid()).child("age");

        if(!age.isEmpty()){
            dR.setValue(age);
        }

        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean updateProfile3(String phone) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dR = firebaseDatabase.getReference("User").child(firebaseAuth.getUid()).child("phone");


        if(!phone.isEmpty()){
            dR.setValue(phone);
        }

        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean updateProfile4(String address) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dR = firebaseDatabase.getReference("User").child(firebaseAuth.getUid()).child("address");

        if(!address.isEmpty()){
            dR.setValue(address);
        }


        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
        return true;
    }




    public void LogOut(View view) {
        if(view == Logout){
            firebaseAuth.signOut();
            finish();
            startActivity( new Intent(this, login_page.class));
        }
    }

    public void Update(View view) {

        showUpdateDeleteDialog();

    }

    public void Search(View view) {
        startActivity( new Intent(this, Search_View.class));
    }
}


