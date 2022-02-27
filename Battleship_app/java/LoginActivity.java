/* By : Meital Kozhidov
 * April 21
  * To: Tamar Benaya
*/
package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* LOGIN ACTIVITY- user gets here after pressing "log in" in main activity
 * after entering username and password, we check them in the db and move to userScreen*/

public class LoginActivity extends AppCompatActivity {

    final int USERNAME_LENGTH_MIN = 4;
    final int PASSWORD_LENGTH_MIN = 6;

    //retrofit client to the api responses
    Api api = ApiClient.getClient().create(Api.class);

    EditText username_input,password_input;
    Button returnBtn;
    ImageButton btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_input = findViewById(R.id.usernamelogin);
        password_input = findViewById(R.id.passwordlogin);
        btnLog = findViewById(R.id.loginBtn);
        returnBtn = findViewById(R.id.return_login);

        //listener for "log in" button- when pressed, calls validateUserData()
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });

        //listener for "return" button- when pressed, return to main activity.
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

    }
    private void validateUserData() {

        //first getting the values
        final String username = username_input.getText().toString();
        final String password = password_input.getText().toString();

        //checking if username/password length is less than minimum username length
        //if so, no need to check in db

        if (username.length()<USERNAME_LENGTH_MIN) {
            username_input.setError("Please enter your username- minimum length is "+USERNAME_LENGTH_MIN);
            username_input.requestFocus();
            btnLog.setEnabled(true);
            return;
        }
        if (password.length()<PASSWORD_LENGTH_MIN) {
            password_input.setError("Please enter your password- minimum length is "+PASSWORD_LENGTH_MIN);
            password_input.requestFocus();
            btnLog.setEnabled(true);
            return;
        }

        //Login User if everything is fine
        loginUser(username,password);
    }

    /* calling the db and checking the user details
    * if checks out - user is logged in. move to userScreen*/
    private void loginUser(String username, String password) {

        //making api call
        Call<Player> login = api.login(username,password);

        login.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                //couldn't connect to db
                if(!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }

                //logged in successfully
                if(response.body().getIsSuccess() == 1){
                    //storing the user in shared preferences
                    SharedPref.getInstance(LoginActivity.this).storeUserName(username);

                    startActivity(new Intent(LoginActivity.this,UserMenuActivity.class));
                }

                //didn't log in - wrong username or password. show message to user
                else{
                    Toast.makeText(LoginActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            /*if there's an error: can't connect to network, host error, can't access db etc. show to user
            * as toast and move him to MainActivity*/
            public void onFailure(Call<Player> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });


    }

}
