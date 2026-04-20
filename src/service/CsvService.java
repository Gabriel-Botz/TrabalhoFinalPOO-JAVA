package service;

import model.FolhaPagamento;
import model.Funcionario;
import util.ICsvReader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvService implements ICsvService{
    private ICsvReader csvUtil;

    public CsvService(ICsvReader csvUtil) {
        this.csvUtil = csvUtil;
    }

    @Override
    public List<Funcionario> carregarEntrada(String caminhoEntrada) {
        return this.csvUtil.lerArquivo(caminhoEntrada);
    }

    @Override
    public List<FolhaPagamento> calcularFolhaDePagamento(List<Funcionario> funcionarios) {
        int i = 0;
        List<FolhaPagamento> folhasDePagamento = new ArrayList<>();
        for (Funcionario f : funcionarios) {

            double valorInss = f.calcularInss();
            double valorIr = f.calcularIr();

            f.setDescontoInss(valorInss);
            f.setDescontoIr(valorIr);

            FolhaPagamento folhaAtual = new FolhaPagamento(i, LocalDate.now(), valorInss, valorIr, f);
            folhasDePagamento.add(folhaAtual);
            i++;
        }
        return folhasDePagamento;
    }

    public void setCsvUtil(ICsvReader csvUtil) {
        this.csvUtil = csvUtil;
    }
}

