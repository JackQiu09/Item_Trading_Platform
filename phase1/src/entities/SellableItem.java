package entities;

public class SellableItem extends Item {

    private double price;

    /**
     * itemID, itemName, description, ownerName are required to create an instance of item.
     *
     * @param itemName
     * @param description
     * @param ownerName
     * @param type
     */
    public SellableItem(String itemName, String description, String ownerName, String type, double price) {
        super(itemName, description, ownerName, type);
        this.price = price;
    }

}
