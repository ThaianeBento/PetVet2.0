package model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dono")
public class Dono extends Pessoa {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dono")
    private Set<Pet> pets = new HashSet<>();

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String telefone;

    public Dono(String nome, int idade, String cpf, String estado, String cidade, String telefone) {
        super(nome, idade, cpf);
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("O estado deve ser informado.");
        }
        this.estado = estado;

        if (cidade == null || cidade.isEmpty()) {
            throw new IllegalArgumentException("A cidade deve ser informada.");
        }
        this.cidade = cidade;

        if (telefone == null || telefone.isEmpty()) {
            throw new IllegalArgumentException("O número de telefone não pode estar vazio.");
        }
        if (telefone.length() != 11) {
            throw new IllegalArgumentException("O telefone precisa ter 11 dígitos, incluindo o DDD.");
        }
        this.telefone = telefone;
    }

    public Dono() {}

    public void setEstado(String estado) {
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar o estado.");
        }
        this.estado = estado;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setCidade(String cidade) {
        if (cidade == null || cidade.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar a cidade.");
        }
        this.cidade = cidade;
    }

    public String getCidade() {
        return this.cidade;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar o telefone.");
        }
        if (telefone.length() != 11) {
            throw new IllegalArgumentException("O telefone informado precisa ter 11 dígitos, incluindo o DDD.");
        }
        this.telefone = telefone;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setPets(Set<Pet> pets) {
        if (pets == null) {
            throw new IllegalArgumentException("O conjunto de pets não pode ser nulo.");
        }
        this.pets = pets;

        for (Pet pet : pets) {
            pet.setDono(this);
        }
    }

    public Set<Pet> getPets() {
        return this.pets;
    }

    public void addPet(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("Pet não pode ser nulo.");
        }
        if (!this.pets.contains(pet)) {
            this.pets.add(pet);
            pet.setDono(this);
        }
    }

    public void removePet(Pet pet) {
        if (pet != null && this.pets.contains(pet)) {
            this.pets.remove(pet);
            pet.setDono(null);
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nEstado: " + this.estado +
                "\nCidade: " + this.cidade +
                "\nTelefone: " + this.telefone +
                "\nPets: " + this.pets;
    }
}
