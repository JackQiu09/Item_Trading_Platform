package entities;

/**
 * A class that represents an admin user.
 */
public class AdminUser extends User {

    // If the user is the initial admin
    private final boolean isInitialAdminUser;

    /**
     * Initialize a new admin user.
     * @param userName The username to use.
     * @param password The password for this new admin.
     * @param isInitAdminUser Whether this new user is an initial admin.
     */
    public AdminUser(String userName, String password, boolean isInitAdminUser) {
        super(userName, password);
        this.isInitialAdminUser = isInitAdminUser;
    }

    /**
     * Checks if user instance is an admin.
     * @return True if user is admin.
     */
    public boolean isAdminUser() {
        return true;
    }

    /**
     * Check if admin user is initial admin.
     * @return True if admin user is initial
     */
    public boolean isInitialAdminUser(){
        return isInitialAdminUser;
    }

    /**
     * Makes a string representation of this admin.
     * @return A string representation of this admin.
     */
    @Override
    public String toString() {
        StringBuilder adminString = new StringBuilder();
        adminString.append("===================================" + "\n");
        adminString.append("Username: " + this.getUserName() + "\n");
        adminString.append("Password: " + this.getPassword() + "\n");
        adminString.append("Are you an Initial Admin user?: " + this.isInitialAdminUser + "\n");
        adminString.append("===================================" + "\n");
        return adminString.toString();
    }
}
