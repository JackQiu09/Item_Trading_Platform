package entities;
import java.io.Serializable;
import java.util.Observable;
import java.util.UUID;

/**
 * A class that represents an appointment
 */
public class Appointment implements Serializable {
    private String appointmentId;
    private String time; //"year-month-day-time" ex."2020-07-09-13:00"
    private String address;
    private ClientUser proposer; //user1
    private ClientUser receiver; //user2
    private boolean isProposerConfirmed; //boolean value of whether the appointment is confirmed
    private boolean isReceiverConfirmed;
    private int user1EditsCount;
    private int user2EditsCount;
    private Item proposerItem;
    private Item receiverItem;
    private String tradeType;
    private double price;
    private Boolean isPermanent;


    /**
     * user1Id, user2Id, time, address, appointmentId are required to create an instance of Entities.Appointment
     * @param user1
     * @param user2
     * @param time
     * @param address
     */

    //appointment for 2 items
    public Appointment(Item proposerItem, Item receiverItem, ClientUser user1, ClientUser user2, String time, String address, Boolean isPermanent){
        this.proposer = user1;
        this.receiver = user2;
        this.time = time;
        this.address = address;
        this.appointmentId = UUID.randomUUID().toString();
        this.user1EditsCount = 0;
        this.user2EditsCount = 0;
        this.isProposerConfirmed = true;
        this.isReceiverConfirmed = false;
        this.tradeType = "trade";
        this.proposerItem = proposerItem;
        this.receiverItem = receiverItem;
        this.isPermanent = isPermanent;
    }

    /**
     * proposerItem, user1, user2, time, address, tradeType, isPermanent are needed to create an instance of appointment
     * @param proposerItem
     * @param user1
     * @param user2
     * @param time
     * @param address
     * @param tradeType
     * @param isPermanent
     */
    //appointment for 1 item
    public Appointment(Item proposerItem, ClientUser user1, ClientUser user2, String time, String address,
                       String tradeType, Boolean isPermanent){
        this.proposer = user1;
        this.receiver = user2;
        this.time = time;
        this.address = address;
        this.appointmentId = UUID.randomUUID().toString();
        this.user1EditsCount = 0;
        this.user2EditsCount = 0;
        this.isProposerConfirmed = true;
        this.isReceiverConfirmed = false;
        this.proposerItem = proposerItem;
        this.tradeType = tradeType;
    }


    /**
     * proposerItem, user1, price, user2, time, address, tradeType, isPermanent are needed to create an instance of appointment
     * @param item1
     * @param price
     * @param user1Id
     * @param user2Id
     * @param time
     * @param address
     */
    //appointment for buying item
    public Appointment(Item item1, double price, ClientUser user1Id, ClientUser user2Id, String time, String address){
        this.proposer = user1Id;
        this.receiver = user2Id;
        this.proposerItem = item1;
        this.time = time;
        this.address = address;
        this.appointmentId = UUID.randomUUID().toString();
        this.user1EditsCount = 0;
        this.user2EditsCount = 0;
        this.isProposerConfirmed = true;
        this.isReceiverConfirmed = false;
        this.price = price;
        this.tradeType = "money";
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
     * Sets a new price for this appointment
     * @param newPrice
     */
    public void setPrice(double newPrice)
    {
        if (tradeType.equals("money"))
        {
            this.price = newPrice;
        } else {
            System.out.println("This is not a money trade!");
        }
    }

    public double getPrice() {
        return price;
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

    public String getTradeType() {return tradeType;}

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

    /**
     * return if received confirmed the appointment
     * @param value
     */
    public void setReceiverConfirmed(boolean value) {isReceiverConfirmed = value;}

    /**
     * return appointment id
     * @return appointmentId
     */
    public String getId() { return appointmentId;}

    /**
     * return the proposer of the trade
     * @return proposer
     */
    public ClientUser getProposer() {
        return proposer;
    }

    /**
     * return the receiver of the trade
     * @return receiver
     */
    public ClientUser getReceiver() {
        return receiver;
    }

    /**
     * return if the trade is permenant
     * @return isPermanent
     */
    public boolean getIsPermanent() {return isPermanent;}

    /**
     * return proposer snapshot
     * @return AppointmentSnapshot
     */
    public AppointmentSnapshot createProposerSnapshot() {
        return new AppointmentSnapshot(this, proposer.getUserName(),time, address,user1EditsCount,user2EditsCount,
                isProposerConfirmed,isReceiverConfirmed);
    }

    /**
     * return proposer money snapshot
     * @return appointmentSnapShot
     */
    public AppointmentSnapshot createProposerMoneySnapshot() {
        return new AppointmentSnapshot(this,time, proposer.getUserName(),address,user1EditsCount,user2EditsCount,
                isProposerConfirmed,isReceiverConfirmed, price);
    }

    /**
     * return receiver snapshot
     * @return appointmentSnapshot
     */
    public AppointmentSnapshot createReceiverSnapshot() {
        return new AppointmentSnapshot(this, receiver.getUserName(),time, address,user1EditsCount,user2EditsCount,
                isProposerConfirmed,isReceiverConfirmed);
    }

    /**
     * return appointment snapshot
     * @return appointmentSnapshot
     */
    public AppointmentSnapshot createReceiverMoneySnapshot() {
        return new AppointmentSnapshot(this,time, receiver.getUserName(),address,user1EditsCount,user2EditsCount,
                isProposerConfirmed,isReceiverConfirmed, price);
    }

    /**
     * return get state from snapshot
     * @param appointmentSnapshot
     */
    public void getStateFromSnapshot(AppointmentSnapshot appointmentSnapshot) {
        time = appointmentSnapshot.getTime();
        address = appointmentSnapshot.getPlace();
        user1EditsCount = appointmentSnapshot.getUser1Edit();
        user2EditsCount = appointmentSnapshot.getUser2Edit();
        isReceiverConfirmed = appointmentSnapshot.getIsReceiverConfirmed();
        isProposerConfirmed = appointmentSnapshot.getIsProposerConfirmed();
    }

    /**
     * get state from money snapshot
     * @param appointmentSnapshot
     */
    public void getStateFromMoneySnapshot(AppointmentSnapshot appointmentSnapshot) {
        time = appointmentSnapshot.getTime();
        address = appointmentSnapshot.getPlace();
        user1EditsCount = appointmentSnapshot.getUser1Edit();
        user2EditsCount = appointmentSnapshot.getUser2Edit();
        isReceiverConfirmed = appointmentSnapshot.getIsReceiverConfirmed();
        isProposerConfirmed = appointmentSnapshot.getIsProposerConfirmed();
        price = appointmentSnapshot.getPrice();
    }

    /**
     * incrementing user's edit counts
     * @param username
     */
    public void incrementUserEditsCount(String username){
        if (username.equals(proposer.getUserName())){
            user1EditsCount+=1;
        }
        else if (username.equals(receiver.getUserName())){
            user2EditsCount+=1;
        }

    }


    /**
     * get proposer's item
     * @return proposerItem
     */
    public Item getProposerItem(){
        return proposerItem;
    }

    /**
     * get receiver's item
     * @return receiverItem
     */
    public Item getReceiverItem(){
        return receiverItem;
    }

    /**
     * get user1 edits count
     * @return user1EditsCount
     */
    public int getUser1EditsCount() {
        return user1EditsCount;
    }

    /**
     * get user 2 edits count
     * @return user2EditsCount
     */
    public int getUser2EditsCount() {
        return user2EditsCount;
    }

    /**
     * overrides to string
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder appointmentString = new StringBuilder();
        if (tradeType.equals("lend")) {
            appointmentString.append("===================================\n");
            appointmentString.append("Appointment ID: " + appointmentId + "\n");
            appointmentString.append("Lending Item: " + proposerItem.getItemName() + "\n");
            appointmentString.append("Time: " + time + "\n");
            appointmentString.append("Address: " + address + "\n");
            appointmentString.append("Proposer: " + proposer.getUserName() + "\n");
            appointmentString.append("Receiver: " + receiver.getUserName() + "\n");
            appointmentString.append("Has Proposer Confirmed?: " + isProposerConfirmed + "\n");
            appointmentString.append("Has Receiver Confirmed?: " + isReceiverConfirmed + "\n");
            appointmentString.append("Proposer Edit Count: " + user1EditsCount + "\n");
            appointmentString.append("Receiver Edit Count: " + user2EditsCount + "\n");
            appointmentString.append("IsPermanent?: " + isPermanent + "\n");
            appointmentString.append("===================================\n");
            return appointmentString.toString();
        } else if (tradeType.equals("burrow")) {
            appointmentString.append("===================================\n");
            appointmentString.append("Appointment ID: " + appointmentId + "\n");
            appointmentString.append("Burrowing Item: " + proposerItem.getItemName() + "\n");
            appointmentString.append("Time: " + time + "\n");
            appointmentString.append("Address: " + address + "\n");
            appointmentString.append("Proposer: " + proposer.getUserName() + "\n");
            appointmentString.append("Receiver: " + receiver.getUserName() + "\n");
            appointmentString.append("Has Proposer Confirmed?: " + isProposerConfirmed + "\n");
            appointmentString.append("Has Receiver Confirmed?: " + isReceiverConfirmed + "\n");
            appointmentString.append("Proposer Edit Count: " + user1EditsCount + "\n");
            appointmentString.append("Receiver Edit Count: " + user2EditsCount + "\n");
            appointmentString.append("IsPermanent?: " + isPermanent + "\n");
            appointmentString.append("===================================\n");
            return appointmentString.toString();
        } else if (tradeType.equals("trade")) {
            appointmentString.append("===================================\n");
            appointmentString.append("Appointment ID: " + appointmentId + "\n");
            appointmentString.append("Proposer's item: " + proposerItem.getItemName() + "\n");
            appointmentString.append("Receiver's item: " + receiverItem.getItemName() + "\n");
            appointmentString.append("Time: " + time + "\n");
            appointmentString.append("Address: " + address + "\n");
            appointmentString.append("Proposer: " + proposer.getUserName() + "\n");
            appointmentString.append("Receiver: " + receiver.getUserName() + "\n");
            appointmentString.append("Has Proposer Confirmed?: " + isProposerConfirmed + "\n");
            appointmentString.append("Has Receiver Confirmed?: " + isReceiverConfirmed + "\n");
            appointmentString.append("Proposer Edit Count: " + user1EditsCount + "\n");
            appointmentString.append("Receiver Edit Count: " + user2EditsCount + "\n");
            appointmentString.append("IsPermanent?: " + isPermanent + "\n");
            appointmentString.append("===================================\n");
            return appointmentString.toString();
        } else { // If the trade type is buying
            appointmentString.append("===================================\n");
            appointmentString.append("Appointment ID: " + appointmentId + "\n");
            appointmentString.append("Item: " + proposerItem.getItemName() + "\n");
            appointmentString.append("Price: " + this.price + "\n");
            appointmentString.append("Time: " + time + "\n");
            appointmentString.append("Address: " + address + "\n");
            appointmentString.append("Proposer: " + proposer.getUserName() + "\n");
            appointmentString.append("Receiver: " + receiver.getUserName() + "\n");
            appointmentString.append("Has Proposer Confirmed?: " + isProposerConfirmed + "\n");
            appointmentString.append("Has Receiver Confirmed?: " + isReceiverConfirmed + "\n");
            appointmentString.append("Proposer Edit Count: " + user1EditsCount + "\n");
            appointmentString.append("Receiver Edit Count: " + user2EditsCount + "\n");
            appointmentString.append("===================================\n");
            return appointmentString.toString();
        }
    }

}
