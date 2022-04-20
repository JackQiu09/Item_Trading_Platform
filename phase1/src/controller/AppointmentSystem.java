package controller;

import entities.Appointment;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import static gateway.FileReadAndWrite.*;

// TODO: finish this
public class AppointmentSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private AppointmentManager appointmentManager;

    public AppointmentSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager =clientUserManager;
        this.appointmentManager = new AppointmentManager(clientUserManager);
    }
    public void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = new ArrayList<>();
        System.out.println(clientUserManager.getCurrentUser().getPendingAppointments().toString());
        System.out.println("Please type in the appointment id you would like to edit/confirm/decline.");
        String input = br.readLine();
        if (!input.equals("exit")) {
            inputArray.add(input);
        }
        System.out.println("Type 'edit', 'confirm', or 'decline' to edit/confirm/decline this appointment");
        input = br.readLine();
        if (!input.equals("exit")) {
            inputArray.add(input);
            processInput(inputArray);
        }
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws IOException {
        if (appointmentManager.matchPendingAppointmentById(inputArray.get(0))){
            switch (inputArray.get(1)) {
                case "edit":
                    editAppointment(inputArray.get(0));
                    break;
                case "confirm":
                    confirmAppointment(inputArray.get(0));
                    break;
                case "decline":
                    declineAppointment(inputArray.get(0));
                    break;
            }
        }
    }

    private void editAppointment(String id) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> inputArray = new ArrayList<>();
        System.out.println("Please suggest a time(e.g. 2020-09-09-12:00):");
        String input = br.readLine();
        if (!input.equals("exit")) {
            inputArray.add(input);
        }
        System.out.println("Please suggest a place:");
        input = br.readLine();
        if (!input.equals("exit")) {
            inputArray.add(input);
        }
        if (appointmentManager.editAppointment(id,inputArray.get(0),inputArray.get(1))) {
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        } else{
            System.out.println("Edit failed.");
        }
    }

    private void confirmAppointment(String id) throws IOException {
        if (appointmentManager.confirmAppointment(id)) {
            if (appointmentManager.isBothConfirmed(id)) {
                System.out.println("Both user has confirmed the appointment, a transaction ticket is created.");
            }
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        } else {
            System.out.println("Invalid id, please try again.");
            confirmAppointment(id);
        }
    }

    private void declineAppointment(String id) throws IOException {
        if (appointmentManager.declineAppointment(id)) {
            System.out.println("Decline successful");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        } else {
            System.out.println("Invalid id, please try again.");
            declineAppointment(id);
        }
    }
}
