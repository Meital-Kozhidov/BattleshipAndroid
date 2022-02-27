/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

/*GAME CLASS : POJO RETROFIT RESPONSE CLASS - represents two types of responses from retrofit api Call
* first type is from calling create_game: this call returns all four fields
* second type is from calling game_update, update_game_stat, delete_game and save_last : these calls return
* only the fields of isSuccess and message since returning the opponents too is unnecessary.*/

public class Game {
    private String opponent1;
    private String opponent2;
    private int isSuccess;
    private String message;

    public Game(String opponent1, String opponent2, int isSuccess, String message){
        this.opponent1=opponent1;
        this.opponent2=opponent2;
        this.message=message;
        this.isSuccess=isSuccess;
    }

    public Game(int isSuccess, String message){
        this.message=message;
        this.isSuccess=isSuccess;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getOpponent1() {
        return opponent1;
    }

    public String getOpponent2() {
        return opponent2;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOpponent1(String opponent1) {
        this.opponent1 = opponent1;
    }

    public void setOpponent2(String opponent2) {
        this.opponent2 = opponent2;
    }
}
