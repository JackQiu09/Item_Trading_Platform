package view.views;

import view.component.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that inherits ProgressBar.DataSource and extends View to view the item list like read a book
 * @param <T> object
 */
public class ItemListView<T> extends View implements ProgressBar.DataSource {
    private final List<T> itemList;
    private final int displayedItemPerPage = 5;
    private int pageIndex = 0;
    private int currentCount = 0;
    private final boolean displayProgressBar;

    /**
     * the constructor of the class
     * @param itemList List<T>
     */
    public ItemListView(List<T> itemList) {
        this(itemList, false);
    }

    /**
     * the constructor of the class with boolean
     * @param itemList List<T>
     * @param displayProgressBar boolean
     */
    public ItemListView(List<T> itemList, boolean displayProgressBar) {
        this.itemList = itemList;
        this.displayProgressBar = displayProgressBar;
        view();
    }

    /**
     * override the method to get the current page
     * @return index of current page
     */
    @Override
    public int getCurrent() {
        return pageIndex;
    }

    /**
     * override the method to get the total page
     * @return total page
     */
    @Override
    public int getTotal() {
        return totalPage() - 1;
    }

    /**
     * override the method to enable
     * @return boolean
     */
    @Override
    public boolean enable() {
        return displayProgressBar;
    }

    /**
     * override the method to display the item
     * @return item list
     */
    private List<T> getDisplayItems() {
        int initialIndex = pageIndex * displayedItemPerPage;
        ArrayList<T> ts = new ArrayList<>();
        currentCount = 0;
        for (int i = initialIndex; i<initialIndex+displayedItemPerPage; i++) {
            if (i < itemList.size()) {
                currentCount++;
                ts.add(itemList.get(i));
            }
        }
        return ts;
    }

    /**
     * return the total page of the item list
     * @return total page
     */
    private int totalPage() {
        return itemList.size() / displayedItemPerPage;
    }

    /**
     * check if index is in range
     * @param index int to check
     * @return boolean
     */
    public boolean contains(int index) {
        return index <= currentCount;
    }

    /**
     * check if item list is empty
     * @return boolean
     */
    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    /**
     * get item at page of index
     * @param index page number
     * @return item list
     */
    public T getItem(int index) {
        if (!contains(index)) {
            return null;
        }
        int initialIndex = pageIndex * displayedItemPerPage;
        return itemList.get(initialIndex + index - 1);
    }

    /**
     * prompt to display
     * @return the set StringBuilder into string
     */
    public String prompt() {
        StringBuilder sb = new StringBuilder();
        if (itemList.size() == 0) {
            sb.append("No results.");
            return sb.toString();
        }
        int count = 1;
        for (T t: getDisplayItems()) {
            sb.append(count++).append(": ").append(t.toString()).append("\n");
        }
        int totalPages = totalPage();
        if (itemList.size() % displayedItemPerPage > 0) {
            totalPages++;
        }
        if (totalPages <= 1) {
            sb.append("Displaying ").append(pageIndex + 1).append(" out of ").append(totalPages).append(" page.");
        } else {
            sb.append("Displaying ").append(pageIndex + 1).append(" out of ").append(totalPages).append(" pages.");
        }
        return sb.toString();
    }

    /**
     * check if exist in the previous page
     * @return boolean
     */
    public boolean isExistPreviousPage() {
        return pageIndex > 0 && totalPage() >= 1;
    }

    /**
     * turn to the previous page
     */
    public void previousPage() {
        if (isExistPreviousPage()) {
            pageIndex--;
        }
    }

    /**
     * check if exist in the next page
     * @return boolean
     */
    public boolean isExistNextPage() {
        int totalPages = (itemList.size() - 1) / displayedItemPerPage;
        return pageIndex < totalPages;
    }

    /**
     * turn to the next page
     */
    public void nextPage() {
        if (isExistNextPage()) {
            pageIndex++;
        }
    }

    /**
     * override the method to View the item list with page
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
                        .setText(this::prompt)
                        .build()
        );
        component(
                MultipleChoiceView.getBuilder()
                        .addQuestion("<", () -> {
                            if (this.isExistPreviousPage()) {
                                return "Previous page.";
                            }
                            return null;
                        }, this::previousPage)
                        .addQuestion(">", () -> {
                            if (this.isExistNextPage()) {
                                return "Next page.";
                            }
                            return null;
                        }, this::nextPage)
                        .build()
        );
    }
}
