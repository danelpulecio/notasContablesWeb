package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.Tema;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@SessionScoped
@Named
public class TemasCuentasInactivasPage extends GeneralConsultaPage<Tema> {

	private static final long serialVersionUID = -6709113217662690209L;

	public TemasCuentasInactivasPage() {
		super();
	}

	@PostConstruct
	public void init() {
		super._init();
		try {
			if (getDatos() == null || getDatos().isEmpty()) {
				setDatos(notasContablesManager.getTemasCuentasInactivas());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Collection<Tema> _buscar() throws Exception {
		return new ArrayList<Tema>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return TEMAS_INACTIVOS;
	}

}
