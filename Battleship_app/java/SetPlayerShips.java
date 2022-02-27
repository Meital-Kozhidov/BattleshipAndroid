/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

/*  SetUserShips class
 *   after the user moves a ship to his desired location in Game Activity:
 *   MotionEvent.ACTION_UP calls a new instance of this class
 *   we check that the ship is in a right position (inside the board)
 *   if so we store the location of the ship in _playerShips
 *   method getPlayerShips returns _playerShips */

public class SetPlayerShips {
    final int BOARD_ROWS=5;
    final int BOARD_COLS=5;
    final int ROWS_START=400;
    final int ROWS_OFFSET=220;
    final int COL_START=20;
    final int COL_OFFSET=200;

    private PlayerShips _playerShips;

    public SetPlayerShips(int top, int left, int right,String shipTag,PlayerShips playerShips){
        int current_row = ROWS_START;
        int ship_row = 0;
        boolean found_row=false;
        int i =0;

        //checking location row-wise
        while(i < BOARD_ROWS && !found_row){
            if(top>current_row && top<current_row+ROWS_OFFSET){
                ship_row = i;
                found_row=true;
            }
            current_row+=ROWS_OFFSET;
            i++;
        }
        int j =1;
        boolean found_col=false;
        int current_col = COL_START;
        int first_place=0;
        int second_place=0; //for bigShips

        //checking location columns-wise
        while(j <= BOARD_COLS && !found_col){
            if(left>current_col && left<current_col+COL_OFFSET){
                first_place = j+ship_row*BOARD_COLS;
                found_col=true;
                if(right-left>COL_OFFSET) {
                    second_place = j + ship_row * BOARD_COLS + 1;
                }
            }
            current_col+=COL_OFFSET;
            j++;
        }

        //location is good - save the ship location: update userShip given as arg
        if(found_row && found_col) {
            switch (shipTag) {
                case "bs1":
                    playerShips.set_bs1(first_place, second_place);
                    break;

                case "bs2":
                    playerShips.set_bs2(first_place, second_place);
                    break;
                case "ss1":
                    playerShips.set_ss1(first_place);
                    break;
                case "ss2":
                    playerShips.set_ss2(first_place);
                    break;
            }
        }
        //saving userShips in this class _userShip variable
        _playerShips=playerShips;
    }

    //return our updated userShips
    public PlayerShips getPlayerShips(){
        return _playerShips;
    }
}
