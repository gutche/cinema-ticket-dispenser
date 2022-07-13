/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author gledrian
 */
public class MultiplexState implements Serializable {
    
    private ArrayList<Theater> theaterList = new ArrayList<Theater>();
    private ArrayList<Film> movieList = new ArrayList<>();
    private ArrayList<ArrayList<String>> ticketList = new ArrayList<>(); //Contiene los tickets vendidos del día
    
    public MultiplexState() throws IOException {
        File theaterDir = new File("./src/resources/theaters");
        for (File theaterFile : theaterDir.listFiles()) {
            theaterList.add(new Theater(theaterFile));
        }
    }

    /**
     * Loads movies and sessions in the movie directory
     * @throws IOException
     */
    public void loadMoviesAndSessions() throws IOException {
        File moviesDir = new File("./src/resources/movies");

        for (File movieFile : moviesDir.listFiles()) {
            Film movie = new Film(movieFile);
            theaterList.get(movie.getTheaterNumber()-1).setFilm(movie);
        }
    }

    /**
     * Gets theater from list of theaters available
     * @param theaterNumber int theaterNumber
     * @return Theater theater
     */
    public Theater getTheater(int theaterNumber) {
        return theaterList.get(theaterNumber);
    }

    /**
     * Gets number of theaters
     * @return int theaterList.size()
     */
    public int getNumberOfTheaters() {
        return theaterList.size();
    }

    /**
     * Gets list of theaters
     * @return ArrayList Theater theaterList
     */
    public ArrayList<Theater> getTheaterList() {
        return theaterList;
    }

    /**
     * Gets list of movies
     * @return ArrayList Film film
     */
    public ArrayList<Film> getMovieList() {
        return movieList;
    }

    /**
     * Gets list of tickets
     * @return ArrayList ArrayList String ticketList
     */
    public ArrayList<ArrayList<String>> getTicketList() {
        return ticketList;
    }
}
