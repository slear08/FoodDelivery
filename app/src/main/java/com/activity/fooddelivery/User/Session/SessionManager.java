package com.activity.fooddelivery.User.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.activity.fooddelivery.Activities.LoginActivity;
import com.activity.fooddelivery.Activities.MainActivity;
import com.activity.fooddelivery.Activities.WelcomeActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    private static int Private_mode = 0;

    public static final String PREF_NAME = "AndroidHivePref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_NAME = "name";




    public SessionManager (Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Private_mode);
        editor = pref.edit();
    }

    public void createLoginSession(String email,String name){
        editor.putBoolean(IS_LOGIN,true);

        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_NAME, name);

        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
    }

    public HashMap<String , String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL,null));


        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }
}