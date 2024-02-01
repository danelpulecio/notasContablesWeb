package co.com.bbva.app.notas.contables.jsf.adminnota;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.dto.Rol;
import co.com.bbva.app.notas.contables.dto.UsuarioInstancias;
import co.com.bbva.app.notas.contables.jsf.beans.ContablesSessionBean;
import co.com.bbva.app.notas.contables.jsf.consultas.GeneralConsultaPage;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SessionScoped
@Named
public class CargaUsuarioPage extends GeneralConsultaPage<UsuarioInstancias> {


	private static final long serialVersionUID = -6709113217662690209L;

	private List<SelectItem> sucursales = new ArrayList<SelectItem>();
	private List<SelectItem> roles = new ArrayList<SelectItem>();

	private String estado = "";
	private String sucursal = "";
	private Integer rol;

	private static final Logger LOGGER = LoggerFactory.getLogger(CargaUsuarioPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

	@PostConstruct
	public void init() throws Exception {
		super._init();
		cargarFiltros();
	}
	public CargaUsuarioPage() {
		super();
	}

//	@Override
//	protected void _init() {
//		super._init();
//		cargarFiltros();
//	}

	public void cargarFiltros() {
		try {
			if (sucursales == null || sucursales.isEmpty()) {
				sucursales = getSelectItemList(notasContablesManager.getCV(Sucursal.class));
				roles = getSelectItemList(notasContablesManager.getCV(Rol.class), false);
			}
		} catch (final Exception e) {

//			LOGGER.error("{} Ocurrio un error al inicializar el mdulo de carga por usuario ", session.getTraceLog(), e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al inicializar el mdulo de carga por usuario");

		}
	}

	public String mostrar() {
		return _getPage();
	}

	@Override
	protected Collection<UsuarioInstancias> _buscar() throws Exception {
		return notasContablesManager.getInstanciasPorEstadoYSucursalOrigenYRol(estado, sucursal, rol);
	}

	@Override
	protected void _validar() throws Exception {
//		 validador.validarSeleccion(sucursal, "Sucursal Origen");
//		 validador.validarSeleccion(estado, "Estado");
//		 if (rol.intValue() < 0) {
//		 nuevoMensaje(FacesMessage.SEVERITY_WARN, "Seleccione una opcin para el campo 'Rol'");
//		 }
	}

	@Override
	protected String _getPage() {
		return CARGA_USUARIO;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getRol() {
		return rol;
	}

	public void setRol(Integer rol) {
		this.rol = rol;
	}

}
