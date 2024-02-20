package co.com.bbva.app.notas.contables.jsf.beans;

import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
import co.com.bbva.app.notas.contables.jsf.BasePage;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Session scope data bean for your application. Create properties here to represent cached data that should be made available across multiple HTTP requests for an individual user.
 * </p>
 *
 * <p>
 * An instance of this class will be created for you automatically, the first time your application evaluates a value binding expression or method binding expression that references a managed bean using this class.
 * </p>
 *
 * @author amocampo
 * @version Created on 3/12/2008, 04:15:47 PM
 */

@Named
@SessionScoped
public class ContablesSessionBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContablesSessionBean.class);


//    @Inject
//    ContablesApplicationBean contablesApplicationBean;

    private static final long serialVersionUID = -7578896067766477600L;

    UsuarioLogueado loginUser = null;

    private Session sessionTrace = new Session();

    /**
     * <p>
     * Automatically managed component initialization. <strong>WARNING:</strong> This method is automatically generated, so any user-specified code inserted here is subject to being replaced.
     * </p>
     */
    private void _init() throws Exception {

    }

    // </editor-fold>

    /**
     * <p>
     * Construct a new session data bean instance.
     * </p>
     */
    public ContablesSessionBean() {
        LOGGER.info("este es el constructor contable session ben");
    }

    /**
     * <p>
     * This method is called when this bean is initially added to session scope. Typically, this occurs as a result of evaluating a value binding or method binding expression, which utilizes the managed bean facility to instantiate this bean and
     * store it into session scope.
     * </p>
     *
     * <p>
     * You may customize this method to initialize and cache data values or resources that are required for the lifetime of a particular user session.
     * </p>
     */
//	@Override
//	public void init() {
//		super.init();
//		try {
//			_init();
//		} catch (Exception e) {
//			log("SessionBean1 Initialization Failure", e);
//			throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
//		}
//	}

    /**
     * <p>
     * This method is called when the session containing it is about to be passivated. Typically, this occurs in a distributed servlet container when the session is about to be transferred to a different container instance, after which the
     * <code>activate()</code> method will be called to indicate that the transfer is complete.
     * </p>
     *
     * <p>
     * You may customize this method to release references to session data or resources that can not be serialized with the session itself.
     * </p>
     */
//	@Override
//	public void passivate() {
//	}

    /**
     * <p>
     * This method is called when the session containing it was reactivated.
     * </p>
     *
     * <p>
     * You may customize this method to reacquire references to session data or resources that could not be serialized with the session itself.
     * </p>
     */
//	@Override
//	public void activate() {
//	}

    /**
     * <p>
     * This method is called when this bean is removed from session scope. Typically, this occurs as a result of the session timing out or being terminated by the application.
     * </p>
     *
     * <p>
     * You may customize this method to clean up resources allocated during the execution of the <code>init()</code> method, or at any later time during the lifetime of the application.
     * </p>
     */
//	@Override
//	public void destroy() {
//	}
    @PreDestroy
    public void destroy (){
        LOGGER.info("DestruyendoContableSessionBeanBean");
    }

    /**
     * <p>
     * Return a reference to the scoped data bean.
     * </p>
     *
     * @return reference to the scoped data bean
     */
//    protected ContablesApplicationBean getGescodeApplicationBean() {
//        return contablesApplicationBean;
//    }

    public UsuarioLogueado getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(UsuarioLogueado loginUser) {
        this.loginUser = loginUser;
    }

    public String getFecha() {
        return StringUtils.getString(new Date(), "d 'de' MMMM 'de' yyyy - hh:mm");
    }

    public Session getSessionTrace() {
        LOGGER.info("getSessionTrace session trace");
        return sessionTrace;
    }

    public void setSessionTrace(Session sessionTrace) {
        LOGGER.info("setSessionTrace set session trace");
        this.sessionTrace = sessionTrace;
    }
}
