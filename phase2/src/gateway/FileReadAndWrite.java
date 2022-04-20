package gateway;

import java.io.*;

// TODO: document it
public class FileReadAndWrite {

    public final static String CLIENT_USER_FILE = "./phase2/src/database/client_users.ser";
    public final static String ADMIN_USER_FILE = "./phase2/src/database/admin_users.ser";
    public final static String APPOINTMENT_SNAPSHOT = "./phase2/src/database/appointment_snapshot.ser";
    public final static String CLIENT_USER_SNAPSHOT = "./phase2/src/database/client_user_snapshot.ser";
    public final static String LOGIN_PROMPT = "loginPrompt.txt";
    public final static String SIGNUP_PROMPT = "signUpPrompt.txt";
    public final static String APPOINTENTS_PROMPT = "AppointmentUserSystemPrompt.txt";
    public final static String CLIENT_USER_SYSTEM_PROMPT = "clientUserSystemPrompt.txt";
    public final static String ADMIN_USER_SYSTEM_PROMPT = "adminUserSystemPrompt.txt";
    public final static String INITIAL_ADMIN_USER_SYSTEM_PROMPT = "initialAdminUserPrompt.txt";
    public final static String CREATE_ADMIN_PROMPT = "createAdminPrompt.txt";
    public final static String TRADE_SYSTEM_PROMPT = "tradingSystemPrompt.txt";
    public final static String REQUEST_TO_ADD_ITEM_PROMPT = "requestToAddItemPrompt.txt";
    public final static String TRADE_SET_UP_PROMPT = "tradeSetUpPrompt.txt";
    public final static String EDIT_TRADE_PROMPT = "editTradePrompt.txt";
    public final static String CONFIRM_TRANSACTION_PROMPT = "confirmTransactionPrompt.txt";
    public final static String CONFIRM_TRANSACTION_OPTIONS_PROMPT = "confirmTransactionOptionsPrompt.txt";
    public final static String BUY_TRADE_SET_UP_PROMPT = "buyTradeSetUpPrompt.txt";
    public final static String LEND_SET_UP_PROMPT = "lendSetUpPrompt.txt";
    public final static String CHANGE_HOME_PROMPT = "changeHomePrompt.txt";
    public final static String UNDO_ACTION_PROMPT = "undoActionPrompt.txt";
    public final static String SUGGEST_LEND_PROMPT = "suggestLendPrompt.txt";
    public final static String CONFIRM_SUGGEST_PROMPT = "confirmSuggestPrompt.txt";
    public final static String CONFIRM_LEND_PROMPT = "confirmLendPrompt.txt";
    public final static String FINISH_SUGGEST_LEND_PROMPT = "finishSuggestLendPrompt.txt";
    public final static String TRANSACTION_TYPE_PROMPT = "transactionTypePrompt.txt";

    // Deposit Prompt for client
    public final static String DEPOSIT_PROMPT = "deposit_prompt.txt";

    // Threshold manager
    public final static String THRESHOLD_MANAGER_FILE = "./phase2/src/database/thresholds.ser";

    //Admin System Prompts
    //Threshold Prompts
    public final static String THRESHOLD_PROMPT = "threshholdPrompt.txt";
    public final static String CHANGE_THRESHOLD_PROMPT = "changeThresholdPrompt.txt";
    public final static String CHANGE_USER_THRESHOLD_PROMPT = "changeUserThresholdPrompt.txt";
    public final static String ADD_USER_THRESHOLD_PROMPT = "addUserThresholdPrompt.txt";
    public final static String REMOVE_THRESHOLD_PROMPT = "removeThresholdPrompt.txt";
    public final static String FREEZE_PROMPT = "freeze_prompt.txt";
    public final static String UNFREEZE_PROMPT = "unfreeze_prompt.txt";
    public final static String INVISIBLE_PROMPT = "invisible_prompt.txt";
    public final static String DEPOSIT_REQUESTS_PROMPT = "deposit_requests_prompt.txt";


    //Transactions Prompts
    public final static String GET_PENDING_TRANSACTION_PROMPT = "getPendingTransactionPrompt.txt";

    public final static String APPOINTMENTS_FILE = "./phase2/src/database/appointments.ser";

    private final static String filePath = "./phase2/src/database/";

    //reference https://stackoverflow.com/a/4716623
    public static String readFromFile(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(filePath + fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //reference:https://howtodoinjava.com/java/io/java-append-to-file/
    public static void appendLineToFile(String fileName, String text){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(
                    new FileWriter(filePath + fileName, true)  //Set true for append mode
            );
            writer.newLine();
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
