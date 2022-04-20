package controller;

import entities.*;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*;

public class TradingSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private TradeManager tradeManager;
    private ItemListManager itemListManager;
    private ClientUserReadWrite curw = new ClientUserReadWrite();
    private AppointmentSystem appointmentSystem;
    private TransactionTicketManager transactionTicketManager;
    private ThresholdManager thresholdManager;
    private ClientUserList clientUserList;

    public TradingSystem(ClientUserManager clientUserManager) throws ClassNotFoundException, IOException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.tradeManager  = new TradeManager(clientUserManager);
        this.appointmentSystem = new AppointmentSystem(clientUserManager);
        this.transactionTicketManager = new TransactionTicketManager();
        this.thresholdManager =  clientUserManager.thresholdManager;
        this.clientUserList = new ClientUserList();
    }



    public void run() throws IOException, ClassNotFoundException {
        System.out.println("Welcome to the Trading System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(TRADE_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        switch (inputArray.get(0)) {
            // Add item to wishlist
            case "1":
                itemListManager.showAllUserInventories();
                addItemToWishlist();
                break;
            // Request to trade an item
            case "2":
                if (transactionTicketManager.checkUserTransactionLimit(clientUserManager, thresholdManager.getUserThreshold("user","transactionLimit"))) {
                    ArrayList<String> transactionType = PromptPresenter.takeInput(TRANSACTION_TYPE_PROMPT);
                    if (transactionType.get(0).equals("1")) {
                        itemListManager.showAllUserInventories();
                        requestToTrade(true);
                    }
                    else if (transactionType.get(0).equals("2")) {
                        itemListManager.showAllUserInventories();
                        requestToTrade(false);
                    }


                }
                else {
                    System.out.println("You have completed too many transactions currently. Please wait your given period of time until you can do so again.");
                }
                break;
            // Request to borrow an item
            case "3":

                if (transactionTicketManager.checkUserTransactionLimit(clientUserManager, thresholdManager.getUserThreshold("user","transactionLimit"))) {
                    ArrayList<String> transactionType = PromptPresenter.takeInput(TRANSACTION_TYPE_PROMPT);
                    if (transactionType.get(0).equals("1")) {
                        itemListManager.showAllUserInventories();
                        suggestToLend(true);
                    }
                    else if (transactionType.get(0).equals("2")) {
                        itemListManager.showAllUserInventories();
                        suggestToLend(false);
                    }
                }
                else {
                    System.out.println("You have completed too many transactions currently. Please wait your given period of time until you can do so again.");
                }
                run();
                break;
            // Lend your items
            case "4":
                if (transactionTicketManager.checkUserTransactionLimit(clientUserManager, thresholdManager.getUserThreshold("user","transactionLimit"))) {
                    itemListManager.showAllUserWishList();
                    lendYourItems(true);
                }
                else {
                    System.out.println("You have completed too many transactions currently. Please wait your given period of time until you can do so again.");
                }
                run();
                break;
            // Request to sell
            case "5":
                if (transactionTicketManager.checkUserTransactionLimit(clientUserManager, thresholdManager.getUserThreshold("user","transactionLimit"))) {
                    requestToBuy();
                }
                else {
                    System.out.println("You have completed too many transactions currently. Please wait your given period of time until you can do so again.");
                }
                run();
                break;
            case "6": // Request to sell
                if (transactionTicketManager.checkUserTransactionLimit(clientUserManager, thresholdManager.getUserThreshold("user","transactionLimit"))) {
                    requestToSell();
                }
                else {
                    System.out.println("You have completed too many transactions currently. Please wait your given period of time until you can do so again.");
                }
                run();
                break;
            // approve/edit/decline an appointment
            case "7":
                appointmentSystem.run();
            case "8":
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
                ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
                clientUserSystem.run();
                break;
        }
    }

    private void addItemToWishlist() throws IOException, ClassNotFoundException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("please type in the id of the item to add to wish list");
            String input = br.readLine();
            if(itemListManager.addItemToWishList(input)) {
                System.out.println("Item added to wishlist.");
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                run();
            }
            else {
                System.out.println("No ID matches your input, try again.");
                run();
            }

    }

    private void requestToTrade(Boolean isPermanent) throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
        System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
        System.out.println("Please type in the id of your item that you would like to trade:");
        String input = br.readLine();
        if (tradeManager.trade(input, inputArray.get(0), inputArray.get(1), inputArray.get(2), isPermanent)) {
            System.out.println("==== An appointment has been created ====");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            run();
        }
    }

    public void requestToBorrow(Boolean isPermanent) throws IOException, ClassNotFoundException {
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
        if (tradeManager.borrow(inputArray.get(0),inputArray.get(1),inputArray.get(2), isPermanent)) {
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            requestToBorrow(isPermanent);
        }
    }

    public void suggestToLend(Boolean isPermanent) throws IOException, ClassNotFoundException {
        ArrayList<String> confirmSuggest = PromptPresenter.takeInput(CONFIRM_SUGGEST_PROMPT);
        if (confirmSuggest.get(0).equals("1")) {
            itemListManager.showAllUserInventories();
            ArrayList<String> suggestInput = PromptPresenter.takeInputLineByLine(SUGGEST_LEND_PROMPT);
            Item borrowItem = itemListManager.findItemByItemId(suggestInput.get(0));
            ClientUser borrower = clientUserManager.getUserByUsername(borrowItem.getOwnerName());
            ItemList borrowerWishlist = borrower.getWishList();
            ItemList lenderInventory = clientUserManager.getCurrentUser().getInventory();
            if (printSuggestionBasedOffWishlist(borrowerWishlist,lenderInventory) != null) {
                ArrayList<String> confirmLendInput = PromptPresenter.takeInput(CONFIRM_LEND_PROMPT);
                if(confirmLendInput.get(0).equals("1")) {
                    ArrayList<String> finishLendInput = PromptPresenter.takeInputLineByLine(FINISH_SUGGEST_LEND_PROMPT);
                    if (tradeManager.trade(printSuggestionBasedOffWishlist(borrowerWishlist,lenderInventory), suggestInput.get(0), finishLendInput.get(0), finishLendInput.get(1), isPermanent)) {
                        System.out.println("==== An appointment has been created ====");
                        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
                        run();
                    }
                }
                else if (confirmLendInput.get(0).equals("2")) {
                    itemListManager.showAllUserInventories();
                    requestToBorrow(isPermanent);
                }
            }
            else {
                System.out.println("No suggestion. Your wishlist is empty!");
            }
        }
        else if (confirmSuggest.get(0).equals("2")) {
            System.out.println("work????????");
            itemListManager.showAllUserInventories();
            requestToBorrow(isPermanent);
            run();
        }

    }

    public String printSuggestionBasedOffWishlist(ItemList listOne, ItemList listTwo) {
        for (Item itemOne: listOne.getItems()) {
            for (Item itemTwo: listTwo.getItems()) {
                if (itemOne.getItemId().equals(itemTwo.getItemId())) {
                    System.out.println("Here is your suggestion!");
                    System.out.println(itemTwo.toString());
                    return itemTwo.getItemId();
                }
            }
        }

        return null;
    }

    public void lendYourItems(Boolean isPermanent) throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(LEND_SET_UP_PROMPT);
        if (tradeManager.lend(inputArray.get(0),inputArray.get(1),inputArray.get(2),inputArray.get(3),isPermanent)) {
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            requestToBorrow(isPermanent);
        }
    }

    public void requestToBuy() throws IOException, ClassNotFoundException {
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(BUY_TRADE_SET_UP_PROMPT);
        if (itemListManager.findItemByItemId(inputArray.get(0)) != null) {
            if (Double.parseDouble(inputArray.get(1)) <= clientUserManager.getCurrentUser().getAccountBalance()) {
                Appointment appointment = tradeManager.createMoneyAppointment(itemListManager.findItemByItemId(inputArray.get(0)),
                        Double.parseDouble(inputArray.get(1)), clientUserManager.getCurrentUser(),itemListManager.findUserByItemId(inputArray.get(0)), inputArray.get(2),inputArray.get(3));
                clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
                itemListManager.findUserByItemId(inputArray.get(0)).addToPendingAppointment(appointment);
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
            } else {
                System.out.println("Your balance is less than the suggested price, try again.");
            }
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            requestToBuy();
        }
    }

    public void requestToSell() {

    }
}
