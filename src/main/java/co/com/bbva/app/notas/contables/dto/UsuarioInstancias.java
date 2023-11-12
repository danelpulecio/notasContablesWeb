package co.com.bbva.app.notas.contables.dto;

import java.util.ArrayList;
import java.util.Collection;

public class UsuarioInstancias implements java.io.Serializable {

	private static final long serialVersionUID = 3991379598331416548L;

	private UsuarioModulo usuarioModulo = new UsuarioModulo();
	private Collection<Instancia> instanciasAsignadas = new ArrayList<Instancia>();

	public UsuarioModulo getUsuarioModulo() {
		return usuarioModulo;
	}

	public void setUsuarioModulo(UsuarioModulo usuarioModulo) {
		this.usuarioModulo = usuarioModulo;
	}

	public Collection<Instancia> getInstanciasAsignadas() {
		return instanciasAsignadas;
	}

	public void setInstanciasAsignadas(Collection<Instancia> instanciasAsignadas) {
		this.instanciasAsignadas = instanciasAsignadas;
	}

	public Integer getCantInstancias() {
		return instanciasAsignadas.size();
	}
}
