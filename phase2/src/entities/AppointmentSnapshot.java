package entities;

import java.io.Serializable;

public class AppointmentSnapshot implements Serializable {
    private Appointment appointment;
    private String time;
    private String place;
    private double price;
    private int user1Edit;
    private int user2Edit;
    private boolean isProposerConfirmed;
    private boolean isReceiverConfirmed;
    private String username;

    public AppointmentSnapshot(Appointment appointment, String username, String time, String place, int user1Edit, int user2Edit,
                               boolean isProposerConfirmed, boolean isReceiverConfirmed) {
        this.appointment = appointment;
        this.username = username;
        this.time = time;
        this.place = place;
        this.user1Edit = user1Edit;
        this.user2Edit = user2Edit;
        this.isProposerConfirmed = isProposerConfirmed;
        this.isReceiverConfirmed = isReceiverConfirmed;
    }

    public AppointmentSnapshot(Appointment appointment, String username, String time, String place, int user1Edit, int user2Edit,
                               boolean isProposerConfirmed, boolean isReceiverConfirmed, double price) {
        this.appointment = appointment;
        this.username = username;
        this.time = time;
        this.place = place;
        this.user1Edit = user1Edit;
        this.user2Edit = user2Edit;
        this.isProposerConfirmed = isProposerConfirmed;
        this.isReceiverConfirmed = isReceiverConfirmed;
        this.price = price;
    }

    public String getUsername() {return username;}
    public Appointment getAppointment() {return appointment;}
    public String getTime() {
        return time;
    }
    public String getPlace() {
        return place;
    }
    public double getPrice() {
        return price;
    }
    public int getUser1Edit() {return user1Edit;}
    public int getUser2Edit() {return user2Edit;}
    public boolean getIsProposerConfirmed() {return isProposerConfirmed;}
    public boolean getIsReceiverConfirmed() {return isReceiverConfirmed;}


}
