package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionTicketList implements Serializable {
    private List<TransactionTicket> transactionTicketList;
    private String id;

    public TransactionTicketList(){
        this.transactionTicketList = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public TransactionTicketList(List<TransactionTicket> transactionTickets){
        this.transactionTicketList = transactionTickets;
        id = UUID.randomUUID().toString();
    }


    public List<TransactionTicket> getTransactionTicketList(){
        return transactionTicketList;
    }

    public String getId(){
        return id;
    }

    public void addToTransactionTicketList(TransactionTicket transactionTicket){
        transactionTicketList.add(transactionTicket);
    }

    public void removeTransactionTicketList(TransactionTicket transactionTicket){
        transactionTicketList.remove(transactionTicket);
    }

    public String toString() {
        StringBuilder ticketListString = new StringBuilder();
        if (!transactionTicketList.isEmpty()) {
            for (TransactionTicket transactionTicket : transactionTicketList) {
                ticketListString.append(transactionTicket.toString());
                ticketListString.append("\n");
                return transactionTicketList.toString();
            }
        }
        return "This list is empty";
    }



}
