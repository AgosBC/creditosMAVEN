package ar.com.ada.creditos.entities;

import javax.persistence.*;
import org.hibernate.annotations.NaturalId;
import java.util.*;

import ar.com.ada.creditos.excepciones.*;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @Column(name = "cliente_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // es el autoincremental
    private int clienteId;

    private String nombre; // no requiere anotacion ya que la variable y la columna tiene el mismo nombre

    @NaturalId
    private int dni;

    private String direccion;

    @Column(name = "direccion_alternativa")
    private String direccionAlt;

    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE) // esto se pone unicamente cuando necesito una fecha sin la Hora (tipo date en
                                 // el DB)
    private Date fechaNacimiento;

    public Cliente(String nombre) {
        this.nombre = nombre;

    }

    // cada cliente tiene un lista de prestamos esta tiene una relacion
    // one to many (un cliente muchos prestamos)

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL/* , fetch = FetchType.EAGER */) // si arego lo agregar
                                                                                               // prestamo de set
                                                                                               // cliente, tambien
                                                                                               // deberia modificar el
                                                                                               // metodo
                                                                                               // agregarPrestamoClienteId
    private List<Prestamo> prestamos = new ArrayList<>();

    // @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    // private List<Cancelacion> cancelaciones = new ArrayList<>();

    // getters y setters

    public Cliente() {
    } // creado por quick fix cuando ingrese Cliente cliente = new Cliente() en
      // Credito buscar + info

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
        // this.cliente.agregarPrestamo(this) //relacion bidireccional. usar si qquiero
        // crear el metodo agregarPrestamo para q agrege un pretamo a cada cliente nuevo
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionAlt() {
        return direccionAlt;
    }

    public void setDireccionAlt(String direccionAlt) {
        this.direccionAlt = direccionAlt;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public void agregarPrestamo(Prestamo prestamo) {
        this.prestamos.add(prestamo);
    }
}
