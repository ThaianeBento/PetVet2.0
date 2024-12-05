package model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class Pessoa {
    private static final Set<String> CPFs_CADASTRADOS = new HashSet<>();

    @Column(nullable = false)
    protected String nome;

    @Column(nullable = false)
    protected int idade;

    @Id
    @Column(nullable = false, unique = true)
    protected String cpf;

    public Pessoa(String nome, int idade, String cpf) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar o nome.");
        }
        if (idade <= 0 || idade >= 120) {
            throw new IllegalArgumentException("A idade deve ser maior que 0 e menor que 120.");
        }
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio.");
        }
        if (CPFs_CADASTRADOS.contains(cpf)) {
            throw new IllegalArgumentException("Este CPF já está cadastrado.");
        }

        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;

        CPFs_CADASTRADOS.add(cpf);
    }

    public Pessoa() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar o nome.");
        }
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade <= 0 || idade >= 120) {
            throw new IllegalArgumentException("A idade não pode ser negativa ou maior que 120.");
        }
        this.idade = idade;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser vazio.");
        }
        if (CPFs_CADASTRADOS.contains(cpf)) {
            throw new IllegalArgumentException("Este CPF já está cadastrado.");
        }

        // Remove o CPF antigo do conjunto e adiciona o novo
        CPFs_CADASTRADOS.remove(this.cpf);
        this.cpf = cpf;
        CPFs_CADASTRADOS.add(cpf);
    }

    @Override
    public String toString() {
        return "Nome : " + this.nome +
                "\nIdade : " + this.idade +
                "\nCPF : " + this.cpf;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pessoa pessoa = (Pessoa) obj;
        return cpf.equals(pessoa.cpf);
    }

    @Override
    public int hashCode() {
        return cpf.hashCode();
    }
}
