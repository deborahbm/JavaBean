
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ControladorProducto {

    private ConexionBBDD con;
    private Connection connection;

    public ControladorProducto() throws SQLException {
        con = new ConexionBBDD();
    }

    public ArrayList<Producto> listarProductos() throws SQLException {
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        String sql = "SELECT * FROM productos";

        con.conectar();
        connection = con.getCon();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int idproducto = resultSet.getInt("idproducto");
            String descripcion = resultSet.getString("descripcion");
            int stockActual = resultSet.getInt("stockActual");
            int stockMinimo = resultSet.getInt("stockMinimo");
            float pvp = resultSet.getFloat("pvp");

            Producto pro = new Producto(idproducto, descripcion, stockActual, stockMinimo, pvp);
            listaProductos.add(pro);
        }

        con.desconectar();
        return listaProductos;
    }

    //obtener por idproducto  
    public Producto obtenerPorId(int idproducto) throws SQLException {
        Producto pro = null;

        String sql = "SELECT * FROM productos WHERE idproducto= ? ";
        con.conectar();
        connection = con.getCon();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idproducto);

        ResultSet res = statement.executeQuery();

        if (res.next()) {
            pro = new Producto(res.getInt("idproducto"), res.getString("descripcion"), res.getInt("stockActual"),
                    res.getInt("stockMinimo"), res.getFloat("pvp"));
        }

        res.close();
        con.desconectar();

        return pro;
    }

    //hacer pedido
    public boolean hacerPedido(Producto pro, int cantidad) throws SQLException {
        boolean insertar = false;

        //insertar registro en pedidos
        String sql = "INSERT INTO pedidos (fecha, cantidad, idproducto) VALUES (?, ?, ?)";
        con.conectar();

        connection = con.getCon();
        PreparedStatement statement = connection.prepareStatement(sql);

        Date date = new Date();

        statement.setString(1, date.toString());
        statement.setInt(2, cantidad);
        statement.setInt(3, pro.getIdproducto());

        boolean rowInserted = statement.executeUpdate() > 0;

        statement.close();
        con.desconectar();

        if (rowInserted) {
            insertar = true;

            boolean rowActualizar = false;
            String sql2 = "UPDATE productos SET stockActual=? WHERE idproducto=?";

            con.conectar();
            connection = con.getCon();
            PreparedStatement statement2 = connection.prepareStatement(sql2);

            statement2.setInt(1, pro.getStockactual());
            statement2.setInt(2, pro.getIdproducto());

            rowActualizar = statement2.executeUpdate() > 0;

            statement.close();
            con.desconectar();

            if (rowActualizar) {

                pro.setStockactual(pro.getStockactual() - cantidad);

                if (pro.getStockactual() < pro.getStockminimo()) {
                    int cantidadCompra = pro.getStockminimo() - cantidad;

                    if (HacerCompra(pro, cantidadCompra)) {
                        System.out.println("\nSE HA ALCANZADO EL STOCK MÍNIMO DEL PRODUCTO. SE REQUIERE HACER COMPRA DEL PRODUCTO");
                        System.out.println("El stock mínimo es de: " + pro.getStockminimo() + " unidades");
                        System.out.println("El stock actual es de: " + pro.getStockactual() + " unidades");

                        if (actualizaStock(pro)) {
                            System.out.println("\nSe ha actualizado el Stock CORRECTAMENTE");
                            System.out.println("El stock actual es de: " + pro.getStockactual() + " unidades");
                        } else {
                            System.out.println("ERROR al actualizar el Stock");
                        }
                    } else {
                        System.out.println("ERROR al actualizar el Stock");
                    }
                }
            } else {
                System.out.println("ERROR al actualizar el Stock");
            }

        }

        return insertar;
    }

    public boolean HacerCompra(Producto pro, int cantidad) throws SQLException {
        String sql = "INSERT INTO compras (cantidad, idproducto) VALUES (?, ?)";
        con.conectar();

        connection = con.getCon();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, pro.getStockactual());
        statement.setInt(2, pro.getIdproducto());

        boolean rowInserted = statement.executeUpdate() > 0;

        statement.close();
        con.desconectar();

        return rowInserted;
    }

    public boolean actualizaStock(Producto pro) throws SQLException {

        System.out.printf("\nIntroduzca las unidades que desea comprar");
        Scanner sc = new Scanner(System.in);
        int unidades = sc.nextInt();
        pro.setStockactual(pro.getStockactual() + unidades);

        boolean rowActualizar = false;
        String sql = "UPDATE productos SET stockActual=? WHERE idproducto=?";

        con.conectar();
        connection = con.getCon();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, pro.getStockactual());
        statement.setInt(2, pro.getIdproducto());

        rowActualizar = statement.executeUpdate() > 0;

        statement.close();
        con.desconectar();

        return rowActualizar;
    }

}
