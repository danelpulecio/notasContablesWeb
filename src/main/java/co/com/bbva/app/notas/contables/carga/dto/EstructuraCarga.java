package co.com.bbva.app.notas.contables.carga.dto;

public class EstructuraCarga implements java.io.Serializable {

	private static final long serialVersionUID = 114373234886697758L;
	
	private int longitud = 0;
	private String tipo = "C";

	public int getLongitud() {
		return longitud;
	}

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
