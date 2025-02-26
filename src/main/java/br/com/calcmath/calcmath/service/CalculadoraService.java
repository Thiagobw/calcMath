package br.com.calcmath.calcmath.service;

import br.com.calcmath.calcmath.model.Operacao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraService {

    public List<String> executarExpressao(List<String> expressao) {

        List<Double> numeros = new ArrayList<>();
        List<Operacao> operadores = new ArrayList<>();

        // Separar números e operadores
        for (String elemento : expressao) {
            if (ehNumero(elemento)) {
                numeros.add(Double.parseDouble(elemento));
            } else if (ehOperador(elemento)) {
                operadores.add(Operacao.fromSimbolo(elemento));
            } else {
                throw new IllegalArgumentException("Caractere inválido na expressão: " + elemento);
            }
        }

        if (numeros.size() <= operadores.size()) {
            throw new IllegalArgumentException("Expressão mal formada!");
        }

        // Primeiro, resolver multiplicação e divisão
        List<Double> tempNumeros = new ArrayList<>(numeros);
        List<Operacao> tempOperadores = new ArrayList<>(operadores);

        for (int i = 0; i < tempOperadores.size(); ) {
            Operacao op = tempOperadores.get(i);
            if (op == Operacao.MULTIPLICACAO || op == Operacao.DIVISAO) {
                double num1 = tempNumeros.get(i);
                double num2 = tempNumeros.get(i + 1);
                double resultado = op.executar(num1, num2);

                tempNumeros.set(i, resultado);
                tempNumeros.remove(i + 1);
                tempOperadores.remove(i);
            } else {
                i++;
            }
        }

        // Agora, resolver soma e subtração
        double resultadoFinal = tempNumeros.get(0);
        for (int i = 0; i < tempOperadores.size(); i++) {
            resultadoFinal = tempOperadores.get(i).executar(resultadoFinal, tempNumeros.get(i + 1));
        }

        // Retorna nova lista com o resultado para continuar a expressão
        return new ArrayList<>(List.of(formatarNumero(resultadoFinal)));
    }

    /**
     * Verifica se a lista representa uma expressão matemática válida.
     * Uma expressão válida segue o padrão: número, operador, número, operador, número...
     */
    public boolean ehExpressao(List<String> expressao) {
        if (expressao == null || expressao.isEmpty()) {
            return false;
        }

        // Uma expressão deve começar com um número
        if (!ehNumero(expressao.get(0))) {
            return false;
        }

        // Uma expressão deve terminar com um número
        if (!ehNumero(expressao.get(expressao.size() - 1))) {
            return false;
        }

        // Verificar alternância entre números e operadores
        boolean deveSer = false; // false = número, true = operador
        int numCount = 0;
        int opCount = 0;

        for (String elemento : expressao) {
            if (elemento.isBlank()) {
                continue; // Ignora espaços em branco
            }

            if (deveSer) { // Deve ser um operador
                if (!ehOperador(elemento)) {
                    return false; // Encontrou número quando esperava operador
                }
                opCount++;
            } else { // Deve ser um número
                if (!ehNumero(elemento)) {
                    return false; // Encontrou operador quando esperava número
                }
                numCount++;
            }

            deveSer = !deveSer; // Alterna o tipo esperado
        }

        // Uma expressão válida tem exatamente um número a mais que operadores
        return numCount == opCount + 1;
    }

    /**
     * Verifica se a string representa um número válido (inteiro ou decimal).
     */
    public boolean ehNumero(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Verifica se a string representa um operador matemático válido.
     */
    public boolean ehOperador(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        return switch (s) {
            case "+", "-", "x", "/" -> true;
            default -> false;
        };
    }

    /**
     * Formata um número para exibição, removendo casas decimais se for um valor inteiro.
     * @param numero O número a ser formatado
     * @return String formatada do número
     */
    private String formatarNumero(double numero) {
        // Verifica se o número é efetivamente um inteiro
        if (numero == Math.floor(numero) && !Double.isInfinite(numero)) {
            return String.valueOf((int) numero);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(numero).replace(',', '.');
    }
}
