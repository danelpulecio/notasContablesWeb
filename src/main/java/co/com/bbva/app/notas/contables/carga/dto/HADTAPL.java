package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

public class HADTAPL extends CommonVO<HADTAPL> implements java.io.Serializable {

	private static final long serialVersionUID = 736723193907548983L;
	private String cuentaContable = "";
	private String indicadorContrato = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getIndicadorContrato() {
		return indicadorContrato;
	}

	public void setIndicadorContrato(String indicadorContrato) {
		this.indicadorContrato = indicadorContrato;
	}

	public String getEstadoCarga() {
		return estadoCarga;
	}

	public void setEstadoCarga(String estadoCarga) {
		this.estadoCarga = estadoCarga;
	}

	public Timestamp getFechaUltimaCarga() {
		return fechaUltimaCarga;
	}

	public void setFechaUltimaCarga(Timestamp fechaUltimaCarga) {
		this.fechaUltimaCarga = fechaUltimaCarga;
	}

	@Override
	public Object getPK() {
		return cuentaContable;
	}

}
