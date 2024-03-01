package co.com.bbva.app.notas.contables.jsf;

import co.com.bbva.app.notas.contables.facade.CargaAltamiraSession;
import co.com.bbva.app.notas.contables.facade.NotasContablesSession;
import co.com.bbva.app.notas.contables.facade.impl.CargaAltamiraSessionBean;
import co.com.bbva.app.notas.contables.facade.impl.NotasContablesSessionBean;
import co.com.bbva.app.notas.contables.jsf.beans.ContablesSessionBean;
import co.com.bbva.app.notas.contables.jsf.beans.UsuarioLogueado;
import co.com.bbva.app.notas.contables.util.EMailSender;
import co.com.bbva.app.notas.contables.util.GetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public abstract class BasePage implements Serializable  {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
	@Inject
	private ContablesSessionBean contablesSessionBean;

	private static final long serialVersionUID = 1L;
	protected final Validador validador;
	protected static final NotasContablesSession notasContablesManager = new NotasContablesSessionBean();
	protected static final CargaAltamiraSession cargaAltamiraManager = new CargaAltamiraSessionBean();
	private GetProperties properties = new GetProperties();
	protected static final EMailSender eMailSender = new EMailSender();

	protected final String DIR_SOPORTES;
	protected final String DIR_REPORTES_EXCEL;
	protected final String DIR_RECEPCION_ALTAMIRA;
	protected final String DIR_TRANSMISION_ALTAMIRA;
	protected final String DIR_CARGA;
	protected final String ACTIVAR_LDAP;
	protected final String DIR_SIRO;

	public BasePage() {
		super();
		LOGGER.info("constructor base page");
		validador = new Validador(this);
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

		java.util.Properties propiedades = properties.readProperties();

		DIR_SOPORTES = propiedades.getProperty("dirSoportes");
//     context.getInitParameter("DIR_SOPORTES");
		DIR_REPORTES_EXCEL = propiedades.getProperty("dirReportesExcel");
//     DIR_REPORTES_EXCEL = context.getInitParameter("DIR_REPORTES_EXCEL");
		DIR_RECEPCION_ALTAMIRA = propiedades.getProperty("dirTransmisionAltamira");
//     DIR_RECEPCION_ALTAMIRA = context.getInitParameter("DIR_RECEPCION_ALTAMIRA");
		DIR_TRANSMISION_ALTAMIRA = propiedades.getProperty("dirRecepcionAltamira");
//     DIR_TRANSMISION_ALTAMIRA = context.getInitParameter("DIR_TRANSMISION_ALTAMIRA");
		DIR_CARGA = propiedades.getProperty("dirCarga");
//     DIR_CARGA = context.getInitParameter("DIR_CARGA");
//     ACTIVAR_LDAP = "0";
		ACTIVAR_LDAP = propiedades.getProperty("activar.ldap");

		DIR_SIRO =  propiedades.getProperty("dirSiro");
//     DIR_SIRO = context.getInitParameter("DIR_SIRO");
		LOGGER.info("constructor finaliza");
	}

	@PreDestroy
	public void destroy () throws IOException {
		LOGGER.info("DestruyendoBasePageBean");
	}

	/**
	 * <p>
	 * Return a reference to the scoped data bean.
	 * </p>
	 *
	 * @return reference to the scoped data bean
	 */
	public ContablesSessionBean getContablesSessionBean() {
		//LOGGER.info("llamando el getContableSesionBean");
		return contablesSessionBean;
	}

	public  String nuevoMensaje(final Severity severidad, final String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, msg, null));
		return "";
	}

	public UsuarioLogueado getUsuarioLogueado() {
		//LOGGER.info("contableSessionBean basePage {}", getContablesSessionBean());
		return getContablesSessionBean().getLoginUser();
	}

	protected  int getCodUsuarioLogueado() {
		LOGGER.info("getContablesSessionBean().getLoginUser().getUsuario().getCodigo().intValue() ---->" + getContablesSessionBean().getLoginUser().getUsuario().getCodigo().intValue());
		return getContablesSessionBean().getLoginUser().getUsuario().getCodigo().intValue();
	}

	protected  boolean hayMensajes() {
		return FacesContext.getCurrentInstance().getMessages().hasNext();
	}

	public boolean esUltimaFase() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getRenderResponse();
	}

	public  List<SelectItem> getSelectItemList(final Map<?, String> mapa) {
		return getSelectItemList(mapa, true);
	}

	public  List<SelectItem> getSelectItemList(final Map<?, String> mapa, boolean mostrarClave) {
		final List<SelectItem> lista = new ArrayList<SelectItem>();
		for (final Object key : mostrarClave ? new TreeSet<Object>(mapa.keySet()) : mapa.keySet()) {
			lista.add(new SelectItem(key, (mostrarClave ? key + " - " : "") + mapa.get(key)));
		}
		return lista;
	}

	public  String lanzarError(final Exception e, final String msg) {
		e.printStackTrace();
		nuevoMensaje(FacesMessage.SEVERITY_ERROR, msg);
		return "";
	}

	public  String lanzarError(final Exception e) {
		return lanzarError(new Exception(e), "Ocurri un error al procesar la peticin ");
	}


}
