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

        if (salarioBruto <= 0) {
            throw new IllegalArgumentException("O salário não pode ser menor ou igual a 0!");
        }

        this.salarioBruto = salarioBruto;
        this.dependentes = new ArrayList<>();
        contadorFuncionarios++;
    }

    public void adicionarDependente(Dependente d) {
        this.dependentes.add(d);
    }

    @Override
    public double calcularInss() {
        double salario = Math.min(getSalarioBruto(), 8157.41);
        double valorDescontoINSS = 0;

        double faixa1 = Math.min(salario, 1518.00);
        valorDescontoINSS = faixa1 * 0.075;

        if (salario > 1518.00) {
            double faixa2 = Math.min(salario, 2793.88);
            valorDescontoINSS += (faixa2 - 1518.00) * 0.09;
        }

        if (salario > 2793.88) {
            double faixa3 = Math.min(salario, 4190.83);
            valorDescontoINSS += (faixa3 - 2793.88) * 0.12;
        }

        if (salario > 4190.83) {
            double faixa4 = Math.min(salario, 8157.41);
            valorDescontoINSS += (faixa4 - 4190.83) * 0.14;
        }

        return valorDescontoINSS;
    }

    @Override
    public double calcularIr() {
        double inss = calcularInss();
        double baseCalculo = getSalarioBruto() - inss - (189.59 * dependentes.size());

        if (baseCalculo <= 2259.20) {
            return 0;
        }

        double valorDescontoIR = 0;

        if (baseCalculo > 2259.20) {
            double faixa1 = Math.min(baseCalculo, 2826.65);
            valorDescontoIR = (faixa1 - 2259.20) * 0.075;
        }

        if (baseCalculo > 2826.65) {
            double faixa2 = Math.min(baseCalculo, 3751.05);
            valorDescontoIR += (faixa2 - 2826.65) * 0.15;
        }

        if (baseCalculo > 3751.05) {
            double faixa3 = Math.min(baseCalculo, 4664.68);
            valorDescontoIR += (faixa3 - 3751.05) * 0.225;
        }

        if (baseCalculo > 4664.68) {
            valorDescontoIR += (baseCalculo - 4664.68) * 0.275;
        }

        return valorDescontoIR;
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
                "\nDesconto INSS: " + descontoInss;
    }
}