package controller.visitorsystem;

import view.component.Label;
import view.views.MultipleChoiceView;
import view.views.View;

/**
 * The class that extends View presents the item
 */
public class ItemDetailView extends View {
    VisitorBrowseItemView.ReadableItem item;

    /**
     * constructor
     * @param item VisitorBrowseItemView.ReadableItem
     */
    public ItemDetailView(VisitorBrowseItemView.ReadableItem item) {
        this.item = item;
    }

    /**
     * override the view method to display the detail of the item
     */
    @Override
    public void view() {
        component(
                new Label("Item detail")
        );
        component(
                new Label(item.originalToString())
        );
        component(
                MultipleChoiceView.getBuilder()
                        .addQuestion("b", "Back to previous page.", this::back)
                        .build()
        );
    }
}
