package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.NotaContableTema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class NotaContableTemaDAO extends CommonSeqDAO<NotaContableTema> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_TEMA, FECHA_CONTABLE, PARTIDA_CONTABLE, NATURALEZA1, CONTRAPARTIDA_CONTABLE, NATURALEZA2, COD_SUC_DEST_PART, COD_SUC_DEST_CONTPART, CODIGO_DIVISA, MONTO, REFERENCIA1, REFERENCIA2, TIPO_IDENTIFICACION1, NUMERO_IDENTIFICACION1, DIGITO_VERIFICACION1, NOMBRE_COMPLETO1, TIPO_IDENTIFICACION2, NUMERO_IDENTIFICACION2, DIGITO_VERIFICACION2, NOMBRE_COMPLETO2, CONTRATO1, CONTRATO2, DESCRIPCION, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_NOTA_CONTABLE_TEMA";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_COMPLETO_BY_NOTA_CONTABLE_SENTENCE = "SELECT NCT.CODIGO \"codigo\", NCT.CODIGO_NOTA_CONTABLE \"codigoNotaContable\", NCT.CODIGO_TEMA \"codigoTema\", NCT.FECHA_CONTABLE \"fechaContableTS\", NCT.PARTIDA_CONTABLE \"partidaContable\", NCT.NATURALEZA1 \"naturalezaPartidaContable\", NCT.CONTRAPARTIDA_CONTABLE \"contrapartidaContable\", NCT.NATURALEZA2 \"natContContable\", NCT.COD_SUC_DEST_PART \"codigoSucursalDestinoPartida\", NCT.COD_SUC_DEST_CONTPART \"codSucDestContraPartida\", NCT.CODIGO_DIVISA \"codigoDivisa\", NCT.MONTO \"monto\", NCT.REFERENCIA1 \"referencia1\", NCT.REFERENCIA2 \"referencia2\", NCT.TIPO_IDENTIFICACION1 \"tipoIdentificacion1\", NCT.NUMERO_IDENTIFICACION1 \"numeroIdentificacion1\", NCT.DIGITO_VERIFICACION1 \"digitoVerificacion1\", NCT.NOMBRE_COMPLETO1 \"nombreCompleto1\", NCT.TIPO_IDENTIFICACION2 \"tipoIdentificacion2\", NCT.NUMERO_IDENTIFICACION2 \"numeroIdentificacion2\", NCT.DIGITO_VERIFICACION2 \"digitoVerificacion2\", NCT.NOMBRE_COMPLETO2 \"nombreCompleto2\", NCT.CONTRATO1 \"contrato1\", NCT.CONTRATO2 \"contrato2\", NCT.DESCRIPCION \"descripcion\", NCT.NUMERO_ASIENTO \"numeroAsiento\", NCT.NUMERO_APUNTE \"numeroApunte\", TE.NOMBRE \"tema__nombre\", TE.CODIGO \"tema__codigo\", TE.RIESGO_OPERACIONAL \"tema__riesgoOperacional\"  FROM "
			+ " NC_NOTA_CONTABLE_TEMA NCT LEFT JOIN NC_TEMA TE ON (TE.CODIGO = NCT.CODIGO_TEMA)  WHERE (CODIGO_NOTA_CONTABLE = ?)";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) AND (CODIGO_TEMA = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_CONCEPTO_AND_ESTADO_AND_TEMA_SENTENCE = "SELECT NCT.CODIGO \"codigo\", NCT.CODIGO_NOTA_CONTABLE \"codigoNotaContable\", NCT.CODIGO_TEMA \"codigoTema\", NCT.FECHA_CONTABLE \"fechaContableTS\", NCT.PARTIDA_CONTABLE \"partidaContable\", NCT.NATURALEZA1 \"naturalezaPartidaContable\", NCT.CONTRAPARTIDA_CONTABLE \"contrapartidaContable\", NCT.NATURALEZA2 \"natContContable\", NCT.COD_SUC_DEST_PART \"codigoSucursalDestinoPartida\", NCT.COD_SUC_DEST_CONTPART \"codSucDestContraPartida\", NCT.CODIGO_DIVISA \"codigoDivisa\", NCT.MONTO \"monto\", NCT.REFERENCIA1 \"referencia1\", NCT.REFERENCIA2 \"referencia2\", NCT.TIPO_IDENTIFICACION1 \"tipoIdentificacion1\", NCT.NUMERO_IDENTIFICACION1 \"numeroIdentificacion1\", NCT.DIGITO_VERIFICACION1 \"digitoVerificacion1\", NCT.NOMBRE_COMPLETO1 \"nombreCompleto1\", NCT.TIPO_IDENTIFICACION2 \"tipoIdentificacion2\", NCT.NUMERO_IDENTIFICACION2 \"numeroIdentificacion2\", NCT.DIGITO_VERIFICACION2 \"digitoVerificacion2\", NCT.NOMBRE_COMPLETO2 \"nombreCompleto2\", NCT.CONTRATO1 \"contrato1\", NCT.CONTRATO2 \"contrato2\", NCT.DESCRIPCION \"descripcion\", NCT.NUMERO_ASIENTO \"numeroAsiento\", NCT.NUMERO_APUNTE \"numeroApunte\", TE.NOMBRE \"tema__nombre\", TE.CODIGO \"tema__codigo\", TE.RIESGO_OPERACIONAL \"tema__riesgoOperacional\" FROM NC_NOTA_CONTABLE_TEMA NCT LEFT JOIN NC_TEMA TE ON (TE.CODIGO = NCT.CODIGO_TEMA) WHERE (TE.CODIGO_CONCEPTO = ? AND TE.ESTADO = ?) AND (NCT.CODIGO_NOTA_CONTABLE = ?) ORDER BY NCT.CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	private static String SQL_UPDATE_FECHA_SENTENCE = "UPDATE  " + cs_TABLA + " SET FECHA_CONTABLE = ? WHERE (CODIGO = ?)";

	private static String SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?)";

	private final static String QUERY_PARTIDA_IMPUESTO = "SELECT  \n" + //
			"  '0013'   \n" + //
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0') \n" + //  
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0') \n" + // 
			" || LPAD(NCT.COD_SUC_DEST_PART, 4, '0')   \n" + //
			" || '0' || LPAD(NCT.CODIGO, 9, '9')  \n" + // EL 0 IDENTIFICA LAS PARTIDAS DE IMPUESTO
			" || TO_CHAR(NCT.FECHA_CONTABLE, 'YYYYMMDD') \n" + //  
			" || RPAD(IMP.PARTIDA_CONTABLE, 15, ' ')   \n" + //
			" || CASE WHEN DECODE(NCTI.CODIGO_IMPUESTO,103,NCT.NATURALEZA2,NCT.NATURALEZA1) =  'D' THEN '10' ELSE '01' END   \n" + //
			" || CASE WHEN DECODE(NCTI.CODIGO_IMPUESTO,103,NCT.NATURALEZA2,NCT.NATURALEZA1) =  'D' THEN REPLACE(REPLACE(LTRIM(TO_CHAR(NCTI.CALCULADO, '0000000000000D00')), ',', ''), '.', '') || '000000000000000' ELSE '000000000000000' || REPLACE(REPLACE(LTRIM(TO_CHAR(NCTI.CALCULADO, '0000000000000D00')), ',', ''), '.', '')  END \n" + // 
			" || RPAD(NCT.CODIGO_DIVISA, 3, ' ') \n" + //  
			" || LPAD(NC.CODIGO_CONCEPTO, 3, '0')   \n" + //
			" || NVL(NCT.TIPO_IDENTIFICACION1, '0') \n" + //  
			" || LPAD(NVL(NCT.NUMERO_IDENTIFICACION1, '0'), 15, '0')   \n" + //
			" || NVL(NCT.DIGITO_VERIFICACION1,'0')  \n" + //
			" || LPAD(NCT.REFERENCIA1, 12, '0')   \n" + //
			" || LPAD(NVL(NCT.CONTRATO1, '0'), 18, '0')  \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_TIPO_PERDIDA, '0'), 4, '0')   \n" + //
			" || LPAD(NVL(NCTR.CODIGO_CLASE_RIESGO, '0'), 3, '0') \n" + // 
			" || '0'  \n" + //
			" || LPAD(NVL(NCTR.CODIGO_PRODUCTO, '0'), 11, '0') \n" + //
			/** Incidencia COL396113I000838**/
			"|| LPAD(NVL(TO_CHAR(NCTR.FECHA_EVENTO, 'YYYYMMDD'), '0'), 8, '0')\n" + 
			" || LPAD(NVL(NCTR.CODIGO_PROCESO, '0'), 6, '0')   \n" + //
			" || LPAD(NVL(NCTR.CODIGO_LINEA_OPER, '0'), 4, '0')   \n" + //
			/**" || REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' ')   \n" + **/
			" || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')   \n" + //
			" || SUBSTR(NC.NUMERO_RADICACION, 5)  \n" + // 
			" || CASE WHEN NCTR.HORA_INICIO_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_INICIO_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' END  ELSE  '0001-01-01-00.00.00.000000' END \n" + // 
            " || CASE WHEN NCTR.HORA_FIN_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_FIN_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_FIN_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_FIN_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
            " || CASE WHEN NCTR.HORA_DESCUBRIMIENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_DESCUBRIMIENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000' ELSE TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000'  END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
		 	" FROM NC_NOTA_CONTABLE_TEMA NCT   \n" + //
			" LEFT JOIN NC_NOTA_CONTABLE NC ON NCT.CODIGO_NOTA_CONTABLE = NC.CODIGO \n" + //  
			" LEFT JOIN NC_NOTA_CONTABLE_TEMA_RIESGO NCTR ON NCTR.CODIGO_NOTA_CONTABLE = NC.CODIGO AND NCTR.CODIGO_TEMA = NCT.CODIGO \n" + //  
			" LEFT JOIN NC_NOTA_CONTABLE_TEMA_IMPUESTO NCTI ON NCTI.CODIGO_TEMA = NCT.CODIGO \n" + //
			" LEFT JOIN NC_IMPUESTO IMP ON IMP.CODIGO = NCTI.CODIGO_IMPUESTO \n" + //
		    " WHERE NC.CODIGO = ? AND NCTI.EXONERA = 'N' \n" + //
			" ORDER BY NCT.CODIGO";
	private final static String QUERY_PARTIDA = "SELECT \n" + //
			" '0013'  \n" + //
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0') \n" + // 
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0') \n" + //
			" || LPAD(NCT.COD_SUC_DEST_PART, 4, '0') \n" + // 
			" || '1' || LPAD(NCT.CODIGO, 9, '0') \n" + // EL 1 IDENTIFICA LAS PARTIDAS
			" || TO_CHAR(NCT.FECHA_CONTABLE, 'YYYYMMDD') \n" + // 
			" || RPAD(NCT.PARTIDA_CONTABLE, 15, ' ') \n" + // 
			" || CASE WHEN NCT.NATURALEZA1 = 'D' THEN '10' ELSE '01' END \n" + // 
			" || CASE WHEN NCT.NATURALEZA1 = 'D' THEN REPLACE(REPLACE(LTRIM(TO_CHAR(NCT.MONTO, '0000000000000D00')), ',', ''), '.', '') || '000000000000000' ELSE '000000000000000' || REPLACE(REPLACE(LTRIM(TO_CHAR(NCT.MONTO, '0000000000000D00')), ',', ''), '.', '')  END \n" + // 
			" || RPAD(NCT.CODIGO_DIVISA, 3, ' ') \n" + // 
			" || LPAD(NC.CODIGO_CONCEPTO, 3, '0') \n" + // 
			" || NVL(NCT.TIPO_IDENTIFICACION1, '0')\n" + // 
			" || LPAD(NVL(NCT.NUMERO_IDENTIFICACION1, '0'), 15, '0') \n" + // 
			" || NVL(NCT.DIGITO_VERIFICACION1,'0') \n" + //
			" || LPAD(NCT.REFERENCIA1, 12, '0') \n" + // 
			" || LPAD(NVL(NCT.CONTRATO1, '0'), 18, '0') \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_TIPO_PERDIDA, '0'), 4, '0') \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_CLASE_RIESGO, '0'), 3, '0') \n" + //
			" || '0' \n" + //
			" || LPAD(NVL(NCTR.CODIGO_PRODUCTO, '0'), 11, '0') \n" + // 
			/** Incidencia COL396113I000838**/
			"|| LPAD(NVL(TO_CHAR(NCTR.FECHA_EVENTO, 'YYYYMMDD'), '0'), 8, '0')\n" + 
			" || LPAD(NVL(NCTR.CODIGO_PROCESO, '0'), 6, '0') \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_LINEA_OPER, '0'), 4, '0') \n" + // 
			/**" || REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' ')   \n" + **/
			" || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')   \n" + //
			" || SUBSTR(NC.NUMERO_RADICACION, 5)  \n" + // 
			" || CASE WHEN NCTR.HORA_INICIO_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_INICIO_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' END  ELSE  '0001-01-01-00.00.00.000000' END \n" + // 
            " || CASE WHEN NCTR.HORA_FIN_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_FIN_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_FIN_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_FIN_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
            " || CASE WHEN NCTR.HORA_DESCUBRIMIENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_DESCUBRIMIENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000' ELSE TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000'  END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
			" FROM NC_NOTA_CONTABLE_TEMA NCT \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE NC ON NCT.CODIGO_NOTA_CONTABLE = NC.CODIGO \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE_TEMA_RIESGO NCTR ON NCTR.CODIGO_NOTA_CONTABLE = NC.CODIGO AND NCTR.CODIGO_TEMA = NCT.CODIGO \n" + // 
			" WHERE NC.CODIGO = ? ORDER BY NCT.CODIGO"; //
	private final static String QUERY_CONTRA_PARTIDA_IMPUESTO = " SELECT   \n" + // 
			"  '0013'   \n" + // 
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0') \n" + //  
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0')  \n" + // 
			" || LPAD(NCT.COD_SUC_DEST_CONTPART, 4, '0')  \n" + // 
			" || '2' || LPAD(NCT.CODIGO, 9, '9')  \n" + // EL 2 IDENTIFICA A LAS CONTRA PARTIDAS DE IMPUESTOS
			" || TO_CHAR(NCT.FECHA_CONTABLE, 'YYYYMMDD') \n" + //  
			" || RPAD(NCT.CONTRAPARTIDA_CONTABLE, 15, ' ')  \n" + // 
			" || CASE WHEN DECODE(NCTI.CODIGO_IMPUESTO,103,NCT.NATURALEZA1,NCT.NATURALEZA2) =  'D' THEN '10' ELSE '01' END  \n" + // 
			" || CASE WHEN DECODE(NCTI.CODIGO_IMPUESTO,103,NCT.NATURALEZA1,NCT.NATURALEZA2) =  'D' THEN REPLACE(REPLACE(LTRIM(TO_CHAR(NCTI.CALCULADO, '0000000000000D00')), ',', ''), '.', '') || '000000000000000' ELSE '000000000000000' || REPLACE(REPLACE(LTRIM(TO_CHAR(NCTI.CALCULADO, '0000000000000D00')), ',', ''), '.', '')  END \n" + // 
			" || RPAD(NCT.CODIGO_DIVISA, 3, ' ')  \n" + // 
			" || LPAD(NC.CODIGO_CONCEPTO, 3, '0')  \n" + // 
			" || NVL(NCT.TIPO_IDENTIFICACION2, '0') \n" + //  
			" || LPAD(NVL(NCT.NUMERO_IDENTIFICACION2, '0'), 15, '0')  \n" + // 
			" || NVL(NCT.DIGITO_VERIFICACION2,'0')  \n" + // 
			" || LPAD(NCT.REFERENCIA2, 12, '0')  \n" + // 
			" || LPAD(NVL(NCT.CONTRATO2, '0'), 18, '0') \n" + //  
			" || LPAD(NVL(NCTR.CODIGO_TIPO_PERDIDA, '0'), 4, '0') \n" + //  
			" || LPAD(NVL(NCTR.CODIGO_CLASE_RIESGO, '0'), 3, '0')  \n" + // 
			" || '0'  \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_PRODUCTO, '0'), 11, '0') \n" + //  
			/** Incidencia COL396113I000838**/
			"|| LPAD(NVL(TO_CHAR(NCTR.FECHA_EVENTO, 'YYYYMMDD'), '0'), 8, '0')\n" + 
			" || LPAD(NVL(NCTR.CODIGO_PROCESO, '0'), 6, '0')  \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_LINEA_OPER, '0'), 4, '0')  \n" + // 
			/**" || REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' ')   \n" +  **/
			" || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')   \n" + //
			" || CASE WHEN NCTR.HORA_INICIO_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_INICIO_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' END  ELSE  '0001-01-01-00.00.00.000000' END \n" + // 
            " || CASE WHEN NCTR.HORA_FIN_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_FIN_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_FIN_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_FIN_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
            " || CASE WHEN NCTR.HORA_DESCUBRIMIENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_DESCUBRIMIENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000' ELSE TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000'  END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
			" FROM NC_NOTA_CONTABLE_TEMA NCT  \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE NC ON NCT.CODIGO_NOTA_CONTABLE = NC.CODIGO \n" + //  
			" LEFT JOIN NC_NOTA_CONTABLE_TEMA_RIESGO NCTR ON NCTR.CODIGO_NOTA_CONTABLE = NC.CODIGO AND NCTR.CODIGO_TEMA = NCT.CODIGO \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE_TEMA_IMPUESTO NCTI ON NCTI.CODIGO_TEMA = NCT.CODIGO \n" + // 
			" WHERE NC.CODIGO = ? AND NCTI.EXONERA = 'N' \n" + // 
			" ORDER BY NCT.CODIGO";
	private final static String QUERY_CONTRA_PARTIDA = "SELECT  \n" + // 
			" '0013'  \n" + // 
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0') \n" + // 
			" || LPAD(NC.CODIGO_SUCURSAL_ORIGEN, 4, '0') \n" + // 
			" || LPAD(NCT.COD_SUC_DEST_CONTPART, 4, '0') \n" + // 
			" || '3' || LPAD(NCT.CODIGO, 9, '0') \n" + // EL 3 IDENTIFICA A LAS CONTRA PARTIDAS
			" || TO_CHAR(NCT.FECHA_CONTABLE, 'YYYYMMDD') \n" + // 
			" || RPAD(NCT.CONTRAPARTIDA_CONTABLE, 15, ' ') \n" + // 
			" || CASE WHEN NCT.NATURALEZA2 = 'D' THEN '10' ELSE '01' END \n" + // 
			" || CASE WHEN NCT.NATURALEZA2 = 'D' THEN REPLACE(REPLACE(LTRIM(TO_CHAR(NCT.MONTO, '0000000000000D00')), ',', ''), '.', '') || '000000000000000' ELSE '000000000000000' || REPLACE(REPLACE(LTRIM(TO_CHAR(NCT.MONTO, '0000000000000D00')), ',', ''), '.', '')  END \n" + // 
			" || RPAD(NCT.CODIGO_DIVISA, 3, ' ') \n" + // 
			" || LPAD(NC.CODIGO_CONCEPTO, 3, '0') \n" + // 
			" || NVL(NCT.TIPO_IDENTIFICACION2, '0') \n" + // 
			" || LPAD(NVL(NCT.NUMERO_IDENTIFICACION2, '0'), 15, '0') \n" + // 
			" || NVL(NCT.DIGITO_VERIFICACION2,'0') \n" + // 
			" || LPAD(NCT.REFERENCIA2, 12, '0') \n" + // 
			" || LPAD(NVL(NCT.CONTRATO2, '0'), 18, '0') \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_TIPO_PERDIDA, '0'), 4, '0') \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_CLASE_RIESGO, '0'), 3, '0') \n" + // 
			" || '0' \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_PRODUCTO, '0'), 11, '0') \n" + // 
			/** Incidencia COL396113I000838**/
			" || LPAD(NVL(TO_CHAR(NCTR.FECHA_EVENTO, 'YYYYMMDD'), '0'), 8, '0')\n" + 
			" || LPAD(NVL(NCTR.CODIGO_PROCESO, '0'), 6, '0') \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_LINEA_OPER, '0'), 4, '0') \n" + // 
			/**" || REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' ')   \n" + **/
			" || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(NCT.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')   \n" + //
			" || SUBSTR(NC.NUMERO_RADICACION, 5)  \n" + // 
			" || CASE WHEN NCTR.HORA_INICIO_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_INICIO_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_INICIO_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_INICIO_EVENTO,4,2)||'.00.000000' END  ELSE  '0001-01-01-00.00.00.000000' END \n" + // 
            " || CASE WHEN NCTR.HORA_FIN_EVENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_FIN_EVENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_FIN_EVENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' ELSE  TO_CHAR(NCTR.FECHA_FIN_EVENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_FIN_EVENTO,0,2)||'.'||SUBSTR(NCTR.HORA_FIN_EVENTO,4,2)||'.00.000000' END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
            " || CASE WHEN NCTR.HORA_DESCUBRIMIENTO IS NOT NULL THEN CASE WHEN SUBSTR(NCTR.HORA_DESCUBRIMIENTO,7,3) = 'P.M' THEN  TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||TO_CHAR(to_number(SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2))+12)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000' ELSE TO_CHAR(NCTR.FECHA_DESCUBRIMIENTO, 'YYYY-MM-DD')||'-'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,0,2)||'.'||SUBSTR(NCTR.HORA_DESCUBRIMIENTO,4,2)||'.00.000000'  END ELSE  '0001-01-01-00.00.00.000000' END \n" + //
			" FROM NC_NOTA_CONTABLE_TEMA NCT \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE NC ON NCT.CODIGO_NOTA_CONTABLE = NC.CODIGO \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE_TEMA_RIESGO NCTR ON NCTR.CODIGO_NOTA_CONTABLE = NC.CODIGO AND NCTR.CODIGO_TEMA = NCT.CODIGO " + // 
			" WHERE NC.CODIGO = ? ORDER BY NCT.CODIGO";

	public NotaContableTemaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableTema());
	}

	@Override
	public void internalUpdate(Connection con, NotaContableTema row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	public void deleteByNotaContable(Connection con, int codigoNotaContable, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByNotaContable(con, codigoNotaContable);
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar temas de la nota contable", Tema.class.getSimpleName(), "" + codigoNotaContable);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public Collection<NotaContableTema> findByNotaContable(int codigoNotaContable) throws Exception {
		return obtenerLista(SQL_SELECT_COMPLETO_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public NotaContableTema findByNotaContableAndTema(int codigoNotaContable, int codigoTema) throws Exception {
		return getByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_SENTENCE, new Object[] { codigoNotaContable, codigoTema });
	}

	public Collection<NotaContableTema> findByConceptoAndEstadoAndTema(int codigoConcepto, String estado, int codigoTema) throws Exception {
		return obtenerLista(SQL_SELECT_BY_CONCEPTO_AND_ESTADO_AND_TEMA_SENTENCE, codigoConcepto, estado, codigoTema);
	}

	public String getXMLDataByNotaContable(Connection con, int codigoNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public Collection<String> getInfoPartidaArchAltamira(Integer notaContableCodigo) throws Exception {
		Collection<String> ret = new ArrayList<String>();
		ret.addAll(findToStringByGeneral(QUERY_PARTIDA, new Object[] { notaContableCodigo }));
		ret.addAll(findToStringByGeneral(QUERY_PARTIDA_IMPUESTO, new Object[] { notaContableCodigo }));
		return ret;
	}

	public Collection<String> getInfoContraPartidaArchAltamira(Integer notaContableCodigo) throws Exception {
		Collection<String> ret = new ArrayList<String>();
		ret.addAll(findToStringByGeneral(QUERY_CONTRA_PARTIDA, new Object[] { notaContableCodigo }));
		ret.addAll(findToStringByGeneral(QUERY_CONTRA_PARTIDA_IMPUESTO, new Object[] { notaContableCodigo }));
		return ret;
	}

	@Override
	protected Object[] getDataToAdd(Connection con, NotaContableTema row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoTema(), row.getFechaContable(), row.getPartidaContable(), row.getNaturalezaPartidaContable(), row.getContrapartidaContable(),
				row.getNaturalezaContrapartidaContable(), row.getCodigoSucursalDestinoPartida(), row.getCodigoSucursalDestinoContraPartida(), row.getCodigoDivisa(), row.getMonto().doubleValue(), row.getReferencia1(), row.getReferencia2(),
				row.getTipoIdentificacion1(), row.getNumeroIdentificacion1(), row.getDigitoVerificacion1(), row.getNombreCompleto1(), row.getTipoIdentificacion2(), row.getNumeroIdentificacion2(), row.getDigitoVerificacion2(),
				row.getNombreCompleto2(), row.getContrato1(), row.getContrato2(), row.getDescripcion(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableTema getResultSetToVO(ResultSet result) throws Exception {
		NotaContableTema row = new NotaContableTema();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoTema(result.getInt(3));
		row.setFechaContable(result.getDate(4));
		row.setPartidaContable(result.getString(5) != null ? result.getString(5) : "");
		row.setNaturalezaPartidaContable(result.getString(6) != null ? result.getString(6) : "");
		row.setContrapartidaContable(result.getString(7) != null ? result.getString(7) : "");
		row.setNaturalezaContrapartidaContable(result.getString(8) != null ? result.getString(8) : "");
		row.setCodigoSucursalDestinoPartida(result.getString(9) != null ? result.getString(9) : "");
		row.setCodigoSucursalDestinoContraPartida(result.getString(10) != null ? result.getString(10) : "");
		row.setCodigoDivisa(result.getString(11) != null ? result.getString(11) : "");
		row.setMonto(result.getDouble(12));
		row.setReferencia1(result.getString(13) != null ? result.getString(13) : "");
		row.setReferencia2(result.getString(14) != null ? result.getString(14) : "");
		row.setTipoIdentificacion1(result.getString(15) != null ? result.getString(15) : "");
		row.setNumeroIdentificacion1(result.getString(16) != null ? result.getString(16) : "");
		row.setDigitoVerificacion1(result.getString(17) != null ? result.getString(17) : "");
		row.setNombreCompleto1(result.getString(18) != null ? result.getString(18) : "");
		row.setTipoIdentificacion2(result.getString(19) != null ? result.getString(19) : "");
		row.setNumeroIdentificacion2(result.getString(20) != null ? result.getString(20) : "");
		row.setDigitoVerificacion2(result.getString(21) != null ? result.getString(21) : "");
		row.setNombreCompleto2(result.getString(22) != null ? result.getString(22) : "");
		row.setContrato1(result.getString(23) != null ? result.getString(23) : "");
		row.setContrato2(result.getString(24) != null ? result.getString(24) : "");
		row.setDescripcion(result.getString(25) != null ? result.getString(25) : "");
		row.setNumeroAsiento(result.getString(26) != null ? result.getString(26) : "");
		row.setNumeroApunte(result.getString(27) != null ? result.getString(27) : "");

		return row;
	}

	public void updateFecha(NotaContableTema t, int codUsuario) throws Exception {
		update(t, codUsuario, SQL_UPDATE_FECHA_SENTENCE, new Object[] { t.getFechaContable(), t.getCodigo() });
	}

	// public void updateAsientoContra(NotaContableTema registro) throws Exception {
	// throw new Exception("updateAsientoContra: no implementado an");
	// }
}