package ar.com.ada.creditos.excepciones;
import ar.com.ada.creditos.entities.*;

public class ClienteInfoException extends Exception { //extends exception


    
    private Cliente cliente;

    public ClienteInfoException(Cliente cliente, String mensaje) {

        super(cliente.getNombre() + ":" + mensaje);
        this.cliente = cliente;
    }
}
    

