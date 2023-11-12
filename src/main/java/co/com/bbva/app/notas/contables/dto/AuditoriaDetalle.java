package co.com.bbva.app.notas.contables.dto;

public class AuditoriaDetalle extends CommonVO<AuditoriaDetalle> implements java.io.Serializable {

	private static final long serialVersionUID = -8838211976337501327L;
	private int codigo = 0;
	private int codigoAuditoria = 0;
	private String registroOriginal = "";
	private String registroModificado = "";

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigoAuditoria() {
		return codigoAuditoria;
	}

	public void setCodigoAuditoria(int codigoAuditoria) {
		this.codigoAuditoria = codigoAuditoria;
	}

	public String getRegistroOriginal() {
		return registroOriginal;
	}

	public void setRegistroOriginal(String registroOriginal) {
		this.registroOriginal = registroOriginal;
	}

	public String getRegistroModificado() {
		return registroModificado;
	}

	public void setRegistroModificado(String registroModificado) {
		this.registroModificado = registroModificado;
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
