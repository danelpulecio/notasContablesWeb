package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Anexo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class AnexoDAO extends CommonSeqDAO<Anexo> {

	private static String cs_PK = "CODIGO";

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA, CODIGO_INSTANCIA, ESTADO_INSTANCIA, CODIGO_TEMA, CODIGO_USUARIO, NOMBRE, ARCHIVO, ESTADO";

	private static String cs_TABLA = "NC_ANEXO";

	private static String SQL_SELECT_BY_CODIGO_TEMA_SENTENCE = "SELECT AN.CODIGO \"codigo\", AN.FECHA_HORA \"fechaHoraTS\", AN.CODIGO_INSTANCIA \"codigoInstancia\", AN.ESTADO_INSTANCIA \"estadoInstancia\", AN.CODIGO_TEMA \"codigoTema\", AN.CODIGO_USUARIO \"codigoUsuario\", AN.NOMBRE \"nombre\", AN.ARCHIVO \"archivo\", AN.ESTADO \"estado\", USA.NOMBRE_EMPLEADO \"usuNombre\" FROM NC_ANEXO AN LEFT JOIN NC_USUARIO US ON (US.CODIGO = AN.CODIGO_USUARIO) LEFT JOIN NC_USUARIO_ALTAMIRA USA ON (USA.CODIGO_EMPLEADO = US.CODIGO_EMPLEADO) WHERE AN.CODIGO_TEMA !=0 AND AN.CODIGO_TEMA = ? ORDER BY AN.CODIGO";

	// gp12833 - aseguramiento anexos
	private static String SQL_SELECT_BY_NUMERO_RADICADO_SENTENCE = "SELECT	AN.CODIGO \"codigo\", AN.FECHA_HORA \"fechaHoraTS\", AN.CODIGO_INSTANCIA \"codigoInstancia\", AN.ESTADO_INSTANCIA \"estadoInstancia\", AN.CODIGO_TEMA \"codigoTema\", AN.CODIGO_USUARIO \"codigoUsuario\", AN.NOMBRE \"nombre\", AN.ARCHIVO \"archivo\", AN.ESTADO \"estado\", USA.NOMBRE_EMPLEADO \"usuNombre\" FROM CONTABLES.NC_ANEXO AN LEFT JOIN CONTABLES.NC_USUARIO US ON (US.CODIGO = AN.CODIGO_USUARIO) LEFT JOIN CONTABLES.NC_USUARIO_ALTAMIRA USA ON (USA.CODIGO_EMPLEADO = US.CODIGO_EMPLEADO) JOIN CONTABLES.NC_INSTANCIA B ON (AN.CODIGO_INSTANCIA = B.CODIGO) JOIN CONTABLES.NC_NOTA_CONTABLE C ON (B.CODIGO_NOTA_CONTABLE = C.CODIGO) WHERE C.NUMERO_RADICACION = ?";
	// fin gp12833 - aseguramiento anexos
	
	private static String SQL_SELECT_BY_CODIGO_INSTANCIA_SENTENCE = "SELECT AN.CODIGO \"codigo\", AN.FECHA_HORA \"fechaHoraTS\", AN.CODIGO_INSTANCIA \"codigoInstancia\", AN.ESTADO_INSTANCIA \"estadoInstancia\", AN.CODIGO_TEMA \"codigoTema\", AN.CODIGO_USUARIO \"codigoUsuario\", AN.NOMBRE \"nombre\", AN.ARCHIVO \"archivo\", AN.ESTADO \"estado\", USA.NOMBRE_EMPLEADO \"usuNombre\"  FROM CONTABLES.NC_ANEXO AN LEFT JOIN CONTABLES.NC_USUARIO US ON (US.CODIGO = AN.CODIGO_USUARIO) LEFT JOIN CONTABLES.NC_USUARIO_ALTAMIRA USA ON (USA.CODIGO_EMPLEADO = US.CODIGO_EMPLEADO) WHERE (CODIGO_INSTANCIA = ?)";

	private static String SQL_DELETE_BY_CODIGO_INSTACIA_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_INSTANCIA = ?)";

	private static String SQL_DELETE_BY_CODIGO_TEMA_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?)";

	private static String SQL_SELECT_BY_INSTANCIA_SENTENCE = "SELECT " + cs_COLUMNAS + "  FROM " + cs_TABLA + " WHERE (CODIGO_INSTANCIA = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_TEMA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?) ORDER BY CODIGO";

	public AnexoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Anexo());
	}

	public void deleteByCodigoInstancia(Connection con, int codigoInstancia, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_CODIGO_INSTACIA_SENTENCE, codigoInstancia);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByInstancia(con, codigoInstancia);
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar anexos de la instancia de la nota", Tema.class.getSimpleName(), "" + codigoInstancia);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public void deleteByCodigoTemaNotaContable(Connection con, int codigoTema, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_CODIGO_TEMA_SENTENCE, codigoTema);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByTema(con, codigoTema);
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar anexos del tema", Tema.class.getSimpleName(), "" + codigoTema);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public String getXMLDataByInstancia(Connection con, int codigoNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_INSTANCIA_SENTENCE, codigoNotaContable);
	}

	public String getXMLDataByTema(Connection con, int codigoTema) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_TEMA_SENTENCE, codigoTema);
	}

	public Collection<Anexo> findByCodigoTema(Anexo row) throws Exception {
		return obtenerLista(SQL_SELECT_BY_CODIGO_TEMA_SENTENCE, row.getCodigoTema());
	}
	
	// gp12833 - aseguramiento anexos
	public Collection<Anexo> findByCodigoTemaORadicado(Anexo row, String numeroRadicacion) throws Exception {
		Collection<Anexo> anexos = obtenerLista(SQL_SELECT_BY_CODIGO_TEMA_SENTENCE, row.getCodigoTema());
		if(anexos.isEmpty()) {
			anexos = findByNumeroRadicacion(numeroRadicacion);
		}
		return anexos;
	}
	
	public Collection<Anexo> findByNumeroRadicacion(String numeroRadicacion) throws Exception {
		return obtenerLista(SQL_SELECT_BY_NUMERO_RADICADO_SENTENCE, numeroRadicacion);
	}
	// fin gp12833 - aseguramiento anexos
	
	public Collection<Anexo> findByCodigoInstancia(int codInstanncia) throws Exception {
		return obtenerLista(SQL_SELECT_BY_CODIGO_INSTANCIA_SENTENCE, new Object[] { codInstanncia });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Anexo row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getFechaHora(), row.getCodigoInstancia(), row.getEstadoInstancia(), row.getCodigoTema(), row.getCodigoUsuario(), row.getNombre(), row.getArchivo(), row.getEstado() };
	}

	@Override
	public Anexo getResultSetToVO(ResultSet result) throws Exception {
		Anexo row = new Anexo();

		row.setCodigo(result.getInt(1));
		row.setFechaHora(result.getTimestamp(2));
		row.setCodigoInstancia(result.getInt(3));
		row.setEstadoInstancia(result.getString(4));
		row.setCodigoTema(result.getInt(5));
		row.setCodigoUsuario(result.getInt(6));
		row.setNombre(result.getString(7));
		row.setArchivo(result.getString(8));
		row.setEstado(result.getString(9));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") Anexo row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

}