package br.com.calcmath.calcmath;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalcMathController {

    @FXML
    private Label tela;

    private String expressao = "";
    private boolean start = true;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        switch (buttonText) {
            case "C":
                // Limpa tudo
                expressao = "";
                start = true;
                tela.setText("");
                break;

            case "=":
                // Calcula o resultado da expressão
                double resultado = avaliarExpressao(expressao);
                tela.setText(String.valueOf(resultado));
                expressao = String.valueOf(resultado); // Permite continuar calculando com o resultado
                start = true;
                break;

            default:
                // Números e operadores
                if (start) {
                    expressao = buttonText;
                    tela.setText(buttonText);
                    start = false;
                } else {
                    expressao += buttonText;
                    tela.setText(tela.getText() + buttonText);
                }
                break;
        }
    }

    private double avaliarExpressao(String expressao) {
        // Remove espaços (se houver)
        expressao = expressao.replaceAll("\\s+", "");

        // Lista para armazenar números e operadores separadamente
        List<Double> numeros = new ArrayList<>();
        List<Character> operadores = new ArrayList<>();

        String numeroAtual = "";
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                numeroAtual += c;
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (!numeroAtual.isEmpty()) {
                    numeros.add(Double.parseDouble(numeroAtual));
                    numeroAtual = "";
                }
                operadores.add(c);
            }
        }
        // Adiciona o último número
        if (!numeroAtual.isEmpty()) {
            numeros.add(Double.parseDouble(numeroAtual));
        }

        // Primeira passagem: resolve * e /
        List<Double> tempNumeros = new ArrayList<>(numeros);
        List<Character> tempOperadores = new ArrayList<>(operadores);

        for (int i = 0; i < tempOperadores.size(); ) {
            char op = tempOperadores.get(i);
            if (op == '*' || op == '/') {
                double num1 = tempNumeros.get(i);
                double num2 = tempNumeros.get(i + 1);
                double resultado;

                if (op == '*') {
                    resultado = num1 * num2;
                } else {
                    if (num2 == 0) throw new ArithmeticException("Divisão por zero!");
                    resultado = num1 / num2;
                }

                tempNumeros.set(i, resultado);
                tempNumeros.remove(i + 1);
                tempOperadores.remove(i);
            } else {
                i++;
            }
        }

        // Segunda passagem: resolve + e -
        double resultadoFinal = tempNumeros.get(0);
        for (int i = 0; i < tempOperadores.size(); i++) {
            char op = tempOperadores.get(i);
            double num2 = tempNumeros.get(i + 1);

            if (op == '+') {
                resultadoFinal += num2;
            } else if (op == '-') {
                resultadoFinal -= num2;
            }
        }

        return resultadoFinal;
    }
}
