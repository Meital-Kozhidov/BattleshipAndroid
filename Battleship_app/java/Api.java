package com.example.battleship;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Meital Kozhidov for Tamar Benaya, 04/2021
 *
 *
 * BATTLESHIP REST API
 *
 * this REST API interface defines the HTTP server requests
 * used by Retrofit client
 * All methods will allow us perform a HTTP POST or GET request
 * to the specified url- the api stored in the server.
 * it also defines the data mapping of the responses
 *
 *
 */

public interface Api {

    /*these methods insert data to battleship database and return a player object*/
    @POST("register.php")
    @FormUrlEncoded
    Call<Player> register(@Field("username") String username, @Field("password") String password);

    @POST("login.php")
    @FormUrlEncoded
    Call<Player> login(@Field("username") String username, @Field("password") String password);


    /*these methods get data from battleship db's tables users and last_games and return a player object*/
    @GET("get_wins.php")
    Call<Player> getWins(@Query("username") String username);

    @GET("last_ten.php")
    Call<Player> last_games(@Query("username") String username);

    @GET("top_ten.php")
    Call<Player> top_players();


    /*this method inserts data to battleship database , table active_players and return a Game object*/
    @POST("search_opponent.php")
    @FormUrlEncoded
    Call<Game> create_game(@Field("username") String username);


    /*this method gets data from battleship database , table active_games and return a GameHandler object*/
    @GET("status_game.php")
    Call<GameHandler> game_status(@Query("username") String username,
                           @Query("whichOp") int whichOp);

    /*these methods inserts data to battleship database , table active_games and return a Game object*/
    @POST("update_game.php")
    @FormUrlEncoded
    Call<Game> game_update(@Field("username") String username, @Field("hit_location") int location,
                           @Field("last_hitOrMiss") String hitOrMiss, @Field("whichOp") int whichOp,
                           @Field("otherOp") String otherOp);

    @POST("update_game_stat.php")
    @FormUrlEncoded
    Call<Game> update_game_stat(@Field("username") String username, @Field("whichOp") int whichOp);


    /*this method insert data to battleship database , table last_games and return a Game object*/
    @POST("save_last_games.php")
    @FormUrlEncoded
    Call<Game> save_last(@Field("username") String username, @Field("result") int result);


    /*this method delete a row of battleship database, table active_players*/
    @POST("delete_active.php")
    @FormUrlEncoded
    Call<Player> delete_active(@Field("username") String username);

    /*this method delete a row of battleship database, table active_games*/
    @POST("delete_game.php")
    @FormUrlEncoded
    Call<Game> delete_game(@Field("username") String username, @Field("last_status") String last_status,
                           @Field("whichOp") int whichOp);
}

