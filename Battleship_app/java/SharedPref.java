/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * SharedPref class: stores the username of the user after he logs in / registers in the system
 * for automatic logging in the next time the user launches the app & for passing the username
 * from login and registers activities to other activities during app usage
 */

public class SharedPref {

    //Storage File
    public static final String SHARED_PREF_NAME = "battleship";

    //Username
    public static final String USER_NAME = "username";

    public static SharedPref mInstance;

    public static Context mCtx;


    public SharedPref(Context context) {
        mCtx = context;
    }


    public static synchronized SharedPref getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPref(context);
        }
        return mInstance;
    }


    //method to store user data
    public void storeUserName(String names) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, names);
        editor.commit();
    }

    //check if user is logged in
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, null) != null;
    }


    //find logged in user
    public String LoggedInUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, null);

    }


    //Logout user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }

}

