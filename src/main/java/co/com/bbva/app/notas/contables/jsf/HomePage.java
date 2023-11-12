package co.com.bbva.app.notas.contables.jsf;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.carga.dto.UsuarioAltamira;
import co.com.bbva.app.notas.contables.carga.manager.CargaAltamiraScheduled;
import co.com.bbva.app.notas.contables.dao.SessionTraceDao;
import co.com.bbva.app.notas.contables.dto.CentroEspecial;
import co.com.bbva.app.notas.contables.dto.Rol;
import co.com.bbva.app.notas.contables.dto.SessionTraceDto;
import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
import co.com.bbva.app.notas.contables.jsf.adminnota.PendientePage;
import co.com.bbva.app.notas.contables.jsf.beans.UsuarioLogueado;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
//@Named
//@RequestScoped
public class HomePage extends GeneralPage implements IPages {

	
	private PendientePage pendientePage;

	private static final long serialVersionUID = 2629019390528133986L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);
	private final CargaAltamiraScheduled cargaAltamiraScheduled = new CargaAltamiraScheduled();
	private final SessionTraceDao sessionTrace = new SessionTraceDao();
	private final String DIR_AUTH_LDAP;
	private String username;
	private String pwd;
	private String rolActual;
	private List<SelectItem> roles = null;

	public String cronCierreHist() {
		cargaAltamiraScheduled.procesarHistorico();
		return null;
	}

	public String cronCierreAnulado() {
		cargaAltamiraScheduled.procesarAnulados();
		return null;
	}

	public String cronCierreAnular() {
		cargaAltamiraScheduled.procesarAnular();
		return null;
	}

	public String cronActPend() {
		try {
			cargaAltamiraScheduled.loadProcesarActividadesPendientes("micorreo.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public HomePage() {
		super();
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		DIR_AUTH_LDAP = context.getInitParameter("DIR_AUTH_LDAP");
		_init();
	}

	/**
	 * <p>
	 * Automatically managed component initialization. <strong>WARNING:</strong> This method is automatically generated, so any user-specified code inserted here is subject to being replaced.
	 * </p>
	 */
	@Override
	protected void _init() {
		rolActual = "";
		roles = new ArrayList<>();
		username = "";
		pwd = "";

	}

	public String start() {
		return LOGIN;
	}

	private boolean ldapAuthenticate(String user, String password, String traceLog) {
		LOGGER.info("{} URL LDAP Sever: {}", traceLog, DIR_AUTH_LDAP);
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, DIR_AUTH_LDAP);
		env.put(Context.SECURITY_PRINCIPAL, "CN=" + user + "/O=BBVA");
		env.put(Context.SECURITY_CREDENTIALS, password);
		try {
			LdapContext ctx = new InitialLdapContext(env, null);
			LOGGER.info("{} El usuario: {} se ha autenticado correctamente", user, traceLog);
			ctx.close();
			return true;
		} catch (NamingException e) {
			LOGGER.error("{} Error al realizar la autenticacin contra el LDAP", traceLog, e);
			return false;
		}
	}

	public String login() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Session session = new Session(username, request.getRemoteAddr());
		getContablesSessionBean().setSessionTrace(session);
		LOGGER.info("{} El usuario {} est intentando iniciar sesin", session.getTraceLog(), session.getUser());
		try {
			LOGGER.info("{} Persistiendo datos al intentar ingresar", session.getTraceLog());
			sessionTrace.saveTrace(new SessionTraceDto(session));
			UsuarioLogueado usrLogueado = cargarUsuario(session.getTraceLog(), session.getUser());
			if (usrLogueado != null) {
				getContablesSessionBean().setLoginUser(usrLogueado);
				LOGGER.info("{} Usuario {} - Roles asociados: {}", session.getTraceLog(), session.getUser(), usrLogueado.getRoles().size());
				if (usrLogueado.getRolActual() == null && usrLogueado.getRoles().size() > 1) {
					return LOGIN;
				}
				return setRol();
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al ingresar al aplicativo con el usuario: {} ", session.getTraceLog(), session.getUser(), e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al iniciar sesin");
		}
		return null;
	}

	public String logout() {
		UsuarioLogueado usrLoggedIn = getContablesSessionBean().getLoginUser();
		Session session = getContablesSessionBean().getSessionTrace();
		try {
			notasContablesManager.addRegistroAuditoriaIngreso(usrLoggedIn.getUsuario().getCodigo().intValue(), "Salir de la aplicacin", "Logout", "0");
		} catch (Exception e) {
			LOGGER.error("{} Error al cerrar la sesin",session.getTraceLog(), e);
		}
		LOGGER.info("{} El usuario {} cerro la sesin de forma exitosa", session.getTraceLog(), session.getUser());
		getContablesSessionBean().setLoginUser(null);
		return INICIO;
	}

	public String setRol() {
		UsuarioLogueado usrLoggedIn = getContablesSessionBean().getLoginUser();
		Session session = getContablesSessionBean().getSessionTrace();
		SessionTraceDto sTrace = new SessionTraceDto(session);
		LOGGER.info("{} Configurando el rol para el usuario: {}", session.getTraceLog(), session.getUser());
		if (usrLoggedIn != null) {
			Rol rol = new Rol();
			rol.setCodigo(Integer.valueOf(rolActual));
			try {
				usrLoggedIn.setRolActual(notasContablesManager.getRol(rol));
				usrLoggedIn.setUsuario(notasContablesManager.getUsuarioByCodEmpAndRol(usrLoggedIn.getUsuario().getCodigoEmpleado(), rol.getCodigo().intValue()));
				// new code to fix deploy temas and conceptos
				Sucursal sucursal = new Sucursal();
				sucursal.setCodigo(usrLoggedIn.getUsuario().getCodigoAreaModificado());
				sucursal = cargaAltamiraManager.getSucursal(sucursal);
				usrLoggedIn.setSucursal(sucursal);
				CentroEspecial centroEspecial = new CentroEspecial();
				centroEspecial.setCodigoSucursal(usrLoggedIn.getUsuario().getCodigoAreaModificado());
				centroEspecial = notasContablesManager.getCentroEspecialPorSucursal(centroEspecial);
				usrLoggedIn.setCentroEspecial(centroEspecial);
				// end of the new code to fix deploy  temas and conceptos
				LOGGER.info("{} Persistiendo datos del inicio de sesin", session.getTraceLog());
				sTrace.setUserId(usrLoggedIn.getUsuario().getCodigo().intValue());
				sTrace.setLoginSuccess(Boolean.TRUE);
				sessionTrace.updateTrace(sTrace);
				LOGGER.info("{} Verificando accesos para el Rol {} - Usuario: {}", session.getTraceLog(), rol.getNombre(), session.getUser());
				FacesContext facesContext = FacesContext.getCurrentInstance();
				Application application = (Application) facesContext.getExternalContext().getApplicationMap().get("yourApplicationName");
				usrLoggedIn.setMenu(notasContablesManager.getMenu(rol.getCodigo().intValue()), application);
				notasContablesManager.addRegistroAuditoriaIngreso(usrLoggedIn.getUsuario().getCodigo().intValue(), "Ingres a la aplicacin", "Login", "1");
				LOGGER.info("{} Obteniendo los pendientes para el usuario {}", session.getTraceLog(), session.getUser());
				PendientePage bean = pendientePage;
				bean.cargarPendientes();
				if (!bean.getDatos().isEmpty()) {
					LOGGER.info("{} Inicio de sesin exitoso", session.getTraceLog());
					return ADMIN_PENDIENTES;
				}
			} catch (Exception e) {
				LOGGER.error("{} Error al establecer la configuracin del rol", session.getTraceLog(), e);
			}
		}
		LOGGER.info("{} Inicio de sesin exitoso", session.getTraceLog());
		return BIENVENIDO;
	}

	private UsuarioLogueado cargarUsuario(String traceLog, String user) {
		try {
			UsuarioLogueado usrLogueado = null;
			UsuarioModulo usuario = new UsuarioModulo();
			LOGGER.info("{} Validando la informacin del usuario", traceLog);
			usuario.setCodigoEmpleado(username);
			LOGGER.info("{} Obteniendo informacin del usuario: {}", traceLog, user);
			usuario = notasContablesManager.getUsuarioModuloPorCodigoEmpleado(usuario);
			if (usuario.getCodigo().intValue() != 0) {
				LOGGER.info("{} Verificando credenciales contra el LDAP", traceLog);
				boolean authenticate = ACTIVAR_LDAP.equals("0") || ldapAuthenticate(username, pwd, traceLog);
				if (authenticate) {
					usrLogueado = new UsuarioLogueado(usuario);
					LOGGER.info("{} Obteniendo el/los roles asociados al usuario: {}", traceLog, user);
					Collection<UsuarioModulo> usuariosPorRol = notasContablesManager.getUsuariosModuloPorCodigoEmpleado(usuario);
					if (!usuariosPorRol.isEmpty()) {
						for (UsuarioModulo usr: usuariosPorRol) {
							Rol rol = new Rol();
							rol.setCodigo(usr.getCodigoRol().intValue());
							rol = notasContablesManager.getRol(rol);
							if (usuariosPorRol.size() == 1) {
								usrLogueado.setRolActual(rol);
								rolActual = rol.getCodigo().toString();
							}
							usrLogueado.getRoles().add(rol);
						}
					} else {
						LOGGER.warn("{} El usuario {} se encuentra: INACTIVO", traceLog, user);
						nuevoMensaje(FacesMessage.SEVERITY_WARN, "Usuario inactivo. Contacte al administrador del sistema");
						return null;
					}
				} else {
					LOGGER.error("{} Hubo un error al autenticarse contra el LDAP - Usuario: {}", traceLog, user);
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "Contrasea invlida. Por favor rectifique e intente de nuevo.");
				}
			} else {
				LOGGER.error("{} No existe informacin para el usuario {}", traceLog, user);
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El usuario no existe. Por favor rectifique e intente de nuevo.");
			}
			if (usrLogueado != null) {
				LOGGER.info("{} Configurando informacin adicional del usuario: {}", traceLog, user);
				UsuarioAltamira usuarioAltamira = new UsuarioAltamira();
				usuarioAltamira.setCodigoEmpleado(usrLogueado.getUsuario().getCodigoEmpleado());
				usrLogueado.setUsuAltamira(cargaAltamiraManager.getUsuarioAltamira(usuarioAltamira));

				// old piece of  code move to the setRol method
			}
			return usrLogueado;
		} catch (Exception e) {
			LOGGER.error("{} Error al autenticarse con el usuario: {}", traceLog, user, e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al autenticarse");
		}
		return null;
	}

	public String getUsername() {
		if (username == null) {
			username = "";
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRolActual() {
		return rolActual;
	}

	public void setRolActual(String rolActual) {
		this.rolActual = rolActual;
	}

	public List<SelectItem> getRoles() {
		if (getContablesSessionBean().getLoginUser() != null) {
			roles = new ArrayList<>();
			for (Rol rol : getContablesSessionBean().getLoginUser().getRoles()) {
				roles.add(new SelectItem(rol.getCodigo(), rol.getNombre()));
			}
		}
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

}
