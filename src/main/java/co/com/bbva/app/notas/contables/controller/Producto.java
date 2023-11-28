package co.com.bbva.app.notas.contables.controller;


public class Producto {

    private String fruta;

    private String precio;

    public Producto(String fruta, String precio) {
        this.fruta = fruta;
        this.precio = precio;
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

    @Override
    public String toString() {
        return "Producto{" +
                "fruta='" + fruta + '\'' +
                '}';
    }

    public String getFruta() {
        return fruta;
    }

    public void setFruta(String fruta) {
        this.fruta = fruta;
    }
}
