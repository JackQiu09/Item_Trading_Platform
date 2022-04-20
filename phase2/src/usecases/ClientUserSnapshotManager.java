package usecases;

import entities.ClientUserSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientUserSnapshotManager implements Serializable {
    private List<ClientUserSnapshot> clientUserSnapshots;
    private ClientUserManager clientUserManager;

    public ClientUserSnapshotManager(ClientUserManager clientUserManager) {
        this.clientUserManager = clientUserManager;
        this.clientUserSnapshots = new ArrayList<>();
    }

    public void add(ClientUserSnapshot appointmentSnapshot) {
        clientUserSnapshots.add(appointmentSnapshot);
    }

    public ClientUserSnapshot get(int index) {
        return clientUserSnapshots.get(index);
    }

    public List<ClientUserSnapshot> getClientUserSnapshots() {return clientUserSnapshots;}

    public void removeFromList(int index) {
        clientUserSnapshots.remove(index);
    }

    public boolean showAll() {
        if (!clientUserSnapshots.isEmpty()) {
            int i = 0;
            while (i<clientUserSnapshots.size()) {
                System.out.println(("Action id: " +i +", " + clientUserSnapshots.get(i).getClientUser().getUserName() +
                        " changed home to " +clientUserManager.getUserByUsername(clientUserSnapshots.get(i).getClientUser().getUserName()).getHome()));
                i++;
            }
            return true;
        }
        return false;
    }

}
