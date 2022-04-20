package controller;

import entities.*;
import gateway.ClientUserReadWrite;
import gateway.ClientUserSnapshotReadWrite;
import presenter.PromptPresenter;
import usecases.*;

import java.io.IOException;
import java.util.*;

import static gateway.FileReadAndWrite.*;

public class ClientUserSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private ItemListManager itemListManager;
    private TransactionTicketManager transactionTicketManager;
    private ThresholdManager thresholdManager;
    private ClientUserList clientUserList;
    private TransactionTicketSystem transactionTicketSystem;
    private ClientUserSnapshotReadWrite csrw= new ClientUserSnapshotReadWrite();
    private ClientUserSnapshotManager csm = csrw.createClientUserSnapshotManagerFromFile(CLIENT_USER_SNAPSHOT);

    public ClientUserSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.transactionTicketManager = new TransactionTicketManager(clientUserManager);
        this.thresholdManager = clientUserManager.thresholdManager;
        this.transactionTicketSystem = new TransactionTicketSystem(clientUserManager);

    }

    public void run() throws ClassNotFoundException, IOException {
        System.out.println("Welcome to the Client System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(CLIENT_USER_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws ClassNotFoundException, IOException {
        // trade
        if (inputArray.get(0).equals("1")) {
            trading();
        } //Browse Pending Transactions
        else if (inputArray.get(0).equals("2")) {
            System.out.println(clientUserManager.getCurrentUser().getPendingTransaction().toString());
            run();
        }
        else if (inputArray.get(0).equals("3")) { //View Recently Traded Items
            System.out.println(getThreeRecentlyTradedItems().toString());
            run();
        }
        else if (inputArray.get(0).equals("4")) { //view most frequent trading partners
            getMostFrequentTradingPartners();
            run();
        } //Browse Pending Appointments
        else if (inputArray.get(0).equals("5")) {
            System.out.println(clientUserManager.getCurrentUser().getPendingAppointments().toString());
            run();
        } //Browse Confirmed Appointments
        else if (inputArray.get(0).equals("6")) {
            System.out.println(clientUserManager.getCurrentUser().getConfirmedAppointments().toString());
            run();
        } // view wish list
        else if (inputArray.get(0).equals("7")) {
            System.out.println(clientUserManager.getCurrentUser().getWishList().toString());
            System.out.println("USER: " + clientUserManager.getCurrentUser());
            run();
        } // view lending list
        else if (inputArray.get(0).equals("8")) {
            System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
            run();
        }
        // request to add item
        else if (inputArray.get(0).equals("9")) {
            requestToAddItem();
        }
        // Request admin to unfreeze account
        else if (inputArray.get(0).equals("10")) {
            requestAdminToUnfreeze();
            run();
        }

        else if (inputArray.get(0).equals("11")) {
            // View thresholds
            viewThresholds();
        }

        else if (inputArray.get(0).equals("12")){
            //confirm Transactions
            transactionTicketSystem.run();
        }
        else if (inputArray.get(0).equals("13")) {
            //13. Send user to vacation
            sendToVacation();
        }

        else if (inputArray.get(0).equals("14")){
            // 14. Suggest Trade
            suggestTrade();
        }

        else if (inputArray.get(0).equals("15")) {
            // Change home location
            changeHomeLocation();
        }

        else if (inputArray.get(0).equals("16"))
        // Make a deposit
        {
            makeADeposit();
        }


        else if (inputArray.get(0).equals("17")) {
        // View account status
            viewAccountStatus();
        }

        else if (inputArray.get(0).equals("18")) {
            // View your completed transactions
            System.out.println(clientUserManager.getCurrentUser().getHistory().toString());
        }

        else if (inputArray.get(0).equals("19")) {
            // activate account from vacation
            activateFromVacation();
        }
        else if (inputArray.get(0).equals("20")) {
        // Log out
            logout();
        }

    }

    private void trading() throws IOException, ClassNotFoundException {
        if (clientUserManager.getCurrentUser().getAccountStatus().equalsIgnoreCase("active")) {
            checkUserIncompleteTransactionLimit();
            checkLentBorrowDifferenceLimit();
            TradingSystem tradingSystem = new TradingSystem(clientUserManager);
            tradingSystem.run();
        }
        else{
            System.out.println("You account cannot trade, please log in again");
        }
    }

    public void viewAccountStatus() throws IOException, ClassNotFoundException {
        System.out.println(clientUserManager.getCurrentUser().toString());
        run();
    }
    public void viewThresholds() throws IOException, ClassNotFoundException {
        System.out.println(thresholdManager.getAllUserThresholds(clientUserManager.getCurrentUser().getUserName()));
        run();
    }

    public void requestAdminToUnfreeze(){
        if (clientUserManager.getCurrentUser().getAccountStatus().equalsIgnoreCase("frozen")){
            clientUserManager.getClientUserList().addToPendingUnfreezeUser(clientUserManager.getCurrentUser());
            System.out.println("Please wait for the admins to approve your request");
        }
        else{
            System.out.println("Request Invalid because your account is not frozen or is in pending");
        }
    }

    public ItemList getThreeRecentlyTradedItems() { // three items
        return clientUserManager.getCurrentUser().getLastTradedItemList();
    }

    public Item getRecentlyTradedItem() { //one item
        return clientUserManager.getCurrentUser().getLastTradedItem();
    }

    public void checkUserIncompleteTransactionLimit() throws IOException, ClassNotFoundException {
        ClientUser currentUser = clientUserManager.getCurrentUser();
        int currentTransactionCount = currentUser.getCurrentIncompleteTransactionCount();
        if (currentTransactionCount >= thresholdManager.getUserThreshold(currentUser.getUserName(), "incompleteLimit")){
            //user exceeded Transaction Limit
            currentUser.setAccountStatus("pending");
            clientUserManager.getClientUserList().addToPendingUser(currentUser);
            clientUserManager.getClientUserList().removeFromActiveUser(currentUser);
            System.out.println("You have exceeded incompleteTransaction limits, please log in again");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            StartMenuSystem startMenuSystem = new StartMenuSystem();
            startMenuSystem.run();
        }
    }

    public void checkLentBorrowDifferenceLimit() throws IOException, ClassNotFoundException {
        ClientUser currentUser = clientUserManager.getCurrentUser();
        int currentLentBorrowDifferenceCount = currentUser.getCurrentLentBorrowDifference();
        if (currentLentBorrowDifferenceCount > thresholdManager.getUserThreshold(currentUser.getUserName(), "lentBorrowDiff")) {
            //user exceeded Transaction Limit
            currentUser.setAccountStatus("pending");
            clientUserManager.getClientUserList().addToPendingUser(currentUser);
            clientUserManager.getClientUserList().removeFromActiveUser(currentUser);
            System.out.println("You have exceeded lent borrow difference limits, please log in again");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
            StartMenuSystem startMenuSystem = new StartMenuSystem();
            startMenuSystem.run();
        }
    }


    public void getMostFrequentTradingPartners() {
        HashMap<String, Integer> tradingPartners = new HashMap<String, Integer>();
        ClientUser currentUser = clientUserManager.getCurrentUser();
        int numberOfTradingPartners = 3;
        if (currentUser.getHistory().getTransactionTicketList().size() == 0) {
            System.out.println("No completed trades have been made, so i can't display most frequent trading partners!");
        } else {

            for (TransactionTicket ticket : currentUser.getHistory().getTransactionTicketList()) {
                if (ticket.getProposer().equals(currentUser.getUserName())) {
                    if (tradingPartners.containsKey(ticket.getReceiver())) {
                        tradingPartners.put(ticket.getReceiver(), tradingPartners.get(ticket.getReceiver()) + 1);
                    } else {
                        tradingPartners.put(ticket.getReceiver(), 1);
                    }
                } else {
                    if (tradingPartners.containsKey(ticket.getReceiver())) {
                        tradingPartners.put(ticket.getReceiver(), tradingPartners.get(ticket.getReceiver()) + 1);
                    } else {
                        tradingPartners.put(ticket.getProposer(), 1);
                    }
                }
            }

            if (tradingPartners.size() < numberOfTradingPartners) {
                System.out.println("You don't have enough partners but I will display who you have!");
                numberOfTradingPartners = tradingPartners.size();
            }

            Map.Entry<String, Integer> maxEntry = null;

            for (int i = 0; i < numberOfTradingPartners; i++) {
                for (Map.Entry<String, Integer> entry : tradingPartners.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }

                System.out.println(clientUserManager.getUserByUsername(maxEntry.getKey()).toString());
                tradingPartners.remove(maxEntry.getKey());
                maxEntry = null;

            }
        }
    }

    public void requestToAddItem() throws IOException, ClassNotFoundException {
        ArrayList<String> input = PromptPresenter.takeInputLineByLine(REQUEST_TO_ADD_ITEM_PROMPT);
        itemListManager.createAnItem(input.get(0),input.get(1),input.get(2));
        System.out.println("Please wait while an admin approves of your added item!");
        run();
    }

    public void makeADeposit() throws IOException, ClassNotFoundException {
        ArrayList<String> input = PromptPresenter.takeInputLineByLine(DEPOSIT_PROMPT);
        ClientUser curr = clientUserManager.getCurrentUser();
        double depositAmount = Double.parseDouble(input.get(0));
        clientUserManager.requestDeposit(curr, depositAmount);
        System.out.println("Your deposit request for " + input.get(0) + " has been sent to an admin.");
        run();
    }

    public void suggestTrade() throws IOException, ClassNotFoundException {
        System.out.println("Suggesting Most Reasonable Trades");
        if (getThreeRecentlyTradedItems() == null){
            itemListManager.showAllUserInventories();
        }
        else{
            Item recentItem = getRecentlyTradedItem();
            String recentType = recentItem.getType();
            ArrayList<Item> automatedList = itemListManager.getItemsByType(recentType);
            for (Item item: automatedList) {
                System.out.println(item.toString());
            }
        }
        run();
    }

    public void sendToVacation() throws IOException, ClassNotFoundException {
        clientUserManager.getCurrentUser().setAccountStatus("invisible");
        clientUserManager.getClientUserList().addToInvisibleUser(clientUserManager.getCurrentUser());
        clientUserManager.getClientUserList().removeFromActiveUser(clientUserManager.getCurrentUser());
        System.out.println("Your account status is now been set to invisible, you will not be able to trade.");
        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        run();
    }

    public void activateFromVacation() throws IOException, ClassNotFoundException {
        clientUserManager.getCurrentUser().setAccountStatus("active");
        clientUserManager.getClientUserList().removeFromInvisibleUser(clientUserManager.getCurrentUser());
        clientUserManager.getClientUserList().addToActiveUser(clientUserManager.getCurrentUser());
        System.out.println("Your account status is now been set to active, you will be able to trade.");
        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        run();

    }

    public void changeHomeLocation() throws IOException, ClassNotFoundException {
        csm.add(clientUserManager.getCurrentUser().createSnapshot());
        ClientUserSnapshotReadWrite.saveToFile(CLIENT_USER_SNAPSHOT,csm);
        ArrayList<String> input = PromptPresenter.takeInputLineByLine(CHANGE_HOME_PROMPT);
        clientUserManager.getCurrentUser().setHome(input.get(0));
        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        run();
    }

    public void logout() throws IOException {
        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        StartMenuSystem startMenuSystem = new StartMenuSystem();
        startMenuSystem.run();
    }
}
