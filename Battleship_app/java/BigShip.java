/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

/* This class represents a big ship = two squares so two location
 * used by GameActivity */

public class BigShip {
    int _location1;
    int _location2;

    public BigShip(){}
    public void set_locations(int location1, int location2){_location1 = location1;
    _location2=location2;}

    public int get_location1(){
        return _location1;
    }
    public int get_location2(){
        return _location2;
    }
    public void set_location1(int loc){
        _location1=loc;
    }
    public void set_location2(int loc){
        _location2=loc;
    }
}
