package controller;

import presenter.PromptPresenter;
import presenter.PropertiesIterator;
import usecases.ClientUserManager;
import gateway.ClientUserReadWrite;
import usecases.ThresholdManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static gateway.FileReadAndWrite.*;

public class SignUpSystem implements InputProcessable{
    ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
    ClientUserManager clientUserManager = clientUserReadWrite.createClientUserManagerFromFile(CLIENT_USER_FILE);

    public SignUpSystem() throws ClassNotFoundException {
    }


    public void run() throws IOException, ClassNotFoundException {

        processInput(PromptPresenter.takeInputLineByLine(SIGNUP_PROMPT));
    }


    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        String username = inputArray.get(0);
        String password = inputArray.get(1);
        String home = inputArray.get(2);
        clientSignUp(username, password, home);

    }

    public void clientSignUp(String username, String password, String home) throws IOException, ClassNotFoundException {
            boolean signUpSuccess = clientUserManager.createUserAccount(username, password, home);
            if (signUpSuccess) {
                System.out.println("Sign up Success!");
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
                ClientUserSystem clientUserSystem = new ClientUserSystem(clientUserManager);
                clientUserSystem.run();
            } else {
                System.out.println("Username is already taken, please choose another one");
                run();
            }
    }
}
