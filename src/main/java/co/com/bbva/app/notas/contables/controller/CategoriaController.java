package co.com.bbva.app.notas.contables.controller;


import org.primefaces.model.DualListModel;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public abstract class CategoriaController {



    //    private String categoria = "Tecnologia";
    protected List<Producto> lista = new ArrayList<>();
    private List<Producto> target = new ArrayList<>();
    private List<SelectItem> source = new ArrayList<>();
    private List<SelectItem> targetS = new ArrayList<>();
    private DualListModel<Producto> dualListModel = new DualListModel<>(lista,target);
    private DualListModel<SelectItem> dualListModelS = new DualListModel<>(source,targetS);




    public String getCategoria() {
        return "categoria";
    }

    public List<Producto> getLista() {
        if(lista==null)
            lista = new ArrayList<>();
        return lista;
    }




    public DualListModel<Producto> getDualListModel() {
        return dualListModel;
    }

    public void setDualListModel(DualListModel<Producto> dualListModel) {
        this.dualListModel = dualListModel;
    }

    public DualListModel<SelectItem> getDualListModelS() {
        SelectItem danel = new SelectItem("hola","goku");
        source.add(0,danel);
        return dualListModelS;
    }

    public void setDualListModelS(DualListModel<SelectItem> dualListModelS) {
        this.dualListModelS = dualListModelS;
    }

    public void setLista(List<Producto> lista) {
        this.lista = lista;
    }
}
