package co.com.bbva.app.notas.contables.carga.dto;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.bbva.app.notas.contables.jsf.HomePage;

public class MenuItem implements Serializable {

	private static final long serialVersionUID = -6568299162456667984L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);
	
	private String actionNC 	= "";
	private boolean disabledNC;
	private String valueNC	= "";
	public String getActionNC() {
		LOGGER.info("::::::::::: ENTRANDO A REDIRECCIONAR MENU:::::::::::");
		return actionNC;
	}
	public void setActionNC(String actionNC) {
		this.actionNC = actionNC;
	}
	public boolean isDisabledNC() {
		return disabledNC;
	}
	public void setDisabledNC(boolean disabledNC) {
		this.disabledNC = disabledNC;
	}
	public String getValueNC() {
		return valueNC;
	}
	public void setValueNC(String valueNC) {
		this.valueNC = valueNC;
	}
	
}
