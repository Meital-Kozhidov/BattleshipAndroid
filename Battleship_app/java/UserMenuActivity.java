/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */
package com.example.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* USER MENU ACTIVITY - this is the activity the users get to after registering/logging in
* or immediately if user was already logged in before launching the app (from previous usage)
* this activity displays a menu of four options - start a new game, view your ten last scores,
* view top ten players and log out. */
public class UserMenuActivity extends AppCompatActivity implements View.OnClickListener{

    //retrofit client to the api responses
    Api api = ApiClient.getClient().create(Api.class);

    TextView text;
    String username;
    TextView newgame,lastten,topten,logout;

    //Timer for making retrofit calls repeatedly
    Timer timer;

    //Flag for canceling the search for opponent when user clicks on something else
    boolean waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        username = SharedPref.getInstance(this).LoggedInUser();

        timer = new Timer();
        waiting = false;

        //menu buttons
        newgame=(TextView)findViewById(R.id.newgame);
        lastten=(TextView)findViewById(R.id.lastten);
        topten=(TextView)findViewById(R.id.topten);
        logout=(TextView)findViewById(R.id.logout);

        //setting listeners to the menu
        newgame.setOnClickListener(this);
        lastten.setOnClickListener(this);
        topten.setOnClickListener(this);
        logout.setOnClickListener(this);

        text = findViewById(R.id.usermsg);

        setUserMsg();
    }

    /* setUserMsg uses retrofit call to get the number of user's wins
     * and display them to the user */
    public void setUserMsg(){
        Call<Player> getWins = api.getWins(username);
        getWins.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                //connection problem
                if(!response.isSuccessful()){
                    Toast.makeText(UserMenuActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(UserMenuActivity.this,MainActivity.class));
                }
                int wins = response.body().getWins();
                String message= "Hello "+username+"\n"+"Wins: "+wins;
                text.setText(message);

            }
            @Override
            //error handling: go back to MainActivity
            public void onFailure(Call<Player> call, Throwable t) {
                Toast.makeText(UserMenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserMenuActivity.this,MainActivity.class));
            }
        });
    }

    @Override
    /* The user pressed a button- new game, top 10, last 10 or log out
     * if the user is waiting for an opponent (meaning, the user clicked new game and have
     * yet found an opponent and started a new match) we erase the user from active_players.
     * if the user pressed on new game - we call find_opponent()
     * else, we move the user to the wanted activity*/

    public void onClick(View v) {
        String choice = v.getTag().toString();

        //if the user is waiting for an opponent but pressed on something other than new game-
        //we stop the searching.
        if (waiting && !choice.equals("newgame")){
            timer.cancel();
            Call<Player> delete_active = api.delete_active(username);
            delete_active.enqueue(new Callback<Player>() {
                @Override
                public void onResponse(Call<Player> call, Response<Player> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(UserMenuActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UserMenuActivity.this,MainActivity.class));
                    }
                    //if couldn't delete- toast user
                    if(response.body().getIsSuccess()==0)
                        Toast.makeText(UserMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(UserMenuActivity.this, "STOPPED THE SEARCH FOR AN OPPONENT", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Player> call, Throwable t) {
                    Toast.makeText(UserMenuActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserMenuActivity.this,MainActivity.class));
                }
            });
        }

        //if user chose new game- go to find_opponent. else- start desired activity
        switch(choice){
            case "newgame":
                find_opponent();
                break;
            case "logout":
                //logging out user
                SharedPref.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(UserMenuActivity.this,LoginActivity.class));
                break;
            case "topten":
                startActivity(new Intent(UserMenuActivity.this,TopTenActivity.class));
                break;
            case "lastten":
                startActivity(new Intent(UserMenuActivity.this,LastTenActivity.class));
                break;
        }
    }

    /* find_opponent search for an opponent in active_players db table
     * if not found - we try again every 5000ms until found (or user clicked on something else and cancelled the timer)
     * when found - start a new GameActivity*/
    public void find_opponent(){
        Call<Game> create_game = api.create_game(username);

        /* running the retrofit call to create_games repeatedly until opponent is found*/
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //cloning for a new call each time
                create_game.clone().enqueue(new Callback<Game>() {
                    @Override
                    public void onResponse(Call<Game> call, Response<Game> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(UserMenuActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(UserMenuActivity.this,MainActivity.class));
                        }

                        //no available opponent- display toast to user and update status as waiting.
                        if(response.body().getIsSuccess()==0){
                            Toast.makeText(UserMenuActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            waiting = true;

                        }
                        //found an opponent
                        else{

                            timer.cancel();
                            waiting = false;

                            String otherOp;
                            int whichOp;
                            String msg="";

                            //checking if I'm the first or second player
                            if(response.body().getOpponent1().equals(username)){
                                whichOp=1;
                                otherOp=response.body().getOpponent2();
                                msg+="You go first!";
                            }
                            else{
                                whichOp=2;
                                otherOp=response.body().getOpponent1();
                                msg+="You go second!";
                            }

                            Intent i = new Intent(getApplicationContext(), GameActivity.class);
                            i.putExtra("whichOp", whichOp);
                            i.putExtra("otherOp", otherOp);
                            startActivity(i);

                            //moving to gameActivity
                            Toast.makeText(UserMenuActivity.this,msg+"\nYou vs. "+otherOp,Toast.LENGTH_LONG).show();
                        }}

                    @Override
                    public void onFailure (Call <Game> call, Throwable t){
                        Toast.makeText(UserMenuActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserMenuActivity.this,MainActivity.class));
                    }
                });

            };
        };
        //do the retrofit call every 5000 msec
        timer.schedule(timerTask, 0, 5000);
    }
}
