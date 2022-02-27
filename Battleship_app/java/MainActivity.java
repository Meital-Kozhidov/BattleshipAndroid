/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */
package com.example.battleship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/* MAIN ACTIVITY- the first launching activity
* checks if the user is logged in- if so, will automatically transfer him to userScreen
* else - we stay here. The user can either sign up or log in.
*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if user is logged in
        if (SharedPref.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, UserMenuActivity.class));
        }
    }

    //user pressed on sign up button
    public void signUp(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    //user pressed on log in button
    public void logIn(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

}

