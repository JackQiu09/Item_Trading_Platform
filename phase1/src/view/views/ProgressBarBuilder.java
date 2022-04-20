package view.views;

/**
 * A class implements the Builder interface of ProgressBarBuilder
 */
public class ProgressBarBuilder implements ProgressBar.Builder {

    private ProgressBar.DataSource dataSource;
    private int barLength = 70;

    /**
     * to set the data source
     * @param dataSource ProgressBar.DataSource
     * @return ProgressBarBuilder with the set data source
     */
    @Override
    public ProgressBarBuilder setDataSource(ProgressBar.DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    /**
     * to set bar length
     * @param barLength int
     * @return ProgressBarBuilder with the set bar length
     */
    @Override
    public ProgressBarBuilder setBarLength(int barLength) {
        this.barLength = barLength;
        return this;
    }

    /**
     * build the ProgressBarBuilder with set parameters
     * @return the set ProgressBarBuilder
     */
    @Override
    public ProgressBar build() {
        assert dataSource != null;
        return new ProgressBar(dataSource, barLength);
    }
}
