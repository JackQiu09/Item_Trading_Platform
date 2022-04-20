package entities;

import java.util.ArrayList;
import java.util.List;


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
    private int incompleteLimit = 3;
    private int transactionLimit = 7;  // TODO: Move these into thresholdManager
    private double accountBalance = 0;
	private Item lastTradedItem=null;
    private List<ClientUser> tradedPartners;

    /*public ClientUser(String userName,
                      String password,
                      ItemList wishList,
                      ItemList inventory,
                      ItemList pendingItemList,
                      TransactionTicketList history,
                      TransactionTicketList pendingTransaction,
                      AppointmentList appointmentList,
                      String accountStatus,
                      int numBurrowed,
                      int numLent,
                      int incompleteLimit,
                      int transactionLimit) {
        super(userName, password);
        this.wishList = wishList;
        this.inventory = inventory;
        this.pendingItemList = pendingItemList;
        this.history = history;
        this.pendingTransaction = pendingTransaction;
        this.appointmentList = appointmentList;
        this.accountStatus = accountStatus;
        this.numBurrowed = numBurrowed;
        this.numLent = numLent;
        this.incompleteLimit = incompleteLimit;
        this.transactionLimit = transactionLimit;
    }

     */

    public ClientUser(String userName, String password) {
        super(userName, password);
        wishList = new ItemList();
        inventory = new ItemList();
        pendingItemList = new ItemList();
        history = new TransactionTicketList(new ArrayList<>());
        pendingTransaction = new TransactionTicketList(new ArrayList<>());
        confirmedAppointments = new AppointmentList(new ArrayList<>());
        pendingAppointments = new AppointmentList(new ArrayList<>());
		tradedPartners = new ArrayList<ClientUser>();											 
    }

    public void setInventory(ItemList inventory){
        this.inventory = inventory;
    }

    public double getAccountBalance(){return accountBalance;}

    public ItemList getWishList() {
        return wishList;
    }

    public ItemList getInventory() {
        return inventory;
    }

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public int getIncompleteLimit() { return incompleteLimit; }

    public int getNumBurrowed() { return numBurrowed; }

    public int getNumLent() { return numLent; }

    public AppointmentList getConfirmedAppointments() {
        return confirmedAppointments;
    }

    public ItemList getPendingItemList() {
        return pendingItemList;
    }

    public TransactionTicketList getHistory() {
        return history;
    }

    public TransactionTicketList getPendingTransaction() {
        return pendingTransaction;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public AppointmentList getPendingAppointments() {
        return pendingAppointments;
    }

    public void setIncompleteLimit(int num) {
        this.incompleteLimit = num;
    }

    public void incrementIncompleteLimit(){this.incompleteLimit += 1;}

    public void setTransactionLimit(int num) {
        this.transactionLimit = num;
    }

    public void setAccountStatus(String status) { accountStatus = status; }

    public boolean isAdminUser() {
        return false;
    }

    public void addToHistory(TransactionTicket transactionTicket) {
        history.addToTransactionTicketList(transactionTicket);
    }

    public void addToPendingTransactionTicket(TransactionTicket transactionTicket) {
        pendingTransaction.addToTransactionTicketList(transactionTicket);}

    public void addToWishList(Item item) {
        wishList.addToItemListNoDuplicates(item);
    }

    public void addToInventory(Item item) {
        inventory.addToItemList(item);
    }

    public void addToPendingItem(Item item) {pendingItemList.addToItemList(item);}
	public List<ClientUser> getTradedPartners() {
		return tradedPartners;
	}

	public void setTradedPartners(List<ClientUser> tradedPartners) {
		this.tradedPartners = tradedPartners;
	}

	public Item getLastTradedItem() {
		return lastTradedItem;
	}

	public void setLastTradedItem(Item lastTradedItem) {
		this.lastTradedItem = lastTradedItem;
	}


    public void addToPendingAppointment(Appointment appointment) {pendingAppointments.addToAppointment(appointment);}

    public void addToConfirmedAppointment(Appointment appointment) {confirmedAppointments.addToAppointment(appointment);}

    @Override // We can add more to this if we want to distinguish more. (GOES USERNAME, PASSWORD, NONOBJECT VARIABLE IN THE ORDER OF ABOVE)
    public String toString() {
        StringBuilder userString = new StringBuilder();
        userString.append("===================================" + "\n");
        userString.append("Username: " + this.getUserName() + "\n");
        userString.append("Password: " + this.getPassword() + "\n");
        userString.append("Account Status: " + accountStatus + "\n");
        userString.append("Number Of Item(s) Burrowed: " + numBurrowed + "\n");
        userString.append("Number Of Item(s) Lent: " + numLent + "\n");
        userString.append("Incomplete Transaction Limit: " + incompleteLimit + "\n");
        userString.append("Transaction Limit: " + transactionLimit + "\n");
        userString.append("===================================" + "\n");
        return userString.toString();

      /*  return "Username: " + this.getUserName() + ", Password: " + this.getPassword() + "," + accountStatus + "," + numBurrowed + "," +
                numLent + "," + incompleteLimit + "," + transactionLimit;*/
    }

}
