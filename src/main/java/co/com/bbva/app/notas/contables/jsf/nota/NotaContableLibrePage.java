package co.com.bbva.app.notas.contables.jsf.nota;

import co.com.bbva.app.notas.contables.carga.dto.*;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.StringUtils;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

/**
 * <p>
 * Pagina para manejar la administracion de parametros relacionados con la entidad TipoEvento
 * </p>
 * 
 */
@SessionScoped
@Named
public class NotaContableLibrePage extends FlujoNotaContableLibrePage implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(NotaContableLibrePage.class);
	private static int numArchivo = 1;
	private static int numRegistro = -1;
	private String cuentaContable = "";
	private Date fechaNota = null;
	private List<SelectItem> cuentas = new ArrayList<>();
	private List<SelectItem> divisas = new ArrayList<>();
	private List<SelectItem> sucursalesPartida = new ArrayList<>();
	private List<SelectItem> tiposDoc = new ArrayList<>();
	private List<SelectItem> contratos1 = new ArrayList<>();
	private ArrayList<Date> diasNoHabiles = new ArrayList<>();
	private Collection<MontoMaximo> montos = new ArrayList<>();
	private String minFecha = "";
	private String maxFecha = "";
	private Double montoAlertaCOP = 0d;
	private Double montoAlertaEXT = 0d;
	private List<SelectItem> clasesRiesgo = new ArrayList<>();
	private List<SelectItem> perdidaOper = new ArrayList<>();
	private List<SelectItem> procesos = new ArrayList<>();
	private List<SelectItem> productos = new ArrayList<>();
	private List<SelectItem> lineasOperativas = new ArrayList<>();

	// Campos para CCS1-63 CE025
	private List<SelectItem> subProductos = new ArrayList<>();
	private List<SelectItem> canales = new ArrayList<>();
	private List<SelectItem> subcanales = new ArrayList<>();
	private List<SelectItem> clasesRiesgoN2 = new ArrayList<>();
	private List<SelectItem> clasesRiesgoN3 = new ArrayList<>();
	private List<SelectItem> horas = new ArrayList<>();
	private List<SelectItem> minutos = new ArrayList<>();
	private List<SelectItem> horario = new ArrayList<>();

	// banderas para controlar el cierre de popups
	private boolean ocultarPopupTema = false;
	private boolean ocultarPopupRiesgo = false;
	private boolean ocultarPopupGuardarNota = false;
	private boolean aplicaRecuperacion = false;
	//private Session session;

	public NotaContableLibrePage() {
		super();
	}

	public String iniciarPagina() {
		//session = getContablesSessionBean().getSessionTrace();
		try {
			LOGGER.info("{} Registro de NOTA CONTABLE LIBRE - Configurando valores iniciales");
			String codSucursal = getUsuarioLogueado().getSucursal().getCodigo();
			LOGGER.info("{} Obteniendo Info. de la sucursal {}", codSucursal);
			String msg = notasContablesManager.verificarUsuarioSubGerente(codSucursal);
			if (msg.isEmpty()) {
				LOGGER.info("{} Verificando que la sucursal {} corresponda a un centro especial", codSucursal);
				msg = notasContablesManager.verificarCentroEspecial(codSucursal);
				if (msg.isEmpty()) {
					LOGGER.info("{} Obteniendo Info. de los montos mximos");
					montos = notasContablesManager.getMontoMaximos();
					for (MontoMaximo monto : montos) {
						if (monto.getEstado().equals("A") && monto.getDivisa().equals("1")) {
							montoAlertaCOP = monto.getMontoMaximoAlerta();
						} else if (monto.getEstado().equals("A")) {
							montoAlertaEXT = monto.getMontoMaximoAlerta();
						}
					}
					LOGGER.info("{} Obteniendo Info. de los das no hábiles");
					diasNoHabiles = new ArrayList<>(cargaAltamiraManager.getFestivosFecha());

					FechaHabilitada fechaHabilitada = new FechaHabilitada();
					fechaHabilitada.setCodigoSucursal(getUsuarioLogueado().getSucursal().getCodigo());
					fechaHabilitada = notasContablesManager.getFechaHabilitadaPorSucursal(fechaHabilitada);

					LOGGER.info("{} Estableciendo fechas lmite");
					maxFecha = StringUtils.getString(DateUtils.getSQLDate(DateUtils.getNextWorkDay(diasNoHabiles)), "dd-MM-yyyy");
					minFecha = StringUtils.getString(DateUtils.getDateTodayBeforeDays(fechaHabilitada.getDias().intValue(), diasNoHabiles), "dd-MM-yyyy");

					if (nota.getCodigo().intValue() > 0) {
						LOGGER.info("{} Validando Info. de la Nota Contable");
						consultarFlujo();
						nota.setPuedeEditar(true);
						nota.setPuedeAnular(true);
						for (NotaContableRegistroLibre tema : temasNotaContable) {
							temaActual = tema;
							fechaNota = temaActual.getFechaContable();
							codigo = tema.getCodigo();
							verNota();
						}
						temaActual = new NotaContableRegistroLibre();
					}
				} else {
					LOGGER.warn("{} La sucursal: {} NO corresponda a un centro especial", codSucursal);
					nuevoMensaje(FacesMessage.SEVERITY_WARN, msg);
				}
			} else {
				LOGGER.warn("{} No se tiene parametrizado un autorizador");
				nuevoMensaje(FacesMessage.SEVERITY_WARN, msg);
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al intentar al configurar los valores iniciales: NC LIBRE", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema, la aplicación no se pudo iniciar correctamente");
		}
		return null;
	}

	/**
	 * Funcion llamada al iniciar el flujo de creacion de notas contables
	 * 
	 * @return
	 */
	@Override
	public String iniciar() {
		return NOTA_CONTABLE_LIBRE;
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo NotaContable
	 * 
	 * @return
	 */
	public String editar() {
		try {
			LOGGER.info("{} Edicion del registro seleccionado: {}" + this.codigo);
			cuentaContable = "";
			cuentas = new ArrayList<>();
			LOGGER.info("{} Recuperando la Informacion");
			for (NotaContableRegistroLibre nct : temasNotaContable) {
				LOGGER.info(" ::: temasNotaContable ::: " + temasNotaContable.size());
				if (nct.getCodigo() == codigo.intValue()) {
					temaActual = nct;
					if (codigo > 0) {
						cuentaContable = nct.getCuentaContable();
						buscarCuentaContable();
						if (nct.getPucCuenta().getIndicadorSIRO() != null && nct.getPucCuenta().getIndicadorSIRO().equals("T")) {
							cargarModalRiesgo();
						}
					}
					cargarFiltrosNota();
					return null;
				}
			}
			temaActual = new NotaContableRegistroLibre();
			LOGGER.info("getNumRegistro ::::>>>  " + getNumRegistro());
			temaActual.setCodigo(getNumRegistro());
			
		} catch (Exception e) {
			lanzarError(e, "Error al iniciar el editor de tema");
		}
		return null;
	}

	public String buscarCuentaContable() {
		try {
			LOGGER.info("{} Buscar Info. de la(s) cuenta(s) contable(s): {} ", cuentaContable);
			if (cuentaContable.length() >= 4) {
				cuentas = new ArrayList<>();
				temaActual.setCuentaContable("");
				temaActual.setPucCuenta(new PUC());
				LOGGER.info("{} Intentando obtener la(s) cuenta(s) contable(s) en el PUC");
				Collection<PUC> pucs = cargaAltamiraManager.searchPUCPorCuenta(cuentaContable);
				if (pucs.isEmpty()) {
					LOGGER.warn("{} No se encontró Info. para la(s) cuenta(s) contable(s): {} en el PUC", cuentaContable);
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontró coincidencia para la cuenta contable");
				} else {
					boolean sucValida;
					for (PUC p : pucs) {
						sucValida = cargaAltamiraManager.isSucursalValidaPUCOrigen(getUsuarioLogueado().getSucursal(), p);
						if (sucValida) {
							cuentas.add(new SelectItem(p.getNumeroCuenta(), p.getNumeroCuenta() + " " + p.getDescripcion()));
						}
					}
					LOGGER.info("{} Se encontraron {} cuenta(s) contable(s) válidas para la sucursal: {} ", cuentas.size(), getUsuarioLogueado().getSucursal().getCodigo());
					if (cuentas.isEmpty()) {
						LOGGER.warn("{} Ninguna cuenta es válida para la sucursal");
						nuevoMensaje(FacesMessage.SEVERITY_WARN, "Centro no Autorizado para afectar la cuenta " + cuentaContable);
					} else if (cuentas.size() == 1) {
						LOGGER.info("{} La búsqueda de la cuenta contable coincide con un registro");
						temaActual.setCuentaContable(pucs.iterator().next().getNumeroCuenta());
						seleccionarCuenta();
					}
				}
			} else {
				LOGGER.warn("{} Para realizar la búsqueda de la(s) cuenta(s) contable(s) se requiere mnimo 4 caracteres");
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "Por favor indique un número de cuenta de 4 dgitos o más");
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al realizar la consulta de la(s) cuenta contable", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al realizar la consulta de la(s) cuenta(s) contable(s)");
		}
		return null;
	}

	public String seleccionarCuenta() {
		try {
			LOGGER.info("{} Procesando la cuenta contable seleccionada: {}", temaActual.getCuentaContable());
			if (temaActual.getCuentaContable() != null && !temaActual.getCuentaContable().isEmpty()) {
				LOGGER.info("{} Confirmando contra el PUC la cuenta contable");
				PUC puc = new PUC();
				puc.setNumeroCuenta(temaActual.getCuentaContable());
				puc = cargaAltamiraManager.getPUC(puc);
				temaActual.setPucCuenta(puc);

				LOGGER.info("{} Verificando si la cuenta contable requiere contrato");
				HADTAPL hadtapl = new HADTAPL();
				hadtapl.setCuentaContable(temaActual.getCuentaContable());
				hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);
				String cuentaContableS = temaActual.getCuentaContable();
				while (hadtapl.getIndicadorContrato().equals("") && !cuentaContableS.equals("")) {
					hadtapl = new HADTAPL();
					hadtapl.setCuentaContable(cuentaContableS);
					hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);
					cuentaContableS = cuentaContableS.substring(0, cuentaContableS.length() - 1);
				}
				temaActual.setHadtapl(hadtapl);
				cargarFiltrosNota();
			} else {
				temaActual.setCuentaContable("");
				temaActual.setPucCuenta(new PUC());
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al confirmar y establecer las propiedades de la cuenta contable seleccionada", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al procesar la cuenta contable seleccionada");
		}
		return null;
	}

	private void cargarFiltrosNota() throws Exception {
		LOGGER.info("{} Obteniendo Info. y estableciendo valores inciales de los filtros");
		cargarSucursales();
		cargarDivisas();
		tiposDoc = getSelectItemList(notasContablesManager.getCV(TipoIdentificacion.class), false);
	}

	private void cargarSucursales() throws Exception {
		sucursalesPartida = new ArrayList<>();
		String sucUsuario = getUsuarioLogueado().getSucursal().getCodigo();

		boolean sinSucPartida = temaActual.getCodigoSucursalDestino() == null || temaActual.getCodigoSucursalDestino().isEmpty();

		// una vez seleccionada la cuenta, se buscan las sucursales asociadas
		Collection<Sucursal> sucursales = cargaAltamiraManager.getSucursales();
		for (Sucursal s : sucursales) {
			if (cargaAltamiraManager.isSucursalValidaPUCDestino(s, temaActual.getPucCuenta())) {
				FechaHabilitada fecha = new FechaHabilitada();
				fecha.setCodigoSucursal(s.getCodigo());
				fecha = notasContablesManager.getFechaHabilitadaPorSucursal(fecha);

				Date minFechaSuc = DateUtils.getDateTodayBeforeDays(fecha.getDias().intValue(), diasNoHabiles);

				if (minFechaSuc.compareTo(fechaNota) <= 0) {
					// sucursal por defecto para partida
					if (sinSucPartida && s.getCodigo().equals(sucUsuario)) {
						temaActual.setCodigoSucursalDestino(sucUsuario);
					}
					sucursalesPartida.add(new SelectItem(s.getCodigo(), s.getCodigo() + " " + s.getNombre()));
				}
			}
		}
	}

	/**
	 * Carga las divisas a mostrar dependiendo de la nota contable
	 */
	private void cargarDivisas() throws Exception {
		divisas = new ArrayList<>();
		TreeSet<Divisa> divSet = new TreeSet<>(cargaAltamiraManager.getDivisas());
		String tipoDivisa = temaActual.getPucCuenta().getTipoMoneda();
		for (Divisa div : divSet) {
			String codDiv = div.getCodigo();
			boolean indDivisa = false;
			// si el tema indica que no es multidivisa
			if (!tipoDivisa.equals("2")) {
				// si el tema indica que es local y la divisa es de tipo pesos
				if (tipoDivisa.equals("0") && codDiv.equals("COP")) {
					indDivisa = true;
				}
				// si el tema indica que es moneda extranjera y la divisa es diferente a pesos
				// colombianos
				else if (tipoDivisa.equals("1") && !codDiv.equals("COP")) {
					indDivisa = true;
				}
			} else {
				// si es multidivisa se permite cualquier tipo
				indDivisa = true;
			}
			// MODIFICACION COL513513S013857 CARGUE DE DIVISAS POR TIPO DE PARTIDA Y TIPO DE
			// DIVISA
			String tipoPartida = temaActual.getCuentaContable().isEmpty() ? "" : temaActual.getCuentaContable().substring(0, 1);
			if ((tipoPartida.equals("4") || tipoPartida.equals("5"))) {
				if (tipoDivisa.equals("0") && codDiv.equals("COP")) {
					indDivisa = true;
				} else if (tipoDivisa.equals("1") && codDiv.equals("COD")) {
					indDivisa = true;
				} else if (tipoDivisa.equals("2") && (codDiv.equals("COD") || codDiv.equals("COP"))) {
					indDivisa = true;
				} else {
					indDivisa = false;
				}
			}
			if (indDivisa) {
				divisas.add(new SelectItem(codDiv, codDiv + " " + div.getNombre()));
			}
		}
	}

	public String validarFecha() {
		LOGGER.info("{} Validando la fecha seleccionada");
		java.sql.Date fecha = new java.sql.Date(DateUtils.getDate(maxFecha, "dd-MM-yyyy").getTime());
		Date fechaActual = new Date();
		java.sql.Date fechaAct = DateUtils.getSQLDate(fechaActual);
		LOGGER.info("{} Confirmando que se cumplan los criterios para la fecha :::: " + fechaNota);	
		if (fechaNota.after(DateUtils.getSQLDate(fechaAct))) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a HOY");
			fechaNota = fecha;
		} else if (fechaNota == null || diasNoHabiles.contains(fechaNota)) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha debe ser un da hábil");
			fechaNota = fecha;
		} else if (fechaNota == null || fechaNota.after(fecha)) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a " + StringUtils.getString(fecha, "yyyy-MM-dd"));
			fechaNota = fecha;
		}
		return null;
	}

	public String cargarModalRiesgo() {
		try {

			char cuenta = temaActual.getCuentaContable().charAt(0);			
			
			if(cuenta == '4') {
				aplicaRecuperacion = true;
			}else {
				aplicaRecuperacion = false;
				temaActual.getRiesgoOperacional().setAppRecuperacion("N");
			}
			
			clasesRiesgo = getSelectItemList(cargaAltamiraManager.getCVClasRiesPorCuenta(temaActual.getCuentaContable()), false);
			if (temaActual.getRiesgoOperacional().getCodigoClaseRiesgo() == null || temaActual.getRiesgoOperacional().getCodigoClaseRiesgo().isEmpty()) {
				perdidaOper = new ArrayList<>();

			} else {
				buscarPerdidasCuenta();
			}
			
			horas = new ArrayList<>();

			for (int i = 1; i <= 12; i++) {
				String horast = Integer.toString(i);

				if (i < 10) {
					horast = "0" + horast;
				}
				horas.add(new SelectItem(horast, horast));
			}

			minutos = new ArrayList<>();

			for (int j = 0; j <= 59; j++) {
				String mint = Integer.toString(j);

				if (j < 10) {
					mint = "0" + mint;
				}
				minutos.add(new SelectItem(mint, mint));
			}

			// horario
			horario = new ArrayList<>();
			horario.add(new SelectItem("A.M", "A.M"));
			horario.add(new SelectItem("P.M", "P.M"));

			procesos = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalProceso.class), false);
			productos = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalProducto.class), false);

			if (temaActual.getRiesgoOperacional().getCodigoProducto() != null) {
				buscarSubProducto();
			}

			lineasOperativas = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalLineaOperativa.class), false);

			canales = getSelectItemList(notasContablesManager.getCV(Canal.class), false);

			if (temaActual.getRiesgoOperacional().getCodigoCanal() != null) {
				buscarSubCanal();
			}

			if (temaActual.getRiesgoOperacional().getClaseRiesgo() != null) {
				buscarClaseRiesgoN2();
			}

			if (temaActual.getRiesgoOperacional().getClaseRiesgoN2() != null) {
				buscarClaseRiesgoN3();
			}

		} catch (Exception e) {
			lanzarError(e, "Error al inicializar el editor de riesgo operacional para la nota actual");
		}
		return null;
	}

	public String buscarPerdidasCuenta() {
		try {
			perdidaOper = getSelectItemList(cargaAltamiraManager.getCVPerdidaOper(temaActual.getCuentaContable(), temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
			clasesRiesgoN2 = getSelectItemList(notasContablesManager.findByClaseN2(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
		} catch (Exception e) {
			lanzarError(e, "Error al buscar el listado de perdidas operacionales");
		}
		return null;
	}

	/**
	 * Permite obtner la lista de subProductos dependiendo de un producto
	 * 
	 * @return
	 */
	public String buscarSubProducto() {
		try {
			subProductos = getSelectItemList(notasContablesManager.findByProducto(temaActual.getRiesgoOperacional().getCodigoProducto()), false);
		} catch (Exception e) {
			lanzarError(e, "Error al buscar el listado de subProductos operacionales");
		}
		return null;
	}

	/**
	 * Permite obtener la lista de subcanales por medio del codigo del canal
	 * 
	 * @return
	 */
	public String buscarSubCanal() {
		try {
			subcanales = getSelectItemList(notasContablesManager.findByCanal(temaActual.getRiesgoOperacional().getCodigoCanal()), false);
		} catch (Exception e) {
			lanzarError(e, "Error al buscar el listado de subCanales");
		}
		return null;
	}

	/**
	 * Permite obtner el listado de clase riesgo N2 dependiendo de la clase de riesgo
	 * 
	 * @return
	 */
	public String buscarClaseRiesgoN2() {
		try {
			clasesRiesgoN2 = getSelectItemList(notasContablesManager.findByClaseN2(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
		} catch (Exception e) {
			lanzarError(e, "Error al buscar el listado de clase riesgo N2");
		}
		return null;
	}

	/**
	 * Permite obtner el listado de clase riesgo N3 dependiendo de la clase de riesgo N3
	 * 
	 * @return
	 */
	public String buscarClaseRiesgoN3() {
		try {
			clasesRiesgoN3 = getSelectItemList(notasContablesManager.findByClaseN3(temaActual.getRiesgoOperacional().getCodigoClaseRiesgoN2(),temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
		} catch (Exception e) {
			lanzarError(e, "Error al buscar el listado de clase riesgo N3");
		}
		return null;
	}

	public String buscarTercero() {
		buscarTercero(temaActual.getTipoIdentificacion(), temaActual.getNumeroIdentificacion());
		return null;
	}

	private void buscarTercero(String tipo, String numero) {
		try {
			LOGGER.info("{} Intentando consultar tercero: {}", numero);
			numero = StringUtils.getStringLeftPaddingFixed(numero, 15, '0');
			String numTercero = "";
			String dvTercero = "";
			String nombreTercero = "";
			contratos1 = new ArrayList<>();

			LOGGER.info("{} Buscando en Clientes");
			Cliente cliente = new Cliente();
			cliente.setTipoIdentificacion(tipo);
			cliente.setNumeroIdentificacion(numero);
			cliente = cargaAltamiraManager.getClientePorTipoYNumeroIdentificacion(cliente);

			LOGGER.info("{} Verificando si se encontró Info. en clientes");
			if (!cliente.getNumeroIdentificacion().equals("")) {
				LOGGER.info("{} Estableciendo propiedades del tercero:cliente");
				numTercero = cliente.getNumeroIdentificacion();
				dvTercero = cliente.getDigitoVerificacion();
				nombreTercero = cliente.getPrimerNombre().trim() + " " + cliente.getPrimerApellido().trim() + " " + cliente.getSegundoApellido().trim();

				LOGGER.info("{} Validando si se deben cargar los contratos");
				if (temaActual.getHadtapl().getIndicadorContrato().equals("S")) {
					Contrato contrato = new Contrato();
					contrato.setNumeroCliente(cliente.getNumeroCliente());
					LOGGER.info("{} Obteniendo Info. de los contratos");
					Collection<Contrato> contratos = cargaAltamiraManager.getContratosPorCliente(contrato);
					for (Contrato c : contratos) {
						contratos1.add(new SelectItem(c.getNumeroContrato(), c.getNumeroContrato()));
					}
					LOGGER.info("{} Se encontró {} contrato(s)", contratos1.size());
				}
			} else {
				LOGGER.info("{} No se encontró Info. en Clientes - Buscando en Terceros");
				Tercero tercero = new Tercero();
				tercero.setTipoIdentificacion(tipo);
				tercero.setNumeroIdentificacion(numero);
				tercero = cargaAltamiraManager.getTerceroPorTipoYNumeroIdentificacion(tercero);

				LOGGER.info("{} Verificando si se encontró Info. en terceros");
				if (!tercero.getTipoIdentificacion().equals("")) {
					LOGGER.info("{} Estableciendo propiedades del tercero:tercero");
					numTercero = tercero.getNumeroIdentificacion();
					dvTercero = tercero.getDigitoVerificacion();
					nombreTercero = tercero.getPrimerNombre().trim() + " " + tercero.getPrimerApellido().trim() + " " + tercero.getSegundoApellido().trim();
				} else {
					LOGGER.warn("{} No se encontró Info. del tercero consultado: {}", numero);
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontró ningún cliente ni tercero con la combinacin tipo y número de identificación");
				}
			}
			LOGGER.info("{} Estableciendo atributos del tercero");
			temaActual.setNumeroIdentificacion(numTercero);
			temaActual.setDigitoVerificacion(dvTercero);
			temaActual.setNombreCompleto(nombreTercero);
		} catch (Exception e) {
			LOGGER.error("{} Error intentando buscar el tercero: {}", numero, e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al buscar el tercero con identificación " + numero);
		}
	}

	/**
	 * Metodo encargado de cargar los archivos para cada tema
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void listener(FileUploadEvent event) throws Exception {
		LOGGER.info("{} Iniciando la carga del archivo (SOPORTE)");

		String fileName = "";

		try {
			UploadedFile uploadedFile = event.getFile();
			String originalFileName = uploadedFile.getFileName();
			String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			long currentTime = System.currentTimeMillis();

			String prefijo = getNumArchivo() + "_";
			fileName = (prefijo + currentTime + fileExtension).toUpperCase();
			String filePath = DIR_SOPORTES + fileName;

			LOGGER.info("{} Preparando datos del soporte para subir el archivo: {}", originalFileName);
			LOGGER.info("{} Path de carga del soporte: {}", filePath);

			try (InputStream is = uploadedFile.getInputStream();
				 FileOutputStream fos = new FileOutputStream(filePath)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			}

			LOGGER.info("{} Carga del soporte completada");

			Anexo anexo = new Anexo();
			anexo.setArchivo(fileName);
			anexo.setNombre(originalFileName);
			anexo.setCodigoUsuario(getCodUsuarioLogueado());
			anexo.setFechaHora(new Timestamp(currentTime));
			anexo.setBorrar(false);
			anexo.setEstadoInstancia("1");
			anexo.setUsuNombre(getUsuarioLogueado().getUsuAltamira().getNombreEmpleado());
			anexos.add(anexo);
			
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "Successful: El archivo : " + DIR_SOPORTES + fileName + " se subio correctamente.");
			
		} catch (Exception e) {
			LOGGER.error("{} Error al intentar cargar el archivo (SOPORTE): {}", fileName, e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al cargar el archivo: " + DIR_SOPORTES + fileName);
			throw e;
		}
	}
//	public void listener(UploadEvent event) throws Exception {
//		LOGGER.info("{} Iniciando la carga del archivo (SOPORTE)");
//		String fileName = "";
//		try {
//			long time = new Date().getTime();
//			UploadItem item = event.getUploadItem();
//			LOGGER.info("{} Preparando datos del soporte para subir el archivo: {}", item.getFileName());
//			String prefijo = getNumArchivo() + "_";
//			fileName = (prefijo + time + item.getFileName().substring(item.getFileName().lastIndexOf("."))).toUpperCase();
//			File localFile = new File(DIR_SOPORTES + fileName);
//
//			LOGGER.info("{} Path de carga del soporte: {}", localFile.getPath());
//			final InputStream is = new FileInputStream(item.getFile());
//			FileOutputStream fos = new FileOutputStream(localFile);
//			int count = 0;
//			int length = 1024;
//			final byte[] info = new byte[length];
//			LOGGER.info("{} Procesando la carga del soporte");
//			while (is.read(info) != -1) {
//				fos.write(info);
//				count += length;
//			}
//			is.close();
//			fos.close();
//			item.getFile().delete();
//
//			LOGGER.info("{} Creacin del anexo para el soporte cargado");
//			Anexo anexo = new Anexo();
//			anexo.setArchivo(fileName);
//			anexo.setNombre(item.getFileName());
//			anexo.setCodigoUsuario(getCodUsuarioLogueado());
//			anexo.setFechaHora(new Timestamp(new Date().getTime()));
//			anexo.setBorrar(false);
//			anexo.setEstadoInstancia("1");
//			anexo.setUsuNombre(getUsuarioLogueado().getUsuAltamira().getNombreEmpleado());
//			anexos.add(anexo);
//		} catch (Exception e) {
//			LOGGER.error("{} Error al intentar cargar el archivo (SOPORTE): {}", fileName, e);
//			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al cargar el archivo: " + DIR_SOPORTES + fileName);
//			throw e;
//		}
//	}

	public String borrarAnexo() {
		LOGGER.info("{} Eliminando el soporte cargado y el registro del anexo");
		for (Anexo anexo : anexos) {
			if (anexo.getBorrar()) {
				anexos.remove(anexo);
				ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
				File f = new File(context.getRealPath(anexo.getArchivo()));
				f.delete();
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "El soporte " + anexo.getNombre() + " se ha borrado correctamente");
				return null;
			}
		}
		return null;
	}

	public String guardarNota() {
		try {
			LOGGER.info("{} Intentando grabar la Nota Contable Libre");
			LOGGER.info("{} Procesando validaciones y Persistiendo Info.");
			if (validarNota()) {
				if (nota.getCodigo().intValue() > 0) {
					LOGGER.info("En el segundo if de guardarNota para Nota Contable Libre");
					notasContablesManager.updateNotaContableLibre(nota, anexos, temasNotaContable, getCodUsuarioLogueado());
					LOGGER.info("Luego del updateNotaContableLibre");
					aprobar();
					LOGGER.info("Luego del aprobar()");
					PrimeFaces.current().executeScript("PF('popupNotaContableLibre').hide();");
				} else {
					nota.setTipoNota("L");
					nota.setCodigoSucursalOrigen(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
					LOGGER.info("{} Creando registro de la Instancia en base de datos");
					int idInstancia = notasContablesManager.crearInstanciaNotaContable(nota, new ArrayList<>(), temasNotaContable, anexos,
							new ArrayList<>(), new ArrayList<>(), getCodUsuarioLogueado());
					if (idInstancia != 0) {
						LOGGER.info("{} Obteniendo Info. de Instancia & Nota Contable");
						Instancia instancia = new Instancia();
						instancia.setCodigo(idInstancia);
						instancia = notasContablesManager.getInstancia(instancia);

						NotaContable notaContable = new NotaContable();
						notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
						notaContable = notasContablesManager.getNotaContable(notaContable);

						LOGGER.info("{} El registro de la NOTA CONTABLE LIBRE: {} fue exitoso", notaContable.getNumeroRadicacion());
						nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido radicada correctamente con el número: " + notaContable.getNumeroRadicacion());
						ocultarPopupGuardarNota = true;
						UsuarioModulo usuarioModulo = new UsuarioModulo();
						PrimeFaces.current().executeScript("PF('popupNotaContableLibre').hide();");
						try {
							LOGGER.info("{} Intentando enviar email de la nota grabada");
							int codigoUsuarioAsignado = instancia.getCodigoUsuarioActual().intValue();
							usuarioModulo.setCodigo(codigoUsuarioAsignado);
							usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
							enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
									"Módulo Notas Contables - Registro para aprobar",
									"Por favor ingrese al mdulo de Notas Contables, se le ha asignado un registro que requiere su verificacin");
						} catch (Exception e) {
							LOGGER.error("{} Error al intentar enviar el email de la nota (L) grabada", e);
							nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al enviar el correo electronico a " + usuarioModulo.getEMailModificado());
						}
						cancelarNota();
					} else {
						LOGGER.error("{} Error al crear la instancia de la Nota Contable Libre");
						nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema no fue posible crear la nota contable");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al intentar grabar la Nota Contable Libre", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al guardar la nota contable");
		}
		return null;
	}

	private boolean validarNota() {
		LOGGER.info("{} Validando campos requeridos");
		validarFecha();
		validador.validarRequerido(nota.getDescripcion(), "Descripción de la nota");
		if (anexos.isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "El registro de contabilidad libre exige soportes, por favor agregue al menos uno");
		}
		for (NotaContableTotal total : totalesNota) {
			if (total.getTotal() != 0) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El total para la divisa " + total.getCodigoDivisa() + " debe sumar cero (0)");
			}
		}
		validarRegistrosTema();
		if (getFacesContext().getMessageList().size() > 0) {
			ocultarPopupGuardarNota = false;
		}
		return !getFacesContext().getMessages().hasNext();
	}

	private boolean validarRegistrosTema() {
		LOGGER.info("{} Validando que el registro contenga la información requerida");
		if (temasNotaContable.isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha diligenciado la información de ningún registro para la Nota Contable");
			return false;
		}
		validarDuplas();
		validarSucursales();
		return true;
	}

	private void validarSucursales() {
		try {
			LOGGER.info("{} Validando fechas habilitadas de las sucursales");
			for (NotaContableRegistroLibre reg : temasNotaContable) {
				FechaHabilitada fecha = new FechaHabilitada();
				fecha.setCodigoSucursal(reg.getCodigoSucursalDestino());
				fecha = notasContablesManager.getFechaHabilitadaPorSucursal(fecha);
				Date minFechaSuc = DateUtils.getDateTodayBeforeDays(fecha.getDias().intValue(), diasNoHabiles);

				if (minFechaSuc.compareTo(fechaNota) > 0) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "El centro de destino " + reg.getCodigoSucursalDestino() + " no está habilitado para la fecha contable de la nota");
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al validar las fechas habilitadas de las sucursales");
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al validar las fechas habilitadas por sucursal");
		}
	}

	private void validarDuplas() {
		LOGGER.info("{} Validando que la suma de los registros sea igual a cero (0)");
		HashMap<String, HashMap<Integer, Double>> mapa = new HashMap<>();

		for (NotaContableRegistroLibre reg : temasNotaContable) {
			reg.setFechaContable(new java.sql.Date(fechaNota.getTime()));
			int multiplo = reg.getNaturalezaCuentaContable().equalsIgnoreCase("D") ? 1 : -1;
			int cuenta = getCuentaRara(reg.getCuentaContable());

			// si la cuenta necesita verificacion de suma por tupla
			if (cuenta > 0) {
				HashMap<Integer, Double> sumas = mapa.get(reg.getCodigoSucursalDestino());
				if (sumas == null) {
					sumas = new HashMap<>();
				}
				switch (cuenta) {
				case 61:
				case 62:
					sumar(sumas, 6162, reg.getMonto() * multiplo);
					break;
				case 63:
				case 64:
					sumar(sumas, 6364, reg.getMonto() * multiplo);
					break;
				case 81:
				case 83:
					sumar(sumas, 8183, reg.getMonto() * multiplo);
					break;
				case 82:
				case 84:
					sumar(sumas, 8284, reg.getMonto() * multiplo);
					break;
				default:
					break;
				}
				mapa.put(reg.getCodigoSucursalDestino(), sumas);
			}
		}
		// si el mapa tiene datos, se verifica la suma a cero
		if (!mapa.isEmpty()) {
			for (String sucDest : mapa.keySet()) {
				for (int dupla : mapa.get(sucDest).keySet()) {
					if (mapa.get(sucDest).get(dupla).doubleValue() != 0) {
						String duplaString = "" + dupla;
						nuevoMensaje(FacesMessage.SEVERITY_WARN, "Por favor verifique la sumatoria para las cuentas " + duplaString.substring(0, 2) + " - "
								+ duplaString.substring(2, 4) + " de la sucursal de destino " + sucDest);
					}
				}
			}
		}
	}

	private void sumar(HashMap<Integer, Double> sumas, int i, double d) {
		Double valor = sumas.get(i);
		if (valor == null) {
			valor = 0d;
		}
		valor += d;
		sumas.put(i, valor);
	}

	public String cancelarNota() {
		LOGGER.info("{} ... Restaurando a Valores Iniciales ...");
		cuentaContable = "";
		fechaNota = null;
		nota = new NotaContable();
		temaActual = new NotaContableRegistroLibre();
		temasNotaContable = new ArrayList<>();
		totalesNota = new ArrayList<>();
		anexos = new ArrayList<>();
		return null;
	}

	public String guardarRiesgo() {
		if (validarRiesgo()) {
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "La información de riesgo operacional se ha guardado temporalmente");
			ocultarPopupRiesgo = true;
			PrimeFaces.current().executeScript("PF('modalRiesgoRegLibre').hide();");
		}
		return null;
	}

	private boolean validarRiesgo() {
		LOGGER.info("{} Se va guardar la Info. diligenciada en ventana modal de Riesgo Para Nota Reg Libre. ");
		validador.validarPositivo(temaActual.getRiesgoOperacional().getImporteParcial(), "Importe Parcial");
		validador.validarPositivo(temaActual.getRiesgoOperacional().getImporteTotal(), "Importe Total");
		
		temaActual.getRiesgoOperacional().setFechaEvento(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaEventoPF()));
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaEvento(), "Fecha Inicial del Evento");
		
		temaActual.getRiesgoOperacional().setFechaFinEvento(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaFinEventoPF()));
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaFinEvento(), "Fecha Finalización del Evento");
		
		temaActual.getRiesgoOperacional().setFechaDescubrimientoEvento(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaDescubrimientoEventoPF()));
		validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento(), "Fecha del Descubrimiento");
		
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraInicioEvento(), "Hora Inicio Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosInicioEvento(), "Minutos Inicio Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraFinalEvento(), "Hora Finalización Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosFinalEvento(), "Minutos Fin Evento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraDescubrimiento(), "Hora Descubrimiento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosDescubrimiento(), "Minutos Descubrimiento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHorario(), "horario fecha inicial");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHorarioDescubre(), "horario descubrimiento");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getHorarioFinal(), "horario fecha final");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo(), "Clase de Riesgo");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoTipoPerdida(), "Tipo Pérdida");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProceso(), "Proceso");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoLineaOperativa(), "Línea Operativa");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProducto(), "Producto");

		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoSubProducto(), "SubProducto Afectado");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoCanal(), "Canal");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoSubCanal(), "SubCanal");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgoN2(), "SFC Clase Riesgo N2");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgoN3(), "SFC Clase Riesgo N3");
		validador.validarRequerido(temaActual.getRiesgoOperacional().getNombreOutSource(), "Nombre Outsourcing");
		validador.validarSeleccion(temaActual.getRiesgoOperacional().getAppRecuperacion(), "Aplica Recuperación");

		if (temaActual.getRiesgoOperacional().getAppRecuperacion().equals("S")) {
			
			temaActual.getRiesgoOperacional().setFechaRecuperacion(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaRecuperacionPF()));
			validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaRecuperacion(), "Fecha Recuperación");
			
			validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraRecuperacion(), "Hora Recuperación");
			validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosRecuperacion(), "Minutos Recuperación");
			validador.validarRequerido(temaActual.getRiesgoOperacional().getHorarioRecuperacion(), "horario Recuperación");
			validador.validarSeleccion(temaActual.getRiesgoOperacional().getTipoRecuperacion(), "Tipo Recuperación");
		}

		// Validacion para ocultar el popup una vez que la validacion es exitosa
		if (getFacesContext().getMessageList().size() > 0) {
			ocultarPopupRiesgo = false;
		}

		return !getFacesContext().getMessages().hasNext();
	}

	public String cancelarRiesgo() {
		if (temaActual.getCodigo() <= 0) {
			temaActual.setRiesgoOperacional(new RiesgoOperacional());
		} else {
			try {
				temaActual.setRiesgoOperacional(notasContablesManager.getRiesgoPorNotaContableYTemaNotaContable(temaActual.getCodigoNotaContable(), temaActual.getCodigo()));
			} catch (Exception e) {
				lanzarError(e, "Error recuperando la información de riesgo operacional");
			}
		}
		return null;
	}

	public String guardarTema() {
		LOGGER.info("{} Se va guardar la Info. diligenciada");
		if (validarTema()) {
			boolean exist = false;
			for (NotaContableRegistroLibre tema : temasNotaContable) {
				if (tema.getCodigo() == temaActual.getCodigo()) {
					exist = true;
				}
			}
			if (!exist) {
				temasNotaContable.add(temaActual);
			}
			if (temaActual.getCodigo() > 0) {
				temaActual.setEditada(true);
			}
			ajustarTotalesNota();
			temaActual = new NotaContableRegistroLibre();
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "El tema se ha guardado temporalmente");
			ocultarPopupTema = true;
			PrimeFaces.current().executeScript("PF('editorRegLibre').hide();");
		}
		return null;
	}

	private void ajustarTotalesNota() {
		LOGGER.info("{} Estructura y agrupa los montos totales de la Nota Contable");
		totalesNota = new ArrayList<>();
		for (NotaContableRegistroLibre tema : temasNotaContable) {
			if (!tema.getCodigoDivisa().equals("")) {
				boolean existeDivisa;
				String multiplo = tema.getNaturalezaCuentaContable().equalsIgnoreCase("D") ? "" : "-";
				existeDivisa = false;
				for (NotaContableTotal totalNota : totalesNota) {
					if (totalNota.getCodigoDivisa().equals(tema.getCodigoDivisa())) {
						existeDivisa = true;

						Double tot = Double.valueOf(multiplo + (Math.round(tema.getMonto() * 100)));
						totalNota.setTotal(((Math.round(totalNota.getTotal() * 100)) + tot) / 100);
						break;
					}
				}
				if (!existeDivisa) {
					NotaContableTotal totalNota = new NotaContableTotal();
					totalNota.setCodigoDivisa(tema.getCodigoDivisa());
					totalNota.setTotal(Double.valueOf(multiplo + (Math.round(tema.getMonto() * 100))) / 100);
					totalesNota.add(totalNota);
				}
			}
		}
	}

	private int getCuentaRara(String cuenta) {
		cuenta = cuenta.substring(0, 2);
		if (cuenta.startsWith("61") || cuenta.startsWith("62") || cuenta.startsWith("63") || cuenta.startsWith("64") || cuenta.startsWith("81") || cuenta.startsWith("82")
				|| cuenta.startsWith("83") || cuenta.startsWith("84")) {
			return Integer.valueOf(cuenta);
		}
		return 0;
	}

	/**
	 * Validaciones asociadas al tema actual
	 * 
	 * @return
	 */
	private boolean validarTema() {
		LOGGER.info("{} validación de campos requeridos");
		validador.validarRequerido(temaActual.getCodigoSucursalDestino(), "Sucursal de destino");
		validador.validarRequerido(temaActual.getReferencia(), "Referencia de cruce");
		boolean requiereContrato = temaActual.getHadtapl().getIndicadorContrato().equals("S");
		boolean requiereTercero = temaActual.getPucCuenta().getTipoApunte() != null && temaActual.getPucCuenta().getTipoApunte().equals("T")
				|| temaActual.getPucCuenta().getIndicadorTercero() != null && temaActual.getPucCuenta().getIndicadorTercero().equals("X");

		if (requiereContrato || requiereTercero) {
			validador.validarSeleccion(temaActual.getTipoIdentificacion(), "Tipo de documento del tercero");
			validador.validarRequerido(temaActual.getNumeroIdentificacion(), "número de documento del tercero");
			if (temaActual.getNombreCompleto().isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "La información del tercero es requerida");
			}
			if (requiereContrato) {
				validador.validarSeleccion(temaActual.getContrato(), "Contrato del tercero");
			}
		}

		for (MontoMaximo monto : montos) {
			if (monto.getDivisa().equals("1") && temaActual.getCodigoDivisa().equals("COP") && temaActual.getMonto() > monto.getMontoMaximo()
					|| !monto.getDivisa().equals("1") && !temaActual.getCodigoDivisa().equals("COP") && temaActual.getMonto() > monto.getMontoMaximo()) {
				NumberFormat f = NumberFormat.getCurrencyInstance();
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El importe supera el monto mximo autorizado (" + f.format(monto.getMontoMaximo()) + ")");
			}
		}

		validador.validarSeleccion(temaActual.getCodigoDivisa(), "Tipo de Divisa");
		validador.validarPositivo(temaActual.getMonto(), "Importe");

		if (temaActual.getPucCuenta().getIndicadorSIRO() != null && temaActual.getPucCuenta().getIndicadorSIRO().equals("T")
				&& temaActual.getRiesgoOperacional().getImporteParcial().doubleValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La cuenta contable requiere información de Riesgo Operacional. Por favor diligenciela");
		}

		if (getFacesContext().getMessageList().size() > 0) {
			ocultarPopupTema = false;
		}
		return !getFacesContext().getMessages().hasNext();
	}

	public String cancelarTema() {
		LOGGER.info("{} Se cancela la operacin del registro {}", temaActual.getCuentaContable());
		for (NotaContableRegistroLibre nct : temasNotaContable) {
			if (nct.getCodigo() == temaActual.getCodigo()) {
				if (nct.getCodigo() <= 0) {
					temasNotaContable.remove(temaActual);
				} else {
					codigo = nct.getCodigo();
					try {
						NotaContableRegistroLibre newReg = notasContablesManager.getRegistroLibreNotaContablePorNotaContableYCodigo(temaActual.getCodigoNotaContable(), codigo);
						nct.reset(newReg);
						verNota();
					} catch (Exception e) {
						LOGGER.error("{} Error al recuperar la Info. diligenciada del registro para la Nota Contable");
						nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al recuperar la información del registro");
					}
				}
				temaActual = new NotaContableRegistroLibre();
				ajustarTotalesNota();
				break;
			}
		}
		return null;
	}

	private synchronized int getNumArchivo() {
		return numArchivo++;
	}

	private synchronized int getNumRegistro() {
		return numRegistro--;
	}

	@Override
	public NotaContable getNota() {
		return nota;
	}

	@Override
	public void setNota(NotaContable nota) {
		this.nota = nota;
	}

	@Override
	public NotaContableRegistroLibre getTemaActual() {
		return temaActual;
	}

	@Override
	public void setTemaActual(NotaContableRegistroLibre temaActual) {
		this.temaActual = temaActual;
	}

	@Override
	public List<NotaContableRegistroLibre> getTemasNotaContable() {
		return temasNotaContable;
	}

	@Override
	public void setTemasNotaContable(List<NotaContableRegistroLibre> temasNotaContable) {
		this.temasNotaContable = temasNotaContable;
	}

	public List<SelectItem> getDivisas() {
		return divisas;
	}

	public void setDivisas(List<SelectItem> divisas) {
		this.divisas = divisas;
	}

	public List<SelectItem> getSucursalesPartida() {
		return sucursalesPartida;
	}

	public void setSucursalesPartida(List<SelectItem> sucursalesPartida) {
		this.sucursalesPartida = sucursalesPartida;
	}

	public List<SelectItem> getTiposDoc() {
		return tiposDoc;
	}

	public void setTiposDoc(List<SelectItem> tiposDoc) {
		this.tiposDoc = tiposDoc;
	}

	public List<SelectItem> getContratos1() {
		return contratos1;
	}

	public void setContratos1(List<SelectItem> contratos1) {
		this.contratos1 = contratos1;
	}

	public ArrayList<Date> getDiasNoHabiles() {
		return diasNoHabiles;
	}

	public void setDiasNoHabiles(ArrayList<Date> diasNoHabiles) {
		this.diasNoHabiles = diasNoHabiles;
	}

	public String getMinFecha() {
		return minFecha;
	}

	public void setMinFecha(String minFecha) {
		this.minFecha = minFecha;
	}

	public String getMaxFecha() {
		return maxFecha;
	}

	public void setMaxFecha(String maxFecha) {
		this.maxFecha = maxFecha;
	}

	public List<SelectItem> getClasesRiesgo() {
		return clasesRiesgo;
	}

	public void setClasesRiesgo(List<SelectItem> clasesRiesgo) {
		this.clasesRiesgo = clasesRiesgo;
	}

	public List<SelectItem> getPerdidaOper() {
		return perdidaOper;
	}

	public void setPerdidaOper(List<SelectItem> perdidaOper) {
		this.perdidaOper = perdidaOper;
	}

	public List<SelectItem> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<SelectItem> procesos) {
		this.procesos = procesos;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public List<SelectItem> getLineasOperativas() {
		return lineasOperativas;
	}

	public void setLineasOperativas(List<SelectItem> lineasOperativas) {
		this.lineasOperativas = lineasOperativas;
	}

	public List<Anexo> getAnexosTema() {
		return anexos;
	}

	public void setAnexosTema(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	@Override
	public ArrayList<NotaContableTotal> getTotalesNota() {
		return totalesNota;
	}

	@Override
	public void setTotalesNota(ArrayList<NotaContableTotal> totalesNota) {
		this.totalesNota = totalesNota;
	}

	public Date getFechaNota() {
		return fechaNota;
	}

	public void setFechaNota(Date fechaNota) {
		this.fechaNota = fechaNota;
	}

	public List<SelectItem> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<SelectItem> cuentas) {
		this.cuentas = cuentas;
	}

	public Collection<MontoMaximo> getMontos() {
		return montos;
	}

	public void setMontos(Collection<MontoMaximo> montos) {
		this.montos = montos;
	}

	public Double getMontoAlertaCOP() {
		return montoAlertaCOP;
	}

	public void setMontoAlertaCOP(Double montoAlertaCOP) {
		this.montoAlertaCOP = montoAlertaCOP;
	}

	public Double getMontoAlertaEXT() {
		return montoAlertaEXT;
	}

	public void setMontoAlertaEXT(Double montoAlertaEXT) {
		this.montoAlertaEXT = montoAlertaEXT;
	}

	/**
	 * @return the ocultarPopupTema
	 */
	public boolean isOcultarPopupTema() {
		return ocultarPopupTema;
	}

	/**
	 * @param ocultarPopupTema the ocultarPopupTema to set
	 */
	public void setOcultarPopupTema(boolean ocultarPopupTema) {
		this.ocultarPopupTema = ocultarPopupTema;
	}

	/**
	 * @return the ocultarPopupRiesgo
	 */
	public boolean isOcultarPopupRiesgo() {
		return ocultarPopupRiesgo;
	}

	/**
	 * @param ocultarPopupRiesgo the ocultarPopupRiesgo to set
	 */
	public void setOcultarPopupRiesgo(boolean ocultarPopupRiesgo) {
		this.ocultarPopupRiesgo = ocultarPopupRiesgo;
	}

	/**
	 * @return the ocultarPopupGuardarNota
	 */
	public boolean isOcultarPopupGuardarNota() {
		return ocultarPopupGuardarNota;
	}

	/**
	 * @param ocultarPopupGuardarNota the ocultarPopupGuardarNota to set
	 */
	public void setOcultarPopupGuardarNota(boolean ocultarPopupGuardarNota) {
		this.ocultarPopupGuardarNota = ocultarPopupGuardarNota;
	}

	/**
	 * @return the subProductos
	 */
	public List<SelectItem> getSubProductos() {
		return subProductos;
	}

	/**
	 * @param subProductos the subProductos to set
	 */
	public void setSubProductos(List<SelectItem> subProductos) {
		this.subProductos = subProductos;
	}

	/**
	 * @return the canales
	 */
	public List<SelectItem> getCanales() {
		return canales;
	}

	/**
	 * @param canales the canales to set
	 */
	public void setCanales(List<SelectItem> canales) {
		this.canales = canales;
	}

	/**
	 * @return the subcanales
	 */
	public List<SelectItem> getSubcanales() {
		return subcanales;
	}

	/**
	 * @param subcanales the subcanales to set
	 */
	public void setSubcanales(List<SelectItem> subcanales) {
		this.subcanales = subcanales;
	}

	/**
	 * @return the clasesRiesgoN2
	 */
	public List<SelectItem> getClasesRiesgoN2() {
		return clasesRiesgoN2;
	}

	/**
	 * @param clasesRiesgoN2 the clasesRiesgoN2 to set
	 */
	public void setClasesRiesgoN2(List<SelectItem> clasesRiesgoN2) {
		this.clasesRiesgoN2 = clasesRiesgoN2;
	}

	/**
	 * @return the clasesRiesgoN3
	 */
	public List<SelectItem> getClasesRiesgoN3() {
		return clasesRiesgoN3;
	}

	/**
	 * @param clasesRiesgoN3 the clasesRiesgoN3 to set
	 */
	public void setClasesRiesgoN3(List<SelectItem> clasesRiesgoN3) {
		this.clasesRiesgoN3 = clasesRiesgoN3;
	}

	/**
	 * @return the horas
	 */
	public List<SelectItem> getHoras() {
		return horas;
	}

	/**
	 * @param horas the horas to set
	 */
	public void setHoras(List<SelectItem> horas) {
		this.horas = horas;
	}

	/**
	 * @return the minutos
	 */
	public List<SelectItem> getMinutos() {
		return minutos;
	}

	/**
	 * @param minutos the minutos to set
	 */
	public void setMinutos(List<SelectItem> minutos) {
		this.minutos = minutos;
	}

	/**
	 * @return the horario
	 */
	public List<SelectItem> getHorario() {
		return horario;
	}

	/**
	 * @param horario the horario to set
	 */
	public void setHorario(List<SelectItem> horario) {
		this.horario = horario;
	}

	/**
	 * @return the aplicaRecuperacion
	 */
	public boolean isAplicaRecuperacion() {
		return aplicaRecuperacion;
	}

	/**
	 * @param aplicaRecuperacion the aplicaRecuperacion to set
	 */
	public void setAplicaRecuperacion(boolean aplicaRecuperacion) {
		this.aplicaRecuperacion = aplicaRecuperacion;
	}

}
