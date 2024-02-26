package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.NotaContable;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

public class NotaContableDAO extends CommonSeqDAO<NotaContable> {

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA_REGISTRO_MODULO, FECHA_HORA_REGISTRO_ALTAMIRA, CODIGO_SUCURSAL_ORIGEN, CODIGO_CONCEPTO, CODIGO_TIPO_EVENTO, NUMERO_RADICACION, TIPO_NOTA, DESCRIPCION, ASIENTO_CONTABLE, ESTADO";

	private static String cs_TABLA = "NC_NOTA_CONTABLE";
	private static String cs_PK = "CODIGO";

	private static String cs_SELECT_MAX_CODE_BY_SUCURSAL_ORIGEN_SENTENCE = "SELECT COUNT(*) FROM " + cs_TABLA + " WHERE (NUMERO_RADICACION LIKE ':param%')";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO_SUCURSAL_ORIGEN";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_SUCURSAL_ORIGEN";

	private static String SQL_SELECT_BY_NUMERO_RADICACION_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NUMERO_RADICACION = ?)";

	private static String SQL_SELECT_BY_ESTADO_INSTANCIA_SENTENCE = "SELECT A.CODIGO, A.FECHA_HORA_REGISTRO_MODULO, A.FECHA_HORA_REGISTRO_ALTAMIRA, A.CODIGO_SUCURSAL_ORIGEN, A.CODIGO_CONCEPTO, A.CODIGO_TIPO_EVENTO, A.NUMERO_RADICACION, A.TIPO_NOTA, A.DESCRIPCION, A.ASIENTO_CONTABLE, A.ESTADO FROM NC_NOTA_CONTABLE A, NC_INSTANCIA B WHERE (A.CODIGO = B.CODIGO_NOTA_CONTABLE) AND (B.ESTADO = ?) ORDER BY A.CODIGO";

	private static String SQL_SELECT_BY_PRECIERRE = "SELECT DISTINCT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO FROM NC_NOTA_CONTABLE NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA
			+ " SET FECHA_HORA_REGISTRO_ALTAMIRA = ?, CODIGO_SUCURSAL_ORIGEN = ?, CODIGO_CONCEPTO = ?, CODIGO_TIPO_EVENTO = ?, DESCRIPCION = ?, ESTADO = ? WHERE (CODIGO = ?)";

	private static String SQL_SELECT_BY_PK_SENTENCE = "SELECT NC.CODIGO \"codigo\", NC.FECHA_HORA_REGISTRO_MODULO \"fechaRegistroModulo\", NC.FECHA_HORA_REGISTRO_ALTAMIRA \"fechaRegistroAltamira\", NC.CODIGO_SUCURSAL_ORIGEN \"codigoSucursalOrigen\", NC.CODIGO_CONCEPTO \"codigoConcepto\", NC.CODIGO_TIPO_EVENTO \"codigoTipoEvento\", NC.NUMERO_RADICACION \"numeroRadicacion\", NC.TIPO_NOTA \"tipoNota\", NC.DESCRIPCION \"descripcion\", NC.ASIENTO_CONTABLE \"asientoContable\", NC.ESTADO \"estado\", CO.NOMBRE \"concepto__nombre\", EV.NOMBRE \"tipoEvento__nombre\", SU.NOMBRE \"sucursalOrigen__nombre\" FROM NC_NOTA_CONTABLE NC LEFT JOIN NC_CONCEPTO CO ON (CO.CODIGO = NC.CODIGO_CONCEPTO) LEFT JOIN NC_TIPO_EVENTO EV ON (NC.CODIGO_TIPO_EVENTO = EV.CODIGO) LEFT JOIN NC_SUCURSAL SU ON (SU.CODIGO = NC.CODIGO_SUCURSAL_ORIGEN) WHERE NC.CODIGO = ?";

	private static String sql_SELECT_TO_BACKUP = "SELECT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO"
			+ " FROM " + cs_TABLA + " NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO < 6 AND FECHA_HORA_REGISTRO_MODULO < ?";

	private static String sql_SELECT_ANULADOS = "SELECT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO"
			+ " FROM " + cs_TABLA + " NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO = 9 AND FECHA_HORA_REGISTRO_MODULO < ?";

	private static String sql_SELECT_A_ANULAR = "SELECT NC.CODIGO, NC.FECHA_HORA_REGISTRO_MODULO, NC.FECHA_HORA_REGISTRO_ALTAMIRA, NC.CODIGO_SUCURSAL_ORIGEN, NC.CODIGO_CONCEPTO, NC.CODIGO_TIPO_EVENTO, NC.NUMERO_RADICACION, NC.TIPO_NOTA, NC.DESCRIPCION, NC.ASIENTO_CONTABLE, NC.ESTADO"
			+ " FROM " + cs_TABLA + " NC LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO_NOTA_CONTABLE = NC.CODIGO) WHERE INS.ESTADO < 6 AND FECHA_HORA_REGISTRO_MODULO < ?";

	private final static String QUERY_EXPORT = "  SELECT  \r\n" +
			"     '0013'  \r\n" +
			"    || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(k.DESCRIPCION, ' '), 500, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ') \r\n" +
			"    || LPAD('0','12','0')   \r\n" +
			"    || LPAD(' ','12',' ')   \r\n" +
			"    || LPAD(' ','1',' ')    \r\n" +
			"    || CASE WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.PARTIDA_CONTABLE,'0') ) THEN LPAD(NVL(k.COD_SUC_DEST_PART,'0'),'4','0') WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.CONTRAPARTIDA_CONTABLE,'0') )THEN LPAD(NVL(k.COD_SUC_DEST_CONTPART,'0'),'4','0') ELSE LPAD('0','4','0') END  \r\n" +
			"    || LPAD(' ','2',' ')  \r\n" +
			"    || LPAD((SELECT EXTRACT(YEAR FROM sysdate) FROM dual),'4','0')  \r\n" +
			"    || LPAD((SELECT EXTRACT(MONTH FROM sysdate) FROM dual),'2','0')  \r\n" +
			"    || LPAD((SELECT TO_CHAR(SYSDATE,'YYYY-MM-DD') FROM dual),'10',' ')  \r\n" +
			"    || LPAD((SELECT EXTRACT(YEAR FROM k.FECHA_CONTABLE) FROM dual),'4','0')  \r\n" +
			"    || LPAD((SELECT EXTRACT(MONTH FROM k.FECHA_CONTABLE) FROM dual),'2','0')  \r\n" +
			"    || LPAD(NVL(TO_CHAR(k.FECHA_CONTABLE,'YYYY-MM-DD'),' '),'10','0')  \r\n" +
			"    || LPAD(NVL(TO_CHAR(r.FECHA_EVENTO,'YYYY-MM-DD'),' '),'10','0')    \r\n" +
			"    || CASE WHEN r.HORA_INICIO_EVENTO IS NOT NULL THEN  CASE WHEN ((SUBSTR(r.HORA_INICIO_EVENTO ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_INICIO_EVENTO,0,2))+12)||(SUBSTR(r.HORA_INICIO_EVENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_INICIO_EVENTO,0,2)))||(SUBSTR(r.HORA_INICIO_EVENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"    || CASE WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.PARTIDA_CONTABLE,' ') ) THEN LPAD(k.PARTIDA_CONTABLE ,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || CASE WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.CONTRAPARTIDA_CONTABLE,' ') ) THEN LPAD(k.CONTRAPARTIDA_CONTABLE,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || LPAD(NVL(r.CODIGO_TIPO_PERDIDA,'0'),'4','0')  \r\n" +
			"    || CASE WHEN v.CODIGO IS NOT NULL THEN LPAD(SUBSTR(v.CODIGO,1,1),'1','0') ELSE LPAD('0','1','0') END  \r\n" +
			"    || LPAD(NVL(v.CODIGO,'0'),'3','0')  \r\n" +
			"    || LPAD('0','18','0')   \r\n" +
			"    || LPAD(NVL(a.CODIGO_SUCURSAL_ORIGEN,'0'),'4','0')  \r\n" +
			"    || CASE WHEN k.NATURALEZA1='H'  THEN '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) END   \r\n" +
			"    || CASE WHEN ((NVL(k.PARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA1='H') AND NVL(r.TIPO_RECUPERACION,' ')='R_SEGURO' AND r.APP_RECUPERACION='S' ) THEN  '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE CASE WHEN ((NVL(k.PARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA1='D') AND NVL(r.TIPO_RECUPERACION,' ')='R_SEGURO' AND r.APP_RECUPERACION='S' ) THEN '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||LPAD('0','17','0') END  END \r\n" +
			"    || CASE WHEN ((NVL(k.PARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA1='H') AND NVL(r.TIPO_RECUPERACION,' ')='R_DIRECTA' AND r.APP_RECUPERACION='S') THEN  '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE CASE WHEN ((NVL(k.PARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA1='H') AND NVL(r.TIPO_RECUPERACION,' ')='R_DIRECTA' AND r.APP_RECUPERACION='S') THEN '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||LPAD('0','17','0') END  END   \r\n" +
			"    || LPAD('0' ,'18','0')  \r\n" +
			"    || RPAD('BBVA COLOMBIA','60',' ')  \r\n" +
			"    || LPAD(NVL(g.CODIGO,'0'),'4','0')  \r\n" +
			"    || TRANSLATE(RPAD(NVL(g.NOMBRE,' '),'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD(NVL(TO_CHAR(r.FECHA_FIN_EVENTO,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"    || CASE WHEN r.HORA_FIN_EVENTO IS NOT NULL THEN CASE WHEN ((SUBSTR(r.HORA_FIN_EVENTO ,7,3)) = 'P.M')THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_FIN_EVENTO,0,2))+12)||(SUBSTR(r.HORA_FIN_EVENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_FIN_EVENTO,0,2)))||(SUBSTR(r.HORA_FIN_EVENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"    ||  LPAD(NVL(TO_CHAR(r.FECHA_DESCUBRIMIENTO,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"    || CASE WHEN r.HORA_DESCUBRIMIENTO IS NOT NULL THEN CASE WHEN ((SUBSTR(r.HORA_DESCUBRIMIENTO ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_DESCUBRIMIENTO,0,2))+12)||(SUBSTR(r.HORA_DESCUBRIMIENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_DESCUBRIMIENTO,0,2)))||(SUBSTR(r.HORA_DESCUBRIMIENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"    || LPAD((SELECT TO_CHAR(sysdate, 'HH24:MI:SS') FROM DUAL),'8',' ')  \r\n" +
			"    || LPAD(NVL(TO_CHAR(r.FECHA_RECUPERACION,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"    || CASE WHEN ((r.HORA_RECUPERACION IS NOT NULL) AND (SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')),0,2))+12)||(SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,3,4))||'00','8','0') WHEN ((r.HORA_RECUPERACION IS NOT NULL) AND (SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,7,3)) = 'A.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_RECUPERACION,0,2)))||(SUBSTR(r.HORA_RECUPERACION ,3,4))||'00','8','0') ELSE LPAD(' ','8',' ') END   \r\n" +
			"    || TRANSLATE(RPAD(NVL(h.NOMBRE,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || TRANSLATE(RPAD(NVL(j.NOMBRE,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || CASE WHEN (k.CONTRATO1 IS NOT NULL) THEN LPAD(k.CONTRATO1,'30','0') ELSE LPAD('0','30','0') END  \r\n" +
			"    || CASE WHEN (k.CONTRATO2 IS NOT NULL) THEN LPAD(k.CONTRATO2,'30','0') ELSE LPAD('0','30','0') END  \r\n" +
			"    || CASE WHEN (k.NUMERO_IDENTIFICACION1 IS NOT NULL) THEN LPAD(k.NUMERO_IDENTIFICACION1,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || CASE WHEN (k.NUMERO_IDENTIFICACION2 IS NOT NULL) THEN LPAD(k.NUMERO_IDENTIFICACION2,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || CASE WHEN (k.NOMBRE_COMPLETO1 IS NOT NULL) THEN TRANSLATE(RPAD(k.NOMBRE_COMPLETO1,'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ') ELSE LPAD(' ','80',' ') END  \r\n" +
			"    || CASE WHEN (k.NOMBRE_COMPLETO2 IS NOT NULL) THEN TRANSLATE(RPAD(k.NOMBRE_COMPLETO2,'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ') ELSE LPAD(' ','80',' ') END  \r\n" +
			"    || RPAD('NOTAS CONTABLES','40',' ')  \r\n" +
			"    || TRANSLATE(RPAD(NVL(m.DESCRIPCION,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || CASE WHEN (proce.CODIGO_PROCESO_ARIS IS NOT NULL) THEN LPAD(proce.CODIGO_PROCESO_ARIS,'10',' ') ELSE LPAD(' ','10',' ') END  \r\n" +
			"    || TRANSLATE(RPAD(NVL(proce.NOMBRE,' '),'200',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || CASE WHEN k.NATURALEZA1='H'  THEN '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) END  \r\n" +
			"    || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(r.NOMBRE_OUTSOURCE, ' '), 60, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||LPAD('0','15','0')  \r\n" +
			"    ||LPAD(NVL(q.CODIGO,' '),'4',' ')  \r\n" +
			"    ||TRANSLATE(RPAD(NVL(q.NOMBRE,' '),'150',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||LPAD(NVL(s.CODIGO,' '),'6',' ')  \r\n" +
			"    ||TRANSLATE(RPAD(NVL(s.NOMBRE,' '),'150',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||LPAD(NVL(k.CODIGO_DIVISA,' '),'3',' ')  \r\n" +
			"    ||LPAD(NVL(a.NUMERO_RADICACION,'0'),'15','0')  \r\n" +
			"    FROM CONTABLES.NC_NOTA_CONTABLE a   \r\n" +
			"    JOIN CONTABLES.NC_NOTA_CONTABLE_TEMA k ON a.CODIGO=k.CODIGO_NOTA_CONTABLE  \r\n" +
			"    JOIN CONTABLES.NC_NOTA_CONTABLE_TEMA_RIESGO r ON r.CODIGO_NOTA_CONTABLE=a.CODIGO AND r.CODIGO_TEMA=k.CODIGO  \r\n" +
			"    JOIN CONTABLES.NC_TEMA t ON t.CODIGO=k.CODIGO_TEMA   \r\n" +
			"    JOIN CONTABLES.NC_CLASE_RIESGO v ON v.CODIGO=r.CODIGO_CLASE_RIESGO  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_LINE_OPER g ON g.CODIGO=r.CODIGO_LINEA_OPER  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_PROD h ON h.CODIGO=r.CODIGO_PRODUCTO  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_SUBPROD j ON r.CODIGO_SUBPRODUCTO=j.CODIGO  \r\n" +
			"    JOIN CONTABLES.NC_CANAL m ON m.CODIGO=r.CODIGO_CANAL  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_PROC proce ON proce.CODIGO=r.CODIGO_PROCESO  \r\n" +
			"    JOIN CONTABLES.NC_OPER_N2_SFC q ON q.CODIGO=r.CODIGO_OPER_N2_SFC AND v.CODIGO=q.CODIGO_NC_CLASE_RIESGO  \r\n" +
			"    JOIN CONTABLES.NC_OPER_N3_SFC s ON s.CODIGO=r.CODIGO_OPER_N3_SFC AND s.CODIGO_N1_SFC=v.CODIGO AND s.CODIGO_N2_SFC=q.CODIGO \r\n" +
			"    JOIN CONTABLES.NC_INSTANCIA I ON I.CODIGO_NOTA_CONTABLE=a.CODIGO   \r\n" +
			"    JOIN CONTABLES.NC_PUC p ON (p.INDICADOR_SIRO='T' AND (p.NUMERO_CUENTA=k.PARTIDA_CONTABLE))  \r\n" +
			"    WHERE t.RIESGO_OPERACIONAL='S' AND I.ESTADO='5'  \r\n" +
			"    union all  \r\n" +
			"    SELECT  \r\n" +
			"    '0013'  \r\n" +
			"  || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(a.DESCRIPCION, ' '), 500, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || LPAD('0','12','0')  \r\n" +
			"  || LPAD(' ','12',' ')  \r\n" +
			"  || LPAD(' ','1',' ')  \r\n" +
			"  || LPAD(NVL(k.COD_SUC_DESTINO,'0'),'4','0')  \r\n" +
			"  || LPAD(' ','2',' ')  \r\n" +
			"  || LPAD((SELECT EXTRACT(YEAR FROM sysdate) FROM dual),'4','0')  \r\n" +
			"  || LPAD((SELECT EXTRACT(MONTH FROM sysdate) FROM dual),'2','0')  \r\n" +
			"  || LPAD((SELECT TO_CHAR(SYSDATE,'YYYY-MM-DD') FROM dual),'10',' ')  \r\n" +
			"  || LPAD((SELECT EXTRACT(YEAR FROM k.FECHA_CONTABLE) FROM dual),'4','0')  \r\n" +
			"  || LPAD((SELECT EXTRACT(MONTH FROM k.FECHA_CONTABLE) FROM dual),'2','0')  \r\n" +
			"  || LPAD(NVL(TO_CHAR(k.FECHA_CONTABLE,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"  || LPAD(NVL(TO_CHAR(r.FECHA_EVENTO,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"  || CASE WHEN r.HORA_INICIO_EVENTO IS NOT NULL THEN CASE WHEN ((SUBSTR(r.HORA_INICIO_EVENTO ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_INICIO_EVENTO,0,2))+12)||(SUBSTR(r.HORA_INICIO_EVENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_INICIO_EVENTO,0,2)))||(SUBSTR(r.HORA_INICIO_EVENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"  || LPAD(NVL(k.CUENTA_CONTABLE,'0'),'15','0')  \r\n" +
			"  || LPAD('0','15','0')  \r\n" +
			"  || LPAD(NVL(r.CODIGO_TIPO_PERDIDA,'0'),'4','0')  \r\n" +
			"  || CASE WHEN v.CODIGO IS NOT NULL THEN LPAD(SUBSTR(v.CODIGO,1,1),'1','0') ELSE LPAD('0','1','0') END  \r\n" +
			"  || LPAD(NVL(v.CODIGO,'0'),'3','0')  \r\n" +
			"  || LPAD('0','18','0')   \r\n" +
			"  || LPAD(NVL(a.CODIGO_SUCURSAL_ORIGEN,'0'),'4','0')  \r\n" +
			"  || CASE WHEN k.NATURALEZA='H'  THEN '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) END  \r\n" +
			"  || CASE WHEN (NVL(k.CUENTA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA='H' AND NVL(r.TIPO_RECUPERACION,' ')='R_SEGURO') THEN  '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE  CASE WHEN (NVL(k.CUENTA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA='D' AND NVL(r.TIPO_RECUPERACION,' ')='R_SEGURO')  THEN '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||LPAD('0','17','0') END  END  \r\n" +
			"  || CASE WHEN (NVL(k.CUENTA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA='H' AND NVL(r.TIPO_RECUPERACION,' ')='R_DIRECTA') THEN '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE  CASE WHEN (NVL(k.CUENTA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA='D' AND NVL(r.TIPO_RECUPERACION,' ')='R_DIRECTA') THEN '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||LPAD('0','17','0') END  END  \r\n" +
			"  || LPAD('0' ,'18','0')  \r\n" +
			"  || RPAD('BBVA COLOMBIA','60',' ')  \r\n" +
			"  || LPAD(NVL(g.CODIGO,'0'),'4','0')  \r\n" +
			"  || TRANSLATE(RPAD(NVL(g.NOMBRE,' '),'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || LPAD (' ','40',' ')  \r\n" +
			"  || LPAD (' ','40',' ')  \r\n" +
			"  || LPAD (' ','40',' ')  \r\n" +
			"  || LPAD (' ','40',' ')   \r\n" +
			"  || LPAD (' ','40',' ')  \r\n" +
			"  || LPAD(NVL(TO_CHAR(r.FECHA_FIN_EVENTO,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"  || CASE WHEN r.HORA_FIN_EVENTO IS NOT NULL THEN CASE WHEN ((SUBSTR(r.HORA_FIN_EVENTO ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_FIN_EVENTO,0,2))+12)||(SUBSTR(r.HORA_FIN_EVENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_FIN_EVENTO,0,2)))||(SUBSTR(r.HORA_FIN_EVENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"  || LPAD(NVL(TO_CHAR(r.FECHA_DESCUBRIMIENTO,'YYYY-MM-DD'),' '),'10',' ')   \r\n" +
			"  || CASE WHEN r.HORA_DESCUBRIMIENTO IS NOT NULL THEN CASE WHEN ((SUBSTR(r.HORA_DESCUBRIMIENTO ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_DESCUBRIMIENTO,0,2))+12)||(SUBSTR(r.HORA_DESCUBRIMIENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_DESCUBRIMIENTO,0,2)))||(SUBSTR(r.HORA_DESCUBRIMIENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"  || LPAD((SELECT TO_CHAR(sysdate, 'HH24:MI:SS') FROM DUAL),'8',' ')  \r\n" +
			"  || LPAD(NVL(TO_CHAR(r.FECHA_RECUPERACION,'YYYY-MM-DD'),' '),'10',' ')   \r\n" +
			"  || CASE WHEN ((r.HORA_RECUPERACION IS NOT NULL) AND (SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')),0,2))+12)||(SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,3,4))||'00','8','0') WHEN ((r.HORA_RECUPERACION IS NOT NULL) AND (SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,7,3)) = 'A.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_RECUPERACION,0,2)))||(SUBSTR(r.HORA_RECUPERACION ,3,4))||'00','8','0') ELSE LPAD(' ','8',' ') END  \r\n" +
			"  || TRANSLATE(RPAD(NVL(h.NOMBRE,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || TRANSLATE(RPAD(NVL(j.NOMBRE,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || CASE WHEN (k.CONTRATO IS NOT NULL) THEN LPAD(k.CONTRATO,'30','0') ELSE LPAD('0','30','0') END  \r\n" +
			"  || LPAD('0','30','0')  \r\n" +
			"  || CASE WHEN (k.NUMERO_IDENTIFICACION IS NOT NULL) THEN LPAD(k.NUMERO_IDENTIFICACION,'15','0') ELSE LPAD('0','15','0') END   \r\n" +
			"  || LPAD('0','15','0')  \r\n" +
			"  || CASE WHEN (k.NOMBRE_COMPLETO IS NOT NULL) THEN TRANSLATE(RPAD(k.NOMBRE_COMPLETO,'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ') ELSE LPAD(' ','80',' ') END  \r\n" +
			"  || LPAD(' ','80',' ')  \r\n" +
			"  || RPAD('NOTAS CONTABLES','40',' ')  \r\n" +
			"  || TRANSLATE(RPAD(NVL(m.DESCRIPCION,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || CASE WHEN (proce.CODIGO_PROCESO_ARIS IS NOT NULL) THEN LPAD(proce.CODIGO_PROCESO_ARIS,'10',' ') ELSE LPAD(' ','10',' ')  END  \r\n" +
			"  || TRANSLATE(RPAD(proce.NOMBRE,'200',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || CASE WHEN k.NATURALEZA='H'  THEN '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) END  \r\n" +
			"  || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(r.NOMBRE_OUTSOURCE, ' '), 60, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || LPAD('0','15','0')  \r\n" +
			"  || LPAD(NVL(q.CODIGO,' '),'4',' ')  \r\n" +
			"  || TRANSLATE(RPAD(NVL(q.NOMBRE,' '),'150',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || LPAD(NVL(s.CODIGO,' '),'6',' ')  \r\n" +
			"  || TRANSLATE(RPAD(NVL(s.NOMBRE,' '),'150',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"  || LPAD(NVL(k.CODIGO_DIVISA,' '),'3',' ')  \r\n" +
			"  || LPAD(NVL(a.NUMERO_RADICACION,'0'),'15','0')   \r\n" +
			"  FROM CONTABLES.NC_NOTA_CONTABLE a  \r\n" +
			"  JOIN CONTABLES.NC_NOTA_CONT_REGISTRO_LIBRE k ON a.CODIGO=k.CODIGO_NOTA_CONTABLE  \r\n" +
			"  JOIN CONTABLES.NC_NOTA_CONTABLE_TEMA_RIESGO r ON r.CODIGO_NOTA_CONTABLE=a.CODIGO  AND r.CODIGO_TEMA=k.CODIGO  \r\n" +
			"  JOIN CONTABLES.NC_CLASE_RIESGO v ON v.CODIGO=r.CODIGO_CLASE_RIESGO  \r\n" +
			"  JOIN CONTABLES.NC_RIES_OPER_LINE_OPER g ON g.CODIGO=r.CODIGO_LINEA_OPER  \r\n" +
			"  JOIN CONTABLES.NC_RIES_OPER_PROD h ON h.CODIGO=r.CODIGO_PRODUCTO  \r\n" +
			"  JOIN CONTABLES.NC_RIES_OPER_SUBPROD j ON r.CODIGO_SUBPRODUCTO=j.CODIGO  \r\n" +
			"  JOIN CONTABLES.NC_CANAL m ON m.CODIGO=r.CODIGO_CANAL  \r\n" +
			"  JOIN CONTABLES.NC_RIES_OPER_PROC proce ON proce.CODIGO=r.CODIGO_PROCESO  \r\n" +
			"  JOIN CONTABLES.NC_OPER_N2_SFC q ON q.CODIGO=r.CODIGO_OPER_N2_SFC AND v.CODIGO=q.CODIGO_NC_CLASE_RIESGO \r\n" +
			"  JOIN CONTABLES.NC_OPER_N3_SFC s ON s.CODIGO=r.CODIGO_OPER_N3_SFC AND s.CODIGO_N1_SFC=v.CODIGO AND s.CODIGO_N2_SFC=q.CODIGO \r\n" +
			"  JOIN CONTABLES.NC_INSTANCIA I ON I.CODIGO_NOTA_CONTABLE=a.CODIGO  \r\n" +
			"  JOIN CONTABLES.NC_PUC p ON (p.INDICADOR_SIRO='T' AND (p.NUMERO_CUENTA=k.CUENTA_CONTABLE))  \r\n" +
			"  WHERE k.CUENTA_CONTABLE IN (SELECT p.NUMERO_CUENTA FROM NC_PUC p WHERE INDICADOR_SIRO='T' AND INDICADOR_SIRO IS NOT NULL)  \r\n" +
			"  AND I.ESTADO='5' \r\n" +
			"    union all  \r\n" +
			"  SELECT  \r\n" +
			"     '0013'  \r\n" +
			"    || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(k.DESCRIPCION, ' '), 500, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ') \r\n" +
			"    || LPAD('0','12','0')   \r\n" +
			"    || LPAD(' ','12',' ')   \r\n" +
			"    || LPAD(' ','1',' ')    \r\n" +
			"    || CASE WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.PARTIDA_CONTABLE,'0') ) THEN LPAD(NVL(k.COD_SUC_DEST_PART,'0'),'4','0') WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.CONTRAPARTIDA_CONTABLE,'0') )THEN LPAD(NVL(k.COD_SUC_DEST_CONTPART,'0'),'4','0') ELSE LPAD('0','4','0') END  \r\n" +
			"    || LPAD(' ','2',' ')  \r\n" +
			"    || LPAD((SELECT EXTRACT(YEAR FROM sysdate) FROM dual),'4','0')  \r\n" +
			"    || LPAD((SELECT EXTRACT(MONTH FROM sysdate) FROM dual),'2','0')  \r\n" +
			"    || LPAD((SELECT TO_CHAR(SYSDATE,'YYYY-MM-DD') FROM dual),'10',' ')  \r\n" +
			"    || LPAD((SELECT EXTRACT(YEAR FROM k.FECHA_CONTABLE) FROM dual),'4','0')  \r\n" +
			"    || LPAD((SELECT EXTRACT(MONTH FROM k.FECHA_CONTABLE) FROM dual),'2','0')  \r\n" +
			"    || LPAD(NVL(TO_CHAR(k.FECHA_CONTABLE,'YYYY-MM-DD'),' '),'10','0')  \r\n" +
			"    || LPAD(NVL(TO_CHAR(r.FECHA_EVENTO,'YYYY-MM-DD'),' '),'10','0')    \r\n" +
			"    || CASE WHEN r.HORA_INICIO_EVENTO IS NOT NULL THEN  CASE WHEN ((SUBSTR(r.HORA_INICIO_EVENTO ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_INICIO_EVENTO,0,2))+12)||(SUBSTR(r.HORA_INICIO_EVENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_INICIO_EVENTO,0,2)))||(SUBSTR(r.HORA_INICIO_EVENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"    || CASE WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.PARTIDA_CONTABLE,' ') ) THEN LPAD(k.PARTIDA_CONTABLE ,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || CASE WHEN (p.INDICADOR_SIRO='T' AND p.NUMERO_CUENTA=NVL(k.CONTRAPARTIDA_CONTABLE,' ') ) THEN LPAD(k.CONTRAPARTIDA_CONTABLE,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || LPAD(NVL(r.CODIGO_TIPO_PERDIDA,'0'),'4','0')  \r\n" +
			"    || CASE WHEN v.CODIGO IS NOT NULL THEN LPAD(SUBSTR(v.CODIGO,1,1),'1','0') ELSE LPAD('0','1','0') END  \r\n" +
			"    || LPAD(NVL(v.CODIGO,'0'),'3','0')  \r\n" +
			"    || LPAD('0','18','0')   \r\n" +
			"    || LPAD(NVL(a.CODIGO_SUCURSAL_ORIGEN,'0'),'4','0')  \r\n" +
			"    || CASE WHEN k.NATURALEZA2='H'  THEN '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) END   \r\n" +
			"    || CASE WHEN ((NVL(k.CONTRAPARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA2='H') AND NVL(r.TIPO_RECUPERACION,' ')='R_SEGURO' AND r.APP_RECUPERACION='S' ) THEN  '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE CASE WHEN ((NVL(k.CONTRAPARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA2='D') AND NVL(r.TIPO_RECUPERACION,' ')='R_SEGURO' AND r.APP_RECUPERACION='S' ) THEN  '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', ''))  ELSE '+'||LPAD('0','17','0') END  END  \r\n" +
			"    || CASE WHEN ((NVL(k.CONTRAPARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA2='H') AND NVL(r.TIPO_RECUPERACION,' ')='R_DIRECTA' AND r.APP_RECUPERACION='S') THEN  '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE CASE WHEN ((NVL(k.CONTRAPARTIDA_CONTABLE,'0') LIKE ('4%') AND k.NATURALEZA2='D') AND NVL(r.TIPO_RECUPERACION,' ')='R_DIRECTA' AND r.APP_RECUPERACION='S') THEN  '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', ''))  ELSE '+'||LPAD('0','17','0') END  END   \r\n" +
			"    || LPAD('0' ,'18','0')  \r\n" +
			"    || RPAD('BBVA COLOMBIA','60',' ')  \r\n" +
			"    || LPAD(NVL(g.CODIGO,'0'),'4','0')  \r\n" +
			"    || TRANSLATE(RPAD(NVL(g.NOMBRE,' '),'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD (' ','40',' ')  \r\n" +
			"    ||	LPAD(NVL(TO_CHAR(r.FECHA_FIN_EVENTO,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"    || CASE WHEN r.HORA_FIN_EVENTO IS NOT NULL THEN CASE WHEN ((SUBSTR(r.HORA_FIN_EVENTO ,7,3)) = 'P.M')THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_FIN_EVENTO,0,2))+12)||(SUBSTR(r.HORA_FIN_EVENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_FIN_EVENTO,0,2)))||(SUBSTR(r.HORA_FIN_EVENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"    ||  LPAD(NVL(TO_CHAR(r.FECHA_DESCUBRIMIENTO,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"    || CASE WHEN r.HORA_DESCUBRIMIENTO IS NOT NULL THEN CASE WHEN ((SUBSTR(r.HORA_DESCUBRIMIENTO ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_DESCUBRIMIENTO,0,2))+12)||(SUBSTR(r.HORA_DESCUBRIMIENTO ,3,4))||'00','8','0') ELSE LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_DESCUBRIMIENTO,0,2)))||(SUBSTR(r.HORA_DESCUBRIMIENTO ,3,4))||'00','8','0') END ELSE LPAD(' ','8',' ') END  \r\n" +
			"    || LPAD((SELECT TO_CHAR(sysdate, 'HH24:MI:SS') FROM DUAL),'8',' ')  \r\n" +
			"    || LPAD(NVL(TO_CHAR(r.FECHA_RECUPERACION,'YYYY-MM-DD'),' '),'10',' ')  \r\n" +
			"    || CASE WHEN ((r.HORA_RECUPERACION IS NOT NULL) AND (SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,7,3)) = 'P.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')),0,2))+12)||(SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,3,4))||'00','8','0') WHEN ((r.HORA_RECUPERACION IS NOT NULL) AND (SUBSTR(NVL(r.HORA_RECUPERACION,LPAD(' ','8',' ')) ,7,3)) = 'A.M') THEN LPAD(TO_CHAR(to_number(SUBSTR(r.HORA_RECUPERACION,0,2)))||(SUBSTR(r.HORA_RECUPERACION ,3,4))||'00','8','0') ELSE LPAD(' ','8',' ') END   \r\n" +
			"    || TRANSLATE(RPAD(NVL(h.NOMBRE,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || TRANSLATE(RPAD(NVL(j.NOMBRE,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || CASE WHEN (k.CONTRATO1 IS NOT NULL) THEN LPAD(k.CONTRATO1,'30','0') ELSE LPAD('0','30','0') END  \r\n" +
			"    || CASE WHEN (k.CONTRATO2 IS NOT NULL) THEN LPAD(k.CONTRATO2,'30','0') ELSE LPAD('0','30','0') END  \r\n" +
			"    || CASE WHEN (k.NUMERO_IDENTIFICACION1 IS NOT NULL) THEN LPAD(k.NUMERO_IDENTIFICACION1,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || CASE WHEN (k.NUMERO_IDENTIFICACION2 IS NOT NULL) THEN LPAD(k.NUMERO_IDENTIFICACION2,'15','0') ELSE LPAD('0','15','0') END  \r\n" +
			"    || CASE WHEN (k.NOMBRE_COMPLETO1 IS NOT NULL) THEN TRANSLATE(RPAD(k.NOMBRE_COMPLETO1,'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ') ELSE LPAD(' ','80',' ') END  \r\n" +
			"    || CASE WHEN (k.NOMBRE_COMPLETO2 IS NOT NULL) THEN TRANSLATE(RPAD(k.NOMBRE_COMPLETO2,'80',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ') ELSE LPAD(' ','80',' ') END  \r\n" +
			"    || RPAD('NOTAS CONTABLES','40',' ')  \r\n" +
			"    || TRANSLATE(RPAD(NVL(m.DESCRIPCION,' '),'40',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || CASE WHEN (proce.CODIGO_PROCESO_ARIS IS NOT NULL) THEN LPAD(proce.CODIGO_PROCESO_ARIS,'10',' ') ELSE LPAD(' ','10',' ') END  \r\n" +
			"    || TRANSLATE(RPAD(NVL(proce.NOMBRE,' '),'200',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    || CASE WHEN k.NATURALEZA2='H'  THEN '-'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) ELSE '+'||(REPLACE(REPLACE(LTRIM(TO_CHAR(r.IMPORTE_TOTAL, '000000000000000D00')), ',', ''), '.', '')) END  \r\n" +
			"    || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(r.NOMBRE_OUTSOURCE, ' '), 60, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||LPAD('0','15','0')  \r\n" +
			"    ||LPAD(NVL(q.CODIGO,' '),'4',' ')  \r\n" +
			"    ||TRANSLATE(RPAD(NVL(q.NOMBRE,' '),'150',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||LPAD(NVL(s.CODIGO,' '),'6',' ')  \r\n" +
			"    ||TRANSLATE(RPAD(NVL(s.NOMBRE,' '),'150',' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')  \r\n" +
			"    ||LPAD(NVL(k.CODIGO_DIVISA,' '),'3',' ')  \r\n" +
			"    ||LPAD(NVL(a.NUMERO_RADICACION,'0'),'15','0')  \r\n" +
			"    FROM CONTABLES.NC_NOTA_CONTABLE a   \r\n" +
			"    JOIN CONTABLES.NC_NOTA_CONTABLE_TEMA k ON a.CODIGO=k.CODIGO_NOTA_CONTABLE  \r\n" +
			"    JOIN CONTABLES.NC_NOTA_CONTABLE_TEMA_RIESGO r ON r.CODIGO_NOTA_CONTABLE=a.CODIGO AND r.CODIGO_TEMA=k.CODIGO  \r\n" +
			"    JOIN CONTABLES.NC_TEMA t ON t.CODIGO=k.CODIGO_TEMA   \r\n" +
			"    JOIN CONTABLES.NC_CLASE_RIESGO v ON v.CODIGO=r.CODIGO_CLASE_RIESGO  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_LINE_OPER g ON g.CODIGO=r.CODIGO_LINEA_OPER  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_PROD h ON h.CODIGO=r.CODIGO_PRODUCTO  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_SUBPROD j ON r.CODIGO_SUBPRODUCTO=j.CODIGO  \r\n" +
			"    JOIN CONTABLES.NC_CANAL m ON m.CODIGO=r.CODIGO_CANAL  \r\n" +
			"    JOIN CONTABLES.NC_RIES_OPER_PROC proce ON proce.CODIGO=r.CODIGO_PROCESO  \r\n" +
			"    JOIN CONTABLES.NC_OPER_N2_SFC q ON q.CODIGO=r.CODIGO_OPER_N2_SFC AND v.CODIGO=q.CODIGO_NC_CLASE_RIESGO  \r\n" +
			"    JOIN CONTABLES.NC_OPER_N3_SFC s ON s.CODIGO=r.CODIGO_OPER_N3_SFC AND s.CODIGO_N1_SFC=v.CODIGO AND s.CODIGO_N2_SFC=q.CODIGO \r\n" +
			"    JOIN CONTABLES.NC_INSTANCIA I ON I.CODIGO_NOTA_CONTABLE=a.CODIGO   \r\n" +
			"    JOIN CONTABLES.NC_PUC p ON (p.INDICADOR_SIRO='T' AND (p.NUMERO_CUENTA=k.CONTRAPARTIDA_CONTABLE))  \r\n" +
			"    WHERE t.RIESGO_OPERACIONAL='S' AND I.ESTADO='5'  ";


	private final static String QUERY_EXPORT_CONCILIACION =	" select   \r\n" +
			"  LPAD(NVL( CODIGO_NOTA_CONTABLE, '0'),'15','0') \r\n" +
			"  ||contables.CHARACTER_VALIDATION(NUMERO_RADICACION,20,'0',' ') \r\n" +
			"  ||contables.CHARACTER_VALIDATION(TIPO_NOTA,1,'0',' ') \r\n" +
			"  ||contables.CHARACTER_VALIDATION(DES_TIPO_NOTA ,23 ,'0',' ')    \r\n" +
			"  ||contables.CHARACTER_VALIDATION(DESCRIPCION,500, ' ', ' ')  \r\n" +
			"  ||contables.CHARACTER_VALIDATION(Cod_sucursal_origen,4 , ' ', ' ') \r\n" +
			"  ||contables.CHARACTER_VALIDATION(sucursal_origen,70, ' ', ' ')  \r\n" +
			"  ||contables.CHARACTER_VALIDATION(usuario_asignado , 7, '0',' ')  \r\n" +
			"  ||TO_CHAR(fecha_asignacion, 'YYYY-MM-DD.HH24.MI.SSxFF') --fecha_asignacion 2022-01-01-01.00.00.000000  \r\n" +
			"  ||contables.CHARACTER_VALIDATION(Estado,1,'0','0') \r\n" +
			"  ||contables.CHARACTER_VALIDATION(des_Estado ,20 , ' ', ' ') \r\n" +
			"  ||contables.CHARACTER_VALIDATION(tema ,4 ,' ',' ') \r\n" +
			"  ||contables.CHARACTER_VALIDATION(des_tema,100, ' ', ' ')   \r\n" +
			"  ||RPAD(NVL( partida, ' '),'15',' ')   \r\n" +
			"  ||RPAD(NVL( nat1, ' '),'2',' ')   \r\n" +
			"  ||RPAD(NVL( cod_suc_destino1, ' '),'4',' ')   \r\n" +
			"  ||contables.CHARACTER_VALIDATION(nombre_suc_destino1,70, ' ', ' ') \r\n" +
			"  ||RPAD(NVL( contrapartida, ' '),'15',' ')   \r\n" +
			"  ||RPAD(NVL( nat2, ' '),'2',' ')   \r\n" +
			"  ||LPAD(NVL(TRIM( cod_suc_destino2), '0'),'4','0')    \r\n" +
			"  ||contables.CHARACTER_VALIDATION(nombre_suc_destino2 ,70, ' ', ' ')   \r\n" +
			"  ||RPAD(NVL( divisa, ' '),'3',' ')   \r\n" +
			"  || REPLACE(REPLACE(LTRIM(TO_CHAR(monto, '0000000000000000D00')), ',', ''), '.', '')  \r\n" +
			"  ||RPAD(NVL( identificacion1, ' '),'17',' ')   \r\n" +
			"  ||contables.CHARACTER_VALIDATION(nombre_completo1 , 100 , ' ', ' ') \r\n" +
			"  ||RPAD(NVL( identificacion2, ' '),'17',' ')   \r\n" +
			"  ||contables.CHARACTER_VALIDATION(nombre_completo2 ,100 , ' ', ' ')   \r\n" +
			"  ||contables.CHARACTER_VALIDATION(DESCRIPCION_TEMA,500, ' ', ' ')   \r\n" +
			"  ||RPAD(NVL( Area, ' '),'4',' ')   \r\n" +
			"  ||contables.CHARACTER_VALIDATION(Nombre_Area , 70 , ' ', ' ') \r\n" +
			"  || LPAD(NVL(TO_CHAR(FECHA_CONTABLE,'YYYY-MM-DD'),' '),'10','0')    \r\n" +
			"  ||LPAD(NVL( CODIGO_IMPUESTO, '0'),'5','0')   \r\n" +
			"  || REPLACE(REPLACE(LTRIM(TO_CHAR(NVL(BASE, 0), '0000000000000000D00')), ',', ''), '.', '')   \r\n" +
			"  || REPLACE(REPLACE(LTRIM(TO_CHAR(NVL(CALCULADO, 0), '0000000000000000D00')), ',', ''), '.', '')   \r\n" +
			"  || RPAD(NVL( PARTIDA_CONTABLE, ' '),'15',' ')   \r\n" +
			"  || contables.CHARACTER_VALIDATION( NOMBRE , 100 , ' ', ' ') \r\n" +
			"  || REPLACE(REPLACE(LTRIM(TO_CHAR(NVL(VALOR , 0) , '00000000D00')), ',', ''), '.', '')   \r\n" +
			"  from(   \r\n" +
			"  --- CONTABILIDAD POR CONCEPTOS O NOTAS DE OFICINA -----   \r\n" +
			"  select k.A_CODIGO CODIGO_NOTA_CONTABLE, k.A_NUMERO_RADICACION NUMERO_RADICACION, k.A_TIPO_NOTA TIPO_NOTA ,    \r\n" +
			"  decode(k.A_TIPO_NOTA,'L','Contabilidad Libre','R','Nota Contable Oficina','C','Cruce Partida Pendiente') DES_TIPO_NOTA, k.A_DESCRIPCION DESCRIPCION ,   \r\n" +
			"         k.b_codigo_sucursal_origen Cod_sucursal_origen, i.nombre sucursal_origen,   \r\n" +
			"         h.codigo_empleado usuario_asignado, k.b_ultima_actualizacion fecha_asignacion,    \r\n" +
			"         k.B_ESTADO Estado, decode(k.B_ESTADO,4,'Precierre',5,'Cierre',6,'En Firme',9,'Anulado','Pendiente Aprobacin') des_Estado ,   \r\n" +
			"         to_char( l.codigo ) tema, nvl(l.nombre,'') des_tema, nvl(k.partida_contable, '') partida, nvl(k.naturaleza1,'') nat1,   \r\n" +
			"         nvl(k.cod_suc_dest_part,'') cod_suc_destino1, nvl(m.nombre,'') nombre_suc_destino1,   \r\n" +
			"         k.contrapartida_contable contrapartida, k.naturaleza2 nat2, k.cod_suc_dest_contpart cod_suc_destino2, n.nombre nombre_suc_destino2,   \r\n" +
			"         k.codigo_divisa divisa, k.monto, k.tipo_identificacion1||k.numero_identificacion1||k.digito_verificacion1 identificacion1,      \r\n" +
			"         k.nombre_completo1, k.tipo_identificacion2||k.numero_identificacion2||k.digito_verificacion2 identificacion2,    \r\n" +
			"         k.nombre_completo2, k.descripcion DESCRIPCION_TEMA, k.b_codigo_sucursal_origen Area, i.nombre Nombre_Area,   \r\n" +
			"         k.FECHA_CONTABLE, NULL CODIGO_IMPUESTO,  NULL base, NULL CALCULADO, NULL PARTIDA_CONTABLE, NULL NOMBRE, NULL VALOR    \r\n" +
			"         from contables.nc_nota_contable_instancia_conciliacion_view  k   \r\n" +
			"         LEFT JOIN CONTABLES.nc_sucursal m ON k.cod_suc_dest_part = m.codigo  -- ok   \r\n" +
			"         LEFT JOIN CONTABLES.nc_sucursal n ON k.cod_suc_dest_contpart = n.codigo -- OK   \r\n" +
			"         LEFT JOIN CONTABLES.NC_TEMA l ON k.codigo_tema = l.codigo -- ok   \r\n" +
			"  	   LEFT JOIN CONTABLES.nc_usuario h ON K.b_codigo_usuario_actual = h.codigo  --ok   \r\n" +
			"  	   LEFT JOIN CONTABLES.NC_SUCURSAL i ON K.b_codigo_sucursal_origen = i.codigo   \r\n" +
			"  	   where    \r\n" +
			"         TO_DATE(k.FECHA_CONTABLE,'DD/MM/YY') >= TO_DATE(?,'DD/MM/YY')    \r\n" +
			"         AND TO_DATE(k.FECHA_CONTABLE,'DD/MM/YY') < TO_DATE(?,'DD/MM/YY') -- ok   \r\n" +
			"  union all   \r\n" +
			"  --2 --- CONTABILIDAD POR CONCEPTOS -- VALORES DE IMPUESTOS ----   \r\n" +
			"  select k.a_CODIGO CODIGO_NOTA_CONTABLE, k.a_NUMERO_RADICACION, k.a_TIPO_NOTA TIPO_NOTA,   \r\n" +
			"  decode(k.A_TIPO_NOTA,'L','Contabilidad Libre','R','Nota Contable Oficina','C','Cruce Partida Pendiente') DES_TIPO_NOTA, k.a_DESCRIPCION,   \r\n" +
			"         k.b_codigo_sucursal_origen Cod_sucursal_origen, i.nombre sucursal_origen,   \r\n" +
			"         h.codigo_empleado usuario_asignado, k.b_ultima_actualizacion fecha_asignacion,    \r\n" +
			"         k.b_estado Estado, decode(k.B_ESTADO,4,'Precierre',5,'Cierre',6,'En Firme',9,'Anulado','Pendiente Aprobacin') des_Estado,   \r\n" +
			"         to_char( l.codigo ) tema, nvl(l.nombre,'') des_tema, nvl( IM.PARTIDA_CONTABLE, '') partida, nvl(k.naturaleza1,'') nat1,   \r\n" +
			"         nvl(k.cod_suc_dest_part,'') cod_suc_destino1, nvl(m.nombre,'') nombre_suc_destino1,   \r\n" +
			"         k.contrapartida_contable contrapartida, k.naturaleza2 nat2, k.cod_suc_dest_contpart cod_suc_destino2, n.nombre nombre_suc_destino2,   \r\n" +
			"         k.codigo_divisa divisa, G.CALCULADO monto, k.tipo_identificacion1||k.numero_identificacion1||k.digito_verificacion1 identificacion1,      \r\n" +
			"         k.nombre_completo1, k.tipo_identificacion2||k.numero_identificacion2||k.digito_verificacion2 identificacion2,    \r\n" +
			"         k.nombre_completo2, k.descripcion DESCRIPCION_TEMA,  k.b_codigo_sucursal_origen Area, i.nombre Nombre_Area,   \r\n" +
			"         k.FECHA_CONTABLE,case when k.monto = g.base then G.CODIGO_IMPUESTO end CODIGO_IMPUESTO,   \r\n" +
			"         case when k.monto = g.base then G.base end base,   \r\n" +
			"         case when k.monto = g.base then G.CALCULADO end CALCULADO,   \r\n" +
			"         case when k.monto = g.base then IM.PARTIDA_CONTABLE end PARTIDA_CONTABLE,   \r\n" +
			"         case when k.monto = g.base then IM.NOMBRE end NOMBRE,   \r\n" +
			"         case when k.monto = g.base then IM.VALOR end VALOR   \r\n" +
			"    from contables.nc_nota_contable_instancia_conciliacion_view  k   \r\n" +
			"         LEFT JOIN CONTABLES.nc_sucursal m ON k.cod_suc_dest_part = m.codigo   \r\n" +
			"         LEFT JOIN CONTABLES.nc_sucursal n ON k.cod_suc_dest_contpart = n.codigo   \r\n" +
			"         LEFT JOIN CONTABLES.NC_TEMA l ON k.codigo_tema = l.codigo   \r\n" +
			"  	   LEFT JOIN CONTABLES.nc_usuario h ON k.b_codigo_usuario_actual = h.codigo   \r\n" +
			"  	   LEFT JOIN CONTABLES.NC_SUCURSAL i ON k.b_codigo_sucursal_origen = i.codigo   \r\n" +
			"  	   LEFT JOIN CONTABLES.NC_NOTA_CONTABLE_TEMA_IMPUESTO G ON G.CODIGO_NOTA_CONTABLE = k.a_codigo -- ok   \r\n" +
			"         LEFT JOIN CONTABLES.NC_IMPUESTO IM ON G.CODIGO_IMPUESTO = IM.CODIGO   \r\n" +
			"   where    \r\n" +
			"  	   TO_DATE(k.FECHA_CONTABLE,'DD/MM/YY') >= TO_DATE(?,'DD/MM/YY') AND TO_DATE(k.FECHA_CONTABLE,'DD/MM/YY') < TO_DATE(?,'DD/MM/YY')   \r\n" +
			"         AND  k.monto = g.base  AND G.BASE!=0   \r\n" +
			"  union all   \r\n" +
			"  --3 --- CONTABILIDAD LIBRE ----   \r\n" +
			"  select k.a_CODIGO CODIGO_NOTA_CONTABLE, k.a_NUMERO_RADICACION, k.a_TIPO_NOTA TIPO_NOTA,   \r\n" +
			"        decode(k.A_TIPO_NOTA,'L','Contabilidad Libre','R','Nota Contable Oficina','C','Cruce Partida Pendiente') DES_TIPO_NOTA,k.a_DESCRIPCION,   \r\n" +
			"         k.b_codigo_sucursal_origen Cod_sucursal_origen, i.nombre sucursal_origen,   \r\n" +
			"         h.codigo_empleado usuario_asignado, k.b_ultima_actualizacion fecha_asignacion,    \r\n" +
			"         k.b_estado Estado, decode(k.B_ESTADO,4,'Precierre',5,'Cierre',6,'En Firme',9,'Anulado','Pendiente Aprobacin') des_Estado,   \r\n" +
			"         '' tema,'' des_tema , k.cuenta_contable partida, nvl(k.naturaleza,'') nat1,   \r\n" +
			"         k.cod_suc_destino cod_suc_destino1, nvl(n.nombre,'') nombre_suc_destino1, '' contrapartida, '' nat2,    \r\n" +
			"         '' cod_suc_destino2, '' nombre_suc_destino2, k.codigo_divisa, k.monto,    \r\n" +
			"         k.tipo_identificacion||k.numero_identificacion||k.digito_verificacion identificacion1,      \r\n" +
			"         k.nombre_completo nombre_completo1, '' identificacion2, '' nombre_completo2, '' descripcion,   \r\n" +
			"        k.b_codigo_sucursal_origen Area, f.nombre Nombre_Area, k.FECHA_CONTABLE,   \r\n" +
			"        case when k.monto = g.base then G.CODIGO_IMPUESTO end CODIGO_IMPUESTO,   \r\n" +
			"        case when k.monto = g.base then G.base end base,   \r\n" +
			"        case when k.monto = g.base then G.CALCULADO end CALCULADO,   \r\n" +
			"        case when k.monto = g.base then IM.PARTIDA_CONTABLE end PARTIDA_CONTABLE,   \r\n" +
			"        case when k.monto = g.base then IM.NOMBRE end NOMBRE,   \r\n" +
			"        case when k.monto = g.base then IM.VALOR end VALOR   \r\n" +
			"   from contables.nc_nota_contable_REGISTRO_LIBRE_instancia_conciliacion_view  k   \r\n" +
			"   	   LEFT JOIN CONTABLES.nc_sucursal n ON k.cod_suc_destino = n.codigo   \r\n" +
			" 	   LEFT JOIN CONTABLES.nc_usuario h ON k.b_codigo_usuario_actual = h.codigo   \r\n" +
			" 	   LEFT JOIN CONTABLES.NC_SUCURSAL i ON k.b_codigo_sucursal_origen = i.codigo   \r\n" +
			" 	   LEFT JOIN CONTABLES.NC_SUCURSAL f ON k.b_codigo_sucursal_origen = f.codigo   \r\n" +
			"        LEFT JOIN (SELECT * FROM CONTABLES.NC_NOTA_CONTABLE_TEMA_IMPUESTO WHERE BASE!=0) G ON G.CODIGO_NOTA_CONTABLE = k.a_codigo    \r\n" +
			"        LEFT JOIN CONTABLES.NC_IMPUESTO IM ON G.CODIGO_IMPUESTO = IM.CODIGO   \r\n" +
			"  where     \r\n" +
			"      TO_DATE(k.FECHA_CONTABLE,'DD/MM/YY') >= TO_DATE(?,'DD/MM/YY') AND TO_DATE(k.FECHA_CONTABLE,'DD/MM/YY') < TO_DATE(?,'DD/MM/YY')   \r\n" +
			" union all   \r\n" +
			" --4 --- CONTABILIDAD CRUCE POR PARTIDA PENDIENTE ----   \r\n" +
			" select k.a_CODIGO CODIGO_NOTA_CONTABLE, k.a_NUMERO_RADICACION, k.a_TIPO_NOTA TIPO_NOTA,   \r\n" +
			"       decode(k.A_TIPO_NOTA,'L','Contabilidad Libre','R','Nota Contable Oficina','C','Cruce Partida Pendiente') DES_TIPO_NOTA,k.a_DESCRIPCION,   \r\n" +
			"        k.b_codigo_sucursal_origen Cod_sucursal_origen, i.nombre sucursal_origen,   \r\n" +
			"        h.codigo_empleado usuario_asignado, k.b_ultima_actualizacion fecha_asignacion,    \r\n" +
			"        k.b_estado Estado,  decode(k.B_ESTADO,4,'Precierre',5,'Cierre',6,'En Firme',9,'Anulado','Pendiente Aprobacin') des_Estado,   \r\n" +
			"        '' tema, '' des_tema , k.cuenta partida, nvl(k.naturaleza,'') nat1,   \r\n" +
			"        k.SUCURSAL_DESTINO cod_suc_destino1, nvl(n.nombre,'') nombre_suc_destino1, '' contrapartida, '' nat2,    \r\n" +
			"        '' cod_suc_destino2, '' nombre_suc_destino2, k.DIVISA codigo_divisa, k.IMPORTE,    \r\n" +
			"        '' identificacion1,  '' nombre_completo1, '' identificacion2, '' nombre_completo2, '' descripcion,    \r\n" +
			"        k.b_codigo_sucursal_origen Area, f.nombre Nombre_Area,  k.FECHA_CONTABLE,   \r\n" +
			"         case when k.IMPORTE = g.base then G.CODIGO_IMPUESTO end CODIGO_IMPUESTO,   \r\n" +
			"         case when k.IMPORTE = g.base then G.base end base,   \r\n" +
			"         case when k.IMPORTE = g.base then G.CALCULADO end CALCULADO,   \r\n" +
			"         case when k.IMPORTE = g.base then IM.PARTIDA_CONTABLE end PARTIDA_CONTABLE,   \r\n" +
			"         case when k.IMPORTE = g.base then IM.NOMBRE end NOMBRE,   \r\n" +
			"         case when k.IMPORTE = g.base then IM.VALOR  end VALOR   \r\n" +
			"   from CONTABLES.nc_not_con_cruce_part_pend_instancia_conciliacion_view k   \r\n" +
			" 	   LEFT JOIN CONTABLES.nc_sucursal n ON k.SUCURSAL_DESTINO = n.codigo   \r\n" +
			" 	   LEFT JOIN CONTABLES.nc_usuario h ON k.b_codigo_usuario_actual = h.codigo   \r\n" +
			" 	   LEFT JOIN CONTABLES.NC_SUCURSAL i ON k.b_codigo_sucursal_origen = i.codigo   \r\n" +
			" 	   LEFT JOIN CONTABLES.NC_SUCURSAL f ON k.b_codigo_sucursal_origen = f.codigo   \r\n" +
			"        LEFT JOIN (SELECT * FROM CONTABLES.NC_NOTA_CONTABLE_TEMA_IMPUESTO WHERE BASE!=0) G ON G.CODIGO_NOTA_CONTABLE = k.a_codigo    \r\n" +
			"        LEFT JOIN CONTABLES.NC_IMPUESTO IM ON G.CODIGO_IMPUESTO = IM.CODIGO        \r\n" +
			"  where    \r\n" +
			"        k.FECHA_CONTABLE >= TO_DATE(?,'DD/MM/YY') AND k.FECHA_CONTABLE < TO_DATE(?,'DD/MM/YY')   \r\n" +
			" ) y  "  ;


	public NotaContableDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContable());
	}

	@Override
	public void internalUpdate(Connection con, NotaContable row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getFechaRegistroAltamiraTs(), row.getCodigoSucursalOrigen(), row.getCodigoConcepto(), row.getCodigoTipoEvento(),
				row.getDescripcion(), row.getEstado(), row.getCodigo() });
	}

	public NotaContable findByNumeroRadicacion(NotaContable row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_NUMERO_RADICACION_SENTENCE, row.getNumeroRadicacion());
	}

	public Collection<NotaContable> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<NotaContable> findByEstado(NotaContable row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<NotaContable> findByPrecierreCierre(boolean esPrecierre) throws Exception {
		return findByGeneral(SQL_SELECT_BY_PRECIERRE, new Object[] { esPrecierre ? "4" : "5" });
	}

	public Collection<NotaContable> findByEstadoInstancia(String estadoInstancia) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_INSTANCIA_SENTENCE, estadoInstancia);
	}

	private int getMaxCodePorSucursalOrigenFecha(Connection con, String numRad) throws Exception {
		return getMaxCode(con, cs_SELECT_MAX_CODE_BY_SUCURSAL_ORIGEN_SENTENCE.replaceAll(":param", numRad));
	}

	@Override
	protected Object[] getDataToAdd(Connection con, NotaContable row) throws Exception {
		row.setCodigo(getMaxCode(con));
		
		String sucursal = row.getCodigoSucursalOrigen();
		String fecha = StringUtils.getString(DateUtils.getTimestamp(), "ddMMyyyy");
		int consecutivo = getMaxCodePorSucursalOrigenFecha(con, sucursal + fecha);
		
		String numRadicacion = sucursal + fecha + StringUtils.getStringLeftPadding("" + consecutivo, 3, '0');

		return new Object[] { row.getCodigo(), DateUtils.getTimestamp(), null, row.getCodigoSucursalOrigen(), row.getCodigoConcepto(), row.getCodigoTipoEvento(), numRadicacion,
				row.getTipoNota(), row.getDescripcion(), row.getAsientoContable(), row.getEstado() };
	}

	@Override
	public NotaContable getResultSetToVO(ResultSet result) throws Exception {
		NotaContable row = new NotaContable();

		row.setCodigo(result.getInt(1));
		row.setFechaRegistroModuloTs(result.getTimestamp(2));
		row.setFechaRegistroAltamiraTs(result.getTimestamp(3));
		row.setCodigoSucursalOrigen(result.getString(4));
		row.setCodigoConcepto(result.getInt(5));
		row.setCodigoTipoEvento(result.getInt(6));
		row.setNumeroRadicacion(result.getString(7));
		row.setTipoNota(result.getString(8));
		row.setDescripcion(result.getString(9));
		row.setAsientoContable(result.getString(10));
		row.setEstado(result.getString(11));

		return row;
	}

	@Override
	public NotaContable findByPrimaryKey(NotaContable row) throws Exception {
		return obtenerObjeto(SQL_SELECT_BY_PK_SENTENCE, row.getCodigo());
	}

	public Collection<NotaContable> findToBackup(int cantMeses) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -cantMeses);
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		return findByGeneral(sql_SELECT_TO_BACKUP, ts);
	}

	public Collection<NotaContable> findByAnulados(int cantDias) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -cantDias);
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		return findByGeneral(sql_SELECT_ANULADOS, ts);
	}

	public Collection<NotaContable> findToAnular(int cantDias) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -cantDias);
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		return findByGeneral(sql_SELECT_A_ANULAR, ts);
	}

	/**
	 * Permite retornar la consulta de la información necesaria para un archivo plano
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<String> getDataForFile() throws Exception {
		return findToStringByGeneral(QUERY_EXPORT, new Object[] {});
	}

	public Collection<String> getDataConciliationFile(String fechaDesde , String fechaHasta) throws Exception {
		return findToStringByGeneral(QUERY_EXPORT_CONCILIACION, new Object[] { fechaDesde , fechaHasta , fechaDesde , fechaHasta , fechaDesde , fechaHasta , fechaDesde , fechaHasta});
	}
}