/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser.modes;

import cinemadispenser.*;
import sienens.CinemaTicketDispenser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gledrian
 */
public class MovieTicketSale extends Operation {
    
    private MultiplexState state = new MultiplexState();
    private Theater theater;
    private Session session;
    private  List<Seat> seatsSelected = new ArrayList<>();
    private  Operation payment;

    public MovieTicketSale(CinemaTicketDispenser dispenser, Multiplex multiplex) throws IOException {
        super(dispenser, multiplex);
        payment = new PerformPayment(dispenser,multiplex);
    }

    @Override
    public void doOperation() {
        try {
            theater = selectTheater();
            if(theater!=null){
                session = selectSession(theater);
                if(session!=null){
                    selectSeats(session);
                    if (!seatsSelected.isEmpty()) {
                        performPayment();
                    }
                }    
            }
        } catch (IOException ex) {
            Logger.getLogger(MovieTicketSale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Selects theater
     * @return Theater theater
     */
    private Theater selectTheater() {
        getDispenser().setMenuMode();
        getDispenser().setTitle(getMultiplex().getLanguangeBundle().getString("MovieTicketSale_Film_Title"));
        getDispenser().setDescription("");
        for (int index = 0; index < state.getNumberOfTheaters(); index++) {
            getDispenser().setOption(index, state.getTheater(index).getFilm().getTitle());
        }
        if (state.getNumberOfTheaters()<6){
            for (int index = state.getNumberOfTheaters(); index < 6; index++) {
                getDispenser().setOption(index, null);
            }
        }

        getDispenser().setOption(5,getMultiplex().getLanguangeBundle().getString("Cancel"));
        char c = getDispenser().waitEvent(30);

        switch (c) {
            case 'A':
                return state.getTheater(0);
            case 'B':
                return state.getTheater(1);
            case 'C':
                return state.getTheater(2);
            case 'D':
                return state.getTheater(3);
            default:
                break;
        }
        return null;
    }

    /**
     * Selects session
     * @param theater Theater theater
     * @return Theater theater
     */
    private Session selectSession(Theater theater) {
        getDispenser().setTitle(getMultiplex().getLanguangeBundle().getString("MovieTicketSale_Session_Title"));
        getDispenser().setImage(theater.getFilm().getPoster());
        getDispenser().setDescription(theater.getFilm().getDescription());

        for (int index = 0; index < theater.getSessionList().size(); index++) {
            getDispenser().setOption(index, theater.getSessionList().get(index).getTime().toString());
        }

        if (theater.getSessionList().size()<6){ //Si hay menos sessiones que botónes, rellenas el resto de botónes con null
            for (int index = theater.getSessionList().size(); index < 6; index++) {
                getDispenser().setOption(index, null);
            }
        }

        getDispenser().setOption(5,getMultiplex().getLanguangeBundle().getString("Cancel"));
        char c = getDispenser().waitEvent(30);

        switch (c) {
            case 'A':
                return theater.getSessionList().get(0);
            case 'B':
                return theater.getSessionList().get(1);
            case 'C':
                return theater.getSessionList().get(2);
            case 'D':
                return theater.getSessionList().get(3);
            default:
                break;
        }
        return null;
    }

    /**
     * Selects seats
     * @param session Session session
     * @throws IOException
     */
    private void selectSeats(Session session) throws IOException {
        presentSeats();
        getDispenser().setTitle(getMultiplex().getLanguangeBundle().getString("MovieTicketSale_Seat_Title"));
        getDispenser().setOption(0, getMultiplex().getLanguangeBundle().getString("Cancel"));
        getDispenser().setOption(1, getMultiplex().getLanguangeBundle().getString("Accept"));

        boolean exit = false;
        while (!exit) {

            char c = getDispenser().waitEvent(30);

            if (c == 'A' || c == '\u0000') {
                seatsSelected.clear();
                exit = true;
            } else if (c == 'B') {
                if (!seatsSelected.isEmpty()) {  //Must select at least 1 seat to continue
                    exit = true;
                }
            } else if (c != 0) {
                byte col = (byte) (c & 0xFF);
                byte row = (byte) ((c & 0xFF00) >> 8);
                if (!session.isOccupied(row, col) && !seatsSelected.contains(new Seat(row,col))) {
                    //Marks seat
                    if (seatsSelected.size() < 4) {
                        getDispenser().setTitle(getMultiplex().getLanguangeBundle().getString("MovieTicketSale_Ticket_Row") + row + " " + getMultiplex().getLanguangeBundle().getString("MovieTicketSale_Ticket_Seat") + col);
                        getDispenser().markSeat(row, col, 3);
                        seatsSelected.add(new Seat(row, col));
                    }
                } else {
                    // Only unselect seats that have been selected in the current operation
                    if (seatsSelected.contains(new Seat(row, col))) {
                        getDispenser().setTitle(getMultiplex().getLanguangeBundle().getString("MovieTicketSale_Ticket_Row") + row + " " + getMultiplex().getLanguangeBundle().getString("MovieTicketSale_Ticket_Seat") + col);
                        getDispenser().markSeat(row, col, 2);
                        seatsSelected.remove(new Seat(row, col));
                    }
                }
            }
        }
    }

    /**
     * Calculates total price and executes PerformPayment Operation
     * @throws IOException
     */
    private void performPayment() throws IOException {
       getMultiplex().setTotalPrice(computePrice());
       getDispenser().setMessageMode();
       getDispenser().setTitle(getMultiplex().getLanguangeBundle().getString("MovieTicketSale_PerformPayment_Title"));
       getDispenser().setDescription(seatsSelected.size()+ " " + getMultiplex().getLanguangeBundle().getString(seatsSelected.size() == 1 ? "MovieTicketSale_PerformPayment_Description_1" : "MovieTicketSale_PerformPayment_Description_More") + theater.getFilm().getTitle() + ", " + getMultiplex().getTotalPrice() + " €");
       getDispenser().setOption(0,getMultiplex().getLanguangeBundle().getString("Cancel"));
       getDispenser().setOption(1,null);
       char c = getDispenser().waitEvent(30);

       if (c== '1') {
           payment.doOperation();
           if (getMultiplex().isSuccessfulPurchase()) {
               for (Seat seat : seatsSelected) {
                   session.occupySeat(seat.getRow(), seat.getCol());
               }
               printTicket();
               seatsSelected.clear();
               serializeMultiplexState();
           }
       }
    }

    /**
     * Displays states of seats in the selected session
     */
    private void presentSeats() {
        getDispenser().setTheaterMode(theater.getMaxRow(), theater.getMaxCol());
        for (int row = 1; row <= theater.getMaxRow(); row++) {
            for (int col = 1; col <= theater.getMaxCol(); col++) {
                if (theater.getSeatSet().contains(new Seat(row,col))) {
                    if (session.isOccupied(row, col)) {
                        getDispenser().markSeat(row, col, 1);
                    } else {
                       getDispenser().markSeat(row, col, 2);
                    }
                } else {
                    getDispenser().markSeat(row, col, 0);
                }
            }
        }
    }

    /**
     * Computes the total price
     * @return int totalPrice
     */
    private int computePrice() {
        return theater.getPrice() * seatsSelected.size();
    }

    /**
     * Prints tickets with discount info
     */
    private void printTicket() {
        for(Seat s : seatsSelected){
            ArrayList<String> ticket = new ArrayList<>();
            ticket.add("   Entrada para " + theater.getFilm().getTitle());
            ticket.add("   ===================");
            ticket.add("   Sala " + theater.getNumber());
            ticket.add("   " + session.getTime());

            ticket.add("   Fila " + s.getRow());
            ticket.add("   Asiento " + s.getCol());
            ticket.add("   Precio " + theater.getPrice() + "€");
            ticket.add("   Descuento: " + (getMultiplex().isMember() ?"30%":"0%"));
            state.getTicketList().add(ticket);
            getDispenser().print(ticket);
        }
    }

    /**
     * Serializes MultiplexState file (saves changes)
     * @throws IOException
     */
    public void serializeMultiplexState() throws IOException {
        ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(new File("./src/resources/MultiplexState").toString()));
        file.writeObject(state);
        file.close();
    }

    /**
     * Deserializes MultiplexState.bin file so we could load it as the current state instead of making a new one. Used when reloading
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void deserializeMultiplexState() throws IOException, ClassNotFoundException {
        ObjectInputStream file = new ObjectInputStream(new FileInputStream(new File("./src/resources/MultiplexState").toString()));
        state = (MultiplexState) file.readObject();
        file.close();
    }

    /**
     * Gets state
     * @return MultiplexState state
     */
    public MultiplexState getState() {
        return state;
    }

    @Override
    public String getTitle() {
        return getClass().getSimpleName();
    }
    
}
