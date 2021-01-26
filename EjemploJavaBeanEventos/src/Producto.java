
import java.beans.*;
import java.io.Serializable;
/**
 *
 * @author Déborah Blas Muñoz
 */

//BEAN FUENTE
public class Producto implements Serializable {


    private String descripcion;
    private int idproducto;
    private int stockactual;//PROPIEDAD COMPARTIDA. Si esta propiedad cambia, susceptible de lanzar un evento
    private int stockminimo;
    private float pvp;
    
//BEAN FUENTE
    private PropertyChangeSupport propertySupport;

    public Producto() {
        propertySupport = new PropertyChangeSupport(this);
    }

    public Producto(int idproducto, String descripcion,
            int stockactual, int stockminimo, float pvp) {
        propertySupport = new PropertyChangeSupport(this);
        this.idproducto = idproducto;
        this.descripcion = descripcion;
        this.stockactual = stockactual;
        this.stockminimo = stockminimo;
        this.pvp = pvp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getStockactual() {
        return stockactual;
    }

    public void setStockactual(int valorNuevo) {
        int valorAnterior = this.stockactual;
        this.stockactual = valorNuevo;

        // provoca la compra de material
        if (this.stockactual < getStockminimo()) 
            propertySupport.firePropertyChange("stockactual",valorAnterior, this.stockactual);

    }

    public int getStockminimo() {
        return stockminimo;
    }

    public void setStockminimo(int stockminimo) {
        this.stockminimo = stockminimo;
    }

    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

}
