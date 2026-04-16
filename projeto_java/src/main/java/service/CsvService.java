package service;

import model.FolhaPagamento;
import model.Funcionario;
import util.ICsvUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvService implements ICsvService{
    private ICsvUtil csvUtil;

    public CsvService(ICsvUtil csvUtil) {
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

    @Override
    public void gerarSaida(List<FolhaPagamento> folhasPagamento, String caminhoSaida) {

    }

    public void setCsvUtil(ICsvUtil csvUtil) {
        this.csvUtil = csvUtil;
    }
}
