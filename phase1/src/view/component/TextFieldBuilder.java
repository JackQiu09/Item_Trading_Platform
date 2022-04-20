package view.component;

import view.model.CommandFunction;
import view.model.StringValidator;

/**
 * A class implements the interface TextField.Builder of TextFieldBuilder
 */
public class TextFieldBuilder implements TextField.Builder {
    private CommandFunction onFill;
    private StringValidator validator;
    private StringValidator error;

    /**
     * to set onFill
     * @param onFill CommandFunction to set
     * @return TextFieldBuilder with set onFill
     */
    @Override
    public TextField.Builder onFill(CommandFunction onFill) {
        this.onFill = onFill;
        return this;
    }

    /**
     * to set validator
     * @param validator StringValidator to set
     * @return TextFieldBuilder with set validator
     */
    @Override
    public TextField.Builder addValidator(StringValidator validator) {
        this.validator = validator;
        return this;
    }

    /**
     * to set error
     * @param error StringValidator to set
     * @return TextFieldBuilder with set error
     */
    @Override
    public TextField.Builder setError(StringValidator error) {
        this.error = error;
        return this;
    }

    /**
     * build the TextField with set parameters
     * @return the set TextField
     */
    @Override
    public TextField build() {
        assert onFill != null;
        assert validator != null;
        return new TextField(onFill, validator, error);
    }
}
