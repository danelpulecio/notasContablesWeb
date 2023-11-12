package co.com.bbva.app.notas.contables.controller;



//import co.com.bbva.app.notas.contables.entities.Producto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

//contexto


@Named
@RequestScoped
//@Model esterotipo que junta rquestscoped y name, lo unico es q el nombre del bean queda por defecto el nombre de la clase
public class ProductoController extends CategoriaController implements Serializable {



    //@Produces
    //@Model
    public String titulo() {
        return "Hola mundo JavaServer Face 3.0";
    }

    //@Produces
    //@RequestScoped
    //@Named("listado")
//    public List<Producto> listado() {
//        return Arrays.asList(new Producto("Peras"), new Producto("Manzanas"), new Producto("Naranjas"));
//    }
}
