package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.Instancia;
import co.com.bbva.app.notas.contables.dto.NotaContable;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.*;

@SessionScoped
@Named
public class ConsultaNotaContablePage extends GeneralConsultaPage<Instancia> {

	private static final long serialVersionUID = -6709113217662690209L;

	private List<SelectItem> tiposCriterio = new ArrayList<>();
	private String numRadicacion;
	private String criterio;
	private String asientoContable;
	private Date fecha;
	
	//gp12833 - aseguramiento anexos
	private static boolean recuperarAnexos;
	// Fin - gp12833 - aseguramiento anexos

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaNotaContablePage.class);

	@PostConstruct
	public void init(){
		LOGGER.info("postConstructor: {}", tiposCriterio);
		if (tiposCriterio == null || tiposCriterio.isEmpty()) {
			tiposCriterio = new ArrayList<>();
			fecha = Calendar.getInstance().getTime();
			criterio = "1";
			tiposCriterio.add(new SelectItem("1", "Numero de radicacion"));
			tiposCriterio.add(new SelectItem("2", "Asiento contable Altamira"));
			tiposCriterio.add(new SelectItem("3", "Fecha Radicacion Modulo"));
		}
		LOGGER.info("postConstructor cargado: {}", tiposCriterio);
	}

//	Session session = getContablesSessionBean().getSessionTrace();
		
	/**
	 * indica el numero de pagina del scroller de actividades
	 */
	private Integer scrollPageAct = 1;

	public ConsultaNotaContablePage() {
		super();
	}
	
	@Override
	protected void _init() {
		super._init();
		if (tiposCriterio == null || tiposCriterio.isEmpty()) {
			tiposCriterio = new ArrayList<>();
			fecha = Calendar.getInstance().getTime();
			criterio = "1";
			tiposCriterio.add(new SelectItem("1", "Numero de radicacion"));
			tiposCriterio.add(new SelectItem("2", "Asiento contable Altamira"));
			tiposCriterio.add(new SelectItem("3", "Fecha Radicacin Modulo"));
		}
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {
		NotaContable notaContable = new NotaContable();
		switch (Integer.valueOf(criterio.trim())) {
		case 1:
			notaContable.setNumeroRadicacion(numRadicacion);
//			LOGGER.info("{} Consulta nota contable por numero radicacion : {}", session.getTraceLog(),numRadicacion );
			return new ArrayList<>(notasContablesManager.getInstanciasPorNumeroRadicacion(notaContable, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		case 2:
			notaContable.setAsientoContable(asientoContable);
			notaContable.setFechaRegistroAltamiraTs(new Timestamp(fecha.getTime()));
//			LOGGER.info("{} Consulta nota contable por asiento contable : {}", session.getTraceLog(),asientoContable );
			return new ArrayList<>(notasContablesManager.getInstanciasPorAsientoContableAndFecha(notaContable, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		case 3:
			notaContable.setFechaRegistroAltamiraTs(new Timestamp(fecha.getTime()));
//			LOGGER.info("{} Consulta nota contable por instancia : {}", session.getTraceLog(),new Timestamp(fecha.getTime()) );
			return new ArrayList<>(notasContablesManager.getInstanciasPorNotaContable(notaContable, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		}
		return new ArrayList<>();
	}

	@Override
	protected void _validar() throws Exception {
		switch (Integer.valueOf(criterio.trim())) {
		case 1:
			validador.validarRequerido(numRadicacion, "número de radicación");
			break;
		case 2:
			validador.validarRequerido(asientoContable, "Asiento contable");
		case 3:
			validador.validarRequerido(fecha, "Fecha");
			break;
		}
	}

	@Override
	protected String _getPage() {
		return CONSULTA_NOTA_CONTABLE;
	}

	public String iniciar() {
		recuperarAnexos = false;
		return super.iniciar();
	}
	
	public String recuperarAnexos() {
		String vista = iniciar();
		recuperarAnexos = true;
		return vista;
	}
	
	public boolean getProcesoRecuperarAnexos() {
		return recuperarAnexos;
	}
	// Fin - gp12833 - aseguramiento anexos
	
	public List<SelectItem> getTiposCriterio() {
		LOGGER.info("Tipos de criterios: {}", tiposCriterio);
		return tiposCriterio;
	}

	public void setTiposCriterio(List<SelectItem> tiposCriterio) {
		this.tiposCriterio = tiposCriterio;
	}

	public String getNumRadicacion() {
		return numRadicacion;
	}

	public void setNumRadicacion(String numRadicacion) {
		this.numRadicacion = numRadicacion;
	}

	public String getCriterio() {
		LOGGER.info("Criterio: {}", criterio);
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	public String getAsientoContable() {
		return asientoContable;
	}

	public void setAsientoContable(String asientoContable) {
		this.asientoContable = asientoContable;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
