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
  /**
   * 演算子で終わっているかどうかの判定
   *
   * @param str Label
   * @return 直前が演算子なら true
   */
  static Boolean isAfterOperator(String str) {
    return str.endsWith(" ") || str.endsWith("(");
  }

  /**
   * 与えられた文字列が数値として認識できるか
   *
   * @param str チェックする文字列
   * @return 認識できるなら true
   */
  static Boolean isNumber(String str) {
    return str != null && str.matches("[0-9.]+");
  }

  /**
   * Leading-0s を消す
   *
   * @param str 消したい文字列
   * @return 消した後の文字列
   */
  static String removeLeadingZero(String str) {
    String[] tokens = str.split(" ");
    String lastToken = tokens[tokens.length - 1];
    if (lastToken.equals("0")) str = str.substring(0, str.length() - 1);
    return str;
  }
}

public class App extends Application {
  // ----- Member Variables ----- //
  static String str = "0";
  static Text txt = new Text(str);
  static ScrollPane topLabel = new ScrollPane();
  static Boolean isDotUsed = false;
  static Integer bracketCnt = 0;

  // ----- Constants ----- //

  static final String[] buttonText = {"C", "BS", "(", ")", "e", "7", "8", "9", "/", "abs", "4", "5", "6", "*", "mod", "1", "2", "3", "-", "log", "0", ".", "=", "+", "^"};

  // ----- Main ----- //
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

  // Button Input Converter
  private void onInput(ActionEvent ev) {
    Button btn = (Button) ev.getSource();
    String txt = btn.getText();
    onInput(txt);
  }

  // Input Handler Main
  private void onInput(String txt) {
    if (txt.equals("C")) ClearHandler();
    else if (txt.equals("BS")) BackspaceHandler();
    else if (txt.equals("(")) OpenBracketHandler();
    else if (txt.equals(")")) CloseBracketHandler();
    else if (txt.equals("=")) EqualHandler();
    else if (txt.equals(".")) DotHandler();
    else if (txt.equals("abs")) {
      // absは特別
      // 直前が数字の場合はreturn
      if (!Utility.isAfterOperator(str)) return;
      AppendToText("abs(");
      bracketCnt++;
      isDotUsed = false;
    } else if (txt.equals("mod")) OperatorHandler(" mod ");
    else if (txt.equals("log")) OperatorHandler(" log base ");
    else if (!Utility.isNumber(txt) && !txt.equals("e")) OperatorHandler(" " + txt + " ");
    else NumberHandler(txt);
    ScrollLabelToRight();
  }

  /**
   * Clear の処理
   */
  private void ClearHandler() {
    txt.setText(str = "0");
    isDotUsed = false;
    bracketCnt = 0;
    ScrollLabelToRight();
  }

  /**
   * BackSpace の処理
   */
  private void BackspaceHandler() {
    assert (str.length() > 0);
    if (str.endsWith("abs(")) {
      // 特殊例
      str = str.substring(0, str.length() - 4);
      bracketCnt--;
    } else if (str.endsWith("(")) {
      str = str.substring(0, str.length() - 1);
      bracketCnt--;
    } else if (str.endsWith(")")) {
      str = str.substring(0, str.length() - 1);
      bracketCnt++;
    } else if (str.endsWith(" ")) {
      // 演算子で終わっている
      // 消すべき部分を計算
      String[] tokens = str.split(" ");
      int r = tokens.length;
      while (r > 0 && (!Utility.isNumber(tokens[r - 1]) && !tokens[r - 1].equals("e"))) r--;

      // つなげる
      if (r > 0) str = tokens[0];
      else str = "";
      for (int i = 1; i < r; i++) str = str.concat(" " + tokens[i]);
    } else {
      // 数字で終わっている
      str = str.substring(0, str.length() - 1);
    }
    if (str.length() == 0) str = "0";
    txt.setText(str);
  }

  /**
   * 開き括弧が押された時の処理
   */
  private void OpenBracketHandler() {
    // 数字の直後かつ初期状態じゃない
    if (!Utility.isAfterOperator(str) && !str.equals("0")) return;

    str = Utility.removeLeadingZero(str);
    bracketCnt++;
    AppendToText("(");
  }

  /**
   * 閉じ括弧が押された時の処理
   */
  private void CloseBracketHandler() {
    // 演算子直後の閉じ括弧
    if (Utility.isAfterOperator(str)) return;

    // 対応する開き括弧がない
    if (bracketCnt == 0) return;

    bracketCnt--;
    AppendToText(")");
  }

  /**
   * 「=」が押されたときの処理
   */
  private void EqualHandler() {
    // Todo: implement
    AppendToText("=");
  }

  /**
   * 小数点を扱う
   */
  private void DotHandler() {
    // すでに小数点が使われているなら避けるべき
    if (isDotUsed) return;

    // 演算子の直後は0を加える
    if (Utility.isAfterOperator(str)) NumberHandler("0");

    isDotUsed = true;
    AppendToText(".");
  }

  /**
   * 演算子の入力を扱う
   *
   * @param s 扱う演算子
   */
  private void OperatorHandler(String s) {
    // 演算子の後ろに演算子はいけない
    if (Utility.isAfterOperator(str)) return;

    AppendToText(s);

    // 小数点をもう一度使えるようにする
    isDotUsed = false;
  }

  /**
   * 数字の入力を扱う
   *
   * @param s 扱う数字
   */
  private void NumberHandler(String s) {
    // 括弧のあとは演算子が来る
    if (str.endsWith(")")) return;

    // e は単体であるべき
    if (str.endsWith("e")) return;
    if (s.equals("e") && !Utility.isAfterOperator(str)) return;

    // Leading-0sは消す
    str = Utility.removeLeadingZero(str);

    AppendToText(s);
  }

  /**
   * 文字列を数式の最後に加える
   *
   * @param s 加える文字列
   */
  private void AppendToText(String s) {
    txt.setText(str = str.concat(s));
  }

  /**
   * 数式のスクロールを右端にする
   */
  private void ScrollLabelToRight() {
    topLabel.setHvalue(topLabel.getHmax());
  }
}
