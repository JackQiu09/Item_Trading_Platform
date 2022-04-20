package view.component;

import view.model.InputLayer;
import view.model.StringValidator;
import view.model.CommandFunction;

import java.util.Set;

/**
 * A class that implements InputLayer about selection
 */
public class SelectionField implements InputLayer {

    /**
     * An interface with the method to get the Selections
     */
    public interface DataSource {
        Set<String> getSelections();
    }

    private final DataSource dataSource;
    private final CommandFunction onFill;
    private final StringValidator error;

    /**
     * the constructor of the class
     * @param dataSource DataSource
     * @param onFill CommandFunction
     * @param error StringValidator
     */
    SelectionField(DataSource dataSource, CommandFunction onFill, StringValidator error) {
        this.dataSource = dataSource;
        this.onFill = onFill;
        this.error = error;
    }

    /**
     * The interface of Builder Design Pattern
     */
    public interface Builder {
        Builder setDataSource(DataSource dataSource);
        Builder onFill(CommandFunction onFill);
        Builder setError(StringValidator error);
        SelectionField build();
    }

    /**
     * override to get the Builder
     * @return the set Builder
     */
    public static Builder getBuilder() {
        return new SelectionFieldBuilder();
    }

    /**
     * override to run if the string is contained
     * @param comm string to see if contained in dataSource
     * @return boolean
     */
    @Override
    public boolean read(String comm) {
        if (dataSource.getSelections().contains(comm)) {
            onFill.run(comm);
            return true;
        } else {
            if (error != null) {
                return error.validate(comm);
            }
            return false;
        }
    }
}
