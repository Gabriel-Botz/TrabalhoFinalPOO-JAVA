package model;

import java.time.LocalDate;

public class FolhaPagamento {

        private int codigo;
        private Funcionario funcionario;
        private LocalDate dataPagamento;
        private double descontoINSS;
        private double descontoIR;
        private double salarioLiquido;


    public FolhaPagamento(int codigo, LocalDate dataPagamento, double descontoINSS, double descontoIR, Funcionario funcionario) {
            this.codigo = codigo;
            this.dataPagamento = dataPagamento;
            this.descontoINSS = descontoINSS;
            this.descontoIR = descontoIR;
        this.funcionario = funcionario;
        this.salarioLiquido = funcionario.getSalarioBruto() - descontoINSS - descontoIR;
    }

    public int getCodigo() {
        return codigo;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public double getDescontoINSS() {
        return descontoINSS;
    }

    public double getDescontoIR() {
        return descontoIR;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public double getSalarioLiquido() {
        return salarioLiquido;
    }

    @Override
    public String toString(){
        return "Nome: " + funcionario.getNome() + "\nSalário bruto: " + funcionario.getSalarioBruto()
                + "\nDesconto INSS: " + getDescontoINSS() + "\nDesconto IR: " + getDescontoIR() + "\nSalario liquido: "
                + salarioLiquido;
    }
}