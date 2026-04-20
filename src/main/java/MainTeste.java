//criação de um Main temporário para testar a etapa 4
import model.Dependente;
import model.FolhaPagamento;
import model.Funcionario;
import model.Parentesco;
import service.CsvService;
import util.CsvUtil;

import java.time.LocalDate;
import java.util.List;

public class MainTeste{
    public static void main(String[] args){
        Funcionario f1 = new Funcionario("Maria dos Santos","01234567890",
            LocalDate.of(2000, 2, 28),3500.00);
        try{
            f1.adicionarDependente(new Dependente("João","00011122234",
                LocalDate.of(2018, 3, 1), Parentesco.SOBRINHO));
            f1.adicionarDependente(new Dependente("Joana","09876543201",
                LocalDate.of(2017, 10, 1), Parentesco.FILHO));
        }catch(Exception e){
            System.out.println("Erro ao adicionar dependente! " + e.getMessage());
        }

        Funcionario f2 = new Funcionario("Raquel","01256567450",
            LocalDate.of(2000, 6, 28),9000.00);
        try{
            f2.adicionarDependente(new Dependente("Bruno","00011122235",
                LocalDate.of(2019, 3, 1), Parentesco.FILHO));
        }catch(Exception e){
            System.out.println("Erro ao adicionar dependente! " + e.getMessage());
        }

        List<Funcionario> funcionarios = List.of(f1, f2);

        CsvService service = new CsvService(new CsvUtil());
        List<FolhaPagamento> folhas = service.calcularFolhaDePagamento(funcionarios);

        System.out.println("RESULTADO");
        for(FolhaPagamento folha : folhas){
            System.out.printf("\nNome:    %s%n", folha.getFuncionario().getNome());
            System.out.printf("CPF:     %s%n", folha.getFuncionario().getCpf());
            System.out.printf("INSS:    %.2f%n", folha.getDescontoINSS());
            System.out.printf("IR:      %.2f%n", folha.getDescontoIR());
            System.out.printf("Líquido: %.2f%n", folha.getSalarioLiquido());
        }
        service.gerarSaida(folhas, "gerarSaida(teste).CSV");
    }
}