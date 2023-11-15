package co.com.bbva.app.notas.contables.jsf;

import co.com.bbva.app.notas.contables.jsf.beans.ContablesApplicationBean;
import co.com.bbva.app.notas.contables.jsf.beans.ContablesRequestBean;
import co.com.bbva.app.notas.contables.jsf.beans.ContablesSessionBean;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public abstract class GeneralPage extends BasePage {


    @Inject
    private ContablesRequestBean contablesRequestBean;

    @Inject
    private ContablesApplicationBean contablesApplicationBean;

    private static final long serialVersionUID = 1L;

    protected abstract void _init() throws Exception;

    protected Integer codigo = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralPage.class);

    Session session = getContablesSessionBean().getSessionTrace();

    public GeneralPage() {
        super();
    }

    /**
     * <p>
     * Callback method that is called whenever a page is navigated to, either directly via a URL, or indirectly via page navigation. Customize this method to acquire resources that will be needed for event handlers and lifecycle methods, whether or
     * not this page is performing post back processing.
     * </p>
     *
     * <p>
     * Note that, if the current request is a postback, the property values of the components do <strong>not</strong> represent any values submitted with this request. Instead, they represent the property values that were saved for this view when it
     * was rendered.
     * </p>
     */
//	@PostConstruct
//	public void init() {
//		super.init();
//		try {
//			_init();
//		} catch (Exception e) {
//			log(getClass().getSimpleName() + " Initialization Failure", e);
//			if (e instanceof FacesException) {
//				throw (FacesException) e;
//			}
//
//			LOGGER.error("{} Error inicializando ", session.getTraceLog(), e);
//			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error inicializando");
//		}
//	}

    /**
     * <p>
     * Return a reference to the scoped data bean.
     * </p>
     *
     * @return reference to the scoped data bean
     */
    public ContablesRequestBean getContablesRequestBean() {
        return contablesRequestBean;
    }

    /**
     * <p>
     * Return a reference to the scoped data bean.
     * </p>
     *
     * @return reference to the scoped data bean
     */
    public ContablesApplicationBean getContablesApplicationBean() {
        return contablesApplicationBean;
    }

    //	@Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public List<SelectItem> getSelectItemSiNo() {
        final List<SelectItem> lista = new ArrayList<SelectItem>();
        lista.add(new SelectItem("S", "Si"));
        lista.add(new SelectItem("N", "No"));
        return lista;
    }

    public List<Integer> getKeys(final List<String> items) {
        final List<Integer> lista = new ArrayList<Integer>();
        for (final String item : items) {
            lista.add(Integer.valueOf(item.toString()));
        }
        return lista;
    }

    protected void println(String string) {
        System.out.println(string);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

}
