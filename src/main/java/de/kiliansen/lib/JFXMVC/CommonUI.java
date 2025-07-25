package de.kiliansen.lib.JFXMVC;

/**
 * CommonUI is an interface that defines the basic structure for UI components.
 * It helps separate the UI logic from the application logic, allowing for better modularity and testability.
 * It provides methods to set the coordinator and retrieve the resource path for UI components.
 */
public interface CommonUI<F> {

    /**
     * Sets the coordinator for this UI component.
     *
     * @param coordinator the coordinator to set
     * @param <T>        the type of the coordinator, extending CommonApplicationCoordinator
     */
    <T extends CommonApplicationCoordinator<F>> void setCoordinator(T coordinator);

    /**
     * Gets the resource path for the UI component.
     *
     * @return the resource path as a String
     */
    String getResourcePath();
}