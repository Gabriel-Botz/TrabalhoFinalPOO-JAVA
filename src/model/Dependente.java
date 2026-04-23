package model;

import exception.DependenteException;
import java.time.LocalDate;
import java.time.Period;

public class Dependente extends Pessoa {

    private Parentesco parentesco;

    private static int contadorDependentes = 0;

    public Dependente(String nome, String cpf, LocalDate dataNascimento, Parentesco parentesco) {
        super(nome, cpf, dataNascimento);

        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        if (idade >= 18) {
            throw new DependenteException("Dependente deve ser menor de 18 anos!");
        }

        this.parentesco = parentesco;
        contadorDependentes++;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public static int getContadorDependentes() {
        return contadorDependentes;
    }

    @Override
    public String toString() {
        return "Nome: " + getNome() + "\nCPF: " + getCpf() + "\nParentesco: " + getParentesco();
    }
}