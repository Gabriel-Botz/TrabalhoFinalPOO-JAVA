package main;

import dao.DependenteDAO;
import dao.FolhaPagamentoDAO;
import dao.FuncionarioDAO;
import model.Dependente;
import model.FolhaPagamento;
import model.Funcionario;
import service.CsvService;
import util.CsvReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do arquivo de entrada: ");
        String caminhoEntrada = scanner.nextLine();

        System.out.print("Digite o nome do arquivo de saída: ");
        String caminhoSaida = scanner.nextLine();

        // gera data e hora para o nome dos arquivos de saída e rejeitados
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String caminhoRejeitados = caminhoSaida.replace(".csv", "_rejeitados_" + dataHora + ".csv");
        String caminhoSaidaFinal = caminhoSaida.replace(".csv", "_" + dataHora + ".csv");

        // 1. lê o CSV de entrada
        CsvService csvService = new CsvService(new CsvReader(caminhoRejeitados));
        List<Funcionario> funcionarios = csvService.carregarEntrada(caminhoEntrada);

        // 2. insere funcionários e dependentes no banco
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        DependenteDAO dependenteDAO = new DependenteDAO();

        for (Funcionario f : funcionarios) {
            try {
                funcionarioDAO.inserir(f);

                int idFuncionario = funcionarioDAO.buscarIdPorCpf(f.getCpf());

                for (Dependente d : f.getDependentes()) {
                    try {
                        dependenteDAO.inserir(d, idFuncionario);
                    } catch (SQLException e) {
                        salvarRejeitado(caminhoRejeitados, d.getNome(), "CPF duplicado no banco");
                        System.err.println("Erro ao inserir dependente: " + e.getMessage());
                    }
                }
            } catch (SQLException e) {
                salvarRejeitado(caminhoRejeitados, f.getNome(), "CPF duplicado no banco");
                System.err.println("Erro ao inserir funcionário: " + e.getMessage());
            }
        }

        // 3. calcula folha e gera CSV de saída
        List<FolhaPagamento> folhas = csvService.calcularFolhaDePagamento(funcionarios);
        csvService.gerarSaida(folhas, caminhoSaidaFinal);

        // 4. insere folha de pagamento no banco
        FolhaPagamentoDAO folhaPagamentoDAO = new FolhaPagamentoDAO();
        for (FolhaPagamento fp : folhas) {
            try {
                int idFuncionario = funcionarioDAO.buscarIdPorCpf(fp.getFuncionario().getCpf());
                folhaPagamentoDAO.inserir(fp, idFuncionario);
            } catch (SQLException e) {
                System.err.println("Erro ao inserir folha: " + e.getMessage());
            }
        }

        System.out.println("Processamento concluído!");
        scanner.close();
    }

    private static void salvarRejeitado(String caminho, String descricao, String motivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho, true))) {
            bw.write(descricao + ";" + motivo);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar rejeitado: " + e.getMessage());
        }
    }
}