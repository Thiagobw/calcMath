package br.com.calcmath.calcmath;

import br.com.calcmath.calcmath.service.CalculadoraService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalcMathController {

    @FXML
    private Label tela;

    private String expressao = "";
    private boolean start = true;
    private final CalculadoraService calculadoraService = new CalculadoraService();

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        switch (buttonText) {
            case "C":
                expressao = "";
                start = true;
                tela.setText("");
                break;

            case "=":
                double resultado = calculadoraService.avaliarExpressao(expressao);
                tela.setText(String.valueOf(resultado));
                expressao = String.valueOf(resultado);
                start = true;
                break;

            default:
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
}
