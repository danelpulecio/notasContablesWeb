package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.Auditoria;
import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.Collection;

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad Auditoria
 * </p>
 * 
 */
@SessionScoped
@Named
public class AuditoriaPage extends GeneralParametrosPage<Auditoria, Auditoria> {

	String param = getParam();
	private static final long serialVersionUID = 1L;

	private Auditoria auditoria;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditoriaPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

	public AuditoriaPage() {
		super(true);
		auditoria = new Auditoria();
	}

	/**
	 * retorna una instancia de Auditoria
	 * 
	 * @return
	 */
	@Override
	protected Auditoria _getInstance() {
		return new Auditoria();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	public Collection<Auditoria> _buscarTodos() throws Exception {
		return notasContablesManager.getRegistrosAuditoria();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Auditoria filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Auditoria> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar Auditoria: {}", session.getTraceLog(),param );
		}
		return notasContablesManager.searchRegistrosAuditoria(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		// TODO agregar funcionalidad de edicion de datos
	}

	@Override
	protected boolean _guardar() throws Exception {
		// TODO agregar funcionalidad de guardado o actualizacion
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		// TODO logica de validacion
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		return true;
	}

	public String consultarDetalle() {
		try {
			auditoria = notasContablesManager.getRegistroAuditoria(auditoria.getCodigo().intValue());
			auditoria.setDetalle(notasContablesManager.getDetalleAuditoriaPorCodigoAuditoria(auditoria.getCodigo().intValue()));
			UsuarioModulo us = new UsuarioModulo();
			us.setCodigo(auditoria.getCodigoUsuario().intValue());
			auditoria.setUsuario(notasContablesManager.getUsuarioModulo(us));
		} catch (Exception e) {
			LOGGER.error("{} Hubo un error al consultar la auditora en el sistema" , e  );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Hubo un error al consultar la auditora en el sistema");

		}
		return null;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Auditoria
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		return true;
	}

	@Override
	protected String _getPage() {
		return AUDITORIA;
	}

	public Auditoria getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}
}
