package co.com.bbva.app.notas.contables.controller;


import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public abstract class CategoriaController {


    //    private String categoria = "Tecnologia";
    protected List<Producto> lista = new ArrayList<>();
    private List<Producto> target = new ArrayList<>();
    private List<SelectItem> source = new ArrayList<>();
    private List<SelectItem> targetS = new ArrayList<>();
    private DualListModel<Producto> dualListModel;
    private DualListModel<SelectItem> dualListModelS = new DualListModel<>(source, targetS);

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaController.class);


    public String getCategoria() {
        return "categoria";
    }

    public List<Producto> getLista() {
        if (lista == null)
            lista = new ArrayList<>();
        return lista;
    }


    public DualListModel<Producto> getDualListModel() {
        LOGGER.info("get dualist");
        listarCat();
        dualListModel = new DualListModel<>(lista, target);
        return dualListModel;
    }

    public void setDualListModel(DualListModel<Producto> dualListModel) {
        this.dualListModel = dualListModel;
    }

    public DualListModel<SelectItem> getDualListModelS() {
        SelectItem danel = new SelectItem("hola", "goku");
        source.add(0, danel);
        return dualListModelS;
    }

    public void setDualListModelS(DualListModel<SelectItem> dualListModelS) {
        this.dualListModelS = dualListModelS;
    }

    public void setLista(List<Producto> lista) {
        this.lista = lista;
    }

    public void listarCat() {
        LOGGER.info("lista before: {}", lista);
        if (lista.isEmpty()) {
            Producto pr1 = new Producto("Pera", "10");
            Producto pr2 = new Producto("Manzana", "20");
            Producto pr4 = new Producto("Naranja", "30");
            Producto pr5 = new Producto("Mango", "40");
            Producto pr6 = new Producto("Limon", "50");
            Producto pr7 = new Producto("Papaya", "60");
            Producto pr8 = new Producto("mandarina", "70");
            lista.add(pr1);
            lista.add(pr2);
            lista.add(pr4);
            lista.add(pr5);
            lista.add(pr6);
            lista.add(pr7);
            lista.add(pr8);
        }
        LOGGER.info("lista after: {}", lista);
    }
}
