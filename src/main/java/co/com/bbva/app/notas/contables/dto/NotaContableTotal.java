package co.com.bbva.app.notas.contables.dto;

public class NotaContableTotal extends CommonVO<NotaContableTotal> implements java.io.Serializable {

	private static final long serialVersionUID = 6190137059005870545L;

	private int codigo = 0;
	private int codigoNotaContable = 0;
	private String codigoDivisa = "";
	private double total = 0;

	// variable temporal usada en contabilidad libre para guardar la sucursal de destino y hacer validaciones de suma=0 por divisa y suc destino
	private String codSucursal;

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

	public String getCodigoDivisa() {
		return codigoDivisa;
	}

	public void setCodigoDivisa(String codigoDivisa) {
		this.codigoDivisa = codigoDivisa;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getCodSucursal() {
		return codSucursal;
	}

	public void setCodSucursal(String codSucursal) {
		this.codSucursal = codSucursal;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}

	@Override
	public String toString() {
		return "NotaContableTotal{" +
				"codigo=" + codigo +
				", codigoNotaContable=" + codigoNotaContable +
				", codigoDivisa='" + codigoDivisa + '\'' +
				", total=" + total +
				", codSucursal='" + codSucursal + '\'' +
				'}';
	}
}
