package model;

import service.Calculo;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Funcionario extends Pessoa implements Calculo{
    private double salarioBruto;
    private double descontoInss;
    private double descontoIr;
    private List<Dependente> dependentes = new ArrayList<>();

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

        double valorDescontoINSS = 0;
        double faixa1, faixa2, faixa3, faixa4;

        faixa1 = Math.min(getSalarioBruto(), 1518.00);
        valorDescontoINSS += faixa1 * 0.075;

        if (getSalarioBruto() > 1518.00){
            faixa2 = Math.min(getSalarioBruto(), 2793.88);
            valorDescontoINSS += (faixa2 - 1518.01) * 0.09;
        }
        if (getSalarioBruto() > 2793.88) {
            faixa3 = Math.min(getSalarioBruto(), 4190.83);
            valorDescontoINSS += (faixa3 - 2793.88) * 0.12;
        }
        if (getSalarioBruto() > 4190.83) {
            faixa4 = Math.min(getSalarioBruto(), 8157.41);
            valorDescontoINSS += (faixa4 - 4190.83) * 0.14;
        }
        this.descontoInss = valorDescontoINSS;
        return valorDescontoINSS;
    }

    @Override
    public double calcularIr() {

        double faixa1, faixa2, faixa3, faixa4;
        double valorDescontoIR = 0;
        double valorBase = getSalarioBruto() - calcularInss() - (189.59 * dependentes.size());


        if (valorBase > 2259.00){
            faixa1 = Math.min(valorBase, 2826.65);
            valorDescontoIR += (faixa1 - 2259.00) * 0.075;
        }
        if (valorBase > 2826.65){
            faixa2 = Math.min(valorBase, 3751.05);
            valorDescontoIR += (faixa2 - 2826.65) * 0.15;
        }
        if (valorBase > 3751.05){
            faixa3 = Math.min(valorBase, 4664.68);
            valorDescontoIR += (faixa3 - 3751.05) * 0.225;
        }
        if (valorBase > 4664.68){
            faixa4 = (valorBase - 4664.68) * 0.275;
            valorDescontoIR += faixa4;
        }
        this.descontoIr = valorDescontoIR;
        return valorDescontoIR;
    }

    public double calcularSalarioLiquido() {
        this.descontoInss = calcularInss();
        this.descontoIr = calcularIr();
        return salarioBruto - descontoInss - descontoIr;
    }
}
