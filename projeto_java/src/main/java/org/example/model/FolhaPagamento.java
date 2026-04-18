package model;

import java.time.LocalDate;

public class FolhaPagamento {

    private int codigo;
    private Funcionario funcionario;
    private LocalDate dataPagamento;
    private double descontoINSS;
    private double descontoIR;
    private double salarioLiquido;

    public FolhaPagamento(int codigo, Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário não pode ser nulo!");
        }
        this.codigo = codigo;
        this.funcionario = funcionario;
        this.dataPagamento = LocalDate.now();
        this.descontoINSS = funcionario.calcularInss();
        this.descontoIR = funcionario.calcularIr();
        this.salarioLiquido = funcionario.getSalarioBruto() - descontoINSS - descontoIR;
    }

    public int getCodigo() {
        return codigo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
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

    public double getSalarioLiquido() {
        return salarioLiquido;
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
                "\nFuncionário: " + funcionario.getNome() +
                "\nData de Pagamento: " + dataPagamento +
                "\nDesconto INSS: " + descontoINSS +
                "\nDesconto IR: " + descontoIR +
                "\nSalário Líquido: " + salarioLiquido;
    }
}