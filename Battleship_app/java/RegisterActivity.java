/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */
package com.example.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* REGISTER ACTIVITY- user gets here after pressing "register" in main activity
* after entering username and password, we validate them and then save in the db and move to userScreen*/

public class RegisterActivity extends AppCompatActivity {

    final int USERNAME_LENGTH_MIN = 4;
    final int PASSWORD_LENGTH_MIN = 6;

    //retrofit client to the api responses
    Api api = ApiClient.getClient().create(Api.class);

    EditText username,password;
    Button returnBtn;
    ImageButton btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username_signup);
        password = findViewById(R.id.password_signup);
        btnRegister = findViewById(R.id.signupBtn);
        returnBtn = findViewById(R.id.return_signup);

        //listener for "register" button- when pressed, calls validateUserData()
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });

        //listener for "return" button- when pressed, return to main activity.
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

    }

    /* Validates the username and password the user chose:
    * checks if username and password lengths are legal
    * checks if the password contains at least one special character, one number, one big and
    * one small letter.
    * if not, show message to the user of the problem. when data is validated, call to
    * registerUser */
    private void validateUserData() {

        //find values
        final String usernameStr = username.getText().toString();
        final String passwordStr = password.getText().toString();

        String msg ="";
        int flagBig=0,flagSmall=0,flagNum=0,flagSpecial=0;
        if(usernameStr.length() < USERNAME_LENGTH_MIN){
            username.setError("Username requires minimum length of "+USERNAME_LENGTH_MIN);
            username.requestFocus();
            btnRegister.setEnabled(true);
            return;
        }
        if(passwordStr.length() < PASSWORD_LENGTH_MIN) {
            msg += "Password requires minimum length of " + PASSWORD_LENGTH_MIN + "\n";
        }
        else{
            for (int i=0; i<passwordStr.length(); i++){
                if(passwordStr.charAt(i) >= 'A' && passwordStr.charAt(i) <= 'Z')
                    flagBig=1;
                if(passwordStr.charAt(i) >= 'a' && passwordStr.charAt(i) <= 'z')
                    flagSmall=1;
                if(passwordStr.charAt(i) >= '0' && passwordStr.charAt(i) <= '9')
                    flagNum=1;
                if((passwordStr.charAt(i) >= '!' && passwordStr.charAt(i) <= '/') ||
                        ((passwordStr.charAt(i) >= ':' && passwordStr.charAt(i) <= '@')))
                    flagSpecial=1;
            }
            if(flagBig==0)
                msg+= "Password requires minimum of one big letter\n";
            if(flagSmall==0)
                msg+= "Password requires minimum of one small letter\n";
            if(flagNum==0)
                msg+= "Password requires minimum of one digit\n";
            if(flagSpecial==0)
                msg+= "Password requires minimum of one special character: @,%,$, etc.\n";
        }

        // detected a problem- show to user
        if (msg!=""){
            password.setError(msg);
            password.requestFocus();
            btnRegister.setEnabled(true);
            return;
        }

        registerUser(usernameStr,passwordStr);

    }

    /* register the user to the database*/
    private void registerUser(String user_name, String user_pass) {

        //making api call with retrofit
        Call<Player> login = api.register(user_name,user_pass);

        login.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                //couldn't connect to db
                if(!response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
                if(response.body().getIsSuccess() == 1){
                    //storing the user in shared preferences
                    SharedPref.getInstance(RegisterActivity.this).storeUserName(user_name);

                    //move to userMenuActivity
                    startActivity(new Intent(RegisterActivity.this,UserMenuActivity.class));
                }else{
                    //the user didn't register- this username already exists in the db
                    Toast.makeText(RegisterActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            /*if there's an error: can't connect to network, host error, can't access db etc. show to user
             * as toast and move him to MainActivity*/
            public void onFailure(Call<Player> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));

            }
        });
    }
}
