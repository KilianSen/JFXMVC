# JFXMVC - A Simple JavaFX MVC Framework

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

JFXMVC is a lightweight framework designed to simplify the development of JavaFX applications by providing a clear structure based on the Model-View-Controller (MVC) pattern. It helps you organize your UI components, manage navigation, and reduce boilerplate code.

## Features

*   **Type-Safe Generics**: Uses generics throughout the framework (`Model<T>`, `CommonUI<T>`, `CommonViewController<T>`) to ensure type safety for your application's data model.
*   **Simple MVC Structure**: Easily separate your application's logic from its UI.
*   **View Coordination**: A central coordinator manages scenes and navigation.
*   **Scene Caching**: Improves performance by caching scenes and controllers.
*   **Composite Layouts**: Build complex UIs by combining smaller components.
*   **FXML Integration**: Seamlessly works with FXML for UI design.

## Core Components

The framework is built around a few core components:

### `Model<T>`
The `Model<T>` class is a generic data holder that can be used to share state and data across different parts of your application.

### `CommonApplicationCoordinator<T>`
The `CommonApplicationCoordinator<T>` is the central piece of the framework. It is initialized with a `Model<T>` and manages the lifecycle of different UI components. Its main responsibilities are:
-   Displaying UI components (`CommonUI`).
-   Caching scenes and controllers to improve performance by reusing them.
-   Managing the primary JavaFX `Stage`.

To show a view, you can use the `showCommonUI` method:
```java
coordinator.showCommonUI(MyCustomViewController.class);
```

### `CommonUI<F>`
`CommonUI<F>` is an interface that all UI components must implement. It defines the contract for a UI view within the framework.
-   `<T extends CommonApplicationCoordinator<F>> void setCoordinator(T coordinator)`: Sets the coordinator for the UI component, allowing it to interact with other parts of the application.
-   `String getResourcePath()`: Returns the path to the FXML file that defines the UI layout.

### `CommonViewController<F>`
`CommonViewController<F>` is an abstract base class that implements `CommonUI<F>`. It is recommended to extend this class for your FXML controllers to reduce boilerplate code.

Example of a simple `CommonViewController` implementation:
```java
public class MyCustomViewController extends CommonViewController<MyModel> {
    @Override
    public String getResourcePath() {
        return "/path/to/your/view.fxml";
    }

    // Your controller logic here
}
```

### `CommonLayout<F>`
`CommonLayout<F>` is an abstract class for creating composite UIs. It allows you to combine multiple `CommonUI` components into a single layout.
-   `getUiElements()`: This abstract method must be implemented to provide a list of `CommonUI` classes that are part of this layout.
-   It initializes all child UI elements and sets their coordinator.

Example of a `CommonLayout`:

```java
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.util.List;

public class MyCustomLayout extends CommonLayout<MyModel> {
    @FXML
    private AnchorPane headerContainer;
    
    @FXML
    private AnchorPane bodyContainer;
    
    @FXML
    private AnchorPane footerContainer;

    @Override
    public List<Class<? extends CommonUI<MyModel>>> getUiElements() {
        return List.of(HeaderUI.class, BodyUI.class, FooterUI.class);
    }

    @Override
    public <T extends CommonApplicationCoordinator<MyModel>> void setCoordinator(T coordinator) {
        super.setCoordinator(coordinator);
        
        // Embed all UI elements in the layout
        for (CommonLayoutView<MyModel> layoutView : uiElements) {
            if (layoutView.ui() instanceof HeaderUI) {
                headerContainer.getChildren().setAll(layoutView.view());
            } else if (layoutView.ui() instanceof BodyUI) {
                bodyContainer.getChildren().setAll(layoutView.view());
            } else if (layoutView.ui() instanceof FooterUI) {
                footerContainer.getChildren().setAll(layoutView.view());
            }
        }
    }

    @Override
    public String getResourcePath() {
        return "/path/to/your/layout.fxml";
    }
}
```

## How to Use

1.  **Define your Model** class that will hold your application's data.
2.  **Create your FXML files** for your views.
3.  **Create a controller class** for each view that extends `CommonViewController<YourModel>`.
4.  **Implement the `getResourcePath` method** to point to the FXML file.
5.  If you have a main application coordinator, it should extend `CommonApplicationCoordinator<YourModel>`.
6.  **Use the `showCommonUI` method** from your coordinator to display your views.

## Installation

To use JFXMVC in your own Maven project, add the following dependency to your `pom.xml`.

*Note: You must first build the project from source and install it to your local Maven repository.*

```xml
<dependency>
    <groupId>de.kiliansen.lib.JFXMVC</groupId>
    <artifactId>JFXMVC</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Building from Source

### Prerequisites
*   Java Development Kit (JDK) 24 or newer
*   Apache Maven

### Steps
1.  Clone the repository:
    ```sh
    git clone https://github.com/KilianSen/JFXMVC.git
    ```
2.  Navigate to the project directory:
    ```sh
    cd JFXMVC
    ```
3.  Build the project and install it to your local Maven repository:
    ```sh
    mvn clean install
    ```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
