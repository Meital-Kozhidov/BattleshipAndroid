/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

/* GAMEHANDLER CLASS: POJO RETROFIT RESPONSE CLASS this class represents
 * the response from the retrofit api GET call of game_status*/

public class GameHandler {

    //the username of the opponent that played last
    private String last_played;

    //hit location of last_played
    private int hit_location;

    //the hit or miss of the *previous* hit_location.
    //last_played responses in this field to the hit_location of the opponent that played before
    private String last_hitOrMiss;

    //status is empty during the game.when "win"- the opponent
    //that is last_played won
    private String last_status;

    public GameHandler(String last_played,
            String last_hitOrMiss,
            int hit_location,
            String last_status){
        this.last_played=last_played;
        this.last_hitOrMiss=last_hitOrMiss;
        this.hit_location=hit_location;
        this.last_status=last_status;
    }

    public int getHit_Location() {
        return hit_location;
    }

    public void setHit_Location(int hit_Location) {
        this.hit_location = hit_Location;
    }

    public String getLast_hitOrMiss() {
        return last_hitOrMiss;
    }

    public String getLast_played() {
        return last_played;
    }

    public String getLast_status() {
        return last_status;
    }

    public void setLast_hitOrMiss(String last_hitOrMiss) {
        this.last_hitOrMiss = last_hitOrMiss;
    }

    public void setLast_played(String last_played) {
        this.last_played = last_played;
    }

    public void setLast_status(String last_status) {
        this.last_status = last_status;
    }
}
