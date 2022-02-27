/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */
package com.example.battleship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*GAME ACTIVITY- This class controls a single two-opponents battleship game
* from the game start (user is transferred to this activity after the server found him an opponent
* and the game can begin) until the games end (user presses return during or after the winner
* is announced and return to UserMenuActivity
* */

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    //number of locations for the ships
    final int SHIP_PLACES=6;

    //finals for building the game board
    final int BOARD_ROWS=5;
    final int BOARD_COLS=5;
    final int BOARD_WIDTH =200;
    final int BOARD_HEIGHT=200;
    final int MARGIN_COL =0;
    final int RIGHT_MARGIN_COL =5;      //space between cols
    final int ROWS_MARGIN_BOTTOM=5;     //space between rows
    final int ROWS_MARGIN=10;
    final int PADDING=45;

    final int WIN = 1;
    final int LOST = 2;

    //userShips stores the user's ships locations
    PlayerShips playerShips;

    //onTouchListener variables
    float mInitialX, mInitialY;
    int mInitialLeft, mInitialTop;
    View mMovingView = null;

    //ship images
    ImageView bs1,bs2,ss1,ss2;

    //game over image
    ImageView gameoverpic;

    //the score of each round
    TextView score;

    Button readybt,returnUserScreen;
    TableLayout tl;
    RelativeLayout rl;

    //board is only clickable when my turn
    boolean clickable=false;

    //counters for hits
    int hit_count_op=0;
    int hit_count_user=0;

    String username;
    int whichOp;
    String otherOp;

    boolean gameOver;
    String h=""; //for hitOrMiss

    //user pressed return
    boolean exited;

    //the square I shot last round
    View last_shot;

    Api api = ApiClient.getClient().create(Api.class);


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle extras = getIntent().getExtras();
        username = SharedPref.getInstance(this).LoggedInUser();
        whichOp = extras.getInt("whichOp");
        otherOp = extras.getString("otherOp");

        exited = false;
        playerShips= new PlayerShips();
        gameOver=false;

        score = (TextView)findViewById(R.id.score);
        readybt = (Button)findViewById(R.id.readybt);
        returnUserScreen=(Button)findViewById(R.id.returnUserScreen);
        gameoverpic = (ImageView)findViewById(R.id.gameover);
        gameoverpic.setVisibility(View.INVISIBLE);

        bs1 = (ImageView)findViewById(R.id.bigShip);
        bs2 = (ImageView)findViewById(R.id.bigShip2);
        ss1 = (ImageView)findViewById(R.id.smallShip);
        ss2 = (ImageView)findViewById(R.id.smallShip2);

        /*setOnTouchListener to the ships*/
        bs1.setOnTouchListener(this);
        bs2.setOnTouchListener(this);
        ss1.setOnTouchListener(this);
        ss2.setOnTouchListener(this);

        tl = (TableLayout) findViewById(R.id.main_table);
        rl = (RelativeLayout)findViewById(R.id.rl);

        //setting the game board
        setLayout();
    }

    /*Building BOARD_ROWS X BOARD_COLS TABLE of ImageViews
    * each ImageView has a tag - from 1 to the number of ImageViews
    * we number the tags by chronological order: left upper square is 1,
    * right bottom square is BOARD_ROWS X BOARD_COLS.
    * and we set onClickListeners for the ImageViews*/
    public void setLayout(){
        int tag_counter=0;
        for (int i = 0; i < BOARD_ROWS; i++) {
            TableRow tr_head = new TableRow(this);
            tr_head.setBackgroundColor(Color.WHITE);
            TableLayout.LayoutParams lp =
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(ROWS_MARGIN,ROWS_MARGIN,ROWS_MARGIN,ROWS_MARGIN_BOTTOM);
            tr_head.setLayoutParams(lp);
            tr_head.setGravity(Gravity.CENTER_HORIZONTAL);
            for (int j=0; j<BOARD_COLS; j++){
                ImageView cols = new ImageView(this);
                TableRow.LayoutParams tr =
                        new TableRow.LayoutParams(BOARD_WIDTH,BOARD_HEIGHT);
                tr.setMargins(MARGIN_COL,MARGIN_COL,RIGHT_MARGIN_COL,MARGIN_COL);
                tag_counter++;
                cols.setTag(tag_counter);
                cols.setOnClickListener(this);
                cols.setBackgroundColor(Color.BLACK);
                cols.setPadding(PADDING, PADDING, PADDING, PADDING);
                tr_head.addView(cols,tr);           }
            tl.addView(tr_head, lp);
        }
    }

    @Override
    /*Setting the ships in the location the user dragged them to
    * this method deals with displaying the drag of the ship and its final loction
    * and stores the ship in userShips*/
    public boolean onTouch(View view, MotionEvent motionEvent) {
        RelativeLayout.LayoutParams mLayoutParams;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMovingView = view;
                mLayoutParams = (RelativeLayout.LayoutParams) mMovingView.getLayoutParams();
                mInitialX = motionEvent.getRawX();
                mInitialY = motionEvent.getRawY();
                mInitialLeft = mLayoutParams.leftMargin;
                mInitialTop = mLayoutParams.topMargin;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMovingView != null) {
                    mLayoutParams = (RelativeLayout.LayoutParams) mMovingView.getLayoutParams();
                    mLayoutParams.leftMargin = (int) (mInitialLeft + motionEvent.getRawX() - mInitialX);
                    mLayoutParams.topMargin = (int) (mInitialTop + motionEvent.getRawY() - mInitialY);
                    mMovingView.setLayoutParams(mLayoutParams);
                }
                break;

            case MotionEvent.ACTION_UP:
                /*user finished moving a single ship - store it in userShips by building new SetUserShips*/
                int top = (int)mMovingView.getTop();
                int left = (int)mMovingView.getLeft();
                int right = (int)mMovingView.getRight();
                String shipTag= view.getTag().toString();
                playerShips= new SetPlayerShips(top, left, right,shipTag,playerShips).getPlayerShips();

                mMovingView = null;
                break;
        }

        return true;
    }

    /*when user presses "ready" (after placing ships) it calls loadBoard
    * checking if all ships are placed
    * if so - we make the ships disappear
    * if I am op1- I play first - make the board clickable
    * else - call updateBoard()*/
    public void loadBoard(View v) {
        //user pressed "ready"
        if (playerShips.get_locations().size()<SHIP_PLACES)
            Toast.makeText(getApplicationContext(), "Please position ships correctly to play\n", Toast.LENGTH_LONG).show();

        else {
            //make the ships and ready button disappear
            readybt.setVisibility(View.INVISIBLE);
            bs1.setVisibility(View.INVISIBLE);
            bs2.setVisibility(View.INVISIBLE);
            ss1.setVisibility(View.INVISIBLE);
            ss2.setVisibility(View.INVISIBLE);

            //if I play first- my turn to shoot
            if (whichOp==1) {
                clickable = true;
                score.setText("You Go First- SHOOT!");
            }
            //else go to updateBoard() and wait for my turn
            else
                updateBoard();
            }
    }

    @Override
    /*this onClick method is called after clicking on the game board
    * we check if this square wasn't already shot - if not, we make an api call to update the game db*/
    public void onClick(View v) {

        //the click counts only if the board is clickable
        //the board is clickable only when it's my turn
        if (clickable) {
            //this square's already been shot
            if(v.getBackground() instanceof BitmapDrawable){
                Toast.makeText(getApplicationContext(), "ALREADY SHOT THAT SQUARE\n", Toast.LENGTH_LONG).show();
                return;
            }

            score.setVisibility(View.INVISIBLE);
            //change the background to show this square is now been shot
            v.setBackground(getResources().getDrawable(R.drawable.white_x,getTheme()));

            //save this square because we may need to change it (if next round we see it was a hit)
            last_shot = v;

            //the tag of the shooting square
            int place = Integer.parseInt(v.getTag().toString());

            //retrofit POST Call to update the game
            Call<Game> update_game = api.game_update(username, place,h,whichOp,otherOp);
            update_game.enqueue(new Callback<Game>() {
                @Override
                public void onResponse(Call<Game> call, Response<Game> response) {
                    //connection problem
                    if(!response.isSuccessful()){
                        Toast.makeText(GameActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(GameActivity.this,MainActivity.class));
                    }

                    //if when we try to update we discover the otherOp already exited
                    if (response.body().getMessage().equals("exit") && !exited) {
                        Toast.makeText(getApplicationContext(), otherOp + " EXITED THE GAME!", Toast.LENGTH_LONG).show();
                        finishedGame();
                    }
                    //else- we updated successfully the game in the db
                    else {
                        clickable = false;
                        updateBoard();
                    }
                }
                @Override
                //error handling
                public void onFailure (Call <Game> call, Throwable t){
                    Toast.makeText(GameActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GameActivity.this,MainActivity.class));
                }
            });
        }
    }

    /*This method gets the data of the current game with game_status GET request
    * we define a timer so we can make this api call every few seconds-
    * this way we can wait until it is this user's turn in the game:
    * if last_played is otherOp - we cancel the timer and read the data.
    * now it's our turn to click on the board!
    * if last_status is "exit" - otherOp exited , we show msg , exit too and cancel timer
    * if last_status is "win" - this user won! this is the way otherOp tells user he won. we show to user
    * and delete this game from db */

    public void updateBoard(){

        //API GET REQUEST
        Call<GameHandler> game_status = api.game_status(username, whichOp);

        //setting the timer
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                //this user exited (pressed return button) - cancel timer
                if (exited)
                    timer.cancel();

                else {
                    //clone the call to game_status in order to do this more than once
                    game_status.clone().enqueue(new Callback<GameHandler>() {
                        @Override
                        public void onResponse(Call<GameHandler> call, Response<GameHandler> response) {

                            //otherOp exited
                            if (response.body().getLast_status().equals("exit")) {
                                timer.cancel();
                                Toast.makeText(getApplicationContext(), otherOp + " EXITED THE GAME!", Toast.LENGTH_LONG).show();
                                finishedGame();
                            }

                            //this user won this game
                            else if (response.body().getLast_status().equals("win")) {
                                timer.cancel();
                                gameOver = true;
                                String s = "YOU WON! \n\n" + otherOp + " LOST\n\n\n" +
                                        "Congratulations!\n\n\n";
                                setEndLayout(s);
                                save_last(WIN);

                                //delete this game from db
                                Call<Game> delete_game = api.delete_game(username, "win", whichOp);

                                delete_game.enqueue(new Callback<Game>() {
                                    @Override
                                    public void onResponse(Call<Game> call, Response<Game> response) {
                                        if(!response.isSuccessful()){
                                            Toast.makeText(GameActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(GameActivity.this,MainActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Game> call, Throwable t) {
                                        Toast.makeText(GameActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(GameActivity.this,MainActivity.class));
                                    }
                                });
                            }

                            /*last_played is otherOp-
                             * we cancel the timer - it's this user's turn now
                             * we read the data :
                             * if the last square we shot was a "hit" :
                             * we change the background to red_x to show the user and add to counter*/
                            else if (response.body().getLast_played().equals(otherOp)) {

                                timer.cancel();
                                //last square this user shot we a hit!
                                if (response.body().getLast_hitOrMiss().equals("hit")) {
                                    last_shot.setBackground(getResources().getDrawable(R.drawable.red_x, getTheme()));
                                    hit_count_user++;
                                }
                                int shot_loc = response.body().getHit_Location();

                                //call animate_shot() to show this user where otherOp shot him
                                animate_shot(shot_loc);

                                if (playerShips.get_locations().contains(shot_loc)) {
                                    //It's a hit for otherOp!
                                    //storing "hit" in h for the later onClick method (it will store this in db)
                                    hit_count_op++;
                                    h = "hit";
                                }
                                //it's a miss for otherOp
                                //storing "miss" in h
                                else {
                                    h = "miss";
                                }

                                //it's this user's turn - the board is clickable
                                clickable = true;
                                readybt.setVisibility(View.INVISIBLE);

                                //otherOp won - displaying message and using api call to update this game last_status
                                //in the db so otherOp knows he won
                                if (hit_count_op == SHIP_PLACES) {
                                    gameOver = true;
                                    String s = "YOU LOST! \n\n" + otherOp + " WON\n\n\n" +
                                            "Better Luck Next Time!\n\n\n";
                                    setEndLayout(s);
                                    save_last(LOST);
                                    Call<Game> stat_game = api.update_game_stat(username, whichOp);
                                    stat_game.enqueue(new Callback<Game>() {
                                        @Override
                                        public void onResponse(Call<Game> call, Response<Game> response) {
                                            if(!response.isSuccessful()){
                                                Toast.makeText(GameActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(GameActivity.this,MainActivity.class));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Game> call, Throwable t) {
                                            Toast.makeText(GameActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(GameActivity.this,MainActivity.class));

                                        }
                                    });

                                }

                                //otherOp didn't win - show score
                                //plus if it was a hit for otherOp- animate the ship in the hit location
                                else {
                                    if (h.equals("hit")) {
                                        Animation anim;
                                        anim = AnimationUtils.loadAnimation(getApplicationContext(),
                                                R.anim.fade_out);

                                        ImageView v;
                                        if (playerShips.get_ss1() == shot_loc) {
                                            v = ss1;
                                        } else if (playerShips.get_ss2() == shot_loc) {
                                            v = ss2;
                                        } else if (playerShips.get_bs1_first() == shot_loc ||
                                                playerShips.get_bs1_second() == shot_loc) {
                                            v = bs1;
                                        } else {
                                            v = bs2;
                                        }

                                        v.startAnimation(anim);
                                    }
                                    score.setText("You: " + hit_count_user + " " + otherOp + ": " + hit_count_op + "\nYour turn- Shoot!");
                                }
                                    score.setVisibility(View.VISIBLE);

                            }
                            else {
                                //this isn't this user's turn yet
                                score.setText(otherOp + " turn, please wait ...");
                                score.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<GameHandler> call, Throwable t) {
                            Toast.makeText(GameActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GameActivity.this,MainActivity.class));
                        }
                    });
                }
            }
        };

        //repeat the TimerTask every 10000 msec
        timer.schedule(timerTask, 0, 10000);
    }

    /*Game is over - this method sets the end game screen
    * we add a "game over" picture and set the layout accordingly*/
    public void setEndLayout(String s){
        gameoverpic.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.gameover);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        score.setLayoutParams(params);

        params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.score);
        returnUserScreen.setLayoutParams(params);

        tl.setVisibility(View.INVISIBLE);
        rl.setBackground(getResources().getDrawable(R.drawable.endgamebg,getTheme()));
        score.setText(s);
    }

    /*return to userMenuActivity*/
    public void finishedGame(){
        Intent i = new Intent(getApplicationContext(), UserMenuActivity.class);
        startActivity(i);
    }

    /*this method saves this game score (win or lose) to the db in the db table of "last_games"
    * each user has its own row in the db table*/
    public void save_last(int result){
        Call<Game> save_last = api.save_last(username, result);
        save_last.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(GameActivity.this,"connection problem- try again later",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GameActivity.this,MainActivity.class));
                }
                if(response.body().getIsSuccess()==0)
                    Toast.makeText(GameActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                Toast.makeText(GameActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GameActivity.this,MainActivity.class));
            }
        });
    }


    /*pressedReturn() is called after the user pressed "return" button*/
    public void pressedReturn(View view){

        //to stop the timer we sign exited as "true"
        exited=true;

        //if the game is over the user pressed return -
        //we call finisedGame()
        if(gameOver){
            finishedGame();
        }
        //else - the game isn't over- we need to delete it from db
        else {
            Call<Game> delete_game = api.delete_game(username, "exit", whichOp);

            delete_game.enqueue(new Callback<Game>() {
                @Override
                public void onResponse(Call<Game> call, Response<Game> response) {
                    //after we delete it we can call finishedGame()
                    finishedGame();
                }

                @Override
                public void onFailure(Call<Game> call, Throwable t) {
                    Toast.makeText(GameActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    //animate the shot of otherOp - fade_in the shotted square, and
    //paint it to red for 5000 msc to show this user when otherOp shot him
    public void animate_shot(int loc){
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        ImageView x = findViewById(R.id.rl).findViewWithTag(loc);
        Drawable copy = x.getBackground();
        x.setBackgroundColor(Color.RED);
        x.startAnimation(animation);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(copy instanceof ColorDrawable)
                    x.setBackgroundColor(Color.BLACK);
                else
                    x.setBackground(copy);
            }
        }, 5000);
    }
}
