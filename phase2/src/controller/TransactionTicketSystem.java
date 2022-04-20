package controller;

import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.AppointmentManager;
import usecases.ClientUserManager;
import usecases.TransactionTicketManager;

import java.io.IOException;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*;

public class TransactionTicketSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private TransactionTicketManager transactionTicketManager;

    public TransactionTicketSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.transactionTicketManager = new TransactionTicketManager(clientUserManager);
    }
    public void run() throws IOException, ClassNotFoundException {
        System.out.println(clientUserManager.getCurrentUser().getPendingTransaction().getTransactionTicketList().toString());
        processInput(PromptPresenter.takeInput(CONFIRM_TRANSACTION_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        confirm(inputArray.get(0));
    }

    public void confirm(String id) throws IOException, ClassNotFoundException {
        if (transactionTicketManager.confirm(id)) {
            if (transactionTicketManager.completeTransaction(id)) {
                System.out.println("Transaction completed.");
            }
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        } else {
            System.out.println("Invalid id, please try again");

        }
    }
}
