package model;

import service.Calculavel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Funcionario extends Pessoa implements Calculavel {

    private double salarioBruto;
    private double descontoInss;
    private double descontoIr;
    private List<Dependente> dependentes;

    private static int contadorFuncionarios = 0;

    public Funcionario(String nome, String cpf, LocalDate dataNascimento, double salarioBruto) {
        super(nome, cpf, dataNascimento);
        this.dependentes = new ArrayList<>();
        if (salarioBruto <= 0) {
            throw new IllegalArgumentException("O salário não pode ser menor ou igual a 0!");
        }
        this.salarioBruto = salarioBruto;
        contadorFuncionarios++;
    }

    public void adicionarDependente(Dependente d) {
        this.dependentes.add(d);
    }

    @Override
    public double calcularInss() {
        double valorDescontoINSS = 0;

        double faixa1 = Math.min(getSalarioBruto(), 1518.00);
        valorDescontoINSS += faixa1 * 0.075;

        if (getSalarioBruto() > 1518.00) {
            double faixa2 = Math.min(getSalarioBruto(), 2793.88);
            valorDescontoINSS += (faixa2 - 1518.00) * 0.09;
        }
        if (getSalarioBruto() > 2793.88) {
            double faixa3 = Math.min(getSalarioBruto(), 4190.83);
            valorDescontoINSS += (faixa3 - 2793.88) * 0.12;
        }
        if (getSalarioBruto() > 4190.83) {
            double faixa4 = Math.min(getSalarioBruto(), 8157.41);
            valorDescontoINSS += (faixa4 - 4190.83) * 0.14;
        }

        this.descontoInss = valorDescontoINSS;
        return valorDescontoINSS;
    }

    @Override
    public double calcularIr() {
        double valorDescontoIR = 0;
        double valorBase = getSalarioBruto() - this.descontoInss - (189.59 * dependentes.size());

        if (valorBase > 2259.00) {
            double faixa1 = Math.min(valorBase, 2826.65);
            valorDescontoIR += (faixa1 - 2259.00) * 0.075;
        }
        if (valorBase > 2826.65) {
            double faixa2 = Math.min(valorBase, 3751.05);
            valorDescontoIR += (faixa2 - 2826.65) * 0.15;
        }
        if (valorBase > 3751.05) {
            double faixa3 = Math.min(valorBase, 4664.68);
            valorDescontoIR += (faixa3 - 3751.05) * 0.225;
        }
        if (valorBase > 4664.68) {
            valorDescontoIR += (valorBase - 4664.68) * 0.275;
        }

        this.descontoIr = valorDescontoIR;
        return valorDescontoIR;
    }

    public double calcularSalarioLiquido() {
        this.descontoInss = calcularInss();
        this.descontoIr = calcularIr();
        return salarioBruto - descontoInss - descontoIr;
    }

    public double getSalarioBruto() {
        return salarioBruto;
    }

    public double getDescontoInss() {
        return descontoInss;
    }

    public double getDescontoIr() {
        return descontoIr;
    }

    public List<Dependente> getDependentes() {
        return dependentes;
    }

    public void setDescontoInss(double descontoInss) {
        this.descontoInss = descontoInss;
    }

    public void setDescontoIr(double descontoIr) {
        this.descontoIr = descontoIr;
    }

    public void setSalarioBruto(double salarioBruto) {
        if (salarioBruto <= 0) {
            throw new IllegalArgumentException("O salário bruto não pode ser menor ou igual a 0!");
        }
        this.salarioBruto = salarioBruto;
    }

    public static int getContadorFuncionarios() {
        return contadorFuncionarios;
    }

    @Override
    public String toString() {
        return "Nome: " + getNome() +
                "\nCPF: " + getCpf() +
                "\nSalário bruto: " + salarioBruto +
                "\nDesconto INSS: " + descontoInss +
                "\nDesconto IR: " + descontoIr;
    }
}