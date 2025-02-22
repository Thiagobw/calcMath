package br.com.calcmath.calcmath.model;

import java.util.function.BiFunction;

public enum Operacao {
    SOMA("+", (a, b) -> a + b),
    SUBTRACAO("-", (a, b) -> a - b),
    MULTIPLICACAO("*", (a, b) -> a * b),
    DIVISAO("/", (a, b) -> {
        if (b == 0) throw new ArithmeticException("Divisão por zero!");
        return a / b;
    });

    private final String simbolo;
    private final BiFunction<Double, Double, Double> operacao;

    Operacao(String simbolo, BiFunction<Double, Double, Double> operacao) {
        this.simbolo = simbolo;
        this.operacao = operacao;
    }

    public double executar(double a, double b) {
        return operacao.apply(a, b);
    }

    public String getSimbolo() {
        return simbolo;
    }
}
