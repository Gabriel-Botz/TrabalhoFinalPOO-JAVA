package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/folha_pagamento";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "123456";
    private static final String SCHEMA = "folha_pagamento";

    public static Connection getConexao() throws SQLException {
        Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        conexao.setSchema(SCHEMA);
        return conexao;
    }
}