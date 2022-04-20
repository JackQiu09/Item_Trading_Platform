package usecases;

import entities.ClientUser;
import entities.Item;
import entities.ItemList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemListManager {
    private ItemList itemList;
    private ClientUserManager clientUserManager;

    public ItemListManager(ClientUserManager clientUserManager){
        this.clientUserManager = clientUserManager;
    }

    public ItemList getItemList(){
        return this.itemList;
    }


    private void addItemToPendingList(Item item) {
        clientUserManager.getCurrentUser().addToPendingItem(item);
    }

    public void createAnItem(String itemName, String description, String type) {
        addItemToPendingList(new Item(itemName, description, clientUserManager.getCurrentUser().getUserName(), type));
    }

    /**
     * Checks if any item is pending for any of the client user
     * @return true if no item is pending, false otherwise
     */
    public boolean showAllPendingItem() {
        boolean notEmpty = false;
        for (ClientUser clientUser: clientUserManager.getClientUserList().getAllClientUser()) {
            if (!clientUser.getPendingItemList().isEmpty()) {
                System.out.println(clientUser.getPendingItemList().toString());
                notEmpty = true;
            }
        }
        if (notEmpty) {
            return true;
        } else {
            System.out.println("No Pending Item");
            return false;
        }
    }

    /**
     * Approve item having provided id
     * @param id
     * @return true if approval successful, false otherwise
     */
    public boolean approveItem(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item: clientUser.getPendingItemList().getItems()) {
                if (item.getItemId().equals(id)) {
                    clientUser.addToInventory(item);
                    clientUser.getPendingItemList().getItems().remove(item);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * prints inventory of all the client users in the system
     */
    public void showAllUserInventories() {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            if (clientUser.getHome().equals(clientUserManager.getCurrentUser().getHome()) && clientUser != clientUserManager.getCurrentUser() || clientUserManager.getCurrentUser().getHome().equals("")) {
                System.out.println(clientUser.toString());
                System.out.println(clientUser.getInventory().toString());
            }
        }
    }

    public void showAllUserWishList() {
        for (ClientUser clientUser : clientUserManager.getClientUserList().getActiveUser()) {
            if (clientUser != clientUserManager.getCurrentUser()) {
                System.out.println(clientUser.toString());
                System.out.println(clientUser.getWishList().toString());
            }
        }
    }

    /**
     * Allows to find item using id
     * @param id
     * @return Item if item found with any of the active user, null otherwise
     */
    public Item findItemByItemId(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Return the owner of the item using item id
     * @param id
     * @return ClientUser if item is with clientUser, null otherwise
     */
    public ClientUser findUserByItemId(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item : clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    return clientUser;
                }
            }
        }
        return null;
    }

    /**
     * Adds item to wishlist of current clientUser
     * @param id
     * @return true if adding item to wishlist successful, false otherwise
     */
    public boolean addItemToWishList(String id) {
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) {
            for (Item item: clientUser.getInventory().getItems()) {
                if (item.getItemId().equals(id)) {
                    ClientUser userToAdd = clientUserManager.getCurrentUser();
                    //System.out.println(userToAdd.toString());
                    userToAdd.addToWishList(item);
                    //System.out.println("ADDED:" + userToAdd.getWishList().toString());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Provide itemList owned by User
     * @param username
     * @return ItemList owned by clientuser
     */
    public ItemList getItemListByUser(String username){
        ItemList itemList = new ItemList();
        for(Item item: this.itemList.getItems()){
            if(item.getOwnerName().equalsIgnoreCase(username)){
                itemList.getItems().add(item);
            }
        }
        return itemList;
    }


    public ArrayList<Item> getItemsByType(String type){
        ArrayList<Item> typeList = new ArrayList<>();
        for (ClientUser clientUser: clientUserManager.getClientUserList().getActiveUser()) { //get active user
            if (clientUser.getHome().equals(clientUserManager.getCurrentUser().getHome()) && clientUser != clientUserManager.getCurrentUser() || clientUserManager.getCurrentUser().getHome().equals("")) {
                ItemList currentUserInventory = clientUser.getInventory();
                for (Item item: currentUserInventory.getItems()){
                    if (item.getType().equalsIgnoreCase(type)) {
                        typeList.add(item);
                    }
                }
            }
        }
        return typeList;
    }


}
