package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A list that keeps track of all admin users, and notes which is the initial admin.
 */
public class AdminUserList implements Serializable {
    private List<AdminUser> allAdminUser;
    private List<AdminUser> initialAdmin;
    private List<AdminUser> nonInitialAdmin;

    /**
     * Initialize this list.
     */
    public AdminUserList() {
        allAdminUser = new ArrayList<>();
        initialAdmin = new ArrayList<>();
        nonInitialAdmin = new ArrayList<>();
    }

    /**
     * Get the adminUserList.
     * @return The adminUserList.
     */
    public List<AdminUser> getAllAdminUser() {
        return allAdminUser;
    }

    public List<AdminUser> getNonInitialAdmin() {
        return nonInitialAdmin;
    }

    /**
     * Add a new admin to this list.
     * @param adminUser The admin user to be added.
     */
    public void addToAllAdminUser(AdminUser adminUser) {allAdminUser.add(adminUser);}

    /**
     * Add a new admin as initial admin.
     * @param adminUser The admin user to add as an initial admin.
     */
    public void addToInitialAdminUser(AdminUser adminUser) {initialAdmin.add(adminUser);}

    /**
     * Add a new admin to the non-initial admins.
     * @param adminUser The non-initial admin user to be added.
     */
    public void addToNonInitialAdminUser(AdminUser adminUser) {initialAdmin.add(adminUser);}

    private List<AdminUser> getInitialAdmins()
            // Functor method for possible extendability
    {
        List<AdminUser> adminList = new ArrayList<AdminUser>();
        for (AdminUser a : allAdminUser)
        {
            if (a.isInitialAdminUser())
            {
                adminList.add(a);
            }
        }
        return adminList;
    }

}
