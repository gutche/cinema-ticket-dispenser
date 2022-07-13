/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser.modes;


import cinemadispenser.Multiplex;
import sienens.CinemaTicketDispenser;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author gledrian
 */
public class MainMenu extends Operation{
    public ArrayList<Operation> operationList = new ArrayList<>();
    
    public MainMenu(CinemaTicketDispenser dispenser, Multiplex multiplex) throws IOException {
        super(dispenser,multiplex);
        operationList.add(new LanguageSelection(dispenser,multiplex));
        operationList.add(new MovieTicketSale(dispenser,multiplex));
    }

    /**
     * Presents Main Menu options
     */
    public void presentMenu() {
        getDispenser().setMessageMode();
        getDispenser().setTitle(getTitle());
        getDispenser().setDescription(getMultiplex().getLanguangeBundle().getString("MainMenu_Description"));
        getDispenser().setOption(0,getMultiplex().getLanguangeBundle().getString("MainMenu_Option1"));
        getDispenser().setOption(1,getMultiplex().getLanguangeBundle().getString("MainMenu_Option2"));
    }
    
    @Override
    public void doOperation() {
        presentMenu();
        char option = getDispenser().waitEvent(30);
        if (option == 'A') {
            for (Operation operation: operationList) {
                if (operation instanceof MovieTicketSale) {
                    operation.doOperation();
                }
            }
        } else if (option == 'B') {
            for (Operation operation : operationList) {
                if (operation instanceof LanguageSelection) {
                    operation.doOperation();
                }
            }
        }
    }
    
    @Override
    public String getTitle() {
        return getMultiplex().getLanguangeBundle().getString("MainMenu_Title");
    }
        
}
