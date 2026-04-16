package util;

import exception.DependenteException;
import model.Dependente;
import model.FolhaPagamento;
import model.Funcionario;
import model.Parentesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CsvUtil implements ICsvUtil {
    @Override
    public List<Funcionario> lerArquivo(String caminho) {
        List<Funcionario> funcionarios = new ArrayList<>();
        Funcionario funcionarioAtual = null;
        Dependente dependenteAtual = null;
        boolean proximoEhFuncionario = true;
        try(BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String novaLinha;
            HashSet<String> cpfs = new HashSet<>();
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMdd");
            while((novaLinha = br.readLine()) != null){
                if(novaLinha.trim().isEmpty()){
                    proximoEhFuncionario = true;
                    continue;
                }

                String[] colunas = novaLinha.split(";");

                if(proximoEhFuncionario){
                    String nome = colunas[0];
                    String cpf = colunas[1];
                    LocalDate dataNascimento = LocalDate.parse(colunas[2], formatador);
                    double salario = Double.parseDouble(colunas[3]);
                    boolean cpfInedito = cpfs.add(cpf);
                    if(cpfInedito) {
                        funcionarioAtual = new Funcionario(nome, cpf, dataNascimento, salario);
                        funcionarios.add(funcionarioAtual);
                    } else throw new IllegalArgumentException("CPF Já existe!");

                    proximoEhFuncionario = false;
                } else {
                    String nome = colunas[0];
                    String cpf = colunas[1];
                    LocalDate dataNascimento = LocalDate.parse(colunas[2], formatador);
                    LocalDate dataAtual = LocalDate.now();
                    Period periodoDeVida = Period.between(dataNascimento, dataAtual);
                    int idadeExataDoDependente = periodoDeVida.getYears();
                    if(idadeExataDoDependente >= 18) throw new DependenteException("O dependente " + nome + " possui " + idadeExataDoDependente + " anos e não pode ser cadastrado (maior de idade).");
                    Parentesco parentesco = null;
                    switch (colunas[3].toUpperCase()){
                        case "FILHO":
                            parentesco = Parentesco.FILHO;
                            break;
                        case "SOBRINHO":
                            parentesco = Parentesco.SOBRINHO;
                            break;
                        case "OUTROS":
                            parentesco = Parentesco.OUTROS;
                            break;
                        default:
                            throw new IllegalArgumentException("Parentesco inesperado.");
                    }

                    boolean cpfInedito = cpfs.add(cpf);
                    if(cpfInedito){
                        dependenteAtual = new Dependente(nome, cpf, dataNascimento, parentesco);
                        funcionarioAtual.adicionarDependente(dependenteAtual);
                    } else throw new IllegalArgumentException("CPF Já existe!");
                }

            }

        } catch (IOException e) {
            System.err.println("Erro crítico de leitura no sistema: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Atenção - Arquivo com dados inválidos: " + e.getMessage());
        } catch (DependenteException e) {
            System.err.println("Atenção - Dependente rejeitado: " + e.getMessage());
        }

        return funcionarios;
    }

    @Override
    public void escreverArquivo(String caminho, List<FolhaPagamento> folhasPagamento) {

    }
}
