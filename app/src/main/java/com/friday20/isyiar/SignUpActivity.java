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
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText editTextNama, editTextUsername, editTextEmail, editTextPassword;
    private TextInputLayout inputLayoutNama, inputLayoutUsername, inputLayoutEmail, inputLayoutPassword;
    private MaterialRadioButton rb1, rb2;
    private FirebaseDatabase database;
    private DatabaseReference df;
    private String tipe="";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    userSinUp();
                }
            }
        });

        findViewById(R.id.txtlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void userSinUp(){
        if (rb1.isChecked())
            tipe="1";
        else
            tipe="2";

        final SignUp reg = new SignUp(
                editTextNama.getText().toString(),
                editTextUsername.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString(),
                tipe
        );

        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(editTextUsername.getText().toString()).exists()) {
                    mDialog.dismiss();
                    inputLayoutUsername.setError("Username already exist!");
                }else {
                    mDialog.dismiss();
                    df.child(editTextUsername.getText().toString()).setValue(reg);
                    User user = new User(
                            editTextUsername.getText().toString(),
                            editTextNama.getText().toString(),
                            editTextEmail.getText().toString(),
                            tipe
                    );
                    DataUser.getInstance(SignUpActivity.this).userLogin(user);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(SignUpActivity.this, "Sign Up Successfully !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        editTextNama = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPass);
        inputLayoutNama = findViewById(R.id.txName);
        inputLayoutUsername = findViewById(R.id.txUsername);
        inputLayoutEmail = findViewById(R.id.txEmail);
        inputLayoutPassword = findViewById(R.id.txPass);
        coordinatorLayout = findViewById(R.id.container);
        rb1=findViewById(R.id.rb1);
        rb2=findViewById(R.id.rb2);
        database = FirebaseDatabase.getInstance();
        df = database.getReference("users");
    }

    public boolean validate() {
        boolean valid = false;
        String nama = editTextNama.getText().toString();
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (nama.isEmpty()) {
            valid = false;
            inputLayoutNama.setError("Please enter your name!");
        } else {
            valid = true;
            inputLayoutNama.setError(null);
        }

        if (username.isEmpty()) {
            valid = false;
            inputLayoutUsername.setError("Please enter username!");
        } else {
            if (username.length() > 3) {
                valid = true;
                inputLayoutUsername.setError(null);
            } else {
                valid = false;
                inputLayoutUsername.setError("Username is to short!");
            }
        }

        if (email.isEmpty()) {
            valid = false;
            inputLayoutEmail.setError("Please enter your email!");
        } else {
            valid = true;
            inputLayoutEmail.setError(null);
        }


        if (password.isEmpty()) {
            valid = false;
            inputLayoutPassword.setError("Please enter valid password!");
        } else {
            if (password.length() > 5) {
                valid = true;
                inputLayoutPassword.setError(null);
            } else {
                valid = false;
                inputLayoutPassword.setError("Password is to short!");
            }
        }

        return valid;
    }
}