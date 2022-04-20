package view.component;

import view.component.Label;
import view.model.Binding;

/**
 * A class implements the Builder interface of Label
 */
public class LabelBuilder implements Label.Builder {
    Label label = new Label("");

    /**
     * to set text when only one text
     * @param text string to set
     * @return label with text
     */
    @Override
    public Label.Builder setText(String text) {
        label = new Label(text);
        return this;
    }

    /**
     * to set text with multiple text
     * @param text Binding<String>
     * @return label with text
     */
    @Override
    public Label.Builder setText(Binding<String> text) {
        label = new Label(text);
        return this;
    }

    /**
     * build the Label with set parameters
     * @return the set Label
     */
    @Override
    public Label build() {
        return label;
    }
}
