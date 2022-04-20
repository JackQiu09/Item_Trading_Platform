package view.views;

import view.controller.NavigationViewController;
import view.model.InputLayer;
import view.model.TextLayer;

import java.util.ArrayList;

/**
 * An abstract class to view
 */
public abstract class View {

    private NavigationViewController navigationViewController;
    private final ArrayList<TextLayer> textLayers = new ArrayList<>();
    private final ArrayList<InputLayer> inputLayers = new ArrayList<>();

    /**
     * setting the Navigation View Controller
     * @param controller NavigationViewController
     */
    public void setNavigationViewController(NavigationViewController controller) {
        this.navigationViewController = controller;
    }

    /**
     * get the textLayers
     * @return textLayers
     */
    public ArrayList<TextLayer> getTextLayers() {

        return textLayers;
    }

    /**
     * get the inputLayers
     * @return inputLayers
     */
    public ArrayList<InputLayer> getInputLayers() {

        return inputLayers;
    }

    public abstract void view();

    /**
     * check if anything inside the inputLayers
     * @return boolean
     */
    private boolean hasInputLayer() {
        return inputLayers.size() > 0;
    }

    /**
     * check if anything inside the textLayers
     * @return boolean
     */
    private boolean hasTextLayer() {
        return textLayers.size() > 0;
    }

    /**
     * compose view together
     * @param view could have textLayers or inputLayers
     */
    public void component(View view) {
        if (view.hasTextLayer()) {
            textLayers.addAll(view.getTextLayers());
        }
        if (view.hasInputLayer()) {
            inputLayers.addAll(view.getInputLayers());
        }
    }

    /**
     * compose textLayer
     * @param textLayer TextLayer
     */
    public void component(TextLayer textLayer) {
        textLayers.add(textLayer);
    }

    /**
     * compose inputLayer
     * @param inputLayer InputLayer
     */
    public void component(InputLayer inputLayer) {
        inputLayers.add(inputLayer);
    }

    /**
     * redirect the new view
     * @param newView View
     */
    public void redirect(View newView) {
        if (navigationViewController != null)
            navigationViewController.redirect(newView);
        else
            throw new NullPointerException("Attempting to redirect without having a navigation view controller.");
    }

    /**
     * to the previous step
     */
    public void back() {
        if (navigationViewController != null)
            navigationViewController.back();
        else
            throw new NullPointerException("Attempting to go back without having a navigation view controller.");
    }

    /**
     * clear everything
     */
    public void rebuild() {
        textLayers.clear();
        inputLayers.clear();
        view();
    }
}
