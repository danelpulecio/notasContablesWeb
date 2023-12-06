package co.com.bbva.app.notas.contables.carga.dto;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.bbva.app.notas.contables.jsf.HomePage;

public class MenuItem implements Serializable {

	private static final long serialVersionUID = -6568299162456667984L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItem.class);
	
	private String action 	= "";
	private boolean disabled;
	private String value	= "";
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
