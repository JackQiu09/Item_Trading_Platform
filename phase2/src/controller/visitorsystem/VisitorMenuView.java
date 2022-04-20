package controller.visitorsystem;

import view.component.Label;
import view.views.MultipleChoiceView;
import view.views.View;

/**
 * The class that extends View display the menu to the visitor
 */
public class VisitorMenuView extends View {

    /**
     * override the view method to display the menu to the visitor
     */
    @Override
    public void view() {
        component(
                new Label("Welcome to our visitor system.")
        );
        component(
                MultipleChoiceView.getBuilder()
                        .addQuestion("1", "Browse our amazing inventory!", () -> redirect(new VisitorBrowseItemView()))
                        .addQuestion("2", "Browser our inventory with specific item type!", () -> redirect(new VisitorBrowseWithTypeView()))
                        .addQuestion("3", "Or search our database with a name!", () -> redirect(new VisitorSearchItemView()))
                        .addQuestion("b", "Go back.", this::back)
                .build()
        );
        component(
                new Label("Build with ❤️")
        );
    }
}
