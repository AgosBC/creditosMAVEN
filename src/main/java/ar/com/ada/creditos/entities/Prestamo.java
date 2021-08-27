package ar.com.ada.creditos.entities;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @Column(name = "prestamo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prestamoId;

    private BigDecimal importe;
    private int cuotas;

    @Column(name = "fecha_prestamo")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;

    @Column(name = "fecha_alta")
    private Date fechaAlta;

    @Column(name = "estado_id")
    private int estadoId; // Por ahora vamos a crear este como int

    @ManyToOne // este tiene una relacion many to one (muchos prestamos a un cliente)
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id") // es un join columns va donde va la FK
    Cliente cliente;

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.cliente.agregarPrestamo(this);// relacion bidireccional.
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    // enumerado

    public EstadoPrestamoEnum getEstadoId() {

        return EstadoPrestamoEnum.parse(this.estadoId);
    }

    public void setEstadoId(EstadoPrestamoEnum estadoId) {
        this.estadoId = estadoId.getValue();
    }

    // ENUMERADO

    public enum EstadoPrestamoEnum {

        SOLICITADO(1), RECHAZADO(2), PENDIENTE_APROBACION(3), APROBADO(4), INCOBRABLE(5), CANCELADO(6), PREAPROBADO(7);

        private final int value;

        // NOTE: Enum constructor tiene que estar en privado
        private EstadoPrestamoEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EstadoPrestamoEnum parse(int id) {
            EstadoPrestamoEnum status = null; // Default
            for (EstadoPrestamoEnum item : EstadoPrestamoEnum.values()) {
                if (item.getValue() == id) {
                    status = item;
                    break;
                }
            }
            return status;
        }

    }

}
