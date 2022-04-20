package controller.visitorsystem;

import view.component.Label;
import view.component.TextField;
import view.views.MultipleChoiceView;
import view.views.ShortAnswerView;
import view.views.View;

/**
 * The class that extends View provided to the visitor to search the database with a name
 */
public class VisitorSearchItemView extends View {
    private final String validate = "^[a-zA-Z0-9 ]{1,32}$";

    /**
     * override the view method for the visitor to find the item with the item type
     */
    @Override
    public void view() {
        component(
                MultipleChoiceView.getBuilder()
                        .addQuestion("b", "Go back...", this::back)
                        .build()
        );
        component(
                ShortAnswerView.getBuilder()
                        .addQuestion("Please enter item type.", comm -> {
                            if (TextField.validate(comm, validate)) {
                                component(
                                        new Label("Item Type entered: " + comm)
                                );
                                component(
                                        new Label("Sweet! Now we only need a name...: ")
                                );
                                return true;
                            }
                            return false;
                        })
                        .addQuestion(null, validate)
                        .onSuccess(input -> redirect(new VisitorBrowseItemView(input[0], input[1])))
                .build()
        );
    }
}
