package usecases;

import entities.ClientUser;
import entities.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A functorial class used for generating, managing, and viewing thresholds.
 * @version 1.5
 * @since 1.0
 */
public class ThresholdManager implements Serializable {

    private DefaultThresholds defaults = new DefaultThresholds();
    private HashMap<String, HashMap<String, Integer>> userThresholds = new HashMap<String, HashMap<String, Integer>>();

    // Helper class for creating default thresholds
    private class DefaultThresholds implements Serializable {
        private HashMap<String, Integer> defaultThresholds = initializeDefaultThresholds();

        public DefaultThresholds(){}

        private HashMap<String, Integer> initializeDefaultThresholds() {
            HashMap<String, Integer> def = new HashMap<String, Integer>();
            def.put("lentBorrowDiff", 0);
            def.put("incompleteLimit",3);
            def.put("transactionLimit", 7);
            def.put("editsCount", 3);
            return def;
        }

        private HashMap<String, Integer> getDefaultThresholds() {
            HashMap<String, Integer> defaultCopy = new HashMap<String, Integer>();
            for (String threshold : defaultThresholds.keySet()) {
                defaultCopy.put(threshold, defaultThresholds.get(threshold));
            }
            return defaultCopy;
        }

        private void addDefaultThreshold(String thresholdName, Integer value) {
            defaultThresholds.put(thresholdName, value);
        }

        private void removeDefaultThreshold(String thresholdName) {
            if (defaultThresholds.containsKey(thresholdName)) {
                defaultThresholds.remove(thresholdName);
            }
        }
    }

    /**
     * Adds a user to threshold manager, and gives them default thresholds.
     * @param userId username for this user.
     */
    public void addUserThreshold(String userId) {
        userThresholds.put(userId, defaults.getDefaultThresholds());
    }

    /**
     * Changes a specific user's value for a specified threshold.
     * @param userName The user's username.
     * @param thresholdName Name of the threshold to be changed.
     * @param newValue The new value for this user's threshold.
     */
    public void changeUserThreshold(String userName, String thresholdName, Integer newValue) {
        boolean userExists = userThresholds.containsKey(userName);
        if (defaults.defaultThresholds.containsKey(thresholdName) && userExists) {
            userThresholds.get(userName).replace(thresholdName, newValue);
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
        }
    }

    /**
     * Add ThresholdValue to default ThresholdValue
     * @param thresholdName
     * @param value
     */
    public void addThreshold(String thresholdName, Integer value) {
        defaults.addDefaultThreshold(thresholdName, value);
        for (HashMap<String, Integer> val : userThresholds.values()) {
            val.put(thresholdName, value);
        }
    }

    /**
     * removes default ThresholdValue
     * @param thresholdName
     */
    public void removeThreshold(String thresholdName) {
        if (defaults.defaultThresholds.containsKey(thresholdName)) {
            defaults.removeDefaultThreshold(thresholdName);
            for (HashMap<String, Integer> val : userThresholds.values()) {
                val.remove(thresholdName);
            }
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
        }
    }

    /**
     * Changes the threshold value for all users within the system.
     * @param thresholdName Name of the threshold to be changed.
     * @param newValue  New value of the threshold.
     */
    public void changeGlobalThresholdValue(String thresholdName, Integer newValue) {
        if (defaults.defaultThresholds.containsKey(thresholdName)) {
            defaults.defaultThresholds.replace(thresholdName, newValue);
            for (HashMap<String, Integer> val : userThresholds.values()) {
                val.replace(thresholdName, newValue);
            }
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
        }
    }

    /**
     * Gets the specified threshold value for a user.
     * @param userName The name of the user.
     * @param thresholdName The name of the threshold.
     * @return The value of the threshold.
     */
    public Integer getUserThreshold(String userName, String thresholdName) {
        if (defaults.defaultThresholds.containsKey(thresholdName)) {
            // System.out.println(userThresholds.get(userName).get(thresholdName));
            return userThresholds.get(userName).get(thresholdName);
        } else {
            System.out.println("The following threshold does not exist: " + thresholdName);
            return null;
        }
    }

    /**
     * Gets all the threshold values for a user
     * @param userId the name of the user.
     * @return A string that writes all the thresholds for this user.
     */
    public String getAllUserThresholds(String userId) {
        StringBuilder formattedThresholds = new StringBuilder();
        // iterating every set of entry in the HashMap.
        for (Map.Entry<String, Integer> set : userThresholds.get(userId).entrySet()) {
            formattedThresholds.append(set.getKey()).append(" = ").append(set.getValue()).append("\n");
        }
        return formattedThresholds.toString();
    }
}