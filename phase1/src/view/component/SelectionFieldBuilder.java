package view.component;

import view.model.CommandFunction;
import view.model.StringValidator;

/**
 * A class implements the Builder interface of SelectionFieldBuilder
 */
public class SelectionFieldBuilder implements SelectionField.Builder {
    private SelectionField.DataSource dataSource;
    private CommandFunction onFill;
    private StringValidator error;

    /**
     * to set the dataSource
     * @param dataSource SelectionField.DataSource
     * @return SelectionFieldBuilder with the set dataSource
     */
    @Override
    public SelectionField.Builder setDataSource(SelectionField.DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    /**
     * to set the onFill
     * @param onFill CommandFunction
     * @return SelectionFieldBuilder with set onFill
     */
    @Override
    public SelectionField.Builder onFill(CommandFunction onFill) {
        this.onFill = onFill;
        return this;
    }

    /**
     * to set the error
     * @param error StringValidator
     * @return SelectionFieldBuilder with set error
     */
    @Override
    public SelectionField.Builder setError(StringValidator error) {
        this.error = error;
        return this;
    }

    /**
     * build the SelectionField with set parameters
     * @return the set SelectionField
     */
    @Override
    public SelectionField build() {
        assert onFill != null;
        assert dataSource != null;
        return new SelectionField(dataSource, onFill, error);
    }
}
