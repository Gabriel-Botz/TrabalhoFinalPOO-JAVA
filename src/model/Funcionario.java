package model;

import service.Calculo;
import java.util.ArrayList;
import java.util.List;

public class Funcionario extends Pessoa implements Calculo{
    private double salarioBruto;
    private double descontoInss;
    private double descontoIr;
    private List<Dependente> dependentes = new ArrayList<>();

    @Override
    public double calcularInss() {
        double[][] faixas = {
                {1320.00, 0.075}, {2571.29, 0.09}, {3856.94, 0.12}, {7786.02, 0.14}
        };

        double totalInss = 0.0;
        double limiteAnterior = 0.0;

        for (double[] faixa : faixas){
            double limiteSuperior = faixa [0];
            double aliquota = faixa[1];

            if(salarioBruto <= limiteAnterior) break;
        }

        this.descontoInss = totalInss;
        return totalInss;
    }

    @Override
    public double calcularIr() {
        double deducaoDependentes=dependentes.size()*189.59;
        double baseCalculo = salarioBruto-descontoInss-deducaoDependentes;

        if (baseCalculo <= 0){
            this.descontoIr=0.0;
            return 0.0;
        }
        double[][] tabelaIr ={
                {2259.20, 0.0, 0.0}, { 2826.65, 0.075, 169.44 }, { 3751.05, 0.15,  381.44 }, { 4664.68, 0.225, 662.77 }, { Double.MAX_VALUE, 0.275, 896.00 }
        };

        double ir=0.0;

        for (double[] faixa : tabelaIr){
            double limite = faixa[0];
            double aliquota = faixa[1];
            double parcelaDeduzida = faixa[2];

            if (baseCalculo <= limite){
                ir = (baseCalculo*aliquota)-parcelaDeduzida;
                break;
            }
        }

        this.descontoIr = Math.max(0.0, ir);
        return this.descontoIr;
    }

    public double calcularSalarioLiquido(){
        calcularIr();
        calcularInss();
        return salarioBruto-descontoIr-descontoIr;
    }
}
