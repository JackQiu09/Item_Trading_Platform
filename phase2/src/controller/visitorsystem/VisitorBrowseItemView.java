package controller.visitorsystem;

import controller.SignUpSystem;
import entities.ClientUser;
import entities.Item;
import gateway.ClientUserReadWrite;
import usecases.ClientUserManager;
import view.component.Label;
import view.component.TextField;
import view.views.ItemListView;
import view.views.MultipleChoiceView;
import view.views.View;

import java.util.ArrayList;
import java.util.List;

import static gateway.FileReadAndWrite.CLIENT_USER_FILE;

/**
 * The class that extends the View class for visitor to browse items
 */
public class VisitorBrowseItemView extends View {

    ClientUserManager clientUserManager;
    private final String type;
    private final String name;
    private final ItemListView<ReadableItem> itemListView;

    /**
     * constructs the class
     * @param type type of the item visitor want to browse
     * @param name name of the item visitor is looking for
     */
    public VisitorBrowseItemView(String type, String name) {
        this.type = type;
        this.name = name;
        ClientUserReadWrite clientUserReadWrite = new ClientUserReadWrite();
        try {
            clientUserManager = clientUserReadWrite.createClientUserManagerFromFile(CLIENT_USER_FILE);
        } catch (Exception ignore) {

        }

        itemListView = new ItemListView<>(getItemList(), true);
    }

    public VisitorBrowseItemView() {
        this(null, null);
    }

    /**
     * override the view method to display to the visitor to looking for the item the vistor wants
     */
    @Override
    public void view() {
        component(
                new Label("Browsing database with visitor mode.")
        );
        component(
                new Label(() -> {
                    if (type == null)
                        return "Result: ";
                    StringBuilder sb = new StringBuilder();
                    sb.append("Searching for type: \"").append(type).append("\"");
                    if (name != null)
                        sb.append(", name: \"").append(name).append("\".");
                    return sb.toString();
                })
        );
        component(
                itemListView
        );
        component(
                MultipleChoiceView.getBuilder()
                        .addQuestion("s", "Enter 's' to sign up at any time!", this::signup)
                        .addQuestion("b", "Back to previous page.", this::back)
                        .build()
        );
        component(
                new Label(() -> {
                    if (itemListView.isEmpty())
                        return null;
                    return "Enter the number of an item to view details!";
                })
        );
        component(
                TextField.getBuilder()
                        .addValidator(input -> TextField.validate(input, "^[0-9]{1,2}$") && itemListView.contains(Integer.parseInt(input)))
                        .onFill(input -> redirect(new ItemDetailView(itemListView.getItem(Integer.parseInt(input)))))
                        .setError(input -> {
                            System.out.printf("\"%s\" is not a valid input.\n", input);
                            return true;
                        })
                .build()
        );
    }

    /**
     * get the list of the items
     * @return outputList List<ReadableItem>
     */
    private List<ReadableItem> getItemList() {
        List<ReadableItem> outputList = new ArrayList<>();
        for (ClientUser user : clientUserManager.getClientUserList().getAllClientUser()) {
            for (Item item : user.getInventory().getItems()) {
                if (name != null && type != null) {
                    if (item.getItemName().toLowerCase().matches(regExBuilder(name).toLowerCase()) && item.getType().equals(type)) {
                        outputList.add(new ReadableItem(item));
                    }
                } else if (type == null || item.getType().equals(type)) {
                    outputList.add(new ReadableItem(item));
                }
            }
        }
        return outputList;
    }

    /**
     * A class present the item
     */
    static class ReadableItem{
        private final Item item;

        /**
         * construct the class
         * @param item Item
         */
        public ReadableItem(Item item) {
            this.item = item;
        }

        /**
         * display the item into string
         * @return item.toString()
         */
        public String originalToString() {
            return item.toString();
        }

        /**
         * present the item
         * @return the string that present the item
         */
        @Override
        public String toString() {
            return String.format("item id:%s\n\ttype: %s,\tname: %s\n\tdescription: %s", item.getItemId(), item.getType(), item.getItemName(), item.getDescription());
        }
    }

    /**
     * the function to sign up
     */
    private void signup() {
        try {
            SignUpSystem signUpSystem = new SignUpSystem();
            signUpSystem.run();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    /**
     * present the name into regular expression
     * @param name string
     * @return string in the regEx form
     */
    private String regExBuilder(String name) {
        String[] slice = name.split("[ ]+");
        StringBuilder regEx = new StringBuilder();
        for (String piece : slice) {
            regEx.append("(?=.*\\b").append(piece).append("\\b)");
        }
        regEx.append(".*");
        return regEx.toString();
    }
}
