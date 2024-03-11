package co.com.bbva.app.notas.contables.controller;


import java.util.List;

public class Producto {

    private String fruta;

    private String precio;

    private List<PuntosDeVentaDto> puntoVenta;



    public Producto(String fruta, String precio) {
        this.fruta = fruta;
        this.precio = precio;
    }

    public Producto(String fruta, String precio, List<PuntosDeVentaDto> puntoVenta) {
        this.fruta = fruta;
        this.precio = precio;
        this.puntoVenta = puntoVenta;
    }

    public Producto(String fruta) {
        this.fruta = fruta;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFruta() {
        return fruta;
    }

    public void setFruta(String fruta) {
        this.fruta = fruta;
    }

    public List<PuntosDeVentaDto> getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(List<PuntosDeVentaDto> puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "fruta='" + fruta + '\'' +
                ", precio='" + precio + '\'' +
                ", puntoVenta=" + puntoVenta +
                '}';
    }
}
