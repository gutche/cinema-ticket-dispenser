/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser;

import cinemadispenser.modes.MainMenu;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import cinemadispenser.modes.MovieTicketSale;
import sienens.CinemaTicketDispenser;
import java.io.FileReader;
import java.io.File;


/**
 *
 * @author gledrian
 */
public class Multiplex {

    private String language;
    private final CinemaTicketDispenser dispenser = new CinemaTicketDispenser();
    private final MainMenu mainMenu = new MainMenu(dispenser,this);
    private final ArrayList<String> languageList = new ArrayList<>();
    private final ArrayList<Long> cardList = new ArrayList<>();
    private boolean successfulPurchase = false;
    private int totalPrice;
    private boolean member;

    public Multiplex() throws IOException {
    }

    /**
     * Starts the program
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void start() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(new FileReader(new File("./src/resources/members_list/Tarjetas.txt").toString()));
        while(sc.hasNextLine()) {
            cardList.add(Long.parseLong(sc.nextLine().replaceAll(" ","")));
        }
        
        File languagesDir = new File("./src/resources/languages/");
        // searches for languages and adds them to language list
        for (File languageFile: Objects.requireNonNull(languagesDir.listFiles())) {
            languageList.add(languageFile.toString().substring(26,languageFile.toString().indexOf(".p")));
        }

        dispenser.setMessageMode();
        dispenser.setTitle("Is it a new day or a reload?");
        dispenser.setOption(0,"Reload");
        dispenser.setOption(1,"New day");
        char option = dispenser.waitEvent(30);

        if (option == 'A') {
            if (mainMenu.operationList.get(1) instanceof MovieTicketSale) {
                ((MovieTicketSale) mainMenu.operationList.get(1)).deserializeMultiplexState();
            }
        }else  {
            if (mainMenu.operationList.get(1) instanceof MovieTicketSale) {
                ((MovieTicketSale) mainMenu.operationList.get(1)).getState().loadMoviesAndSessions();
            }
        }

        setLanguage(getLanguageList().get(3));
        while(true){
                mainMenu.doOperation();
        }
    }

    /**
     * Checks if purchase is successful
     * @return boolean successfulPurchase
     */
    public boolean isSuccessfulPurchase() {
        return successfulPurchase;
    }

    /**
     * Sets state of purchase
     * @param successfulPurchase boolean successfulPurchase
     */
    public void setSuccessfulPurchase(boolean successfulPurchase) {
        this.successfulPurchase = successfulPurchase;
    }

    /**
     * Gets total price
     * @return int totalPrice
     */
    public int getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets total price
     * @param totalPrice int totalPrice
     */
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Gets language being used
     * @return String language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language to use
     * @param lang String language
     */
    public void setLanguage(String lang) {
        language = lang;
    }

    /**
     * Gets list of languages available
     * @return ArrayList String languageList
     */
    public ArrayList<String> getLanguageList(){
        return languageList;
    }

    /**
     * Gets list of cards
     * @return ArrayList Long cardList
     */
    public ArrayList<Long> getCardList() {
        return cardList;
    }

    /**
     * Checks if card is a member
     * @return boolean member
     */
    public boolean isMember() {
        return member;
    }

    /**
     * Sets member
     * @param member boolean member
     */
    public void setMember(boolean member) {
        this.member = member;
    }

    /**
     * Gets Language Bundle
     * @return java.util.ResourceBundle.getBundle("resources/languages/" + language)
     */
    public ResourceBundle getLanguangeBundle() {
        return java.util.ResourceBundle.getBundle("resources/languages/" + language);
    }
}