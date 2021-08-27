package ar.com.ada.creditos.managers;

import ar.com.ada.creditos.entities.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

public class ClienteManager {

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

    public void create(Cliente cliente) {

        Session session = sessionFactory.openSession(); // iniciar sesion
        session.beginTransaction();

        session.save(cliente); // guardar cliente

        session.getTransaction().commit(); // commit
        session.close(); // cerrar sesion
    }

    public Cliente read(int clienteId) {//

        Session session = sessionFactory.openSession();

        Cliente cliente = session.get(Cliente.class, clienteId);

        session.close();

        return cliente;
    }

    public Cliente readByDNI(int dni) {
        Session session = sessionFactory.openSession();

        Cliente cliente = session.byNaturalId(Cliente.class).using("dni", dni).load();

        session.close();

        return cliente;
    }

    public void update(Cliente cliente) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(cliente);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Cliente cliente) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(cliente);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Este metodo en la vida real no debe existir ya qeu puede haber miles de
     * usuarios
     * 
     * @return
     */
    public List<Cliente> buscarTodos() { // quitar mas adelante

        Session session = sessionFactory.openSession();

        /// NUNCA HARCODEAR SQLs nativos en la aplicacion.
        // ESTO es solo para nivel educativo
        Query query = session.createNativeQuery("SELECT * FROM cliente", Cliente.class);
        // query = session.createQuery("From Obse")
        List<Cliente> todos = query.getResultList();

        return todos;

    }

    /**
     * Busca una lista de clientes por el nombre completo Esta armado para que se
     * pueda generar un SQL Injection y mostrar commo NO debe programarse.
     * 
     * @param nombre
     * @return
     */
    public List<Cliente> buscarPor(String nombre) {

        Session session = sessionFactory.openSession();

        // SQL Injection vulnerability exposed.
        // Deberia traer solo aquella del nombre y con esto demostrarmos que trae todas
        // si pasamos
        // como nombre: "' or '1'='1"
        // Query query = session.createNativeQuery("SELECT * FROM cliente where nombre =
        // '" + nombre + "'", Cliente.class);

        // List<Cliente> clientesHackeable = query.getResultList(); // SQL INJECTION

        // version corregida usando parametros SQL

        Query queryConParametrosSql = session.createNativeQuery("SELECT * FROM cliente where nombre = ?", // se pasa ?
                                                                                                          // como
                                                                                                          // parametro
                Cliente.class);
        queryConParametrosSql.setParameter(1, nombre);

        List<Cliente> clientesDeQueryConParametrosSQL = queryConParametrosSql.getResultList();

        // version usando JPQL, pensamos en objetos y no en tablas. mas complejo. usado
        // en select sencillos

        // Query queryConJPQL = session.createQuery("SELECT c FROM Cliente c where
        // c.nombre = :nombreFiltro", //usan aleas y Cliente (clase en java, no lista en
        // mysql)
        // Cliente.class);
        // queryConJPQL.setParameter("nombreFiltro", nombre);

        // List<Cliente> clientesDeJPQL = queryConJPQL.getResultList();

        return clientesDeQueryConParametrosSQL;

    }

    public Cliente buscarPorId(int clienteId) { // 2.0

        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery("SELECT * FROM cliente where cliente_id = ?", Cliente.class);
        query.setParameter(1, clienteId);

        Cliente clienteEncontrado = ((Cliente) query.getSingleResult());

        return clienteEncontrado;

    }

    // Cuenta cantidad de clientes
    public int contarClienteQueryNativa() {
        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery("SELECT count(*) FROM cliente");

        int resultado = ((Number) query.getSingleResult()).intValue();

        return resultado;

    }

    // Cuenta cantidad de clientes
    public int contarClienteJPQL() {
        Session session = sessionFactory.openSession();

        Query query = session.createQuery("SELECT count(c) FROM Cliente c");

        int resultado = ((Number) query.getSingleResult()).intValue();

        return resultado;

    }

}
