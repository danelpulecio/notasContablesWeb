package co.com.bbva.app.notas.contables.jsf.nota;

import co.com.bbva.app.notas.contables.carga.dto.PartidaPendiente;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.jsf.GeneralPage;
import co.com.bbva.app.notas.contables.jsf.IPages;
import co.com.bbva.app.notas.contables.jsf.adminnota.PendientePage;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.EMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad TipoEvento
 * </p>
 * 
 */
@SessionScoped
@Named
public class NotaRefCrucePage extends GeneralPage implements IPages, Serializable {

	
	private PendientePage pendientePage;

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(NotaRefCrucePage.class);
	private String param = "";
	private List<PartidaPendiente> cuentas = new ArrayList<>();
	private List<PartidaPendiente> partidasPendientes = new ArrayList<>();
	private List<PartidaPendiente> partidasSeleccionadas = new ArrayList<>();
	private Integer causalDevolucion = 0;
	private String otraCausalDev = "";
	private boolean chequeoReasignacion = false;
	private int usuariologueado = 0;
	private int usuarioinstancia = 0;
	private List<SelectItem> causalesDev = new ArrayList<>();
	// banderas para controlar el cierre de popups
	private boolean ocultarPopupAprobar = false;
	private boolean ocultarPopupAnular = false;
	//private Session session;
	protected final EMailSender enviarEMail;
	protected NotaContable nota = new NotaContable();
	protected ArrayList<NotaContableTotal> totalesNota = new ArrayList<>();
	private Integer scrollPage = 1;

	class CompPartPendPorCuenta implements Comparator<PartidaPendiente> {

		@Override
		public int compare(PartidaPendiente p1, PartidaPendiente p2) {
			return p1.getCuenta().compareTo(p2.getCuenta());
		}

	}

	public NotaRefCrucePage() {
		super();
		enviarEMail = new EMailSender();
	}

	@Override
	public void _init() {
	}

	public String iniciarPagina() {
//		session = getContablesSessionBean().getSessionTrace();
		try {
			LOGGER.info("{} Registro de NOTA CONTABLE REF. CRUCE - Configurando valores iniciales");
			String codSucursal = getUsuarioLogueado().getUsuario().getCodigoAreaModificado();
			LOGGER.info("{} Obteniendo Info. de la sucursal {}", codSucursal);
			String msg = notasContablesManager.verificarUsuarioSubGerente(codSucursal);
			if (msg.isEmpty()) {
				LOGGER.info("{} Consultando cuentas de partidas pendientes para la sucursal {}", codSucursal);
				cuentas = new ArrayList<>(cargaAltamiraManager.getPartidasPendientesCuentasPorSucursal(codSucursal));
				if (cuentas.isEmpty()) {
					LOGGER.info("{} No se encontr información de cuentas" );
					nuevoMensaje(FacesMessage.SEVERITY_INFO, "Actualmente no hay cuentas para cruzar.");
				} else {
					LOGGER.info("{} Se encontraron {} cuentas", cuentas.size());
					Collections.sort(cuentas, new CompPartPendPorCuenta());
				}
			} else {
				LOGGER.warn("{} No se tiene parametrizado un autorizador");
				nuevoMensaje(FacesMessage.SEVERITY_WARN, msg);
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al intentar al configurar los valores iniciales: NC REF. CRUCE", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema, la aplicación no se pudo iniciar correctamente");
		}
		return null;
	}

	/**
	 * Funcion llamada al iniciar el flujo de creacion de notas contables
	 * 
	 * @return
	 */
	public String iniciar() {
		return NOTA_REF_CRUCE;
	}

	public String buscarFiltro() {
		try {
			LOGGER.info("{} Realizando la busqueda de Partidas Pendientes");
			boolean filtro = false;
			for (PartidaPendiente c : cuentas) {
				if (c.getSeleccionada()) {
					filtro = true;
					break;
				}
			}
			partidasPendientes = new ArrayList<>();
			LOGGER.info("{} Obteniendo Info. de Partidas Pendientes - Filtro: {}", param);
			Collection<PartidaPendiente> partidas = cargaAltamiraManager.searchPartidaPendientePorSucursal(getUsuarioLogueado().getUsuario().getCodigoAreaModificado(), param);
			if (!partidas.isEmpty()) {
				for (PartidaPendiente p : partidas) {
					if (!partidasSeleccionadas.contains(p)) {
						if (!filtro) {
							partidasPendientes.add(p);
						} else {
							for (PartidaPendiente c : cuentas) {
								if (c.getSeleccionada() && c.getCuenta().equals(p.getCuenta())) {
									partidasPendientes.add(p);
									break;
								}
							}
						}
					}
				}
			}
			LOGGER.info("{} Hay {} partidas pendientes disponibles", partidasPendientes.size());
			Collections.sort(partidasPendientes);
		} catch (Exception e) {
			LOGGER.error("{} Error intentando consultar las partidas pendientes", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema consultando las partidas pendientes");
		}
		return null;
	}

	public String seleccionar() {
		LOGGER.info("{} Se ha seleccionado la(s) cuenta(s)");
		for (PartidaPendiente p : cuentas) {
			p.setSeleccionada(!p.getSeleccionada());
		}
		return null;
	}

	public String adicionar() {
		try {
			LOGGER.info("{} Se ha selecionado una Partida Pendiente");
			for (PartidaPendiente p : partidasPendientes) {
				if (p.getSeleccionada()) {
					LOGGER.info("{} Ajuste de las partidas pendientes disponibles");
					partidasPendientes.remove(p);
					p.setUsada("S");
					p.setSeleccionada(false);
					LOGGER.info("{} Se agrega a la Nota Contable la partida seleccionada: {}");
					partidasSeleccionadas.add(p);
					break;
				}
			}
			Collections.sort(partidasSeleccionadas);
			ajustarTotalesNota();
		} catch (Exception e) {
			LOGGER.error("{} Error al agregar una partida pendiente a la Nota Contable",  e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al agregar la partida seleccionada");
		}
		return null;
	}

	public String remover() {
		try {
			LOGGER.info("{} Se ha eliminado una Partida Pendiente de las seleccionadas");
			for (PartidaPendiente p : partidasSeleccionadas) {
				if (p.getSeleccionada()) {
					LOGGER.info("{} Se elimina la partida seleccionada: {}", p.getConcepto());
					partidasSeleccionadas.remove(p);
					p.setUsada("N");
					p.setSeleccionada(false);
					LOGGER.info("{} Ajuste de las partidas pendientes disponibles");
					partidasPendientes.add(p);
					break;
				}
			}
			ajustarTotalesNota();
		} catch (Exception e) {
			LOGGER.error("{} Error al eliminar una partida pendiente en la Nota Contable", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al eliminar la partida seleccionada");
		}
		return null;
	}

	private void ajustarTotalesNota() {
		LOGGER.info("{} Estructura y agrupa los montos totales por divisa");
		totalesNota = new ArrayList<>();
		for (PartidaPendiente p : partidasSeleccionadas) {
			String negativo = p.getNaturaleza().equalsIgnoreCase("D") ? "" : "-";
			boolean existeDivisa = false;
			for (NotaContableTotal totalNota : totalesNota) {
				if (totalNota.getCodigoDivisa().equals(p.getDivisa())) {
					existeDivisa = true;
					Double tot = Double.parseDouble(negativo + (Math.round(p.getImporte() * 100)));
					totalNota.setTotal(Double.valueOf((Math.round(totalNota.getTotal() * 100)) + tot) / 100);
					break;
				}
			}
			if (!existeDivisa) {
				NotaContableTotal totalNota = new NotaContableTotal();
				totalNota.setCodigoDivisa(p.getDivisa());
				totalNota.setTotal(Double.valueOf(negativo + (Math.round(p.getImporte() * 100))) / 100);
				totalesNota.add(totalNota);
			}
		}
	}

	public boolean validarRegistros() {
		LOGGER.info("{} validación de campos requeridos");
		for (NotaContableTotal total : totalesNota) {
			if (total.getTotal() != 0) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "El total para la divisa " + total.getCodigoDivisa() + " debe sumar cero (0)");
			}
		}
		if (partidasSeleccionadas.isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha seleccionado ninguna partida contable para registrar la Nota Contable");
		}
		return !getFacesContext().getMessages().hasNext();
	}

	public String guardar() {
		try {
			LOGGER.info("{} Intentando grabar la Nota Contable Ref. Cruce");
			LOGGER.info("{} Procesando validaciones y Persistiendo Info.");
			if (validarRegistros()) {
				if (nota.getCodigo().intValue() <= 0) {
					nota.setCodigoSucursalOrigen(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
					nota.setCodigoTipoEvento(0);
					nota.setTipoNota("C");
					LOGGER.info("{} Creando registro de la Instancia en base de datos");
					int idInstancia = notasContablesManager.crearInstanciaNotaContable(nota, new ArrayList<>(), new ArrayList<>(),
							new ArrayList<>(), new ArrayList<>(), partidasSeleccionadas, getCodUsuarioLogueado());
					if (idInstancia != 0) {
						Instancia instancia = new Instancia();
						instancia.setCodigo(idInstancia);
						try {
							instancia = notasContablesManager.getInstancia(instancia);

							NotaContable notaContable = new NotaContable();
							notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
							notaContable = notasContablesManager.getNotaContable(notaContable);
							LOGGER.info("{} El registro de la NOTA CONTABLE REF. CRUCE: {} fue exitoso", notaContable.getNumeroRadicacion());

							int codigoUsuarioAsignado = instancia.getCodigoUsuarioActual().intValue();
							UsuarioModulo usuarioModulo = new UsuarioModulo();
							usuarioModulo.setCodigo(codigoUsuarioAsignado);
							usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
							cancelar();
							nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido registrada correctamente con el nmero de radicacin " + notaContable.getNumeroRadicacion());
							try {
								LOGGER.info("{} Intentando enviar email de la nota grabada");
								enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
										"Mdulo Notas Contables - Registro para aprobar",
										"Por favor ingrese al mdulo de Notas Contables, se le ha asignado un registro que requiere su verificacin");
							} catch (Exception e) {
								LOGGER.error("{} Error al intentar enviar el email de la nota (L) grabada", e);
								nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al enviar el correo electronico a " + usuarioModulo.getEMailModificado());
							}
						} catch (Exception e) {
							LOGGER.error("{} Error al obtener la Info. de la nota (C) grabada", e);
						}
					} else {
						LOGGER.error("{} Error al crear la instancia de la Nota Contable Ref. Cruce");
						nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema no fue posible crear la nota contable");					}
				} else {
					LOGGER.error("{} Error la nota contable ya ha sido registrada - Codigo: {}", nota.getCodigo().intValue());
					nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: La información de la nota ya ha sido registrada anteriormente");
				}
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al intentar grabar la Nota Contable Ref. Cruce", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al guardar la nota contable");
		}
		return null;
	}

	public String cancelar() {
		LOGGER.info("{} ... Restableciendo valores ...");
		partidasSeleccionadas = new ArrayList<>();
		totalesNota = new ArrayList<>();
		nota = new NotaContable();
		buscarFiltro();
		return null;
	}

	public boolean isMostrarAsiento() {
		try {
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
			return instancia.getEstado() != null && instancia.getEstado().trim().equals("6");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void consultarFlujo() {
		try {
//			session = getContablesSessionBean().getSessionTrace();
			Integer codUsuAsignado = nota.getCodUsuAsignado().intValue();
			nota = notasContablesManager.getNotaContable(nota);
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			if (codUsuAsignado > 0 && !instancia.getEstado().equals("4") && !instancia.getEstado().equals("5") && !instancia.getEstado().equals("9")) {
				nota.setPuedeAprobar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
			}
			if (codUsuAsignado > 0 && !instancia.getEstado().equals("1") && !instancia.getEstado().equals("9")) {
				nota.setPuedeRechazar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
				causalesDev = getSelectItemList(notasContablesManager.getCV(CausalDevolucion.class), false);
			}
			if (codUsuAsignado > 0 && instancia.getEstado().equals("1")) {
				nota.setPuedeAnular(true);
			}
			partidasSeleccionadas = new ArrayList<>();
			Collection<NotaContableCrucePartidaPendiente> temasNota = new ArrayList<>(
					notasContablesManager.getCrucesPartidasPendientesPorNotaContable(nota.getCodigo().intValue()));
			for (NotaContableCrucePartidaPendiente n : temasNota) {
				PartidaPendiente p = new PartidaPendiente();
				p.setCuenta(n.getCuentaContable());
				p.setSucursalDestino(n.getCodigoSucursalDestino());
				p.setNaturaleza(n.getNaturaleza());
				p.setDivisa(n.getDivisa());
				p.setReferenciaCruce(n.getReferenciaCruce());
				p.setImporte(n.getImporte());
				p.setConcepto(n.getConcepto());
				p.setFechaContable(n.getFechaContable());
				p.setNumeroAsiento(n.getNumeroAsiento());
				partidasSeleccionadas.add(p);
			}
			ajustarTotalesNota();
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "No se ha podido completar la ejecucin solicitada");
		}
	}

	public String aprobar() {
		try {

			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), true, 0);

			// Codigo nuevo
			// Para Resolver incidencia que ocurre cuando una
			// Nota Contable Es Aprobada en una misma sesin; en diferentes Navegadores Web
			// lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no se encuentren en Altamira
			this.chequeoReasignacion = true;
			this.usuariologueado = getCodUsuarioLogueado();
			this.usuarioinstancia = instancia.getCodigoUsuarioActual().intValue();
			if (getCodUsuarioLogueado() == instancia.getCodigoUsuarioActual().intValue()) {
				chequeoReasignacion = true;

			} else {
				chequeoReasignacion = false;
			} // fin codigo

			if (errorMessage.equals("")) {

				int codigoUsuarioAsignado = 0;

				int evaluoActividad = notasContablesManager.siguienteActividad(instancia, new ArrayList<>(), new ArrayList<>(),
						getCodUsuarioLogueado(), true, 0, null, chequeoReasignacion);
				if (evaluoActividad != -1) {
					codigoUsuarioAsignado = evaluoActividad;
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_INFO,
							"Se present un error al aprobar la nota: Verfique que el Aplicativo Notas Contables est  abierto en  un nico navegador Web y en una nica pestaa.  ");

					return null;
				}

				UsuarioModulo usuarioModulo = new UsuarioModulo();
				usuarioModulo.setCodigo(codigoUsuarioAsignado);
				usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
				nota.setPuedeAprobar(false);
				nota.setPuedeRechazar(false);
				nota.setPuedeEditar(false);
				nota.setPuedeAnular(false);
				// se cargan los pendientes
				pendientePage.cargarPendientes();
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido aprobada correctamente");
				ocultarPopupAprobar = true;
				try {
					enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
							"Mdulo Notas Contables - Registro para aprobar",
							"Por favor ingrese al mdulo de Notas Contables, se le ha asignado un registro que requiere su verificacin");
				} catch (Exception e) {
					nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se present un error al enviar el correo a la direccin: " + usuarioModulo.getEMailModificado());
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, errorMessage);
				ocultarPopupAprobar = false;
			}
		} catch (Exception e) {
			lanzarError(e, "Se present un error al aprobar la nota contable");
		}
		return null;
	}

	public String rechazar() {
		try {
			Instancia instanciado = new Instancia();
			instanciado.setCodigoNotaContable(nota.getCodigo());
			instanciado = notasContablesManager.getInstanciaPorNotaContable(instanciado);

			// Codigo nuevo
			// Para Resolver incidencia que ocurre cuando una
			// Nota Contable Es Aprobada en una misma sesin; en diferentes Navegadores Web
			// lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no se encuentren en Altamira
			this.chequeoReasignacion = true;
			this.usuariologueado = getCodUsuarioLogueado();
			this.usuarioinstancia = instanciado.getCodigoUsuarioActual().intValue();
			if (getCodUsuarioLogueado() == instanciado.getCodigoUsuarioActual().intValue()) {
				chequeoReasignacion = true;

			} else {
				chequeoReasignacion = false;
			} // fin codigo
			if (chequeoReasignacion) {
				validador.validarSeleccion(causalDevolucion, "Causal de devolucion");
				if (causalDevolucion == 18) {// causal: otra
					validador.validarRequerido(otraCausalDev, "Descripcin de la causal");
				}
				// si no hay errores de validacion
				if (!getFacesContext().getMessages().hasNext()) {
					Instancia instancia = new Instancia();
					instancia.setCodigoNotaContable(nota.getCodigo());
					instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

					String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), false, causalDevolucion);

					if (errorMessage.equals("")) {
						int codigoUsuarioAsignado = notasContablesManager.siguienteActividad(instancia, new ArrayList<>(), new ArrayList<>(),
								getCodUsuarioLogueado(), false, causalDevolucion, otraCausalDev);

						UsuarioModulo usuarioModulo = new UsuarioModulo();
						usuarioModulo.setCodigo(codigoUsuarioAsignado);
						usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
						// se cargan los pendientes
						nota.setPuedeAprobar(false);
						nota.setPuedeRechazar(false);
						nota.setPuedeEditar(false);
						nota.setPuedeAnular(false);
						pendientePage.cargarPendientes();
						nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido rechazada correctamente");
						ocultarPopupAnular = true;
						try {
							enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
									"Mdulo Notas Contables - Registro rechazado",
									"Por favor ingrese al mdulo de Notas Contables, se le ha asignado un registro que requiere su verificacin");
						} catch (Exception e) {
							nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se present un error al enviar el correo a la direccin: " + usuarioModulo.getEMailModificado());
							
						}
					} else {
						nuevoMensaje(FacesMessage.SEVERITY_INFO, errorMessage);
						ocultarPopupAnular = false;
					}
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO,
						"Se present un error al rechazar la nota: Verfique que el Aplicativo Notas Contables /n est  abierto en  un nico navegador Web y en una nica pestaa. ");
				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se present un error al rechazar la nota contable");
		}
		return null;
	}

	public String anular() {
		try {

			// Codigo nuevo
			Instancia instanciado = new Instancia();
			instanciado.setCodigoNotaContable(nota.getCodigo());
			instanciado = notasContablesManager.getInstanciaPorNotaContable(instanciado);

			// Para Resolver incidencia que ocurre cuando una
			// Nota Contable Es Aprobada en una misma sesin; en diferentes Navegadores Web
			// lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no se encuentren en Altamira
			this.chequeoReasignacion = true;
			this.usuariologueado = getCodUsuarioLogueado();
			this.usuarioinstancia = instanciado.getCodigoUsuarioActual().intValue();
			if (getCodUsuarioLogueado() == instanciado.getCodigoUsuarioActual().intValue()) {
				chequeoReasignacion = true;

			} else {
				chequeoReasignacion = false;
			} // fin codigo
			if (chequeoReasignacion) {
				Instancia instancia = new Instancia();
				instancia.setCodigoNotaContable(nota.getCodigo());
				instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

				notasContablesManager.anularNotaContable(instancia, getCodUsuarioLogueado());
				nota.setPuedeAprobar(false);
				nota.setPuedeRechazar(false);
				nota.setPuedeEditar(false);
				nota.setPuedeAnular(false);

				pendientePage.cargarPendientes();
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido anulada correctamente");

				ocultarPopupAnular = true;
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO,
						"Se present un error al anular la nota: Verfique que el Aplicativo Notas Contables /n est  abierto en  un nico navegador Web y en una nica pestaa. ");
				ocultarPopupAnular = false;

				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se present un error al anular la nota contable");
		}
		return null;
	}

	public NotaContable getNota() {
		return nota;
	}

	public void setNota(NotaContable nota) {
		this.nota = nota;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Collection<PartidaPendiente> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<PartidaPendiente> cuentas) {
		this.cuentas = cuentas;
	}

	public List<PartidaPendiente> getPartidasPendientes() {
		return partidasPendientes;
	}

	public void setPartidasPendientes(List<PartidaPendiente> partidasPendientes) {
		this.partidasPendientes = partidasPendientes;
	}

	public List<PartidaPendiente> getPartidasSeleccionadas() {
		return partidasSeleccionadas;
	}

	public void setPartidasSeleccionadas(List<PartidaPendiente> partidasSeleccionadas) {
		this.partidasSeleccionadas = partidasSeleccionadas;
	}

	public Integer getScrollPage() {
		if (scrollPage == null) {
			scrollPage = 1;
		}
		return scrollPage;
	}

	public void setScrollPage(Integer srollPage) {
		this.scrollPage = srollPage;
	}

	public int getTotalFilas() {
		return partidasPendientes == null ? 0 : partidasPendientes.size();
	}

	public ArrayList<NotaContableTotal> getTotalesNota() {
		return totalesNota;
	}

	public void setTotalesNota(ArrayList<NotaContableTotal> totalesNota) {
		this.totalesNota = totalesNota;
	}

	public Integer getCausalDevolucion() {
		return causalDevolucion;
	}

	public void setCausalDevolucion(Integer causalDevolucion) {
		this.causalDevolucion = causalDevolucion;
	}

	public String getOtraCausalDev() {
		return otraCausalDev;
	}

	public void setOtraCausalDev(String otraCausalDev) {
		this.otraCausalDev = otraCausalDev;
	}

	public List<SelectItem> getCausalesDev() {
		return causalesDev;
	}

	public void setCausalesDev(List<SelectItem> causalesDev) {
		this.causalesDev = causalesDev;
	}

	/**
	 * @return the ocultarPopupAprobar
	 */
	public boolean isOcultarPopupAprobar() {
		return ocultarPopupAprobar;
	}

	/**
	 * @param ocultarPopupAprobar the ocultarPopupAprobar to set
	 */
	public void setOcultarPopupAprobar(boolean ocultarPopupAprobar) {
		this.ocultarPopupAprobar = ocultarPopupAprobar;
	}

	/**
	 * @return the ocultarPopupAnular
	 */
	public boolean isOcultarPopupAnular() {
		return ocultarPopupAnular;
	}

	/**
	 * @param ocultarPopupAnular the ocultarPopupAnular to set
	 */
	public void setOcultarPopupAnular(boolean ocultarPopupAnular) {
		this.ocultarPopupAnular = ocultarPopupAnular;
	}

}
