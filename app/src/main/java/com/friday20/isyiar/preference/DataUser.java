package com.friday20.isyiar.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.friday20.isyiar.MainActivity;
import com.friday20.isyiar.SignInActivity;
import com.friday20.isyiar.SignUpActivity;
import com.friday20.isyiar.model.User;


public class DataUser {
    private static final String SHARED_PREF_NAME = "data_user";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TYPE = "type";

    private static DataUser mInstance;
    private static Context mCtx;

    private DataUser(Context context) {
        mCtx = context;
    }

    public static synchronized DataUser getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataUser(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_NAMA, user.getNama());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_TYPE, user.getUserType());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_NAMA, null ),
                sharedPreferences.getString(KEY_EMAIL, null ),
                sharedPreferences.getString(KEY_TYPE, null )
        );
    }

    public void signOut() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(mCtx, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mCtx.startActivity(intent);
        //mCtx.startActivity(new Intent(mCtx, SignInActivity.class));
    }
}
