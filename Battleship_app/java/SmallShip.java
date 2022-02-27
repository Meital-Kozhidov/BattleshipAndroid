/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

/* This class represents a small ship - only one square = one location
 * used by GameActivity */

public class SmallShip {
    int _location;
    public SmallShip(){}
    public void set_location(int location){_location = location;}
    public int get_location(){
        return _location;
    }
}
