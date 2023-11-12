package co.com.bbva.app.notas.contables.dto;

public class FechaHabilitada extends CommonVO<FechaHabilitada> implements java.io.Serializable {

	private static final long serialVersionUID = 6248858483771443644L;
	private Number codigo = 0;
	private String codigoSucursal = "";
	private Number dias = 2;

	private Boolean selected = false;

	private String nombreSucursal = "";

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public String getCodigoSucursal() {
		return codigoSucursal;
	}

	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}

	public Number getDias() {
		return dias;
	}

	public void setDias(Number dias) {
		this.dias = dias;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
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
