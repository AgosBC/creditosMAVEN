package ar.com.ada.creditos.entities.reportes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Entity // solo entity ya que no hay una tabla 
// error de Entity org.hibernate.AnnotationException: No identifier specified for entity: ar.com.ada.creditos.entities.reportes.ReporteCantClientePrestamos
//@Embeddable //The JPA annotation @Embedded is used to embed a type into another entity. 
// Con esta anotación, indicamos que la clase puede ser «integrable» dentro de una entidad (se puede incrustar)
public class ReporteCantClientePrestamos {

    //atributos que necesito para el reporte (cant de clientes y prestamos)
    @Id
    @Column(name="cant_clientes")
    public int cantClientes;

    @Column(name="cant_prestamos")
    public int cantPrestamos;
}

//@Embedded : Con esta anotación, indicamos que el campo o la propiedad de una entidad es
//una instancia de una clase que puede ser integrable. Es decir, para que funcione, el campo que hayamos
//anotado como @Embedded, debe corresponderse con una clase que tenga la anotación @Embeddable.

    

