package util;
import model.Dependente;
import model.Funcionario;
import model.Parentesco;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class CsvReader {

    public List<Funcionario> lerArquivo(String nomeArquivo) throws IOException {

        String linha;
        List<Funcionario> funcionarios = new ArrayList<>();

        boolean proximoEhFuncionario = true;
        Funcionario funcionarioAtual = null;

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            while ((linha = br.readLine()) != null) {

                if (linha.isBlank()) {
                    proximoEhFuncionario = true;
                    continue;
                }
                String[] campos = linha.split(";");

                if (proximoEhFuncionario) {
                    funcionarioAtual = new Funcionario(campos[0], campos[1], LocalDate.parse(campos[2], DateTimeFormatter.ofPattern("yyyyMMdd")), Double.parseDouble(campos[3]));
                    funcionarios.add(funcionarioAtual);
                    proximoEhFuncionario = false;
                } else {
                    Dependente d = new Dependente(campos[0], campos[1], LocalDate.parse(campos[2], DateTimeFormatter.ofPattern("yyyyMMdd")), Parentesco.valueOf(campos[3]));
                    funcionarioAtual.adicionarDependente(d);
                }
            }
        }
        return funcionarios;
    }
}