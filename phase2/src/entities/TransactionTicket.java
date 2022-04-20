package entities;

//import sun.tools.tree.InlineNewInstanceExpression;

import java.io.Serializable;
import java.util.UUID;

public class TransactionTicket implements Serializable {

    private String time;
    private String appointmentId;
    private Item item1;
    private Item item2;
    private String tradeType;
    private String proposer;
    private String receiver;
    private boolean isUser1Confirmed = false; //physically present at scene
    private boolean isUser2Confirmed = false;
    private boolean isCompleted = false;
    private String ticketID;
    private double price;
    private Boolean isPermanent;

    // two item(two way)
    public TransactionTicket(Item item1, Item item2, String time, String user1, String user2, String apptId, String tradeType, Boolean isPermanent) {
        this.time = time;
        this.proposer = user1;
        this.receiver = user2;
        this.appointmentId = apptId;
        this.item1 = item1;
        this.item2 = item2;
        this.tradeType = tradeType;
        this.ticketID = UUID.randomUUID().toString();
        this.isPermanent = isPermanent;
    }

    //one items(one way)
    public TransactionTicket(Item item1, String time, String user1, String user2, String apptId,String tradeType, Boolean isPermanent) {
        this.time = time;
        this.proposer = user1;
        this.receiver = user2;
        this.appointmentId = apptId;
        this.item1 = item1;
        this.tradeType = tradeType;
        this.ticketID = UUID.randomUUID().toString();
        this.isPermanent = isPermanent;
    }

    public TransactionTicket(Item item, String time, String user1, String user2, String apptId, String tradeType, double price) {
        this.time = time;
        this.proposer = user1;
        this.receiver = user2;
        this.appointmentId = apptId;
        this.item1 = item;
        this.tradeType = tradeType;
        this.price = price;
        this.ticketID = UUID.randomUUID().toString();

    }


    public String getTime(){
        return time;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getTradeType() {return tradeType;}

    public String getProposer() {
        return proposer;
    }

    public String getTicketID(){
        return ticketID;
    }

    public String getReceiver() {
        return receiver;
    }

    public Item getItem1(){ return item1;}

    public Item getItem2(){return item2;}

    public Boolean getIsPermanent() {
        return isPermanent;
    }

    public boolean getIsUser1Confirmed(){
        return isUser1Confirmed;
    }

    public boolean getIsUser2Confirmed(){
        return isUser2Confirmed;
    }

    public void setIsCompleted(boolean new_isCompleted){
        this.isCompleted = new_isCompleted;
    }
    public void setUser1Confirmed(boolean new_isCompleted){
        this.isUser1Confirmed = new_isCompleted;
    }

    public void setUser2Confirmed(boolean new_isCompleted) {
        this.isUser2Confirmed = new_isCompleted;
    }

    @Override
    public String toString() {
        StringBuilder transactionTicketString = new StringBuilder();
        if (!tradeType.equals("trade")) {
            transactionTicketString.append("===================================" + "\n");
            transactionTicketString.append("Transaction ID: " + ticketID + "\n");
            transactionTicketString.append("Item: " + item1.getItemName() + "\n");
            if (tradeType.equals("money")) {
                transactionTicketString.append("price: " + price + "\n");
            }
            transactionTicketString.append("Time: " + time + "\n");
            transactionTicketString.append("Proposer: " + proposer + "\n");
            transactionTicketString.append("Receiver: " + receiver + "\n");
            transactionTicketString.append("Has Proposer Confirmed?: " + isUser1Confirmed + "\n");
            transactionTicketString.append("Has Receiver Confirmed?: " + isUser2Confirmed + "\n");
            transactionTicketString.append("===================================" + "\n");
            return transactionTicketString.toString();
        }
        else{
            transactionTicketString.append("===================================" + "\n");
            transactionTicketString.append("Transaction ID: " + ticketID + "\n");
            transactionTicketString.append("Item One: " + item1.getItemName() + "\n");
            transactionTicketString.append("Item Two: " + item1.getItemName() + "\n");
            transactionTicketString.append("Time: " + time + "\n");
            transactionTicketString.append("Proposer: " + proposer + "\n");
            transactionTicketString.append("Receiver: " + receiver + "\n");
            transactionTicketString.append("Has Proposer Confirmed?: " + isUser1Confirmed + "\n");
            transactionTicketString.append("Has Receiver Confirmed?: " + isUser2Confirmed + "\n");
            transactionTicketString.append("===================================" + "\n");
            return transactionTicketString.toString();
        }
    }
}