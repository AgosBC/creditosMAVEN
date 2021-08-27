package ar.com.ada.creditos;

import ar.com.ada.creditos.managers.*;
import java.util.Scanner;

import org.hibernate.engine.spi.Managed;
import org.hibernate.engine.spi.ManagedEntity;

import ar.com.ada.creditos.excepciones.*;
import ar.com.ada.creditos.entities.*;
import ar.com.ada.creditos.entities.Prestamo.EstadoPrestamoEnum;

import java.util.Date;
import java.util.Scanner;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Credito {

    public static Scanner Teclado = new Scanner(System.in);

    protected ClienteManager ABMCliente = new ClienteManager(); // prestamo cliente? buscar un nombre de variable mas
                                                                // claro
    protected PrestamoManager ABMPrestamo = new PrestamoManager();
    protected CancelacionManager ABMCancelacion = new CancelacionManager();

    public void iniciar() throws Exception {

        try {

            ABMCliente.setup(); // en cliente manager
            ABMPrestamo.setup();
            ABMCancelacion.setup();

            printOpciones();

            int opcion = Teclado.nextInt();
            Teclado.nextLine();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        try {
                            alta();
                        } catch (ClienteDNIException exdni) {
                            System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2:
                        baja();
                        break;

                    case 3:
                        modifica();
                        break;

                    case 4:
                        listar(); // se tiene que ir ..?
                        break;

                    case 5:
                        listarPorNombre();
                        break;

                    case 6:
                        agregarPrestamoClienteId();
                        break;

                    case 7:
                        listarPrestamos();
                        break;

                    case 8:
                        buscarCancelacionesPrestamo();
                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printOpciones();

                opcion = Teclado.nextInt();
                Teclado.nextLine();
            }

            // Hago un safe exit del manager
            ABMCliente.exit();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Fallo en el sistema");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema");

        }
    }

    public void alta() throws Exception {
        Cliente cliente = new Cliente();
        System.out.println("Ingrese el nombre:");
        cliente.setNombre(Teclado.nextLine());
        System.out.println("Ingrese el DNI:");
        cliente.setDni(Teclado.nextInt());
        Teclado.nextLine();
        System.out.println("Ingrese la direccion:");
        cliente.setDireccion(Teclado.nextLine());

        System.out.println("Ingrese el Direccion alternativa(OPCIONAL):");

        String domAlternativo = Teclado.nextLine();

        if (domAlternativo != null)
            cliente.setDireccionAlt(domAlternativo);

        System.out.println("Ingrese fecha de nacimiento:");
        Date fecha = null;
        DateFormat dateformatArgentina = new SimpleDateFormat("dd/MM/yy");

        fecha = dateformatArgentina.parse(Teclado.nextLine());
        cliente.setFechaNacimiento(fecha);

        Prestamo prestamo = new Prestamo();

        prestamo.setImporte(new BigDecimal(10000));
        prestamo.setCuotas(5);
        prestamo.setFechaPrestamo(new Date());
        prestamo.setFechaAlta(new Date());
        prestamo.setEstadoId(EstadoPrestamoEnum.APROBADO);
        prestamo.setCliente(cliente);

        ABMCliente.create(cliente);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Cliente generada con exito.  " + cliente.getClienteId);
         */

        System.out.println("Cliente generado con exito.  " + cliente);

    }

    public void baja() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el ID de Cliente:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.read(id);

        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado.");

        } else {

            try {

                ABMCliente.delete(clienteEncontrado);
                System.out
                        .println("El registro del cliente " + clienteEncontrado.getClienteId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una cliente. Error: " + e.getCause());
            }

        }
    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Cliente:");
        int dni = Teclado.nextInt();
        Cliente clienteEncontrado = ABMCliente.readByDNI(dni);

        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado.");

        } else {
            ABMCliente.delete(clienteEncontrado);
            System.out.println("El registro del DNI " + clienteEncontrado.getDni() + " ha sido eliminado.");
        }
    }

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la cliente a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la cliente a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Cliente clienteEncontrado = ABMCliente.read(id);

        if (clienteEncontrado != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(clienteEncontrado.toString() + " seleccionado para modificacion.");// RECORDATORIAO!
                                                                                                  // CAMBIAR

            System.out.println(
                    "Elija qué dato de la cliente desea modificar: \n1: nombre, \n2: DNI, \n3: domicilio, \n4: domicilio alternativo, \n5: fecha nacimiento");
            int selecper = Teclado.nextInt();
            Teclado.nextLine();

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    clienteEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");

                    clienteEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese la nueva direccion:");
                    clienteEncontrado.setDireccion(Teclado.nextLine());

                    break;
                case 4:
                    System.out.println("Ingrese la nueva direccion alternativa:");
                    clienteEncontrado.setDireccionAlt(Teclado.nextLine());

                    break;
                case 5:
                    System.out.println("Ingrese fecha de nacimiento:");
                    Date fecha = null;
                    DateFormat dateformatArgentina = new SimpleDateFormat("dd/MM/yy");

                    fecha = dateformatArgentina.parse(Teclado.nextLine());
                    clienteEncontrado.setFechaNacimiento(fecha);
                    break;
                default:
                    break;
            }

            // Teclado.nextLine();

            ABMCliente.update(clienteEncontrado);

            System.out.println("El registro de " + clienteEncontrado.getNombre() + " ha sido modificado.");

        } else {
            System.out.println("Cliente no encontrado.");
        }

    }

    public void listar() { // no haria falta? en el sistema real no muestra la lista completa de clientes

        List<Cliente> todos = ABMCliente.buscarTodos();
        for (Cliente c : todos) {
            mostrarCliente(c);
        }
        int cantidadClientes = ABMCliente.contarClienteJPQL();
        System.out.println("La cantidad de clientes totales es: " + cantidadClientes); // agrego el contador para que no
                                                                                       // solo liste los clientes ponga
                                                                                       // tambien el total
    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Cliente> clientes = ABMCliente.buscarPor(nombre);
        for (Cliente cliente : clientes) {
            mostrarCliente(cliente);
        }
    }

    public void mostrarCliente(Cliente cliente) {

        System.out.print("Id: " + cliente.getClienteId() + " Nombre: " + cliente.getNombre() + " DNI: "
                + cliente.getDni() + " Domicilio: " + cliente.getDireccion());

        if (cliente.getDireccionAlt() != null)
            System.out.print(" Alternativo: " + cliente.getDireccionAlt());

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaNacimientoStr = formatter.format(cliente.getFechaNacimiento());

        System.out.println(" Fecha Nacimiento: " + fechaNacimientoStr); // separado del print anterior? revisar
    }

    public void agregarPrestamoClienteId() {

        System.out.println("Ingrese Id del cliente");
        int nCliente = Teclado.nextInt();
        Teclado.nextLine();
        Cliente clienteId = ABMCliente.read(nCliente);
        if (clienteId == null) {
            System.out.println("cliente no encontrado");
            return;
        }

        System.out.println("Ingrese monto del prestamo");
        Prestamo prestamo1 = new Prestamo();
        BigDecimal importe = Teclado.nextBigDecimal();
        Teclado.nextLine();

        prestamo1.setImporte(importe);
        prestamo1.setCliente(clienteId);

        System.out.println("ingrese cantidad de cuotas de cancelacion");
        int cuota = Teclado.nextInt();
        Teclado.nextLine();
        prestamo1.setCuotas(cuota);

        prestamo1.setFechaAlta(new Date());
        prestamo1.setFechaPrestamo(new Date());

        ABMPrestamo.create(prestamo1);

    }

    public void mostrarPrestamos(Prestamo p) {

        System.out.println("Cliente: " + p.getCliente().getNombre());
        System.out.println("ID de prestamo es: " + p.getPrestamoId() + " Importe del prestamo: $" + p.getImporte()
                + " Cuotas a pagar: " + p.getCuotas());

    }

    public void listarPrestamos() {

        List<Prestamo> todos = ABMPrestamo.buscarPrestamos();
        for (Prestamo prestamo : todos) {
            mostrarPrestamos(prestamo);

        }

        // int cantidadPrestamos = ABMPrestamo.contarP
        // System.out.println("La cantidad de clientes totales es: "+
        // cantidadPrestamos);
    }

    // metodo que me diga la deuda de un cliente
    // devuelve un Bigdecimal y lo busque por cliente id
    // dos metodos uno va a buscar los prestamos, otro a buscar las cancelaciones
    // (sumarlas)
    // y esas restarlas a los prestamos

    public void mostrarTotalPrestamo() {

        System.out.println("ingrese ID del cliente");
        int clienteId = Teclado.nextInt();
        Teclado.nextLine();
        Cliente cliente = ABMCliente.buscarPorId(clienteId);

        BigDecimal total = ABMPrestamo.totalPrestamoCliente(cliente);

        System.out.println("El importe total de todos sus prestamos es: " + total);

    }

    public void buscarCancelacionesPrestamo() {

        System.out.println("ingrese id prestamo");
        int prestamoId = Teclado.nextInt();
        Teclado.nextLine();

        BigDecimal total = ABMCancelacion.sumaCancel(prestamoId);

        System.out.println(total);

    }

    public static void printOpciones() {
        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para agregar un cliente.");
        System.out.println("2. Para eliminar un cliente.");
        System.out.println("3. Para modificar un cliente.");
        System.out.println("4. Para ver el listado de clientes.");
        System.out.println("5. Buscar un cliente por nombre especifico(SQL Injection)).");
        System.out.println("6. Otorgar un prestamo (cliente Id).");
        System.out.println("7. Ver lista de prestamos");
        System.out.println("8. Cancelacion");
        System.out.println("0. Para terminar.");
        System.out.println("");
        System.out.println("=======================================");
    }

}
