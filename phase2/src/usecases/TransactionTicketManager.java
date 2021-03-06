package usecases;

import entities.Appointment;
import entities.ClientUser;
import entities.TransactionTicket;
import entities.TransactionTicketList;
import entities.*;
import gateway.ClientUserReadWrite;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static gateway.FileReadAndWrite.CLIENT_USER_FILE;

public class TransactionTicketManager {
    private ClientUserManager clientUserManager;
    private TransactionTicketList pendingTransactionTicketList;
    private TransactionTicketList historyTransactionTicketList;
    private AppointmentManager appointmentManager;
    private TradeManager tradeManager;

    public TransactionTicketManager(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.tradeManager  = new TradeManager(clientUserManager);
        this.appointmentManager = new AppointmentManager(clientUserManager);
    }

    public TransactionTicketManager(TransactionTicketList pendingTransactionTicketList, TransactionTicketList historyTransactionTicketList) {
        this.pendingTransactionTicketList = pendingTransactionTicketList;
        this.historyTransactionTicketList = historyTransactionTicketList;
    }
    public TransactionTicketManager(){
    }

    public void removeExpiredTicket(TransactionTicket ticket) {
        pendingTransactionTicketList.removeTransactionTicketList(ticket);
    }

    public void addTicketToHistory(TransactionTicket ticket) {
        historyTransactionTicketList.addToTransactionTicketList(ticket);
    }

    public void addTicketToPending(TransactionTicket ticket) {
        pendingTransactionTicketList.addToTransactionTicketList(ticket);
    }

    private boolean isCurrentUserProposer(TransactionTicket transactionTicket) {
        return transactionTicket.getProposer().equals(clientUserManager.getCurrentUser().getUserName());
    }

    private boolean isCurrentUserReceiver(TransactionTicket transactionTicket) {
        return transactionTicket.getReceiver().equals(clientUserManager.getCurrentUser().getUserName());
    }
    public boolean confirm(String id) {
        TransactionTicket transactionTicket = getTransactionTicketByTicketID(id);
        if (transactionTicket != null) {
            if (isCurrentUserProposer(transactionTicket)) {
                transactionTicket.setUser1Confirmed(true);
                System.out.println("Confirm successful");
                return true;
            } else if (isCurrentUserReceiver(transactionTicket)) {
                transactionTicket.setUser2Confirmed(true);
                System.out.println("Confirm successful");
                return true;
            }
        }
        return false;
    }

    /**
     * Provides transaction ticket history of the user
     * @param username
     * @return transactionticketlist generated by the user in history
     */
    public TransactionTicketList getHistoryTransactionTicketByUser(String username){
        TransactionTicketList historyTransactionTicketList = new TransactionTicketList();
        for(TransactionTicket ticket: this.historyTransactionTicketList.getTransactionTicketList()){
            if(ticket.getProposer().equalsIgnoreCase(username) || ticket.getReceiver().equalsIgnoreCase(username)){
                historyTransactionTicketList.getTransactionTicketList().add(ticket);
            }
        }
        return historyTransactionTicketList;
    }

    /**
     * Return pending transaction ticket list of the user
     * @param username
     * @return pending transaction ticket list of the given user
     */
    public TransactionTicketList getPendingTransactionTicketByUser(String username){
        TransactionTicketList pendingTicketList = new TransactionTicketList();
        for(TransactionTicket ticket: this.pendingTransactionTicketList.getTransactionTicketList()){
            if(ticket.getProposer().equalsIgnoreCase(username) || ticket.getReceiver().equalsIgnoreCase(username)){
                pendingTicketList.getTransactionTicketList().add(ticket);
            }
        }
        return pendingTicketList;
    }

    /**
     * Return list of pending transaction ticket ids of the user
     * @param username
     * @return list of pending transaction tickets of the given user
     */
    public List<String> getPendingTransactionTicketsID(String username){
        TransactionTicketList pendingTicketList = new TransactionTicketList();
        List<String> strList = new ArrayList<>();
        for(TransactionTicket ticket: this.pendingTransactionTicketList.getTransactionTicketList()){
            strList.add(ticket.getTicketID());
        }
        return strList;
    }

    /**
     * Return transactionTicket of the provided ticketId
     * @param ticketID
     * @return transactionticket if found with given ticketId, false otherwise
     */
    public TransactionTicket getTransactionTicketByTicketID(String ticketID){
        for(TransactionTicket ticket: clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList()){
            if (ticket.getTicketID().equals(ticketID)){
                return ticket;
            }
        }
        return null; // no matched ticket
    }

    // means that both user1 and user2 had physically attended the meeting and completed the trade.
    /**
     * Complete the trade for the given ticketId
     * @param ticketID
     * @return true if trade completes, false otherwise
     */
    public boolean completeTransaction(String ticketID) throws IOException {
        //TODO: finish this method
        TransactionTicket ticket = getTransactionTicketByTicketID(ticketID);
        boolean user1ConfirmStatus = ticket.getIsUser1Confirmed();
        boolean user2ConfirmStatus = ticket.getIsUser2Confirmed();
        //both users showed up-> transaction completed
        if (user1ConfirmStatus && user2ConfirmStatus){
            ticket.setIsCompleted(true);
            String user1 = ticket.getProposer();
            String user2 = ticket.getReceiver();
            ClientUser clientUser1 = clientUserManager.getUserByUsername(user1);
            ClientUser clientUser2 = clientUserManager.getUserByUsername(user2);
            //add to ticket history for each user
            clientUser1.getPendingTransaction().removeTransactionTicketList(ticket);
            clientUser2.getPendingTransaction().removeTransactionTicketList(ticket);
            clientUser1.addToHistory(ticket);
            clientUser2.addToHistory(ticket);
            Item item1 = ticket.getItem1(); //proposer user1
            Item item2 = ticket.getItem2(); //receiver user2
            //remove the items from each user's inventory and wishlist
            clientUser1.removeFromInventory(item1);
            clientUser2.removeFromInventory(item2);
            clientUser1.removeFromWishList(item1);
            clientUser2.removeFromWishList(item2);


            if (!ticket.getIsPermanent() && ticket.getTradeType().equals("trade")) {
                clientUser1.addToInventory(item2);
                clientUser2.addToInventory(item1);
                System.out.println(appointmentManager.getCompletedAppointmentById(ticket.getAppointmentId()).getId());
                System.out.println(ticket.getItem2().getItemId());
                System.out.println(ticket.getItem1().getItemId());
                if (tradeManager.trade(ticket.getItem2().getItemId(), ticket.getItem1().getItemId(), changeStringToDate(ticket.getTime()), appointmentManager.getCompletedAppointmentById(ticket.getAppointmentId()).getAddress(), true)) {
                    System.out.println("==== An appointment has been created ====");
                    ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
                }
            }
            return true;
        }
        else{
            //transaction is not complete
            return false;
        }
    }

    /**
     * Check whether user crosses transaction limit on the day
     * @param clientUserManager
     * @param transactionLimit
     * @return true if transaction limit of user is not crossing, false otherwise
     */
    public boolean checkUserTransactionLimit(ClientUserManager clientUserManager, int transactionLimit) {
        int transactionLimitCounter = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        Calendar transactionTicketDate = Calendar.getInstance();
        Calendar limitTransactionTicketDate = Calendar.getInstance();

        Calendar currentTime = Calendar.getInstance();
        System.out.println("Current Date: " + format.format(currentTime.getTime()));

        for (TransactionTicket ticket: clientUserManager.getCurrentUser().getHistory().getTransactionTicketList()) {
            String dateString = ticket.getTime();
            System.out.println(dateString);
            try {
                transactionTicketDate.setTime(format.parse(dateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            limitTransactionTicketDate.add(Calendar.DAY_OF_MONTH, -7);
            String newDate = format.format(limitTransactionTicketDate.getTime());
            System.out.println(currentTime.compareTo(limitTransactionTicketDate));
            System.out.println("NEW DATE: " + newDate);
            System.out.println("transactionLimit Counter: " + transactionLimitCounter);

            if (currentTime.compareTo(limitTransactionTicketDate) == 1) {

                transactionLimitCounter++;
            }
        }
        System.out.println("transactionLimit Counter: " + transactionLimitCounter);
        System.out.println("transactionLimit: " + transactionLimit);
        if (transactionLimitCounter >= transactionLimit) {
            return false;
        }
        else {
            return true;
        }
    }

    public String changeStringToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        Calendar transactionTicketDate = Calendar.getInstance();
        try {
            transactionTicketDate.setTime(format.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        transactionTicketDate.add(Calendar.DAY_OF_MONTH, 30);
        String newDate = format.format(transactionTicketDate.getTime());
        return  newDate;
    }





}
