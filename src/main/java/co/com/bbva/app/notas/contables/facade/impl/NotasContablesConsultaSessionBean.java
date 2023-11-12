package co.com.bbva.app.notas.contables.facade.impl;

import co.com.bbva.app.notas.contables.carga.dao.PartidaPendienteDAO;
import co.com.bbva.app.notas.contables.carga.dao.RechazoSalidaDAO;
import co.com.bbva.app.notas.contables.carga.dto.CierreMensual;
import co.com.bbva.app.notas.contables.carga.dto.PartidaPendiente;
import co.com.bbva.app.notas.contables.carga.dto.RechazoSalida;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.dao.*;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.facade.NotasContablesSession;
import co.com.bbva.app.notas.contables.util.DateUtils;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.*;

public abstract class NotasContablesConsultaSessionBean implements NotasContablesSession {

	protected static final int IMP_PARTIDA = 0;
	protected static final int PARTIDA = 1;
	protected static final int REG_LIBRE = 4;
	protected static final int CRUCE_REF = 5;

	protected final PartidaPendienteDAO partidaPendienteDAO = new PartidaPendienteDAO();

	protected final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
	protected final AuditoriaDetalleDAO auditoriaDetalleDAO = new AuditoriaDetalleDAO();
	protected final MontoMaximoDAO montoMaximoDAO = new MontoMaximoDAO();
	protected final RolDAO rolDAO = new RolDAO();
	protected final SubMenuRolDAO subMenuRolDAO = new SubMenuRolDAO();
	protected final UsuarioModuloDAO usuarioModuloDAO = new UsuarioModuloDAO();
	protected final TemaAutorizacionDAO temaAutorizacionDAO = new TemaAutorizacionDAO();
	protected final CausalDevolucionDAO causalDevolucionDAO = new CausalDevolucionDAO();
	protected final TipoEventoDAO tipoEventoDAO = new TipoEventoDAO();
	protected final EnteAutorizadorDAO enteAutorizadorDAO = new EnteAutorizadorDAO();
	protected final MontoAutorizadoDAO montoAutorizadoDAO = new MontoAutorizadoDAO();
	protected final MontoAutorizadoGeneralDAO montoAutorizadoGeneralDAO = new MontoAutorizadoGeneralDAO();
	protected final FechaHabilitadaDAO fechaHabilitadaDAO = new FechaHabilitadaDAO();
	protected final ImpuestoDAO impuestoDAO = new ImpuestoDAO();
	protected final CuentaCODDAO cuentaCODDAO = new CuentaCODDAO();
	protected final PadrinoDAO padrinoDAO = new PadrinoDAO();
	protected final UnidadAnalisisDAO unidadAnalisisDAO = new UnidadAnalisisDAO();
	protected final CentroEspecialDAO centroEspecialDAO = new CentroEspecialDAO();
	protected final ConceptoDAO conceptoDAO = new ConceptoDAO();
	protected final TemaImpuestoDAO temaImpuestoDAO = new TemaImpuestoDAO();
	protected final TemaProductoDAO temaProductoDAO = new TemaProductoDAO();
	protected final TemaDAO temaDAO = new TemaDAO();
	protected final RechazoSalidaDAO rechazoSalidaDAO = new RechazoSalidaDAO();

	protected final NotaContableTemaRiesgoDAO notaContableTemaRiesgoDAO = new NotaContableTemaRiesgoDAO();
	protected final NotaContableTemaImpuestoDAO notaContableTemaImpuestoDAO = new NotaContableTemaImpuestoDAO();
	protected final NotaContableTemaDAO notaContableTemaDAO = new NotaContableTemaDAO();
	protected final NotaContableTotalDAO notaContableTotalDAO = new NotaContableTotalDAO();

	protected final NotaContableRegistroLibreDAO notaContableRegistroLibreDAO = new NotaContableRegistroLibreDAO();

	protected final NotaContableCrucePartidaPendienteDAO notaContableCrucePartidaPendienteDAO = new NotaContableCrucePartidaPendienteDAO();

	protected final NotaContableDAO notaContableDAO = new NotaContableDAO();
	protected final ParametroDAO parametroDAO = new ParametroDAO();

	protected final InstanciaDAO instanciaDAO = new InstanciaDAO();
	protected final ActividadRealizadaDAO actividadRealizadaDAO = new ActividadRealizadaDAO();
	protected final AnexoDAO anexoDAO = new AnexoDAO();
	protected final ObservacionDAO observacionDAO = new ObservacionDAO();
	protected final RiesgoOperacionalSubProdDAO subProductoDAO = new RiesgoOperacionalSubProdDAO();
	protected final SubCanalDAO subCanalDAO = new SubCanalDAO();
	protected final JerarquiaOperacionSFC_N2DAO claseRiesgoN2DAO = new JerarquiaOperacionSFC_N2DAO();
	protected final JerarquiaOperacionSFC_N3DAO claseRiesgoN3DAO = new JerarquiaOperacionSFC_N3DAO();
	

	@Override
	public AuditoriaDetalle getDetalleAuditoriaPorCodigoAuditoria(int codigoAuditoria) throws Exception {
		AuditoriaDetalle auditoriaDetalle = new AuditoriaDetalle();

		auditoriaDetalle.setCodigoAuditoria(codigoAuditoria);
		return auditoriaDetalleDAO.getByCodigoAuditoria(auditoriaDetalle);
	}

	@Override
	public Auditoria getRegistroAuditoria(int codigoAuditoria) throws Exception {
		Auditoria row = new Auditoria();

		row.setCodigo(codigoAuditoria);
		return auditoriaDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<Auditoria> getRegistrosAuditoria() throws Exception {
		return auditoriaDAO.findAll();
	}

	@Override
	public Collection<Auditoria> searchRegistrosAuditoria(String palabraClave) throws Exception {
		return auditoriaDAO.findBy(palabraClave);
	}

	@Override
	public MontoMaximo getMontoMaximo(MontoMaximo row) throws Exception {
		return montoMaximoDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<MontoMaximo> getMontoMaximosPorEstado(MontoMaximo row) throws Exception {
		return montoMaximoDAO.findByEstado(row);
	}

	@Override
	public Collection<MontoMaximo> getMontoMaximos() throws Exception {
		return montoMaximoDAO.findAll();
	}

	@Override
	public Rol getRol(Rol row) throws Exception {
		return rolDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<Rol> getRolesPorEstado(Rol row) throws Exception {
		return rolDAO.findByEstado(row);
	}

	@Override
	public Collection<Rol> getRoles() throws Exception {
		return rolDAO.findAll();
	}

	@Override
	public Collection<Rol> searchRol(String palabraClave) throws Exception {
		return rolDAO.findBy(palabraClave);
	}

	@Override
	public <T extends CommonVO<T>> Map<String, String> getCV(Class<T> clazz) throws Exception {
		return new SuperDAO<T>(clazz.newInstance()).getCV();
	}

	@Override
	public <T extends CommonVO<T>> Map<String, String> getCVBy(Class<T> clazz, String filtro) throws Exception {
		return new SuperDAO<T>(clazz.newInstance()).getCVBy(filtro);
	}

	@Override
	public Map<String, String> getCVEntesAut() throws Exception {
		return enteAutorizadorDAO.getCVEntesAut();
	}

	@Override
	public UsuarioModulo getUsuarioModulo(UsuarioModulo row) throws Exception {
		return usuarioModuloDAO.findByPrimaryKey(row);
	}

	@Override
	public UsuarioModulo getUsuarioByCodEmpAndRol(String codigoEmp, int codigoRol) throws Exception {
		return usuarioModuloDAO.getUsuarioByCodEmpAndRol(codigoEmp, codigoRol);
	}

	@Override
	public UsuarioModulo getUsuarioModuloPorCodigoEmpleado(UsuarioModulo row) throws Exception {
		return usuarioModuloDAO.findByCodigoEmpleado(row);
	}

	@Override
	public Collection<UsuarioModulo> getUsuariosModuloPorCodigoEmpleado(UsuarioModulo row) throws Exception {
		return usuarioModuloDAO.findAllByCodigoEmpleado(row);
	}

	@Override
	public Collection<UsuarioModulo> getUsuariosModuloPorEstado(UsuarioModulo row) throws Exception {
		return usuarioModuloDAO.findByEstado(row);
	}

	@Override
	public Collection<UsuarioModulo> getUsuariosModuloPorRolYEstado(int codigoRol, String estado) throws Exception {
		return usuarioModuloDAO.findByRolAndEstado(codigoRol, estado);
	}

	@Override
	public Collection<UsuarioModulo> getUsuariosModuloPorAreaYRolYEstado(String codigoArea, int codigoRol, String estado) throws Exception {
		return usuarioModuloDAO.findByAreaAndRolAndEstado(codigoArea, codigoRol, estado);
	}

	@Override
	public Collection<UsuarioModulo> getUsuariosModulo() throws Exception {
		return usuarioModuloDAO.findAll();
	}

	@Override
	public List<UsuarioModulo> getUsuariosAltamiraInactivos() throws Exception {
		return usuarioModuloDAO.findByAltamiraInactivos();

	}

	@Override
	public Collection<UsuarioModulo> searchUsuarioModulo(String palabraClave) throws Exception {
		return usuarioModuloDAO.findBy(palabraClave);
	}

	@Override
	public TemaAutorizacion getTemaAutorizacion(TemaAutorizacion row) throws Exception {
		return temaAutorizacionDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<TemaAutorizacion> getTemasAutorizacionPorEstado(TemaAutorizacion row) throws Exception {
		return temaAutorizacionDAO.findByEstado(row);
	}

	@Override
	public Collection<TemaAutorizacion> getTemasAutorizacion() throws Exception {
		return temaAutorizacionDAO.findAll();
	}

	@Override
	public Collection<TemaAutorizacion> searchTemaAutorizacion(String palabraClave) throws Exception {
		return temaAutorizacionDAO.findBy(palabraClave);
	}

	@Override
	public CausalDevolucion getCausalDevolucion(CausalDevolucion row) throws Exception {
		return causalDevolucionDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<CausalDevolucion> getCausalesDevolucionPorEstado(CausalDevolucion row) throws Exception {
		return causalDevolucionDAO.findByEstado(row);
	}

	@Override
	public Collection<CausalDevolucion> getCausalesDevolucion() throws Exception {
		return causalDevolucionDAO.findAll();
	}

	@Override
	public Collection<CausalDevolucion> searchCausalDevolucion(String palabraClave) throws Exception {
		return causalDevolucionDAO.findBy(palabraClave);
	}

	@Override
	public TipoEvento getTipoEvento(TipoEvento row) throws Exception {
		return tipoEventoDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<TipoEvento> getTiposEventoPorEstado(TipoEvento row) throws Exception {
		return tipoEventoDAO.findByEstado(row);
	}

	@Override
	public Collection<TipoEvento> getTiposEvento() throws Exception {
		return tipoEventoDAO.findAll();
	}

	@Override
	public Collection<TipoEvento> searchTipoEvento(String palabraClave) throws Exception {
		return tipoEventoDAO.findBy(palabraClave);
	}

	@Override
	public EnteAutorizador getEnteAutorizador(EnteAutorizador row) throws Exception {
		return enteAutorizadorDAO.findByPrimaryKey(row);
	}

	@Override
	public EnteAutorizador getEnteAutorizadorPorUsuario(EnteAutorizador row) throws Exception {
		return enteAutorizadorDAO.findByCodigoUsuario(row);
	}

	@Override
	public Collection<EnteAutorizador> getEntesAutorizadoresPorEstado(EnteAutorizador row) throws Exception {
		return enteAutorizadorDAO.findByEstado(row);
	}

	@Override
	public EnteAutorizador getEntesAutorizadoresPorSucursalYEstado(EnteAutorizador row) throws Exception {
		return enteAutorizadorDAO.findByCodigoSucursalAndEstado(row);
	}

	@Override
	public Collection<EnteAutorizador> getEntesAutorizadores() throws Exception {
		return enteAutorizadorDAO.findAll();
	}

	@Override
	public Collection<EnteAutorizador> searchEnteAutorizador(String palabraClave) throws Exception {
		return enteAutorizadorDAO.findBy(palabraClave);
	}

	@Override
	public MontoAutorizado getMontoAutorizado(MontoAutorizado row) throws Exception {
		return montoAutorizadoDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<MontoAutorizado> getMontosAutorizadosPorEstado(MontoAutorizado row) throws Exception {
		return montoAutorizadoDAO.findByEstado(row);
	}

	@Override
	public MontoAutorizado getMontoAutorizadoPorTemaAutorizacionYTipoEventoNotaContableYEstado(MontoAutorizado row) throws Exception {
		return montoAutorizadoDAO.findByTemaAutorizacionAndTipoEventoNotaContableAndEstado(row);
	}

	@Override
	public Collection<MontoAutorizado> getMontosAutorizados() throws Exception {
		return montoAutorizadoDAO.findAll();
	}

	@Override
	public Collection<MontoAutorizado> searchMontoAutorizado(String palabraClave) throws Exception {
		return montoAutorizadoDAO.findBy(palabraClave);
	}

	@Override
	public MontoAutorizadoGeneral getMontoAutorizadoGeneral(MontoAutorizadoGeneral row) throws Exception {
		return montoAutorizadoGeneralDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<MontoAutorizadoGeneral> getMontosAutorizadosGeneralesPorEstado(MontoAutorizadoGeneral row) throws Exception {
		return montoAutorizadoGeneralDAO.findByEstado(row);
	}

	@Override
	public MontoAutorizadoGeneral getMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(MontoAutorizadoGeneral row, double montoNotaContable)
			throws Exception {
		return montoAutorizadoGeneralDAO.findByTemaAutorizacionAndTipoEventoNotaContableAndEstado(row, montoNotaContable);
	}

	@Override
	public int getPosicionMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(MontoAutorizadoGeneral row, double montoNotaContable) throws Exception {
		return montoAutorizadoGeneralDAO.getPositionByTemaAutorizacionAndTipoEventoNotaContableAndEstado(row, montoNotaContable);
	}

	@Override
	public Collection<MontoAutorizadoGeneral> getMontosAutorizadosGenerales() throws Exception {
		return montoAutorizadoGeneralDAO.findAll();
	}

	@Override
	public Collection<MontoAutorizadoGeneral> searchMontoAutorizadoGeneral(String palabraClave) throws Exception {
		return montoAutorizadoGeneralDAO.findBy(palabraClave);
	}

	@Override
	public FechaHabilitada getFechaHabilitada(FechaHabilitada row) throws Exception {
		return fechaHabilitadaDAO.findByPrimaryKey(row);
	}

	@Override
	public FechaHabilitada getFechaHabilitadaPorSucursal(FechaHabilitada row) throws Exception {
		return fechaHabilitadaDAO.findByCodigoSucursal(row);
	}

	@Override
	public Collection<FechaHabilitada> getFechasHabilitadas() throws Exception {
		return fechaHabilitadaDAO.findAll();
	}

	@Override
	public Collection<FechaHabilitada> searchFechaHabilitada(String palabraClave) throws Exception {
		return fechaHabilitadaDAO.findBy(palabraClave);
	}

	@Override
	public Impuesto getImpuesto(Impuesto row) throws Exception {
		return impuestoDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<Impuesto> getImpuestosPorEstado(Impuesto row) throws Exception {
		return impuestoDAO.findByEstado(row);
	}

	@Override
	public Collection<Impuesto> getImpuestos() throws Exception {
		return impuestoDAO.findAll();
	}

	@Override
	public Collection<Impuesto> searchImpuesto(String palabraClave) throws Exception {
		return impuestoDAO.findBy(palabraClave);
	}

	@Override
	public CuentaCOD getCuentaCOD(CuentaCOD row) throws Exception {
		return cuentaCODDAO.findByPrimaryKey(row);
	}

	@Override
	public CuentaCOD getCuentaCODPorCuentaContable(CuentaCOD row) throws Exception {
		return cuentaCODDAO.findByCuentaContable(row);
	}

	@Override
	public Collection<CuentaCOD> getCuentasCOD() throws Exception {
		return cuentaCODDAO.findAll();
	}

	@Override
	public Collection<CuentaCOD> searchCuentaCOD(String palabraClave) throws Exception {
		return cuentaCODDAO.findBy(palabraClave);
	}

	@Override
	public Padrino getPadrino(Padrino row) throws Exception {
		return padrinoDAO.findByPrimaryKey(row);
	}

	@Override
	public Padrino getPadrinoPorUsuario(Padrino row) throws Exception {
		return padrinoDAO.findByCodigoUsuario(row);
	}

	@Override
	public Collection<Padrino> getPadrinosPorEstado(Padrino row) throws Exception {
		return padrinoDAO.findByEstado(row);
	}

	@Override
	public Collection<Padrino> getPadrinosPorUnidadAnalisisYEstado(Padrino row) throws Exception {
		return padrinoDAO.findByCodigoUnidadAnalisisAndEstado(row);
	}

	@Override
	public Collection<Padrino> getPadrinos() throws Exception {
		return padrinoDAO.findAll();
	}

	@Override
	public Collection<Padrino> searchPadrino(String palabraClave) throws Exception {
		return padrinoDAO.findBy(palabraClave);
	}

	@Override
	public UnidadAnalisis getUnidadAnalisis(UnidadAnalisis row) throws Exception {
		return unidadAnalisisDAO.findByPrimaryKey(row);
	}

	@Override
	public UnidadAnalisis getUnidadAnalisisPorAutorizaReferenciaCruceYEstado(UnidadAnalisis row) throws Exception {
		return unidadAnalisisDAO.findByAutorizaReferenciaCruceAndEstado(row);
	}

	@Override
	public Collection<UnidadAnalisis> getUnidadesAnalisisPorEstado(UnidadAnalisis row) throws Exception {
		return unidadAnalisisDAO.findByEstado(row);
	}

	@Override
	public Collection<UnidadAnalisis> getUnidadesAnalisis() throws Exception {
		return unidadAnalisisDAO.findAll();
	}

	@Override
	public Collection<UnidadAnalisis> searchUnidadAnalisis(String palabraClave) throws Exception {
		return unidadAnalisisDAO.findBy(palabraClave);
	}

	@Override
	public CentroEspecial getCentroEspecial(CentroEspecial row) throws Exception {
		return centroEspecialDAO.findByPrimaryKey(row);
	}

	@Override
	public CentroEspecial getCentroEspecialPorSucursal(CentroEspecial row) throws Exception {
		return centroEspecialDAO.findByCodigoSucursal(row);
	}

	@Override
	public Collection<CentroEspecial> getCentrosEspecialesPorEstado(CentroEspecial row) throws Exception {
		return centroEspecialDAO.findByEstado(row);
	}

	@Override
	public Collection<CentroEspecial> getCentrosEspeciales() throws Exception {
		return centroEspecialDAO.findAll();
	}

	@Override
	public Collection<CentroEspecial> searchCentroEspecial(String palabraClave) throws Exception {
		return centroEspecialDAO.findBy(palabraClave);
	}

	@Override
	public Collection<Concepto> getConceptosPorEstado(Concepto row) throws Exception {
		return conceptoDAO.findByEstado(row);
	}

	@Override
	public Collection<Concepto> getConceptosPorEstadoParaConcepto(Concepto row) throws Exception {
		return conceptoDAO.findByEstadoParaConcepto(row);
	}

	@Override
	public Collection<Concepto> getConceptos() throws Exception {
		List<Concepto> ret = new ArrayList<Concepto>(conceptoDAO.findAll());
		Collections.sort(ret, new Comparator<Concepto>() {

			@Override
			public int compare(Concepto object1, Concepto object2) {
				return object1.getCodigo().intValue() - object2.getCodigo().intValue();
			}

		});
		return ret;
	}

	@Override
	public Collection<Concepto> searchConcepto(String palabraClave, String estado) throws Exception {
		List<Concepto> ret = new ArrayList<Concepto>(conceptoDAO.findBy(palabraClave, estado));
		Collections.sort(ret, new Comparator<Concepto>() {

			@Override
			public int compare(Concepto object1, Concepto object2) {
				return object1.getCodigo().intValue() - object2.getCodigo().intValue();
			}

		});
		return ret;
	}

	@Override
	public Collection<Concepto> searchConceptoByNombre(String palabraClave) throws Exception {
		return conceptoDAO.findByNombre(palabraClave);
	}

	@Override
	public Collection<Concepto> searchConceptoParaNotaContable(String palabraClave, String tipoArea, String codigoArea) throws Exception {
		ArrayList<Concepto> conceptosNota = new ArrayList<Concepto>();
		Collection<Concepto> conceptos = new ArrayList<Concepto>();
		CentroEspecial centroEspecial = new CentroEspecial();
		boolean indIncluir = false;

		conceptos = conceptoDAO.findByNombre(palabraClave);
		for (Concepto concepto : conceptos) {
			indIncluir = false;
			if (concepto.getCentrosAutAreasCentrales().equals("S")) {
				if (tipoArea.equals("S") || tipoArea.equals("C")) {
					indIncluir = true;
				}
			}
			if (concepto.getCentrosAutSucursales().equals("S")) {
				if (tipoArea.equals("O")) {
					indIncluir = true;
				}
			}
			if (concepto.getCentrosAutCentroEspecial().equals("S")) {
				centroEspecial = new CentroEspecial();
				centroEspecial.setCodigoSucursal(codigoArea);
				centroEspecial = getCentroEspecialPorSucursal(centroEspecial);
				if (centroEspecial.getCodigo().intValue() != 0) {
					indIncluir = true;
				}
			}
			if (indIncluir) {
				conceptosNota.add(concepto);
			}
		}

		return conceptos;
	}

	@Override
	public Collection<TemaImpuesto> getImpuestosPorTema(int codigoTema) throws Exception {
		return temaImpuestoDAO.findByTema(codigoTema);
	}

	@Override
	public boolean isImpuestoIncluido(Collection<TemaImpuesto> impuestosTema, int codigoImpuesto) {
		for (TemaImpuesto temaImpuesto : impuestosTema) {
			if (temaImpuesto.getCodigoImpuesto() == codigoImpuesto) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<TemaProducto> getProductosPorTema(int codigoTema) throws Exception {
		return temaProductoDAO.findByTema(codigoTema);
	}

	@Override
	public boolean isProductoIncluido(Collection<TemaProducto> productosTema, String codigoProducto) {
		for (TemaProducto temaProducto : productosTema) {
			if (temaProducto.getCodigoProducto().equals(codigoProducto)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSucursalIncluida(Collection<Sucursal> sucursalesCentro, String codigoSucursal) {
		for (Sucursal sucursal : sucursalesCentro) {
			if (sucursal.getCodigo().equals(codigoSucursal)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Tema getTema(Tema row) throws Exception {
		return temaDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<Tema> getTemasPorEstado(Tema row) throws Exception {
		return temaDAO.findByEstado(row);
	}

	@Override
	public Collection<Tema> getTemasPorPartidaOContraPartida(Tema row) throws Exception {
		return temaDAO.findByPartidaOrContraPartida(row);
	}

	@Override
	public Collection<Tema> getTemasPorConcepto(Tema row) throws Exception {
		return temaDAO.findByConcepto(row);
	}

	@Override
	public Collection<Tema> getTemasPorConceptoYEstado(Tema row) throws Exception {
		return temaDAO.findByConceptoAndEstado(row);
	}

	@Override
	public Collection<Tema> getTemas() throws Exception {
		return temaDAO.findAll();
	}

	@Override
	public List<Tema> getTemasCuentasInactivas() throws Exception {
		return temaDAO.findByCuentasInactivas();
	}

	@Override
	public Collection<Tema> searchTema(String palabraClave) throws Exception {
		return temaDAO.findBy(palabraClave);
	}

	@Override
	public Collection<RiesgoOperacional> getRiesgoPorTemaNotaContable(int codigoTemaNotaContable) throws Exception {
		return notaContableTemaRiesgoDAO.findByTemaNotaContable(codigoTemaNotaContable);
	}

	@Override
	public RiesgoOperacional getRiesgoPorNotaContableYTemaNotaContable(int codigoNotaContable, int codigoTemaNotaContable) throws Exception {
		return notaContableTemaRiesgoDAO.findByNotaContableAndTemaNotaContable(codigoNotaContable, codigoTemaNotaContable);
	}

	@Override
	public Collection<NotaContableTemaImpuesto> getImpuestosPorTemaNotaContable(int codigoTemaNotaContable) throws Exception {
		return notaContableTemaImpuestoDAO.findByTemaNotaContable(codigoTemaNotaContable);
	}

	@Override
	public NotaContableTemaImpuesto getImpuestoPorNotaContableYTemaNotaContableYImpuesto(int codigoNotaContable, int codigoTemaNotaContable, int codigoImpuesto) throws Exception {
		return notaContableTemaImpuestoDAO.findByNotaContableAndTemaNotaContableAndImpuesto(codigoNotaContable, codigoTemaNotaContable, codigoImpuesto);
	}

	@Override
	public Collection<NotaContableCrucePartidaPendiente> getCrucesPartidasPendientesPorNotaContable(int codigoNotaContable) throws Exception {
		return notaContableCrucePartidaPendienteDAO.findByNotaContable(codigoNotaContable);
	}

	@Override
	public boolean searchPartida(ArrayList<PartidaPendiente> partidasSeleccionadas, PartidaPendiente partidaPendiente) {
		PartidaPendiente partidaPendienteAux = new PartidaPendiente();
		int count = 0;
		boolean indExiste = false;

		while (count < partidasSeleccionadas.size() && !indExiste) {
			partidaPendienteAux = partidasSeleccionadas.get(count);
			if (partidaPendienteAux.getCuenta().equals(partidaPendiente.getCuenta())) {
				if (partidaPendienteAux.getSucursalDestino().equals(partidaPendiente.getSucursalDestino())) {
					if (partidaPendienteAux.getDivisa().equals(partidaPendiente.getDivisa())) {
						if (partidaPendienteAux.getConcepto().equals(partidaPendiente.getConcepto())) {
							if (partidaPendienteAux.getReferenciaCruce().equals(partidaPendiente.getReferenciaCruce())) {
								if (partidaPendienteAux.getNaturaleza().equals(partidaPendiente.getNaturaleza())) {
									if (partidaPendienteAux.getDescripcion().equals(partidaPendiente.getDescripcion())) {
										if (partidaPendienteAux.getImporte() == partidaPendiente.getImporte()) {
											indExiste = true;
										}
									}
								}
							}
						}
					}
				}
			}
			count++;
		}

		return indExiste;
	}

	@Override
	public Collection<NotaContableTotal> getTotalesPorNotaContable(int codigoNotaContable) throws Exception {
		return notaContableTotalDAO.findByNotaContable(codigoNotaContable);
	}

	@Override
	public Collection<NotaContableTema> getTemasPorNotaContable(int codigoNotaContable) throws Exception {
		return notaContableTemaDAO.findByNotaContable(codigoNotaContable);
	}

	@Override
	public NotaContableTema getTemaNotaContablePorNotaContableYTema(int codigoNotaContable, int codigoTema) throws Exception {
		return notaContableTemaDAO.findByNotaContableAndTema(codigoNotaContable, codigoTema);
	}

	@Override
	public NotaContableTema getTemaNotaContablePorCodigo(NotaContableTema row) throws Exception {
		return notaContableTemaDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<NotaContableTema> getTemaNotaContablePorConceptoEstadoYTema(int codigoNotaContable, String estado, int codigoTema) throws Exception {
		return notaContableTemaDAO.findByConceptoAndEstadoAndTema(codigoNotaContable, estado, codigoTema);
	}

	@Override
	public Collection<NotaContableRegistroLibre> getRegistrosLibresPorNotaContable(int codigoNotaContable) throws Exception {
		return notaContableRegistroLibreDAO.findByNotaContable(codigoNotaContable);
	}

	@Override
	public NotaContableRegistroLibre getRegistroLibreNotaContablePorNotaContableYCodigo(int codigoNotaContable, int codigo) throws Exception {
		return notaContableRegistroLibreDAO.findByNotaContableAndCodigo(codigoNotaContable, codigo);
	}

	@Override
	public Collection<Anexo> getAnexosPorTema(Anexo row) throws Exception {
		return anexoDAO.findByCodigoTema(row);
	}

	// gp12833 - aseguramiento anexos
	@Override
	public Collection<Anexo> getAnexosPorTemaORadicado(Anexo row, String numeroRadicacion) throws Exception {
		return anexoDAO.findByCodigoTemaORadicado(row, numeroRadicacion);
	}

	public Collection<Anexo> findByNumeroRadicacion(String numeroRadicacion) throws Exception {
		return anexoDAO.findByNumeroRadicacion(numeroRadicacion);
	}
	// fin gp12833 - aseguramiento anexos

	@Override
	public Collection<Anexo> getAnexosPorInstancia(int codInstanncia) throws Exception {
		return anexoDAO.findByCodigoInstancia(codInstanncia);
	}

	/*
	 * CONSULTA DE NOTAS CONTABLES
	 */

	@Override
	public NotaContable getNotaContable(NotaContable row) throws Exception {
		return notaContableDAO.findByPrimaryKey(row);
	}

	@Override
	public NotaContable getNotaContablePorNumeroRadicacion(NotaContable row) throws Exception {
		return notaContableDAO.findByNumeroRadicacion(row);
	}

	@Override
	public Collection<NotaContable> getNotaContablesPorEstado(NotaContable row) throws Exception {
		return notaContableDAO.findByEstado(row);
	}

	@Override
	public Collection<NotaContable> getNotaContablesPorEstadoInstancia(String estadoInstancia) throws Exception {
		return notaContableDAO.findByEstadoInstancia(estadoInstancia);
	}

	@Override
	public Collection<Instancia> getInstanciasPor(String codigoSucursalOrigen, String codigoSucursalDestino, Integer codigoConcepto, Integer codigoTema, Integer codigoTipoEvento,
			String partidaContable, String numeroIdentificacion, Date fechaDesde, Date fechaHasta, String codigoDivisa, String codigoEstado, String descripcion,
			String codSucUsuario, int codRol) throws Exception {
		return instanciaDAO.findNormalBy(codigoSucursalOrigen, codigoSucursalDestino, codigoConcepto, codigoTema, codigoTipoEvento, partidaContable, numeroIdentificacion,
				fechaDesde, fechaHasta, codigoDivisa, codigoEstado, descripcion, codSucUsuario, codRol);
	}

	@Override
	public Collection<Instancia> getInstanciasRegLibrePor(String sucOrigen, String sucDestino, String partida, String numIdentificacion, Date desde, Date hasta, String divisa,
			String estado, String descripcion, String codSucUsuario, int codRol) throws Exception {
		return instanciaDAO.findRegLibreBy(sucOrigen, sucDestino, partida, numIdentificacion, desde, hasta, divisa, estado, descripcion, codSucUsuario, codRol);
	}

	@Override
	public Collection<Instancia> getInstanciasCruceRefPor(String sucOrigen, String sucDestino, String partida, Date desde, Date hasta, String divisa, String estado,
			String codSucUsuario, int codRol) throws Exception {
		return instanciaDAO.findCruceRefBy(sucOrigen, sucDestino, partida, desde, hasta, divisa, estado, codSucUsuario, codRol);
	}

	@Override
	public Collection<NotaContable> getNotaContablesPrecierreCierre(boolean esPrecierre) throws Exception {
		return notaContableDAO.findByPrecierreCierre(esPrecierre);
	}

	@Override
	public Collection<NotaContable> getNotaContables() throws Exception {
		return notaContableDAO.findAll();
	}

	@Override
	public Collection<Instancia> getInstanciasPorNotaContable(NotaContable notaContable, String codSucUsuario, int codRol) throws Exception {
		return instanciaDAO.findByFecha(notaContable.getFechaRegistroAltamiraTs(), codSucUsuario, codRol);
	}

	@Override
	public Collection<Instancia> getInstanciasPorNumeroRadicacion(NotaContable notaContable, String codSucUsuario, int codRol) throws Exception {
		return instanciaDAO.findByNumeroRadicacion(notaContable.getNumeroRadicacion(), codSucUsuario, codRol);
	}

	@Override
	public Collection<Instancia> getInstanciasPorAsientoContableAndFecha(NotaContable notaContable, String codSucUsuario, int codRol) throws Exception {
		return instanciaDAO.findByAsientoContableAndFecha(notaContable.getAsientoContable(), notaContable.getFechaRegistroAltamiraTs(), codSucUsuario, codRol);
	}

	@Override
	public Collection<Instancia> getInstanciasPorEstado(Instancia row) throws Exception {
		return instanciaDAO.findByEstado(row);
	}

	@Override
	public Collection<Instancia> getInstanciasPorSucursalOrigen(Instancia row) throws Exception {
		return instanciaDAO.findBySucursalOrigen(row);
	}

	@Override
	public Collection<Instancia> getInstanciasPorSucursalOrigenYFechas(Instancia row, Date fechaDesde, Date fechaHasta) throws Exception {
		return instanciaDAO.findByCodigoSucursalAndFechas(row.getCodigoSucursalOrigen(), fechaDesde, fechaHasta);
	}

	@Override
	public Collection<Instancia> getInstanciasPorEstadoYSucursalOrigen(Instancia row) throws Exception {
		return instanciaDAO.findByEstadoAndSucursalOrigen(row);
	}

	@Override
	public Collection<Instancia> getInstanciasPorUsuario(Instancia row) throws Exception {
		return instanciaDAO.findByUsuario(row);
	}

	@Override
	public Collection<Instancia> getInstanciasPorEstadoYUsuario(Instancia row) throws Exception {
		return instanciaDAO.findByEstadoAndUsuario(row);
	}

	@Override
	public Collection<Instancia> getInstanciasPorEstadoYSucursalOrigenYUsuario(Instancia row) throws Exception {
		return instanciaDAO.findByEstadoAndSucursalOrigenAndUsuario(row);
	}

	@Override
	public Collection<Instancia> getInstancias() throws Exception {
		return instanciaDAO.findAll();
	}

	@Override
	public ActividadRealizada getActividadRealizada(ActividadRealizada row) throws Exception {
		return actividadRealizadaDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<ActividadRealizada> getActividadesRealizadasPorInstancia(ActividadRealizada row) throws Exception {
		return actividadRealizadaDAO.findByCodigoInstancia(row);
	}

	@Override
	public Observacion getObservacion(Observacion row) throws Exception {
		return observacionDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<Observacion> getObservacionesPorTema(Observacion row) throws Exception {
		return observacionDAO.findByCodigoTema(row);
	}

	// Funciones del flujo
	@SuppressWarnings("unchecked")
	@Override
	public int getDiasHabilesDesdeUltimoCierre() throws NamingException, CreateException, Exception, RemoteException {
		CargaAltamiraSessionBean cargaAltamiraManager = new CargaAltamiraSessionBean();
		Collection<CierreMensual> cierresMensuales = cargaAltamiraManager.getCierresMensuales();
		Iterator<CierreMensual> itCierresMensuales;
		CierreMensual cierreMensual = new CierreMensual();
		ArrayList<java.util.Date> diasNoHabiles = new ArrayList(cargaAltamiraManager.getFestivosFecha());
		int diaSemana = 0;
		int dias = 0;

		if (cierresMensuales.size() != 0) {
			itCierresMensuales = cierresMensuales.iterator();
			while (itCierresMensuales.hasNext()) {
				cierreMensual = itCierresMensuales.next();
				break;
			}
		}

		dias = DateUtils.getDaysBetweenLastDayOfMonthAndToday(cierreMensual.getMes(), cierreMensual.getYear(), diasNoHabiles);
		Calendar cal = Calendar.getInstance();
		diaSemana = DateUtils.getDayOfWeek(cal.getTime());
		if (diaSemana != 1 && diaSemana != 7) {
			java.util.Date date = DateUtils.getDate(cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR), "d-M-yyyy");
			if (!diasNoHabiles.contains(date)) {
				dias++;
			}
		}

		return dias;
	}

	@Override
	public Collection<UsuarioInstancias> getInstanciasPorEstadoYSucursalOrigenYRol(String estado, String codigoSucursal, int codigoRol) throws Exception {
		ArrayList<UsuarioInstancias> instanciasUsuarioRol = new ArrayList<UsuarioInstancias>();
		Collection<Instancia> instanciasUsuario = new ArrayList<Instancia>();
		UsuarioInstancias usuarioInstancias = new UsuarioInstancias();

		Collection<UsuarioModulo> usuariosRol = getUsuariosModuloPorAreaYRolYEstado(codigoSucursal, codigoRol, "A");
		for (UsuarioModulo usuarioModulo : usuariosRol) {
			// se consultan las instancias asignadas (de una forma poco optima, segun veo)
			Instancia instancia = new Instancia();
			instancia.setCodigoUsuarioActual(usuarioModulo.getCodigo().intValue());
			instancia.setEstado(estado);
			instanciasUsuario = getInstanciasPorEstadoYUsuario(instancia);

			// se consulta el rol (sera mejor por consulta directa, pero en fin.... si a roma fueres)
			Rol r = new Rol();
			r.setCodigo(usuarioModulo.getCodigoRol());
			r = getRol(r);
			usuarioModulo.setRol(r);

			usuarioInstancias = new UsuarioInstancias();
			usuarioInstancias.setUsuarioModulo(usuarioModulo);
			usuarioInstancias.setInstanciasAsignadas(instanciasUsuario);

			instanciasUsuarioRol.add(usuarioInstancias);
		}

		return instanciasUsuarioRol;
	}

	@Override
	public UsuarioModulo getUsuarioAsignadoPorBalanceo(String estado, String codigoSucursal, int codigoRol) throws Exception {
		Collection<UsuarioInstancias> instanciasEstadoRol = new ArrayList<UsuarioInstancias>();
		Iterator<UsuarioInstancias> itInstanciasEstadoRol;
		UsuarioInstancias usuarioInstancias = new UsuarioInstancias();
		UsuarioModulo usuarioModuloAsignado = new UsuarioModulo();
		int instanciasAsignadas = 32767;

		instanciasEstadoRol = getInstanciasPorEstadoYSucursalOrigenYRol(estado, codigoSucursal, codigoRol);
		if (instanciasEstadoRol.size() != 0) {
			itInstanciasEstadoRol = instanciasEstadoRol.iterator();
			while (itInstanciasEstadoRol.hasNext()) {
				usuarioInstancias = itInstanciasEstadoRol.next();

				if (usuarioInstancias.getUsuarioModulo().getEstado().equals("A")) {
					if (usuarioInstancias.getInstanciasAsignadas().size() <= instanciasAsignadas) {
						instanciasAsignadas = usuarioInstancias.getInstanciasAsignadas().size();
						usuarioModuloAsignado = usuarioInstancias.getUsuarioModulo();
					}
				}
			}
		}

		return usuarioModuloAsignado;
	}

	@Override
	public Concepto getConcepto(Concepto row) throws Exception {
		return conceptoDAO.findByPrimaryKey(row);
	}

	@Override
	public Instancia getInstancia(Instancia row) throws Exception {
		return instanciaDAO.findByPrimaryKey(row);
	}

	@Override
	public Instancia getInstanciaPorNotaContable(Instancia row) throws Exception {
		return instanciaDAO.findByNotaContable(row);
	}

	@Override
	public Collection<SubMenuRol> findSubMenuRolByRol(Rol rol) throws Exception {
		return subMenuRolDAO.findByRol(rol.getCodigo().intValue());
	}

	@Override
	public Collection<String> getInfoGeneralArchAltamira(Integer notaContableCodigo) throws Exception {
		List<String> ret = new ArrayList<String>();
		List<String> partida = new ArrayList<String>(notaContableTemaDAO.getInfoPartidaArchAltamira(notaContableCodigo));
		List<String> contraPartida = new ArrayList<String>(notaContableTemaDAO.getInfoContraPartidaArchAltamira(notaContableCodigo));
		for (int i = 0; i < partida.size(); i++) {
			ret.add(partida.get(i));
			ret.add(contraPartida.get(i));
		}
		return ret;
	}

	@Override
	public Collection<String> getInfoCruceArchAltamira(Integer notaContableCodigo) throws Exception {
		return notaContableCrucePartidaPendienteDAO.getInfoPartidasArchAltamira(notaContableCodigo);
	}

	@Override
	public Collection<String> getInfoLibreArchAltamira(Integer notaContableCodigo) throws Exception {
		return notaContableRegistroLibreDAO.getInfoPartidasArchAltamira(notaContableCodigo);
	}

	@Override
	public Collection<RechazoSalida> getRechazoSalidaByNotaContable(String numRadicacion) throws Exception {
		return rechazoSalidaDAO.findByNotaContable(numRadicacion);
	}

	@Override
	public Collection<Parametro> getParametros() throws Exception {
		return parametroDAO.findAll();
	}

	@Override
	public Collection<Instancia> getInstanciasPendientes() throws Exception {
		return instanciaDAO.findPendientes();
	}

	@Override
	public Collection<ActividadRealizada> getActividadesParaReporteTiempos(Integer codUsuario) throws Exception {
		return actividadRealizadaDAO.findParaReporteTiempos(codUsuario);
	}
	
	
}
