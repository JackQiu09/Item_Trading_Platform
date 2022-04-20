package usecases;

import entities.TransactionTicket;
import entities.TransactionTicketList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionTicketManager {
    private TransactionTicketList historyTransactionTicketList;
    // TODO pendingTransactionTicketList to pendingTransactionTicketList
    private TransactionTicketList pendingTransactionTicketList;

    public TransactionTicketManager(TransactionTicketList historyTransactionTicketList, TransactionTicketList pendingTransactionTicketList) {
        this.historyTransactionTicketList = historyTransactionTicketList;
        this.pendingTransactionTicketList = pendingTransactionTicketList;
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

    // TODO Very similar you can prob modularise it
    public TransactionTicketList getHistoryTransactionTicketByUser(String username){
        TransactionTicketList historyTransactionTicketList = new TransactionTicketList();
        for(TransactionTicket ticket: this.historyTransactionTicketList.getTransactionTicketList()){
            if(ticket.getProposer().equalsIgnoreCase(username) || ticket.getReceiver().equalsIgnoreCase(username)){
                historyTransactionTicketList.getTransactionTicketList().add(ticket);
            }
        }
        return historyTransactionTicketList;
    }

    public TransactionTicketList getPendingTransactionTicketByUser(String username){
        TransactionTicketList pendingTicketList = new TransactionTicketList();
        for(TransactionTicket ticket: this.pendingTransactionTicketList.getTransactionTicketList()){
            if(ticket.getProposer().equalsIgnoreCase(username) || ticket.getReceiver().equalsIgnoreCase(username)){
                pendingTicketList.getTransactionTicketList().add(ticket);
            }
        }
        return pendingTicketList;
    }

    public List<String> getPendingTransactionTicketsID(String username){
        TransactionTicketList pendingTicketList = new TransactionTicketList();
        List<String> strList = new ArrayList<>();
        for(TransactionTicket ticket: this.pendingTransactionTicketList.getTransactionTicketList()){
            strList.add(ticket.getTicketID());
        }
        return strList;
    }

    public TransactionTicket getTransactionTicketByTicketID(String ticketID){
        TransactionTicketList ticketList = new TransactionTicketList();
        for(TransactionTicket ticket: ticketList.getTransactionTicketList()){
            if (ticket.getTicketID().equals(ticketID)){
                return ticket;
            }
        }
        return null; // no matched ticket
    }

    // means that both user1 and user2 had physically attended the meeting and completed the trade.
    public boolean completeTransaction(String ticketID){
        //TODO: finish this method
        TransactionTicket ticket = getTransactionTicketByTicketID(ticketID);
        boolean user1ConfirmStatus = ticket.getIsUser1Confirmed();
        boolean user2ConfirmStatus = ticket.getIsUser2Confirmed();
        //both users showed up-> transaction completed
        if (user1ConfirmStatus && user2ConfirmStatus){
            ticket.setIsCompleted(true);
            return true;
        }
        else{
            //transaction is not complete
            return false;
        }
    }

    public boolean checkUserTransactionLimit(ClientUserManager clientUserManager, int transactionLimit) {
        int transactionLimitCounter = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        Calendar transactionTicketDate = Calendar.getInstance();
        Calendar limitTransactionTicketDate = Calendar.getInstance();

        Calendar currentTime = Calendar.getInstance();
        System.out.println("Current Date: " + format.format(currentTime.getTime()));

        // CHANGE TO COMPLETED TRANSACTION
        for (TransactionTicket ticket: clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList()) {
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
        if (transactionLimitCounter >= transactionLimit) {
            return false;
        }
        else {
            return true;
        }
        //Date transactionDate;
    }

}
