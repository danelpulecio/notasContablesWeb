package co.com.bbva.app.notas.contables.facade.impl;

import co.com.bbva.app.notas.contables.carga.dao.*;
import co.com.bbva.app.notas.contables.carga.dto.*;
import co.com.bbva.app.notas.contables.dao.*;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.facade.CargaAltamiraSession;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.EMailSender;
import co.com.bbva.app.notas.contables.util.IRol;
import co.com.bbva.app.notas.contables.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.*;

public class CargaAltamiraSessionBean implements CargaAltamiraSession {

	static final long serialVersionUID = 3206093459760846163L;

	private static final String bodyTema = "Los siguientes temas han sido inactivados debido a que estn asociados a cuentas contables no activas en el ltimo cargue:";
	private static final String enter = "\r\n";
	protected final EMailSender enviarEMail = new EMailSender();
	private final RegistroCargaDAO registroCargaDAO = new RegistroCargaDAO();
	private final AuditoriaCargaDAO auditoriaCargaDAO = new AuditoriaCargaDAO();
	private final FestivoDAO festivoDAO = new FestivoDAO();
	private final FechaHabilitadaDAO fechaHabilitadaDAO = new FechaHabilitadaDAO();
	private final ClaseRiesgoDAO claseRiesgoDAO = new ClaseRiesgoDAO();
	private final ClienteDAO clienteDAO = new ClienteDAO();
	private final ContratoDAO contratoDAO = new ContratoDAO();
	private final PerdidaOperacionalClaseRiesgoDAO perdidaOperacionalClaseRiesgoDAO = new PerdidaOperacionalClaseRiesgoDAO();
	private final PartidaPendienteDAO partidaPendienteDAO = new PartidaPendienteDAO();
	private final PerdidaOperacionalDAO perdidaOperacionalDAO = new PerdidaOperacionalDAO();
	private final UsuarioAltamiraDAO usuarioAltamiraDAO = new UsuarioAltamiraDAO();
	private final HADTAPLDAO hadtaplDAO = new HADTAPLDAO();
	private final ProductoDAO productoDAO = new ProductoDAO();
	private final SucursalDAO sucursalDAO = new SucursalDAO();
	private final PUCDAO pucDAO = new PUCDAO();
	private final DivisaDAO divisaDAO = new DivisaDAO();
	private final TemaDAO temaDAO = new TemaDAO();
	private final UsuarioModuloDAO usuarioDAO = new UsuarioModuloDAO();
	private final TerceroDAO terceroDAO = new TerceroDAO();
	private final TipoIdentificacionDAO tipoIdentificacionDAO = new TipoIdentificacionDAO();
	private final TipoTelefonoDAO tipoTelefonoDAO = new TipoTelefonoDAO();
	private final TipoIndicativoDAO tipoIndicativoDAO = new TipoIndicativoDAO();
	private final PaisDAO paisDAO = new PaisDAO();
	private final RiesgoOperacionalProductoDAO riesgoOperacionalProductoDAO = new RiesgoOperacionalProductoDAO();
	private final RiesgoOperacionalProcesoDAO riesgoOperacionalProcesoDAO = new RiesgoOperacionalProcesoDAO();
	private final RiesgoOperacionalLineaOperativaDAO riesgoOperacionalLineaOperativaDAO = new RiesgoOperacionalLineaOperativaDAO();
	private final MunicipioDAO municipioDAO = new MunicipioDAO();
	private final ErrorValidacionDAO errorValidacionDAO = new ErrorValidacionDAO();
	private final DepartamentoDAO departamentoDAO = new DepartamentoDAO();
	private final ActividadEconomicaDAO actividadEconomicaDAO = new ActividadEconomicaDAO();
	private final PerfilDAO perfilDAO = new PerfilDAO();
	private final CierreMensualDAO cierreMensualDAO = new CierreMensualDAO();
	private final JerarquiaOperacionSFC_N2DAO operacionSFC_N2DAO = new JerarquiaOperacionSFC_N2DAO();
	private final JerarquiaOperacionSFC_N3DAO operacionSFC_N3DAO = new JerarquiaOperacionSFC_N3DAO();

	private static final Logger LOGGER = LoggerFactory.getLogger(CargaAltamiraSessionBean.class);

	@SuppressWarnings("unchecked")
	@Override
	public void beginLoad(String nombreTabla, boolean deleteAll) throws Exception {
		SuperDAO dao = new SuperDAO(null);
		if (nombreTabla.equals("NC_SUCURSAL")) {
			try {
				dao.deleteAll("NC_FECHA_HABILITADA");
			} catch (Exception e) {
				LOGGER.error(" beginLoad " ,  e );
			}
		}
		if (deleteAll) {
			dao.deleteAll(nombreTabla);
		} 
		/**
		else {
			try {
				dao.updateEstadoCarga(nombreTabla, "I");
			} catch (Exception e) {
				// se asume que la tabla no tiene este campo
			}
		}
		**/
	}

	// RegistroCarga
	/*
	 * Nombre DTO: RegistroCarga Nombre DAO: registroCargaDAO Plural: RegistrosCarga
	 */
	@Override
	public void addRegistroCarga(RegistroCarga row) throws Exception {
		registroCargaDAO.add(row, -1);
	}

	@Override
	public void updateRegistroCarga(RegistroCarga row) throws Exception {
		registroCargaDAO.update(row, -1);
	}

	@Override
	public void deleteRegistroCarga(RegistroCarga row) throws Exception {
		registroCargaDAO.delete(row, -1);
	}

	@Override
	public RegistroCarga getRegistroCarga(RegistroCarga row) throws Exception {
		return registroCargaDAO.findByPrimaryKey(row);
	}

	@Override
	public RegistroCarga getRegistroCargaPorNombreArchivo(String nombreArchivo) throws Exception {
		return registroCargaDAO.getByNombreArchivo(nombreArchivo);
	}

	@Override
	public Collection<RegistroCarga> getRegistrosCarga() throws Exception {
		return registroCargaDAO.findAll();
	}

	// AuditoriaCarga
	/*
	 * Nombre DTO: AuditoriaCarga Nombre DAO: auditoriaCargaDAO Plural: AuditoriasCarga
	 */
	private void addAuditoriaCarga(AuditoriaCarga row) throws Exception {
		auditoriaCargaDAO.add(row, -1);
	}

	@Override
	public void updateAuditoriaCarga(AuditoriaCarga row) throws Exception {
		auditoriaCargaDAO.update(row, -1);
	}

	@Override
	public void deleteAuditoriaCarga(AuditoriaCarga row) throws Exception {
		auditoriaCargaDAO.delete(row, -1);
	}

	@Override
	public AuditoriaCarga getAuditoriaCarga(AuditoriaCarga row) throws Exception {
		return auditoriaCargaDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<AuditoriaCarga> getAuditoriasCarga() throws Exception {
		return auditoriaCargaDAO.findAll();
	}

	// ClaseRiesgo
	/*
	 * Nombre DTO: ClaseRiesgo Nombre DAO: claseRiesgoDAO Plural: ClasesRiesgo
	 */
	@Override
	public void addClaseRiesgo(ClaseRiesgo row) throws Exception {
		claseRiesgoDAO.add(row, -1);
	}

	@Override
	public void updateClaseRiesgo(ClaseRiesgo row) throws Exception {
		claseRiesgoDAO.update(row, -1);
	}

	@Override
	public void deleteClaseRiesgo(ClaseRiesgo row) throws Exception {
		claseRiesgoDAO.delete(row, -1);
	}

	@Override
	public ClaseRiesgo getClaseRiesgo(ClaseRiesgo row) throws Exception {
		return claseRiesgoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveClaseRiesgo(ClaseRiesgo row) throws Exception {
		try {
			addClaseRiesgo(row);
		} catch (Exception le_e) {
			updateClaseRiesgo(row);
			LOGGER.error(" error saveClaseRiesgo " ,  le_e );
		}
	}

	@Override
	public void beginLoadClaseRiesgo() throws Exception {
		claseRiesgoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<ClaseRiesgo> getClasesRiesgo() throws Exception {
		return claseRiesgoDAO.findAll();
	}

	@Override
	public Collection<ClaseRiesgo> searchClaseRiesgo(String palabraClave) throws Exception {
		return claseRiesgoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadClaseRiesgo(String nombreArchivo) throws Exception {
		ClaseRiesgo row = new ClaseRiesgo();
		Collection<ClaseRiesgo> rows = new ArrayList<ClaseRiesgo>();
		AuditoriaCarga auditoriaCarga;

		rows = claseRiesgoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<ClaseRiesgo> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// Cliente
	/*
	 * Nombre DTO: Cliente Nombre DAO: clienteDAO Plural: Clientes
	 */
	@Override
	public void addCliente(Cliente row) throws Exception {
		clienteDAO.add(row, -1);
	}

	@Override
	public void updateCliente(Cliente row) throws Exception {
		clienteDAO.update(row, -1);
	}

	@Override
	public void deleteCliente(Cliente row) throws Exception {
		clienteDAO.delete(row, -1);
	}

	@Override
	public Cliente getCliente(Cliente row) throws Exception {
		return clienteDAO.findByPrimaryKey(row);
	}

	@Override
	public Cliente getClientePorTipoYNumeroIdentificacion(Cliente row) throws Exception {
		return clienteDAO.findByTipoAndNumeroIdentificacion(row);
	}

	@Override
	public void saveCliente(Cliente row) throws Exception {
		try {
			addCliente(row);
		} catch (Exception le_e) {
			updateCliente(row);
			LOGGER.error(" error saveCliente " ,  le_e );
		}
	}

	@Override
	public void beginLoadCliente() throws Exception {
		clienteDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Cliente> getClientes() throws Exception {
		return clienteDAO.findAll();
	}

	@Override
	public Collection<Cliente> searchCliente(String palabraClave) throws Exception {
		return clienteDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadCliente(String nombreArchivo) throws Exception {
		/**BLOQUEO BASE DE DATOS 
		Cliente row = new Cliente();
		Collection<Cliente> rows = new ArrayList<Cliente>();
		AuditoriaCarga auditoriaCarga;

		rows = clienteDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Cliente> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getTipoIdentificacion() + " - " + row.getNumeroIdentificacion());
				auditoriaCarga.setDescripcionRegistro(row.getPrimerApellido() + " " + row.getSegundoApellido() + " " + row.getPrimerNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
		**/
	}

	// Contrato
	/*
	 * Nombre DTO: Contrato Nombre DAO: contratoDAO Plural: Contratos
	 */
	@Override
	public void addContrato(Contrato row) throws Exception {
		contratoDAO.add(row, -1);
	}

	@Override
	public void deleteContrato(Contrato row) throws Exception {
		contratoDAO.delete(row, -1);
	}

	@Override
	public Contrato getContrato(Contrato row) throws Exception {
		return contratoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveContrato(Contrato row) throws Exception {
		try {
			addContrato(row);
		} catch (Exception le_e) {
			LOGGER.error(" error saveContrato " ,  le_e );

		}
	}

	@Override
	public void beginLoadContrato() throws Exception {
		contratoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Contrato> getContratos() throws Exception {
		return contratoDAO.findAll();
	}

	@Override
	public Collection<Contrato> getContratosPorCliente(Contrato row) throws Exception {
		return contratoDAO.findByNumeroCliente(row);
	}

	@Override
	public Collection<Contrato> searchContrato(String palabraClave) throws Exception {
		return contratoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadContrato(String nombreArchivo) throws Exception {
		/**BLOQUEO BASE DE DATOS **/
		/**
		Contrato row = new Contrato();
		Collection<Contrato> rows = new ArrayList<Contrato>();
		AuditoriaCarga auditoriaCarga;

		rows = contratoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Contrato> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getNumeroCliente() + " - " + row.getNumeroContrato());
				auditoriaCarga.setDescripcionRegistro(row.getNumeroCliente() + " - " + row.getNumeroContrato());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
		**/
	}

	// PerdidaOperacionalClaseRiesgo
	/*
	 * Nombre DTO: PerdidaOperacionalClaseRiesgo Nombre DAO: perdidaOperacionalClaseRiesgoDAO Plural: PerdidaOperacionalClaseRiesgo
	 */
	@Override
	public void addPerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception {
		perdidaOperacionalClaseRiesgoDAO.add(row, -1);
	}

	@Override
	public void updatePerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception {
		perdidaOperacionalClaseRiesgoDAO.update(row, -1);
	}

	@Override
	public void deletePerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception {
		perdidaOperacionalClaseRiesgoDAO.delete(row, -1);
	}

	@Override
	public PerdidaOperacionalClaseRiesgo getPerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception {
		return perdidaOperacionalClaseRiesgoDAO.findByPrimaryKey(row);
	}

	@Override
	public void savePerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception {
		try {
			addPerdidaOperacionalClaseRiesgo(row);
		} catch (Exception le_e) {
			LOGGER.error(" Error savePerdidaOperacionalClaseRiesgo" ,le_e );
		}
	}

	@Override
	public void beginLoadPerdidaOperacionalClaseRiesgo() throws Exception {
		perdidaOperacionalClaseRiesgoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<PerdidaOperacionalClaseRiesgo> getPerdidaOperacionalClaseRiesgo() throws Exception {
		return perdidaOperacionalClaseRiesgoDAO.findAll();
	}

	@Override
	public Collection<PerdidaOperacionalClaseRiesgo> searchPerdidaOperacionalClaseRiesgo(String palabraClave) throws Exception {
		return perdidaOperacionalClaseRiesgoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadPerdidaOperacionalClaseRiesgo(String nombreArchivo) throws Exception {
		PerdidaOperacionalClaseRiesgo row = new PerdidaOperacionalClaseRiesgo();
		Collection<PerdidaOperacionalClaseRiesgo> rows = new ArrayList<PerdidaOperacionalClaseRiesgo>();
		AuditoriaCarga auditoriaCarga;

		rows = perdidaOperacionalClaseRiesgoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<PerdidaOperacionalClaseRiesgo> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCuenta() + " - " + row.getCodigoClaseRiesgo() + " - " + row.getCodigoTipoPerdida());
				auditoriaCarga.setDescripcionRegistro("");
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// PartidaPendiente
	/*
	 * Nombre DTO: PartidaPendiente Nombre DAO: partidaPendienteDAO Plural: PartidasPendientes
	 */
	@Override
	public void addPartidaPendiente(PartidaPendiente row) throws Exception {
		partidaPendienteDAO.add(row, -1);
	}

	@Override
	public void updatePartidaPendiente(PartidaPendiente row) throws Exception {
		partidaPendienteDAO.update(row, -1);
	}

	@Override
	public void deletePartidaPendiente(PartidaPendiente row) throws Exception {
		partidaPendienteDAO.delete(row, -1);
	}

	@Override
	public PartidaPendiente getPartidaPendiente(PartidaPendiente row) throws Exception {
		return partidaPendienteDAO.findByPrimaryKey(row);
	}

	@Override
	public void savePartidaPendiente(PartidaPendiente row) throws Exception {
		try {
			addPartidaPendiente(row);
		} catch (Exception le_e) {
			updatePartidaPendiente(row);
			LOGGER.error(" Error savePartidaPendiente" ,le_e );

		}
	}

	@Override
	public void beginLoadPartidaPendiente() throws Exception {
		partidaPendienteDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<PartidaPendiente> getPartidasPendientes() throws Exception {
		return partidaPendienteDAO.findAll();
	}

	@Override
	public Collection<PartidaPendiente> getPartidasPendientesPorSucursal(String codigoSucursal) throws Exception {
		return partidaPendienteDAO.findBySucursal(codigoSucursal);
	}

	@Override
	public Collection<PartidaPendiente> getPartidasPendientesCuentasPorSucursal(String codigoSucursal) throws Exception {
		return partidaPendienteDAO.findCuentasBySucursal(codigoSucursal);
	}

	@Override
	public Collection<PartidaPendiente> searchPartidaPendiente(String palabraClave) throws Exception {
		return partidaPendienteDAO.findBy(palabraClave);
	}

	@Override
	public Collection<PartidaPendiente> searchPartidaPendientePorSucursal(String codigoSucursal, String palabraClave) throws Exception {
		return partidaPendienteDAO.findByAllAndSucursal(codigoSucursal, palabraClave);
	}

	// este metodo no debe hacer nada debido a que la tabla se limpia antes de realizar la carga de datos
	@Override
	public void endLoadPartidaPendiente(String nombreArchivo) throws Exception {
		// PartidaPendiente row = new PartidaPendiente();
		// Collection rows = (Collection) new ArrayList();
		// AuditoriaCarga auditoriaCarga;
		//
		// rows = partidaPendienteDAO.findByEstadoCarga("I");
		// if (rows.size() != 0) {
		// Iterator itRows = rows.iterator();
		//
		// while (itRows.hasNext()) {
		// row = (PartidaPendiente) itRows.next();
		//
		// auditoriaCarga = new AuditoriaCarga();
		// auditoriaCarga.setCodigoRegistro(String.valueOf(row.getCuenta()));
		// auditoriaCarga.setDescripcionRegistro(row.getDescripcion());
		// auditoriaCarga.setNombreArchivo(nombreArchivo);
		// auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
		// auditoriaCarga.setFecha(DateUtils.getTimestamp());
		// auditoriaCarga.setEstado(row.getEstadoCarga());
		// addAuditoriaCarga(auditoriaCarga);
		// }
		// }
	}

	// PerdidaOperacional
	/*
	 * Nombre DTO: PerdidaOperacional Nombre DAO: perdidaOperacionalDAO Plural: PerdidasOperacionales
	 */
	@Override
	public void addPerdidaOperacional(PerdidaOperacional row) throws Exception {
		perdidaOperacionalDAO.add(row, -1);
	}

	@Override
	public void updatePerdidaOperacional(PerdidaOperacional row) throws Exception {
		perdidaOperacionalDAO.update(row, -1);
	}

	@Override
	public void deletePerdidaOperacional(PerdidaOperacional row) throws Exception {
		perdidaOperacionalDAO.delete(row, -1);
	}

	@Override
	public PerdidaOperacional getPerdidaOperacional(PerdidaOperacional row) throws Exception {
		return perdidaOperacionalDAO.findByPrimaryKey(row);
	}

	@Override
	public void savePerdidaOperacional(PerdidaOperacional row) throws Exception {
		try {
			addPerdidaOperacional(row);
		} catch (Exception le_e) {
			updatePerdidaOperacional(row);
			LOGGER.error(" Error savePerdidaOperacional" ,le_e );
		}
	}

	@Override
	public void beginLoadPerdidaOperacional() throws Exception {
		perdidaOperacionalDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<PerdidaOperacional> getPerdidasOperacionales() throws Exception {
		return perdidaOperacionalDAO.findAll();
	}

	@Override
	public Collection<PerdidaOperacional> searchPerdidaOperacional(String palabraClave) throws Exception {
		return perdidaOperacionalDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadPerdidaOperacional(String nombreArchivo) throws Exception {
		PerdidaOperacional row = new PerdidaOperacional();
		Collection<PerdidaOperacional> rows = new ArrayList<PerdidaOperacional>();
		AuditoriaCarga auditoriaCarga;

		rows = perdidaOperacionalDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<PerdidaOperacional> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getDescripcion());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// UsuarioAltamira
	/*
	 * Nombre DTO: UsuarioAltamira Nombre DAO: usuarioAltamiraDAO Plural: UsuariosAltamira
	 */
	@Override
	public void addUsuarioAltamira(UsuarioAltamira row) throws Exception {
		usuarioAltamiraDAO.add(row, -1);
	}

	@Override
	public void updateUsuarioAltamira(UsuarioAltamira row) throws Exception {
		usuarioAltamiraDAO.update(row, -1);
	}

	@Override
	public void deleteUsuarioAltamira(UsuarioAltamira row) throws Exception {
		usuarioAltamiraDAO.delete(row, -1);
	}

	@Override
	public UsuarioAltamira getUsuarioAltamira(UsuarioAltamira row) throws Exception {
		return usuarioAltamiraDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveUsuarioAltamira(UsuarioAltamira row) throws Exception {
		try {
			addUsuarioAltamira(row);
		} catch (Exception le_e) {
			updateUsuarioAltamira(row);
			LOGGER.error(" Error saveUsuarioAltamira" ,le_e );
		}
	}

	@Override
	public void beginLoadUsuarioAltamira() throws Exception {
		usuarioAltamiraDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<UsuarioAltamira> getUsuariosAltamira() throws Exception {
		return usuarioAltamiraDAO.findAll();
	}

	@Override
	public Collection<UsuarioAltamira> searchUsuarioAltamira(String palabraClave) throws Exception {
		return usuarioAltamiraDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadUsuarioAltamira(String nombreArchivo) throws Exception {
		UsuarioAltamira row = new UsuarioAltamira();
		Collection<UsuarioAltamira> rows = new ArrayList<UsuarioAltamira>();
		AuditoriaCarga auditoriaCarga;

		rows = usuarioAltamiraDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<UsuarioAltamira> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigoEmpleado());
				auditoriaCarga.setDescripcionRegistro(row.getNombreEmpleado());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// HADTAPL
	/*
	 * Nombre DTO: HADTAPL Nombre DAO: hadtaplDAO Plural: HADTAPL
	 */
	@Override
	public void addHADTAPL(HADTAPL row) throws Exception {
		hadtaplDAO.add(row, -1);
	}

	@Override
	public void updateHADTAPL(HADTAPL row) throws Exception {
		hadtaplDAO.update(row, -1);
	}

	@Override
	public void deleteHADTAPL(HADTAPL row) throws Exception {
		hadtaplDAO.delete(row, -1);
	}

	@Override
	public HADTAPL getHADTAPL(HADTAPL row) throws Exception {
		return hadtaplDAO.findByPrimaryKey(row);
	}

	@Override
	public HADTAPL getHADTAPLPorCuenta(HADTAPL row) throws Exception {
		return hadtaplDAO.getByCuenta(row);
	}

	@Override
	public void saveHADTAPL(HADTAPL row) throws Exception {
		try {
			addHADTAPL(row);
		} catch (Exception le_e) {
			updateHADTAPL(row);
			LOGGER.error(" Error saveHADTAPL" ,le_e );
		}
	}

	@Override
	public void beginLoadHADTAPL() throws Exception {
		hadtaplDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<HADTAPL> getHADTAPL() throws Exception {
		return hadtaplDAO.findAll();
	}

	@Override
	public Collection<HADTAPL> searchHADTAPL(String palabraClave) throws Exception {
		return hadtaplDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadHADTAPL(String nombreArchivo) throws Exception {
		HADTAPL row = new HADTAPL();
		Collection<HADTAPL> rows = new ArrayList<HADTAPL>();
		AuditoriaCarga auditoriaCarga;

		rows = hadtaplDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<HADTAPL> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCuentaContable());
				auditoriaCarga.setDescripcionRegistro(row.getIndicadorContrato());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// Producto
	/*
	 * Nombre DTO: Producto Nombre DAO: productoDAO Plural: Productos
	 */
	@Override
	public void addProducto(Producto row) throws Exception {
		productoDAO.add(row, -1);
	}

	@Override
	public void updateProducto(Producto row) throws Exception {
		productoDAO.update(row, -1);
	}

	@Override
	public void deleteProducto(Producto row) throws Exception {
		productoDAO.delete(row, -1);
	}

	@Override
	public Producto getProducto(Producto row) throws Exception {
		return productoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveProducto(Producto row) throws Exception {
		try {
			addProducto(row);
		} catch (Exception le_e) {
			updateProducto(row);
			LOGGER.error(" Error saveProducto" ,le_e );
		}
	}

	@Override
	public void beginLoadProducto() throws Exception {
		productoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Producto> getProductos() throws Exception {
		return productoDAO.findAll();
	}

	@Override
	public Collection<Producto> searchProducto(String palabraClave) throws Exception {
		return productoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadProducto(String nombreArchivo) throws Exception {
		Producto row = new Producto();
		Collection<Producto> rows = new ArrayList<Producto>();
		AuditoriaCarga auditoriaCarga;

		rows = productoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Producto> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// Sucursal
	/*
	 * Nombre DTO: Sucursal Nombre DAO: sucursalDAO Plural: Sucursales
	 */
	@Override
	public void addSucursal(Sucursal row) throws Exception {
		sucursalDAO.add(row, -1);
	}

	@Override
	public void updateSucursal(Sucursal row) throws Exception {
		sucursalDAO.update(row, -1);
	}

	@Override
	public void deleteSucursal(Sucursal row) throws Exception {
		sucursalDAO.delete(row, -1);
	}

	@Override
	public Sucursal getSucursal(Sucursal row) throws Exception {
		return sucursalDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveSucursal(Sucursal row) throws Exception {
		try {
			addSucursal(row);
		} catch (Exception le_e) {
			updateSucursal(row);
			LOGGER.error(" Error saveSucursal" ,le_e );
		}
		FechaHabilitada fecha = new FechaHabilitada();
		fecha.setCodigoSucursal(row.getCodigo());
		fecha.setDias(2);
		fechaHabilitadaDAO.add(fecha, 0);

	}

	@Override
	public void beginLoadSucursal() throws Exception {
		sucursalDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Sucursal> getSucursales() throws Exception {
		return sucursalDAO.findAll();
	}

	@Override
	public Collection<Sucursal> searchSucursal(String palabraClave) throws Exception {
		return sucursalDAO.findBy(palabraClave);
	}

	@Override
	public Collection<Sucursal> getSucursalesPorTiposCentro(String tiposCentro) throws Exception {
		return sucursalDAO.findByTipoCentro(tiposCentro);
	}

	@Override
	public Collection<Sucursal> getSucursalesPorConcepto(Concepto concepto) throws Exception, RemoteException, NamingException, CreateException {
		NotasContablesSessionBean notasContablesManager = new NotasContablesSessionBean();
		ArrayList<Sucursal> sucursales;
		Sucursal sucursal = new Sucursal();
		CentroEspecial centroEspecial = new CentroEspecial();
		Collection<CentroEspecial> centrosEspeciales = new ArrayList<CentroEspecial>();
		Iterator<CentroEspecial> itCentrosEspeciales;
		String tiposCentro = "";

		if (concepto.getCentrosAutSucursales().equals("S")) {
			tiposCentro = "'O'";
		}
		if (concepto.getCentrosAutAreasCentrales().equals("S")) {
			if (!tiposCentro.equals("")) {
				tiposCentro += ", ";
			}
			tiposCentro += "'S', 'C', 'A', 'R'";
		}

		sucursales = new ArrayList<Sucursal>(sucursalDAO.findByTipoCentro(tiposCentro));

		if (concepto.getCentrosAutCentroEspecial().equals("S")) {
			centroEspecial.setEstado("A");
			centrosEspeciales = notasContablesManager.getCentrosEspecialesPorEstado(centroEspecial);

			if (centrosEspeciales.size() != 0) {
				itCentrosEspeciales = centrosEspeciales.iterator();
				while (itCentrosEspeciales.hasNext()) {
					centroEspecial = itCentrosEspeciales.next();

					sucursal = new Sucursal();
					sucursal.setCodigo(centroEspecial.getCodigoSucursal());
					sucursal = getSucursal(sucursal);

					if (sucursales.indexOf(sucursales) == -1) {
						sucursales.add(sucursal);
					}
				}
			}
		}

		return sucursales;
	}

	@Override
	public boolean isSucursalValidaPUCOrigen(Sucursal sucursal, PUC cuenta) {
		boolean indValida = false;
		String centrosOrigen = "";

		if (cuenta.getTipoCentroOrigenAutorizado().indexOf(sucursal.getTipoCentro().charAt(0)) != -1) {
			if (!cuenta.getIndicadorCentroOrigen().equals("E")) {
				indValida = true;
			}
		}

		if (cuenta.getIndicadorCentroOrigen().equals("I")) {
			centrosOrigen = StringUtils.insertSplit(StringUtils.replace(cuenta.getCentrosOrigenAutorizados(), " ", "0"), ",", 4) + ",";
			if (centrosOrigen.indexOf(sucursal.getCodigo() + ",") != -1) {
				indValida = true;
			}
		} else if (cuenta.getIndicadorCentroOrigen().equals("E")) {
			centrosOrigen = StringUtils.insertSplit(StringUtils.replace(cuenta.getCentrosOrigenAutorizados(), " ", "0"), ",", 4) + ",";
			if (centrosOrigen.indexOf(sucursal.getCodigo() + ",") == -1) {
				indValida = true;
			}
		}

		return indValida;
	}

	@Override
	public boolean isSucursalValidaPUCDestino(Sucursal sucursal, PUC cuenta) {
		boolean indValida = false;
		String centrosDestino = "";

		if (cuenta.getTipoCentroDestinoAutorizado().indexOf(sucursal.getTipoCentro().charAt(0)) != -1) {
			if (!cuenta.getIndicadorCentroDestino().equals("E")) {
				indValida = true;
			}
		}

		if (cuenta.getIndicadorCentroDestino().equals("I")) {
			centrosDestino = StringUtils.insertSplit(StringUtils.replace(cuenta.getCentrosDestinoAutorizados(), " ", "0"), ",", 4) + ",";
			if (centrosDestino.indexOf(sucursal.getCodigo() + ",") != -1) {
				indValida = true;
			}
		} else if (cuenta.getIndicadorCentroDestino().equals("E")) {
			centrosDestino = StringUtils.insertSplit(StringUtils.replace(cuenta.getCentrosDestinoAutorizados(), " ", "0"), ",", 4) + ",";
			if (centrosDestino.indexOf(sucursal.getCodigo() + ",") == -1) {
				indValida = true;
			}
		}

		return indValida;
	}

	@Override
	public void endLoadSucursal(String nombreArchivo) throws Exception {
		Sucursal row = new Sucursal();
		Collection<Sucursal> rows = new ArrayList<Sucursal>();
		AuditoriaCarga auditoriaCarga;

		rows = sucursalDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Sucursal> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(String.valueOf(row.getCodigo()));
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	@Override
	public Collection<Sucursal> getCadenaAutorizacionSucursal(Sucursal row) throws Exception {
		ArrayList<Sucursal> rows = new ArrayList<Sucursal>();
		Sucursal sucursal = new Sucursal();
		String codigoCentroSuperior = "";

		sucursal = getSucursal(row);
		codigoCentroSuperior = sucursal.getCodigoCentroSuperior().trim();
		rows.add(sucursal);

		while (!sucursal.getCodigoCentroSuperior().trim().equals("9999")) {
			sucursal = new Sucursal();
			sucursal.setCodigo(codigoCentroSuperior);
			sucursal = getSucursal(sucursal);
			codigoCentroSuperior = sucursal.getCodigoCentroSuperior().trim();
			rows.add(sucursal);
		}
		sucursal = new Sucursal();
		sucursal.setCodigo("9999");
		sucursal = getSucursal(sucursal);
		codigoCentroSuperior = sucursal.getCodigoCentroSuperior().trim();
		rows.add(sucursal);
		return rows;
	}

	// PUC
	/*
	 * Nombre DTO: PUC Nombre DAO: pucDAO Plural: PUCs
	 */
	@Override
	public void addPUC(PUC row) throws Exception {
		pucDAO.add(row, -1);
	}

	@Override
	public void updatePUC(PUC row, int codigoUsuario) throws Exception {
		pucDAO.update(row, -1);
	}

	@Override
	public void updatePUCs(Collection<PUC> pucs, int codigoUsuario) throws Exception {
		Iterator<PUC> itPUCs;
		PUC puc = new PUC();

		if (pucs.size() != 0) {
			itPUCs = pucs.iterator();
			while (itPUCs.hasNext()) {
				puc = itPUCs.next();
				updatePUC(puc, codigoUsuario);
			}
		}
	}

	@Override
	public void deletePUC(PUC row) throws Exception {
		pucDAO.delete(row, -1);
	}

	@Override
	public PUC getPUC(PUC row) throws Exception {
		return pucDAO.findByPrimaryKey(row);
	}

	@Override
	public void savePUC(PUC row, int codigoUsuario) throws Exception {
		try {
			addPUC(row);
		} catch (Exception le_e) {
			updatePUC(row, codigoUsuario);
			LOGGER.error(" Error savePUC" ,le_e );
		}
	}

	@Override
	public void beginLoadPUC() throws Exception {
		pucDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<PUC> getPUCs() throws Exception {
		return pucDAO.findAll();
	}

	@Override
	public Collection<PUC> searchPUC(String palabraClave) throws Exception {
		return pucDAO.findBy(palabraClave);
	}

	@Override
	public Collection<PUC> findCentrosBy(String numCuenta) throws Exception {
		return pucDAO.findCentrosBy(numCuenta);
	}

	@Override
	public void endLoadPUC(String nombreArchivo) throws Exception {
		AuditoriaCarga auditoriaCarga;

		HashMap<String, Collection<Tema>> map = new HashMap<String, Collection<Tema>>();

		Collection<PUC> rows = pucDAO.findByEstadoCarga("I");
		for (PUC puc : rows) {
			// se buscan los posibles temas activos asociados a la cuenta
			Tema tema = new Tema();
			tema.setPartidaContable(puc.getNumeroCuenta());
			tema.setContraPartidaContable(puc.getNumeroCuenta());
			Collection<Tema> temas = temaDAO.findByPartidaOrContraPartida(tema);
			for (Tema t : temas) {
				temaDAO.changeEstado(t, 0);

			}
			if (!temas.isEmpty()) {
				map.put(puc.getNumeroCuenta(), temas);
			}
			// registro de auditoria
			auditoriaCarga = new AuditoriaCarga();
			auditoriaCarga.setCodigoRegistro(puc.getNumeroCuenta());
			auditoriaCarga.setDescripcionRegistro(puc.getDescripcion());
			auditoriaCarga.setNombreArchivo(nombreArchivo);
			auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
			auditoriaCarga.setFecha(DateUtils.getTimestamp());
			addAuditoriaCarga(auditoriaCarga);
		}
		// se envia el correo al administrador de consolidacion
		if (!map.isEmpty()) {
			try {
				UsuarioModulo usu = new UsuarioModulo();
				usu = usuarioDAO.findByRolAndEstado(IRol.ADMINISTRADOR_CONSOLIDACION, "A").iterator().next();

				String body = bodyTema;
				for (String cuenta : map.keySet()) {
					body += enter + "Cuenta: " + cuenta;
					for (Tema t : map.get(cuenta)) {
						body += enter + t.getNombre();
					}
				}
				enviarEMail.sendEmail(usu.getEMailModificado(), "", "Temas Inactivados", body);
			} catch (Exception e) {
				LOGGER.error(" Error al enviar el correo al usuario con el rol administrador de Consolidacion  {} " , IRol.ADMINISTRADOR_CONSOLIDACION  , e );

			}
		}
	}

	// Divisa
	/*
	 * Nombre DTO: Divisa Nombre DAO: divisaDAO Plural: Divisas
	 */
	@Override
	public void addDivisa(Divisa row) throws Exception {
		divisaDAO.add(row, -1);
	}

	@Override
	public void updateDivisa(Divisa row) throws Exception {
		divisaDAO.update(row, -1);
	}

	@Override
	public void deleteDivisa(Divisa row) throws Exception {
		divisaDAO.delete(row, -1);
	}

	@Override
	public Divisa getDivisa(Divisa row) throws Exception {
		return divisaDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveDivisa(Divisa row) throws Exception {
		try {
			addDivisa(row);
		} catch (Exception le_e) {
			updateDivisa(row);
			LOGGER.error(" Error saveDivisa " ,  le_e );
		}
	}

	@Override
	public void beginLoadDivisa() throws Exception {
		divisaDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Divisa> getDivisas() throws Exception {
		return divisaDAO.findAll();
	}

	@Override
	public Collection<Divisa> searchDivisa(String palabraClave) throws Exception {
		return divisaDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadDivisa(String nombreArchivo) throws Exception {
		Divisa row = new Divisa();
		Collection<Divisa> rows = new ArrayList<Divisa>();
		AuditoriaCarga auditoriaCarga;

		rows = divisaDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Divisa> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// Festivo
	/*
	 * Nombre DTO: Festivo Nombre DAO: festivoDAO Plural: Festivos
	 */
	@Override
	public void addFestivo(Festivo row) throws Exception {
		Festivo domingo = new Festivo();
		int diaSemana = 0;

		festivoDAO.add(row, -1);

		diaSemana = DateUtils.getDayOfWeek(row.getFecha());
		if (diaSemana == 7) {
			domingo.setFecha(DateUtils.getSQLDate(DateUtils.getDatePlusDays(row.getFecha(), 1)));
			domingo.setEstadoCarga("A");
			domingo.setFechaUltimaCarga(DateUtils.getTimestamp());

			festivoDAO.add(domingo, -1);
		}
	}

	@Override
	public Collection<Festivo> getFestivos() throws Exception {
		return festivoDAO.findAll();
	}

	@Override
	public Collection<Date> getFestivosFecha() throws Exception {
		ArrayList<Date> rows = new ArrayList<Date>();

		for (Festivo row : festivoDAO.findAll()) {
			rows.add(row.getFecha());
		}
		return rows;
	}

	// Tercero
	/*
	 * Nombre DTO: Tercero Nombre DAO: terceroDAO Plural: Terceros
	 */
	@Override
	public void addTercero(Tercero row, int codigoUsuario) throws Exception {
		terceroDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateTercero(Tercero row) throws Exception {
		terceroDAO.update(row, -1);
	}

	@Override
	public void deleteTercero(Tercero row) throws Exception {
		terceroDAO.delete(row, -1);
	}

	@Override
	public Tercero getTercero(Tercero row) throws Exception {
		return terceroDAO.findByPrimaryKey(row);
	}

	@Override
	public Tercero getTerceroPorTipoYNumeroIdentificacion(Tercero row) throws Exception {
		return terceroDAO.findByTipoAndNumeroIdentificacion(row);
	}

	@Override
	public void saveTercero(Tercero row, int codigoUsuario) throws Exception {
		try {
			addTercero(row, codigoUsuario);
		} catch (Exception le_e) {
			updateTercero(row);
			LOGGER.error(" Error saveTercero " ,  le_e );
		}
	}

	@Override
	public void beginLoadTercero() throws Exception {
		terceroDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Tercero> getTerceros() throws Exception {
		return terceroDAO.findAll();
	}

	@Override
	public Collection<Tercero> searchTercero(String palabraClave) throws Exception {
		return terceroDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadTercero(String nombreArchivo) throws Exception {
		Tercero row = new Tercero();
		Collection<Tercero> rows = new ArrayList<Tercero>();
		AuditoriaCarga auditoriaCarga;

		rows = terceroDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Tercero> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getTipoIdentificacion() + " - " + row.getNumeroIdentificacion());
				auditoriaCarga.setDescripcionRegistro(row.getPrimerApellido() + " " + row.getSegundoApellido() + " " + row.getPrimerNombre() + " " + row.getSegundoNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// TipoIdentificacion
	/*
	 * Nombre DTO: TipoIdentificacion Nombre DAO: tipoIdentificacionDAO Plural: TiposIdentificacion
	 */
	@Override
	public void addTipoIdentificacion(TipoIdentificacion row) throws Exception {
		tipoIdentificacionDAO.add(row, -1);
	}

	@Override
	public void updateTipoIdentificacion(TipoIdentificacion row) throws Exception {
		tipoIdentificacionDAO.update(row, -1);
	}

	@Override
	public void deleteTipoIdentificacion(TipoIdentificacion row) throws Exception {
		tipoIdentificacionDAO.delete(row, -1);
	}

	@Override
	public TipoIdentificacion getTipoIdentificacion(TipoIdentificacion row) throws Exception {
		return tipoIdentificacionDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveTipoIdentificacion(TipoIdentificacion row) throws Exception {
		try {
			addTipoIdentificacion(row);
		} catch (Exception le_e) {
			updateTipoIdentificacion(row);
			LOGGER.error(" Error saveTipoIdentificacion " ,  le_e );
		}
	}

	@Override
	public void beginLoadTipoIdentificacion() throws Exception {
		tipoIdentificacionDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<TipoIdentificacion> getTiposIdentificacion() throws Exception {
		return tipoIdentificacionDAO.findAll();
	}

	@Override
	public Collection<TipoIdentificacion> searchTipoIdentificacion(String palabraClave) throws Exception {
		return tipoIdentificacionDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadTipoIdentificacion(String nombreArchivo) throws Exception {
		TipoIdentificacion row = new TipoIdentificacion();
		Collection<TipoIdentificacion> rows = new ArrayList<TipoIdentificacion>();
		AuditoriaCarga auditoriaCarga;

		rows = tipoIdentificacionDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<TipoIdentificacion> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// TipoTelefono
	/*
	 * Nombre DTO: TipoTelefono Nombre DAO: tipoTelefonoDAO Plural: TiposTelefonos
	 */
	@Override
	public void addTipoTelefono(TipoTelefono row) throws Exception {
		tipoTelefonoDAO.add(row, -1);
	}

	@Override
	public void updateTipoTelefono(TipoTelefono row) throws Exception {
		tipoTelefonoDAO.update(row, -1);
	}

	@Override
	public void deleteTipoTelefono(TipoTelefono row) throws Exception {
		tipoTelefonoDAO.delete(row, -1);
	}

	@Override
	public TipoTelefono getTipoTelefono(TipoTelefono row) throws Exception {
		return tipoTelefonoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveTipoTelefono(TipoTelefono row) throws Exception {
		try {
			addTipoTelefono(row);
		} catch (Exception le_e) {
			updateTipoTelefono(row);
			LOGGER.error(" Error saveTipoTelefono " ,  le_e );
		}
	}

	@Override
	public void beginLoadTipoTelefono() throws Exception {
		tipoTelefonoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<TipoTelefono> getTiposTelefonos() throws Exception {
		return tipoTelefonoDAO.findAll();
	}

	@Override
	public Collection<TipoTelefono> searchTipoTelefono(String palabraClave) throws Exception {
		return tipoTelefonoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadTipoTelefono(String nombreArchivo) throws Exception {
		TipoTelefono row = new TipoTelefono();
		Collection<TipoTelefono> rows = new ArrayList<TipoTelefono>();
		AuditoriaCarga auditoriaCarga;

		rows = tipoTelefonoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<TipoTelefono> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// TipoIndicativo
	/*
	 * Nombre DTO: TipoIndicativo Nombre DAO: tipoIndicativoDAO Plural: TiposIndicativos
	 */
	@Override
	public void addTipoIndicativo(TipoIndicativo row) throws Exception {
		tipoIndicativoDAO.add(row, -1);
	}

	@Override
	public void updateTipoIndicativo(TipoIndicativo row) throws Exception {
		tipoIndicativoDAO.update(row, -1);
	}

	@Override
	public void deleteTipoIndicativo(TipoIndicativo row) throws Exception {
		tipoIndicativoDAO.delete(row, -1);
	}

	@Override
	public TipoIndicativo getTipoIndicativo(TipoIndicativo row) throws Exception {
		return tipoIndicativoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveTipoIndicativo(TipoIndicativo row) throws Exception {
		try {
			addTipoIndicativo(row);
		} catch (Exception le_e) {
			updateTipoIndicativo(row);
			LOGGER.error(" Error saveTipoIndicativo " ,  le_e );
		}
	}

	@Override
	public void beginLoadTipoIndicativo() throws Exception {
		tipoIndicativoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<TipoIndicativo> getTiposIndicativos() throws Exception {
		return tipoIndicativoDAO.findAll();
	}

	@Override
	public Collection<Pais> getPaises() throws Exception {
		return paisDAO.findAll();
	}

	@Override
	public Collection<TipoIndicativo> searchTipoIndicativo(String palabraClave) throws Exception {
		return tipoIndicativoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadTipoIndicativo(String nombreArchivo) throws Exception {
		TipoIndicativo row = new TipoIndicativo();
		Collection<TipoIndicativo> rows = new ArrayList<TipoIndicativo>();
		AuditoriaCarga auditoriaCarga;

		rows = tipoIndicativoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<TipoIndicativo> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCiudad() + " - " + row.getDepartamento());
				auditoriaCarga.setDescripcionRegistro(row.getIndicativo());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// RiesgoOperacionalProducto
	/*
	 * Nombre DTO: RiesgoOperacionalProducto Nombre DAO: riesgoOperacionalProductoDAO Plural: RiesgosOperacionalesProducto
	 */
	@Override
	public void addRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception {
		riesgoOperacionalProductoDAO.add(row, -1);
	}

	@Override
	public void updateRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception {
		riesgoOperacionalProductoDAO.update(row, -1);
	}

	@Override
	public void deleteRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception {
		riesgoOperacionalProductoDAO.delete(row, -1);
	}

	@Override
	public RiesgoOperacionalProducto getRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception {
		return riesgoOperacionalProductoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception {
		try {
			addRiesgoOperacionalProducto(row);
		} catch (Exception le_e) {
			updateRiesgoOperacionalProducto(row);
			LOGGER.error(" Error saveRiesgoOperacionalProducto " ,  le_e );
		}
	}

	@Override
	public void beginLoadRiesgoOperacionalProducto() throws Exception {
		riesgoOperacionalProductoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<RiesgoOperacionalProducto> getRiesgosOperacionalesProducto() throws Exception {
		return riesgoOperacionalProductoDAO.findAll();
	}

	@Override
	public Collection<RiesgoOperacionalProducto> searchRiesgoOperacionalProducto(String palabraClave) throws Exception {
		return riesgoOperacionalProductoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadRiesgoOperacionalProducto(String nombreArchivo) throws Exception {
		RiesgoOperacionalProducto row = new RiesgoOperacionalProducto();
		Collection<RiesgoOperacionalProducto> rows = new ArrayList<RiesgoOperacionalProducto>();
		AuditoriaCarga auditoriaCarga;

		rows = riesgoOperacionalProductoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<RiesgoOperacionalProducto> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// RiesgoOperacionalProceso
	/*
	 * Nombre DTO: RiesgoOperacionalProceso Nombre DAO: riesgoOperacionalProcesoDAO Plural: RiesgosOperacionalesProcesos
	 */
	@Override
	public void addRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception {
		riesgoOperacionalProcesoDAO.add(row, -1);
	}

	@Override
	public void updateRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception {
		riesgoOperacionalProcesoDAO.update(row, -1);
	}

	@Override
	public void deleteRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception {
		riesgoOperacionalProcesoDAO.delete(row, -1);
	}

	@Override
	public RiesgoOperacionalProceso getRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception {
		return riesgoOperacionalProcesoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception {
		try {
			addRiesgoOperacionalProceso(row);
		} catch (Exception le_e) {
			updateRiesgoOperacionalProceso(row);
			LOGGER.error(" Error saveRiesgoOperacionalProceso " ,  le_e );
		}
	}

	@Override
	public void beginLoadRiesgoOperacionalProceso() throws Exception {
		riesgoOperacionalProcesoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<RiesgoOperacionalProceso> getRiesgosOperacionalesProcesos() throws Exception {
		return riesgoOperacionalProcesoDAO.findAll();
	}

	@Override
	public Collection<RiesgoOperacionalProceso> searchRiesgoOperacionalProceso(String palabraClave) throws Exception {
		return riesgoOperacionalProcesoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadRiesgoOperacionalProceso(String nombreArchivo) throws Exception {
		RiesgoOperacionalProceso row = new RiesgoOperacionalProceso();
		Collection<RiesgoOperacionalProceso> rows = new ArrayList<RiesgoOperacionalProceso>();
		AuditoriaCarga auditoriaCarga;

		rows = riesgoOperacionalProcesoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<RiesgoOperacionalProceso> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// RiesgoOperacionalLineaOperativa
	/*
	 * Nombre DTO: RiesgoOperacionalLineaOperativa Nombre DAO: riesgoOperacionalLineaOperativaDAO Plural: RiesgosOperacionalesLineasOperativas
	 */
	@Override
	public void addRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception {
		riesgoOperacionalLineaOperativaDAO.add(row, -1);
	}

	@Override
	public void updateRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception {
		riesgoOperacionalLineaOperativaDAO.update(row, -1);
	}

	@Override
	public void deleteRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception {
		riesgoOperacionalLineaOperativaDAO.delete(row, -1);
	}

	@Override
	public RiesgoOperacionalLineaOperativa getRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception {
		return riesgoOperacionalLineaOperativaDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception {
		try {
			addRiesgoOperacionalLineaOperativa(row);
		} catch (Exception le_e) {
			updateRiesgoOperacionalLineaOperativa(row);
			LOGGER.error(" Error saveRiesgoOperacionalLineaOperativa " ,  le_e );
		}
	}

	@Override
	public void beginLoadRiesgoOperacionalLineaOperativa() throws Exception {
		riesgoOperacionalLineaOperativaDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<RiesgoOperacionalLineaOperativa> getRiesgosOperacionalesLineasOperativas() throws Exception {
		return riesgoOperacionalLineaOperativaDAO.findAll();
	}

	@Override
	public Collection<RiesgoOperacionalLineaOperativa> searchRiesgoOperacionalLineaOperativa(String palabraClave) throws Exception {
		return riesgoOperacionalLineaOperativaDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadRiesgoOperacionalLineaOperativa(String nombreArchivo) throws Exception {
		RiesgoOperacionalLineaOperativa row = new RiesgoOperacionalLineaOperativa();
		Collection<RiesgoOperacionalLineaOperativa> rows = new ArrayList<RiesgoOperacionalLineaOperativa>();
		AuditoriaCarga auditoriaCarga;

		rows = riesgoOperacionalLineaOperativaDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<RiesgoOperacionalLineaOperativa> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// Municipio
	/*
	 * Nombre DTO: Municipio Nombre DAO: municipioDAO Plural: Municipios
	 */
	@Override
	public void addMunicipio(Municipio row) throws Exception {
		municipioDAO.add(row, -1);
	}

	@Override
	public void updateMunicipio(Municipio row) throws Exception {
		municipioDAO.update(row, -1);
	}

	@Override
	public void deleteMunicipio(Municipio row) throws Exception {
		municipioDAO.delete(row, -1);
	}

	@Override
	public Municipio getMunicipio(Municipio row) throws Exception {
		return municipioDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveMunicipio(Municipio row) throws Exception {
		try {
			addMunicipio(row);
		} catch (Exception le_e) {
			updateMunicipio(row);
			LOGGER.error(" Error saveRiesgoOperacionalLineaOperativa " ,  le_e );
		}
	}

	@Override
	public void beginLoadMunicipio() throws Exception {
		municipioDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Municipio> getMunicipios() throws Exception {
		return municipioDAO.findAll();
	}

	@Override
	public Collection<Municipio> getMunicipiosPorDepartamento(String codigoDepto) throws Exception {
		return municipioDAO.findByDepartamento(codigoDepto);
	}

	@Override
	public Collection<Municipio> searchMunicipio(String palabraClave) throws Exception {
		return municipioDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadMunicipio(String nombreArchivo) throws Exception {
		Municipio row = new Municipio();
		Collection<Municipio> rows = new ArrayList<Municipio>();
		AuditoriaCarga auditoriaCarga;

		rows = municipioDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Municipio> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigoDepartamento() + " - " + row.getCodigoMunicipio());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// ErrorValidacion
	/*
	 * Nombre DTO: ErrorValidacion Nombre DAO: errorValidacionDAO Plural: ErroresValidacion
	 */
	@Override
	public void addErrorValidacion(ErrorValidacion row) throws Exception {
		errorValidacionDAO.add(row, -1);
	}

	@Override
	public void updateErrorValidacion(ErrorValidacion row) throws Exception {
		errorValidacionDAO.update(row, -1);
	}

	@Override
	public void deleteErrorValidacion(ErrorValidacion row) throws Exception {
		errorValidacionDAO.delete(row, -1);
	}

	@Override
	public ErrorValidacion getErrorValidacion(ErrorValidacion row) throws Exception {
		return errorValidacionDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveErrorValidacion(ErrorValidacion row) throws Exception {
		try {
			addErrorValidacion(row);
		} catch (Exception le_e) {
			updateErrorValidacion(row);
			LOGGER.error(" Error saveErrorValidacion " ,  le_e );
		}
	}

	@Override
	public void beginLoadErrorValidacion() throws Exception {
		errorValidacionDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<ErrorValidacion> getErroresValidacion() throws Exception {
		return errorValidacionDAO.findAll();
	}

	@Override
	public Collection<ErrorValidacion> searchErrorValidacion(String palabraClave) throws Exception {
		return errorValidacionDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadErrorValidacion(String nombreArchivo) throws Exception {
		ErrorValidacion row = new ErrorValidacion();
		Collection<ErrorValidacion> rows = new ArrayList<ErrorValidacion>();
		AuditoriaCarga auditoriaCarga;

		rows = errorValidacionDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<ErrorValidacion> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// Departamento
	/*
	 * Nombre DTO: Departamento Nombre DAO: departamentoDAO Plural: Departamentos
	 */
	@Override
	public void addDepartamento(Departamento row) throws Exception {
		departamentoDAO.add(row, -1);
	}

	@Override
	public void updateDepartamento(Departamento row) throws Exception {
		departamentoDAO.update(row, -1);
	}

	@Override
	public void deleteDepartamento(Departamento row) throws Exception {
		departamentoDAO.delete(row, -1);
	}

	@Override
	public Departamento getDepartamento(Departamento row) throws Exception {
		return departamentoDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveDepartamento(Departamento row) throws Exception {
		try {
			addDepartamento(row);
		} catch (Exception le_e) {
			updateDepartamento(row);
			LOGGER.error(" Error saveDepartamento " ,  le_e );
		}
	}

	@Override
	public void beginLoadDepartamento() throws Exception {
		departamentoDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<Departamento> getDepartamentos() throws Exception {
		return departamentoDAO.findAll();
	}

	@Override
	public Collection<Departamento> searchDepartamento(String palabraClave) throws Exception {
		return departamentoDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadDepartamento(String nombreArchivo) throws Exception {
		Departamento row = new Departamento();
		Collection<Departamento> rows = new ArrayList<Departamento>();
		AuditoriaCarga auditoriaCarga;

		rows = departamentoDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<Departamento> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// ActividadEconomica
	/*
	 * Nombre DTO: ActividadEconomica Nombre DAO: actividadEconomicaDAO Plural: ActividadesEconomicas
	 */
	@Override
	public void addActividadEconomica(ActividadEconomica row) throws Exception {
		actividadEconomicaDAO.add(row, -1);
	}

	@Override
	public void updateActividadEconomica(ActividadEconomica row) throws Exception {
		actividadEconomicaDAO.update(row, -1);
	}

	@Override
	public void deleteActividadEconomica(ActividadEconomica row) throws Exception {
		actividadEconomicaDAO.delete(row, -1);
	}

	@Override
	public ActividadEconomica getActividadEconomica(ActividadEconomica row) throws Exception {
		return actividadEconomicaDAO.findByPrimaryKey(row);
	}

	@Override
	public void saveActividadEconomica(ActividadEconomica row) throws Exception {
		try {
			addActividadEconomica(row);
		} catch (Exception le_e) {
			updateActividadEconomica(row);
			LOGGER.error(" Error saveActividadEconomica " ,  le_e );
		}
	}

	private void saveCierreMensual(CierreMensual row) throws Exception {
		try {
			addCierreMensual(row);
		} catch (Exception le_e) {
		}
	}
	
	private void saveOperacionSFCN2(JerarquiaOperacionSFC_N2 row) throws Exception {
		try {
			addOperacionSFCN2(row);
		} catch (Exception le_e) {
		}
	}

	private void saveOperacionSFCN3(JerarquiaOperacionSFC_N3 row) throws Exception {
		try {
			addOperacionSFCN3(row);
		} catch (Exception le_e) {
		}
	}
	
	@Override
	public void beginLoadActividadEconomica() throws Exception {
		actividadEconomicaDAO.updateEstadoCarga("I");
	}

	@Override
	public Collection<ActividadEconomica> getActividadesEconomicas() throws Exception {
		return actividadEconomicaDAO.findAll();
	}

	@Override
	public Collection<ActividadEconomica> searchActividadEconomica(String palabraClave) throws Exception {
		return actividadEconomicaDAO.findBy(palabraClave);
	}

	@Override
	public void endLoadActividadEconomica(String nombreArchivo) throws Exception {
		ActividadEconomica row = new ActividadEconomica();
		Collection<ActividadEconomica> rows = new ArrayList<ActividadEconomica>();
		AuditoriaCarga auditoriaCarga;

		rows = actividadEconomicaDAO.findByEstadoCarga("I");
		if (rows.size() != 0) {
			Iterator<ActividadEconomica> itRows = rows.iterator();

			while (itRows.hasNext()) {
				row = itRows.next();

				auditoriaCarga = new AuditoriaCarga();
				auditoriaCarga.setCodigoRegistro(row.getCodigo());
				auditoriaCarga.setDescripcionRegistro(row.getNombre());
				auditoriaCarga.setNombreArchivo(nombreArchivo);
				auditoriaCarga.setNotas("El registro exista y no viene en el archivo de carga");
				auditoriaCarga.setFecha(DateUtils.getTimestamp());

				addAuditoriaCarga(auditoriaCarga);
			}
		}
	}

	// Perfil
	/*
	 * Nombre DTO: Perfil Nombre DAO: perfilDAO Plural: Perfiles
	 */

	@Override
	public Collection<Perfil> getPerfiles() throws Exception {
		return perfilDAO.findAll();
	}

	@Override
	public Collection<Perfil> searchPerfiles(String palabraClave) throws Exception {
		return perfilDAO.findBy(palabraClave);
	}

	// CierrMensual
	/*
	 * Nombre DTO: CierreMensual Nombre DAO: cierreMensualDAO Plural: CierresMensuales
	 */
	@Override
	public void addCierreMensual(CierreMensual row) throws Exception {
		cierreMensualDAO.add(row, -1);
	}

	@Override
	public void addOperacionSFCN2(JerarquiaOperacionSFC_N2 row) throws Exception {
		operacionSFC_N2DAO.add(row, -1);
	}

	@Override
	public void addOperacionSFCN3(JerarquiaOperacionSFC_N3 row) throws Exception {
		operacionSFC_N3DAO.add(row, -1);
	}
	
	@Override
	public void deleteCierreMensual(CierreMensual row) throws Exception {
		cierreMensualDAO.delete(row, -1);
	}

	@Override
	public CierreMensual getCierreMensual(CierreMensual row) throws Exception {
		return cierreMensualDAO.findByPrimaryKey(row);
	}

	@Override
	public Collection<CierreMensual> getCierresMensuales() throws Exception {
		return cierreMensualDAO.findAll();
	}

	/*
	 * Funciones de carga
	 */
	@Override
	public int getNumeroDatos(String linea) {
		int contador = 0;
		int contadorDatos = 0;

		for (contador = 0; contador < linea.length(); contador++) {
			if (linea.charAt(contador) == ';') {
				contadorDatos++;
			}
		}

		return contadorDatos - 1;
	}

	private Festivo getStringToFestivo(String as_linea, EstructuraCarga[] aec_estructura) {
		Festivo festivo = new Festivo();
		String ls_linea = as_linea;

		try {
			festivo.setFecha(DateUtils.getSQLDate(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim(), "yyyy-MM-dd"));
		} catch (Exception le_e) {
			festivo.setFecha(null);
		}
		festivo.setEstadoCarga("A");
		festivo.setFechaUltimaCarga(DateUtils.getTimestamp());

		return festivo;
	}

	private ClaseRiesgo getStringToClaseRiesgo(String as_linea, EstructuraCarga[] aec_estructura) {
		ClaseRiesgo claseRiesgo = new ClaseRiesgo();
		String ls_linea = as_linea;

		claseRiesgo.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			claseRiesgo.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			claseRiesgo.setNombre(ls_linea.trim());
		}
		claseRiesgo.setEstadoCarga("A");
		claseRiesgo.setFechaUltimaCarga(DateUtils.getTimestamp());

		return claseRiesgo;
	}

	private Cliente getStringToCliente(String as_linea, EstructuraCarga[] aec_estructura) {
		Cliente cliente = new Cliente();
		String ls_linea = as_linea;

		cliente.setNumeroCliente(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		cliente.setTipoIdentificacion(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		cliente.setNumeroIdentificacion(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		cliente.setDigitoVerificacion(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[3].getLongitud());
		cliente.setPrimerApellido(ls_linea.substring(0, aec_estructura[4].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[4].getLongitud());
		cliente.setSegundoApellido(ls_linea.substring(0, aec_estructura[5].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[5].getLongitud());
		try {
			cliente.setPrimerNombre(ls_linea.substring(0, aec_estructura[6].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[6].getLongitud());
		} catch (Exception le_e) {
			cliente.setPrimerNombre(ls_linea.trim());
		}
		cliente.setEstadoCarga("A");
		cliente.setFechaUltimaCarga(DateUtils.getTimestamp());

		return cliente;
	}

	private Contrato getStringToContrato(String as_linea, EstructuraCarga[] aec_estructura) {
		Contrato contrato = new Contrato();
		String ls_linea = as_linea;

		contrato.setNumeroCliente(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			contrato.setNumeroContrato(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			contrato.setNumeroContrato(ls_linea.trim());
		}
		contrato.setEstadoCarga("A");
		contrato.setFechaUltimaCarga(DateUtils.getTimestamp());

		return contrato;
	}

	private PerdidaOperacionalClaseRiesgo getStringToPerdidaOperacionalClaseRiesgo(String as_linea, EstructuraCarga[] aec_estructura) {
		PerdidaOperacionalClaseRiesgo perdidaOperacionalClaseRiesgo = new PerdidaOperacionalClaseRiesgo();
		String ls_linea = as_linea;

		perdidaOperacionalClaseRiesgo.setCuenta(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		perdidaOperacionalClaseRiesgo.setCodigoClaseRiesgo(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		try {
			perdidaOperacionalClaseRiesgo.setCodigoTipoPerdida(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		} catch (Exception le_e) {
			perdidaOperacionalClaseRiesgo.setCodigoTipoPerdida(ls_linea.trim());
		}
		perdidaOperacionalClaseRiesgo.setEstadoCarga("A");
		perdidaOperacionalClaseRiesgo.setFechaUltimaCarga(DateUtils.getTimestamp());

		return perdidaOperacionalClaseRiesgo;
	}

	private PartidaPendiente getStringToPartidaPendiente(String as_linea, EstructuraCarga[] aec_estructura) {
		PartidaPendiente partidaPendiente = new PartidaPendiente();
		String ls_linea = as_linea;
		String ls_importe = "";

		partidaPendiente.setCuenta(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		partidaPendiente.setDescripcion(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		partidaPendiente.setSucursalDestino(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		partidaPendiente.setNaturaleza(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[3].getLongitud());
		partidaPendiente.setReferenciaCruce(ls_linea.substring(0, aec_estructura[4].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[4].getLongitud());
		partidaPendiente.setDivisa(ls_linea.substring(0, aec_estructura[5].getLongitud()).trim());
		if (partidaPendiente.getDivisa().equals("")) {
			partidaPendiente.setDivisa("COP");
		}
		ls_linea = ls_linea.substring(aec_estructura[5].getLongitud());
		ls_importe = ls_linea.substring(0, aec_estructura[6].getLongitud()).trim();
		ls_importe = ls_importe.substring(0, 15) + "." + ls_importe.substring(15, 17);
		partidaPendiente.setImporte(new Double(ls_importe).doubleValue());
		ls_linea = ls_linea.substring(aec_estructura[6].getLongitud());
		partidaPendiente.setConcepto(ls_linea.substring(0, aec_estructura[7].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[7].getLongitud());
		partidaPendiente.setFechaContable(DateUtils.getTimestamp(DateUtils.getDate(ls_linea.substring(0, aec_estructura[8].getLongitud()), "yyyyMMdd")));
		ls_linea = ls_linea.substring(aec_estructura[8].getLongitud());
		partidaPendiente.setEstadoCarga("A");
		partidaPendiente.setFechaUltimaCarga(DateUtils.getTimestamp());

		return partidaPendiente;
	}

	private PerdidaOperacional getStringToPerdidaOperacional(String as_linea, EstructuraCarga[] aec_estructura) {
		PerdidaOperacional perdidaOperacional = new PerdidaOperacional();
		String ls_linea = as_linea;

		perdidaOperacional.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			perdidaOperacional.setDescripcion(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			perdidaOperacional.setDescripcion(ls_linea.trim());
		}
		perdidaOperacional.setEstadoCarga("A");
		perdidaOperacional.setFechaUltimaCarga(DateUtils.getTimestamp());

		return perdidaOperacional;
	}

	private UsuarioAltamira getStringToUsuarioAltamira(String as_linea, EstructuraCarga[] aec_estructura) {
		UsuarioAltamira usuarioAltamira = new UsuarioAltamira();
		String ls_linea = as_linea;

		usuarioAltamira.setCodigoEmpleado(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		usuarioAltamira.setNombreEmpleado(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		try {
			usuarioAltamira.setCodigoArea(Integer.parseInt(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim()));
		} catch (Exception le_e) {
			usuarioAltamira.setCodigoArea(0);
		}
		ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		usuarioAltamira.setNombreArea(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[3].getLongitud());
		usuarioAltamira.setCodigoPerfil(ls_linea.substring(0, aec_estructura[4].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[4].getLongitud());
		usuarioAltamira.setNombrePerfil(ls_linea.substring(0, aec_estructura[5].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[5].getLongitud());
		try {
			usuarioAltamira.setCorreoElectronico(ls_linea.substring(0, aec_estructura[6].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[6].getLongitud());
		} catch (Exception le_e) {
			usuarioAltamira.setCorreoElectronico(ls_linea.trim());
		}
		usuarioAltamira.setEstadoCarga("A");
		usuarioAltamira.setFechaUltimaCarga(DateUtils.getTimestamp());

		return usuarioAltamira;
	}

	private HADTAPL getStringToHADTAPL(String as_linea, EstructuraCarga[] aec_estructura) {
		HADTAPL hadtapl = new HADTAPL();
		String ls_linea = as_linea;

		hadtapl.setCuentaContable(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			hadtapl.setIndicadorContrato(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			hadtapl.setIndicadorContrato(ls_linea.trim());
		}
		hadtapl.setEstadoCarga("A");
		hadtapl.setFechaUltimaCarga(DateUtils.getTimestamp());

		return hadtapl;
	}

	private Producto getStringToProducto(String as_linea, EstructuraCarga[] aec_estructura) {
		Producto producto = new Producto();
		String ls_linea = as_linea;

		producto.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			producto.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			producto.setNombre(ls_linea.trim());
		}
		producto.setEstadoCarga("A");
		producto.setFechaUltimaCarga(DateUtils.getTimestamp());

		return producto;
	}

	private Sucursal getStringToSucursal(String as_linea, EstructuraCarga[] aec_estructura) {
		Sucursal sucursal = new Sucursal();
		String ls_linea = as_linea;

		sucursal.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		sucursal.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		sucursal.setTipoCentro(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		sucursal.setCodigoCentroSuperior(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[3].getLongitud());
		try {
			sucursal.setNombreCentroSuperior(ls_linea.substring(0, aec_estructura[4].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[4].getLongitud());
		} catch (Exception le_e) {
			sucursal.setNombreCentroSuperior(ls_linea.trim());
		}
		sucursal.setEstadoCarga("A");
		sucursal.setFechaUltimaCarga(DateUtils.getTimestamp());

		return sucursal;
	}

	private PUC getStringToPUC(String as_linea, EstructuraCarga[] aec_estructura) {
		PUC puc = new PUC();
		String ls_linea = as_linea;

		puc.setNumeroCuenta(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		puc.setDigitoVerificacion(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		puc.setDescripcion(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		puc.setEstadoCuenta(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[3].getLongitud());
		puc.setCentroResponsable(ls_linea.substring(0, aec_estructura[4].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[4].getLongitud());
		puc.setIndicadorCuenta(ls_linea.substring(0, aec_estructura[5].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[5].getLongitud());
		puc.setNaturaleza(ls_linea.substring(0, aec_estructura[6].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[7].getLongitud());
		puc.setTipoCuenta(ls_linea.substring(0, aec_estructura[7].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[7].getLongitud());
		puc.setFormaActualizacion(ls_linea.substring(0, aec_estructura[8].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[8].getLongitud());
		puc.setContrapartidaDebe(ls_linea.substring(0, aec_estructura[9].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[9].getLongitud());
		puc.setContrapartidaHaber(ls_linea.substring(0, aec_estructura[10].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[10].getLongitud());
		puc.setTipoApunte(ls_linea.substring(0, aec_estructura[11].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[11].getLongitud());
		puc.setTipoCentroOrigenAutorizado(ls_linea.substring(0, aec_estructura[12].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[12].getLongitud());
		puc.setIndicadorCentroOrigen(ls_linea.substring(0, aec_estructura[13].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[13].getLongitud());
		puc.setCentrosOrigenAutorizados(ls_linea.substring(0, aec_estructura[14].getLongitud()));
		ls_linea = ls_linea.substring(aec_estructura[14].getLongitud());
		puc.setTipoCentroDestinoAutorizado(ls_linea.substring(0, aec_estructura[15].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[15].getLongitud());
		puc.setIndicadorCentroDestino(ls_linea.substring(0, aec_estructura[16].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[16].getLongitud());
		puc.setCentrosDestinoAutorizados(ls_linea.substring(0, aec_estructura[17].getLongitud()));
		ls_linea = ls_linea.substring(aec_estructura[17].getLongitud());
		puc.setTipoMoneda(ls_linea.substring(0, aec_estructura[18].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[18].getLongitud());
		puc.setIndicadorSIRO(ls_linea.substring(0, aec_estructura[19].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[19].getLongitud());
		try {
			puc.setIndicadorTercero(ls_linea.substring(0, aec_estructura[20].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[20].getLongitud());
		} catch (Exception le_e) {
			puc.setIndicadorTercero(ls_linea.trim());
		}
		puc.setEstadoCarga("A");
		puc.setFechaUltimaCarga(DateUtils.getTimestamp());

		return puc;
	}

	private Divisa getStringToDivisa(String as_linea, EstructuraCarga[] aec_estructura) {
		Divisa divisa = new Divisa();
		String ls_linea = as_linea;

		divisa.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			divisa.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			divisa.setNombre(ls_linea.trim());
		}
		divisa.setEstadoCarga("A");
		divisa.setFechaUltimaCarga(DateUtils.getTimestamp());

		return divisa;
	}

	private Tercero getStringToTercero(String as_linea, EstructuraCarga[] aec_estructura) {
		Tercero tercero = new Tercero();
		String ls_linea = as_linea;

		tercero.setTipoIdentificacion(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		tercero.setNumeroIdentificacion(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		tercero.setDigitoVerificacion(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		tercero.setPrimerApellido(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[3].getLongitud());
		tercero.setSegundoApellido(ls_linea.substring(0, aec_estructura[4].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[4].getLongitud());
		tercero.setPrimerNombre(ls_linea.substring(0, aec_estructura[5].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[5].getLongitud());
		tercero.setSegundoNombre(ls_linea.substring(0, aec_estructura[6].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[6].getLongitud());
		tercero.setPEATVIA(ls_linea.substring(0, aec_estructura[7].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[7].getLongitud());
		tercero.setDireccion(ls_linea.substring(0, aec_estructura[8].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[8].getLongitud());
		tercero.setDepartamento(ls_linea.substring(0, aec_estructura[9].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[9].getLongitud());
		tercero.setMunicipio(ls_linea.substring(0, aec_estructura[10].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[10].getLongitud());
		tercero.setActividadEconomica(ls_linea.substring(0, aec_estructura[11].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[11].getLongitud());
		tercero.setTipoTelefono(ls_linea.substring(0, aec_estructura[12].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[12].getLongitud());
		tercero.setIndicativo(ls_linea.substring(0, aec_estructura[13].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[13].getLongitud());
		tercero.setTelefono(ls_linea.substring(0, aec_estructura[14].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[14].getLongitud());
		tercero.setAplicativo(ls_linea.substring(0, aec_estructura[15].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[15].getLongitud());
		tercero.setUsuario(ls_linea.substring(0, aec_estructura[16].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[16].getLongitud());
		tercero.setOficinaModificacion(ls_linea.substring(0, aec_estructura[17].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[17].getLongitud());
		try {
			tercero.setFecha(ls_linea.substring(0, aec_estructura[18].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[18].getLongitud());
		} catch (Exception le_e) {
			tercero.setFecha(ls_linea.trim());
		}
		tercero.setEstadoCarga("A");
		tercero.setFechaUltimaCarga(DateUtils.getTimestamp());

		return tercero;
	}

	private TipoIdentificacion getStringToTipoIdentificacion(String as_linea, EstructuraCarga[] aec_estructura) {
		TipoIdentificacion tipoIdentificacion = new TipoIdentificacion();
		String ls_linea = as_linea;

		tipoIdentificacion.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			tipoIdentificacion.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			tipoIdentificacion.setNombre(ls_linea.trim());
		}
		tipoIdentificacion.setEstadoCarga("A");
		tipoIdentificacion.setFechaUltimaCarga(DateUtils.getTimestamp());

		return tipoIdentificacion;
	}

	private TipoTelefono getStringToTipoTelefono(String as_linea, EstructuraCarga[] aec_estructura) {
		TipoTelefono tipoTelefono = new TipoTelefono();
		String ls_linea = as_linea;

		tipoTelefono.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			tipoTelefono.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			tipoTelefono.setNombre(ls_linea.trim());
		}
		tipoTelefono.setEstadoCarga("A");
		tipoTelefono.setFechaUltimaCarga(DateUtils.getTimestamp());

		return tipoTelefono;
	}

	private TipoIndicativo getStringToTipoIndicativo(String as_linea, EstructuraCarga[] aec_estructura) {
		TipoIndicativo tipoIndicativo = new TipoIndicativo();
		String ls_linea = as_linea;

		tipoIndicativo.setCiudad(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		tipoIndicativo.setDepartamento(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		try {
			tipoIndicativo.setIndicativo(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		} catch (Exception le_e) {
			tipoIndicativo.setIndicativo(ls_linea.trim());
		}
		tipoIndicativo.setEstadoCarga("A");
		tipoIndicativo.setFechaUltimaCarga(DateUtils.getTimestamp());

		return tipoIndicativo;
	}

	private RiesgoOperacionalProducto getStringToRiesgoOperacionalProducto(String as_linea, EstructuraCarga[] aec_estructura) {
		RiesgoOperacionalProducto riesgoOperacionalProducto = new RiesgoOperacionalProducto();
		String ls_linea = as_linea;

		riesgoOperacionalProducto.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			riesgoOperacionalProducto.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			riesgoOperacionalProducto.setNombre(ls_linea.trim());
		}
		riesgoOperacionalProducto.setEstadoCarga("A");
		riesgoOperacionalProducto.setFechaUltimaCarga(DateUtils.getTimestamp());

		return riesgoOperacionalProducto;
	}

	private RiesgoOperacionalProceso getStringToRiesgoOperacionalProceso(String as_linea, EstructuraCarga[] aec_estructura) {
		RiesgoOperacionalProceso riesgoOperacionalProceso = new RiesgoOperacionalProceso();
		String ls_linea = as_linea;

		riesgoOperacionalProceso.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			riesgoOperacionalProceso.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			riesgoOperacionalProceso.setNombre(ls_linea.trim());
		}
		riesgoOperacionalProceso.setEstadoCarga("A");
		riesgoOperacionalProceso.setFechaUltimaCarga(DateUtils.getTimestamp());

		return riesgoOperacionalProceso;
	}

	private RiesgoOperacionalLineaOperativa getStringToRiesgoOperacionalLineaOperativa(String as_linea, EstructuraCarga[] aec_estructura) {
		RiesgoOperacionalLineaOperativa riesgoOperacionalLineaOperativa = new RiesgoOperacionalLineaOperativa();
		String ls_linea = as_linea;

		riesgoOperacionalLineaOperativa.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			riesgoOperacionalLineaOperativa.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			riesgoOperacionalLineaOperativa.setNombre(ls_linea.trim());
		}
		riesgoOperacionalLineaOperativa.setEstadoCarga("A");
		riesgoOperacionalLineaOperativa.setFechaUltimaCarga(DateUtils.getTimestamp());

		return riesgoOperacionalLineaOperativa;
	}

	private Municipio getStringToMunicipio(String as_linea, EstructuraCarga[] aec_estructura) {
		Municipio municipio = new Municipio();
		String ls_linea = as_linea;

		municipio.setCodigoDepartamento(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		municipio.setCodigoMunicipio(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		try {
			municipio.setNombre(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
		} catch (Exception le_e) {
			municipio.setNombre(ls_linea.trim());
		}
		municipio.setEstadoCarga("A");
		municipio.setFechaUltimaCarga(DateUtils.getTimestamp());

		return municipio;
	}

	private ErrorValidacion getStringToErrorValidacion(String as_linea, EstructuraCarga[] aec_estructura) {
		ErrorValidacion errorValidacion = new ErrorValidacion();
		String ls_linea = as_linea;

		errorValidacion.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			errorValidacion.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			errorValidacion.setNombre(ls_linea.trim());
		}
		errorValidacion.setEstadoCarga("A");
		errorValidacion.setFechaUltimaCarga(DateUtils.getTimestamp());

		return errorValidacion;
	}

	private Departamento getStringToDepartamento(String as_linea, EstructuraCarga[] aec_estructura) {
		Departamento departamento = new Departamento();
		String ls_linea = as_linea;

		departamento.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			departamento.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			departamento.setNombre(ls_linea.trim());
		}
		departamento.setEstadoCarga("A");
		departamento.setFechaUltimaCarga(DateUtils.getTimestamp());

		return departamento;
	}

	private ActividadEconomica getStringToActividadEconomica(String as_linea, EstructuraCarga[] aec_estructura) {
		ActividadEconomica actividadEconomica = new ActividadEconomica();
		String ls_linea = as_linea;

		actividadEconomica.setCodigo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
		ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
		try {
			actividadEconomica.setNombre(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());
			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
		} catch (Exception le_e) {
			actividadEconomica.setNombre(ls_linea.trim());
		}
		actividadEconomica.setEstadoCarga("A");
		actividadEconomica.setFechaUltimaCarga(DateUtils.getTimestamp());

		return actividadEconomica;
	}

	private CierreMensual getStringToCierreMensual(String as_linea, EstructuraCarga[] aec_estructura) {
		CierreMensual cierrre = new CierreMensual();
		String ls_linea = as_linea;

		try {
			cierrre.setYear(Integer.parseInt(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim()));
			ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
			cierrre.setMes(Integer.parseInt(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim()));
			cierrre.setFechaUltimaCarga(DateUtils.getTimestamp());
		} catch (Exception le_e) {
		}
		return cierrre;
	}
	
	private JerarquiaOperacionSFC_N2 getStringToOperacionSFCN2(String as_linea, EstructuraCarga[] aec_estructura) {
		JerarquiaOperacionSFC_N2 operacionN2 = new JerarquiaOperacionSFC_N2();
		String ls_linea = as_linea;

		try {		
			operacionN2.setConsecutivo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());
			
			ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
			operacionN2.setCodigoNcClaseRiesgo(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());

			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
			operacionN2.setCodigo(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());
			
			ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
			operacionN2.setNombre(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());										
						
		} catch (Exception le_e) {
			operacionN2.setNombre(ls_linea.trim());
		}
		operacionN2.setFechaEstadoCarga(DateUtils.getTimestamp());
		return operacionN2;
	}
	
	private JerarquiaOperacionSFC_N3 getStringToOperacionSFCN3(String as_linea, EstructuraCarga[] aec_estructura) {
		JerarquiaOperacionSFC_N3 operacionN3 = new JerarquiaOperacionSFC_N3();
		String ls_linea = as_linea;

		try {
			operacionN3.setConsecutivo(ls_linea.substring(0, aec_estructura[0].getLongitud()).trim());

			ls_linea = ls_linea.substring(aec_estructura[0].getLongitud());
			operacionN3.setCodigoNcN1ClaseRiesgo(ls_linea.substring(0, aec_estructura[1].getLongitud()).trim());

			ls_linea = ls_linea.substring(aec_estructura[1].getLongitud());
			operacionN3.setCodigoNcClaseRiesgo(ls_linea.substring(0, aec_estructura[2].getLongitud()).trim());

			ls_linea = ls_linea.substring(aec_estructura[2].getLongitud());
			operacionN3.setCodigo(ls_linea.substring(0, aec_estructura[3].getLongitud()).trim());

			ls_linea = ls_linea.substring(aec_estructura[3].getLongitud());
			operacionN3.setNombre(ls_linea.substring(0, aec_estructura[4].getLongitud()).trim());

		} catch (Exception le_e) {
			operacionN3.setNombre(ls_linea.trim());
		}
		operacionN3.setEstadoCarga("A");
		operacionN3.setFechaEstadoCarga(DateUtils.getTimestamp());
		
		return operacionN3;
	}

	@Override
	public void addRow(String as_nombreTabla, String ls_linea, EstructuraCarga[] estructurasCarga) throws Exception {
		Object lo_object;

		if (as_nombreTabla.equals("NC_FESTIVO")) {
			lo_object = getStringToFestivo(ls_linea, estructurasCarga);
			try {
				addFestivo((Festivo) lo_object);
			} catch (Exception e) {
				LOGGER.error("El festivo ya fue registrado previamente", e);
			}
		} else if (as_nombreTabla.equals("NC_CLASE_RIESGO")) {
			lo_object = getStringToClaseRiesgo(ls_linea, estructurasCarga);
			saveClaseRiesgo((ClaseRiesgo) lo_object);
		} else if (as_nombreTabla.equals("NC_CLIENTE")) {
			lo_object = getStringToCliente(ls_linea, estructurasCarga);
			saveCliente((Cliente) lo_object);
		} else if (as_nombreTabla.equals("NC_CONTRATO")) {
			lo_object = getStringToContrato(ls_linea, estructurasCarga);
			saveContrato((Contrato) lo_object);
		} else if (as_nombreTabla.equals("NC_PERD_OPER_CLAS_RIES")) {
			lo_object = getStringToPerdidaOperacionalClaseRiesgo(ls_linea, estructurasCarga);
			savePerdidaOperacionalClaseRiesgo((PerdidaOperacionalClaseRiesgo) lo_object);
		} else if (as_nombreTabla.equals("NC_PARTIDA_PENDIENTE")) {
			lo_object = getStringToPartidaPendiente(ls_linea, estructurasCarga);
			savePartidaPendiente((PartidaPendiente) lo_object);
		} else if (as_nombreTabla.equals("NC_PERDIDA_OPERACION")) {
			lo_object = getStringToPerdidaOperacional(ls_linea, estructurasCarga);
			savePerdidaOperacional((PerdidaOperacional) lo_object);
		} else if (as_nombreTabla.equals("NC_USUARIO_ALTAMIRA")) {
			lo_object = getStringToUsuarioAltamira(ls_linea, estructurasCarga);
			saveUsuarioAltamira((UsuarioAltamira) lo_object);
		} else if (as_nombreTabla.equals("NC_HADTAPL")) {
			lo_object = getStringToHADTAPL(ls_linea, estructurasCarga);
			saveHADTAPL((HADTAPL) lo_object);
		} else if (as_nombreTabla.equals("NC_PRODUCTO")) {
			lo_object = getStringToProducto(ls_linea, estructurasCarga);
			saveProducto((Producto) lo_object);
		} else if (as_nombreTabla.equals("NC_SUCURSAL")) {
			lo_object = getStringToSucursal(ls_linea, estructurasCarga);
			saveSucursal((Sucursal) lo_object);
		} else if (as_nombreTabla.equals("NC_PUC")) {
			lo_object = getStringToPUC(ls_linea, estructurasCarga);
			savePUC((PUC) lo_object, -1);
		} else if (as_nombreTabla.equals("NC_DIVISA")) {
			lo_object = getStringToDivisa(ls_linea, estructurasCarga);
			saveDivisa((Divisa) lo_object);
		} else if (as_nombreTabla.equals("NC_TERCERO")) {
			lo_object = getStringToTercero(ls_linea, estructurasCarga);
			saveTercero((Tercero) lo_object, -1);
		} else if (as_nombreTabla.equals("NC_TIPO_IDENTIFICACION")) {
			lo_object = getStringToTipoIdentificacion(ls_linea, estructurasCarga);
			saveTipoIdentificacion((TipoIdentificacion) lo_object);
		} else if (as_nombreTabla.equals("NC_TIPO_TELEFONO")) {
			lo_object = getStringToTipoTelefono(ls_linea, estructurasCarga);
			saveTipoTelefono((TipoTelefono) lo_object);
		} else if (as_nombreTabla.equals("NC_TIPO_INDICATIVO")) {
			lo_object = getStringToTipoIndicativo(ls_linea, estructurasCarga);
			saveTipoIndicativo((TipoIndicativo) lo_object);
		} else if (as_nombreTabla.equals("NC_RIES_OPER_PROD")) {
			lo_object = getStringToRiesgoOperacionalProducto(ls_linea, estructurasCarga);
			saveRiesgoOperacionalProducto((RiesgoOperacionalProducto) lo_object);
		} else if (as_nombreTabla.equals("NC_RIES_OPER_PROC")) {
			lo_object = getStringToRiesgoOperacionalProceso(ls_linea, estructurasCarga);
			saveRiesgoOperacionalProceso((RiesgoOperacionalProceso) lo_object);
		} else if (as_nombreTabla.equals("NC_RIES_OPER_LINE_OPER")) {
			lo_object = getStringToRiesgoOperacionalLineaOperativa(ls_linea, estructurasCarga);
			saveRiesgoOperacionalLineaOperativa((RiesgoOperacionalLineaOperativa) lo_object);
		} else if (as_nombreTabla.equals("NC_MUNICIPIO")) {
			lo_object = getStringToMunicipio(ls_linea, estructurasCarga);
			saveMunicipio((Municipio) lo_object);
		} else if (as_nombreTabla.equals("NC_ERROR_VALIDACION")) {
			lo_object = getStringToErrorValidacion(ls_linea, estructurasCarga);
			saveErrorValidacion((ErrorValidacion) lo_object);
		} else if (as_nombreTabla.equals("NC_DEPARTAMENTO")) {
			lo_object = getStringToDepartamento(ls_linea, estructurasCarga);
			saveDepartamento((Departamento) lo_object);
		} else if (as_nombreTabla.equals("NC_ACTIVIDAD_ECONOMICA")) {
			lo_object = getStringToActividadEconomica(ls_linea, estructurasCarga);
			saveActividadEconomica((ActividadEconomica) lo_object);
		} else if (as_nombreTabla.equals("NC_CIERRE_MENSUAL")) {
			lo_object = getStringToCierreMensual(ls_linea, estructurasCarga);
			saveCierreMensual((CierreMensual) lo_object);
			//Proyecto CCS1-63 CE025
		} else if (as_nombreTabla.equals("NC_OPER_N2_SFC")) {
			lo_object = getStringToOperacionSFCN2(ls_linea, estructurasCarga);
			saveOperacionSFCN2((JerarquiaOperacionSFC_N2) lo_object);
			//Proyecto CCS1-63 CE025
		} else if (as_nombreTabla.equals("NC_OPER_N3_SFC")) {
			lo_object = getStringToOperacionSFCN3(ls_linea, estructurasCarga);
			saveOperacionSFCN3((JerarquiaOperacionSFC_N3) lo_object);
		}
	}

	@Override
	public Map<String, String> getCVSucursal() throws Exception {
		return sucursalDAO.getCVSucursal();
	}

	@Override
	public Map<String, String> getCVUsuarioAltamira(Number codigoSucursal, int codPadrino, String estado) throws Exception {
		return usuarioAltamiraDAO.getCVUsuarioAltamira(codigoSucursal, codPadrino, estado);
	}

	@Override
	public Map<?, String> getCVClasRiesPorCuenta(String cuenta) throws Exception {
		return claseRiesgoDAO.getCVClasRiesPorCuenta(cuenta);
	}

	@Override
	public Map<?, String> getCVPerdidaOper(String cuenta, String claseRiesgo) throws Exception {
		return perdidaOperacionalDAO.getCVPorCuentaClaseRiesgo(cuenta, claseRiesgo);
	}

	@Override
	public Collection<String> getPUCArchAltamira() throws Exception {
		return pucDAO.getPUCArchAltamira();
	}

	@Override
	public Collection<PUC> searchPUCPorCuenta(String cuenta) throws Exception {
		return pucDAO.searchPUCPorCuenta(cuenta);
	}

	@Override
	public Collection<String> getTercerosArch() throws Exception {
		return terceroDAO.getTercerosArch();
	}

	
}
