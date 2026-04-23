package util;

import exception.CpfDuplicadoException;
import exception.DependenteException;
import model.Dependente;
import model.Funcionario;
import model.Parentesco;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CsvReader implements ICsvReader {

    private String caminhoRejeitados;

    public CsvReader(String caminhoRejeitados) {
        this.caminhoRejeitados = caminhoRejeitados;
    }

    @Override
    public List<Funcionario> lerArquivo(String caminho) {
        List<Funcionario> funcionarios = new ArrayList<>();
        Funcionario funcionarioAtual = null;
        boolean proximoEhFuncionario = true;
        List<String> rejeitados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String novaLinha;
            HashSet<String> cpfs = new HashSet<>();
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMdd");

            while ((novaLinha = br.readLine()) != null) {
                try {
                    if (novaLinha.trim().isEmpty()) {
                        proximoEhFuncionario = true;
                        continue;
                    }

                    String[] colunas = novaLinha.split(";");

                    if (proximoEhFuncionario) {
                        String nome = colunas[0];
                        String cpf = colunas[1];
                        LocalDate dataNascimento = LocalDate.parse(colunas[2], formatador);
                        double salario = Double.parseDouble(colunas[3].replace(",", "."));

                        boolean cpfInedito = cpfs.add(cpf);
                        if (cpfInedito) {
                            funcionarioAtual = new Funcionario(nome, cpf, dataNascimento, salario);
                            funcionarios.add(funcionarioAtual);
                        } else {
                            throw new CpfDuplicadoException("CPF já existe: " + cpf);
                        }
                        proximoEhFuncionario = false;

                    } else {
                        String nome = colunas[0];
                        String cpf = colunas[1];
                        LocalDate dataNascimento = LocalDate.parse(colunas[2], formatador);

                        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
                        if (idade >= 18) {
                            throw new DependenteException("Dependente " + nome + " possui " + idade + " anos");
                        }

                        Parentesco parentesco;
                        switch (colunas[3].toUpperCase()) {
                            case "FILHO": parentesco = Parentesco.FILHO; break;
                            case "SOBRINHO": parentesco = Parentesco.SOBRINHO; break;
                            case "OUTROS": parentesco = Parentesco.OUTROS; break;
                            default: throw new IllegalArgumentException("Parentesco inválido: " + colunas[3]);
                        }

                        boolean cpfInedito = cpfs.add(cpf);
                        if (cpfInedito) {
                            Dependente dependente = new Dependente(nome, cpf, dataNascimento, parentesco);
                            funcionarioAtual.adicionarDependente(dependente);
                        } else {
                            throw new CpfDuplicadoException("CPF de dependente já existe: " + cpf);
                        }
                    }

                } catch (CpfDuplicadoException e) {
                    System.err.println("CPF duplicado: " + e.getMessage());
                    rejeitados.add(e.getMessage() + ";CPF duplicado");
                } catch (DependenteException e) {
                    System.err.println("Dependente rejeitado: " + e.getMessage());
                    rejeitados.add(e.getMessage() + ";Maior de idade");
                } catch (IllegalArgumentException e) {
                    System.err.println("Dado inválido: " + e.getMessage());
                    rejeitados.add(e.getMessage() + ";Dado inválido");
                }
            }

        } catch (IOException e) {
            System.err.println("Erro crítico de leitura: " + e.getMessage());
        }

        salvarRejeitados(rejeitados);
        return funcionarios;
    }

    private void salvarRejeitados(List<String> rejeitados) {
        if (rejeitados.isEmpty()) return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoRejeitados))) {
            bw.write("Descricao;Motivo");
            bw.newLine();
            for (String linha : rejeitados) {
                bw.write(linha);
                bw.newLine();
            }
            System.out.println("Arquivo de rejeitados gerado: " + caminhoRejeitados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar rejeitados: " + e.getMessage());
        }
    }
}