package service;

import model.FolhaPagamento;
import model.Funcionario;

import java.util.List;

/**
 * Interface que define os serviços de manipulação e processamento de dados via CSV.
 * Atua como o orquestrador entre a leitura de dados brutos, regras de cálculo e saída de resultados.
 */
public interface ICsvService {
    /**
     * Realiza a leitura e tradução do arquivo CSV de entrada para objetos de domínio.
     * * O processo utiliza uma máquina de estados para identificar blocos de funcionários
     * e seus respectivos dependentes separados por linhas em branco.
     * @param caminhoEntrada O caminho do arquivo .csv no sistema de arquivos.
     * @return Uma lista de objetos {@link model.Funcionario} com dependentes vinculados.
     * @throws exception.DependenteException Caso um dependente não atenda aos requisitos de idade.
     * @throws IllegalArgumentException Caso existam CPFs duplicados no arquivo.
     */
    List<Funcionario> carregarEntrada(String caminhoEntrada);

    /**
     * Processa a lista de funcionários bruta para gerar os recibos da folha de pagamento.
     * <p>
     * Este método atua como um orquestrador: ele itera sobre a lista de funcionários
     * e delega o cálculo matemático dos impostos para a própria entidade (que deve
     * implementar a interface {@link service.Calculavel}).
     * </p>
     * <p>
     * Após receber os valores de <b>INSS</b> e <b>IR</b>, o método apura o salário líquido
     * e consolida todas as informações.
     * </p>
     * * @param funcionarios A lista de objetos {@link model.Funcionario} previamente carregada pelo utilitário.
     * @return Uma lista de instâncias de {@link model.FolhaPagamento} contendo o espelho de cálculos do mês.
     */
    List<FolhaPagamento> calcularFolhaDePagamento(List<Funcionario> funcionarios);
}
