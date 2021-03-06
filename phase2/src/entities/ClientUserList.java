package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientUserList implements Serializable {
    private List<ClientUser> allClientUser;
    private List<ClientUser> activeUser;
    private List<ClientUser> pendingUser;
    private List<ClientUser> frozenUser;
    private List<ClientUser> invisibleUser;
    private List<ClientUser> pendingUnfreezeUser;


    public ClientUserList() {
        this.allClientUser = new ArrayList<>();
        this.activeUser = new ArrayList<>();
        this.pendingUser = new ArrayList<>();
        this.frozenUser = new ArrayList<>();
        this.invisibleUser = new ArrayList <>();
        this.pendingUnfreezeUser = new ArrayList<>();
    }

    public List<ClientUser> getAllClientUser() {
        return allClientUser;
    }

    public List<ClientUser> getActiveUser() {
        return activeUser;
    }

    public List<ClientUser> getFrozenUser() {
        return frozenUser;
    }

    public List<ClientUser> getPendingUser() {
        return pendingUser;
    }

    public List<ClientUser> getInvisibleUser(){return invisibleUser;}

    public List<ClientUser> getPendingUnfreezeUser(){return pendingUnfreezeUser;}

    public void addToAllClientUser(ClientUser clientUser) {
        allClientUser.add(clientUser);
    }

    public void addToActiveUser(ClientUser clientUser) {
        activeUser.add(clientUser);
    }

    public void addToPendingUser(ClientUser clientUser) {
        pendingUser.add(clientUser);
    }

    public void addToFrozenUser(ClientUser clientUser) {
        frozenUser.add(clientUser);
    }

    public void addToInvisibleUser(ClientUser clientUser){invisibleUser.add(clientUser);}

    public void addToPendingUnfreezeUser(ClientUser clientUser){pendingUnfreezeUser.add(clientUser);}

    public void removeFromActiveUser(ClientUser clientUser) {
        activeUser.remove(clientUser);
    }

    public void removeFromPendingUser(ClientUser clientUser) {
        pendingUser.remove(clientUser);
    }

    public void removeFromFrozenUser(ClientUser clientUser) {
        frozenUser.remove(clientUser);
     }

    public void removeFromPendingUnfreezeUser(ClientUser clientUser){pendingUnfreezeUser.remove(clientUser);}

    public void removeFromInvisibleUser(ClientUser clientUser){ invisibleUser.remove(clientUser);}

}
