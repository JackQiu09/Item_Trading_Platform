package controller;

import entities.ClientUserSnapshot;
import gateway.AppointmentSnapshotReadWrite;
import gateway.ClientUserReadWrite;
import gateway.ClientUserSnapshotReadWrite;
import presenter.PromptPresenter;
//import sun.tools.java.ScannerInputReader;
import usecases.AppointmentManager;
import usecases.AppointmentSnapshotManager;
import usecases.ClientUserManager;
import usecases.ClientUserSnapshotManager;

import static gateway.FileReadAndWrite.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SnapshotSystem implements InputProcessable{
    private ClientUserManager clientUserManager;
    private AppointmentSnapshotReadWrite asrw = new AppointmentSnapshotReadWrite();
    private AppointmentSnapshotManager asm = asrw.createAppointmentSnapshotManagerFromFile(APPOINTMENT_SNAPSHOT);
    private ClientUserSnapshotReadWrite csrw = new ClientUserSnapshotReadWrite();
    private ClientUserSnapshotManager csm = csrw.createClientUserSnapshotManagerFromFile(CLIENT_USER_SNAPSHOT);
    private AppointmentManager appointmentManager;

    public SnapshotSystem(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.appointmentManager = new AppointmentManager(clientUserManager,asm);
    }

    public void run() throws IOException, ClassNotFoundException {
        processInput(PromptPresenter.takeInput(UNDO_ACTION_PROMPT));
    }

    @Override
    public void processInput(ArrayList<String> inputArray) throws IOException, ClassNotFoundException {
        // undo appointment edit
        if (inputArray.get(0).equals("1")) {
            if (asm.displayNotification()) {
                undoEdit();
            } else {
                System.out.println("No appointment edits to undo at this moment. Please come back later.");
                run();
            }

        } else if (inputArray.get(0).equals("2")) {
            if (csm.showAll()) {
                undo();
            } else {
                System.out.println("No home edit to undo at this moment. Please come back later.");
                run();
            }
        } else if (inputArray.get(0).equals("9")) {
            AdminSystem adminSystem = new AdminSystem(clientUserManager);
            adminSystem.run();
        }
    }

    public void undoEdit() throws IOException, ClassNotFoundException {
        System.out.println("Please type in the action id to undo:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();
        System.out.println("Please type in the id of the appointment:");
        String input2 = br.readLine();
        int index = Integer.parseInt(input);
        if (!appointmentManager.getPendingAppointmentById(input2).getTradeType().equals("money"))
            appointmentManager.getPendingAppointmentById(input2).getStateFromSnapshot(asm.get(index));
        else {
            appointmentManager.getPendingAppointmentById(input2).getStateFromMoneySnapshot(asm.get(index));
        }
        asm.removeFromList(index);
        System.out.println("undo successful");
        AppointmentSnapshotReadWrite.saveToFile(APPOINTMENT_SNAPSHOT,asm);
        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        run();
    }

    public void undo() throws IOException, ClassNotFoundException {
        System.out.println("Please type in the action id to undo:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();
        System.out.println("Please type in the user name associated with the action:");
        String input2 = br.readLine();
        int index = Integer.parseInt(input);
        clientUserManager.getUserByUsername(input2).getStateFromSnapshot(csm.get(index));
        csm.removeFromList(index);
        System.out.println("undo successful");
        ClientUserSnapshotReadWrite.saveToFile(CLIENT_USER_SNAPSHOT,csm);
        ClientUserReadWrite.saveToFile(CLIENT_USER_FILE,clientUserManager);
        run();
        }

}
