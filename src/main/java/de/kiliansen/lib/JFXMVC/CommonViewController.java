package de.kiliansen.lib.JFXMVC;

import lombok.Getter;
import lombok.Setter;

/**
 * An abstract base class for view controllers that implement the {@link CommonUI} interface.
 * This class provides a common structure for controllers, including a reference to the
 * {@link CommonApplicationCoordinator} and the resource path for the FXML file.
 *
 * <p>By extending this class, you can create controllers that are easily managed by the
 * {@code JFXMVC} framework. It simplifies the implementation of the {@link CommonUI} interface
 * by providing a default implementation for {@code getCoordinator} and {@code setCoordinator}.</p>
 *
 * @see CommonUI
 * @see CommonApplicationCoordinator
 */
public abstract class CommonViewController<F> implements CommonUI<F> {

    /**
     * The application coordinator responsible for managing scenes and navigation.
     * This is typically injected by the framework when a view is shown.
     */
    @Getter
    @Setter
    private CommonApplicationCoordinator<F> coordinator;

    /**
     * Returns the resource path of the FXML file associated with this controller.
     * This method must be implemented by subclasses to specify the location of the FXML file.
     *
     * @return The resource path as a string (e.g., "/views/MyView.fxml").
     */
    public abstract String getResourcePath();
}
