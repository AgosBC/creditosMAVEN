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

public class CancelacionManager {

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

    public void create(Cancelacion cancelacion) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(cancelacion);

        session.getTransaction().commit();
        session.close();
    }

    public Cancelacion read(int cancelacionId) {

        Session session = sessionFactory.openSession();

        Cancelacion cancelacion = session.get(Cancelacion.class, cancelacionId);

        session.close();

        return cancelacion;
    }

    public void update(Cancelacion cancelacion) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(cancelacion);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Cancelacion cancelacion) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(cancelacion);

        session.getTransaction().commit();
        session.close();
    }

    // public BigDecimal totalCancelacionCliente (int clienteId)
    /*
     * public BigDecimal sumaCancelaciones (int clienteId){
     * 
     * Session session = sessionFactory.openSession(); // un numero que sea el
     * resultado de la suma de todas las cancelaciones de un cliente Query query =
     * session.createNativeQuery("select SUM from cancelaciones") BigDecimal
     * cancelado =
     * 
     * return cancelado }
     * 
     * Query query = session.createNativeQuery(
     * "select (select count(*) from cliente) as cant_clientes, (select count(*) from prestamo) as cant_prestamos"
     * , ReporteCantClientePrestamos.class);
     */

    public BigDecimal sumaCancel(int prestamoId) {

        Session session = sessionFactory.openSession();
        Query query = session.createNativeQuery("select SUM(importe) from cancelacion where prestamo_id= ?");
        query.setParameter(1, prestamoId);
        double resultado = ((Number) query.getSingleResult()).doubleValue();
        BigDecimal suma = new BigDecimal(resultado);
        session.close();

        return suma;

    }
}
