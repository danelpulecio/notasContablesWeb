package co.com.bbva.app.notas.contables.dto;

public class CuentaCOD extends CommonVO<CuentaCOD> implements java.io.Serializable {

	private static final long serialVersionUID = 4216199261005405659L;
	private int codigo = 0;
	private String cuentaContable = "";

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
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
