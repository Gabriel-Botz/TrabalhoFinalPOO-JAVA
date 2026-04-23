package dao;

import model.Dependente;
import java.sql.*;

public class DependenteDAO {

    public boolean cpfExiste(String cpf) throws SQLException {
        String sql = "SELECT cpf FROM folha_pagamento.dependente WHERE cpf = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void inserir(Dependente d, int idFuncionario) throws SQLException {
        if (cpfExiste(d.getCpf())) {
            throw new SQLException("CPF de dependente já existe no banco: " + d.getCpf());
        }
        String sql = "INSERT INTO folha_pagamento.dependente (nome, cpf, data_nascimento, parentesco, id_funcionario) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getNome());
            ps.setString(2, d.getCpf());
            ps.setDate(3, Date.valueOf(d.getDataNascimento()));
            ps.setString(4, d.getParentesco().name());
            ps.setInt(5, idFuncionario);
            ps.executeUpdate();
            System.out.println("Dependente inserido: " + d.getNome());
        }
    }
}