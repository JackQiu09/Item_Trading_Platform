package view.views;

import view.component.TextField;
import view.model.StringValidator;

import java.util.ArrayList;

/**
 * A class implements the Builder interface of ShortAnswerViewBuilder
 */
public class ShortAnswerViewBuilder implements ShortAnswerView.Builder {
    private ShortAnswerView.ShortAnswerOnSuccess onSuccess;
    private final ArrayList<String> questions = new ArrayList<>();
    private final ArrayList<StringValidator> validators = new ArrayList<>();
    private StringValidator error;
    private boolean displayProgressBar = false;

    /**
     * override to set the onSuccess
     * @param success ShortAnswerView.ShortAnswerOnSuccess
     * @return ShortAnswerViewBuilder with set success
     */
    @Override
    public ShortAnswerViewBuilder onSuccess(ShortAnswerView.ShortAnswerOnSuccess success) {
        this.onSuccess = success;
        return this;
    }

    /**
     * override to set error
     * @param error StringValidator
     * @return ShortAnswerViewBuilder with set error
     */
    @Override
    public ShortAnswerViewBuilder setError(StringValidator error) {
        this.error = error;
        return this;
    }

    /**
     * override to add question and regEx
     * @param question string
     * @param validationRegEx string
     * @return ShortAnswerViewBuilder with set question and regEx
     */
    @Override
    public ShortAnswerViewBuilder addQuestion(String question, String validationRegEx) {
        questions.add(question);
        validators.add((comm) -> TextField.validate(comm, validationRegEx));
        return this;
    }

    /**
     * override to add question and StringValidator
     * @param question String
     * @param validation StringValidator
     * @return ShortAnswerViewBuilder with set question and StringValidator
     */
    @Override
    public ShortAnswerViewBuilder addQuestion(String question, StringValidator validation) {
        questions.add(question);
        validators.add(validation);
        return this;
    }

    /**
     * override to display the progress bar
     * @param displayProgressBar boolean
     * @return ShortAnswerViewBuilder with set displayProgressBar
     */
    @Override
    public ShortAnswerViewBuilder showProgressBar(boolean displayProgressBar) {
        this.displayProgressBar = displayProgressBar;
        return this;
    }

    /**
     * build the ShortAnswerView with set parameters
     * @return the set ShortAnswerView
     */
    @Override
    public ShortAnswerView build() {
        assert onSuccess != null;
        assert questions.size() > 0;
        assert validators.size() > 0;
        return new ShortAnswerView(questions, validators, error, onSuccess, displayProgressBar);
    }
}
