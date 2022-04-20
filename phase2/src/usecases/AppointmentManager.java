package usecases;


import entities.Appointment;
import entities.AppointmentList;
import entities.*;
import gateway.ThresholdReadWrite;
import static gateway.FileReadAndWrite.*;

import entities.TransactionTicket;

import java.util.Observable;

public class AppointmentManager extends Observable{
    private ClientUserManager clientUserManager;
    private ThresholdManager thresholdmanager;
    private AppointmentList appointmentList;
    private AppointmentSnapshotManager asm;
    private ThresholdReadWrite thresholdReadWrite = new ThresholdReadWrite();
    private ThresholdManager thresholdManager = thresholdReadWrite.createThresholdManagerFromFile(THRESHOLD_MANAGER_FILE);

    public AppointmentManager(ClientUserManager clientUserManager, AppointmentSnapshotManager asm) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
        this.asm = asm;
    }

    public AppointmentManager(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
    }

    // changing Time/address/Time and Address
    // For changing both Time and Address, use format: "new time,new address"
    // userId is the person that is changing
    /**
     * Checks if current user is proposer
     * @param id
     * @return true if current user is proposer, false otherwise.
     */
    private boolean isCurrentUserProposer(String id) {
        return getPendingAppointmentById(id).getProposer().getUserName().equals(clientUserManager.getCurrentUser().getUserName());
    }

    /**
     * Checks if current user is receiver
     * @param id
     * @return true if current user is receiver, false otherwise.
     */
    private boolean isCurrentUserReceiver(String id) {
        return getPendingAppointmentById(id).getReceiver().getUserName().equals(clientUserManager.getCurrentUser().getUserName());
    }

    /**
     * Provides Appointment with given id if the appointment is pending of active client users
     *
     * @param id
     * @returns Appointment if found with given id, false otherwise
     */
    public Appointment getPendingAppointmentById(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Appointment appointment : clientUser.getPendingAppointments().getAppointmentList()) {
                if (appointment.getId().equals(id)) {
                    return appointment;
                }
            }
        }
        return null;
    }

    /**
     * Allows to edit appointment
     * @param apptId
     * @param time
     * @param place
     * @return true if editing successful, false otherwise
     */
    public Appointment getCompletedAppointmentById(String id) {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            for (Appointment appointment : clientUser.getConfirmedAppointments().getAppointmentList()) {
                if (appointment.getId().equals(id)) {
                    return appointment;
                }
            }
        }
        return null;
    }



    public boolean editAppointment(String apptId, String time, String place) {
        Appointment appointment= getPendingAppointmentById(apptId);
        if (appointment != null) {
            //user exceeded maximum edits
            if (isCurrentUserProposer(apptId)) {
                if (appointment.getUser1EditsCount() < 3) {
                    if (!appointment.getTradeType().equals("money")) {
                        asm.add(appointment.createProposerSnapshot());
                    } else {
                        asm.add(appointment.createProposerMoneySnapshot());
                    }
                    appointment.setTime(time);
                    appointment.setAddress(place);
                    appointment.incrementUserEditsCount(clientUserManager.getCurrentUser().getUserName());
                    appointment.setProposerConfirm(true);
                    appointment.setReceiverConfirmed(false);
                    if (!appointment.getTradeType().equals("money")) {
                        setChanged();
                        notifyObservers(appointment.getReceiver().getUserName()  + " edited Appointment ID: " + appointment.getId()+ " time/place to "
                                + appointment.getTime() + "/" + appointment.getAddress());
                    }
                    System.out.println("Edit successful.");
                    return true; //update success
                }
                System.out.println("You have reached the maximum edit limit for this appointment");
                return false;
            }
            else if (isCurrentUserReceiver(apptId)){
                if (appointment.getUser2EditsCount() < 3) {
                    if (!appointment.getTradeType().equals("money")) {
                        asm.add(appointment.createReceiverSnapshot());
                    } else {
                        asm.add(appointment.createReceiverMoneySnapshot());
                    }
                    appointment.setTime(time);
                    appointment.setAddress(place);
                    appointment.incrementUserEditsCount(clientUserManager.getCurrentUser().getUserName());
                    appointment.setProposerConfirm(false);
                    appointment.setReceiverConfirmed(true);
                    if (!appointment.getTradeType().equals("money")) {
                        setChanged();
                        notifyObservers(appointment.getReceiver().getUserName()  + " edited Appointment ID: " + appointment.getId()+ " time/place to "
                                + appointment.getTime() + "/" + appointment.getAddress());
                    }
                    System.out.println("Edit successful.");
                    return true; //update success
                }
                System.out.println("You have reached the maximum edit limit for this appointment");
                return false;
            }
        }
        System.out.println("Invalid appointment id, please try again");
        return false; //apptId invalid, update unsuccessful
    }

    public boolean editMoneyAppointment(String apptId, double price, String time, String place) {
        if (editAppointment(apptId, time, place))
        {
            Appointment appointment = getPendingAppointmentById(apptId);
            appointment.setPrice(price);
            setChanged();
            notifyObservers(appointment.getReceiver().getUserName() + " edited Appointment ID: " + appointment.getId()+" time/place/price to "
                    + appointment.getTime() + "/" + appointment.getAddress()+"/"+appointment.getPrice());
            return true;
        } else {
            return false;
        }
    }

    /**
     * confirms appointment having appointment id as input
     * @param id
     * @return true if appointment confirmed, false otherwise
     */
    public boolean confirmAppointment(String id) {
        Appointment appointment= getPendingAppointmentById(id);
        if (appointment != null) {
            if (isCurrentUserProposer(id)) {
                appointment.setReceiverConfirmed(true);
                System.out.println("Confirm successful.");
                return true;
            } else if (isCurrentUserReceiver(id)) {
                appointment.setReceiverConfirmed(true);
                System.out.println("Confirm successful.");
                return true;
            }
        }
        return false;
    }

    /**
     * Declines appointment having appointmentId as input
     * Removes appointment from the available appointment list of proposer and receiver client users
     * @param id
     * @return true if appoint declined successfully and removed from the available appointment list,
     * false otherwise
     */
    public boolean declineAppointment(String id) {
        Appointment appointment = getPendingAppointmentById(id);
        if (appointment != null) {
            appointment.getReceiver().getPendingAppointments().getAppointmentList().remove(appointment);
            appointment.getProposer().getPendingAppointments().getAppointmentList().remove(appointment);
            return true;
        }
        return false;
    }

    /**
     * Confirms status of an appointment iff proposer and receiver confirms the status of appointment
     * Create transaction ticket for the appointment if status is true, else remove the appointment from the pending appointments
     * from proposer and receiver
     * @param id
     * @return true if both proposer and receiver confirms the appointment, false otherwise
     *
     */
    public boolean isBothConfirmed(String id) {
        Appointment appointment = getPendingAppointmentById(id);
        if (appointment != null) {
            if (appointment.getIsProposerConfirmed() && appointment.getIsReceiverConfirmed()) {
                appointment.getProposer().getPendingAppointments().getAppointmentList().remove(appointment);
                appointment.getProposer().addToConfirmedAppointment(appointment);
                appointment.getReceiver().getPendingAppointments().getAppointmentList().remove(appointment);
                appointment.getReceiver().addToConfirmedAppointment(appointment);
                switch (appointment.getTradeType()) {
                    case "burrow":
                        createBorrowTransactionTicket(appointment);
                        return true;
                    case "lend":
                        createLendTransactionTicket(appointment);
                        return true;
                    case "trade":
                        createTwoWayTransactionTicket(appointment);
                        return true;
                    case "money":
                        ClientUser proposer = clientUserManager.getUserByUsername(appointment.getProposer().getUserName());
                        ClientUser receiver = clientUserManager.getUserByUsername(appointment.getReceiver().getUserName());
                        proposer.setAccountBalance(proposer.getAccountBalance() - appointment.getPrice());
                        receiver.setAccountBalance(receiver.getAccountBalance() + appointment.getPrice());
                        createMoneyTransactionTicket(appointment);
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * Create two way transaction ticket for the given appointment
     * @param appointment
     */
    public void createTwoWayTransactionTicket(Appointment appointment){
        TransactionTicket transactionTicket =  new TransactionTicket(appointment.getProposerItem(), appointment.getReceiverItem(),
                appointment.getTime(), appointment.getProposer().getUserName(), appointment.getReceiver().getUserName(), appointment.getId(),"trade", appointment.getIsPermanent());
        appointment.getProposer().addToPendingTransactionTicket(transactionTicket);
        appointment.getReceiver().addToPendingTransactionTicket(transactionTicket);
    }

    public void createMoneyTransactionTicket(Appointment appointment) {
        TransactionTicket transactionTicket = new TransactionTicket(appointment.getProposerItem(),appointment.getTime(),appointment.getProposer().getUserName(),
                appointment.getReceiver().getUserName(),appointment.getId(),"money",appointment.getPrice());
        appointment.getProposer().addToPendingTransactionTicket(transactionTicket);
        appointment.getReceiver().addToPendingTransactionTicket(transactionTicket);
    }

    public void createBorrowTransactionTicket(Appointment appointment) {
        TransactionTicket transactionTicket =  new TransactionTicket(appointment.getProposerItem(),appointment.getTime(),appointment.getProposer().getUserName(),
                appointment.getReceiver().getUserName(),appointment.getId(),"burrow", appointment.getIsPermanent());
        appointment.getProposer().addToPendingTransactionTicket(transactionTicket);
        appointment.getReceiver().addToPendingTransactionTicket(transactionTicket);
    }

    public void createLendTransactionTicket(Appointment appointment) {
        TransactionTicket transactionTicket =  new TransactionTicket(appointment.getProposerItem(),appointment.getTime(),appointment.getProposer().getUserName(),
                appointment.getReceiver().getUserName(),appointment.getId(),"lend", appointment.getIsPermanent());
        appointment.getProposer().addToPendingTransactionTicket(transactionTicket);
        appointment.getReceiver().addToPendingTransactionTicket(transactionTicket);
    }


    public AppointmentList getConfirmedAppointmentListByUsername(String username){
        return clientUserManager.getUserByUsername(username).getConfirmedAppointments();
    }


    public AppointmentList getPendingAppointmentListByUsername(String username){
        return clientUserManager.getUserByUsername(username).getPendingAppointments();
    }



    /**
     * Check whether given id is matching with the available pending appointments of the current user
     * @param id
     * @return
     */
    public boolean matchPendingAppointmentById(String id) {
        for (Appointment app: clientUserManager.getCurrentUser().getPendingAppointments().getAppointmentList()) {
            if (app.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }



}
