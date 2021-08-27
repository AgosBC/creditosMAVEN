package ar.com.ada.creditos.managers;

import ar.com.ada.creditos.entities.*;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.metamodel.model.domain.internal.PluralAttributeBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

public class PrestamoManager {

    protected SessionFactory sessionFactory; // buscar funcion de esto

    public void setup() {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings
                                                                                                  // from
                                                                                                  // hibernate.cfg.xml

                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw ex;
        }

    }

    public void exit() {
        sessionFactory.close();
    }

    // cargar un prestamo a un cliente existente
    // opcion listar prestamos

    public void create(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    public Prestamo read(int prestamoId) {

        Session session = sessionFactory.openSession();

        Prestamo prestamo = session.get(Prestamo.class, prestamoId);

        session.close();

        return prestamo;
    }

    /*
     * public Cliente readByCliente(int dni) { que prermita buscar los prestamos que
     * tiene un cliente deberia agregar en cliente o usar el de Id //estaria aca, en
     * cliente manager o en creditos? - CREDITOS - RECORDATORIO, CAMBIAR Session
     * session = sessionFactory.openSession();
     * 
     * Cliente cliente = session.byNaturalId(Cliente.class).using("dni",
     * dni).load();
     * 
     * session.close();
     * 
     * return prestamo; }
     */

    public void update(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Prestamo prestamo) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(prestamo);

        session.getTransaction().commit();
        session.close();
    }

    public List<Prestamo> buscarPrestamos() { // uitar mas adelante

        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery("SELECT * FROM prestamo", Prestamo.class);
        // query = session.createQuery("From Obse")
        List<Prestamo> todos = query.getResultList();

        return todos;

    }

    // sumar todos los prestamos de un cliente
    // could not resolve property: clienteId of:
    // ar.com.ada.creditos.entities.Prestamo [SELECT SUM(importe) FROM
    // ar.com.ada.creditos.entities.
    // cambio: int cliente id a cliente cliente + otro metodo que pida el cliente id
    // y lo asocie a su objeto Cliente class y ese sea ingresado en este parametro
    // public bigdecimal totalprestamos cliente (Cliente cliente)
    // public Cliente buscarCliente (int clienteId)

    public BigDecimal totalPrestamoCliente(int clienteId) {

        Session session = sessionFactory.openSession();

        Query query = session.createQuery("SELECT SUM(importe) FROM Prestamo p WHERE p.clienteId= :clienteid",
                Prestamo.class);
        query.setParameter("clienteid", clienteId);

        double total = ((Number) query.getSingleResult()).doubleValue();
        BigDecimal totalPrestamo = new BigDecimal(total);

        return totalPrestamo;

    }

    public BigDecimal totalPrestamoCliente(Cliente cliente) {

        Session session = sessionFactory.openSession();

        Query query = session.createQuery("SELECT SUM(importe) FROM Prestamo p WHERE p.cliente = :clientex",
                Prestamo.class);
        query.setParameter("clientex", cliente);

        double total = ((Number) query.getSingleResult()).doubleValue();
        BigDecimal totalPrestamo = new BigDecimal(total);

        return totalPrestamo;

    }

    // Query queryConJPQL = session.createQuery("SELECT c FROM Cliente c where
    // c.nombre = :nombreFiltro", //usan aleas y Cliente (clase en java, no lista en
    // mysql)
    // Cliente.class);
    // queryConJPQL.setParameter("nombreFiltro", nombre);

    // List<Cliente> clientesDeJPQL = queryConJPQL.getResultList();

}
