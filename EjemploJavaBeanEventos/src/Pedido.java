
import java.beans.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Déborah Blas Muñoz
 */
//bean receptor
public class Pedido implements Serializable, PropertyChangeListener {

    private int numeropedido;
    private Producto producto;
    private Date fecha;
    private int cantidad;

    public Pedido() {

    }

    public Pedido(int numeropedido, Producto producto,
            Date fecha, int cantidad) {
        this.numeropedido = numeropedido;
        this.producto = producto;
        this.fecha = fecha;
        this.cantidad = cantidad;
    }

    public int getNumeropedido() {
        return numeropedido;
    }

    public void setNumeropedido(int numeropedido) {
        this.numeropedido = numeropedido;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void propertyChange(PropertyChangeEvent evt) {

        int cantidadCompra = producto.getStockactual() - Integer.parseInt(evt.getNewValue().toString());

        try {
            ControladorProducto controller = new ControladorProducto();
            if (controller.HacerCompra(producto, cantidadCompra)) {
                producto.setStockactual(producto.getStockactual()+cantidadCompra);
                
                if (controller.actualizaStock(producto)) {
                    System.out.println("\nEl Stock ha sido actualizado. Es stock actual es: "+producto.getStockactual());
                } else {
                    System.out.println("ERROR al actualizar el Stock");
                }
            } else {
                System.out.println("ERROR al realizar la compra");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
