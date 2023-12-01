package launcher;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("utcn-logo.png"));
        ComponentFactory componentFactory = ComponentFactory.getInstance(false,primaryStage);
    }
}
