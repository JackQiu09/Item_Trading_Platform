package view.views;

import view.model.Binding;
import view.model.DescribableFunction;
import view.model.Function;
import view.model.StringValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class implements the Builder interface of MultipleChoiceView
 */
public class MultipleChoiceViewBuilder implements MultipleChoiceView.Builder {
    private final List<String> keyList = new ArrayList<>();
    private final Map<String, DescribableFunction> commandMap = new HashMap<>();
    private StringValidator error;
    private boolean hideKey = false;

    /**
     * override to set the error
     * @param error StringValidator
     * @return MultipleChoiceView with set error
     */
    @Override
    public MultipleChoiceViewBuilder setError(StringValidator error) {
        this.error = error;
        return this;
    }

    /**
     * override to add question with one question
     * @param choice string
     * @param question string
     * @param onSubmit Function
     * @return MultipleChoiceView with added error
     */
    @Override
    public MultipleChoiceViewBuilder addQuestion(String choice, String question, Function onSubmit) {
        return addQuestion(choice, () -> question, onSubmit);
    }

    /**
     * override to add question with binding string
     * @param choice String
     * @param bindingString Binding<String>
     * @param onSubmit Function
     * @return MultipleChoiceView with added error
     */
    @Override
    public MultipleChoiceViewBuilder addQuestion(String choice, Binding<String> bindingString, Function onSubmit) {
        commandMap.put(choice, new DescribableFunction() {
            @Override
            public String value() {
                return bindingString.value();
            }

            @Override
            public void apply() {
                onSubmit.apply();
            }
        });
        keyList.add(choice);
        return this;
    }

    /**
     * override to set the hide key
     * @param hide boolean
     * @return MultipleChoiceView with set hide key
     */
    @Override
    public MultipleChoiceView.Builder hideKey(boolean hide) {
        this.hideKey = hide;
        return this;
    }

    /**
     * build the MultipleChoiceView with set parameters
     * @return the set MultipleChoiceView
     */
    @Override
    public MultipleChoiceView build() {
        assert keyList.size() > 0;
        return new MultipleChoiceView(keyList, commandMap, error, hideKey);
    }
}
