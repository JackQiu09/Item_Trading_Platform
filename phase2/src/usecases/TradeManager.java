package usecases;

import entities.Item;
import entities.Appointment;
import entities.ClientUser;


public class TradeManager {
    private ClientUserManager clientUserManager;
    private ItemListManager itemListManager;



    public TradeManager(ClientUserManager clientUserManager) {
          this.clientUserManager = clientUserManager;
          this.itemListManager = new ItemListManager(clientUserManager);
    }

    /*
    public boolean canBorrow(ClientUser clientUser) {
        if ((double) (clientUser.getNumLent() - clientUser.getNumBurrowed()) <
                clientUserManager.thresholdManager.getUserThreshold(clientUser.getUserName(), "lentBorrowDiff")) {
            return true;
        }
        return false;
    }

     */
    /**
     * Do the trading with provided items on specific time at provided address
     * @param item1
     * @param item2
     * @param time
     * @param address
     * @return Appointment of two items
     */

    public boolean trade(String lendItemId, String burrowItemId, String time, String address, Boolean isPermanent) {
        Item item1 = itemListManager.findItemByItemId(lendItemId);
        Item item2 = itemListManager.findItemByItemId(burrowItemId);
        System.out.println(item1.toString());
        System.out.println(item2.toString());
        if (item1 != null && item2 != null) {
            Appointment appointment = createAppointmentTwoItems(itemListManager.findItemByItemId(lendItemId),
                    itemListManager.findItemByItemId(burrowItemId),
                    clientUserManager.getCurrentUser(),
                    itemListManager.findUserByItemId(burrowItemId), time, address, isPermanent);
            clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
            itemListManager.findUserByItemId(burrowItemId).addToPendingAppointment(appointment);
            return true;
        }
        return false;
    }

    /**
     * Lend an item to provided user
     * @param item1
     * @param user2
     * @param time
     * @param address
     * @return appointment for the lending of item
     */

    public boolean lend(String lendItemId, String user2, String time, String address, boolean isPermanent){
        Item item1 = itemListManager.findItemByItemId(lendItemId);
        if (item1 != null) {
            Appointment appointment = createAppointmentLend(item1,clientUserManager.getCurrentUser(),
                    clientUserManager.getUserByUsername(user2),time,address, isPermanent);
            clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
            clientUserManager.getUserByUsername(user2).addToPendingAppointment(appointment);
            return true;
        }
        return false;
    }

    /**
     * borrows item from provided clientuser
     * @param item1
     * @param user2
     * @param time
     * @param address
     * @return appointment to borrow an item
     */

    public boolean borrow(String burrowItemId, String time, String address, boolean isPermanent){
        Item item1 = itemListManager.findItemByItemId(burrowItemId);
        if (item1 != null) {
            Appointment appointment = createAppointmentBurrow(itemListManager.findItemByItemId(burrowItemId),
                    clientUserManager.getCurrentUser(),
                    itemListManager.findUserByItemId(burrowItemId), time, address, isPermanent);
            clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
            itemListManager.findUserByItemId(burrowItemId).addToPendingAppointment(appointment);
            return true;
        }
        return false;
    }

    //creates instance of appointment with 2 items
    /**
     * creates instance of appointment with 2 items
     * @param item1
     * @param item2
     * @param userid1
     * @param userid2
     * @param time
     * @param address
     * @return Appointment for two items
     */
    private Appointment createAppointmentTwoItems(Item item1, Item item2, ClientUser proposer, ClientUser receiver,
                                                 String time, String address, Boolean isPermanent){
        return new Appointment(item1, item2, proposer, receiver, time, address, isPermanent);
    }

    //creates instance of appointment with 1 item
    /**
     * creates instance of appointment with 1 item
     * @param item1
     * @param userid1
     * @param userid2
     * @param time
     * @param address
     * @return Appointment for 1 item
     */
    private Appointment createAppointmentLend(Item item1, ClientUser proposer, ClientUser receiver, String time, String address, Boolean isPermanent){
        return new Appointment(item1, proposer, receiver, time, address,"lend", isPermanent);
    }

    private Appointment createAppointmentBurrow(Item item1, ClientUser proposer, ClientUser receiver, String time, String address, Boolean isPermanent){
        return new Appointment(item1, proposer, receiver, time, address,"burrow", isPermanent);
    }

    public Appointment createMoneyAppointment(Item item1, double price, ClientUser user1, ClientUser user2, String time, String address) {
        return new Appointment(item1, price, user1, user2, time, address);
    }
}

