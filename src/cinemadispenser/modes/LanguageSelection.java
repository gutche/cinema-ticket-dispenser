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
public class LanguageSelection extends Operation {
    
    public LanguageSelection(CinemaTicketDispenser dispenser,Multiplex multiplex) {
        super(dispenser,multiplex);
    }
    
    @Override
    public void doOperation() {
        getDispenser().setMenuMode();
        getDispenser().setTitle(getTitle());
        getDispenser().setDescription("");

        int counter = 0;
        for (String language: getMultiplex().getLanguageList()) {
            getDispenser().setOption(counter, language.toUpperCase());
            counter++;
        }

        if (getMultiplex().getLanguageList().size() < 6) {
            for (int index = getMultiplex().getLanguageList().size(); index < 6;  index++) {
                getDispenser().setOption(index, null);
            }
        }

        getDispenser().setOption(5,"Cancel");

        // language selection
        char option = getDispenser().waitEvent(30);
        if (option == 'A') {
            getMultiplex().setLanguage(getMultiplex().getLanguageList().get(0));
        }
        else if (option == 'B') {
            getMultiplex().setLanguage(getMultiplex().getLanguageList().get(1));
        }
        else if (option == 'C') {
            getMultiplex().setLanguage(getMultiplex().getLanguageList().get(2));
        }
        else if (option == 'D') {
            getMultiplex().setLanguage(getMultiplex().getLanguageList().get(3));
        }
    }
    
    @Override
    public String getTitle() {
        return getMultiplex().getLanguangeBundle().getString("LanguageSelection_Title");
    }
}
