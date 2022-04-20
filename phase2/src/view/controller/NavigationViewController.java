package view.controller;

import view.model.InputLayer;
import view.model.TextLayer;
import view.views.View;
import view.model.ViewStack;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The controller class to navigate the view
 */
public class NavigationViewController {
    private final ViewStack viewStack;

    /**
     * constructor of the class
     */
    public NavigationViewController() {
        viewStack = new ViewStack();
    }

    /**
     * clear the viewStack and set the rootView
     * @param rootView the new View in viewStack
     */
    public void setRootView(View rootView) {
        this.viewStack.clear();
        redirect(rootView);
    }

    /**
     * override the method to run with the input
     * @param reader BufferedReader
     */
    public void run(BufferedReader reader) {
        String comm = null;
        while (true) {
            view(viewStack.top());
            try {
                comm = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            read(viewStack.top(), comm);
        }
    }

    /**
     * override the view method to display the textLayer
     * @param view View
     */
    private void view(View view) {
        for (TextLayer textLayer : view.getTextLayers()) {
            textLayer.print();
        }
    }

    /**
     * override the read method to read the inputLayer
     * @param view to get the inputLayer
     * @param comm string to read
     */
    private void read(View view, String comm) {
        for (InputLayer inputLayer : view.getInputLayers()) {
            if (inputLayer.read(comm)) {
                return;
            }
        }
    }

    /**
     * override the redirect method to the new view
     * @param newView new View
     */
    public void redirect(View newView) {
        newView.view();
        newView.setNavigationViewController(this);
        viewStack.push(newView);
    }

    /**
     * to the previous step
     */
    public void back() {
        viewStack.pop();
        viewStack.top().rebuild();
    }
}
