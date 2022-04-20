package usecases;

import java.io.Serializable;
import java.util.*;

import entities.AppointmentSnapshot;

public class AppointmentSnapshotManager implements Observer, Serializable {
    private List<AppointmentSnapshot> appointmentSnapshots;
    private List<String> notifications;

    public AppointmentSnapshotManager() {
        this.appointmentSnapshots = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public void add(AppointmentSnapshot appointmentSnapshot) {
        appointmentSnapshots.add(appointmentSnapshot);
    }

    public AppointmentSnapshot get(int index) {
        return appointmentSnapshots.get(index);
    }

    public void removeFromList(int index) {
        appointmentSnapshots.remove(index);
        notifications.remove(index);
    }

    public boolean displayNotification() {
        if (!notifications.isEmpty()) {
            int i = 0;
            while (i < notifications.size()) {
                System.out.println("Action Id: " + i + "\n" + notifications.get(i));
                i++;
            }
            return true;
        }
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        notifications.add(arg.toString());
    }


}
