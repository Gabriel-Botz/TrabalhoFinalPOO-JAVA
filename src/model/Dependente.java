package model;

import exception.DependenteException;
import java.time.LocalDate;
import java.time.Period;

public class Dependente extends Pessoa {

    private Parentesco parentesco;

    public Dependente(String nome, String cpf, LocalDate dataNascimento, Parentesco parentesco) {
        super(nome, cpf, dataNascimento);
        this.parentesco = parentesco;

        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            if (idade >= 18){
                throw new DependenteException("Dependente deve ser menor de 18 anos!");
            }
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    @Override
    public String toString(){
        return "Nome: " + getNome() + "\nCPF: " + getCpf() + "\nParentesco: " + getParentesco();
    }
}