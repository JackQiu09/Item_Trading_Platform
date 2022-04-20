package entities;

public class AdminUser extends User {

    // If the user is the initial admin
    private final boolean isInitialAdminUser;

    public AdminUser(String userName, String password, boolean isInitAdminUser) {
        super(userName, password);
        this.isInitialAdminUser = isInitAdminUser;
    }

    public boolean isAdminUser() {
        return true;
    }

    public boolean isInitialAdminUser(){
        return isInitialAdminUser;
    }

    @Override
    public String toString() {
        StringBuilder adminString = new StringBuilder();
        adminString.append("===================================" + "\n");
        adminString.append("Username: " + this.getUserName() + "\n");
        adminString.append("Password: " + this.getPassword() + "\n");
        adminString.append("Are you an Initial Admin user?: " + this.isInitialAdminUser + "\n");
        adminString.append("===================================" + "\n");
        return adminString.toString();
        //return "IsInitialAdminUser: " + this.isInitialAdminUser + ", Username: " + this.getUserName() + ", Password: " + this.getPassword();
    }
}
