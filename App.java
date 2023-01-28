import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class Utility {
  static Boolean isAfterOperator(String str) {
    return str.endsWith(" ") || str.endsWith("(");
  }

  static Boolean isNumber(String str) {
    return str != null && str.matches("[0-9.]+");
  }

  static String removeLeadingZero(String str) {
    String[] tokens = str.split(" ");
    String lastToken = tokens[tokens.length - 1];
    if (lastToken.equals("0")) str = str.substring(0, str.length() - 1);
    return str;
  }
}

public class App extends Application {
  static String str = "0";
  static Text txt = new Text(str);
  static ScrollPane topLabel = new ScrollPane();
  static final String[] buttonText = {"C", "BS", "(", ")", "e", "7", "8", "9", "/", "abs", "4", "5", "6", "*", "mod", "1", "2", "3", "-", "log", "0", ".", "=", "+", "^"};
  static Boolean isDotUsed = false;

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
      buttons[i].setOnAction(this::onInput);
      gridPane.add(buttons[i], i % 5, i / 5);
    }
    root.setCenter(gridPane);

    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  private void onInput(ActionEvent ev) {
    Button btn = (Button) ev.getSource();
    String txt = btn.getText();
    onInput(txt);
  }

  private void onInput(String txt) {
    if (txt.equals("C")) ClearHandler();
    else if (txt.equals("BS")) BackspaceHandler();
    else if (txt.equals("(")) OpenBracketHandler();
    else if (txt.equals(")")) CloseBracketHandler();
    else if (txt.equals("=")) EqualHandler();
    else if (txt.equals(".")) DotHandler();
    else if (txt.equals("abs")) OperatorHandler(" abs(");
    else if (txt.equals("mod")) OperatorHandler(" mod ");
    else if (txt.equals("log")) OperatorHandler(" log base ");
    else if (!Utility.isNumber(txt) && !txt.equals("e")) OperatorHandler(" " + txt + " ");
    else NumberHandler(txt);
    ScrollLabelToRight();
  }

  private void ClearHandler() {
    // Todo: implement
    txt.setText(str = "0");
  }

  private void BackspaceHandler() {
    // Todo: implement
    assert (str.length() > 0);
    str = str.substring(0, str.length() - 1);
    if (str.length() == 0) str = "0";
    txt.setText(str);
  }

  private void OpenBracketHandler() {
    // Todo: implement
    AppendToText("(");
  }

  private void CloseBracketHandler() {
    // Todo: implement
    AppendToText(")");
  }

  private void EqualHandler() {
    // Todo: implement
    AppendToText("=");
  }

  private void DotHandler() {
    if (isDotUsed) return;
    isDotUsed = true;
    AppendToText(".");
  }

  private void OperatorHandler(String s) {
    if (Utility.isAfterOperator(str)) return;
    AppendToText(s);
    isDotUsed = false;
  }

  private void NumberHandler(String s) {
    if (str.endsWith("e")) return;
    if (s.equals("e") && !Utility.isAfterOperator(str)) return;
    if (!isDotUsed) str = Utility.removeLeadingZero(str);
    AppendToText(s);
  }

  private void AppendToText(String s) {
    txt.setText(str = str.concat(s));
  }

  private void ScrollLabelToRight() {
    topLabel.setHvalue(topLabel.getHmax());
  }
}
