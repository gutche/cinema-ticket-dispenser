/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser.modes;

import cinemadispenser.Multiplex;
import sienens.CinemaTicketDispenser;
/**
 *
 * @author gledrian
 */
public abstract class Operation {
    
    private final CinemaTicketDispenser dispenser;
    private final Multiplex multiplex;

    public abstract void doOperation();
    public abstract String getTitle();
    
    public Operation(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        this.dispenser = dispenser;
        this.multiplex = multiplex;
    }

    /**
     * Gets dispenser
     * @return CinemaTicketDispenser dispenser
     */
    public CinemaTicketDispenser getDispenser() {
        return dispenser;
    }

    /**
     * Gets multiplex
     * @return Multiplex multiplex
     */
    public Multiplex getMultiplex() {
        return multiplex;
    }
}
