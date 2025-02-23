package br.com.calcmath.calcmath;

import br.com.calcmath.calcmath.service.CalculadoraService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class CalcMathController {

    @FXML
    public Label operacao;
    @FXML
    public Label result;

    private List<String> expressao = new ArrayList<>();

    private final CalculadoraService calculadoraService = new CalculadoraService();

    @FXML
    private void handleButtonAction(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        switch (buttonText) {
            case "C":
                expressao.clear();
                atualizarTela("", operacao);
                atualizarTela("", result);
                break;

            case "=":
                if (!expressao.isEmpty()) {
                    String expressaoStr = String.join("", expressao); // Concatena a lista em uma String
                    double resultado = calculadoraService.avaliarExpressao(expressaoStr);
                    atualizarTela(expressaoStr + " = ", operacao);
                    atualizarTela(String.valueOf(resultado), result);
                    expressao.clear();
                    expressao.add(String.valueOf(resultado));
                }
                break;
            default:
                if (expressao.isEmpty() || ehOperador(buttonText)) {
                    expressao.add(buttonText);
                } else {
                    // Se o último elemento não for um operador, então deve ser um número, concatena
                    String ultimo = expressao.getLast();
                    if (!ehOperador(ultimo)) {
                        expressao.set(expressao.size() - 1, ultimo + buttonText);
                    } else {
                        expressao.add(buttonText);
                    }
                }
                atualizarTela(String.join(" ", expressao), result); // Exibe com espaços
                break;
        }
    }

    private boolean ehOperador(String s) {
        return s.equals("+") || s.equals("-") || s.equals("x") || s.equals("/");
    }

    private void atualizarTela(String texto, Label label) {
        label.setText(texto);
    }
}
