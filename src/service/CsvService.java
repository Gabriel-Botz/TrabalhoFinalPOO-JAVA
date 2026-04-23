package service;

import model.FolhaPagamento;
import model.Funcionario;
import util.ICsvReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvService implements ICsvService {

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

    @Override
    public void gerarSaida(List<FolhaPagamento> folhasPagamento, String caminhoSaida) {
        // o caminho já chega com data e hora do Main, não precisa gerar aqui
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoSaida))) {
            for (FolhaPagamento folha : folhasPagamento) {
                String nome = folha.getFuncionario().getNome();
                String cpf = folha.getFuncionario().getCpf();
                double inss = folha.getDescontoINSS();
                double ir = folha.getDescontoIR();
                double liquido = folha.getSalarioLiquido();

                String linha = String.format(new Locale("pt", "BR"),
                        "%s;%s;%.2f;%.2f;%.2f",
                        nome, cpf, inss, ir, liquido);

                bw.write(linha);
                bw.newLine();
            }
            System.out.println("Arquivo de saída gerado: " + caminhoSaida);
        } catch (IOException e) {
            System.out.println("Erro ao gerar arquivo de saída: " + e.getMessage());
        }
    }

    public void setCsvUtil(ICsvReader csvUtil) {
        this.csvUtil = csvUtil;
    }
}