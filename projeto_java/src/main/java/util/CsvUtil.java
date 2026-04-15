package util;

import exception.DependenteException;
import model.Dependente;
import model.Funcionario;
import model.Parentesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
            while((novaLinha = br.readLine()) != null){
                if(novaLinha.trim().isEmpty()){
                    proximoEhFuncionario = true;
                    continue;
                }

                String[] colunas = novaLinha.split(";");
                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMdd");

                if(proximoEhFuncionario){
                    LocalDate dataNascimento = LocalDate.parse(colunas[2], formatador);
                    double salario = Double.parseDouble(colunas[3]);
                    boolean cpfExiste = cpfs.add(colunas[1]);
                    if(cpfExiste) {
                        funcionarioAtual = new Funcionario(colunas[0],colunas[1],dataNascimento, salario);
                        funcionarios.add(funcionarioAtual);
                    } else throw new IllegalArgumentException("CPF Já existe!");

                    proximoEhFuncionario = false;
                } else {
                    LocalDate dataNascimento = LocalDate.parse(colunas[2], formatador);
                    LocalDate dataAtual = LocalDate.now();
                    Period periodoDeVida = Period.between(dataAtual, dataNascimento);
                    int idadeExataDoDependente = periodoDeVida.getYears();
                    if(idadeExataDoDependente >= 18) throw new DependenteException("O dependente " + colunas[0] + " possui " + idadeExataDoDependente + " anos e não pode ser cadastrado (maior de idade).");
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

                    boolean cpfExiste = cpfs.add(colunas[1]);
                    if(cpfExiste){
                        dependenteAtual = new Dependente(colunas[0],colunas[1],dataNascimento, parentesco);
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
    public void escreverArquivo(String caminho, List<Funcionario> funcionarios) {

    }
}
