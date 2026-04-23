package dao;

import model.FolhaPagamento;
import java.sql.*;

public class FolhaPagamentoDAO {

    public void inserir(FolhaPagamento fp, int idFuncionario) throws SQLException {
        String sql = "INSERT INTO folha_pagamento.folha_pagamento (id_funcionario, data_pagamento, desconto_inss, desconto_ir, salario_liquido) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncionario);
            ps.setDate(2, Date.valueOf(fp.getDataPagamento()));
            ps.setDouble(3, fp.getDescontoINSS());
            ps.setDouble(4, fp.getDescontoIR());
            ps.setDouble(5, fp.getSalarioLiquido());
            ps.executeUpdate();
            System.out.println("Folha inserida para: " + fp.getFuncionario().getNome());
        }
    }
}