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

    public Funcionario(String nome, String cpf, LocalDate dataNascimento, double salarioBruto){

        super(nome, cpf, dataNascimento);

        this.dependentes = new ArrayList<>();

        if (salarioBruto <= 0){
           throw new IllegalArgumentException("O salário não pode ser menor ou igual a 0!");
       }
        this.salarioBruto = salarioBruto;
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
        if (salarioBruto <= 0){
            throw new IllegalArgumentException("O salário bruto não pode ser menor ou igual a 0!");
        }
        this.salarioBruto = salarioBruto;
    }

    public void adicionarDependente(Dependente d){
        this.dependentes.add(d);
    }

    @Override
    public String toString(){
        return "Nome:" + getNome() + "\nCPF: " + getCpf() + "\nSalário bruto: " + salarioBruto + "\nDesconto INSS: " + descontoInss + "\nDesconto Imposto de Renda: " + descontoIr;
    }

    @Override
    public double calcularInss() {

         double valorDescontoINSS = 0;
         double faixa1, faixa2, faixa3, faixa4;

         faixa1 = Math.min(getSalarioBruto(), 1302.00);
         valorDescontoINSS += faixa1 * 0.075;

         if (getSalarioBruto() > 1302.00){
             faixa2 = Math.min(getSalarioBruto(), 2571.29);
             valorDescontoINSS += (faixa2 - 1302.00) * 0.09;
         }
         if (getSalarioBruto() > 2571.29) {
             faixa3 = Math.min(getSalarioBruto(), 3856.94);
             valorDescontoINSS += (faixa3 - 2571.29) * 0.12;
         }
         if (getSalarioBruto()> 3856.94) {
             faixa4 = Math.min(getSalarioBruto(), 7507.49);
             valorDescontoINSS += (faixa4 - 3856.94) * 0.14;
         }
         return valorDescontoINSS;
    }

    @Override
    public double calcularIr() {

        double faixa1, faixa2, faixa3, faixa4;
        double valorDescontoIR = 0;
        double valorBase = getSalarioBruto() - calcularInss() - (189.59 * dependentes.size());


        if (valorBase > 2259.20){
            faixa1 = Math.min(valorBase, 2826.65);
            valorDescontoIR += (faixa1 - 2259.20) * 0.075;
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
        return valorDescontoIR;
    }
}