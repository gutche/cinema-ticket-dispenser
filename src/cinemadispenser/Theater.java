/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser;

import java.io.*;
import java.util.*;

/**
 *
 * @author gledrian
 */
public class Theater implements Serializable {
    private int number;
    private int price;
    private HashSet<Seat> seatSet = new HashSet<Seat>();
    private Film film;
    private ArrayList<Session> sessionsList = new ArrayList<Session>();
    private int maxRow;
    private int maxCol;
    private File seatingArrangement;
    
    public Theater(File file) throws IOException {
        seatingArrangement = file;
        loadSeats();
    }

    /**
     * Sets film
     * @param film Film film
     */
    public void setFilm(Film film) {
        this.film = film;
        price = film.getPrice();
        number = film.getTheaterNumber();
        sessionsList = film.getSessionArrayList();
    }

    /**
     * Gets theater number
     * @return int number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets price
     * @return int price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets seatSet
     * @return Set seatSeet
     */
    public Set getSeatSet() {
        return seatSet;
    }

    /**
     * Gets film
     * @return Film film
     */
    public Film getFilm() {
        return film;
    }

    /**
     * Gets list of sessions
     * @return Array List sessionsList
     */
    public ArrayList<Session> getSessionList() {
        return sessionsList;
    }

    /**
     * Gets maximum number of rows in theater's seating arrangement
     * @return int maxRow
     */
    public int getMaxRow() {
        return maxRow;
    }

    /**
     * Gets maximum number of columns in theater's seating arrangement
     * @return int maxCol
     */
    public int getMaxCol() {
        return maxCol;
    }

    /**
     * Reads the seating arrangement file for this theater
     * @throws FileNotFoundException
     */
    public void loadSeats() throws FileNotFoundException {
        Scanner scTheater = new Scanner(new FileReader(seatingArrangement));
        int row = 1;
        int col = 1;
        while (scTheater.hasNextLine()) {
            String line = scTheater.nextLine();
            for (char ch: line.toCharArray()) {
                if (ch == '*') {
                    seatSet.add(new Seat(row, col));
                }
                col += 1;
            }
            if (maxCol < col - 1) {
                maxCol = col;
            }
            if (maxRow < row ) {
                maxRow = row;
            }
            col = 1;
            row += 1;
        }
    }
}
