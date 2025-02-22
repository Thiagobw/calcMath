package br.com.calcmath.calcmath.service;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraService {

    public double avaliarExpressao(String expressao) {
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
