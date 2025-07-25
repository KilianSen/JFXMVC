package de.kiliansen.lib.JFXMVC;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonApplicationCoordinator<T> {
    public static final Logger logger = LogManager.getLogger(CommonApplicationCoordinator.class);

    /**
     * A cache for scenes and controllers to avoid reloading them every time.
     * This uses a ConcurrentHashMap for thread-safe access.
     */
    private final Map<Class<? extends CommonUI<T>>, Scene> sceneCache = new ConcurrentHashMap<>();

    /**
     * A cache for controllers to avoid reloading them every time.
     * This uses a ConcurrentHashMap for thread-safe access.
     */
    private final Map<Class<? extends CommonUI<T>>, CommonUI<T>> controllerCache = new ConcurrentHashMap<>();

    @Getter
    protected Stage stage;

    /**
     * The model used by this coordinator.
     * This is typically used to manage application state and data.
     */
    public final Model<T> model;

    /**
     * Constructs a CommonApplicationCoordinator with a model.
     *
     * @param model the model to be used by this coordinator
     */
    CommonApplicationCoordinator(Model<T> model) {
        this.model = model;
    }

    /**
     * Shows a CommonUI instance in the primary stage.
     *
     * @param commonUI the CommonUI class to show
     * @param <t>      the type of CommonUI
     *
     * Note: This method checks if the scene for the given CommonUI is already cached.
     */
    public <t extends CommonUI<T>> t showCommonUI(Class<t> commonUI) {
        try {

            // Get current window size
            if (stage == null) {
                logger.error("Stage is not initialized. Cannot show CommonUI: {}", commonUI.getSimpleName());
                throw new IllegalStateException("Stage is not initialized.");
            }
            var currentWidth = stage.getWidth();
            var currentHeight = stage.getHeight();

            // Check if the scene and controller are already cached


            if (sceneCache.containsKey(commonUI) && controllerCache.containsKey(commonUI)) {
                Scene cachedScene = sceneCache.get(commonUI);
                CommonUI<T> controller = controllerCache.get(commonUI);

                if (controller != null) {
                    @SuppressWarnings("unchecked")
                    t s = (t) controller;
                    s.setCoordinator(this);
                    Platform.runLater(() -> {
                        stage.setScene(cachedScene);
                        stage.setResizable(true);
                        stage.show();
                        // Restore the previous window size
                        stage.setWidth(currentWidth);
                        stage.setHeight(currentHeight);
                    });

                    logger.info("Reusing cached scene for {}", commonUI.getSimpleName());
                    return s;
                } else {
                    logger.warn("Cached controller for {} is not an instance of CommonUI", commonUI.getSimpleName());
                }
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(commonUI.getDeclaredConstructor().newInstance().getResourcePath()));
            Parent root = loader.load();
            t instance = loader.getController();

            instance.setCoordinator(this);

            Scene scene = new Scene(root);
            sceneCache.put(commonUI, scene);
            controllerCache.put(commonUI, instance);
            Platform.runLater(() -> {
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();

                // Restore the previous window size
                stage.setWidth(currentWidth);
                stage.setHeight(currentHeight);
            });
            logger.info("Loaded and displayed UI: {}", instance.getClass().getSimpleName());

            return instance;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load common UI: " + commonUI.getSimpleName(), e);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
