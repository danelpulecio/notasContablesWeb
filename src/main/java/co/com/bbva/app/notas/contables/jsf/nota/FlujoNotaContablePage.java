package co.com.bbva.app.notas.contables.jsf.nota;

import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.carga.dto.TipoIdentificacion;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.facade.NotasContablesSession;
import co.com.bbva.app.notas.contables.jsf.GeneralPage;
import co.com.bbva.app.notas.contables.jsf.IPages;
import co.com.bbva.app.notas.contables.jsf.adminnota.PendientePage;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.EMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la
 * entidad TipoEvento
 * </p>
 * 
 */
@SessionScoped
@Named
public class FlujoNotaContablePage extends GeneralPage implements IPages {

	
	private PendientePage pendientePage;

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FlujoNotaContablePage.class);
//	private Session session = getContablesSessionBean().getSessionTrace();
	protected final EMailSender enviarEMail;
	protected NotaContable nota = new NotaContable();
	protected NotaContableTema temaActual = new NotaContableTema();
	protected List<NotaContableTema> temasNotaContable = new ArrayList<>();
	protected ArrayList<NotaContableTotal> totalesNota = new ArrayList<NotaContableTotal>();
	protected ArrayList<Anexo> anexos = new ArrayList<Anexo>();
	private Integer causalDevolucion = 0;
	private String otraCausalDev = "";
	private String numeroRadicacion;
	private boolean chequeoReasignacion = false;
	private int usuariologueado = 0;
	private int usuarioinstancia = 0;
	private List<SelectItem> causalesDev = new ArrayList<SelectItem>();
	private boolean ocultarPopupAprobar = false;
	private boolean ocultarPopupAnular = false;

	public FlujoNotaContablePage() {
		super();
		enviarEMail = new EMailSender();
	}

	@Override
	public void _init() {
	}

	public void consultarFlujo() {
		try {
			Integer codUsuAsignado = nota.getCodUsuAsignado().intValue();
			nota = notasContablesManager.getNotaContable(nota);
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			if (codUsuAsignado > 0 && !instancia.getEstado().equals("4") && !instancia.getEstado().equals("5") && !instancia.getEstado().equals("9")) {
				nota.setPuedeAprobar(getCodUsuarioLogueado() == codUsuAsignado);
			}
			if (codUsuAsignado > 0 && !instancia.getEstado().equals("1") && !instancia.getEstado().equals("9")) {
				nota.setPuedeRechazar(getCodUsuarioLogueado() == codUsuAsignado);
				causalesDev = getSelectItemList(notasContablesManager.getCV(CausalDevolucion.class), false);
			}
			totalesNota = new ArrayList<>(notasContablesManager.getTotalesPorNotaContable(nota.getCodigo().intValue()));
			iniciarConcepto();
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "No se ha podido completar la ejecucin solicitada ");
		}
	}

	/**
	 * Funcion llamada al iniciar el flujo de creacion de notas contables
	 * 
	 * @return
	 */
	public String iniciar() {
		return NOTA_CONTABLE;
	}

	private void iniciarConcepto() {
		try {
			Tema tema = new Tema();
			tema.setCodigoConcepto(nota.getCodigoConcepto());
			tema.setEstado("A");

			Collection<Tema> temas = notasContablesManager.getTemasPorConceptoYEstado(tema);
			temasNotaContable = new ArrayList<NotaContableTema>(
					notasContablesManager.getTemaNotaContablePorConceptoEstadoYTema(nota.getCodigoConcepto().intValue(), "A", nota.getCodigo().intValue()));
			for (Tema t : temas) {
				boolean contains = false;
				for (NotaContableTema nct : temasNotaContable) {
					if (nct.getTema().getCodigo().intValue() == t.getCodigo().intValue()) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					NotaContableTema nct1 = new NotaContableTema();
					nct1.setTema(t);
					temasNotaContable.add(nct1);
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Error al consultar los temas del concepto indicado");
		}
	}

	public String verNota() {
		try {
			// temaActual = notasContablesManager.getTemaNotaContablePorCodigo(temaActual);
			for (NotaContableTema nct : temasNotaContable) {
				if (nct.getCodigo().intValue() == codigo.intValue()) {
					temaActual = nct;
					temaActual.setTema(notasContablesManager.getTema(temaActual.getTema()));
					if (codigo.intValue() > 0) {
						// se consultan los nombres del tipo deidentificacion
						TipoIdentificacion tipoIdentificacion = new TipoIdentificacion();
						if (!temaActual.getNombreCompleto1().isEmpty()) {
							tipoIdentificacion.setCodigo(temaActual.getTipoIdentificacion1());
							temaActual.setNombreTipoDoc1(cargaAltamiraManager.getTipoIdentificacion(tipoIdentificacion).getNombre());
						}
						if (!temaActual.getNombreCompleto2().isEmpty()) {
							tipoIdentificacion.setCodigo(temaActual.getTipoIdentificacion2());
							temaActual.setNombreTipoDoc2(cargaAltamiraManager.getTipoIdentificacion(tipoIdentificacion).getNombre());
						}
						Sucursal sucursal = new Sucursal();
						sucursal.setCodigo(temaActual.getCodigoSucursalDestinoPartida());
						temaActual.setSucursalDestinoPartida(cargaAltamiraManager.getSucursal(sucursal));
						sucursal.setCodigo(temaActual.getCodigoSucursalDestinoContraPartida());
						temaActual.setSucursalDestinoContraPartida(cargaAltamiraManager.getSucursal(sucursal));

						temaActual.setRiesgoOperacional(
								notasContablesManager.getRiesgoPorNotaContableYTemaNotaContable(nota.getCodigo().intValue(), temaActual.getCodigo().intValue()));
						Anexo an = new Anexo();
						an.setCodigoTema(temaActual.getCodigo().intValue());
						// gp12833 - aseguramiento anexos
						temaActual.setAnexoTema(new ArrayList<Anexo>(notasContablesManager.getAnexosPorTemaORadicado(an, nota.getNumeroRadicacion())));
						// temaActual.setAnexoTema(new
						// ArrayList<Anexo>(notasContablesManager.getAnexosPorTema(an,
						// nota.getNumeroRadicacion())));
						// fin gp12833 - aseguramiento anexos
					}
					temaActual.setImpuestoTema(new ArrayList<NotaContableTemaImpuesto>());
					Collection<TemaImpuesto> impuestosTema = notasContablesManager.getImpuestosPorTema(temaActual.getTema().getCodigo().intValue());
					for (TemaImpuesto impuestoTema : impuestosTema) {
						// se consulta el valor del impuesto
						Impuesto impuesto = new Impuesto();
						impuesto.setCodigo(impuestoTema.getCodigoImpuesto());
						impuesto = notasContablesManager.getImpuesto(impuesto);

						impuestoTema.setImpuesto(impuesto);
						NotaContableTemaImpuesto imp = new NotaContableTemaImpuesto();
						if (codigo.intValue() > 0) {
							imp = notasContablesManager.getImpuestoPorNotaContableYTemaNotaContableYImpuesto(nota.getCodigo().intValue(), temaActual.getCodigo().intValue(),
									impuestoTema.getCodigoImpuesto());
						}
						imp.setImpuestoTema(impuestoTema);
						imp.setBoolExonera(imp.getExonera().equalsIgnoreCase("S"));
						temaActual.getImpuestoTema().add(imp);
					}
					// se verifica la partida y contra partida
					PUC partidaContable = new PUC();
					partidaContable.setNumeroCuenta(temaActual.getTema().getPartidaContable());
					partidaContable = cargaAltamiraManager.getPUC(partidaContable);
					temaActual.getTema().setPucPartida(partidaContable);

					PUC contraPartidaContable = new PUC();
					contraPartidaContable.setNumeroCuenta(temaActual.getTema().getContraPartidaContable());
					contraPartidaContable = cargaAltamiraManager.getPUC(contraPartidaContable);
					temaActual.getTema().setPucContraPartida(contraPartidaContable);
					break;
				}
			}
		} catch (Exception e) {
			lanzarError(e, "Error al iniciar el editor de tema");
		}
		return null;
	}

	public String aprobar() {
		try {
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), true, 0);
			// Codigo nuevo
			// Para Resolver incidencia que ocurre cuando una
			// Nota Contable Es Aprobada en una misma sesión; en diferentes Navegadores Web
			// lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no
			// se encuentren en Altamira
			this.chequeoReasignacion = true;
			this.usuariologueado = getCodUsuarioLogueado();
			this.usuarioinstancia = instancia.getCodigoUsuarioActual().intValue();
			if (getCodUsuarioLogueado() == instancia.getCodigoUsuarioActual().intValue()) {
				chequeoReasignacion = true;

			} else {
				chequeoReasignacion = false;
			} // fin codigo

			if (errorMessage.equals("")) {
				for (NotaContableTema tema : temasNotaContable) {
					temaActual = tema;
					// se carga la informacion del tema
					verNota();
				}
				int codigoUsuarioAsignado = 0;

				int evaluoActividad = notasContablesManager.siguienteActividad(instancia, temasNotaContable, totalesNota, getCodUsuarioLogueado(), true, 0, null,
						chequeoReasignacion);
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
			// Nota Contable Es Aprobada en una misma sesión; en diferentes Navegadores Web
			// lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no
			// se encuentren en Altamira
			this.chequeoReasignacion = true;
			this.usuariologueado = getCodUsuarioLogueado();
			this.usuarioinstancia = instanciado.getCodigoUsuarioActual().intValue();
			if (getCodUsuarioLogueado() == instanciado.getCodigoUsuarioActual().intValue()) {
				chequeoReasignacion = true;

			} else {
				chequeoReasignacion = false;
			} // fin codigo

			if (chequeoReasignacion) {
				validador.validarSeleccion(causalDevolucion, "Causal de devolución");
				if (causalDevolucion == 18) {// causal: otra
					validador.validarRequerido(otraCausalDev, "Descripción de la causal");
				}
				// si no hay errores de validacion
				if (!getFacesContext().getMessages().hasNext()) {
					Instancia instancia = new Instancia();
					instancia.setCodigoNotaContable(nota.getCodigo());
					instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

					String errorMessage = notasContablesManager.verificarUsuarioSiguienteActividad(instancia, getCodUsuarioLogueado(), false, causalDevolucion);

					if (errorMessage.equals("")) {
						for (NotaContableTema tema : temasNotaContable) {
							temaActual = tema;
							codigo = temaActual.getCodigo().intValue();
							// se carga la informacion del tema
							verNota();
						}
						int codigoUsuarioAsignado = notasContablesManager.siguienteActividad(instancia, temasNotaContable, totalesNota, getCodUsuarioLogueado(), false,
								causalDevolucion, otraCausalDev);

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
						causalDevolucion = 0;
						otraCausalDev = "";
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
				ocultarPopupAnular = false;
				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se present un error al rechazar la nota contable");
		}
		return null;
	}

	public String anular() {
		try {

			Instancia instanciado = new Instancia();
			instanciado.setCodigoNotaContable(nota.getCodigo());
			instanciado = notasContablesManager.getInstanciaPorNotaContable(instanciado);

			// Codigo nuevo
			// Para Resolver incidencia que ocurre cuando una
			// Nota Contable Es Aprobada en una misma sesión; en diferentes Navegadores Web
			// lo que ocasionaba que se saltara de Estado; y subsequentemente que notas no
			// se encuentren en Altamira
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
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido anulada correctamente");
				pendientePage.cargarPendientes();

			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO,
						"Se present un error al anular la nota: Verfique que el Aplicativo Notas Contables /n est  abierto en  un nico navegador Web y en una nica pestaa. ");
				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se present un error al anular la nota contable");
		}
		return null;
	}

	public String cargarAnexos() {
		try {
			anexos = new ArrayList<Anexo>(notasContablesManager.findByNumeroRadicacion(numeroRadicacion));
		} catch (Exception e) {
			lanzarError(e, "Error al consultar el número de radicado.");
		}
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

	public boolean isMostrarAsientoImpuesto() {
		try {
			Instancia instancia = new Instancia();
			instancia.setCodigoNotaContable(temaActual.getCodigoNotaContable());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
			return instancia.getEstado() != null && instancia.getEstado().trim().equals("6");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	protected void crearNotaContableTema(NotaContableTema temaNota, Tema tema, Integer cod, String maxFecha) throws Exception {
		LOGGER.info("{} Estableciendo las propiedades del tema: {}", tema.getNombre());
		temaNota.setCodigo(cod--);
		temaNota.setTema(tema);
		temaNota.setCodigoTema(tema.getCodigo().intValue());
		temaNota.setObligatorio(tema.getObligatorio().equals("S"));
		temaNota.setFechaContable(new java.sql.Date(DateUtils.getDate(maxFecha, "dd-MM-yyyy").getTime()));
		temaNota.setPartidaContable(tema.getPartidaContable());
		temaNota.setNaturalezaPartidaContable(tema.getNaturaleza1());
		temaNota.setContrapartidaContable(tema.getContraPartidaContable());
		temaNota.setNaturalezaContrapartidaContable(tema.getNaturaleza2());
		LOGGER.info("{} Obteniendo Info. de los impuestos para el tema");
		ArrayList<NotaContableTemaImpuesto> impTemaActual = new ArrayList<>();
		Collection<TemaImpuesto> listaImpTema = notasContablesManager.getImpuestosPorTema(tema.getCodigo().intValue());
		LOGGER.info("{} Procesando y definiendo el impuesto");
		for (TemaImpuesto impuestoTema : listaImpTema) {
			Impuesto impuesto = new Impuesto();
			impuesto.setCodigo(impuestoTema.getCodigoImpuesto());
			impuesto = notasContablesManager.getImpuesto(impuesto);
			impuestoTema.setImpuesto(impuesto);

			NotaContableTemaImpuesto impuestoTNC = new NotaContableTemaImpuesto();
			impuestoTNC.setCodigoImpuesto(impuestoTema.getCodigoImpuesto());
			impuestoTNC.setExonera("N");
			impuestoTNC.setBoolExonera(false);
			impuestoTNC.setImpuestoTema(impuestoTema);
			impTemaActual.add(impuestoTNC);
		}
		temaNota.setImpuestoTema(impTemaActual);

		LOGGER.info("{} Estableciendo valores iniciales de la PARTIDA");
		PUC partidaContable = new PUC();
		partidaContable.setNumeroCuenta(tema.getPartidaContable());
		partidaContable = cargaAltamiraManager.getPUC(partidaContable);
		tema.setPucPartida(partidaContable);

		LOGGER.info("{} Estableciendo valores iniciales de la CONTRAPARTIDA");
		PUC contraPartidaContable = new PUC();
		contraPartidaContable.setNumeroCuenta(tema.getContraPartidaContable());
		contraPartidaContable = cargaAltamiraManager.getPUC(contraPartidaContable);
		tema.setPucContraPartida(contraPartidaContable);

		Sucursal sucursal = getUsuarioLogueado().getSucursal();
		LOGGER.info("{} Se imprime valores de usuario Logueado antes del despliegue de Temas al momento de grabar notas temas sucursal {} ",getUsuarioLogueado().getSucursal().getCodigo());
		LOGGER.info("{} Se imprime valores de usuario Logueado antes del despliegue de Temas al momento de grabar notas temas usuario codigo area modificado {} ",getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
		LOGGER.info("{} Se imprime valores de usuario Logueado antes del despliegue de Temas al momento de grabar notas temas get usuario codigo empleado  {} ", getUsuarioLogueado().getUsuario().getCodigoEmpleado());

		temaNota.setAutorizada(true);
		LOGGER.info("{} Validando la PARTIDA y CONTRAPARTIDA contra el PUC");
		if (!cargaAltamiraManager.isSucursalValidaPUCOrigen(sucursal, partidaContable)) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN,
					"La partida contable del tema (" + tema.getNombre() + " - " + tema.getPartidaContable() + ") no est autorizada en el PUC para el centro origen");
			temaNota.setAutorizada(false);
			LOGGER.warn("{} La PARTIDA CONTABLE: {} no est autorizada en el PUC", partidaContable);
		}
		if (!cargaAltamiraManager.isSucursalValidaPUCOrigen(sucursal, contraPartidaContable)) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN,
					"La contrapartida contable del tema (" + tema.getNombre() + " - " + tema.getContraPartidaContable() + ") no est autorizada en el PUC para el centro origen");
			temaNota.setAutorizada(false);
			LOGGER.warn("{} La CONTRAPARTIDA CONTABLE: {} no est autorizada en el PUC", contraPartidaContable);
		}
		temaNota.setRiesgoOperacional(new RiesgoOperacional());
	}

	public NotaContable getNota() {
		return nota;
	}

	public void setNota(NotaContable nota) {
		this.nota = nota;
	}

	// gp12833 - aseguramiento anexos
	public String getNumeroRadicacion() {
		return numeroRadicacion;
	}

	public void setNumeroRadicacion(String numeroRadicacion) {
		this.numeroRadicacion = numeroRadicacion;
	}
	// fin gp12833 - aseguramiento anexos

	public NotaContableTema getTemaActual() {
		return temaActual;
	}

	public void setTemaActual(NotaContableTema temaActual) {
		this.temaActual = temaActual;
	}

	public List<NotaContableTema> getTemasNotaContable() {
		return temasNotaContable;
	}

	public void setTemasNotaContable(List<NotaContableTema> temasNotaContable) {
		this.temasNotaContable = temasNotaContable;
	}

	public ArrayList<NotaContableTotal> getTotalesNota() {
		return totalesNota;
	}

	public void setTotalesNota(ArrayList<NotaContableTotal> totalesNota) {
		this.totalesNota = totalesNota;
	}

	public NotasContablesSession getNotasContablesManager() {
		return notasContablesManager;
	}

	public List<SelectItem> getCausalesDev() {
		return causalesDev;
	}

	public void setCausalesDev(List<SelectItem> causalesDev) {
		this.causalesDev = causalesDev;
	}

	public Integer getCausalDevolucion() {
		return causalDevolucion;
	}

	public String getOtraCausalDev() {
		return otraCausalDev;
	}

	public void setCausalDevolucion(Integer causalDevolucion) {
		this.causalDevolucion = causalDevolucion;
	}

	public void setOtraCausalDev(String otraCausalDev) {
		this.otraCausalDev = otraCausalDev;
	}

	public ArrayList<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(ArrayList<Anexo> anexos) {
		this.anexos = anexos;
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
