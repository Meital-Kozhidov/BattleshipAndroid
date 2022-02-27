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

/* TOP TEN ACTIVITY- shows the user the 10 players with most wins
 * user gets here after pressing "top 10 players" in userScreen*/

public class TopTenActivity extends AppCompatActivity {
    Api api = ApiClient.getClient().create(Api.class);

    TextView text;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        username = SharedPref.getInstance(this).LoggedInUser();
        text = findViewById(R.id.toptentable);
        listPlayer();

    }

    /* listPlayer calls the db and gets 10 top players
     * then shows the users*/
    public void listPlayer() {
        Call<Player> top_players = api.top_players();

        top_players.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                //connection problem
                if(!response.isSuccessful()){
                    Toast.makeText(TopTenActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(TopTenActivity.this,MainActivity.class));
                }
                if (response.body().getIsSuccess() == 1)
                    text.setText(response.body().getResults());
                else
                    Toast.makeText(TopTenActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure (Call < Player > call, Throwable t){
                Toast.makeText(TopTenActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TopTenActivity.this,MainActivity.class));
            }
        });
    }


    /* User pressed return to return to userScreen*/
    public void returnUser(View view) { startActivity(new Intent(getApplicationContext(), UserMenuActivity.class)); }
}