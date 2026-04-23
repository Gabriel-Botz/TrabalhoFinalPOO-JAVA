package dao;

import model.Funcionario;
import java.sql.*;

public class FuncionarioDAO {

    public boolean cpfExiste(String cpf) throws SQLException {
        String sql = "SELECT cpf FROM folha_pagamento.funcionario WHERE cpf = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void inserir(Funcionario f) throws SQLException {
        if (cpfExiste(f.getCpf())) {
            throw new SQLException("CPF já existe no banco: " + f.getCpf());
        }
        String sql = "INSERT INTO folha_pagamento.funcionario (nome, cpf, data_nascimento, salario_bruto) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setDate(3, Date.valueOf(f.getDataNascimento()));
            ps.setDouble(4, f.getSalarioBruto());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Funcionário inserido com id: " + rs.getInt(1));
            }
        }
    }

    public int buscarIdPorCpf(String cpf) throws SQLException {
        String sql = "SELECT id FROM folha_pagamento.funcionario WHERE cpf = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Funcionário não encontrado: " + cpf);
        }
    }
}