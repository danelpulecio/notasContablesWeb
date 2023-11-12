package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;
import co.com.bbva.app.notas.contables.carga.dto.UsuarioAltamira;

import java.io.Serializable;
import java.util.ArrayList;

@Tabla(nombreTabla = "NC_USUARIO")
public class UsuarioModulo extends CommonVO<UsuarioModulo> implements Serializable {

	private static final long serialVersionUID = -5599928713359198638L;

	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private Number codigo;

	@Columna(nombreDB = "CODIGO_EMPLEADO", nombreApp = "codigoEmpleado")
	private String codigoEmpleado;

	@Columna(nombreDB = "CODIGO_ROL", nombreApp = "codigoRol")
	private Number codigoRol;

	@Columna(nombreDB = "CODIGO_AREA", nombreApp = "codigoAreaModificado")
	private String codigoAreaModificado;

	@Columna(nombreDB = "NOMBRE_AREA", nombreApp = "nombreAreaModificado")
	private String nombreAreaModificado;

	@Columna(nombreDB = "CODIGO_PERFIL", nombreApp = "codigoPerfilModificado")
	private String codigoPerfilModificado;

	@Columna(nombreDB = "NOMBRE_PERFIL", nombreApp = "nombrePerfilModificado")
	private String nombrePerfilModificado;

	@Columna(nombreDB = "EMAIL_USUARIO", nombreApp = "eMailModificado")
	private String eMailModificado;

	@Columna(nombreDB = "ACTUALIZAR_AUT", nombreApp = "actualizarAutomatico")
	private String actualizarAutomatico;

	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado;

	@Columna(nombreDB = "LAST_UPDATE", nombreApp = "lastUpdate")
	private String lastUpdate;

	private UsuarioAltamira usuAlt;

	private Number duracionPromedio = 0;

	private ArrayList<ActividadRealizada> actividades = new ArrayList<>();

	private Rol rol;

	public UsuarioModulo() {
		this.actualizarAutomatico = "N";
		this.codigo = 0;
		this.codigoAreaModificado = "";
		this.codigoEmpleado = "";
		this.codigoPerfilModificado = "";
		this.codigoRol = 0;
		this.eMailModificado = "";
		this.estado = "A";
		this.nombreAreaModificado = "";
		this.nombrePerfilModificado = "";
		this.lastUpdate = "";
		this.rol = new Rol();
		this.usuAlt = new UsuarioAltamira();
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}

	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	public Number getCodigoRol() {
		return codigoRol;
	}

	public void setCodigoRol(Number codigoRol) {
		this.codigoRol = codigoRol;
	}

	public String getCodigoAreaModificado() {
		return codigoAreaModificado;
	}

	public void setCodigoAreaModificado(String codigoAreaModificado) {
		this.codigoAreaModificado = codigoAreaModificado;
	}

	public String getNombreAreaModificado() {
		return nombreAreaModificado;
	}

	public void setNombreAreaModificado(String nombreAreaModificado) {
		this.nombreAreaModificado = nombreAreaModificado;		
	}

	public String getCodigoPerfilModificado() {
		return codigoPerfilModificado;
	}

	public void setCodigoPerfilModificado(String codigoPerfilModificado) {
		this.codigoPerfilModificado = codigoPerfilModificado;
	}

	public String getNombrePerfilModificado() {
		return nombrePerfilModificado;
	}

	public void setNombrePerfilModificado(String nombrePerfilModificado) {
		this.nombrePerfilModificado = nombrePerfilModificado;
	}

	public String getActualizarAutomatico() {
		return actualizarAutomatico;
	}

	public void setActualizarAutomatico(String actualizarAutomatico) {
		this.actualizarAutomatico = actualizarAutomatico;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEMailModificado() {
		return eMailModificado;
	}

	public void setEMailModificado(String mailModificado) {
		eMailModificado = mailModificado;
	}

	public UsuarioAltamira getUsuAlt() {
		return usuAlt;
	}

	public void setUsuAlt(UsuarioAltamira usuAlt) {
		this.usuAlt = usuAlt;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Number getDuracionPromedio() {
		return duracionPromedio;
	}

	public void setDuracionPromedio(Number duracionPromedio) {
		this.duracionPromedio = duracionPromedio;
	}

	public ArrayList<ActividadRealizada> getActividades() {
		return actividades;
	}

	public void setActividades(ArrayList<ActividadRealizada> actividades) {
		this.actividades = actividades;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
