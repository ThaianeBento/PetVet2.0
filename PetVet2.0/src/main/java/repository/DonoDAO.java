package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Dono;

import java.util.List;

public class DonoDAO {
    private EntityManagerFactory emf;

    public DonoDAO() {
        emf = Persistence.createEntityManagerFactory("testes");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public void salvarDono(Dono dono) {
        if (dono == null) {
            throw new IllegalArgumentException("O objeto Dono não pode ser nulo.");
        }
        if (dono.getTelefone().length() != 11) {
            throw new IllegalArgumentException(" O telefone precisa ter 11 dígitos, incluindo o DDD.");  // Alterei a mensagem aqui
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(dono);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar o Dono: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Dono findByCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio.");
        }
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Dono> query = em.createQuery("SELECT d FROM Dono d WHERE d.cpf = :cpf", Dono.class);
            query.setParameter("cpf", cpf);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Dono pelo CPF: " + cpf, e);
        } finally {
            em.close();
        }
    }

    public List<Dono> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Dono d", Dono.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos os Donos: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void updatePorCampo(String cpf, String novoNome, Integer novaIdade, String novoEstado, String novaCidade, String novoTelefone) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio.");
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Dono dono = em.createQuery("SELECT d FROM Dono d WHERE d.cpf = :cpf", Dono.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();

            if (novoNome != null && !novoNome.isEmpty()) {
                dono.setNome(novoNome);
            }
            if (novaIdade != null && (novaIdade > 0 && novaIdade < 120)) {
                dono.setIdade(novaIdade);
            }
            if (novoEstado != null && !novoEstado.isEmpty()) {
                dono.setEstado(novoEstado);
            }
            if (novaCidade != null && !novaCidade.isEmpty()) {
                dono.setCidade(novaCidade);
            }
            if (novoTelefone != null && !novoTelefone.isEmpty() && novoTelefone.length() == 11) {
                dono.setTelefone(novoTelefone);
            }

            em.merge(dono);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar Dono: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void delete(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio.");
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Dono dono = em.createQuery("SELECT d FROM Dono d WHERE d.cpf = :cpf", Dono.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();

            if (dono != null) {
                em.remove(dono);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao deletar Dono: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
