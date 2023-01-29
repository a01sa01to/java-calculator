import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyEvent.CHAR_UNDEFINED;

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

  /**
   * 配列を出力する
   *
   * @param arr 配列
   * @param <T> 配列要素の型
   */
  static <T> void PrintArray(T[] arr) {
    for (T t : arr) {
      System.out.print(t);
      System.out.print(" ");
    }
    System.out.print("\n");
  }

  /**
   * 配列を出力する
   *
   * @param arr 配列
   * @param <T> 配列要素の型
   */
  static <T> void PrintArray(List<T> arr) {
    for (T t : arr) {
      System.out.print(t);
      System.out.print(" ");
    }
    System.out.print("\n");
  }
}

class Parser {
  private static String[] tokens;
  private static Boolean isError = false;
  private static String errMsg = "";

  private static double ThrowError(String format, Object... arr) {
    isError = true;
    errMsg = String.format(format, arr);
    return 0;
  }

  public static String Parse(String s) {
    // Initialize
    isError = false;
    errMsg = "";
    String t = s.replace("log base", "log");
    t = t.replace("(", "( ");
    t = t.replace(")", " )");
    t = t.replace("  ", " ");
    tokens = t.split(" ");
    System.out.print("Tokens: ");
    Utility.PrintArray(tokens);

    double ret = Eq1(0, tokens.length);
    return isError ? errMsg : Double.toString(ret);
  }

  private static double ParseNum(int idx) {
    if (tokens[idx].equals("e")) return Math.E;
    if (!Utility.isNumber(tokens[idx])) {
      return ThrowError("Error (NaN)", idx, tokens[idx]);
    }
    return Double.parseDouble(tokens[idx]);
  }

  // 括弧外の + - を処理する
  private static double Eq1(int l, int r) {
    System.out.printf("Eq1: l=%d, r=%d\n", l, r);
    if (r - l == 1) return ParseNum(l);
    int bracketCnt = 0;
    List<Integer> lst = new ArrayList<>();
    for (int i = l; i < r; i++) {
      if (tokens[i].contains("(")) bracketCnt++;
      if (tokens[i].contains(")")) bracketCnt--;
      if (bracketCnt == 0 && tokens[i].equals("+")) lst.add(i);
      if (bracketCnt == 0 && tokens[i].equals("-")) lst.add(i);
    }
    lst.add(r);
    System.out.print("Eq1: ");
    Utility.PrintArray(lst);
    double ret = Eq2(l, lst.get(0));
    for (int i = 0; i < lst.size() - 1; i++) {
      if (tokens[lst.get(i)].equals("+")) ret += Eq2(lst.get(i) + 1, lst.get(i + 1));
      if (tokens[lst.get(i)].equals("-")) ret -= Eq2(lst.get(i) + 1, lst.get(i + 1));
    }
    return ret;
  }

  // 括弧外の * / mod を処理する
  private static double Eq2(int l, int r) {
    System.out.printf("Eq2: l=%d, r=%d\n", l, r);
    if (r - l == 1) return ParseNum(l);
    int bracketCnt = 0;
    List<Integer> lst = new ArrayList<>();
    for (int i = l; i < r; i++) {
      if (tokens[i].contains("(")) bracketCnt++;
      if (tokens[i].contains(")")) bracketCnt--;
      if (bracketCnt == 0 && tokens[i].equals("*")) lst.add(i);
      if (bracketCnt == 0 && tokens[i].equals("/")) lst.add(i);
      if (bracketCnt == 0 && tokens[i].equals("mod")) lst.add(i);
    }
    lst.add(r);
    System.out.print("Eq2: ");
    Utility.PrintArray(lst);
    double ret = Eq3(l, lst.get(0));
    for (int i = 0; i < lst.size() - 1; i++) {
      if (tokens[lst.get(i)].equals("*")) ret *= Eq3(lst.get(i) + 1, lst.get(i + 1));
      if (tokens[lst.get(i)].equals("/")) {
        double tmp = Eq3(lst.get(i) + 1, lst.get(i + 1));
        if (tmp == 0) return ThrowError("Error (div 0)");
        ret /= tmp;
      }
      if (tokens[lst.get(i)].equals("mod")) {
        double tmp = Eq3(lst.get(i) + 1, lst.get(i + 1));
        if (tmp == 0) return ThrowError("Error (mod 0)");
        ret %= tmp;
      }
    }
    return ret;
  }

  // 括弧外の ^ log を処理する
  private static double Eq3(int l, int r) {
    System.out.printf("Eq3: l=%d, r=%d\n", l, r);
    if (r - l == 1) return ParseNum(l);
    int bracketCnt = 0;
    List<Integer> lst = new ArrayList<>();
    for (int i = l; i < r; i++) {
      if (tokens[i].contains("(")) bracketCnt++;
      if (tokens[i].contains(")")) bracketCnt--;
      if (bracketCnt == 0 && tokens[i].equals("^")) lst.add(i);
      if (bracketCnt == 0 && tokens[i].equals("log")) lst.add(i);
    }
    lst.add(r);
    System.out.print("Eq3: ");
    Utility.PrintArray(lst);
    double ret = Eq4(l, lst.get(0));
    for (int i = 0; i < lst.size() - 1; i++) {
      if (tokens[lst.get(i)].equals("^")) ret = Math.pow(ret, Eq4(lst.get(i) + 1, lst.get(i + 1)));
      if (tokens[lst.get(i)].equals("log")) {
        double tmp = Eq4(lst.get(i) + 1, lst.get(i + 1));
        if (tmp == 1) return ThrowError("Error (log base 1)");
        ret = Math.log(ret) / Math.log(tmp);
      }
    }
    return ret;
  }

  // 括弧を一段階下げる
  private static double Eq4(int l, int r) {
    System.out.printf("Eq4: l=%d, r=%d\n", l, r);
    if (r - l == 1) return ParseNum(l);
    int lft = (int) 1e9, rgt = -1;
    for (int i = l; i < r; i++) {
      if (tokens[i].contains("(")) lft = Math.min(lft, i);
      if (tokens[i].contains(")")) rgt = Math.max(rgt, i);
    }
    System.out.printf("lft=%d, rgt=%d\n", l, r);
    assert lft != (int) 1e9;
    assert rgt != -1;
    double tmp = Eq1(lft + 1, rgt);
    if (tokens[lft].contains("abs")) return Math.abs(tmp);
    return tmp;
  }
}

public class App extends Application {
  // ----- Constants ----- //
  static final String[] buttonText = {"C", "BS", "(", ")", "e", "7", "8", "9", "/", "abs", "4", "5", "6", "*", "mod", "1", "2", "3", "-", "log", "0", ".", "=", "+", "^"};
  static final Integer BtnNum = buttonText.length;

  // ----- Member Variables ----- //
  static String str = "0";
  static Text txt = new Text(str);
  static ScrollPane topLabel = new ScrollPane();
  static Integer bracketCnt = 0;
  static Boolean isDotUsed = false;
  static Boolean isEqualUsed = false;
  static Button[] buttons = new Button[BtnNum];

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
    for (int i = 0; i < BtnNum; i++) {
      buttons[i] = new Button();
      buttons[i].setPrefSize(50, 50);
      buttons[i].setText(buttonText[i]);
      buttons[i].setOnAction(this::onBtnInput);
      gridPane.add(buttons[i], i % 5, i / 5);
    }
    root.setCenter(gridPane);
    UpdateButtonState();

    // Keyboard Input
    root.setOnKeyPressed(this::onSpecialKeyInput);
    root.setOnKeyTyped(this::onNormalKeyInput);

    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    topLabel.requestFocus();
  }

  // Button Input Converter
  private void onBtnInput(ActionEvent ev) {
    Button btn = (Button) ev.getSource();
    String txt = btn.getText();
    onInput(txt);
    topLabel.requestFocus();
  }

  // Keyboard Input Converter
  private void onSpecialKeyInput(KeyEvent ev) {
    KeyCode code = ev.getCode();
    assert code != KeyCode.UNDEFINED;
    if (code == KeyCode.DELETE) onInput("C");
    else if (code == KeyCode.BACK_SPACE) onInput("BS");
    else if (code == KeyCode.ENTER) onInput("=");
  }

  private void onNormalKeyInput(KeyEvent ev) {
    String str = ev.getCharacter();
    assert !str.equals(CHAR_UNDEFINED);
    str = str.toLowerCase();
    if (str.equals("|")) str = "abs";
    if (str.equals("l")) str = "log";
    if (str.equals("%")) str = "mod";
    onInput(str);
  }

  // Input Handler Main
  private void onInput(String txt) {
    boolean btnExist = false;
    for (int i = 0; i < BtnNum; i++) {
      if (buttons[i].getText().equals(txt)) {
        btnExist = true;
        if (buttons[i].isDisabled()) return;
      }
    }
    if (!btnExist) return;

    if (txt.equals("C")) ClearHandler();
    else if (txt.equals("BS")) BackspaceHandler();
    else if (txt.equals("(")) OpenBracketHandler();
    else if (txt.equals(")")) CloseBracketHandler();
    else if (txt.equals("=")) EqualHandler();
    else if (txt.equals(".")) DotHandler();
    else if (txt.equals("abs")) {
      str = Utility.removeLeadingZero(str);
      AppendToText("abs(");
      bracketCnt++;
      isDotUsed = false;
    } else if (txt.equals("mod")) OperatorHandler(" mod ");
    else if (txt.equals("log")) OperatorHandler(" log base ");
    else if (!Utility.isNumber(txt) && !txt.equals("e")) OperatorHandler(" " + txt + " ");
    else NumberHandler(txt);
    ScrollLabelToRight();
    UpdateButtonState();
  }

  /**
   * Clear の処理
   */
  private void ClearHandler() {
    txt.setText(str = "0");
    isDotUsed = false;
    isEqualUsed = false;
    bracketCnt = 0;
    ScrollLabelToRight();
    for (int i = 2; i < BtnNum; i++) buttons[i].setDisable(false);
  }

  /**
   * BackSpace の処理
   */
  private void BackspaceHandler() {
    assert str.length() > 0;
    if (isEqualUsed) {
      // = まで消したい
      int idx = str.indexOf(" = ");
      str = str.substring(0, idx);
      isEqualUsed = false;
      for (int i = 2; i < BtnNum; i++) buttons[i].setDisable(false);
    } else if (str.endsWith("abs(")) {
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

      // 最後に小数点を使っているか
      if (tokens[r - 1].contains(".")) isDotUsed = true;

      // つなげる
      str = tokens[0];
      for (int i = 1; i < r; i++) str = str.concat(" " + tokens[i]);
    } else {
      // 数字で終わっている
      if (str.charAt(str.length() - 1) == '.') isDotUsed = false;
      str = str.substring(0, str.length() - 1);
    }
    if (str.length() == 0) str = "0";
    txt.setText(str);
  }

  /**
   * 開き括弧が押された時の処理
   */
  private void OpenBracketHandler() {
    str = Utility.removeLeadingZero(str);
    bracketCnt++;
    AppendToText("(");
  }

  /**
   * 閉じ括弧が押された時の処理
   */
  private void CloseBracketHandler() {
    bracketCnt--;
    AppendToText(")");
  }

  /**
   * 「=」が押されたときの処理
   */
  private void EqualHandler() {
    if (Utility.isAfterOperator(str)) return;
    txt.setText(str = str.concat(" = " + Parser.Parse(str)));
    isEqualUsed = true;
    for (int i = 2; i < BtnNum; i++) buttons[i].setDisable(true);
  }

  /**
   * 小数点を扱う
   */
  private void DotHandler() {
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

  /**
   * 現在の状況に応じてボタンの有効状態を更新する
   */
  private void UpdateButtonState() {
    // とりあえずC,BS以外全部無効化
    for (int i = 2; i < BtnNum; i++) buttons[i].setDisable(true);

    // = が使われている場合はそのまま
    if (isEqualUsed) return;

    // 数字
    {
      final Integer[] idx = {5, 6, 7, 10, 11, 12, 15, 16, 17, 20};
      // 括弧のあとは演算子が来る & e は単体であるべき
      boolean chk = !str.endsWith(")") && !str.endsWith("e");

      if (chk) for (Integer i : idx) buttons[i].setDisable(false);

      // eの判定 演算子直後 or 初期状態
      if (chk && (Utility.isAfterOperator(str) || str.equals("0"))) buttons[4].setDisable(false);
    }

    // 演算子 と =
    {
      final Integer[] idx = {8, 13, 14, 18, 19, 23, 24};
      // 演算子の後ろに演算子はいけない & 小数点の後ろじゃない
      boolean chk = !Utility.isAfterOperator(str) && !str.endsWith(".");
      if (chk) for (Integer i : idx) buttons[i].setDisable(false);

      // =
      // 括弧は閉じている
      if (chk && bracketCnt == 0) buttons[22].setDisable(false);
    }

    // 小数点
    // すでに小数点が使われていない & 数字の後
    if (!isDotUsed && !Utility.isAfterOperator(str) && Utility.isNumber(str.substring(str.length() - 1)))
      buttons[21].setDisable(false);

    // abs
    // 直前が数字ではない or 初期状態
    if (Utility.isAfterOperator(str) || str.equals("0")) buttons[9].setDisable(false);

    // (
    // 数字の直後かつ初期状態じゃない
    if (Utility.isAfterOperator(str) || str.equals("0")) buttons[2].setDisable(false);

    // )
    // 演算子直後の閉じ括弧じゃない & 対応する開き括弧がある
    if (!Utility.isAfterOperator(str) && bracketCnt > 0) buttons[3].setDisable(false);
  }
}
