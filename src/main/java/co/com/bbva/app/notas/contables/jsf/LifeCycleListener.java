package co.com.bbva.app.notas.contables.jsf;

import co.com.bbva.app.notas.contables.jsf.beans.ContablesSessionBean;
import co.com.bbva.app.notas.contables.jsf.beans.MenuVisual;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuItem;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;

/**
 * Container managed security doesn't work well with web frameworks that perform server side page forwards to perform navigation. This JavaServer Faces PhaseListener uses container managed authentication and authorization to solves some of the
 * security issues in J2EE
 * 
 * - It allows fine grained authorization and authentication including secure channel - An application can switch between SSL and non-SSL usage for each individual page - If required, a flag can be set so that once SSL is enabled it is kept for all
 * pages - Pages could require authentication only - J2EE security roles can be ANDed or ORed together
 * 
 * J2EESecurityPhaseListener enforces J2EE authentication and authorization for Java Server Faces applications. The goal is to provide the level of authorization that is supported in Struts.
 * 
 * The PhaseListener is automatically configured when adding the jsf security jar file to the application's library path. The listener configuration is defined in the faces- config.xml file of the META-INF directory in the jar file
 * 
 * &lt;lifecycle&gt; &lt;phase-listener>com.groundside.jsf.security.navigation.listener.J2EESecurityPhaseListener&lt;/phase-listener&gt; &lt;/lifecycle&gt;
 * 
 * Configure the faces-security-config.xml in the WEB-INF directory of your JavaServer Faces application. The faces-security-configuration contains all the authorization definitions for the JavaServer Faces application. A page is registerd with its
 * complete path relative to public_html, e.g. /protected/start.jsp.
 * 
 * As a developer, configuring the PhaseListener and the authentication servlet is all you need to do.
 * 
 * Wildcards are supported at the end of a path. To protect all pages in a directory protected", the page entry in the security configuration file needs to be /protected/*
 * 
 * Authentication is enforced by a redirect to an authentication servlet that need to be configured in the web.xml configuration file. If a user is already authenticated then the authentication servlet is not called. The authentication servlet allows
 * developers to specify a page as to require authentication but no authorization, which is a common scenario.
 * 
 * Pages are protected by J2EE roles, where one or many roles can be specified for a JSF page. The role named can be ORed or ANDed together, which is defined in the faces-security-config.xml file.
 * 
 * Note that all security roles used in the configuration file must either exist in the web.xml deployment descriptor or mapped to other roles in this file. Pleas refer to the J2EE Servlet specifictation for how to configure security roles.
 * 
 * <h2>Servlet context parameters</h2> Context parameters supported by the J2EESecurityViewHandler <h3>SECURITY_DEBUG</h3>
 * 
 * &nbsp;&nbsp;&lt;context-param&gt; &nbsp;&nbsp;&nbsp;&lt;param-name&gt;com.groundside.jsf.security.print_debug_messages&lt;/param-name&gt; &nbsp;&nbsp;&nbsp;&lt;param-value&gt;true&lt;/param-value&gt; &nbsp;&nbsp;&lt;/context-param&gt;
 * 
 * Prints debug messages to analyze problems at runtime. If the parameter is not set in web.xml then no debug messages are printed
 * 
 * @author Frank Nimphius
 * @version 1.0, April 2006
 * 
 */

public class LifeCycleListener implements PhaseListener {

	private static final long serialVersionUID = 4375570851197813777L;
	private static final String LOGIN_PAGE = "/loginPage.xhtml";
	private static final String INDEX = "/loginPage.xhtml";
	private static final String INICIO = "/inicio.xhtml";

	public LifeCycleListener() {
	}

	public void beforePhase(PhaseEvent phaseEvent) {
	}

	public void afterPhase(PhaseEvent phaseEvent) {
		PhaseId phaseid = phaseEvent.getPhaseId();

		if (phaseid == PhaseId.RESTORE_VIEW || phaseid == PhaseId.INVOKE_APPLICATION) {

			// se consulta el cache de paginas autorizadas para el usuario

			HashSet<String> _userPageCache = getUserSessionPageAuthorizationCache();
			if (FacesContext.getCurrentInstance().getViewRoot() != null) {
				// se analizan peticiones a paginas puntuales (xhtml diferentes del login, index e inicio)
				String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
				if (viewId != null && !viewId.equals(LOGIN_PAGE) && !viewId.equals(INDEX) && !viewId.equals(INICIO) && viewId.toLowerCase().endsWith(".xhtml")) {

					// si el usuario ya registra permiso para la pagina actual, se omite la autenticacion
					if (!_userPageCache.contains(viewId)) {
						// se verifica en los roles si tiene permisos de acceso
						boolean pagAutorizada = handleSecurity(phaseEvent, viewId);
						if (pagAutorizada) {
							_userPageCache.add(viewId);
						}
					}
				}
			}
		} 
	}

	public PhaseId getPhaseId() {
		// listen to any phase, we sort the interesting
		// phase events in the before and after Phase
		// methods
		return PhaseId.ANY_PHASE;
	}

	/**
	 * ********************************************* HANDLE SECURITY *
	 * *********************************************/

	@SuppressWarnings("unchecked")
	private HashSet<String> getUserSessionPageAuthorizationCache() {
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		Map sessionScopeMap = ectx.getSessionMap();
		HashSet<String> _userPageCache = (HashSet<String>) sessionScopeMap.get("userAccessCache");
		if (_userPageCache == null) {
			_userPageCache = new HashSet<String>();
			sessionScopeMap.put("userAccessCache", _userPageCache);
		}
		return _userPageCache;
	}

	private boolean handleSecurity(PhaseEvent phaseEvent, String viewId) {
		FacesContext facesContext = phaseEvent.getFacesContext();

		ExternalContext exctx = facesContext.getExternalContext();
		HttpSession session = (HttpSession) exctx.getSession(true);

		ContablesSessionBean c = (ContablesSessionBean) session.getAttribute("contablesSessionBean");

		// if keep ssl mode is configured in faces-security-config.xml then SSL
		// connection is enforced for all subsequent requests
		boolean authorizedUser = c != null && c.getLoginUser() != null && authorizePageAccess(viewId, c);

		// si el usuario no est autorizado
		if (!authorizedUser) {
			// si es un usuario logueado
			if (c != null && c.getLoginUser() != null) {
				facesContext.getViewRoot().setViewId(INICIO);  
			} else {
				facesContext.getViewRoot().setViewId(LOGIN_PAGE);
			} 
			return false;
		}
		return true; 
	}

	private boolean authorizePageAccess(String viewId, ContablesSessionBean c) {
		String filtro = viewId.substring(viewId.lastIndexOf('/') + 1, viewId.lastIndexOf('.'));
		if (c != null) {
			for (MenuVisual m : c.getLoginUser().getOpcionesMenu()) {
				for (MenuItem item : m.getMenuItems()) {
					if (item instanceof DefaultMenuItem) {
						DefaultMenuItem defaultMenuItem = (DefaultMenuItem) item;
						String action = defaultMenuItem.getCommand(); // Obtener la accin del elemento de men
						if (action != null) {
							String actActual = action.substring(action.indexOf('{') + 1, action.lastIndexOf('.'));
							if (actActual.equals(filtro)) {
								return true;
							}
						}
					}
					// Puedes agregar un bloque similar para DefaultSubMenu si lo necesitas
				}
			}
		}
		return false;
	}

//	private boolean authorizePageAccess(String viewId, ContablesSessionBean c) {
//		String filtro = viewId.substring(viewId.lastIndexOf('/') + 1, viewId.lastIndexOf('.'));
//		if (c != null) {
// 			for (MenuVisual m : c.getLoginUser().getOpcionesMenu()) {
//				for (HtmlMenuItem item : m.getMenuItems()) {
//					String action = item.getActionExpression().getExpressionString();
//					String actActual = action.substring(action.indexOf('{') + 1, action.lastIndexOf('.'));
//					if (actActual.equals(filtro)) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

}
