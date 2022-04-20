package view.views;

import view.component.Label;

/**
 * A class that extends View for the progress
 */
public class ProgressBar extends View {
    public interface DataSource {
        int getCurrent();
        int getTotal();
        boolean enable();
    }

    private int barLength;
    private DataSource dataSource;

    /**
     * the constructor of the class
     * @param dataSource DataSource
     * @param barLength length
     */
    ProgressBar(DataSource dataSource, int barLength) {
        this.dataSource = dataSource;
        this.barLength = barLength;
        view();
    }

    /**
     * The interface to build for ProgressBar
     */
    public interface Builder {
        Builder setDataSource(DataSource dataSource);
        Builder setBarLength(int barLength);
        ProgressBar build();
    }

    /**
     * override to get the Builder
     * @return the set Builder
     */
    public static Builder getBuilder() {
        return new ProgressBarBuilder();
    }

    /**
     * get the progress bar as string
     * @param current int
     * @param total int
     * @param barLength int
     * @return class into string
     */
    private String getProgressBar(int current, int total, int barLength) {
        assert barLength > 1;
        assert current <= total;
        StringBuilder progressBar = new StringBuilder("[");
        double percentage = (double)current / (double) total;
        int displayLen = (int)(percentage * (double) barLength);
        for (int i=0; i<barLength; i++) {
            if (i < displayLen) {
                progressBar.append("=");
            } else if (i == displayLen) {
                progressBar.append(">");
            } else {
                progressBar.append(" ");
            }
        }
        progressBar.append("] ");
        progressBar.append(String.format("%.2f", percentage * 100)).append("%");
        progressBar.append("\r");
        return progressBar.toString();
    }

    /**
     * override to view with label
     */
    @Override
    public void view() {
        component(Label.getBuilder().setText(this::toString).build());
    }

    /**
     * override to transform into string
     * @return string or null
     */
    @Override
    public String toString() {
        if (dataSource != null && dataSource.enable())
            if (dataSource.getTotal() > 0 && dataSource.getCurrent() <= dataSource.getTotal())
                return getProgressBar(dataSource.getCurrent(), dataSource.getTotal(), barLength);
        return null;
    }
}
