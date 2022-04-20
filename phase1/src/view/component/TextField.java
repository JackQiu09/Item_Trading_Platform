package view.component;

import view.model.InputLayer;
import view.model.StringValidator;
import view.model.CommandFunction;

import java.util.regex.Pattern;

/**
 * A class that implements InputLayer with text
 */
public class TextField implements InputLayer {
    private final CommandFunction onFill;
    private final StringValidator validator;
    private final StringValidator error;


    /**
     * the constructor of the class
     * @param onFill CommandFunction
     * @param validator StringValidator
     * @param error StringValidator
     */
    TextField(CommandFunction onFill, StringValidator validator, StringValidator error) {
        this.onFill = onFill;
        this.validator = validator;
        this.error = error;
    }

    /**
     * The interface of Builder Design Pattern
     */
    public interface Builder {
        Builder onFill(CommandFunction onFill);
        Builder addValidator(StringValidator validator);
        Builder setError(StringValidator error);
        TextField build();
    }

    /**
     * override to get the Builder
     * @return the set Builder
     */
    public static Builder getBuilder() {
        return new TextFieldBuilder();
    }

    /**
     * override the method to run the comm if validated
     * @param comm String to run
     * @return boolean
     */
    @Override
    public boolean read(String comm) {
        if (validator == null || validator.validate(comm)) {
            onFill.run(comm);
            return true;
        } else {
            if (error != null) {
                return error.validate(comm);
            }
            return false;
        }
    }

    /**
     * validate if the input matches the regEx
     * @param input string to compart
     * @param regEx string of the regular expression
     * @return boolean
     */
    public static boolean validate(String input, String regEx) {
        if (input == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regEx);
        return pattern.matcher(input).matches();
    }
}
