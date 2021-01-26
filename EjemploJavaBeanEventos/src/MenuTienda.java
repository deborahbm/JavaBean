
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
 
/**
 *
 * @author Déborah Blas Muñoz
 */
public class MenuTienda {

    public static void main(String[] args) throws SQLException {
        int opcion;
        Scanner sc = new Scanner(System.in);

        do {

            System.out.println("\n ********** Menú Principal Tienda On Line **********");
            System.out.println("\nSeleccione una opción: ");
            System.out.println("1. Ver catálogo");
            System.out.println("2. Realizar pedido");
            System.out.println("3. Salir");

            opcion = sc.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("********** FIN DEL PROGRAMA **********");
                    break;
                case 1:
                    listarProductos();
                    break;
                case 2:
                    hacerPedido();
                    break;
                default:
                    System.out.println("VALOR NO VÁLIDO, por favor seleccione un número del menú");
                    break;
            }

        } while (opcion != 3);

    }

    public static void listarProductos() throws SQLException {
        ControladorProducto pdao = new ControladorProducto();
        ArrayList<Producto> productos = pdao.listarProductos();

        System.out.println("Catálogo de productos: ");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println("Identificador: " + productos.get(i).getIdproducto() + " Descripción: " + productos.get(i).getDescripcion() + " Precio: " + productos.get(i).getPvp()+"€" +" Stock actual: "+productos.get(i).getStockactual()+" Stock mínimo: "+productos.get(i).getStockminimo());
        }
    }

    public static void hacerPedido() throws SQLException {
        int codigo, cantidad;
        ControladorProducto pdao = new ControladorProducto();
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduzca el número del identificador del producto que desea pedir: ");
        codigo = sc.nextInt();

        Producto pro = pdao.obtenerPorId(codigo);

        if (pro != null) {
            System.out.println("Introduzca el número de unidades: ");
            cantidad = sc.nextInt();

            if (pdao.hacerPedido(pro, cantidad)) {
                System.out.println("\nPedido realizado CORRECTAMENTE");
                System.out.println("Identificador: " + pro.getIdproducto() + " Descripción: " + pro.getDescripcion() + " Precio: " + pro.getPvp()+"€" +" Unidades: "+cantidad);
            } else {
                System.out.println("ERROR al realizar el pedido");
            }
        } else {
            System.out.println("Producto NO ENCONTRADO");
        }

    }
}
