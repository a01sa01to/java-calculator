import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class App extends Application {
  @Override
  public void start(Stage primaryStage) {
    // Initialize App
    primaryStage.setTitle("Calculator");
    primaryStage.setResizable(false);
    BorderPane root = new BorderPane();
    root.setPrefSize(250, 350);

    // Button Display
    GridPane gridPane = new GridPane();
    Button[] buttons = new Button[25];
    for (int i = 0; i < 25; i++) {
      buttons[i] = new Button();
      buttons[i].setPrefSize(50, 50);
      buttons[i].setText(Integer.toString(i));
      gridPane.add(buttons[i], i / 5, i % 5);
    }

    root.setCenter(gridPane);

    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
