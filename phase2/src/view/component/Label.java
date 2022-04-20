package view.component;

import view.model.Binding;
import view.model.TextLayer;

/**
 * A class that implements TextLayer sets texts
 */
public class Label implements TextLayer {
    private String text;
    private Binding<String> bindingString;

    /**
     * constructor of the class with only one string
     * @param text string to label
     */
    public Label(String text) {
        if (text != null)
            this.text = text;
    }

    /**
     * constructor of the class with multiple string
     * @param stringBinding Binding<String>
     */
    public Label(Binding<String> stringBinding) {
        this.bindingString = stringBinding;
    }

    /**
     * The interface to build with text
     */
    public interface Builder {
        Builder setText(String text);
        Builder setText(Binding<String> text);
        Label build();
    }

    /**
     * override to get the Builder
     * @return the set Builder
     */
    public static Builder getBuilder() {
        return new LabelBuilder();
    }

    /**
     * override the print method to bring the texts
     */
    @Override
    public void print() {
        if (bindingString != null && bindingString.value() != null)
            System.out.println(bindingString.value());
        if (text != null)
            System.out.println(text);
    }
}
