package co.com.bbva.app.notas.contables.jsf.nota;

import co.com.bbva.app.notas.contables.carga.dto.HADTAPL;
import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.carga.dto.TipoIdentificacion;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.jsf.GeneralPage;
import co.com.bbva.app.notas.contables.jsf.IPages;
import co.com.bbva.app.notas.contables.jsf.adminnota.PendientePage;
import co.com.bbva.app.notas.contables.jsf.consultas.GeneralConsultaPage;
import co.com.bbva.app.notas.contables.util.EMailSender;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
public class FlujoNotaContableLibrePage extends GeneralPage implements IPages {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlujoNotaContableLibrePage.class);
	
	@Inject
	private PendientePage pendientePage;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final EMailSender enviarEMail;

	// nota contable a manejar
	protected NotaContable nota = new NotaContable();
	// variable para guardar el tema visualizado en un instante dado
	protected NotaContableRegistroLibre temaActual = new NotaContableRegistroLibre();
	// temas asociados a la nota contable
	protected List<NotaContableRegistroLibre> temasNotaContable = new ArrayList<NotaContableRegistroLibre>();
	protected ArrayList<NotaContableTotal> totalesNota = new ArrayList<NotaContableTotal>();
	protected List<Anexo> anexos = new ArrayList<Anexo>();
	protected Instancia instancia = new Instancia();

	private Integer causalDevolucion = 0;
	private String otraCausalDev = "";

	private boolean chequeoReasignacion = false;

	private int usuariologueado = 0;

	private int usuarioinstancia = 0;

	private List<SelectItem> causalesDev = new ArrayList<SelectItem>();
	// banderas para controlar el cierre de popups
	private boolean ocultarPopupAprobar = false;
	private boolean ocultarPopupAnular = false;

	public FlujoNotaContableLibrePage() {
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
			instancia = new Instancia();
			instancia.setCodigoNotaContable(nota.getCodigo());
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);

			if (codUsuAsignado > 0 && !instancia.getEstado().equals("4") && !instancia.getEstado().equals("5") && !instancia.getEstado().equals("9")) {
				nota.setPuedeAprobar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
			}
			if (codUsuAsignado > 0 && !instancia.getEstado().equals("1") && !instancia.getEstado().equals("9")) {
				nota.setPuedeRechazar(getCodUsuarioLogueado() == codUsuAsignado.intValue());
				causalesDev = getSelectItemList(notasContablesManager.getCV(CausalDevolucion.class), false);
			}

			anexos = new ArrayList<Anexo>(notasContablesManager.getAnexosPorInstancia(nota.getCodigo().intValue()));
			temasNotaContable = new ArrayList<NotaContableRegistroLibre>(notasContablesManager.getRegistrosNotaContableLibre(nota.getCodigo()));
			ajustarTotalesNota();
		} catch (Exception e) {
			e.printStackTrace();
			lanzarError(e, "No se ha podido completar la ejecucin solicitada");
		}
	}

	private void ajustarTotalesNota() {
		totalesNota = new ArrayList<NotaContableTotal>();
		for (NotaContableRegistroLibre tema : temasNotaContable) {
			if (!tema.getCodigoDivisa().equals("")) {
				boolean existeDivisa = false;
				// se multiplica el valor dependiendo de la naturaleza (debe ->sumar, haber
				// ->restar)
				int multiplo = tema.getNaturalezaCuentaContable().equalsIgnoreCase("D") ? 1 : -1;
				for (NotaContableTotal totalNota : totalesNota) {
					if (totalNota.getCodigoDivisa().equals(tema.getCodigoDivisa())) {
						existeDivisa = true;
						totalNota.setTotal(totalNota.getTotal() + tema.getMonto() * multiplo);
						break;
					}
				}
				if (!existeDivisa) {
					NotaContableTotal totalNota = new NotaContableTotal();
					totalNota.setCodigoDivisa(tema.getCodigoDivisa());
					totalNota.setTotal(tema.getMonto() * multiplo);
					totalesNota.add(totalNota);
				}
			}
		}
	}

	/**
	 * Funcion llamada al iniciar el flujo de creacion de notas contables
	 * 
	 * @return
	 */
	public String iniciar() {
		return NOTA_CONTABLE_LIBRE;
	}

	public String verNota() {
		LOGGER.info("ENTRANDO A LA NOTA LIBRE :::: > " + this.codigo);
		try {
			// temaActual = notasContablesManager.getTemaNotaContablePorCodigo(temaActual);
			for (NotaContableRegistroLibre nct : temasNotaContable) {
				if (nct.getCodigo() == codigo.intValue()) {
					temaActual = nct;
					if (codigo.intValue() > 0) {
						// se consultan los nombres del tipo deidentificacion
						TipoIdentificacion tipoIdentificacion = new TipoIdentificacion();
						if (!temaActual.getNombreCompleto().isEmpty()) {
							tipoIdentificacion.setCodigo(temaActual.getTipoIdentificacion());
							temaActual.setNombreTipoDoc(cargaAltamiraManager.getTipoIdentificacion(tipoIdentificacion).getNombre());
						}
						Sucursal sucursal = new Sucursal();
						sucursal.setCodigo(temaActual.getCodigoSucursalDestino());
						temaActual.setSucursalDestino(cargaAltamiraManager.getSucursal(sucursal));

						temaActual.setRiesgoOperacional(notasContablesManager.getRiesgoPorNotaContableYTemaNotaContable(nota.getCodigo().intValue(), temaActual.getCodigo()));
						// se verifica la partida y contra partida
						PUC partidaContable = new PUC();
						partidaContable.setNumeroCuenta(temaActual.getCuentaContable());
						partidaContable = cargaAltamiraManager.getPUC(partidaContable);
						temaActual.setPucCuenta(partidaContable);

						// se determina si la cuenta require contrato
						HADTAPL hadtapl = new HADTAPL();
						hadtapl.setCuentaContable(temaActual.getCuentaContable());
						hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);

						// si la busqueda para la cuenta especifica requiere contrato
						if (!hadtapl.getIndicadorContrato().equals("S")) {
							hadtapl = new HADTAPL();
							hadtapl.setCuentaContable(temaActual.getCuentaContable().substring(0, 4));
							hadtapl = cargaAltamiraManager.getHADTAPLPorCuenta(hadtapl);
						}
						temaActual.setHadtapl(hadtapl);
					}
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
				for (NotaContableRegistroLibre tema : temasNotaContable) {
					temaActual = tema;
					// se carga la informacion del tema
					verNota();
				}
				int codigoUsuarioAsignado = 0;

				int evaluoActividad = notasContablesManager.siguienteActividad(instancia, new ArrayList<NotaContableTema>(), new ArrayList<NotaContableTotal>(),
						getCodUsuarioLogueado(), true, 0, null, chequeoReasignacion);
				if (evaluoActividad != -1) {
					codigoUsuarioAsignado = evaluoActividad;
				} else {
					nuevoMensaje(FacesMessage.SEVERITY_INFO,
							"Se presentó un error al aprobar la nota: Verfique que el Aplicativo Notas Contables esté  abierto en  un nico navegador Web y en una única pestaña.  ");

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
				
				PrimeFaces.current().executeScript("PF('notaContableLibreVer').hide();");

				try {
					enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
							"Módulo Notas Contables - Registro para aprobar",
							"Por favor ingrese al mdulo de Notas Contables, se le ha asignado un registro que requiere su verificacin ");
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
				validador.validarSeleccion(causalDevolucion, "Causal de devolucion");
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
						for (NotaContableRegistroLibre tema : temasNotaContable) {
							temaActual = tema;
							// se carga la informacion del tema
							verNota();
						}
						int codigoUsuarioAsignado = notasContablesManager.siguienteActividad(instancia, new ArrayList<NotaContableTema>(), new ArrayList<NotaContableTotal>(),
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
						causalDevolucion = 0;
						otraCausalDev = "";
						ocultarPopupAnular = true;
						
						PrimeFaces.current().executeScript("PF('popupRechazoNotaLibreVer').hide();");
						PrimeFaces.current().executeScript("PF('notaContableLibreVer').hide();");
						
						try {
							enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), getUsuarioLogueado().getUsuario().getEMailModificado(),
									"Módulo Notas Contables - Registro rechazado",
									"Por favor ingrese al módulo de Notas Contables, se le ha asignado un registro que requiere su verificación");
						} catch (Exception e) {
							nuevoMensaje(FacesMessage.SEVERITY_INFO, "Se presentó un error al enviar el correo a la dirección: " + usuarioModulo.getEMailModificado());
						}
					} else {
						nuevoMensaje(FacesMessage.SEVERITY_INFO, errorMessage);
						ocultarPopupAnular = false;
					}
				}
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO,
						"Se presentó un error al rechazar la nota: Verfique que el Aplicativo Notas Contables /n esté  abierto en  un único navegador Web y en una única pestaña. ");
				
				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se presentó un error al rechazar la nota contable");
		}
		return null;
	}

	public String anular() {
		try {
			LOGGER.info("Entrando anular la nota contable.");
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
				pendientePage.cargarPendientes();
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido anulada correctamente");
				ocultarPopupAnular = true;
				PrimeFaces.current().executeScript("PF('popupAnularNotaLibre').hide();");
				PrimeFaces.current().executeScript("PF('popupNotaContableLibre').hide();");
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_INFO,
						"Se presentó un error al anular la nota: Verfique que el Aplicativo Notas Contables /n esté  abierto en  un único navegador Web y en una única pestaña. ");
				ocultarPopupAnular = false;
				return null;
			}
		} catch (Exception e) {
			lanzarError(e, "Se presentó un error al anular la nota contable");
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

	public NotaContable getNota() {
		return nota;
	}

	public void setNota(NotaContable nota) {
		this.nota = nota;
	}

	public NotaContableRegistroLibre getTemaActual() {
		return temaActual;
	}

	public void setTemaActual(NotaContableRegistroLibre temaActual) {
		this.temaActual = temaActual;
	}

	public List<NotaContableRegistroLibre> getTemasNotaContable() {
		return temasNotaContable;
	}

	public void setTemasNotaContable(List<NotaContableRegistroLibre> temasNotaContable) {
		this.temasNotaContable = temasNotaContable;
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

	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	public ArrayList<NotaContableTotal> getTotalesNota() {
		return totalesNota;
	}

	public void setTotalesNota(ArrayList<NotaContableTotal> totalesNota) {
		this.totalesNota = totalesNota;
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
