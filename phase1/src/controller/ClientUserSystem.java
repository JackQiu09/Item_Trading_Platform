package controller;

import entities.*;
import entities.Item;
import entities.ItemList;
import entities.TransactionTicket;
import entities.TransactionTicketList;					 
import gateway.AppointmentListReadWrite;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.AppointmentManager;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.ThresholdManager;
import usecases.TransactionTicketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static gateway.FileReadAndWrite.*;

public class ClientUserSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private ItemListManager itemListManager;
    private ClientUserReadWrite curw = new ClientUserReadWrite();
    private TransactionTicketManager transactionTicketManager;
    private ThresholdManager thresholdManager;

    public ClientUserSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.transactionTicketManager = new TransactionTicketManager();
        this.thresholdManager = new ThresholdManager(clientUserManager);

    }

    public void run() throws ClassNotFoundException, IOException {
//        inventoryListManager = new ItemListManager(FileReadAndWrite.readFromFile(INVENTORY_ITEMS_FILE));
//        clientUserManager.getCurrentUser().setInventory(inventoryListManager.getItemListByUser(clientUserManager.getCurrentUser().getUserName()));
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the Client System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(CLIENT_USER_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws ClassNotFoundException, IOException {
        if (inputArray.get(0).equals("1")) {
            trading();
        } //Browse Pending Transactions
        else if (inputArray.get(0).equals("2")) {
            System.out.println(clientUserManager.getCurrentUser().getPendingTransaction().toString());
            transactionTicketManager.checkUserTransactionLimit(clientUserManager,clientUserManager.getCurrentUser().getTransactionLimit());
            run();
        } //View Recently Traded Items
        else if (inputArray.get(0).equals("3")) {
            run();
        }
        else if (inputArray.get(0).equals("4")) {
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
        }
        else if (inputArray.get(0).equals("7")) {
            System.out.println(clientUserManager.getCurrentUser().getWishList().toString());
            System.out.println("USER: " + clientUserManager.getCurrentUser());
            run();
        } // view lending list
        else if (inputArray.get(0).equals("8")) {
            System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
            run();
        }

        else if (inputArray.get(0).equals("9")) {
            ArrayList<String> input = PromptPresenter.takeInputLineByLine(REQUEST_TO_ADD_ITEM_PROMPT);
            itemListManager.createAnItem(input.get(0),input.get(1),input.get(2));
            System.out.println("Please wait while an admin approves of your added item!");
            run();
        }

        else if (inputArray.get(0).equals("10")) {
            run();

        }

        else if (inputArray.get(0).equals("11")) {
            System.out.println(thresholdManager.displayUserThresholdByCurrentUser());
            //System.out.println(ThresholdManager.getAllUserThresholds(clientUserManager.getCurrentUser().getUserName()));
            run();
        }

        else if (inputArray.get(0).equals("12")){
            //confirm Transactions
            ArrayList<String> confirmTransactionMenuInput = PromptPresenter.takeInput(CONFIRM_TRANSACTION_OPTIONS_PROMPT);
            if (confirmTransactionMenuInput.get(0).equals("1")){// view all pending transactions
                System.out.println(transactionTicketManager.getPendingTransactionTicketsID(clientUserManager.getCurrentUser().getUserName()));
            }
            else if (confirmTransactionMenuInput.get(0).equals("2")) { //confirm a transaction
                ArrayList<String> input12 = PromptPresenter.takeInputLineByLine(CONFIRM_TRANSACTION_PROMPT);
                TransactionTicket ticket = transactionTicketManager.getTransactionTicketByTicketID(input12.get(0));
                boolean boolValue = Boolean.parseBoolean(input12.get(2));
                ticket.confirm(clientUserManager.getCurrentUser().getUserName(), boolValue);
                if (!boolValue){ //if this user didn't show up
                    clientUserManager.getCurrentUser().incrementIncompleteLimit();
                }
            }
            run();

        }

        else if (inputArray.get(0).equals("13")) {
            curw.saveToFile(CLIENT_USER_FILE,clientUserManager);
            StartMenuSystem startMenuSystem = new StartMenuSystem();
            startMenuSystem.run();
        }
    }

    private void trading() throws IOException, ClassNotFoundException {
        TradingSystem tradingSystem = new TradingSystem(clientUserManager);
        tradingSystem.run();
    }

    public List<TransactionTicket> getPendingTransactions(){
        return clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList();
    }
    
    public Item getRecentlyTradedItem() {
    	return clientUserManager.getCurrentUser().getLastTradedItem();
    }
    
    public ClientUser getMostFrequentTradedPartner() {
    	
    	int max = 0;
        int curr = 0;
        ClientUser currKey =  null;
        Set<ClientUser> unique = new HashSet<ClientUser>(clientUserManager.getCurrentUser().getTradedPartners());

            for (ClientUser key : unique) {
                  curr = Collections.frequency(clientUserManager.getCurrentUser().getTradedPartners(), key);

                 if(max < curr){
                   max = curr;
                   currKey = key;
                  }
              }

            return  currKey;
    	
    }

    public void getMostFrequentTradingPartners() {
        HashMap<String, Integer> tradingPartners = new HashMap<String, Integer>();
        ClientUser currentUser = clientUserManager.getCurrentUser();
        int numberOfTradingPartners = 3;
        if (currentUser.getHistory().getTransactionTicketList().size() == 0) {
            System.out.println("No completed trades have been made, so i can't display most frequent trading partners!");
        }
        else if (currentUser.getHistory().getTransactionTicketList().size() < numberOfTradingPartners) {
            numberOfTradingPartners = currentUser.getHistory().getTransactionTicketList().size();
        }


        for (TransactionTicket ticket: currentUser.getHistory().getTransactionTicketList()) {
            if (ticket.getProposer().equals(currentUser.getUserName())) {
               if (tradingPartners.containsKey(ticket.getReceiver())) {
                   tradingPartners.put(ticket.getReceiver(), tradingPartners.get(ticket.getReceiver()) + 1);
               }
                tradingPartners.put(ticket.getReceiver(), 1);
            }
            else {
                if (tradingPartners.containsKey(ticket.getReceiver())) {
                    tradingPartners.put(ticket.getReceiver(), tradingPartners.get(ticket.getReceiver()) + 1);
                }
                tradingPartners.put(ticket.getProposer(), 1);
            }
        }

        Map.Entry<String, Integer> maxEntry = null;
        for (int i = 0; i < numberOfTradingPartners; i++) {
            for (Map.Entry<String, Integer> entry : tradingPartners.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                    System.out.println(clientUserManager.getUserByUsername(entry.getKey()).toString());
                    tradingPartners.remove(entry.getKey());
                }
            }
        }
    }
}
