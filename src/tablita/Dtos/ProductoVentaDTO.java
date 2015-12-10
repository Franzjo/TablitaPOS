package tablita.Dtos;

import tablita.persistencia.Productos;

import java.math.BigDecimal;

/**
 * Created by akino on 11-19-15.
 */
public class ProductoVentaDTO {
    Productos idProductos;
    String descripcion;
    int cantidad;
    BigDecimal total;
    BigDecimal unitario;

    public ProductoVentaDTO(Productos idProductos, String descripcion, int cantidad, BigDecimal total, BigDecimal unitario) {
        this.idProductos = idProductos;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.total = total;
        this.unitario = unitario;
    }

    public BigDecimal getUnitario() {
        return unitario;
    }

    public void setUnitario(BigDecimal unitario) {
        this.unitario = unitario;
    }

    public ProductoVentaDTO() {
    }

    public ProductoVentaDTO(Productos idProductos, String descripcion, int cantidad, BigDecimal total) {
        this.idProductos = idProductos;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.total = total;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Productos getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Productos idProductos) {
        this.idProductos = idProductos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
