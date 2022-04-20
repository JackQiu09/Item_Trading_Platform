package usecases;


import entities.Appointment;
import entities.AppointmentList;
import entities.*;
import gateway.ThresholdReadWrite;
import static gateway.FileReadAndWrite.*;

import entities.TransactionTicket;

public class AppointmentManager {
    private ClientUserManager clientUserManager;
    private ThresholdManager thresholdmanager;
    private AppointmentList appointmentList;
    private ThresholdReadWrite thresholdReadWrite = new ThresholdReadWrite();
    private ThresholdManager thresholdManager = thresholdReadWrite.createThresholdManagerFromFile(THRESHOLD_MANAGER_FILE);

    public AppointmentManager(ClientUserManager clientUserManager) throws ClassNotFoundException {
        this.clientUserManager = clientUserManager;
    }

    // changing Time/address/Time and Address
    // For changing both Time and Address, use format: "new time,new address"
    // userId is the person that is changing
    private boolean isCurrentUserProposer(String id) {
        return getPendingAppointmentById(id).getProposer().equals(clientUserManager.getCurrentUser().getUserName());
    }

    private boolean isCurrentUserReceiver(String id) {
        return getPendingAppointmentById(id).getReceiver().equals(clientUserManager.getCurrentUser().getUserName());
    }

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

    public boolean editAppointment(String apptId, String time, String place) {
        Appointment appointment= getPendingAppointmentById(apptId);
        if (appointment != null) {
            //user exceeded maximum edits
            if (isCurrentUserProposer(apptId)) {
                if (appointment.getUser1EditsCount() < 3) {
                    appointment.setTime(time);
                    appointment.setAddress(place);
                    appointment.incrementUserEditsCount(clientUserManager.getCurrentUser().getUserName());
                    appointment.setProposerConfirm(true);
                    appointment.setReceiverConfirmed(false);
                    System.out.println("Edit successful.");
                    return true; //update success
                }
            }
            else if (isCurrentUserReceiver(apptId)){
                if (appointment.getUser2EditsCount() < 3) {
                    appointment.setTime(time);
                    appointment.setAddress(place);
                    appointment.incrementUserEditsCount(clientUserManager.getCurrentUser().getUserName());
                    appointment.setProposerConfirm(false);
                    appointment.setReceiverConfirmed(true);
                    System.out.println("Edit successful.");
                    return true; //update success
                }
            }
        }
        return false; //apptId invalid, update unsuccessful
    }

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

    public boolean declineAppointment(String id) {
        Appointment appointment = getPendingAppointmentById(id);
        if (appointment != null) {
            getPendingAppointmentListByUsername(appointment.getReceiver()).getAppointmentList().remove(appointment);
            getPendingAppointmentListByUsername(appointment.getProposer()).getAppointmentList().remove(appointment);
            return true;
        }
        return false;
    }

    public boolean isBothConfirmed(String id) {
        Appointment appointment = getPendingAppointmentById(id);
        if (appointment != null) {
            if (appointment.getIsProposerConfirmed() && appointment.getIsReceiverConfirmed()) {
                getPendingAppointmentListByUsername(appointment.getProposer()).getAppointmentList().remove(appointment);
                clientUserManager.getUserByUsername(appointment.getProposer()).addToConfirmedAppointment(appointment);
                getPendingAppointmentListByUsername(appointment.getReceiver()).getAppointmentList().remove(appointment);
                clientUserManager.getUserByUsername(appointment.getReceiver()).addToConfirmedAppointment(appointment);
                if (appointment.isOneWay()) {
                    createOneWayTransactionTicket(appointment);
                    return true;
                } else if (!appointment.isOneWay()) {
                    createTwoWayTransactionTicket(appointment);
                    return true;
                }
            }
        }
        return false;
    }


    public void createTwoWayTransactionTicket(Appointment appointment){
        TransactionTicket transactionTicket =  new TransactionTicket(appointment.getItem1(), appointment.getItem2(),
                appointment.getTime(), appointment.getProposer(), appointment.getReceiver(), appointment.getId(),appointment.isOneWay());
        clientUserManager.getUserByUsername(appointment.getProposer()).addToPendingTransactionTicket(transactionTicket);
        clientUserManager.getUserByUsername(appointment.getReceiver()).addToPendingTransactionTicket(transactionTicket);

    }

    public void createOneWayTransactionTicket(Appointment appointment) {
        TransactionTicket transactionTicket =  new TransactionTicket(appointment.getItem1(),appointment.getTime(),appointment.getProposer(),
                appointment.getReceiver(),appointment.getId(),appointment.isOneWay());
        clientUserManager.getUserByUsername(appointment.getProposer()).addToPendingTransactionTicket(transactionTicket);
        clientUserManager.getUserByUsername(appointment.getReceiver()).addToPendingTransactionTicket(transactionTicket);
    }


    public AppointmentList getConfirmedAppointmentListByUsername(String username){
        return clientUserManager.getUserByUsername(username).getConfirmedAppointments();
    }


    public AppointmentList getPendingAppointmentListByUsername(String username){
        return clientUserManager.getUserByUsername(username).getPendingAppointments();
    }




    public boolean matchPendingAppointmentById(String id) {
        for (Appointment app: clientUserManager.getCurrentUser().getPendingAppointments().getAppointmentList()) {
            if (app.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }



}
