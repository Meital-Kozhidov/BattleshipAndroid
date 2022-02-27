/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

import java.util.List;

/*PLAYER CLASS: POJO RETROFIT RESPONSE CLASS - represents a few types of responses from retrofit api calls
 * calling getWins: this call return only wins
 * calling top_ten: returns results,isSuccess,message
 * calling last_ten: return games,isSuccess,message
 * calling register,login,delete_active: return isSuccess and message.*/

public class Player {

    final String NOT_PLAYED = "0";
    final String WON = "1";

    private String username;
    private int wins;
    private List<Player> results;
    private List<Object> games;
    private int isSuccess;
    private String message;

    public Player(String username, int wins, int isSuccess, String message) {
        this.username = username;
        this.wins = wins;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(String email) {
        this.wins = wins;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResults(List<Player> results) {
        this.results = results;
    }

    //goes through the list on the top players and sets it as String to show to user
    public String getResults() {
        String s="";
        for(int i=0; i<results.size(); i++){
            int j = i+1;
            s+= j+". Username: "+results.get(i).getUsername()+" ; Wins: "+results.get(i).getWins()+"\n";
        }
        return s;
    }

    public void setGames(List<Object> games) {
        this.games = games;
    }

    //goes through the list on the ten last user's games and sets it as String to show to user
    public String getGames() {
        String s="";
        int j=1;
        for(int i=2; i<games.size(); i++){
            String status = games.get(i).toString();
            s+=j+". ";
            if(status.equals(NOT_PLAYED))
                s+="Not Played Yet\n";
            else if(status.equals(WON))
                s+="Won!\n";
            else
                s+="Lost!\n";
            j++;
        }
        return s;
    }



}