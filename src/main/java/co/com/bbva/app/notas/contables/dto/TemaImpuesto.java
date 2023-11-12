package co.com.bbva.app.notas.contables.dto;

public class TemaImpuesto extends CommonVO<TemaImpuesto> implements java.io.Serializable {

	private static final long serialVersionUID = -5159288581297397991L;

	private int codigo = 0;
	private int codigoTema = 0;
	private int codigoImpuesto = 0;

	private Impuesto impuesto = new Impuesto();

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigoTema() {
		return codigoTema;
	}

	public void setCodigoTema(int codigoTema) {
		this.codigoTema = codigoTema;
	}

	public int getCodigoImpuesto() {
		return codigoImpuesto;
	}

	public void setCodigoImpuesto(int codigoImpuesto) {
		this.codigoImpuesto = codigoImpuesto;
	}

	public Impuesto getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Impuesto impuesto) {
		this.impuesto = impuesto;
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
