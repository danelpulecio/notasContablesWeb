/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.bbva.app.notas.contables.jsf.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * <p>
 * Request scope data bean for your application. Create properties here to represent data that should be made available across different pages in the same HTTP request, so that the page bean classes do not have to be directly linked to each other.
 * </p>
 * 
 * <p>
 * An instance of this class will be created for you automatically, the first time your application evaluates a value binding expression or method binding expression that references a managed bean using this class.
 * </p>
 * 
 * @version GescodeRequestBean.java
 * @version Created on 3/12/2008, 04:15:47 PM
 * @author amocampo
 */

@Named
@SessionScoped
public class ContablesRequestBean implements Serializable {

	
	private ContablesSessionBean contablesSessionBean;

	
	private ContablesApplicationBean contablesApplicationBean;


	// <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

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
	 * Construct a new request data bean instance.
	 * </p>
	 */
	public ContablesRequestBean() {
	}

	/**
	 * <p>
	 * This method is called when this bean is initially added to request scope. Typically, this occurs as a result of evaluating a value binding or method binding expression, which utilizes the managed bean facility to instantiate this bean and
	 * store it into request scope.
	 * </p>
	 * 
	 * <p>
	 * You may customize this method to allocate resources that are required for the lifetime of the current request.
	 * </p>
	 */
//	@Override
//	public void init() {
//		super.init();
//		try {
//			_init();
//		} catch (Exception e) {
//			log("RequestBean1 Initialization Failure", e);
//			throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
//		}
//	}

	/**
	 * <p>
	 * This method is called when this bean is removed from request scope. This occurs automatically when the corresponding HTTP response has been completed and sent to the client.
	 * </p>
	 * 
	 * <p>
	 * You may customize this method to clean up resources allocated during the execution of the <code>init()</code> method, or at any later time during the lifetime of the request.
	 * </p>
	 */
//	@Override
//	public void destroy() {
//	}

	/**
	 * <p>
	 * Return a reference to the scoped data bean.
	 * </p>
	 * 
	 * @return reference to the scoped data bean
	 */
	protected ContablesSessionBean getGescodeSessionBean() {
		return contablesSessionBean;
	}

	/**
	 * <p>
	 * Return a reference to the scoped data bean.
	 * </p>
	 * 
	 * @return reference to the scoped data bean
	 */
	protected ContablesApplicationBean getGescodeApplicationBean() {
		return contablesApplicationBean;
	}

}
