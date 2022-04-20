package entities;

import java.io.Serializable;

public class ClientUserSnapshot implements Serializable {
    private ClientUser clientUser;
    private String home;
    private String accStatus;

    public ClientUserSnapshot(ClientUser clientUser, String home, String accStatus) {
        this.clientUser = clientUser;
        this.home = home;
        this.accStatus = accStatus;
    }

    public ClientUser getClientUser() {
        return clientUser;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public String getHome() {
        return home;
    }
}
