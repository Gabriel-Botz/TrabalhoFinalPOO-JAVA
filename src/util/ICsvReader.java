package util;

import model.Funcionario;

import java.util.List;

public interface ICsvReader {
    List<Funcionario> lerArquivo(String caminho);
}