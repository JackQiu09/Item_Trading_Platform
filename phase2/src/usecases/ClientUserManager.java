package usecases;

import entities.*;
import gateway.ClientUserReadWrite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gateway.FileReadAndWrite.CLIENT_USER_FILE;

public class ClientUserManager extends Observable implements Serializable {
    private static final Logger logger = Logger.getLogger(ClientUserManager.class.getName());
    private ClientUserList clientUserList;
    private ClientUser currentUser;
    public ThresholdManager thresholdManager;
    private ArrayList<DepositRequest> depositRequests = new ArrayList<DepositRequest>();

    public ClientUserManager(ClientUserList clientUserList){
        this.clientUserList = clientUserList;
        this.thresholdManager = new ThresholdManager();
    }

    public ClientUserManager(){
        this.clientUserList = new ClientUserList();
        this.thresholdManager = new ThresholdManager();
    }

    public ClientUserList getClientUserList() {
        return clientUserList;
    }

    /**
     * Checks and validates the user with given username, and password
     * @param userName
     * @param password
     * @return returns true if given client user is validated, false otherwise
     */

    public boolean login(String userName, String password){
        for(ClientUser clientUser : clientUserList.getAllClientUser()){
            if(clientUser.getUserName().equals(userName)){
                if(clientUser.passwordMatch(password)){
                    currentUser = clientUser;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Create a user in the client user system with given username and password
     * @param username
     * @param password
     * @return true if user created and added to active users list successfully, false otherwise
     */
    public boolean createUserAccount(String username, String password, String home){
        for(ClientUser clientUser: clientUserList.getAllClientUser()){
            if(clientUser.getUserName().equalsIgnoreCase(username)){
                return false;
            }
        }
        ClientUser clientUser = new ClientUser(username, password, home);
        clientUserList.addToActiveUser(clientUser);
        clientUserList.addToAllClientUser(clientUser);
        thresholdManager.addUserThreshold(username);
        //log the act of sign up and display it on screen
        logger.log(Level.INFO, "Created a new client user");
        currentUser = clientUser;
        return true;
    }

    /**
     * Prints all pending users available in the client user system
     */
    public void showPendingUsers() {
        if (clientUserList.getPendingUser().isEmpty()) {
            System.out.println("No pending users");
        } else {
            for (ClientUser user: clientUserList.getPendingUser()) {
                System.out.println(user.toString());
            }
        }
    }

    public void showPendingUnfreezeUsers() {
        if (clientUserList.getPendingUnfreezeUser().isEmpty()) {
            System.out.println("No pending unfreeze users");
        } else {
            for (ClientUser user: clientUserList.getPendingUnfreezeUser()) {
                System.out.println(user.toString());
            }
        }
    }

    /**
     * prints all the client users available in the system
     */
    public void printAllUsers() {
        if (clientUserList.getAllClientUser().isEmpty()) {
            System.out.println("No client users");
        } else {
            for (ClientUser user: clientUserList.getAllClientUser()) {
                System.out.println(user.toString());
            }
        }
    }

    public ArrayList<DepositRequest> getDepositRequests() {
        return depositRequests;
    }

    public boolean requestDeposit(ClientUser user, double depositAmount)
    {
        depositRequests.add(new DepositRequest(user, depositAmount));
        return true;
    }

    public void changeUserStatusTo(ClientUser user, String status) {
        user.setAccountStatus(status);
    }

    public ClientUser getUserByApptId(String id) {
        for (ClientUser clientUser: clientUserList.getActiveUser()) {
            for (Appointment app: clientUser.getPendingAppointments().getAppointmentList()) {
                if (app.getId().equals(id)) {
                    return clientUser;
                }
            }
        }
        return null;
    }

    public void showDepositRequests()
    {
        for (DepositRequest req : depositRequests)
        {
            System.out.println(req);
        }
    }

    public void approveDeposit(UUID id)
    {
        DepositRequest req = findDepositRequestById(id);
        if (req != null)
        {
            ClientUser user = req.getUser();
            user.setAccountBalance(user.getAccountBalance() + req.getDepositAmount());
            depositRequests.remove(req);
        } else {
            System.out.println("This id does not exist.");
        }
    }

    private DepositRequest findDepositRequestById(UUID id)
    {
        for (DepositRequest req : depositRequests)
        {
            if (req.getId() == id)
            {
                return req;
            }
        }
        return null;
    }

    public ClientUser getCurrentUser(){
        return currentUser;
    }

    /**
     * check if given user exists in the client user system
     * @param username
     * @return true if user available, false otherwise
     */
    public boolean isExist(String username) {
        AtomicBoolean found = new AtomicBoolean(false);
        clientUserList.getAllClientUser().forEach((item) -> {
            if (item.getUserName().equals(username))
                found.set(true);
        });
        return found.get();
    }

    /**
     * Provides clientuser if provided username matches with the any of the client user availble in the client user system
     * @param user
     * @return clientUser if user found, null otherwise
     */
    public ClientUser getUserByUsername(String user){
        List<ClientUser> all_users = clientUserList.getAllClientUser();
        for (ClientUser to_find: all_users){
            if (to_find.getUserName().equals(user)){
                return to_find;
            }
        }
        return null;
    }


}
