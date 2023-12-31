package co.com.bbva.app.notas.contables.jsf;

import co.com.bbva.app.notas.contables.facade.CargaAltamiraSession;
import co.com.bbva.app.notas.contables.facade.NotasContablesSession;
import co.com.bbva.app.notas.contables.facade.impl.CargaAltamiraSessionBean;
import co.com.bbva.app.notas.contables.facade.impl.NotasContablesSessionBean;
import co.com.bbva.app.notas.contables.jsf.beans.ContablesSessionBean;
import co.com.bbva.app.notas.contables.jsf.beans.UsuarioLogueado;
import co.com.bbva.app.notas.contables.util.EMailSender;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;



public abstract class BasePage implements Serializable  {

	@Inject
	private ContablesSessionBean contablesSessionBean;

	private static final long serialVersionUID = 1L;
	protected final Validador validador;
	protected static final NotasContablesSession notasContablesManager = new NotasContablesSessionBean();
	protected static final CargaAltamiraSession cargaAltamiraManager = new CargaAltamiraSessionBean();
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
		validador = new Validador(this);
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		DIR_SOPORTES = context.getInitParameter("DIR_SOPORTES");
		DIR_REPORTES_EXCEL = context.getInitParameter("DIR_REPORTES_EXCEL");
		DIR_RECEPCION_ALTAMIRA = context.getInitParameter("DIR_RECEPCION_ALTAMIRA");
		DIR_TRANSMISION_ALTAMIRA = context.getInitParameter("DIR_TRANSMISION_ALTAMIRA");
		DIR_CARGA = context.getInitParameter("DIR_CARGA");
		ACTIVAR_LDAP = context.getInitParameter("ACTIVAR_LDAP");
		DIR_SIRO = context.getInitParameter("DIR_SIRO");
	}

	/**
	 * <p>
	 * Return a reference to the scoped data bean.
	 * </p>
	 *
	 * @return reference to the scoped data bean
	 */
	public final ContablesSessionBean getContablesSessionBean() {
		return contablesSessionBean;
	}

	public final String nuevoMensaje(final Severity severidad, final String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, msg, null));
		return "";
	}

	public final UsuarioLogueado getUsuarioLogueado() {
		return getContablesSessionBean().getLoginUser();
	}

	protected final int getCodUsuarioLogueado() {
		return getContablesSessionBean().getLoginUser().getUsuario().getCodigo().intValue();
	}

	protected final boolean hayMensajes() {
		return FacesContext.getCurrentInstance().getMessages().hasNext();
	}

	public final boolean esUltimaFase() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getRenderResponse();
	}

	public final List<SelectItem> getSelectItemList(final Map<?, String> mapa) {
		return getSelectItemList(mapa, true);
	}

	public final List<SelectItem> getSelectItemList(final Map<?, String> mapa, boolean mostrarClave) {
		final List<SelectItem> lista = new ArrayList<SelectItem>();
		for (final Object key : mostrarClave ? new TreeSet<Object>(mapa.keySet()) : mapa.keySet()) {
			lista.add(new SelectItem(key, (mostrarClave ? key + " - " : "") + mapa.get(key)));
		}
		return lista;
	}

	public final String lanzarError(final Exception e, final String msg) {
		System.out.println(e.getMessage());
		e.printStackTrace();
		nuevoMensaje(FacesMessage.SEVERITY_ERROR, msg);
		return "";
	}

	public final String lanzarError(final Exception e) {
		return lanzarError(new Exception(e), "Ocurri un error al procesar la peticin ");
	}

}
