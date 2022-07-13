/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinemadispenser.modes;

import cinemadispenser.Multiplex;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import javax.naming.CommunicationException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gledrian
 */
public class PerformPayment extends Operation{
    private final UrjcBankServer bank = new UrjcBankServer();
    
    public PerformPayment(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser,multiplex);
    }
   
    @Override
    public void doOperation() {
            getDispenser().expelCreditCard(0);
            getDispenser().setTitle(getTitle());
            getDispenser().setDescription(getMultiplex().getLanguangeBundle().getString("PerformPayment_ProcessingPayment"));
            getDispenser().waitEvent(3);
                if(bank.comunicationAvaiable()) {
                    try {
                        getMultiplex().setMember(getMultiplex().getCardList().contains(getDispenser().getCardNumber()));
                        if (bank.doOperation(getDispenser().getCardNumber(), getMultiplex().isMember() ?  (int) (getMultiplex().getTotalPrice() * 0.7) : getMultiplex().getTotalPrice())){
                            getMultiplex().setSuccessfulPurchase(true);
                        }else{
                            getDispenser().setDescription(getMultiplex().getLanguangeBundle().getString("PerformPayment_InsufficientBalance_Description"));
                        }
                    } catch (CommunicationException ex) {
                        Logger.getLogger(PerformPayment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    getDispenser().setDescription(getMultiplex().getLanguangeBundle().getString("PerformPayment_!comunicationAvaiable_Title"));
                }
            getDispenser().setTitle(getMultiplex().getLanguangeBundle().getString("PerformPayment_CreditCardExpelled_Description"));
            getDispenser().setDescription(getMultiplex().getLanguangeBundle().getString(getMultiplex().isMember()? "PerformPayment_Discount_Description" : "PerformPayment_Thank_Description"));
            getDispenser().setOption(0, null);
            boolean isRemoved = getDispenser().expelCreditCard(30);
            if(!isRemoved) {
                getDispenser().retainCreditCard(true);
            }
    }
    
    @Override
    public String getTitle() {
        return getMultiplex().getLanguangeBundle().getString("PerformPayment_Wait_Title");
    }


}
