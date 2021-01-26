

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Déborah Blas Muñoz
 */
public class ConexionBBDD {

    private Connection con;
    private final String url = "jdbc:mysql://localhost:3306/gestion_tienda";
    private final String user = "root";
    private final String password= "";
    
    public ConexionBBDD() 
    {

	}
    
    public void conectar() throws SQLException {
    	
        if (con == null || con.isClosed()) 
        {
            try 
            {
                Class.forName("com.mysql.jdbc.Driver");
            } 
            catch (ClassNotFoundException e) 
            {
                throw new SQLException(e);
            }
            
            con = DriverManager.getConnection(url, user, password);
        }
    }
    
    public void desconectar() throws SQLException 
    {
        if (con != null && !con.isClosed()) 
            con.close();
    }
	
    public Connection getCon() 
    {
		return con;
	}
    
}
