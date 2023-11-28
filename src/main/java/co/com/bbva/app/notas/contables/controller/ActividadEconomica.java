package co.com.bbva.app.notas.contables.controller;

import java.io.Serializable;

public class ActividadEconomica implements Serializable {

    private Long id;
    private String codigo;
    private String nombre;
    // Otros atributos según tus necesidades

    // Constructor
    public ActividadEconomica(Long id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Otros getters y setters según tus atributos
}

