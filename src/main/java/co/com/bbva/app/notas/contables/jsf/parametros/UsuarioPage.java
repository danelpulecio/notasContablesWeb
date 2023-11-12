package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.carga.dto.Perfil;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.carga.dto.UsuarioAltamira;
import co.com.bbva.app.notas.contables.dao.SuperDAO;
import co.com.bbva.app.notas.contables.dto.EnteAutorizador;
import co.com.bbva.app.notas.contables.dto.Padrino;
import co.com.bbva.app.notas.contables.dto.Rol;
import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.IRol;
import co.com.bbva.app.notas.contables.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@Named
@RequestScoped
public class UsuarioPage extends GeneralParametrosPage<UsuarioModulo, UsuarioModulo> {


	String param = getParam();
	/**
	 * 
	 */
	private static final long serialVersionUID = -2612093503479337549L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioPage.class);
	private static final String MSG_CREATE_USER = "SUCCESS CREATED USER - APP";
	private static final String MSG_UPDATE_USER = "UPDATED USER INFO - APP";
	private List<SelectItem> roles;
	private List<SelectItem> sucursales;
	private List<SelectItem> perfiles;
	Session session = getContablesSessionBean().getSessionTrace();
	protected final SuperDAO actividadSuperDAO = new SuperDAO(null);
	private String rolSel;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public UsuarioPage() {
		super(true);
	}

	@Override
	protected UsuarioModulo _getInstance() {
		return new UsuarioModulo();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de un nuevo usuario
	 * 
	 * @return
	 */
	public String buscarUsuarioAltamira() {
		String codEmpleado = objActual.getCodigoEmpleado().trim();
		objActual = new UsuarioModulo();
		try {
			if (codEmpleado.length() > 0) {


				LOGGER.info("{} Buscar codigo usuario: {}", session.getTraceLog(),codEmpleado );

				// se busca el usuario altamira
				UsuarioAltamira usuarioAltamira = new UsuarioAltamira();
				usuarioAltamira.setCodigoEmpleado(codEmpleado);
				usuarioAltamira = cargaAltamiraManager.getUsuarioAltamira(usuarioAltamira);

				// si se encuentra el usuario
				if (usuarioAltamira != null && usuarioAltamira.getCodigoEmpleado().trim().length() > 0) {
					objActual.setCodigoEmpleado(usuarioAltamira.getCodigoEmpleado());
					objActual.setCodigoPerfilModificado(usuarioAltamira.getCodigoPerfil());
					objActual.setNombrePerfilModificado(usuarioAltamira.getNombrePerfil());
					objActual.setCodigoAreaModificado(StringUtils.getStringLeftPadding("" + usuarioAltamira.getCodigoArea(), 4, '0'));
					objActual.setNombreAreaModificado(usuarioAltamira.getNombreArea());
					objActual.setEMailModificado(usuarioAltamira.getCorreoElectronico());
					objActual.setUsuAlt(usuarioAltamira);
					setRolSel("-1");
				} else {
					objActual.setCodigoEmpleado(codEmpleado);
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontr el usuario asociado al cdigo " + codEmpleado);
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe indicar el cdigo completo del usuario a buscar");
			}
			consultarListasAuxiliares();

		} catch (Exception e) {
			LOGGER.error("{} Error al inicializar el editor de creacin de usuarios" , session.getTraceLog(), e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el editor de creacin de usuarios");
		}
		return null;
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<UsuarioModulo> _buscarTodos() throws Exception {
		return notasContablesManager.getUsuariosModulo();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<UsuarioModulo> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar usuario: {}", session.getTraceLog(),param );
		}
		return notasContablesManager.searchUsuarioModulo(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de un nuevo usuario
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new UsuarioModulo();
		rolSel = "-1";
		consultarListasAuxiliares();
	}
	
	@Override
	protected boolean _guardar() throws Exception {
		try {
			objActual.setCodigoRol(Integer.valueOf(rolSel));
			// si se trata de un usuario nuevo
			if (objActual.getCodigo().intValue() <= 0) {

				// chambonazo para tapar las brutalidades del desarrollador anterior... (como no normaliza los datos??????)
				for (SelectItem s : sucursales) {
					if (s.getValue().toString().equals(objActual.getCodigoAreaModificado())) {
						objActual.setNombreAreaModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
						break;
					}
				}
				for (SelectItem s : perfiles) {
					if (s.getValue().toString().equals(objActual.getCodigoPerfilModificado())) {
						objActual.setNombrePerfilModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
						break;
					}
				}
				objActual.setLastUpdate(MSG_CREATE_USER);
				LOGGER.info("{} Creacin de Usuario: {} - {} | Rol {} | {}", session.getTraceLog(), objActual.getCodigoEmpleado(), objActual.getCodigo(), objActual.getCodigoRol(), objActual.getLastUpdate());
				notasContablesManager.addUsuarioModulo(objActual, getCodUsuarioLogueado());
			}

		else{
				UsuarioModulo usuarioModuloOriginal = notasContablesManager.getUsuarioModulo(objActual);
				// si se trata de un cambio de rol
				boolean cambioRol = usuarioModuloOriginal.getCodigoRol().intValue() != objActual.getCodigoRol().intValue();
				if (cambioRol && !canUpdate()) {// si se trata de una actualizacion y el cambio es valido
					return false;
				}else{
					for (SelectItem s : sucursales) {
						if (s.getValue().toString().equals(objActual.getCodigoAreaModificado())) {
							objActual.setNombreAreaModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
							break;
						}
					}
					for (SelectItem s : perfiles) {
 						if (s.getValue().toString().equals(objActual.getCodigoPerfilModificado())) {
							objActual.setNombrePerfilModificado(s.getLabel().substring(s.getLabel().indexOf("- ") + 2));
							break;
						}
					}
					canUpdate();
				}
				objActual.setLastUpdate(MSG_UPDATE_USER);
				LOGGER.info("{} Actualizacin de Usuario: {} - {} | Rol {} | {}", session.getTraceLog(), objActual.getCodigoEmpleado(), objActual.getCodigo(), objActual.getCodigoRol(), objActual.getLastUpdate() );
				notasContablesManager.updateUsuarioModulo(objActual, getCodUsuarioLogueado(), cambioRol && objActual.getEstado().equals("A"));
			}
				//}
				return true;
		} catch (Exception e) {
			LOGGER.error("{} Ya existe el usuario con el mismo Rol: {}", session.getTraceLog(),objActual.getCodigo() + " "+objActual.getCodigoEmpleado() + " "+objActual.getCodigoRol() +" "+ objActual.getNombreAreaModificado()  , e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe el usuario con el mismo Rol");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getEMailModificado(), "Correo electrnico");

		if (Integer.valueOf(objActual.getCodigoAreaModificado()) <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar la sucursal");
		}
		if (objActual.getCodigoPerfilModificado().equals("-1")) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el perfil");
		}
		if (Integer.valueOf(rolSel) <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un rol");
		}
	}

	private boolean canUpdate() throws Exception {
		// se consulta la informacin anterior del usuario para verificar si se cambi de rol
		UsuarioModulo usuarioModuloOriginal = notasContablesManager.getUsuarioModulo(objActual);
		// se verifica el cambio de rol solo para padrinos y autorizadores
		if (usuarioModuloOriginal.getCodigoRol().intValue() == IRol.PADRINOS || usuarioModuloOriginal.getCodigoRol().intValue() == IRol.AUTORIZADOR) {
			Padrino padrino = new Padrino();
			padrino.setCodigoUsuario(usuarioModuloOriginal.getCodigo().intValue());
			padrino = notasContablesManager.getPadrinoPorUsuario(padrino);

			// si no esta asociado a un padrino se evalua si es ente autorizador
			if (padrino.getCodigo().intValue() == 0 || padrino.getEstado().equals("I")) {

				EnteAutorizador enteAutorizador = new EnteAutorizador();
				enteAutorizador.setCodigoUsuarioModulo(usuarioModuloOriginal.getCodigo().intValue());
				enteAutorizador = notasContablesManager.getEnteAutorizadorPorUsuario(enteAutorizador);

				// es ente autorizador
				if (enteAutorizador.getCodigo().intValue() != 0) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se puede cambiar el rol del usuario porque est asociado a un Ente Autorizador");
					return false;
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se puede cambiar el rol del usuario porque est asociado a un Padrino");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean _cambiarEstado() throws Exception {
		try {
			String status = objActual.getEstado().equals("A") ? "INACTIVO" : "ACTIVO";
			LOGGER.info("{} Cambio de Estado a {} Usuario: {} - {}", session.getTraceLog(), status, objActual.getCodigoEmpleado(), objActual.getCodigo());
			notasContablesManager.changeEstadoUsuarioModulo(notasContablesManager.getUsuarioModulo(objActual), getCodUsuarioLogueado());
			return true;
		} catch (Exception e) {
			LOGGER.error("{} No existe otro Usario con el mismo Rol para este Centro ", session.getTraceLog(), e);
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No existe otro Usario con el mismo Rol para este Centro");
		}
		return false;
	}

	@Override
	public boolean _borrar() throws Exception {
		LOGGER.info("{} Borra usuario: {}", session.getTraceLog(),objActual.getCodigo() + " "+objActual.getCodigoEmpleado() + " "+objActual.getCodigoRol() );
		notasContablesManager.deleteUsuarioModulo(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				roles = getSelectItemList(notasContablesManager.getCV(Rol.class), false);
				sucursales = getSelectItemList(notasContablesManager.getCV(Sucursal.class));
				perfiles = getSelectItemList(notasContablesManager.getCV(Perfil.class));
			} catch (Exception e) {
				//e.printStackTrace();
				LOGGER.error("{} Error al inicializar el mdulo de administracin de usuarios ", session.getTraceLog(), e);
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administracin de usuarios");
			}
		}
	}

	@Override
	protected String _getPage() {
		return USUARIO;
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<SelectItem> perfiles) {
		this.perfiles = perfiles;
	}

	public String getRolSel() {
		return rolSel;
	}

	public void setRolSel(String rolSel) {
		this.rolSel = rolSel;
	}
}
