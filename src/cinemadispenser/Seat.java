/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser;

import java.io.Serializable;

/**
 *
 * @author gledrian
 */
public class Seat implements Serializable {
    final int row;
    final int col;
    
    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o){
        if(o!=null && o instanceof Seat){
            Seat seat = (Seat) o;
            return this.row == seat.row && this.col==seat.col;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return row * col;
    }

    /**
     * Gets row
     * @return int row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets col
     * @return int col
     */
    public int getCol() {
        return col;
    }
}
