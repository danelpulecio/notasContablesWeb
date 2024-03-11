package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.carga.dto.RechazoSalida;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.jsf.carga.GeneralCargaPage;
import oracle.sql.TIMESTAMP;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotaContable extends CommonVO<NotaContable> implements java.io.Serializable {

	private static final long serialVersionUID = -7165741470107519284L;
	public static final String NORMAL = "R";
	public static final String CRUCE_REFERENCIA = "C";
	public static final String LIBRE = "L";
	private static final Logger LOGGER = LoggerFactory.getLogger(NotaContable.class);

	private Number codigo = 0;
	//private TIMESTAMP fechaRegistroModulo = null;
	private TIMESTAMP fechaRegistroModulo = null;
	private TIMESTAMP fechaRegistroAltamira = null;
	private String codigoSucursalOrigen = "";
	private Number codigoConcepto = 0;
	private String codigoConceptoSt;

	private Number codigoTipoEvento = 0;
	private String numeroRadicacion = "";
	private String tipoNota = "";
	private String descripcion = "";
	private String estado = "A";
	private String asientoContable = "";

	private Concepto concepto = new Concepto();
	private TipoEvento tipoEvento = new TipoEvento();
	private Sucursal sucursalOrigen = new Sucursal();

	private Number codUsuAsignado = 0;
	// indica si el usuario le puede cambiar el estado (abrobar)
	private Boolean puedeAprobar = false;
	// indica si el usuario le puede cambiar el estado (rechazar)
	private Boolean puedeRechazar = false;
	// indica si el usuario le puede anular la nota contable
	private Boolean puedeAnular = false;
	// indica si el usuario puede editar el contenido de la nota
	private Boolean puedeEditar = false;

	private List<NotaContableTema> temas = new ArrayList<NotaContableTema>();
	private List<NotaContableCrucePartidaPendiente> temasCruce = new ArrayList<NotaContableCrucePartidaPendiente>();
	private List<NotaContableRegistroLibre> temasLibre = new ArrayList<NotaContableRegistroLibre>();
	private String causalDeRechazo = "";

	private List<RechazoSalida> rechazos = new ArrayList<RechazoSalida>();


	/**
	 * End
	 * @param puedeEditar
	 */

	public void setPuedeEditar(Boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public Timestamp getFechaRegistroModuloTs() {
		try {
			return fechaRegistroModulo.timestampValue();
		} catch (Exception e) {
			return new Timestamp(new Date().getTime());
		}
	}

	public void setFechaRegistroModuloTs(Timestamp fechaRegistroModulo) {
		this.fechaRegistroModulo = new TIMESTAMP(fechaRegistroModulo);
	}

	public Timestamp getFechaRegistroAltamiraTs() {
		try {
			return fechaRegistroAltamira.timestampValue();
		} catch (Exception e) {
			return new Timestamp(new Date().getTime());
		}
	}

	public void setFechaRegistroAltamiraTs(Timestamp fechaRegistroAltamira) {
		this.fechaRegistroAltamira = new TIMESTAMP(fechaRegistroAltamira);
	}

	public String getCodigoSucursalOrigen() {
		return codigoSucursalOrigen;
	}

	public void setCodigoSucursalOrigen(String codigoSucursalOrigen) {
		this.codigoSucursalOrigen = codigoSucursalOrigen;
	}

	public String getNumeroRadicacion() {
		return numeroRadicacion;
	}

	public void setNumeroRadicacion(String numeroRadicacion) {
		this.numeroRadicacion = numeroRadicacion;
	}

	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public String getAsientoContable() {
		return asientoContable;
	}

	public void setAsientoContable(String asientoContable) {
		this.asientoContable = asientoContable;
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoConcepto() {
		return codigoConcepto;
	}

	public void setCodigoConcepto(Number codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	public String getCodigoConceptoSt() {
		codigoConceptoSt = codigoConcepto.toString();
		return codigoConceptoSt;
	}

	public void setCodigoConceptoSt(String codigoConceptoSt) {
		this.codigoConceptoSt = codigoConcepto.toString();
	}

	public Number getCodigoTipoEvento() {
		return codigoTipoEvento;
	}

	public void setCodigoTipoEvento(Number codigoTipoEvento) {
		this.codigoTipoEvento = codigoTipoEvento;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public TIMESTAMP getFechaRegistroModulo() {
		return fechaRegistroModulo;
	}

	public void setFechaRegistroModulo(TIMESTAMP fechaRegistroModulo) {
		this.fechaRegistroModulo = fechaRegistroModulo;
	}

	public TIMESTAMP getFechaRegistroAltamira() {
		return fechaRegistroAltamira;
	}

	public void setFechaRegistroAltamira(TIMESTAMP fechaRegistroAltamira) {
		this.fechaRegistroAltamira = fechaRegistroAltamira;
	}



	public Sucursal getSucursalOrigen() {
		return sucursalOrigen;
	}

	public Number getCodUsuAsignado() {
		return codUsuAsignado;
	}

	public void setCodUsuAsignado(Number codUsuAsignado) {
		this.codUsuAsignado = codUsuAsignado;
	}

	public Boolean getPuedeAprobar() {
		return puedeAprobar;
	}

	public void setPuedeAprobar(Boolean puedeAprobar) {
		this.puedeAprobar = puedeAprobar;
	}

	public Boolean getPuedeRechazar() {
		return puedeRechazar;
	}

	public void setPuedeRechazar(Boolean puedeRechazar) {
		this.puedeRechazar = puedeRechazar;
	}

	public Boolean getPuedeEditar() {
		return puedeEditar;
	}

	public void setSucursalOrigen(Sucursal sucursalOrigen) {
		this.sucursalOrigen = sucursalOrigen;
	}

	public void setTemas(List<NotaContableTema> temasNota) {
		this.temas = temasNota;
	}

	public List<NotaContableTema> getTemas() {
		return temas;
	}

	public List<NotaContableCrucePartidaPendiente> getTemasCruce() {
		return temasCruce;
	}

	public void setTemasCruce(List<NotaContableCrucePartidaPendiente> temasCruce) {
		this.temasCruce = temasCruce;
	}

	public List<NotaContableRegistroLibre> getTemasLibre() {
		return temasLibre;
	}

	public void setTemasLibre(List<NotaContableRegistroLibre> temasLibre) {
		this.temasLibre = temasLibre;
	}

	public Boolean getPuedeAnular() {
		return puedeAnular;
	}

	public void setPuedeAnular(Boolean puedeAnular) {
		this.puedeAnular = puedeAnular;
	}

	public List<RechazoSalida> getRechazos() {
		return rechazos;
	}

	public void setRechazos(List<RechazoSalida> rechazos) {
		this.rechazos = rechazos;
	}

	public String getCausalDeRechazo() {
		return causalDeRechazo;
	}

	public void setCausalDeRechazo(String causalDeRechazo) {
		this.causalDeRechazo = causalDeRechazo;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.parseInt(pk.toString());
	}

}
