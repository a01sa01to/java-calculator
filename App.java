import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class App extends Application {
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Calculator");
    primaryStage.setResizable(false);

    BorderPane root = new BorderPane();
    root.setPrefSize(200, 300);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
