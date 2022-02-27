/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */

package com.example.battleship;

import java.util.ArrayList;
import java.util.List;

/* This class represents the ships the user chose - two big and two small*/

public class PlayerShips {
    SmallShip ss1;
    SmallShip ss2;
    BigShip bs1;
    BigShip bs2;
    List<Integer> locations;

    public PlayerShips(){
        ss1 = new SmallShip();
        ss2 = new SmallShip();
        bs1 = new BigShip();
        bs2= new BigShip();
        locations= new ArrayList<>();
    }
    public PlayerShips(SmallShip ss1,
                       SmallShip ss2,
                       BigShip bs1,
                       BigShip bs2,
                       ArrayList<Integer> locations){
        this.ss1 = ss1;
        this.ss2 = ss2;
        this.bs1 = bs1;
        this.bs2= bs2;
        this.locations= locations;
    }

    /*this method is called after ship is being positioned again - meaning, user moved a ship
     * for not the first time. we remove the old position of the ship from locations*/
    public void remove_loc(int pre_loc){
        for(int i=0; i<locations.size();i++){
            if(locations.get(i) == pre_loc) {
                locations.remove(i);
                return;
            }
        }
    }

    /* setting the ships locations -
     * if the ship has a location we remove it with remove_loc
     * in anyway we set the new location to the ship and add to locations*/
    public void set_ss1(int place){
        int pre_loc = ss1.get_location();
        if(pre_loc!=0)
            remove_loc(pre_loc);
        ss1.set_location(place);
        locations.add(place);
    }
    public void set_ss2(int place){
        int pre_loc = ss2.get_location();
        if(pre_loc!=0){
            remove_loc(pre_loc);
        }
        ss2.set_location(place);
        locations.add(place);
    }
    public void set_bs1(int place1, int place2){
        int pre_loc = bs1.get_location1();
        int pre_loc_2 = bs1.get_location2();
        if(pre_loc!=0) {
            remove_loc(pre_loc);
            remove_loc(pre_loc_2);
        }
        bs1.set_locations(place1, place2);
        locations.add(place1);
        locations.add(place2);
    }
    public void set_bs2(int place1, int place2){
        int pre_loc = bs2.get_location1();
        int pre_loc_2 = bs2.get_location2();
        if(pre_loc!=0) {
            remove_loc(pre_loc);
            remove_loc(pre_loc_2);
        }
        bs2.set_locations(place1, place2);
        locations.add(place1);
        locations.add(place2);
    }

    //getters
    public int get_bs1_first(){
        return bs1.get_location1();
    }
    public int get_bs1_second(){
        return bs1.get_location2();
    }

    public int get_bs2_first(){
        return bs2.get_location1();
    }
    public int get_bs2_second(){
        return bs2.get_location2();
    }

    public int get_ss1(){
        return ss1.get_location();
    }
    public int get_ss2(){
        return ss2.get_location();
    }

    public List<Integer> get_locations(){
        return locations;
    }

}
