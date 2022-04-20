package controller;

import entities.Appointment;
import entities.ClientUser;
import gateway.AppointmentSnapshotReadWrite;
import gateway.ClientUserReadWrite;
import presenter.PromptPresenter;
import usecases.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Observable;

import static gateway.FileReadAndWrite.*;

public class AppointmentSystem implements InputProcessable {
    private ClientUserManager clientUserManager;
    private AppointmentManager appointmentManager;
    private AppointmentSnapshotReadWrite asrw = new AppointmentSnapshotReadWrite();
    private AppointmentSnapshotManager appointmentSnapshotManager = asrw.createAppointmentSnapshotManagerFromFile(APPOINTMENT_SNAPSHOT);

    public AppointmentSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager =clientUserManager;
        this.appointmentManager = new AppointmentManager(clientUserManager,appointmentSnapshotManager);
    }

    public void run() throws IOException, ClassNotFoundException {
        appointmentManager.addObserver(appointmentSnapshotManager);
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
    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
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
        if (appointmentManager.getPendingAppointmentById(id).getTradeType().equals("money"))
        {
            System.out.println("Please suggest a price: ");
            String innerInput = br.readLine();
            if (!input.equals("exit")) {
                inputArray.add(innerInput);
            }

            if (appointmentManager.editMoneyAppointment(id, Double.parseDouble(inputArray.get(2)), inputArray.get(0), inputArray.get(1))) {
                AppointmentSnapshotReadWrite.saveToFile(APPOINTMENT_SNAPSHOT,appointmentSnapshotManager);
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);

            } else {
                System.out.println("Edit failed.");
            }
        } else {
            if (appointmentManager.editAppointment(id, inputArray.get(0), inputArray.get(1))) {
                AppointmentSnapshotReadWrite.saveToFile(APPOINTMENT_SNAPSHOT,appointmentSnapshotManager);
                ClientUserReadWrite.saveToFile(CLIENT_USER_FILE, clientUserManager);
            } else {
                System.out.println("Edit failed.");
            }
        }
    }

    private void confirmAppointment(String id) throws IOException, ClassNotFoundException {
        if (appointmentManager.confirmAppointment(id)) {
            if (appointmentManager.isBothConfirmed(id)) {
                System.out.println("Both user has confirmed the appointment, a transaction ticket is created.");
            }
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
            TradingSystem tradingSystem = new TradingSystem(clientUserManager);
            tradingSystem.run();
        } else {
            System.out.println("Invalid id, please try again.");
            run();
        }
    }

    private void declineAppointment(String id) throws IOException, ClassNotFoundException {
        if (appointmentManager.declineAppointment(id)) {
            System.out.println("Decline successful");
            ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        } else {
            System.out.println("Invalid id, please try again.");
            run();
        }
    }
}
