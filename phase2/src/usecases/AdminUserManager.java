package usecases;

import entities.AdminUser;
import entities.AdminUserList;
import entities.ClientUser;
import entities.DepositRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminUserManager implements Serializable {
    private AdminUserList adminUserList;
    private AdminUser currentAdminUser;


    public AdminUserManager(AdminUserList adminUserList){
        this.adminUserList = adminUserList;
    }

    public AdminUserManager() {this.adminUserList = new AdminUserList();}

    public AdminUserList getAdminUserList() {
        return adminUserList;
    }

    public boolean approveDeposit (ArrayList<DepositRequest> depositRequests, UUID id)
    {return true;}

    /**
     * Do login with provided userName and password
     *
     * @param userName
     * @param password
     * @return true if login successful, false otherwise
     */
    public boolean login(String userName, String password){
        for(AdminUser adminUser : adminUserList.getAllAdminUser()){
            if(adminUser.getUserName().equals(userName)){
                if(adminUser.passwordMatch(password)){
                    currentAdminUser = adminUser;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    //This method is for testing purposes, will delete later
    public void createInitAdmin(String username, String password) {
        AdminUser ad = new AdminUser(username,password,true);
        adminUserList.addToAllAdminUser(ad);
        adminUserList.addToInitialAdminUser(ad);
    }

    /**
     * Method signs up as admin user with given userName and passwords
     * @param username
     * @param password
     * @return true if signup as admin successful, false otherwise
     */
    public boolean signUpAdminUser(String username, String password){
        for(AdminUser adminUser: adminUserList.getAllAdminUser()){
            if(adminUser.getUserName().equalsIgnoreCase(username)){
                return false;
            }
        }
        AdminUser newAdminUser = new AdminUser(username, password, false);
        adminUserList.addToAllAdminUser(newAdminUser);
        adminUserList.addToNonInitialAdminUser(newAdminUser);
        return true;
    }

    public boolean isCurrentAdminUserInitial(){
        return currentAdminUser.isInitialAdminUser();
    }

    /**
     * Prints all admin users available in system
     */
    public void printAllAdminUsers() {
            for (AdminUser admin: adminUserList.getAllAdminUser()) {
                System.out.println(admin.toString());
            }
    }


}
