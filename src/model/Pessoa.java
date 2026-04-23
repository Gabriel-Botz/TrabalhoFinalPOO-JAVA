package model;

import java.time.LocalDate;

public abstract class Pessoa {

    private String nome;
    private String cpf;
    private LocalDate dataNascimento;

    public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode estar vazio!");
        }
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido, deve ter 11 dígitos!");
        }
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula!");
        }
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + "\nCPF: " + cpf + "\nData de Nascimento: " + dataNascimento;
    }
}