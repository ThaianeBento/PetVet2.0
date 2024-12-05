package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import model.Dono;
import model.Pet;

import java.util.List;

public class PetDAO {

    private EntityManagerFactory emf;

    public PetDAO() {
        emf = Persistence.createEntityManagerFactory("testes");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void salvarPet(Pet pet) {
        if (pet.getDataNascimento() == null) {
            throw new IllegalArgumentException("A data de nascimento não pode ser nula.");
        }
        if (pet.getNome() == null){
            throw new IllegalArgumentException("O nome do pet não pode ser nulo ou vazio.");
        }
        if (pet.getDono() == null){
            throw new IllegalArgumentException("O dono do pet não pode ser nulo.");
        }
        if (pet.getTipo()== null){
            throw new IllegalArgumentException("O tipo do pet não pode ser nulo ou vazio");
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pet);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Pet findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pet.class, id);
        } finally {
            em.close();
        }
    }

    public List<Pet> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pet p", Pet.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void updatePet(Long id, String novoNome, String novoTipo, Long novoDonoId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Pet pet = em.find(Pet.class, id);

            if (pet == null) {
                throw new IllegalArgumentException("Pet com ID " + id + " não encontrado.");
            }

            if (novoNome != null && !novoNome.trim().isEmpty()) {
                pet.setNome(novoNome);
            }
            if (novoTipo != null && !novoTipo.trim().isEmpty()) {
                pet.setTipo(novoTipo);
            }
            if (novoDonoId != null) {
                pet.setDono(em.getReference(Dono.class, novoDonoId));
            }

            em.merge(pet);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Pet pet = em.find(Pet.class, id);
            if (pet != null) {
                em.remove(pet);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Novo método para buscar pet pelo nome
    public Pet findByName(String nome) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pet p WHERE p.nome = :nome", Pet.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Caso não encontre nenhum pet com o nome especificado.
        } finally {
            em.close();
        }
    }
}
