/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */
package com.example.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* LAST TEN ACTIVITY- shows the user the score of his 10 last games
 * user gets here after pressing "My 10 last scores" in userScreen*/

public class LastTenActivity extends AppCompatActivity{
    TextView text;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_ten);

        username = SharedPref.getInstance(this).LoggedInUser();
        text = findViewById(R.id.lasttentable);
        listPlayer();
    }

    /* listPlayer calls the db and gets 10 last scores of user username
     * then shows to the user*/
    public void listPlayer() {
        Api api = ApiClient.getClient().create(Api.class);
        Call<Player> last_ten = api.last_games(username);

        last_ten.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                //connection problem
                if(!response.isSuccessful()){
                    Toast.makeText(LastTenActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LastTenActivity.this,MainActivity.class));
                }
                if(response.body().getIsSuccess() == 1){
                    String games = response.body().getGames();
                    text.setText(games);

                }else{
                    Toast.makeText(LastTenActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }}

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                Toast.makeText(LastTenActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LastTenActivity.this,MainActivity.class));

            }
        });
    }

    //user pressed return button to return to userScreen
    public void returnUserScreen(View view) { startActivity(new Intent(getApplicationContext(), UserMenuActivity.class)); }
}