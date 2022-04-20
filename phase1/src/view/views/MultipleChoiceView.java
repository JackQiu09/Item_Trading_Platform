package view.views;

import view.component.Label;
import view.component.SelectionField;
import view.model.Binding;
import view.model.DescribableFunction;
import view.model.Function;
import view.model.StringValidator;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The class that extends View to view with multiple choice answer
 */
public class MultipleChoiceView extends View {
    private final List<String> keyList;
    private final Map<String, DescribableFunction> commandMap;
    private final StringValidator error;
    private final boolean hideCommandKey;

    /**
     * the constructor of the class
     * @param keyList List<String>
     * @param commandMap Map<String, DescribableFunction>
     * @param error StringValidator
     * @param hideCommandKey boolean
     */
    MultipleChoiceView(List<String> keyList, Map<String, DescribableFunction> commandMap, StringValidator error, boolean hideCommandKey) {
        this.keyList = keyList;
        this.commandMap = commandMap;
        this.error = error;
        this.hideCommandKey = hideCommandKey;
        view();
    }

    /**
     * The interface to build for MultipleChoiceView
     */
    public interface Builder {
        Builder setError(StringValidator error);

        Builder addQuestion(String choice, String question, Function onSubmit);

        Builder addQuestion(String choice, Binding<String> bindingString, Function onSubmit);

        Builder hideKey(boolean hide);

        MultipleChoiceView build();
    }

    /**
     * override to get the Builder
     * @return the set Builder
     */
    public static Builder getBuilder() {
        return new MultipleChoiceViewBuilder();
    }

    /**
     * override to view
     */
    @Override
    public void view() {
        component(Label.getBuilder().setText(this::getPrompt).build());
        component(SelectionField.getBuilder()
                .setDataSource(this::getKeySet)
                .onFill(this::command)
                .setError(this::error)
                .build()
        );
    }

    /**
     * apply the comm into commandMap
     * @param comm string command
     */
    void command(String comm) {
        commandMap.get(comm).apply();
    }

    /**
     * override to set the error
     * @param comm string command
     * @return boolean
     */
    private boolean error(String comm) {
        if (error!=null) {
            error.validate(comm);
            return true;
        }
        return false;
    }

    /**
     * return the key set
     * @return Set<String>
     */
    Set<String> getKeySet() {
        return commandMap.keySet();
    }

    /**
     * get StringBuilder() into string
     * @return key set as string
     */
    String getPrompt() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        if (keyList != null)
            for (String key : keyList) {
                DescribableFunction value = commandMap.get(key);
                if (value.value() != null) {
                    if (!isFirst) {
                        sb.append("\n");
                    } else {
                        isFirst = false;
                    }
                    if (!hideCommandKey) {
                        sb.append(key).append(": ");
                    }
                    sb.append(value.value());
                }
            }
        return sb.toString().equals("") ? null : sb.toString();
    }
}
