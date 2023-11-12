package co.com.bbva.app.notas.contables.dto;

public class NotaContableTemaImpuesto extends CommonVO<NotaContableTemaImpuesto> implements java.io.Serializable {

	private static final long serialVersionUID = 5042759640710902295L;

	private int codigo = 0;
	private int codigoNotaContable = 0;
	private int codigoTemaNotaContable = 0;
	private int codigoImpuesto = 0;
	private double base = 0;
	private double calculado = 0;
	private String exonera = "S";
	private String numeroAsiento = "";
	private String numeroApunte = "";
	// private String numeroAsientoContra = "";
	// private String numeroApunteContra = "";

	private TemaImpuesto impuestoTema = new TemaImpuesto();

	private Boolean boolExonera = false;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigoNotaContable() {
		return codigoNotaContable;
	}

	public void setCodigoNotaContable(int codigoNotaContable) {
		this.codigoNotaContable = codigoNotaContable;
	}

	public int getCodigoTemaNotaContable() {
		return codigoTemaNotaContable;
	}

	public void setCodigoTemaNotaContable(int codigoTemaNotaContable) {
		this.codigoTemaNotaContable = codigoTemaNotaContable;
	}

	public int getCodigoImpuesto() {
		return codigoImpuesto;
	}

	public void setCodigoImpuesto(int codigoImpuesto) {
		this.codigoImpuesto = codigoImpuesto;
	}

	public double getBase() {
		return base;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getCalculado() {
		return calculado;
	}

	public void setCalculado(double calculado) {
		this.calculado = calculado;
	}

	public String getExonera() {
		return exonera;
	}

	public void setExonera(String exonera) {
		this.exonera = exonera;
	}

	public String getNumeroAsiento() {
		return numeroAsiento;
	}

	public void setNumeroAsiento(String numeroAsiento) {
		this.numeroAsiento = numeroAsiento;
	}

	public String getNumeroApunte() {
		return numeroApunte;
	}

	public void setNumeroApunte(String numeroApunte) {
		this.numeroApunte = numeroApunte;
	}

	public TemaImpuesto getImpuestoTema() {
		return impuestoTema;
	}

	public void setImpuestoTema(TemaImpuesto impuestoTema) {
		this.impuestoTema = impuestoTema;
	}

	public Boolean getBoolExonera() {
		return boolExonera;
	}

	public void setBoolExonera(Boolean boolExonera) {
		this.boolExonera = boolExonera;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}
}
