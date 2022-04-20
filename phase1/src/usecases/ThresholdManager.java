package usecases;

import entities.ClientUser;
import entities.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThresholdManager implements Serializable {
    private ClientUserManager clientUserManager;

    private static HashMap<String, HashMap<String, Double>> userThresholds = new HashMap<String, HashMap<String, Double>>();

    // David's Code
    public ThresholdManager(ClientUserManager clientUserManager) {
        this.clientUserManager = clientUserManager;
    }

    public void changeThresholdValue(String username, String choice, int changeValue) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getAllClientUser()) {
            if (clientUser.getUserName().equals(username)) {
                if (choice.equals("incompleteLimit")) {
                    clientUser.setIncompleteLimit(changeValue);
                }
                else if (choice.equals("transactionLimit")) {
                    clientUser.setTransactionLimit(changeValue);
                }
            }
        }
    }

    public void changeGlobalThreshold(String choice, int changeValue) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getAllClientUser()) {
            if (choice.equals("incompleteLimit") && clientUser.getIncompleteLimit() == changeValue) {
                clientUser.setIncompleteLimit(changeValue);
            }
            else if (choice.equals("transactionLimit") && clientUser.getTransactionLimit() == changeValue) {
                clientUser.setTransactionLimit(changeValue);
            }
        }
    }



    public String displayAllUserThresholds() {
        StringBuilder thresholdString = new StringBuilder();
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {

            thresholdString.append("===================================" + "\n");
            thresholdString.append("Number of items Lent: " + clientUser.getUserName() + "\n");
            thresholdString.append("Number of items Lent: " + clientUser.getNumLent() + "\n");
            thresholdString.append("Number of items Lent: " + clientUser.getNumBurrowed()+ "\n");
            thresholdString.append("Incomplete Transaction Limit: " + clientUser.getIncompleteLimit() + "\n");
            thresholdString.append("Transaction Limit: " + clientUser.getTransactionLimit() + "\n");
            thresholdString.append("===================================" + "\n");
        }

        return thresholdString.toString();
    }

    public String displayUserThresholdByCurrentUser() {
        StringBuilder thresholdString = new StringBuilder();
        ClientUser clientUser = clientUserManager.getCurrentUser();
        thresholdString.append("===================================" + "\n");
        thresholdString.append("Number of items Lent: " + clientUser.getUserName() + "\n");
        thresholdString.append("Number of items Lent: " + clientUser.getNumLent() + "\n");
        thresholdString.append("Number of items Lent: " + clientUser.getNumBurrowed()+ "\n");
        thresholdString.append("Incomplete Transaction Limit: " + clientUser.getIncompleteLimit() + "\n");
        thresholdString.append("Transaction Limit: " + clientUser.getTransactionLimit() + "\n");
        thresholdString.append("===================================" + "\n");


        return thresholdString.toString();
    }

    // END OF DAVID'S CODE


    // Helper class for creating default thresholds
    private static class DefaultThresholds {
        private static HashMap<String, Double> defaultThresholds = initializeDefaultThresholds();

        private static HashMap<String, Double> initializeDefaultThresholds() {
            HashMap<String, Double> def = new HashMap<String, Double>();
            def.put("lentBorrowDiff", (double) 0);
            def.put("incompleteLimit", (double) 0);
            def.put("transactionLimit", (double) 7);
            return def;
        }

        private static HashMap<String, Double> getDefaultThresholds() {
            HashMap<String, Double> defaultCopy = new HashMap<String, Double>();
            for (String threshold : defaultThresholds.keySet()) {
                defaultCopy.put(threshold, defaultThresholds.get(threshold));
            }
            return defaultCopy;
        }

        private static void addDefaultThreshold(String thresholdName, Double value) {
            defaultThresholds.put(thresholdName, value);
        }

        private static void removeDefaultThreshold(String thresholdName) {
            if (defaultThresholds.containsKey(thresholdName)) {
                defaultThresholds.remove(thresholdName);
            }
        }
    }

    /**
     * @param userId username for this user
     */
    public void addUserThreshold(String userId) {
        userThresholds.put(userId, DefaultThresholds.getDefaultThresholds());
    }

    public void changeUserThreshold(String userId, String thresholdName, Double newValue) {
        if (DefaultThresholds.defaultThresholds.containsKey(thresholdName)) {
            userThresholds.get(userId).replace(thresholdName, newValue);
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
        }
    }

    public void addThreshold(String thresholdName, Double value) {
        DefaultThresholds.addDefaultThreshold(thresholdName, value);
        for (HashMap<String, Double> val : userThresholds.values()) {
            val.put(thresholdName, value);
        }
    }

    public void removeThreshold(String thresholdName) {
        if (DefaultThresholds.defaultThresholds.containsKey(thresholdName)) {
            DefaultThresholds.removeDefaultThreshold(thresholdName);
            for (HashMap<String, Double> val : userThresholds.values()) {
                val.remove(thresholdName);
            }
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
        }
    }

    public void changeGlobalThresholdValue(String thresholdName, Double newValue) {
        if (DefaultThresholds.defaultThresholds.containsKey(thresholdName)) {
            DefaultThresholds.defaultThresholds.replace(thresholdName, newValue);
            for (HashMap<String, Double> val : userThresholds.values()) {
                val.replace(thresholdName, newValue);
            }
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
        }
    }

    public static Double getUserThreshold(String userId, String thresholdName) {
        if (DefaultThresholds.defaultThresholds.containsKey(thresholdName)) {
            return userThresholds.get(userId).get(thresholdName);
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
            return null;
        }
    }

    public static String getAllUserThresholds(String userId) {
        StringBuilder formattedThresholds = new StringBuilder();
        // iterating every set of entry in the HashMap.
        for (Map.Entry<String, Double> set : userThresholds.get(userId).entrySet()) {
            formattedThresholds.append(set.getKey()).append(" = ").append(set.getValue()).append("\n");
        }
        System.out.println(userThresholds);
        return formattedThresholds.toString();
    }
}