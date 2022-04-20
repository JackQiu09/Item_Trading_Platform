package view.views;

import view.component.Label;
import view.component.TextField;
import view.model.StringValidator;

import java.util.ArrayList;

/**
 * The class that inherits View and implements ProgressBar.DataSource for the short answer view
 */
public class ShortAnswerView extends View implements ProgressBar.DataSource {
    private final ShortAnswerOnSuccess onSuccess;
    private final ArrayList<String> questions;
    private final ArrayList<StringValidator> validators;
    private final ArrayList<String> input = new ArrayList<>();
    private final StringValidator error;
    private final boolean displayProgressBar;

    /**
     * the constructor of the class
     * @param questions ArrayList<String>
     * @param validators ArrayList<StringValidator>
     * @param error StringValidator
     * @param onSuccess ShortAnswerOnSuccess
     * @param displayProgressBar boolean
     */
    ShortAnswerView(ArrayList<String> questions, ArrayList<StringValidator> validators, StringValidator error, ShortAnswerOnSuccess onSuccess, boolean displayProgressBar) {
        this.questions = questions;
        this.validators = validators;
        this.error =error;
        this.onSuccess = onSuccess;
        this.displayProgressBar = displayProgressBar;
        view();
    }

    /**
     * The interface to build for ShortAnswerView
     */
    public interface Builder {
        Builder addQuestion(String question, String validationRegEx);
        Builder addQuestion(String question, StringValidator stringValidator);
        Builder setError(StringValidator error);
        Builder onSuccess(ShortAnswerOnSuccess onSuccess);
        Builder showProgressBar(boolean show);
        ShortAnswerView build();
    }

    /**
     * override to get the Builder
     * @return the set Builder
     */
    public static Builder getBuilder() {
        return new ShortAnswerViewBuilder();
    }

    /**
     * override to get the current input length
     * @return int
     */
    @Override
    public int getCurrent() {
        return input.size();
    }

    /**
     * override to get the total length
     * @return int
     */
    @Override
    public int getTotal() {
        return questions.size();
    }

    /**
     * override to enable
     * @return boolean
     */
    @Override
    public boolean enable() {
        return displayProgressBar;
    }

    /**
     * A interface with method onSuccess
     */
    public interface ShortAnswerOnSuccess {
        void onSuccess(String[] input);
    }

    /**
     * override to view
     */
    @Override
    public void view() {
        component(
                ProgressBar.getBuilder()
                        .setBarLength(50)
                        .setDataSource(this)
                        .build()
        );
        component(
                Label.getBuilder()
                        .setText(this::printQuestion)
                        .build()
        );
        component(
                TextField.getBuilder()
                        .addValidator(this::validate)
                        .onFill(this::getResult)
                        .setError(this::error)
                        .build()
        );
    }

    /**
     * get the corresponding question
     * @return the question
     */
    private String printQuestion() {
        return questions.get(input.size());
    }

    /**
     * validate the command
     * @param comm string command
     * @return the validator by the command
     */
    private boolean validate(String comm) {
        return validators.get(input.size()).validate(comm);
    }

    /**
     * get the result
     * @param result String command
     */
    private void getResult(String result) {
        input.add(result);
        if (input.size() == questions.size()) {
            onSuccess.onSuccess(input.toArray(new String[0]));
            input.clear();
        }
    }

    /**
     * check if the error
     * @param comm String command
     * @return boolean
     */
    private boolean error(String comm) {
        if (error!=null) {
            error.validate(comm);
            return true;
        }
        return false;
    }

}
