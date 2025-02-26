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
    public Label lblExpressaoAnterior;
    @FXML
    public Label lblExpressaoAtual;

    private List<String> expressaoAtual = new ArrayList<>();
    private String expressaoAnterior = "";
    private final CalculadoraService calculadoraService = new CalculadoraService();

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String text = clickedButton.getText();

        switch (text) {
            case "c":
                limparCalculadora();
                break;
            case "<-":
                apagarUltimoElemento();
                break;
            case "=":
                calcularExpressao();
                break;
            default:
                processarEntradaUsuario(text);
                atualizarTela(transformaEmString(expressaoAtual), lblExpressaoAtual);
                break;
        }
    }

    /**
     * Apaga o último elemento ou dígito da expressão atual.
     * Se o último elemento for um número com múltiplos dígitos,
     * remove apenas o último dígito. Se for um operador ou
     * número de um único dígito, remove o elemento inteiro.
     */
    private void apagarUltimoElemento() {
        // Verifica se a expressão está vazia
        if (expressaoAtual.isEmpty()) {
            return;
        }

        // Obtém a posição e o conteúdo do último elemento
        int ultimoIndice = expressaoAtual.size() - 1;
        String ultimoElemento = expressaoAtual.get(ultimoIndice);

        // Se for um operador ou tiver apenas um caractere, remove o elemento inteiro
        if (calculadoraService.ehOperador(ultimoElemento) || ultimoElemento.length() == 1) {
            expressaoAtual.remove(ultimoIndice);
        } else {
            // Se for um número com múltiplos dígitos, remove apenas o último dígito
            String novoValor = ultimoElemento.substring(0, ultimoElemento.length() - 1);

            // Se ainda restar algum dígito, atualiza o elemento; caso contrário, remove-o
            if (!novoValor.isEmpty()) {
                expressaoAtual.set(ultimoIndice, novoValor);
            } else {
                expressaoAtual.remove(ultimoIndice);
            }
        }

        // Atualiza a exibição após a modificação
        atualizarTela(transformaEmString(expressaoAtual), lblExpressaoAtual);
    }

    /**
     * Processa a entrada do usuário e atualiza a expressão atual conforme regras da calculadora
     * @param entrada O texto do botão pressionado pelo usuário
     */
    private void processarEntradaUsuario(String entrada) {
        boolean entradaEhOperador = calculadoraService.ehOperador(entrada);

        if (expressaoAtual.isEmpty()) {
            if (!entradaEhOperador) {
                adicionar(entrada);
            }
            return;
        }
        else if (this.expressaoAtual.size() == 3 && entradaEhOperador && calculadoraService.ehNumero(obterUltimoElemento())) {
            executar();
        }

        String ultimoElemento = obterUltimoElemento();
        boolean ultimoEhOperador = calculadoraService.ehOperador(ultimoElemento);

        if (!ultimoEhOperador && !entradaEhOperador) {
            // Caso: número seguido de número - concatena
            concatenarComUltimoElemento(entrada);
        }
        else if (!ultimoEhOperador && entradaEhOperador) {
            // Caso: número seguido de operador - adiciona operador
            adicionar(entrada);
        }
        else if (ultimoEhOperador && entradaEhOperador) {
            // Caso: operador seguido de operador - substitui
            substituirUltimoElemento(entrada);
        }
        else {
            // Caso: operador seguido de número - adiciona número
            adicionar(entrada);
        }
    }

    /**
     * Limpa a calculadora e atualiza a interface
     */
    private void limparCalculadora() {
        limpar();
        atualizarTela("", lblExpressaoAnterior);
        atualizarTela("", lblExpressaoAtual);
    }

    /**
     * Calcula a expressão atual, se válida, e atualiza a interface
     */
    private void calcularExpressao() {
        if (calculadoraService.ehExpressao(expressaoAtual)) {
            executar();
            atualizarTela(expressaoAnterior + " = ", lblExpressaoAnterior);
            atualizarTela(transformaEmString(expressaoAtual), lblExpressaoAtual);
        }
    }

    /**
     * Obtém o último elemento da expressão atual
     * @return O último elemento ou uma string vazia se a lista estiver vazia
     */
    private String obterUltimoElemento() {
        if (expressaoAtual.isEmpty()) {
            return "";
        }
        return expressaoAtual.get(expressaoAtual.size() - 1);
    }

    /**
     * Concatena o texto com o último elemento da expressão
     * @param texto O texto a ser concatenado
     */
    private void concatenarComUltimoElemento(String texto) {
        int ultimoIndice = expressaoAtual.size() - 1;
        String ultimoElemento = expressaoAtual.get(ultimoIndice);
        alterar(ultimoIndice, ultimoElemento + texto);
    }

    /**
     * Substitui o último elemento da expressão
     * @param texto O novo valor para o último elemento
     */
    private void substituirUltimoElemento(String texto) {
        alterar(expressaoAtual.size() - 1, texto);
    }

    /**
     * Atualiza o texto de um label
     * @param texto O novo texto
     * @param label O label a ser atualizado
     */
    private void atualizarTela(String texto, Label label) {
        label.setText(texto);
    }

    /**
     * Limpa a expressão atual
     */
    private void limpar() {
        this.expressaoAtual.clear();
    }

    /**
     * Adiciona um elemento à expressão atual
     * @param texto O elemento a ser adicionado
     */
    private void adicionar(String texto) {
        this.expressaoAtual.add(texto);
    }

    /**
     * Altera um elemento na posição especificada
     * @param indice A posição do elemento a ser alterado
     * @param novoValor O novo valor para o elemento
     * @throws IndexOutOfBoundsException se o índice for inválido
     */
    private void alterar(int indice, String novoValor) {
        if (indice >= 0 && indice < expressaoAtual.size()) {
            expressaoAtual.set(indice, novoValor);
        } else {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
    }

    /**
     * Executa a expressão matemática atual
     */
    private void executar() {
        this.expressaoAnterior = transformaEmString(expressaoAtual);
        this.expressaoAtual = calculadoraService.executarExpressao(expressaoAtual);
    }

    /**
     * Transforma uma lista de strings em uma única string
     * @param expressao A lista de strings
     * @return A string resultante
     */
    private String transformaEmString(List<String> expressao) {
        return String.join("", expressao);
    }
}
