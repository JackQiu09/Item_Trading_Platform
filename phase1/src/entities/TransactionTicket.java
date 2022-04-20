package entities;

import java.io.Serializable;
import java.util.UUID;

public class TransactionTicket implements Serializable {

    private String time;
    private String appointmentId;
    private Item item1;
    private Item item2;
    private boolean oneWay;
    private String proposer;
    private String receiver;
    private boolean isUser1Confirmed = false; //physically present at scene
    private boolean isUser2Confirmed = false;
    private boolean isCompleted = false;
    private String ticketID;

    // two item(two way)
    public TransactionTicket(Item item1, Item item2, String time, String user1, String user2, String apptId, boolean oneWay) {
        this.time = time;
        this.proposer = user1;
        this.receiver = user2;
        this.appointmentId = apptId;
        this.item1 = item1;
        this.item2 = item2;
        this.oneWay = oneWay;
        this.ticketID = UUID.randomUUID().toString();

    }

    //one items(one way)
    public TransactionTicket(Item item1, String time, String user1, String user2, String apptId,boolean oneWay) {
        this.time = time;
        this.proposer = user1;
        this.receiver = user2;
        this.appointmentId = apptId;
        this.item1 = item1;
        this.oneWay = oneWay;
    }


    public String getTime(){
        return time;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public boolean isOneWay() {return oneWay;}

    public String getProposer() {
        return proposer;
    }

    public String getTicketID(){
        return ticketID;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean getIsUser1Confirmed(){
        return isUser1Confirmed;
    }

    public boolean getIsUser2Confirmed(){
        return isUser2Confirmed;
    }

    public boolean confirm(String username, boolean isUserConfirmed) {
        if (username.equals(proposer) && isUserConfirmed) {
            this.isUser1Confirmed = true;
            return true;
        }
        else if (username.equals(receiver) && isUserConfirmed) {
            this.isUser2Confirmed = true;
            return true;
        }
        else return false;
    }

    public void setIsCompleted(boolean new_isCompleted){
        this.isCompleted = new_isCompleted;
    }

    public boolean getIsCompleted(){
        return this.isCompleted;
    }

    @Override
    public String toString() {
        StringBuilder transactionTicketString = new StringBuilder();
        if (oneWay) {
            transactionTicketString.append("===================================" + "\n");
            transactionTicketString.append("Appointment ID: " + appointmentId + "\n");
            transactionTicketString.append("Item: " + item1.getItemName() + "\n");
            transactionTicketString.append("Time: " + time + "\n");
            transactionTicketString.append("Proposer: " + proposer + "\n");
            transactionTicketString.append("Receiver: " + receiver + "\n");
            transactionTicketString.append("Has Proposer Confirmed?: " + isUser1Confirmed + "\n");
            transactionTicketString.append("Has Receiver Confirmed?: " + isUser2Confirmed + "\n");
            transactionTicketString.append("Has the Transaction Been Completed?: " + isCompleted + "\n");
            transactionTicketString.append("===================================" + "\n");
            return transactionTicketString.toString();
            /*return time + item1 + "," + appointmentId + "," + proposer + "," + receiver + "," + isUser1Confirmed + "," + isUser2Confirmed
                    + "," + isCompleted;*/
        }
        else {
            transactionTicketString.append("===================================" + "\n");
            transactionTicketString.append("Appointment ID: " + appointmentId + "\n");
            transactionTicketString.append("Item One: " + item1.getItemName() + "\n");
            transactionTicketString.append("Item Two: " + item1.getItemName() + "\n");
            transactionTicketString.append("Time: " + time + "\n");
            transactionTicketString.append("Proposer: " + proposer + "\n");
            transactionTicketString.append("Receiver: " + receiver + "\n");
            transactionTicketString.append("Has Proposer Confirmed?: " + isUser1Confirmed + "\n");
            transactionTicketString.append("Has Receiver Confirmed?: " + isUser2Confirmed + "\n");
            transactionTicketString.append("Has the Transaction Been Completed?: " + isCompleted + "\n");
            transactionTicketString.append("===================================" + "\n");
            return transactionTicketString.toString();
            /*return time + item1 + item2 + "," + appointmentId + "," + proposer + "," + receiver + "," + isUser1Confirmed + "," + isUser2Confirmed
                    + "," + isCompleted;*/
        }
    }
}