package ar.com.ada.creditos.entities;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name = "cancelacion")
public class Cancelacion {

    @Id
    @Column(name = "cancelacion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cancelacionId;

    @Column(name = "fecha_cancelacion")
    private Date fecahaCancelacion;

    private BigDecimal importe;

    private int cuota;

    @ManyToOne
    @JoinColumn(name = "prestamo_id", referencedColumnName = "prestamo_id")
    Prestamo prestamo;

    public int getCancelacionId() {
        return cancelacionId;
    }

    public void setCancelacionId(int cancelacionId) {
        this.cancelacionId = cancelacionId;
    }

    public Date getFecahaCancelacion() {
        return fecahaCancelacion;
    }

    public void setFecahaCancelacion(Date fecahaCancelacion) {
        this.fecahaCancelacion = fecahaCancelacion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

}
