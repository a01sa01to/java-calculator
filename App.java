import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {
  static String str = "0";
  static Text txt = new Text(str);
  static ScrollPane topLabel = new ScrollPane();
  static final String[] buttonText = {"C", "BS", "(", ")", "e", "7", "8", "9", "/", "abs", "4", "5", "6", "*", "mod", "1", "2", "3", "-", "log", "0", ".", "=", "+", "^"};

  @Override
  public void start(Stage primaryStage) {
    // Initialize App
    primaryStage.setTitle("Calculator");
    primaryStage.setResizable(false);
    BorderPane root = new BorderPane();
    root.setPrefSize(250, 306);

    // Label Display
    topLabel.setPrefViewportHeight(48);
    topLabel.setPrefSize(250, 56);
    topLabel.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    topLabel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    topLabel.setStyle("-fx-padding: 8px; -fx-font-size: 8px;");
    txt.setStyle("-fx-font-size: 24px;");
    topLabel.setContent(txt);
    root.setTop(topLabel);

    // Button Display
    GridPane gridPane = new GridPane();
    Button[] buttons = new Button[25];
    for (int i = 0; i < 25; i++) {
      buttons[i] = new Button();
      buttons[i].setPrefSize(50, 50);
      buttons[i].setText(buttonText[i]);
      gridPane.add(buttons[i], i % 5, i / 5);
    }
    root.setCenter(gridPane);

    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
