package co.com.bbva.app.notas.contables.jsf.parametros;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad PUC
 * </p>
 * 
 */
@SessionScoped
@Named
public class CentroDestinoPage extends GeneralCentroPage {

	private static final long serialVersionUID = 1L;

	public CentroDestinoPage() {
		super(false);
	}

	@Override
	protected String _getPage() {
		return CENTRO_DESTINO;
	}
}