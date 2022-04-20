package view.model;

import view.views.View;

import java.util.ArrayList;

/**
 * The class that extends View List builds Views into Stack
 */
public class ViewStack extends ArrayList<View> {

    /**
     * add the View
     * @param view View to add
     */
    public void push(View view) {
        add(0, view);
    }

    /**
     * pop the View at the index 0
     * @return removed View
     */
    public View pop() {
        if (isEmpty())
            return null;
        return remove(0);
    }

    /**
     * return the View at the top
     * @return the View at index 0
     */
    public View top() {
        if (isEmpty())
            return null;
        return get(0);
    }
}
