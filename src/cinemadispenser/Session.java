/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gledrian
 */
public class Session implements Serializable {

    private LocalTime time;
    private final Set<Seat> occupiedSeatSet = new HashSet<>();


    public Session(LocalTime time) {
        this.time = time;
    }

    /**
     * Checks if a seat is occupied in this session
     * @param row int row
     * @param col int col
     * @return boolean isOcuppied
     */
    public boolean isOccupied(int row, int col){
        return occupiedSeatSet.contains(new Seat(row,col));
    }

    /**
     * Occupies seat in the session
     * @param row int row
     * @param col int col
     */
    public void occupySeat(int row, int col) {
        occupiedSeatSet.add(new Seat(row,col));
    }

    /**
     * Unoccupies seat in the session
     * @param row int row
     * @param col int col
     */
    public void unoccupySeat(int row, int col) {
        occupiedSeatSet.remove(new Seat(row,col));
    }

    /**
     * Gets time of the session
     * @return LocalTime time
     */
    public LocalTime getTime() {
        return time;
    }

    
    
}
