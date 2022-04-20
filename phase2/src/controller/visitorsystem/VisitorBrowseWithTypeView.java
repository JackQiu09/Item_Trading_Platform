package controller.visitorsystem;

import view.component.Label;
import view.component.TextField;
import view.views.MultipleChoiceView;
import view.views.View;

/**
 * The class that extends View with input from the visitor to browser the inventory with specific item type
 */
public class VisitorBrowseWithTypeView extends View {

    /**
     * override view method to ask for the item type name the visitor wants to find
     */
    @Override
    public void view() {
        component(
                MultipleChoiceView.getBuilder()
                        .addQuestion("b", "Go back...", this::back)
                .build()
        );
        component(
                new Label("Enter item type name")
        );
        component(
                TextField.getBuilder()
                        .addValidator(input -> TextField.validate(input, "^[a-zA-Z0-9]{1,32}$"))
                        .onFill(input -> redirect(new VisitorBrowseItemView(input, null)))
                        .setError(comm -> {
                            System.out.printf("%s is not a validate input.", comm);
                            return true;
                        })
                .build()
        );
    }
}
