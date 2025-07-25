package de.kiliansen.lib.JFXMVC;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * CommonLayout is an abstract class that provides a common structure for UI layouts in the application.
 * It manages a list of UI elements and provides methods to initialize them and set the coordinator.
 * This class is designed to be extended by specific layout implementations,
 * allowing for a flexible and modular UI design.
 **/
public abstract class CommonLayout<F> implements CommonUI<F> {
    /**
     * A list of CommonUI elements that are part of this layout.
     * This is initialized lazily when the UI elements are accessed for the first time.
     */
    public List<CommonLayoutView<F>> uiElements;

    /**
     * Returns a list of CommonUI classes that should be included in this layout.
     * This method must be implemented by subclasses to provide the specific UI elements.
     *
     * @return a list of CommonUI classes
     */
    public abstract List<Class<? extends CommonUI<F>>> getUiElements();

    /**
     * Initializes the UI elements for this layout.
     * This method loads the FXML files for each UI element and creates instances of them.
     * It is called automatically when the UI elements are accessed for the first time.
     */
    public void initializeUiElements() {
        if (uiElements == null) {
            uiElements = getUiElements().stream()
                    .map(uiClass -> {

                        FXMLLoader loader;
                        try {
                            loader = new FXMLLoader(
                                    getClass().getResource(uiClass.getDeclaredConstructor().newInstance().getResourcePath()));
                            Parent root = loader.load();
                            CommonUI<F> instance = loader.getController();

                            return new CommonLayoutView<>(instance, root);
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                                 InvocationTargetException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
        }
    }

    /**
     * Sets the coordinator for all UI elements in this layout.
     * This method initializes the UI elements if they have not been initialized yet.
     * It then sets the coordinator for each UI element.
     *
     * @param coordinator the coordinator to set for the UI elements
     * @param <T>        the type of the coordinator, extending CommonApplicationCoordinator
     */
    public <T extends CommonApplicationCoordinator<F>> void setCoordinator(T coordinator) {
        initializeUiElements();
        for (CommonLayoutView<F> commonLayoutView : uiElements) {
            commonLayoutView.ui.setCoordinator(coordinator);
        }
    }

    /**
     * A record that holds a CommonUI instance and its associated Parent view.
     * @param ui
     * @param view
     */
    public record CommonLayoutView<F>(CommonUI<F> ui, Parent view) {
    }
}
