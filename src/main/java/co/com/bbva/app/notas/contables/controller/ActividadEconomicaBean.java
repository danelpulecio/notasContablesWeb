package co.com.bbva.app.notas.contables.controller;

import javax.annotation.ManagedBean;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class ActividadEconomicaBean implements Serializable {

    private String filtro;
    private List<ActividadEconomica> actividades;
    private List<ActividadEconomica> actividadesFiltradas;

    // Constructor y métodos de inicialización

    // Getters y setters

    public void filtrar() {
        actividadesFiltradas = new ArrayList<>();

        for (ActividadEconomica actividad : actividades) {
            // Implementa tu lógica de filtrado aquí
            if (actividad.getNombre().contains(filtro)) {
                actividadesFiltradas.add(actividad);
            }
        }
    }
}
