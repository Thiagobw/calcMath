package br.com.calcmath.calcmath.service;

import br.com.calcmath.calcmath.model.Operacao;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraService {

    public double avaliarExpressao(String expressao) {
        System.out.println("Expressão recebida: " + expressao);

        expressao = removeEspacos(expressao);

        if (expressao.isEmpty()) {
            throw new IllegalArgumentException("Expressão vazia!");
        }

        List<Double> numeros = new ArrayList<>();
        List<Operacao> operadores = new ArrayList<>();

        StringBuilder numeroAtual = new StringBuilder();

        // Parsing da expressão
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                numeroAtual.append(c);
            } else {
                Operacao operacao = Operacao.fromSimbolo(String.valueOf(c));
                if (operacao != null) {
                    if (!numeroAtual.isEmpty()) {
                        numeros.add(Double.parseDouble(numeroAtual.toString()));
                        numeroAtual.setLength(0);
                    }
                    operadores.add(operacao);
                } else {
                    throw new IllegalArgumentException("Caractere inválido na expressão: " + c);
                }
            }
        }

        // Adiciona o último número armazenado
        if (!numeroAtual.isEmpty()) {
            numeros.add(Double.parseDouble(numeroAtual.toString()));
        }

        if (numeros.size() <= operadores.size()) {
            throw new IllegalArgumentException("Expressão mal formada!");
        }

        // Primeiro, resolvemos multiplicação e divisão
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

        // Agora, resolvemos soma e subtração
        double resultadoFinal = tempNumeros.get(0);
        for (int i = 0; i < tempOperadores.size(); i++) {
            resultadoFinal = tempOperadores.get(i).executar(resultadoFinal, tempNumeros.get(i + 1));
        }

        return resultadoFinal;
    }

    private String removeEspacos(String expressao) {
        return expressao.replaceAll("\\s+", "");
    }
}
