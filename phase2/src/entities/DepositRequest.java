package entities;

import java.util.UUID;

public class DepositRequest {
    private ClientUser user;
    private double depositAmount;
    private UUID id = UUID.randomUUID();


    public DepositRequest(ClientUser user, double depositAmount)
    {
        this.user = user;
        this.depositAmount = depositAmount;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public ClientUser getUser() {
        return user;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DepositRequest{" +
                "user=" + user +
                ", depositAmount=" + depositAmount +
                ", id=" + id +
                '}';
    }
}
