package util;

import model.FolhaPagamento;
import model.Funcionario;

import java.util.List;

public interface ICsvUtil {
    List<Funcionario> lerArquivo(String caminho);
    void escreverArquivo(String caminho, List<FolhaPagamento> folhasPagamento);
}
