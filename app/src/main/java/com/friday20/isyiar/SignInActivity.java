package com.friday20.isyiar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.friday20.isyiar.model.SignUp;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private FirebaseDatabase database;
    private DatabaseReference df;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initViews();
        if (DataUser.getInstance(this).isLoggedIn()) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    userSignIn();
                }
            }
        });

        findViewById(R.id.txtreg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void userSignIn(){
        final String Username = editTextEmail.getText().toString();
        final String Password = editTextPassword.getText().toString();
        final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Username).exists()) {
                    mDialog.dismiss();
                    SignUp registrasi = dataSnapshot.child(Username).getValue(SignUp.class);
                    if (registrasi.getPassword().equals(Password)) {
                        {
                            User user = new User(
                                    registrasi.getUsername(),
                                    registrasi.getName(),
                                    registrasi.getEmail(),
                                    registrasi.getUsertype()
                            );
                            DataUser.getInstance(SignInActivity.this).userLogin(user);
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {
                        Snackbar.make(coordinatorLayout, "Wrong Password !", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                }else{
                    mDialog.dismiss();
                    Snackbar.make(coordinatorLayout, "Username not exist in Database !", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPass);
        textInputLayoutEmail =  findViewById(R.id.txEmail);
        textInputLayoutPassword = findViewById(R.id.txPass);
        coordinatorLayout=findViewById(R.id.container);
        database = FirebaseDatabase.getInstance();
        df = database.getReference("users");
    }

    public boolean validate() {
        boolean valid = false;
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();
        if (Email.isEmpty()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        if (Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid password!");
        } else {
            valid = true;
            textInputLayoutPassword.setError(null);
        }

        return valid;
    }
}