package controller;

import entities.Appointment;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.ClientUserManager;
import usecases.ItemListManager;
import usecases.TradeManager;
import usecases.TransactionTicketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*;

public class TradingSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private TradeManager tradeManager;
    private ItemListManager itemListManager ;
    private ClientUserReadWrite curw = new ClientUserReadWrite();
    private AppointmentSystem appointmentSystem;

    public TradingSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.tradeManager  = new TradeManager(clientUserManager);
        this.appointmentSystem = new AppointmentSystem(clientUserManager);
    }



    public void run() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the Trading System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(TRADE_SYSTEM_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        switch (inputArray.get(0)) {
            case "1":
                itemListManager.showAllUserInventories();
                addItemToWishlist();
                break;
            case "2":
                itemListManager.showAllUserInventories();
                requestToTrade();
                break;
            case "3":
                itemListManager.showAllUserInventories();
                requestToBurrow();
                break;

            case "4": // approve/edit/decline appointment
                appointmentSystem.run();
            case "5":
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
                ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
                clientUserSystem.run();
                break;
        }
    }

    private void addItemToWishlist() throws IOException, ClassNotFoundException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("please type in the id of the item to approve");
            String input = br.readLine();
            if(itemListManager.addItemToWishList(input)) {
                System.out.println("Item added to wishlist.");
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                run();
            }
            else {
                System.out.println("No ID matches your input, try again.");
                addItemToWishlist();
            }

    }

    private void requestToTrade() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
        System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
        System.out.println("Please type in your item id to complete the process.");
        String input = br.readLine();
        if (itemListManager.findItemByItemId(inputArray.get(0)) != null && itemListManager.findItemByItemId(input) != null) {
            Appointment appointment = tradeManager.trade(itemListManager.findItemByItemId(input),
                    itemListManager.findItemByItemId(inputArray.get(0)), inputArray.get(1),inputArray.get(2));
            clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
            itemListManager.findUserByItemId(inputArray.get(0)).addToPendingAppointment(appointment);
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            requestToTrade();
        }
    }

    public void requestToBurrow() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = PromptPresenter.takeInputLineByLine(TRADE_SET_UP_PROMPT);
        System.out.println(clientUserManager.getCurrentUser().getInventory().toString());
        System.out.println("Please type in the item id of the item you want to trade.");
        String input = br.readLine();
        if (itemListManager.findItemByItemId(inputArray.get(0)) != null && itemListManager.findItemByItemId(input) != null) {
            Appointment appointment = tradeManager.borrow(itemListManager.findItemByItemId(inputArray.get(0)),
                    itemListManager.findUserByItemId(inputArray.get(0)),  inputArray.get(1),inputArray.get(2));
            clientUserManager.getCurrentUser().addToPendingAppointment(appointment);
            itemListManager.findUserByItemId(inputArray.get(0)).addToPendingAppointment(appointment);
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            run();
        } else {
            System.out.println("Id does not match. Please try again.");
            requestToBurrow();
        }
    }

}
