package controller;

import entities.*;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.AdminUserManager;
import usecases.ClientUserManager;
import usecases.ThresholdManager;
import usecases.TransactionTicketManager;
import usecases.ItemListManager;
import gateway.AdminUserReadWrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.*;

import static gateway.FileReadAndWrite.*;

// TODO: put this into the UML
public class AdminSystem implements InputProcessable{
    ClientUserManager clientUserManager;
    TransactionTicketManager transactionTicketManager;
    ItemListManager itemListManager ;
    ThresholdManager thresholdManager;
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();

    public AdminSystem(ClientUserManager clientUserManager) {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.thresholdManager = new ThresholdManager(clientUserManager);
    }

    public void run() throws IOException, ClassNotFoundException {
        System.out.println("Welcome to the Admin System! At any time, 'exit' to quit the system.");
        processInput(PromptPresenter.takeInput(ADMIN_USER_SYSTEM_PROMPT));

    }

    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        try {
            switch (inputArray.get(0)) {
                case "1":
                    //show pending users
                    clientUserManager.showPendingUsers();
                    run();
                    break;
                case "2":
                    // show pending items
                    approvePendingItem();
                    run();
                    break;
                case "3":
                    //review pending transactions
                    ArrayList<String> input = PromptPresenter.takeInputLineByLine(GET_PENDING_TRANSACTION_PROMPT);
                    transactionTicketManager.getPendingTransactionTicketByUser(input.get(0));
                    break;
                case "4":  // Thresholds
//                    ThresholdManager thresholdManager = new ThresholdManager(clientUserManager);
                    ArrayList<String> thresholdMenuInput = PromptPresenter.takeInput(THRESHOLD_PROMPT);
                    if (thresholdMenuInput.get(0).equals("1")) {
                        //2. Change Specific User Threshold (String username, String choice, int changeValue)
                        ArrayList<String> input2 = PromptPresenter.takeInputLineByLine(CHANGE_USER_THRESHOLD_PROMPT);
                        int newValue = Integer.parseInt(input2.get(2));
                        thresholdManager.changeThresholdValue(input2.get(0), input2.get(1), newValue);
                    } else if (thresholdMenuInput.get(0).equals("2")) {
                        //5. Change Universal Threshold(String choice, int changeValue)
                        ArrayList<String> input5 = PromptPresenter.takeInputLineByLine(CHANGE_THRESHOLD_PROMPT);
                        int newValue = Integer.parseInt(input5.get(1));
                        thresholdManager.changeGlobalThreshold(input5.get(0), newValue);
                    }

                    else if (thresholdMenuInput.get(0).equals("9")) {
                        //9. logout
                        clientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                        StartMenuSystem startMenuSystem = new StartMenuSystem();
                        startMenuSystem.run();
                    }

                    run();
                    break;
                case "5":
                    clientUserManager.printAllUsers();
                    run();
                    break;
                case "6":
                    //6. Unfreeze User
                    ClientUserList clientUserList = clientUserManager.getClientUserList();
                    List<ClientUser> frozen_list = clientUserList.getFrozenUser();
                    for (ClientUser user: frozen_list){
                        System.out.println(user.getUserName());
                    }
                    ArrayList<String> input6 = PromptPresenter.takeInputLineByLine(UNFREEZE_PROMPT);
                    ClientUser toUnfreeze = clientUserManager.getUserByUsername(input6.get(0));
                    clientUserList.removeFromFrozenUser(toUnfreeze);
                    clientUserList.addToActiveUser(toUnfreeze);
                    run();
                case "7":
                    //7. Freeze User
                    ClientUserList clientUserList2 = clientUserManager.getClientUserList();
                    List<ClientUser> pending_list2 = clientUserList2.getPendingUser();
                    // Prints out a list of pending users.
                    for (ClientUser user: pending_list2) {
                        System.out.println(user.getUserName());
                    }
                    ArrayList<String> input7 = PromptPresenter.takeInputLineByLine(FREEZE_PROMPT);
                    ClientUser toFreeze = clientUserManager.getUserByUsername(input7.get(0));
                    clientUserList2.removeFromPendingUser(toFreeze);
                    clientUserList2.addToFrozenUser(toFreeze);
                    run();

                case "8":
                    //show admin users
                    AdminUserReadWrite adminUserReadWrite = new AdminUserReadWrite();
                    AdminUserManager adminUserManager = adminUserReadWrite.createClientUserManagerFromFile(ADMIN_USER_FILE);
                    adminUserManager.printAllAdminUsers();
                    run();
                    break;
                case "9":
                    System.out.println(thresholdManager.displayAllUserThresholds());
                    run();
                    break;
                case "11":
                    clientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                    StartMenuSystem startMenuSystem = new StartMenuSystem();
                    startMenuSystem.run();
                    break;
            }
        } catch (NullPointerException n) {
            System.out.println("It is empty");
            run();
        }
    }

    private void approvePendingItem() throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (itemListManager.showAllPendingItem()) {
            System.out.println("please type in the id of the item to approve");
            String input = br.readLine();
            if (itemListManager.approveItem(input)) {
                System.out.println("Item approved.");
                clientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                run();
            } else {
                System.out.println("somethign is wrong, please try again.");
                approvePendingItem();
            }
        } else {run();}
    }


}

