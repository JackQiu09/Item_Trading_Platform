package entities;
import java.io.Serializable;
import java.util.UUID;

/**
 * A class that represents an appointment
 */
public class Appointment implements Serializable {
    private String appointmentId;
    // TODO: make it into a LocalDateTime
    private String time; //"year-month-day-time" ex."2020-07-09-13:00"
    private String address;
    private String proposer; //user1 username
    private String receiver; //user2 username
    private boolean isProposerConfirmed; //boolean value of whether the appointment is confirmed
    private boolean isReceiverConfirmed;
    private int user1EditsCount;
    private int user2EditsCount;
    private Item item1;
    private Item item2;
    private boolean oneWay;


    /**
     * user1Id, user2Id, time, address, appointmentId are required to create an instance of Entities.Appointment
     * @param user1Id
     * @param user2Id
     * @param time
     * @param address
     */

    //appointment for 2 items
    public Appointment(Item item1, Item item2, String user1Id, String user2Id, String time, String address){
        this.proposer = user1Id;
        this.receiver = user2Id;
        this.time = time;
        this.address = address;
        this.appointmentId = UUID.randomUUID().toString();
        this.user1EditsCount = 0;
        this.user2EditsCount = 0;
        this.isProposerConfirmed = false;
        this.isReceiverConfirmed = false;
        this.oneWay = false;
        this.item1 = item1;
        this.item2 = item2;
    }

    //appointment for 1 item
    public Appointment(Item item1, String user1Id, String user2Id, String time, String address){
        this.proposer = user1Id;
        this.receiver = user2Id;
        this.time = time;
        this.address = address;
        this.appointmentId = UUID.randomUUID().toString();
        this.user1EditsCount = 0;
        this.user2EditsCount = 0;
        this.isProposerConfirmed = false;
        this.isReceiverConfirmed = false;
        this.item1 = item1;
        this.oneWay = true;
    }

    // David's code, I need to set each variable when reading from CVS
    public Appointment(String appointmentId, String time, String address, String userOneName, String receiver, boolean isProposerConfirmed, boolean isReceiverConfirmed, int user1EditsCount, int user2EditsCount) {
        this.appointmentId = appointmentId;
        this.time = time;
        this.address = address;
        this.proposer = userOneName;
        this.receiver = receiver;
        this.isProposerConfirmed = isProposerConfirmed; //boolean value of whether the appointment is confirmed
        this.isReceiverConfirmed = isReceiverConfirmed;
        this.user1EditsCount = user1EditsCount;
        this.user2EditsCount = user2EditsCount;
    }

    /**
     * setting the time of the appointment
     * @param newTime
     */
    public void setTime(String newTime){
        time = newTime;
    }

    /**
     * setting the address of the location for the appointment to take place
     * @param newAddress
     */
    public void setAddress(String newAddress) {
        address = newAddress;
    }


    /**
     * return the time of the appointment
     * @return time
     */
    public String getTime(){
        return time;
    }

    /**
     * return the address of the location for the appointment to take place
     * @return
     */
    public String getAddress(){
        return address;
    }

    public boolean isOneWay() {return oneWay;}

    /**
     * return the confirmation status of the appointment
     * @return isConfirmed
     */
    public boolean getIsProposerConfirmed() {
        return isProposerConfirmed;
    }

    /**
     * the update status of the appointment
     * @return isUpdated
     */
    public boolean getIsReceiverConfirmed() {
        return isReceiverConfirmed;
    }

    /**
     * returns whether the appointment is confirmed.
     */
    public void setProposerConfirm(boolean value){
        isProposerConfirmed = value;
    }

    public void setReceiverConfirmed(boolean value) {isReceiverConfirmed = value;}

    public String getId() { return appointmentId;}

    public String getProposer() {
        return proposer;
    }

    public String getReceiver() {
        return receiver;
    }

    public void incrementUserEditsCount(String username){
        if (username.equals(proposer)){
            user1EditsCount+=1;
        }
        else if (username.equals(receiver)){
            user2EditsCount+=1;
        }

    }

    public Item getItem1(){
        return item1;
    }
    public Item getItem2(){
        return item2;
    }
    public void setItem1(Item newItem){
        this.item1 = newItem;
    }
    public void setItem2(Item newItem){
        this.item2 = newItem;
    }
    public int getUser1EditsCount() {
        return user1EditsCount;
    }

    public int getUser2EditsCount() {
        return user2EditsCount;
    }


    @Override
    public String toString() {
        StringBuilder appointmentString = new StringBuilder();
        if (oneWay) {
            appointmentString.append("===================================" + "\n");
            appointmentString.append("Appointment ID: " + appointmentId + "\n");
            appointmentString.append("Item: " + item1.getItemName() + "\n");
            appointmentString.append("Time: " + time + "\n");
            appointmentString.append("Address: " + address + "\n");
            appointmentString.append("Proposer: " + proposer + "\n");
            appointmentString.append("Receiver: " + receiver + "\n");
            appointmentString.append("Has Proposer Confirmed?: " + isProposerConfirmed + "\n");
            appointmentString.append("Has Receiver Confirmed?: " + isReceiverConfirmed  + "\n");
            appointmentString.append("Proposer Edit Count: " + user1EditsCount + "\n");
            appointmentString.append("Receiver Edit Count: " + user2EditsCount + "\n");
            appointmentString.append("===================================" + "\n");
            return appointmentString.toString();
            /*return appointmentId + "," + item1.getItemName()+  "," + time + "," + address + "," + proposer + "," +
                    receiver + "," + isProposerConfirmed + "," + "," + isReceiverConfirmed + "," + user1EditsCount + "," + user2EditsCount;*/
        }
        else {
            appointmentString.append("===================================" + "\n");
            appointmentString.append("Appointment ID: " + appointmentId + "\n");
            appointmentString.append("Item One: " + item1.getItemName() + "\n");
            appointmentString.append("Item Two: " + item2.getItemName() + "\n");
            appointmentString.append("Time: " + time + "\n");
            appointmentString.append("Address: " + address + "\n");
            appointmentString.append("Proposer: " + proposer + "\n");
            appointmentString.append("Receiver: " + receiver + "\n");
            appointmentString.append("Has Proposer Confirmed?: " + isProposerConfirmed + "\n");
            appointmentString.append("Has Receiver Confirmed?: " + isReceiverConfirmed  + "\n");
            appointmentString.append("Proposer Edit Count: " + user1EditsCount + "\n");
            appointmentString.append("Receiver Edit Count: " + user2EditsCount + "\n");
            appointmentString.append("===================================" + "\n");
            return appointmentString.toString();
            /*return appointmentId + "," + item1.getItemName() + "," + item2.getItemName() +  "," + time + "," + address
                    + "," + proposer + "," + receiver + "," + isProposerConfirmed + "," + "," + isReceiverConfirmed + "," + user1EditsCount + "," + user2EditsCount;*/
        }
    }

}
