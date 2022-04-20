package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * An class that represents a ClientUser
 */
public class ClientUser extends User {

    private ItemList wishList;
    private ItemList inventory;
    private ItemList pendingItemList; //to be approved by admin
    private TransactionTicketList history;
    private TransactionTicketList pendingTransaction;
    private AppointmentList confirmedAppointments;
    private AppointmentList pendingAppointments;
    private String accountStatus = "active"; //we have active, pending (to be frozen by admin), and frozen;
    private int numBurrowed = 0;             //the fourth status could be invisible (phase 2).
    private int numLent = 0;
    private int currentIncompleteTransactionCount; // current count
    private int currentTransactionCount; //current count
    private int currentLentBorrowDifference; //current count
    private double accountBalance = 0;
    private String home;
    private ItemList lastTradedItems;
    private Item lastTradedItem;

    /**
     *  Username, password and their home are required to create a client user
     * @param userName
     * @param password
     * @param home
     */
    public ClientUser(String userName, String password, String home) {
        super(userName, password);
        this.home = home;
        wishList = new ItemList();
        inventory = new ItemList();
        pendingItemList = new ItemList();
        lastTradedItems = new ItemList();
        history = new TransactionTicketList(new ArrayList<>());
        pendingTransaction = new TransactionTicketList(new ArrayList<>());
        confirmedAppointments = new AppointmentList(new ArrayList<>());
        pendingAppointments = new AppointmentList(new ArrayList<>());
        this.currentIncompleteTransactionCount =0;
        this.currentTransactionCount =0;
        this.currentLentBorrowDifference = numLent - numBurrowed;
    }


    public void setInventory(ItemList inventory){
        this.inventory = inventory;
    }

    /**
     * gets the user's wishlist
     * @return
     */
    public ItemList getWishList() {
        return wishList;
    }

    /**
     * gets the user's inventory
     * @return
     */
    public ItemList getInventory() {
        return inventory;
    }


    /**
     * Gets the user's accountBalance
     * @return
     */
    public double getAccountBalance(){return accountBalance;}

    /**
     * Sets the user's accountBalance
     * @param newBalance
     */
    public void setAccountBalance(double newBalance){this.accountBalance = newBalance;}


    public int getNumBurrowed() { return numBurrowed; }

    public void setNumBurrowed(int num){
        this.numBurrowed = num;
    }
    public int getNumLent() { return numLent; }

    public void setNumLent(int num){
        this.numLent = num;
    }
    /**
     * Get user's confirmedAppointments list
     * @return
     */
    public AppointmentList getConfirmedAppointments() {
        return confirmedAppointments;
    }

    /**
     * Get the user's pendingItemList
     * @return
     */
    public ItemList getPendingItemList() {
        return pendingItemList;
    }

    /**
     * Get the user's history
     * @return
     */
    public TransactionTicketList getHistory() {
        return history;
    }

    /**
     * get the user's pendingTransaction list
     * @return
     */
    public TransactionTicketList getPendingTransaction() {
        return pendingTransaction;
    }

    /**
     * get user's accountStatus
     * @return
     */
    public String getAccountStatus() {
        return accountStatus;
    }

    /**
     * Get the user's home location
     * @return
     */
    public String getHome() {
        return home;
    }

    /**
     * Creates a new client user snapshot
     * @return
     */
    public ClientUserSnapshot createSnapshot() {
        return new ClientUserSnapshot(this, home, accountStatus);
    }

    /**
     * Gets the sate of the snapshot and sets home and accountStatus
     * @param clientUserSnapshot
     */
    public void getStateFromSnapshot(ClientUserSnapshot clientUserSnapshot) {
        home = clientUserSnapshot.getHome();
        accountStatus = clientUserSnapshot.getAccStatus();
    }

    /**
     * Gets the user's pending appointments
     * @return
     */
    public AppointmentList getPendingAppointments() {
        return pendingAppointments;
    }


    public void incrementCurrentIncompleteCount(){this.currentIncompleteTransactionCount += 1;}

    /**
     * Get the user's current incomplete transation count
     * @return
     */
    public int getCurrentIncompleteTransactionCount(){
        return currentIncompleteTransactionCount;
    }

    public void setCurrentIncompleteTransactionCount(int count){
        this.currentIncompleteTransactionCount = count;
    }


    public int getCurrentTransactionCount() {
        return currentTransactionCount;
    }

    public void setCurrentTransactionCount(int count) {
        this.currentTransactionCount = count;
    }

    /**
     * gets the current lent borrow difference value for the user
     * @return
     */
    public int getCurrentLentBorrowDifference(){
        return currentLentBorrowDifference;
    }



    /**
     * Sets the account status
     * @param status
     */
    public void setAccountStatus(String status) { this.accountStatus = status; }

    /**
     * Sets the user's home location
     * @param home
     */
    public void setHome(String home) {

        this.home = home;

    }

    public boolean isAdminUser() {
        return false;
    }

    /**
     * add transaction ticket to history
     * @param transactionTicket
     */
    public void addToHistory(TransactionTicket transactionTicket) {
        history.addToTransactionTicketList(transactionTicket);
    }

    /**
     * Adds transaction ticket to pending
     * @param transactionTicket
     */
    public void addToPendingTransactionTicket(TransactionTicket transactionTicket) {
        pendingTransaction.addToTransactionTicketList(transactionTicket);}

    /**
     * add item to user's wishlist
     * @param item
     */
    public void addToWishList(Item item) {
        wishList.addToItemListNoDuplicates(item);
    }

    /**
     * add item to user's inventory.
     * @param item
     */
    public void addToInventory(Item item) {
        inventory.addToItemList(item);
    }
    /**
     * remove item from user's inventory
     * @param item
     */
    public void removeFromInventory(Item item){inventory.removeFromItemList(item);}

    /**
     * remove item from user's wishlist
     * @param item
     */
    public void removeFromWishList(Item item){wishList.removeFromItemList(item);}


    /**
     * returns the item list of the last traded items
     * @return
     */
    public ItemList getLastTradedItemList() {
        List<TransactionTicket> historyList = history.getTransactionTicketList();
        if (historyList.isEmpty() || historyList.size()<3){
            return null;
        }
        else{
            for (int i = 0; i < 3; i++){
                Item item = historyList.get(i).getItem1();
                lastTradedItems.addToItemList(item);
            }
        }
        return null;
    }

    /**
     * Returns the last traded item
     * @return
     */
    public Item getLastTradedItem() {
        List<TransactionTicket> historyList = history.getTransactionTicketList();
        this.lastTradedItem = historyList.get(0).getItem1();
        return lastTradedItem;
    }


    public void setLastTradedItem(ItemList lastTradedItem) {
        this.lastTradedItems = lastTradedItem;
    }

    /**
     * adds item to user's pending item list
     * @param item
     */
    public void addToPendingItem(Item item) {pendingItemList.addToItemList(item);}

    /**
     * add appointment to user's pending appointment list
     * @param appointment
     */
    public void addToPendingAppointment(Appointment appointment) {pendingAppointments.addToAppointment(appointment);}

    /**
     * adds appointment to user's confirmed appointment list
     * @param appointment
     */
    public void addToConfirmedAppointment(Appointment appointment) {confirmedAppointments.addToAppointment(appointment);}

    /**
     * returns the user in string form
     * @return
     */
    @Override // We can add more to this if we want to distinguish more. (GOES USERNAME, PASSWORD, NONOBJECT VARIABLE IN THE ORDER OF ABOVE)
    public String toString() {
        StringBuilder userString = new StringBuilder();
        userString.append("===================================" + "\n");
        userString.append("Username: " + this.getUserName() + "\n");
        userString.append("Password: " + this.getPassword() + "\n");
        userString.append("Home location: " + home + "\n");
        userString.append("Account Status: " + accountStatus + "\n");
        userString.append("Number Of Item(s) Burrowed: " + numBurrowed + "\n");
        userString.append("Number Of Item(s) Lent: " + numLent + "\n");
        userString.append("Current Incomplete Transactions Counts: " + currentIncompleteTransactionCount + "\n");
        userString.append("Current Lent Borrow Difference Counts: " + currentLentBorrowDifference + "\n");
        userString.append("Account Balance: " + accountBalance + "\n");
        userString.append("===================================" + "\n");
        return userString.toString();
    }

}
