package br.com.calcmath.calcmath;

import br.com.calcmath.calcmath.service.CalculadoraService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalcMathController {

    @FXML
    public Label operacao;
    @FXML
    public Label result;

    private String expressao = "0";

    private final CalculadoraService calculadoraService = new CalculadoraService();

    @FXML
    private void handleButtonAction(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        switch (buttonText) {
            case "C":
                expressao = "0";
                atualizarTela("", operacao);
                atualizarTela(expressao, result);
                break;

            case "=":
                if (!expressao.isEmpty()) {
                    double resultado = calculadoraService.avaliarExpressao(expressao);
                    atualizarTela(expressao + " = ", operacao);
                    atualizarTela(String.valueOf(resultado), result);
                    expressao = String.valueOf(resultado);
                }
                break;

            default:

                if (expressao.isEmpty()) {
                    expressao = buttonText;
                    atualizarTela(buttonText, result);
                } else {
                    expressao += buttonText;
                    atualizarTela(expressao, result);
                }
                break;
        }
    }

    private void atualizarTela(String texto, Label label) {
        label.setText(texto);
    }
}
