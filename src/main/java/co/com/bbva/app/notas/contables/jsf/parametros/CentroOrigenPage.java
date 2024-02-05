package co.com.bbva.app.notas.contables.jsf.parametros;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad PUC
 * </p>
 * 
 */
@SessionScoped
@Named
public class CentroOrigenPage extends GeneralCentroPage {

	private static final long serialVersionUID = 1L;

	public CentroOrigenPage() {
		super(true);
	}

	@Override
	protected String _getPage() {
		return CENTRO_ORIGEN;
	}
}
