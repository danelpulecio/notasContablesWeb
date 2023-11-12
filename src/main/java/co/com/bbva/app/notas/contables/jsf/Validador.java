package co.com.bbva.app.notas.contables.jsf;

import javax.faces.application.FacesMessage;
import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;


public class Validador {

	private final BasePage page;

	public Validador(BasePage page) {
		super();
		this.page = page;
	}

	public void validarEMail(String aValidar, String nombre) {
		if (aValidar != null && !aValidar.toString().contains("@")) {
			page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "El campo '" + nombre + "' debe ser un correo vlido");
		}
	}

	public void validarLongitud(Object aValidar, String nombre, int longitud) {
		validarLongitud(aValidar, nombre, longitud, false);
	}

	public void validarLongitud(Object aValidar, String nombre, int longitud, boolean requerido) {
		if (requerido) {
			validarRequerido(aValidar, nombre);
		} else if (aValidar != null && !aValidar.toString().isEmpty() && aValidar.toString().length() != longitud) {
			page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "El campo '" + nombre + "' debe tener una longitud de " + longitud);
		}
	}

	/**
	 * Si es cadena de caracteres una cadena vacia lanza error, si es numrico 0 o negativo lanza error
	 * 
	 * @param aValidar
	 * @param nombre
	 */
	public void validarRequerido(Object aValidar, String nombre) {
		if (!validarVacio(aValidar)) {
			page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "El campo '" + nombre + "' es requerido");
		}
	}
	
	public void validarFechaMenor(Object va11, Object val2, String fechini, String fechfin){

		String fechaini =(String)va11;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/dd/mm");
		Date fecha1 = (Date) ft.parse(fechaini, new ParsePosition(0));
		String fechafin = (String)val2;
		Date fecha2 = (Date) ft.parse(fechafin, new ParsePosition(0));
		
		if(fecha2.after(fecha1)){
			
			page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha '"+fechfin+ "'no puede ser mayor que la fecha "+fechini);
	
		}
		 
	}
	
	
	
	/**
	 * Valida que el campo asociado a una seleccion sea valido
	 * 
	 * @param aValidar
	 * @param nombre
	 */
	public void validarSeleccion(Object aValidar, String nombre) {
		if (!validarVacio(aValidar)) {
			page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "Seleccione una opcion para el campo '" + nombre + "'");
		}
	}
	
	public void Diferenciafecha(String fecha1, String fecha2){
	
		page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "La "+fecha1+ " no puede ser mayor que la "+fecha2);
		
	}
	
	public void DiferencaValor(String val1, String val2){
		
		page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "El valor de  "+val1+ " no puede ser mayor que el valor de "+val2);
	}
	
	

	private boolean validarVacio(Object aValidar) {
		if (aValidar == null) {
			return false;
		} else if (aValidar instanceof String) {
			if (((String) aValidar).isEmpty()) {
				return false;
			}
		} else if (aValidar instanceof Number) {
			if (((Number) aValidar).doubleValue() <= 0D) {
				return false;
			}
		}
		return true;
	}

	public void validarPositivo(Number aValidar, String nombre) {
		boolean valido = true;
		if (aValidar == null) {
			valido = false;
		} else if (aValidar.doubleValue() <= 0) {
			valido = false;
		}
		if (!valido) {
			page.nuevoMensaje(FacesMessage.SEVERITY_WARN, "El valor del campo '" + nombre + "' debe ser positivo");
		}
	}

}
