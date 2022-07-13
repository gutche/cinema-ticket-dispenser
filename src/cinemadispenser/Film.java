/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser;

import javafx.util.Pair;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author gledrian
 */
public class Film implements Serializable {
    
    private String title;
    private String poster;
    private Duration duration;
    private String description;
    private ArrayList<Session> sessionArrayList = new ArrayList<>();
    private int theaterNumber;
    private int price;
    
    public Film(File file) throws IOException {
        Scanner scanner = new Scanner (new FileReader(file));

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith("Title:")) {
                title = cleanLine(line);
            } else if (line.startsWith("Description:")){
                description = cleanLine(line);
            } else if (line.startsWith("Sessions:")) {
                String sessions = cleanLine(line);
                duration = Duration.between(LocalTime.parse(sessions.substring(0,5), DateTimeFormatter.ofPattern("HH:mm")),LocalTime.parse(sessions.substring(6,11), DateTimeFormatter.ofPattern("HH:mm")));
                while (!sessions.isEmpty()) {
                    sessionArrayList.add(new Session(LocalTime.parse(sessions.substring(0,5), DateTimeFormatter.ofPattern("HH:mm"))));
                    sessions = sessions.substring(sessions.indexOf(":")+3).trim();
                }
            } else if (line.startsWith("Poster:")) {
                poster = "./src/resources/posters/" + cleanLine(line);
            } else if (line.startsWith("Price:")) {
                String priceString = cleanLine(line);
                price = Integer.parseInt(priceString.substring(0, cleanLine(line).indexOf("€")-1));
            } else if (line.startsWith("Theatre:")) {
                theaterNumber = Integer.parseInt(cleanLine(line));
            }
        }       
    }

    /**
     * Removes whitespaces in a string
     * @param line String line
     * @return String line
     */
    private String cleanLine(String line) {
        return line.substring(line.indexOf(":")+1).trim();
    }

    /**
     * Gets film title
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets film poster
     * @return String poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     * Gets film duration
     * @return Duration duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Gets film description
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets list of sessions
     * @return ArrayList Session sessionArrayList
     */
    public ArrayList<Session> getSessionArrayList() {
        return sessionArrayList;
    }

    /**
     * Gets theater number
     * @return int theaterNumber
     */
    public int getTheaterNumber() {
        return theaterNumber;
    }

    /**
     * Gets price
     * @return int price
     */
    public int getPrice() {
        return price;
    }
}
