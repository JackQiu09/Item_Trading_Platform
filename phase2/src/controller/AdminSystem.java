package controller;

import entities.*;
import gateway.AppointmentSnapshotReadWrite;
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
    ArrayList<DepositRequest> depositRequests;
    SnapshotSystem snapshotSystem;

    public AdminSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.itemListManager = new ItemListManager(clientUserManager);
        this.thresholdManager = clientUserManager.thresholdManager;
        this.depositRequests = clientUserManager.getDepositRequests();
        this.snapshotSystem = new SnapshotSystem(clientUserManager);
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
                    viewPendingTransactions();
                    run();
                    break;
                case "4":  // Thresholds
                    changeThresholds();
                    run();
                    break;
                case "5": // view all client users
                    clientUserManager.printAllUsers();
                    run();
                    break;
                case "6":
                    //6. Unfreeze User (only users that have requested to unfreeze will show)
                    unfreezeUser();
                    run();
                    break;
                case "7":
                    //7. Freeze User
                    freezeUser();
                    run();
                    break;

                case "8":
                    //show admin users
                    showAdminUsers();
                    run();
                    break;
                case "9":
                    // Display all user thresholds
                    displayAllUserThresholds();
                    run();
                    break;
                case "10":
                    // Approve a deposit
                    clientUserManager.showDepositRequests();
                    ArrayList<String> input11 = PromptPresenter.takeInputLineByLine(DEPOSIT_REQUESTS_PROMPT);
                    try{
                        UUID id = UUID.fromString(input11.get(0));
                        clientUserManager.approveDeposit(id);
                        System.out.println("here");
                        run();
                        break;
                    } catch (IllegalArgumentException e)
                    {
                        System.out.println("incorrect UUID");
                        System.out.println("here");
                        run();
                        break;
                    }
                case "11": // undo action
                    snapshotSystem.run();
                case "13":
                    logout();
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
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
                run();
            } else {
                System.out.println("somethign is wrong, please try again.");
                approvePendingItem();
            }
        } else {run();}
    }

    public void unfreezeUser() throws IOException {
        ClientUserList clientUserList = clientUserManager.getClientUserList();
        clientUserManager.showPendingUnfreezeUsers();
        ArrayList<String> input6 = PromptPresenter.takeInputLineByLine(UNFREEZE_PROMPT);
        ClientUser toUnfreeze = clientUserManager.getUserByUsername(input6.get(0));
        if (toUnfreeze.getAccountStatus().equals("frozen")) {
            clientUserList.removeFromFrozenUser(toUnfreeze);
            clientUserList.removeFromPendingUnfreezeUser(toUnfreeze);
            clientUserList.addToActiveUser(toUnfreeze);
            clientUserManager.changeUserStatusTo(toUnfreeze, "active");
            System.out.println(toUnfreeze.getUserName()+" is now set to: " + clientUserManager.getCurrentUser().getAccountStatus());
            toUnfreeze.setCurrentIncompleteTransactionCount(0);
            toUnfreeze.setCurrentTransactionCount(0);
            toUnfreeze.setNumBurrowed(0);
            toUnfreeze.setNumLent(0);
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
        }
        else{
            System.out.println("The user is not frozen, you can not unfreeze the user");
        }
    }

    public void freezeUser() throws IOException {
        clientUserManager.showPendingUsers();
        ArrayList<String> input7 = PromptPresenter.takeInputLineByLine(FREEZE_PROMPT);
        ClientUser toFreeze = clientUserManager.getUserByUsername(input7.get(0));
        if (toFreeze.getAccountStatus().equals("pending")){
            clientUserManager.getClientUserList().removeFromPendingUser(toFreeze);
            clientUserManager.getClientUserList().addToFrozenUser(toFreeze);
            clientUserManager.changeUserStatusTo(toFreeze, "frozen");
            System.out.println(toFreeze+" is now set to: " + clientUserManager.getCurrentUser().getAccountStatus());
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        }
        else {
            System.out.println("The user is either frozen already or not in pending status");
        }
    }

    public void showAdminUsers() throws ClassNotFoundException {
        AdminUserReadWrite adminUserReadWrite = new AdminUserReadWrite();
        AdminUserManager adminUserManager = adminUserReadWrite.createClientUserManagerFromFile(ADMIN_USER_FILE);
        adminUserManager.printAllAdminUsers();
    }

    public void logout() throws IOException {
        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        StartMenuSystem startMenuSystem = new StartMenuSystem();
        startMenuSystem.run();
    }

    public void displayAllUserThresholds(){
        for (ClientUser user : clientUserManager.getClientUserList().getAllClientUser()) {
            System.out.println("Getting thresholds for " + user.getUserName());
            System.out.println(thresholdManager.getAllUserThresholds(user.getUserName()));
        }
    }

    public void changeThresholds() throws IOException {
        ArrayList<String> thresholdMenuInput = PromptPresenter.takeInput(THRESHOLD_PROMPT);
        if (thresholdMenuInput.get(0).equals("1")) {
            //2. Change Specific User Threshold (String username, String choice, int changeValue)
            ArrayList<String> input2 = PromptPresenter.takeInputLineByLine(CHANGE_USER_THRESHOLD_PROMPT);
            int newValue = Integer.parseInt(input2.get(2));
            thresholdManager.changeUserThreshold(input2.get(0), input2.get(1), newValue);
        } else if (thresholdMenuInput.get(0).equals("2")) {
            //5. Change Universal Threshold(String choice, int changeValue)
            ArrayList<String> input5 = PromptPresenter.takeInputLineByLine(CHANGE_THRESHOLD_PROMPT);
            int newValue = Integer.parseInt(input5.get(1));
            thresholdManager.changeGlobalThresholdValue(input5.get(0), newValue);
        }
        else if (thresholdMenuInput.get(0).equals("9")) {
            //9. logout
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            StartMenuSystem startMenuSystem = new StartMenuSystem();
            startMenuSystem.run();
        }
    }

    public void viewPendingTransactions(){
        ArrayList<String> input = PromptPresenter.takeInputLineByLine(GET_PENDING_TRANSACTION_PROMPT);
        transactionTicketManager.getPendingTransactionTicketByUser(input.get(0));
    }

}



