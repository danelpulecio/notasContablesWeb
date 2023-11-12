package co.com.bbva.app.notas.contables.dto;

public class TemaProducto extends CommonVO<TemaProducto> implements java.io.Serializable {

	private static final long serialVersionUID = -7575879568386249129L;
	private int codigo = 0;
	private int codigoTema = 0;
	private String codigoProducto = "";

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

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
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
