package com.friday20.isyiar.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.friday20.isyiar.MainActivity;
import com.friday20.isyiar.R;
import com.friday20.isyiar.SignUpActivity;
import com.friday20.isyiar.model.SignUp;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;
import com.friday20.isyiar.room.AppDatabase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Fragment5 extends Fragment {
    private static final String TAG = Fragment5.class.getSimpleName();
    private TextInputEditText editTextNama,  editTextEmail, editTextPassword;
    private TextInputLayout inputLayoutNama, inputLayoutEmail, inputLayoutPassword;
    private MaterialButton button;
    private TextView nama, username;
    private User user;
    private FirebaseDatabase database;
    private DatabaseReference df;
    private AppDatabase db;

    public Fragment5() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_5, container, false);
        //db = new AppDatabase(getActivity());
        editTextNama = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPass);
        inputLayoutNama = view.findViewById(R.id.txName);
        inputLayoutEmail = view.findViewById(R.id.txEmail);
        inputLayoutPassword = view.findViewById(R.id.txPass);
        nama=view.findViewById(R.id.nama);
        username=view.findViewById(R.id.username);
        button=view.findViewById(R.id.button);
        user=DataUser.getInstance(getActivity()).getUser();
        nama.setText(user.getNama());
        username.setText(user.getUsername());
        editTextNama.setText(user.getNama());
        editTextEmail.setText(user.getEmail());
        editTextPassword.setText("Your password");
        database = FirebaseDatabase.getInstance();
        df = database.getReference("users");
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //db.deleteSyiar();
                //db.deleteFav();
                DataUser.getInstance(getActivity()).signOut();
            }
        });

        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNama.setEnabled(true);
                editTextEmail.setEnabled(true);
                editTextPassword.setEnabled(true);
                editTextNama.setFocusable(true);
                button.setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.img_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    SignUp sign = new SignUp(
                      editTextNama.getText().toString(),
                      user.getUsername(),
                      editTextEmail.getText().toString(),
                      editTextPassword.getText().toString(),
                      user.getUserType()
                    );
                    updateData(sign);
                }
            }
        });

        return view;
    }

    public boolean validate() {
        boolean valid = false;
        String nama = editTextNama.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (nama.isEmpty()) {
            valid = false;
            inputLayoutNama.setError("Please enter your name!");
        } else {
            valid = true;
            inputLayoutNama.setError(null);
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

    private void updateData(final SignUp signUp){
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);
        df.child(signUp.getUsername()).setValue(signUp).addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void mVoid){
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Perubahan disimpan !", Toast.LENGTH_LONG).show();
                User user = new User(
                        signUp.getUsername(),
                        signUp.getName(),
                        signUp.getEmail(),
                        signUp.getUsertype()
                );
                DataUser.getInstance(getActivity()).userLogin(user);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }



}
