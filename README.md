# Simple Calculator

Simple Calculator made with Java, JavaFX

## Checked Environment

I've checked this app works with...

- Windows 11
- java version "19.0.2" 2023-01-17
- Java(TM) SE Runtime Environment (build 19.0.2+7-44)
- Java HotSpot(TM) 64-Bit Server VM (build 19.0.2+7-44, mixed mode, sharing)
- JavaFX 19.0.2.1

## Layout

<table>
<tr><td colspan="5">Equation</td></tr>
<tr><td>C</td><td>BS</td><td>(</td><td>)</td><td>e</td></tr>
<tr><td>7</td><td>8</td><td>9</td><td>/</td><td>abs</td></tr>
<tr><td>4</td><td>5</td><td>6</td><td>*</td><td>mod</td></tr>
<tr><td>1</td><td>2</td><td>3</td><td>-</td><td>log</td></tr>
<tr><td>0</td><td>.</td><td>=</td><td>+</td><td>^</td></tr>
</table>

## Calculation Order

1. Inside Brackets
2. `^`, `log`
3. `*`, `/`, `mod`
4. `+`, `-`

(same as we use usually)

## Functions

- `C` : Clears the Equation
- `BS` : Removes 1 character
- `^` : "x ^ y" = $x ^ y$
- `log` : "x log base y" = $\log_y (x)$
- `mod` : Modulo
- `abs` : Absolute value
