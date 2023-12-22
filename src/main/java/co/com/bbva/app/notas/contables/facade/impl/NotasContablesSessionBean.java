package co.com.bbva.app.notas.contables.facade.impl;

import co.com.bbva.app.notas.contables.carga.dto.*;
import co.com.bbva.app.notas.contables.dao.MontoAutorizadoGeneralDAO;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.jsf.beans.UsuarioLogueado;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.EMailSender;
import co.com.bbva.app.notas.contables.util.IRol;
import oracle.sql.TIMESTAMP;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

public class NotasContablesSessionBean extends NotasContablesConsultaSessionBean {

	@Override
	public void addMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception {
		montoMaximoDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception {
		montoMaximoDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception {
		montoMaximoDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoMontoMaximo(MontoMaximo row, int codigoUsuario) throws Exception {
		montoMaximoDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: Rol Nombre DAO: rolDAO Plural: Roles
	 */

	@Override
	public void addRol(Rol row, int codigoUsuario) throws Exception {
		rolDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateRol(Rol row, int codigoUsuario) throws Exception {
		rolDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteRol(Rol row, int codigoUsuario) throws Exception {
		rolDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoRol(Rol row, int codigoUsuario) throws Exception {
		rolDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: UsuarioModulo Nombre DAO: usuarioModuloDAO Plural: UsuariosModulo
	 */

	@Override
	public void addUsuarioModulo(UsuarioModulo row, int codigoUsuario) throws Exception {
		usuarioModuloDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateUsuarioModulo(UsuarioModulo row, int codigoUsuario, boolean reasignarPendientes) throws Exception {
		Connection con = null;
		try {
			con = usuarioModuloDAO.getConexion(false);
			reasignarMisPendientes(con, row.getCodigo().intValue(), codigoUsuario);
			usuarioModuloDAO.update(row, codigoUsuario);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			usuarioModuloDAO.closeConnection(con);
		}
	}

	@Override
	public void deleteUsuarioModulo(UsuarioModulo row, int codigoUsuario) throws Exception {
		usuarioModuloDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoUsuarioModulo(UsuarioModulo row, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = usuarioModuloDAO.getConexion(false);

			boolean activo = row.getEstado().equals("A");
			// si el usuario era de tipo padrino o ente autorizador
			if (row.getCodigoRol().intValue() == IRol.PADRINOS || row.getCodigoRol().intValue() == IRol.AUTORIZADOR) {
				Padrino padrino = new Padrino();
				padrino.setCodigoUsuario(row.getCodigo().intValue());
				padrino = padrinoDAO.findByCodigoUsuario(con, padrino);

				// si no esta asociado a un padrino se evalua si es ente autorizador
				if (padrino.getCodigo().intValue() > 0) {
					padrino.setEstado(row.getEstado());
					padrinoDAO.changeEstado(con, padrino, codigoUsuario);
				} else {
					EnteAutorizador enteAutorizador = new EnteAutorizador();
					enteAutorizador.setCodigoUsuarioModulo(row.getCodigo().intValue());
					enteAutorizador = enteAutorizadorDAO.findByCodigoUsuario(con, enteAutorizador);

					// es ente autorizador
					if (enteAutorizador.getCodigo().intValue() != 0) {
						enteAutorizador.setEstado(row.getEstado());
						enteAutorizadorDAO.changeEstado(con, enteAutorizador, codigoUsuario);
					}
				}
			}
			if (activo) {
				reasignarMisPendientes(con, row.getCodigo().intValue(), codigoUsuario);
			}
			usuarioModuloDAO.changeUserStatus(con, row);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			usuarioModuloDAO.closeConnection(con);
		}
	}

	/*
	 * Nombre DTO: TemaAutorizacion Nombre DAO: temaAutorizacionDAO Plural: TemasAutorizacion
	 */

	@Override
	public void addTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception {
		temaAutorizacionDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception {
		temaAutorizacionDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception {
		temaAutorizacionDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoTemaAutorizacion(TemaAutorizacion row, int codigoUsuario) throws Exception {
		temaAutorizacionDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: CausalDevolucion Nombre DAO: causalDevolucionDAO Plural: CausalesDevolucion
	 */

	@Override
	public void addCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception {
		causalDevolucionDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception {
		causalDevolucionDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception {
		causalDevolucionDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoCausalDevolucion(CausalDevolucion row, int codigoUsuario) throws Exception {
		causalDevolucionDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: TipoEvento Nombre DAO: tipoEventoDAO Plural: TiposEvento
	 */

	@Override
	public void addTipoEvento(TipoEvento row, int codigoUsuario) throws Exception {
		tipoEventoDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateTipoEvento(TipoEvento row, int codigoUsuario) throws Exception {
		tipoEventoDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteTipoEvento(TipoEvento row, int codigoUsuario) throws Exception {
		tipoEventoDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoTipoEvento(TipoEvento row, int codigoUsuario) throws Exception {
		tipoEventoDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: EnteAutorizador Nombre DAO: enteAutorizadorDAO Plural: EntesAutorizadores
	 */

	@Override
	public void addEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception {
		enteAutorizadorDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception {
		enteAutorizadorDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception {
		enteAutorizadorDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoEnteAutorizador(EnteAutorizador row, int codigoUsuario) throws Exception {
		UsuarioModulo usuario = new UsuarioModulo();
		usuario.setCodigo(row.getCodigoUsuarioModulo());
		usuario = getUsuarioModulo(usuario);
		usuario.setEstado(row.getEstado());
		changeEstadoUsuarioModulo(usuario, codigoUsuario);
	}

	/*
	 * Nombre DTO: MontoAutorizado Nombre DAO: montoAutorizadoDAO Plural: MontosAutorizados
	 */

	@Override
	public void addMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception {
		montoAutorizadoDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception {
		montoAutorizadoDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception {
		montoAutorizadoDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoMontoAutorizado(MontoAutorizado row, int codigoUsuario) throws Exception {
		montoAutorizadoDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: MontoAutorizadoGeneral Nombre DAO: montoAutorizadoGeneralDAO Plural: MontosAutorizadosGenerales
	 */

	@Override
	public void addMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception {
		montoAutorizadoGeneralDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception {
		montoAutorizadoGeneralDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception {
		montoAutorizadoGeneralDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoMontoAutorizadoGeneral(MontoAutorizadoGeneral row, int codigoUsuario) throws Exception {
		montoAutorizadoGeneralDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: FechaHabilitada Nombre DAO: fechaHabilitadaDAO Plural: FechasHabilitadas
	 */

	@Override
	public void addFechaHabilitada(FechaHabilitada row, int codigoUsuario) throws Exception {
		fechaHabilitadaDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateFechaHabilitada(FechaHabilitada row, int codigoUsuario) throws Exception {
		fechaHabilitadaDAO.update(row, codigoUsuario);
	}

	@Override
	public void updateTodasFechasHabilitadas(int dias, String opcion, String[] sucursales, int codigoUsuario) throws Exception {
		String strSucursales = "";
		int count = 0;

		if (opcion.equals("T")) {
			fechaHabilitadaDAO.updateAll(dias, codigoUsuario);
		} else {
			int limit = 500;
			int regAct = 0;
			for (count = 0; count < sucursales.length; count++) {
				if (!strSucursales.equals("")) {
					strSucursales += ", ";
				}
				strSucursales += "'" + sucursales[count].trim() + "'";
				regAct++;
				if (regAct >= limit) {
					regAct = 0;
					fechaHabilitadaDAO.updateAllSelected(strSucursales, dias, codigoUsuario);
					strSucursales = "";
				}
			}
			if (!strSucursales.isEmpty()) {
				fechaHabilitadaDAO.updateAllSelected(strSucursales, dias, codigoUsuario);
			}
		}
	}

	@Override
	public void deleteFechaHabilitada(FechaHabilitada row, int codigoUsuario) throws Exception {
		fechaHabilitadaDAO.delete(row, codigoUsuario);
	}

	@Override
	public void loadFechasHabilitadas() throws Exception {
		CargaAltamiraSessionBean cargaAltamiraManager = new CargaAltamiraSessionBean();
		Collection<Sucursal> sucursales;
		FechaHabilitada fechaHabilitada = new FechaHabilitada();

		sucursales = cargaAltamiraManager.getSucursales();
		for (Sucursal sucursal : sucursales) {

			fechaHabilitada = new FechaHabilitada();
			fechaHabilitada.setCodigoSucursal(sucursal.getCodigo());
			fechaHabilitada.setDias(2);

			addFechaHabilitada(fechaHabilitada, 0);
		}

	}

	/*
	 * Nombre DTO: Impuesto Nombre DAO: impuestoDAO Plural: Impuestos
	 */

	@Override
	public void addImpuesto(Impuesto row, int codigoUsuario) throws Exception {
		impuestoDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateImpuesto(Impuesto row, int codigoUsuario) throws Exception {
		impuestoDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteImpuesto(Impuesto row, int codigoUsuario) throws Exception {
		impuestoDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoImpuesto(Impuesto row, int codigoUsuario) throws Exception {
		impuestoDAO.changeEstado(row, codigoUsuario);
	}

	// CuentaCOD
	/*
	 * Nombre DTO: CuentaCOD Nombre DAO: cuentaCODDAO Plural: CuentasCOD
	 */
	@Override
	public void addCuentaCOD(CuentaCOD row, int codigoUsuario) throws Exception {
		cuentaCODDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateCuentaCOD(CuentaCOD row, int codigoUsuario) throws Exception {
		cuentaCODDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteCuentaCOD(CuentaCOD row, int codigoUsuario) throws Exception {
		cuentaCODDAO.delete(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: Padrino Nombre DAO: padrinoDAO Plural: Padrinos
	 */

	@Override
	public void addPadrino(Padrino row, int codigoUsuario) throws Exception {
		padrinoDAO.add(row, codigoUsuario);
	}

	@Override
	public void updatePadrino(Padrino row, int codigoUsuario) throws Exception {
		padrinoDAO.update(row, codigoUsuario);
	}

	@Override
	public void deletePadrino(Padrino row, int codigoUsuario) throws Exception {
		padrinoDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoPadrino(Padrino row, int codigoUsuario) throws Exception {
		UsuarioModulo usuario = new UsuarioModulo();
		usuario.setCodigo(row.getCodigoUsuario());
		usuario = getUsuarioModulo(usuario);
		usuario.setEstado(row.getEstado());
		changeEstadoUsuarioModulo(usuario, codigoUsuario);
	}

	/*
	 * Nombre DTO: UnidadAnalisis Nombre DAO: unidadAnalisisDAO Plural: UnidadesAnalisis
	 */

	@Override
	public void addUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception {
		unidadAnalisisDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception {
		unidadAnalisisDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception {
		unidadAnalisisDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoUnidadAnalisis(UnidadAnalisis row, int codigoUsuario) throws Exception {
		unidadAnalisisDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: CentroEspecial Nombre DAO: centroEspecialDAO Plural: UnidadesAnalisis
	 */

	@Override
	public void addCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception {
		centroEspecialDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception {
		centroEspecialDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception {
		centroEspecialDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoCentroEspecial(CentroEspecial row, int codigoUsuario) throws Exception {
		centroEspecialDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: Concepto Nombre DAO: conceptoDAO Plural: Conceptos
	 */

	@Override
	public void addConcepto(Concepto row, int codigoUsuario) throws Exception {
		conceptoDAO.add(row, codigoUsuario);
	}

	@Override
	public void updateConcepto(Concepto row, int codigoUsuario) throws Exception {
		conceptoDAO.update(row, codigoUsuario);
	}

	@Override
	public void deleteConcepto(Concepto row, int codigoUsuario) throws Exception {
		conceptoDAO.delete(row, codigoUsuario);
	}

	@Override
	public void changeEstadoConcepto(Concepto row, int codigoUsuario) throws Exception {
		conceptoDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: TemaImpuesto Nombre DAO: temaImpuestoDAO Plural: TemasImpuestos
	 */

	private void addTemaImpuesto(Connection con, TemaImpuesto row, int codigoUsuario) throws Exception {
		temaImpuestoDAO.add(con, row, codigoUsuario);
	}

	private void addImpuestosTema(Connection con, Collection<TemaImpuesto> impuestos, int codigoTema, int codigoUsuario) throws Exception {
		for (TemaImpuesto temaImpuesto : impuestos) {
			temaImpuesto.setCodigoTema(codigoTema);
			addTemaImpuesto(con, temaImpuesto, codigoUsuario);
		}
	}

	private void deleteImpuestosPorTema(Connection con, int codigoTema, int codigoUsuario) throws Exception {
		temaImpuestoDAO.deleteByTema(con, codigoTema, codigoUsuario);
	}

	/*
	 * Nombre DTO: TemaProducto Nombre DAO: temaProductoDAO Plural: TemasProductos
	 */

	private void addTemaProducto(Connection con, TemaProducto row, int codigoUsuario) throws Exception {
		temaProductoDAO.add(con, row, codigoUsuario);
	}

	private void addProductosTema(Connection con, Collection<TemaProducto> productos, int codigoTema, int codigoUsuario) throws Exception {
		for (TemaProducto temaProducto : productos) {
			temaProducto.setCodigoTema(codigoTema);
			addTemaProducto(con, temaProducto, codigoUsuario);
		}
	}

	private void deleteProductosPorTema(Connection con, int codigoTema, int codigoUsuario) throws Exception {
		temaProductoDAO.deleteByTema(con, codigoTema, codigoUsuario);
	}

	/*
	 * Nombre DTO: Tema Nombre DAO: temaDAO Plural: Temas
	 */

	@Override
	public void addTema(Tema row, Collection<TemaImpuesto> impuestos, Collection<TemaProducto> productos, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = temaDAO.getConexion(false);
			// String xmlDataOriginal = "";
			// String xmlDataModificada = "";
			// int idAuditoria = 0;
			int idRow = 0;

			idRow = temaDAO.add(con, row, codigoUsuario);
			addImpuestosTema(con, impuestos, idRow, codigoUsuario);
			addProductosTema(con, productos, idRow, codigoUsuario);

			row.setCodigo(idRow);
			// xmlDataOriginal = temaDAO.getXMLDataByPrimaryKey(con, row);
			// xmlDataOriginal += temaImpuestoDAO.getXMLDataByTema(con, row.getCodigo().intValue());
			// xmlDataOriginal += temaProductoDAO.getXMLDataByTema(con, row.getCodigo().intValue());
			//
			// idAuditoria = temaDAO.addRegistroAuditoria(con, codigoUsuario, TemaDAO.ACCION_ADICIONAR, Tema.class.getSimpleName(), "" + idRow);
			//
			// temaDAO.addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, xmlDataModificada);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			temaDAO.closeConnection(con);
		}
	}

	@Override
	public void updateTema(Tema row, Collection<TemaImpuesto> impuestos, Collection<TemaProducto> productos, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = temaDAO.getConexion(false);
			// String xmlDataOriginal = "";
			// String xmlDataModificada = "";
			// int idAuditoria = 0;

			// xmlDataOriginal = temaDAO.getXMLDataByPrimaryKey(row);
			// xmlDataOriginal += temaImpuestoDAO.getXMLDataByTema(row.getCodigo().intValue());
			// xmlDataOriginal += temaProductoDAO.getXMLDataByTema(row.getCodigo().intValue());

			temaDAO.update(con, row, codigoUsuario);

			// xmlDataModificada = temaDAO.getXMLDataByPrimaryKey(row);
			// xmlDataModificada += temaImpuestoDAO.getXMLDataByTema(row.getCodigo().intValue());
			// xmlDataModificada += temaProductoDAO.getXMLDataByTema(row.getCodigo().intValue());

			deleteImpuestosPorTema(con, row.getCodigo().intValue(), codigoUsuario);
			deleteProductosPorTema(con, row.getCodigo().intValue(), codigoUsuario);
			addImpuestosTema(con, impuestos, row.getCodigo().intValue(), codigoUsuario);
			addProductosTema(con, productos, row.getCodigo().intValue(), codigoUsuario);

			// idAuditoria = temaDAO.addRegistroAuditoria(codigoUsuario, TemaDAO.ACCION_EDITAR, Tema.class.getSimpleName(), row.getCodigo().intValue());
			//
			// temaDAO.addRegistroAuditoriaDetalle(idAuditoria, xmlDataOriginal, xmlDataModificada);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			temaDAO.closeConnection(con);
		}
	}

	@Override
	public void deleteTema(Tema row, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = temaDAO.getConexion(false);

			// String xmlDataOriginal = "";
			// String xmlDataModificada = "";
			// int idAuditoria = 0;

			// xmlDataOriginal = temaDAO.getXMLDataByPrimaryKey(row);
			// xmlDataOriginal += temaImpuestoDAO.getXMLDataByTema(row.getCodigo().intValue());
			// xmlDataOriginal += temaProductoDAO.getXMLDataByTema(row.getCodigo().intValue());

			deleteImpuestosPorTema(con, row.getCodigo().intValue(), codigoUsuario);
			deleteProductosPorTema(con, row.getCodigo().intValue(), codigoUsuario);
			temaDAO.delete(con, row, codigoUsuario);

			// idAuditoria = temaDAO.addRegistroAuditoria(codigoUsuario, TemaDAO.ACCION_ELIMINAR, Tema.class.getSimpleName(), row.getCodigo().intValue());
			//
			// temaDAO.addRegistroAuditoriaDetalle(idAuditoria, xmlDataOriginal, xmlDataModificada);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			temaDAO.closeConnection(con);
		}
	}

	@Override
	public void changeEstadoTema(Tema row, int codigoUsuario) throws Exception {
		temaDAO.changeEstado(row, codigoUsuario);
	}

	/*
	 * Nombre DTO: NotaContableTemaRiesgo Nombre DAO: notaContableTemaRiesgoDAO Plural: NotaContableTemasRiesgo
	 */

	private void addNotaContableTemaRiesgo(Connection con, RiesgoOperacional row, int codigoUsuario) throws Exception {
		notaContableTemaRiesgoDAO.add(con, row, codigoUsuario);
	}

	private void addRiesgoOperacionalTemaNotaContable(Connection con, RiesgoOperacional row, int codigoNotaContable, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		RiesgoOperacional notaContableTemaRiesgo = new RiesgoOperacional();
	
		notaContableTemaRiesgo = row;
		
		notaContableTemaRiesgo.setCodigoNotaContable(codigoNotaContable);
		notaContableTemaRiesgo.setCodigoTemaNotaContable(codigoTemaNotaContable);
		
		addNotaContableTemaRiesgo(con, notaContableTemaRiesgo, codigoUsuario);
	}

	private void deleteRiesgoPorTemaNotaContable(Connection con, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		notaContableTemaRiesgoDAO.deleteByTemaNotaContable(con, codigoTemaNotaContable, codigoUsuario);
	}

	/*
	 * Nombre DTO: NotaContableTemaImpuesto Nombre DAO: notaContableTemaImpuestoDAO Plural: NotaContableTemasImpuesto
	 */

	private void addNotaContableTemaImpuesto(Connection con, NotaContableTemaImpuesto row, int codigoUsuario) throws Exception {
		notaContableTemaImpuestoDAO.add(con, row, codigoUsuario);
	}

	private void addImpuestosTemaNotaContable(Connection con, Collection<NotaContableTemaImpuesto> rows, int codigoNotaContable, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		for (NotaContableTemaImpuesto notaContableTemaImpuesto : rows) {
			notaContableTemaImpuesto.setCodigoNotaContable(codigoNotaContable);
			notaContableTemaImpuesto.setCodigoTemaNotaContable(codigoTemaNotaContable);
			addNotaContableTemaImpuesto(con, notaContableTemaImpuesto, codigoUsuario);
		}
	}

	private void deleteImpuestosPorTemaNotaContable(Connection con, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		notaContableTemaImpuestoDAO.deleteByTemaNotaContable(con, codigoTemaNotaContable, codigoUsuario);
	}

	/*
	 * Nombre DTO: NotaContableCrucePartidaPendiente Nombre DAO: notaContableCrucePartidaPendienteDAO Plural: NotaContableCrucePartidasPendientes
	 */

	private int addNotaContableCrucePartidaPendiente(Connection con, NotaContableCrucePartidaPendiente row, int codigoUsuario) throws Exception {
		return notaContableCrucePartidaPendienteDAO.add(con, row, codigoUsuario);
	}

	private void addCrucesPartidasPendientesNotaContable(Connection con, Collection<PartidaPendiente> rows, int codigoNotaContable, int codigoUsuario) throws Exception, NamingException, CreateException, RemoteException {
		NotaContableCrucePartidaPendiente notaContableCrucePartidaPendiente = new NotaContableCrucePartidaPendiente();

		for (PartidaPendiente partidaPendiente : rows) {
			notaContableCrucePartidaPendiente = new NotaContableCrucePartidaPendiente();
			notaContableCrucePartidaPendiente.setCodigoNotaContable(codigoNotaContable);
			notaContableCrucePartidaPendiente.setCodigoSucursalDestino(partidaPendiente.getSucursalDestino());
			notaContableCrucePartidaPendiente.setConcepto(partidaPendiente.getConcepto());
			notaContableCrucePartidaPendiente.setCuentaContable(partidaPendiente.getCuenta());
			notaContableCrucePartidaPendiente.setDescripcionPartidaPendiente(partidaPendiente.getDescripcion());
			notaContableCrucePartidaPendiente.setDivisa(partidaPendiente.getDivisa());
			notaContableCrucePartidaPendiente.setFechaContable(partidaPendiente.getFechaContable());
			notaContableCrucePartidaPendiente.setImporte(partidaPendiente.getImporte());
			notaContableCrucePartidaPendiente.setNaturaleza(partidaPendiente.getNaturaleza());
			notaContableCrucePartidaPendiente.setReferenciaCruce(partidaPendiente.getReferenciaCruce());

			partidaPendiente.setUsada("S");
			partidaPendienteDAO.updateUsada(con, partidaPendiente, codigoUsuario);
			addNotaContableCrucePartidaPendiente(con, notaContableCrucePartidaPendiente, codigoUsuario);
		}
	}

	/*
	 * Nombre DTO: NotaContableTotal Nombre DAO: notaContableTotalDAO Plural: NotaContableTotales
	 */

	private void addNotaContableTotal(Connection con, NotaContableTotal row, int codigoUsuario) throws Exception {
		notaContableTotalDAO.add(con, row, codigoUsuario);
	}

	private void addTotalesNotaContable(Connection con, Collection<NotaContableTotal> rows, int codigoNotaContable, int codigoUsuario) throws Exception {
		for (NotaContableTotal notaContableTotal : rows) {
			notaContableTotal.setCodigoNotaContable(codigoNotaContable);
			addNotaContableTotal(con, notaContableTotal, codigoUsuario);
		}
	}

	private void deleteTotalesPorNotaContable(Connection con, int codigoNotaContable, int codigoUsuario) throws Exception {
		notaContableTotalDAO.deleteByNotaContable(con, codigoNotaContable, codigoUsuario);
	}

	/*
	 * Nombre DTO: NotaContableTema Nombre DAO: notaContableTemaDAO Plural: NotaContableTemas
	 */

	private int addNotaContableTema(Connection con, NotaContableTema row, int codigoUsuario) throws Exception {
		return notaContableTemaDAO.add(con, row, codigoUsuario);
	}

	private void addTemaNotaContable(Connection con, NotaContableTema row, int codigoNotaContable, int codigoUsuario) throws Exception {
		int codigoTemaNotaContable = 0;

		if (row.getFechaContable() != null && row.getMonto().doubleValue() != 0) {
			codigoTemaNotaContable = addNotaContableTema(con, row, codigoUsuario);
			if (row.getRiesgoOperacional().getFechaEvento() != null) {
				addRiesgoOperacionalTemaNotaContable(con, row.getRiesgoOperacional(), codigoNotaContable, codigoTemaNotaContable, codigoUsuario);
			}
			addImpuestosTemaNotaContable(con, row.getImpuestoTema(), codigoNotaContable, codigoTemaNotaContable, codigoUsuario);
			addAnexosTemaNotaContable(con, row.getAnexoTema(), codigoNotaContable, codigoTemaNotaContable, codigoUsuario);
		}
	}

	private void addTemasNotaContable(Connection con, Collection<NotaContableTema> rows, int codigoNotaContable, int codigoUsuario) throws Exception {
		for (NotaContableTema notaContableTema : rows) {
			notaContableTema.setCodigoNotaContable(codigoNotaContable);
			addTemaNotaContable(con, notaContableTema, codigoNotaContable, codigoUsuario);
		}
	}

	private void deleteTemasPorNotaContable(Connection con, int codigoNotaContable, int codigoUsuario) throws Exception {
		Collection<NotaContableTema> temasNotaContable;

		temasNotaContable = getTemasPorNotaContable(codigoNotaContable);
		for (NotaContableTema notaContableTema : temasNotaContable) {
			deleteRiesgoPorTemaNotaContable(con, notaContableTema.getCodigo().intValue(), codigoUsuario);
			deleteImpuestosPorTemaNotaContable(con, notaContableTema.getCodigo().intValue(), codigoUsuario);
		}

		notaContableTemaDAO.deleteByNotaContable(con, codigoNotaContable, codigoUsuario);
	}

	private void deleteTemasLibresPorNotaContable(Connection con, int codigoNotaContable, int codigoUsuario) throws Exception {

		Collection<NotaContableRegistroLibre> temasNotaContable = getRegistrosLibresPorNotaContable(codigoNotaContable);
		for (NotaContableRegistroLibre registro : temasNotaContable) {
			deleteRiesgoPorTemaNotaContable(con, registro.getCodigo(), codigoUsuario);
		}

		notaContableRegistroLibreDAO.deleteByNotaContable(con, codigoNotaContable, codigoUsuario);
	}

	/*
	 * Nombre DTO: NotaContableRegistroLibre Nombre DAO: notaContableRegistroLibreDAO Plural: NotaContableRegistrosLibres
	 */

	private int addNotaContableRegistroLibre(Connection con, NotaContableRegistroLibre row, int codigoUsuario) throws Exception {
		return notaContableRegistroLibreDAO.add(con, row, codigoUsuario);
	}

	private void addRegistroLibreNotaContable(Connection con, NotaContableRegistroLibre row, int codigoNotaContable, int codigoUsuario) throws Exception {
		int codigoRegistroLibreNotaContable = 0;

		if (row.getFechaContable() != null && row.getMonto() != 0) {
			codigoRegistroLibreNotaContable = addNotaContableRegistroLibre(con, row, codigoUsuario);
			if (row.getPucCuenta().getIndicadorSIRO() != null && row.getPucCuenta().getIndicadorSIRO().equals("T")) {
				addRiesgoOperacionalTemaNotaContable(con, row.getRiesgoOperacional(), codigoNotaContable, codigoRegistroLibreNotaContable, codigoUsuario);
			}
		}
	}

	private void addRegistrosLibresNotaContable(Connection con, Collection<NotaContableRegistroLibre> rows, int codigoNotaContable, int codigoUsuario) throws Exception {
		for (NotaContableRegistroLibre notaContableRegistroLibre : rows) {
			notaContableRegistroLibre.setCodigoNotaContable(codigoNotaContable);
			addRegistroLibreNotaContable(con, notaContableRegistroLibre, codigoNotaContable, codigoUsuario);
		}
	}

	

	/*
	 * Nombre DTO: Anexo Nombre DAO: anexoDAO Plural: Anexos
	 */

	private void addAnexosTemaNotaContable(Connection con, Collection<Anexo> rows, int codigoNotaContable, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		for (Anexo anexo : rows) {
			anexo.setCodigoInstancia(codigoNotaContable);
			anexo.setCodigoTema(codigoTemaNotaContable);
			addAnexo(con, anexo, codigoUsuario);
		}
	}

	// private void deleteAnexosPorTemaNotaContable(int codigoTemaNotaContable) throws Exception {
	// anexoDAO.deleteByCodigoTemaNotaContable(codigoTemaNotaContable);
	// }

	private void deleteAnexosPorNotaContable(Connection con, int codigoNotaContable, int codigoUsuario) throws Exception {
		anexoDAO.deleteByCodigoInstancia(con, codigoNotaContable, codigoUsuario);
	}

	@Override
	public void addAnexo(Connection con, Anexo row, int codigoUsuario) throws Exception {
		anexoDAO.add(con, row, codigoUsuario);
	}

	/*
	 * Nombre DTO: NotaContable Nombre DAO: notaContableDAO Plural: NotaContables
	 */

	/*
	 * REGISTRO DE NOTAS CONTABLES
	 */
	private int addNotaContableRegistro(Connection con, NotaContable row, Collection<NotaContableTema> temasNotaContable, Collection<NotaContableTotal> totalesNotaContable, int codigoUsuario) throws Exception {
		Instancia instancia = new Instancia();
		Collection<UsuarioModulo> usuariosModulo;
		Iterator<UsuarioModulo> itUsuariosModulo;
		UsuarioModulo usuarioModulo = new UsuarioModulo();
		int cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL = 6;
		int codigoUsuarioAsignadoInstancia = 0;
		String codigoSucursalOrigenAsignadaInstancia = "";
		int idRow = 0;
		int codigoNotaContable = 0;

		usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(row.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");

		if (usuariosModulo.size() != 0) {
			itUsuariosModulo = usuariosModulo.iterator();
			if (itUsuariosModulo.hasNext()) {
				usuarioModulo = itUsuariosModulo.next();

				codigoUsuarioAsignadoInstancia = usuarioModulo.getCodigo().intValue();
			}
		}

		if (codigoUsuarioAsignadoInstancia != 0) {
			codigoNotaContable = notaContableDAO.add(con, row, codigoUsuario);

			codigoSucursalOrigenAsignadaInstancia = row.getCodigoSucursalOrigen();
			instancia.setCodigoNotaContable(codigoNotaContable);
			instancia.setCodigoUsuarioActual(codigoUsuarioAsignadoInstancia);
			instancia.setFechaHoraInicioTs(DateUtils.getTimestamp());
			instancia.setFechaHoraInicio(new TIMESTAMP(DateUtils.getTimestamp()));
			instancia.setEstado("0");
			instancia.setCodigoSucursalOrigen(codigoSucursalOrigenAsignadaInstancia);
			instancia.setUltimaActualizacionTs(DateUtils.getTimestamp());

			idRow = addInstancia(con, instancia, codigoUsuario);
			
			addTemasNotaContable(con, temasNotaContable, codigoNotaContable, codigoUsuario);
			addTotalesNotaContable(con, totalesNotaContable, codigoNotaContable, codigoUsuario);
		}

		return idRow;
	}

	@Override
	public int updateNotaContableRegistro(NotaContable row, Collection<NotaContableTema> temasNotaContable, Collection<NotaContableTotal> totalesNotaContable, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);

			Collection<UsuarioModulo> usuariosModulo;
			Iterator<UsuarioModulo> itUsuariosModulo;
			UsuarioModulo usuarioModulo = new UsuarioModulo();
			int cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL = 6;
			int codigoUsuarioAsignadoInstancia = 0;

			usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(row.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");

			if (usuariosModulo.size() != 0) {
				itUsuariosModulo = usuariosModulo.iterator();
				if (itUsuariosModulo.hasNext()) {
					usuarioModulo = itUsuariosModulo.next();

					codigoUsuarioAsignadoInstancia = usuarioModulo.getCodigo().intValue();
				}
			}

			if (codigoUsuarioAsignadoInstancia != 0) {
				notaContableDAO.update(con, row, codigoUsuario);
				
				deleteTemasPorNotaContable(con, row.getCodigo().intValue(), codigoUsuario);
				deleteTotalesPorNotaContable(con, row.getCodigo().intValue(), codigoUsuario);
				deleteAnexosPorNotaContable(con, row.getCodigo().intValue(), codigoUsuario);
				addTemasNotaContable(con, temasNotaContable, row.getCodigo().intValue(), codigoUsuario);
				addTotalesNotaContable(con, totalesNotaContable, row.getCodigo().intValue(), codigoUsuario);
			}
			con.commit();
			return codigoUsuarioAsignadoInstancia;
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}
	}

	@Override
	public int updateNotaContableLibre(NotaContable row, List<Anexo> anexos, List<NotaContableRegistroLibre> temasNotaContable, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);

			Collection<UsuarioModulo> usuariosModulo;
			Iterator<UsuarioModulo> itUsuariosModulo;
			UsuarioModulo usuarioModulo = new UsuarioModulo();
			int cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL = 6;
			int codigoUsuarioAsignadoInstancia = 0;

			usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(row.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");

			if (usuariosModulo.size() != 0) {
				itUsuariosModulo = usuariosModulo.iterator();
				if (itUsuariosModulo.hasNext()) {
					usuarioModulo = itUsuariosModulo.next();

					codigoUsuarioAsignadoInstancia = usuarioModulo.getCodigo().intValue();
				}
			}

			if (codigoUsuarioAsignadoInstancia != 0) {
				notaContableDAO.update(con, row, codigoUsuario);
				deleteTemasLibresPorNotaContable(con, row.getCodigo().intValue(), codigoUsuario);
				deleteAnexosPorNotaContable(con, row.getCodigo().intValue(), codigoUsuario);
				addRegistrosLibresNotaContable(con, temasNotaContable, row.getCodigo().intValue(), codigoUsuario);
				addAnexosTemaNotaContable(con, anexos, row.getCodigo().intValue(), 0, codigoUsuario);
				// notaContableDAO.addRegistroAuditoria(codigoUsuario, NotaContableDAO.ACCION_EDITAR, NotaContable.class.getSimpleName(), row.getCodigo().intValue());
			}

			con.commit();
			return codigoUsuarioAsignadoInstancia;
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}

	}

	/*
	 * CANCELACIN DE TRANSITORIAS POR REFERENCIA DE CRUCE
	 */
	private int addNotaContableCruce(Connection con, NotaContable row, Collection<PartidaPendiente> partidasPendientes, int codigoUsuario) throws Exception, NamingException, CreateException, RemoteException {

		Instancia instancia = new Instancia();
		Collection<UsuarioModulo> usuariosModulo;
		Iterator<UsuarioModulo> itUsuariosModulo;
		UsuarioModulo usuarioModulo = new UsuarioModulo();
		int cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL = 6;
		int codigoUsuarioAsignadoInstancia = 0;
		String codigoSucursalOrigenAsignadaInstancia = "";
		int idRow = 0;
		int codigoNotaContable = 0;

		usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(row.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");

		if (usuariosModulo.size() != 0) {
			itUsuariosModulo = usuariosModulo.iterator();
			if (itUsuariosModulo.hasNext()) {
				usuarioModulo = itUsuariosModulo.next();

				codigoUsuarioAsignadoInstancia = usuarioModulo.getCodigo().intValue();
			}
		}

		if (codigoUsuarioAsignadoInstancia != 0) {
			codigoNotaContable = notaContableDAO.add(con, row, codigoUsuario);

			codigoSucursalOrigenAsignadaInstancia = row.getCodigoSucursalOrigen();

			instancia.setCodigoNotaContable(codigoNotaContable);
			instancia.setCodigoUsuarioActual(codigoUsuarioAsignadoInstancia);
			instancia.setFechaHoraInicioTs(DateUtils.getTimestamp());
			instancia.setFechaHoraInicio(new TIMESTAMP(DateUtils.getTimestamp()));
			instancia.setEstado("0");
			instancia.setCodigoSucursalOrigen(codigoSucursalOrigenAsignadaInstancia);
			instancia.setUltimaActualizacionTs(DateUtils.getTimestamp());

			idRow = addInstancia(con, instancia, codigoUsuario);

			addCrucesPartidasPendientesNotaContable(con, partidasPendientes, codigoNotaContable, codigoUsuario);
			// notaContableDAO.addRegistroAuditoria(codigoUsuario, NotaContableDAO.ACCION_ADICIONAR, NotaContable.class.getSimpleName(), codigoNotaContable);
		}

		return idRow;
	}

	/*
	 * CONTABILIDAD LIBRE
	 */
	private int addNotaContableRegistroLibre(Connection con, NotaContable row, Collection<NotaContableRegistroLibre> registrosLibresNotaContable, Collection<Anexo> anexosNotaContable, int codigoUsuario) throws Exception {
		Instancia instancia = new Instancia();
		Collection<UsuarioModulo> usuariosModulo;
		Iterator<UsuarioModulo> itUsuariosModulo;
		UsuarioModulo usuarioModulo = new UsuarioModulo();
		int cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL = 6;
		int codigoUsuarioAsignadoInstancia = 0;
		String codigoSucursalOrigenAsignadaInstancia = "";
		int idRow = 0;
		int codigoNotaContable = 0;

		usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(row.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");

		if (usuariosModulo.size() != 0) {
			itUsuariosModulo = usuariosModulo.iterator();
			if (itUsuariosModulo.hasNext()) {
				usuarioModulo = itUsuariosModulo.next();

				codigoUsuarioAsignadoInstancia = usuarioModulo.getCodigo().intValue();
			}
		}

		if (codigoUsuarioAsignadoInstancia != 0) {
			codigoNotaContable = notaContableDAO.add(con, row, codigoUsuario);

			codigoSucursalOrigenAsignadaInstancia = row.getCodigoSucursalOrigen();

			instancia.setCodigoNotaContable(codigoNotaContable);
			instancia.setCodigoUsuarioActual(codigoUsuarioAsignadoInstancia);
			instancia.setFechaHoraInicioTs(DateUtils.getTimestamp());
			instancia.setFechaHoraInicio(new TIMESTAMP(DateUtils.getTimestamp()));
			instancia.setEstado("0");
			instancia.setCodigoSucursalOrigen(codigoSucursalOrigenAsignadaInstancia);
			instancia.setUltimaActualizacionTs(DateUtils.getTimestamp());

			idRow = addInstancia(con, instancia, codigoUsuario);

			addRegistrosLibresNotaContable(con, registrosLibresNotaContable, codigoNotaContable, codigoUsuario);
			addAnexosTemaNotaContable(con, anexosNotaContable, codigoNotaContable, 0, codigoUsuario);
			// notaContableDAO.addRegistroAuditoria(codigoUsuario, NotaContableDAO.ACCION_ADICIONAR, NotaContable.class.getSimpleName(), codigoNotaContable);
		}

		return idRow;
	}

	/*
	 * Nombre DTO: Instancia Nombre DAO: instanciaDAO Plural: Instancias
	 */

	@Override
	public int addInstancia(Connection con, Instancia row, int codigoUsuario) throws Exception {
		return instanciaDAO.add(con, row, codigoUsuario);
	}

	@Override
	public void updateInstancia(Connection con, Instancia row, int codigoUsuario) throws Exception {
		instanciaDAO.update(con, row, codigoUsuario);
	}

	@Override
	public void deleteInstancia(Connection con, Instancia row, int codigoUsuario) throws Exception {
		instanciaDAO.delete(con, row, codigoUsuario);
	}

	/*
	 * Nombre DTO: ActividadRealizada Nombre DAO: actividadRealizadaDAO Plural: ActividadesRealizadas
	 */

	@Override
	public void addActividadRealizada(Connection con, ActividadRealizada row, int codigoUsuario) throws Exception {
		actividadRealizadaDAO.add(con, row, codigoUsuario);
	}

	@Override
	public void reasignarMisPendientes(Connection con, int codigoUsuario, int codUsuarioLogueado) throws Exception {
		//try {
			Collection<Instancia> instanciasUsuario = new ArrayList<Instancia>();
			Iterator<Instancia> itInstanciasUsuario;
			Instancia instancia = new Instancia();
			ArrayList<UsuarioModulo> usuariosIguales = new ArrayList<UsuarioModulo>();
			UsuarioModulo usuarioModulo = new UsuarioModulo();
			int numeroUsuariosIguales = 0;
			int countUsuarios = 1;

			usuarioModulo = new UsuarioModulo();
			usuarioModulo.setCodigo(codigoUsuario);
			usuarioModulo = getUsuarioModulo(usuarioModulo);

			usuariosIguales = new ArrayList<UsuarioModulo>(getUsuariosModuloPorAreaYRolYEstado(usuarioModulo.getCodigoAreaModificado(), usuarioModulo.getCodigoRol().intValue(), "A"));
			numeroUsuariosIguales = usuariosIguales.size();

			for (countUsuarios = 0; countUsuarios < numeroUsuariosIguales; countUsuarios++) {
				if (usuariosIguales.get(countUsuarios).getCodigo().intValue() == codigoUsuario) {
					usuariosIguales.remove(countUsuarios);
					break;
				}
			}

			numeroUsuariosIguales = usuariosIguales.size();

			instancia.setCodigoUsuarioActual(codigoUsuario);
			instanciasUsuario = getInstanciasPorUsuario(instancia);

			countUsuarios = 0;
			if (instanciasUsuario.size() != 0) {
				itInstanciasUsuario = instanciasUsuario.iterator();
				while (itInstanciasUsuario.hasNext()) {
					instancia = itInstanciasUsuario.next();

					instancia.setCodigoUsuarioActual(usuariosIguales.get(countUsuarios).getCodigo().intValue());

					updateInstancia(con, instancia, codUsuarioLogueado);

					if (countUsuarios < numeroUsuariosIguales - 1) {
						countUsuarios++;
					} else {
						countUsuarios = 0;
					}
				}
			}
	}

	@Override
	public String verificarUsuarioSubGerente(String codigoSucursalOrigen) throws Exception {

		if (getUsuariosModuloPorAreaYRolYEstado(codigoSucursalOrigen, IRol.SUBGERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A").isEmpty()) {
			return "No existe ningn usuario con rol 'SubGerente, Gerente o Responsable del rea Central' para autorizar la Nota Contable. Consulte al Administrador del sistema.";
		}

		return "";
	}

	@Override
	public String verificarCentroEspecial(String codigoSucursalOrigen) throws Exception {
		CentroEspecial centro = new CentroEspecial();
		centro.setCodigoSucursal(codigoSucursalOrigen);
		centro = getCentroEspecialPorSucursal(centro);

		if (centro.getCodigo().intValue() == 0) {
			return "No puede ingresar a este modulo ya que no pertenece a un centro especial";
		}

		return "";
	}

	@Override
	public int crearInstanciaNotaContable(NotaContable notaContable, Collection<NotaContableTema> temasNotaContable, Collection<NotaContableRegistroLibre> registrosLibresNotaContable, Collection<Anexo> anexosNotaContable,
			Collection<NotaContableTotal> totalesNotaContable, Collection<PartidaPendiente> partidasPendientes, int codigoUsuario) throws Exception, NamingException, CreateException, RemoteException {
		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);

			ActividadRealizada actividadRealizada = new ActividadRealizada();
			// int idNotaContable = 0;
			int idInstancia = 0;

			if (notaContable.getTipoNota().equals("R")) {
				idInstancia = addNotaContableRegistro(con, notaContable, temasNotaContable, totalesNotaContable, codigoUsuario);
			} else if (notaContable.getTipoNota().equals("C")) {
				idInstancia = addNotaContableCruce(con, notaContable, partidasPendientes, codigoUsuario);
			} else if (notaContable.getTipoNota().equals("L")) {
				idInstancia = addNotaContableRegistroLibre(con, notaContable, registrosLibresNotaContable, anexosNotaContable, codigoUsuario);
			}

			if (idInstancia != 0) {
				actividadRealizada.setCodigoInstancia(idInstancia);
				actividadRealizada.setCodigoUsuario(codigoUsuario);
				actividadRealizada.setFechaHoraTs(DateUtils.getTimestamp());
				actividadRealizada.setFechaHoraCierreTs(actividadRealizada.getFechaHoraTs());
				actividadRealizada.setEstado("0");
				actividadRealizada.setValor1("");
				actividadRealizada.setValor2("");
				actividadRealizada.setValor3("");

				addActividadRealizada(con, actividadRealizada, codigoUsuario);
			}

			con.commit();
			return idInstancia;
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String verificarUsuarioSiguienteActividad(Instancia instancia, int codigoUsuario, boolean aprobada, int codigoCausalDevolucion) throws Exception {
		CargaAltamiraSessionBean cargaAltamiraManager = new CargaAltamiraSessionBean();

		NotaContable notaContable = new NotaContable();
		NotaContableTotal notaContableTotal = new NotaContableTotal();
		Collection totalesNotaContable;
		Iterator itTotalesNotaContable;
		double totalNotaContable = 0;

		Concepto concepto = new Concepto();
		UnidadAnalisis unidadAnalisis = new UnidadAnalisis();

		Padrino padrino = new Padrino();
		MontoAutorizado montoAutorizado = new MontoAutorizado();
		EnteAutorizador enteAutorizador = new EnteAutorizador();
		MontoAutorizadoGeneral montoAutorizadoGeneral = new MontoAutorizadoGeneral();
		MontoAutorizadoGeneralDAO objMontosAutorizadosGenerales = new MontoAutorizadoGeneralDAO();
		
		int posicionMontoAutorizado = 0;
		int errorAsignacion = 0;
		ArrayList<Object> posicionMontosAutorizadosGenerales;
		int contadorMontoAutorizadoEncontrado = 0;
		Collection sucursalAutorizacion = new ArrayList();
		ArrayList<Sucursal> NuevaSucursalAutorizacion = new ArrayList<Sucursal>();
		Iterator itSucursalAutorizacion;
		Iterator itAutorizacionMonto;
		Sucursal sucursal = new Sucursal();
		Collection usuariosModulo;
		int cs_CODIGO_ADMINISTRADOR_CONSOLIDACION = 2;
		String mensaje = "";

		boolean indEstadoEnteAutorizador = false;
		boolean indExisteAutorizador = false;
		boolean indEstadoPrecierre = false;
		boolean indEstadoCentroSuperiorAutorizador = false;

		notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
		notaContable = getNotaContable(notaContable);

		concepto.setCodigo(notaContable.getCodigoConcepto());
		concepto = getConcepto(concepto);

		if (notaContable.getTipoNota().equals("R")) {
			if (aprobada) {
				if (instancia.getEstado().equals("0")) {
					if (concepto.getVistoBuenoAnalisis().equals("S")) {
						unidadAnalisis.setCodigo(concepto.getCodigoUnidadAnalisis());
						unidadAnalisis = getUnidadAnalisis(unidadAnalisis);

						padrino.setCodigoUnidadAnalisis(unidadAnalisis.getCodigo().intValue());
						padrino.setEstado("A");

						if (getPadrinosPorUnidadAnalisisYEstado(padrino).isEmpty()) {
							mensaje = "No existe ningn usuario con rol 'Padrino' para autorizar las Notas Contables de esa sucursal origen. Por favor consulte al Administrador del sistema.";
						}
					} else {
						indEstadoEnteAutorizador = true;
					}
				}

				if (instancia.getEstado().equals("1")) {
					mensaje = verificarUsuarioSubGerente(notaContable.getCodigoSucursalOrigen());
				}

				if (indEstadoEnteAutorizador || instancia.getEstado().equals("2")) {
					indExisteAutorizador = false;
					if (concepto.getAutorizacionTercero().equals("S")) {
						totalesNotaContable = getTotalesPorNotaContable(notaContable.getCodigo().intValue());

						// El total de la Nota Contable no se esta calculando porque son diferentes Divisa. Se esta tomando el primer total
						if (totalesNotaContable.size() != 0) {
							itTotalesNotaContable = totalesNotaContable.iterator();
							while (itTotalesNotaContable.hasNext() && totalNotaContable == 0) {
								notaContableTotal = (NotaContableTotal) itTotalesNotaContable.next();
								if (notaContableTotal.getCodigoDivisa().equals("COP")) {
									totalNotaContable = notaContableTotal.getTotal();
								}
							}
						}

						montoAutorizado.setCodigoTemaAutorizacion(concepto.getCodigoTemaAutorizacion());
						montoAutorizado.setCodigoTipoAutorizacion(notaContable.getCodigoTipoEvento());
						montoAutorizado.setEstado("A");
						montoAutorizado = getMontoAutorizadoPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizado);

						if (montoAutorizado.getCodigoEnteAutorizador().intValue() != 0) {
							enteAutorizador.setCodigo(montoAutorizado.getCodigoEnteAutorizador());
							enteAutorizador = getEnteAutorizador(enteAutorizador);

							if (enteAutorizador.getCodigoUsuarioModulo().intValue() != 0 && enteAutorizador.getEstado().equals("A")) {
								indExisteAutorizador = true;
							}
						}

						if (!indExisteAutorizador) {
							/* FLUJO ADICIONAL */
							
							montoAutorizadoGeneral.setCodigoTemaAutorizacion(concepto.getCodigoTemaAutorizacion());
							montoAutorizadoGeneral.setCodigoTipoAutorizacion(notaContable.getCodigoTipoEvento());
							montoAutorizadoGeneral.setEstado("A");
							montoAutorizadoGeneral = getMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizadoGeneral, totalNotaContable);
							posicionMontoAutorizado = getPosicionMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizadoGeneral, totalNotaContable);
							posicionMontosAutorizadosGenerales = objMontosAutorizadosGenerales.posicion(montoAutorizadoGeneral, totalNotaContable);
							/**COL514313I001449 MODIFICACION PARA VALIDAR LOS NIVELES DE AUTORIZACION
							 * SE CARGA EN UNA COLECCION LAS SUCURSALES AUTORIZADAS CON USUARIOS Y SE 
							 * COMPARA COMTRA LA POSICION DEL MONTO AUTORIZADO GENERAL SEGUN POSICION posicionMontoAutorizado  ****/	
							if (montoAutorizadoGeneral.getCodigoRol().intValue() != 0) {
								sucursal = new Sucursal();
								sucursal.setCodigo(notaContable.getCodigoSucursalOrigen());
								try {
									sucursalAutorizacion = cargaAltamiraManager.getCadenaAutorizacionSucursal(sucursal);
									indEstadoCentroSuperiorAutorizador = false;
									if (sucursalAutorizacion.size() != 0) {
										itSucursalAutorizacion = sucursalAutorizacion.iterator();
										itAutorizacionMonto = posicionMontosAutorizadosGenerales.iterator();
										montoAutorizadoGeneral =  (MontoAutorizadoGeneral) itAutorizacionMonto.next();
										contadorMontoAutorizadoEncontrado = 0;
											
										while (itSucursalAutorizacion.hasNext()) {
											sucursal = (Sucursal) itSucursalAutorizacion.next();
											usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(sucursal.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
												if (usuariosModulo.size() != 0) {
													NuevaSucursalAutorizacion.add(sucursal);
													if (itAutorizacionMonto.hasNext()){
														montoAutorizadoGeneral =  (MontoAutorizadoGeneral) itAutorizacionMonto.next();	
													}
												}
										}
												
										if (NuevaSucursalAutorizacion.size() == posicionMontosAutorizadosGenerales.size()){
											sucursal = NuevaSucursalAutorizacion.get(posicionMontoAutorizado);
											montoAutorizadoGeneral = (MontoAutorizadoGeneral) posicionMontosAutorizadosGenerales.get(posicionMontoAutorizado);
											usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(sucursal.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
											indEstadoCentroSuperiorAutorizador = true;
										}else {
											errorAsignacion=1;
										}

									if (indEstadoCentroSuperiorAutorizador) {
										indExisteAutorizador = true;
									}
								}
							} catch (Exception le_e) {
								//System.out.println("error" + le_e);
							}
						}
							/**COL514313I001449 MODIFICACION SI NO ESTA CONFIGURADA LA PARAMETRIA SE GENERA MENSAJE 
							 * DE AVISO AL ADMINISTRADOR ****/		
						if (!indExisteAutorizador) {
							mensaje = "No existe ningn usuario con rol 'Autorizador' para autorizar la Nota Contable. Por favor consulte al Administrador del sistema.";
							if(errorAsignacion==1)
								mensaje = "No esta configurada la parametria para autorizar. Por favor consulte al Administrador del sistema.";
						}
					} else {
						indEstadoPrecierre = true;
					}
				}
			}

				if (indEstadoPrecierre || instancia.getEstado().equals("3")) {
					if (getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A").isEmpty()) {
						mensaje = "No existe ningn usuario con rol 'Administrador de Consolidacin' para autorizar la Nota Contable. Por favor consulte al Administrador del sistema.";
					}
				}

				if (instancia.getEstado().equals("4")) {
					if (getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A").isEmpty()) {
						mensaje = "No existe ningn usuario con rol 'Administrador de Consolidacin' para autorizar la Nota Contable. Por favor consulte al Administrador del sistema.";
					}
				}
			}
		}
		return mensaje;
	}

	@Override
	public void siguienteActividad(ISalida salida, TreeMap<String, Instancia> notas) throws Exception {
		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);

			// salida.setNumNota(getNumRadReal(salida.getConsecutivo()));
			String numRadicacion = salida.getNumNota();
			Instancia instancia = notas.get(numRadicacion);
			// si es la primera vez que se procesa la nota en este archivo, se pasa a la siguiente actividad
			if (instancia == null) {
				// se envia la nota asociada a la siguiente actividad
				NotaContable nota = new NotaContable();
				nota.setNumeroRadicacion(numRadicacion);
				instancia = getInstanciasPorNumeroRadicacion(nota, "", 0).iterator().next();
				siguienteActividad(con, instancia, null, null, 0, true, 0, "");
			}

			if (instancia.getEstado().equals("6")) {// si se trata de un cierre, se debe actualizar el asiento y el monto del registro actual
				// si es la primera vez que se analiza la nota, se actualiza la fecha
				if (!notas.containsKey(numRadicacion)) {
					NotaContable nota = new NotaContable();
					nota.setCodigo(instancia.getCodigoNotaContable());
					nota = getNotaContable(nota);
					//nota.setFechaRegistroAltamira(new TIMESTAMP(new Timestamp(new Date().getTime())));
					nota.setFechaRegistroAltamira(new TIMESTAMP(new Timestamp(new Date().getTime())));
					updateNotaContable(con, nota, 0);
				}
				AsientoSalida asiento = (AsientoSalida) salida;
				int tipoReg = Integer.valueOf(asiento.getConsecutivo().substring(0, 1));
				int idReg = Integer.valueOf(asiento.getConsecutivo().substring(1));
				switch (tipoReg) {
				case IMP_PARTIDA:
					updateImpPartida(con, asiento, idReg);
					break;
				case PARTIDA:
					updatePartida(con, asiento, idReg);
					break;
				case REG_LIBRE:
					updateRegLibre(con, asiento, idReg);

					break;
				case CRUCE_REF:
					updateCruceRef(con, asiento, idReg);
					break;
				}
			}
			notas.put(numRadicacion, instancia);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}
	}

	// private String getNumRadReal(String consecutivo) throws Exception {
	// int tipo = Integer.valueOf(consecutivo.substring(0, 1));
	// int codigo = Integer.valueOf(consecutivo.substring(1));
	// int codigoNota = 0;
	// switch (tipo) {
	// case 0:// impuestos de la partida
	// case 2:// impuestos de contrapartida
	// NotaContableTemaImpuesto imp = new NotaContableTemaImpuesto();
	// imp.setCodigo(codigo);
	// imp = notaContableTemaImpuestoDAO.findByPrimaryKey(imp);
	// codigoNota = imp.getCodigoNotaContable();
	// break;
	// case 1:// partidas
	// case 3:// contrapartida
	// NotaContableTema tema = new NotaContableTema();
	// tema.setCodigo(codigo);
	// tema = notaContableTemaDAO.findByPrimaryKey(tema);
	// codigoNota = tema.getCodigoNotaContable().intValue();
	// break;
	// case 4:// registro libre
	// NotaContableRegistroLibre reg = new NotaContableRegistroLibre();
	// reg.setCodigo(codigo);
	// reg = notaContableRegistroLibreDAO.findByPrimaryKey(reg);
	// codigoNota = reg.getCodigoNotaContable();
	// break;
	// case 5:// referencia de cruce
	// NotaContableCrucePartidaPendiente cruce = new NotaContableCrucePartidaPendiente();
	// cruce.setCodigo(codigo);
	// cruce = notaContableCrucePartidaPendienteDAO.findByPrimaryKey(cruce);
	// codigoNota = cruce.getCodigoNotaContable();
	// break;
	// }
	// if (codigoNota > 0) {
	// NotaContable nc = new NotaContable();
	// nc = notaContableDAO.findByPrimaryKey(nc);
	// return nc.getNumeroRadicacion();
	// }
	// return "";
	// }

	private void updatePartida(Connection con, AsientoSalida asiento, int idReg) throws Exception {
		NotaContableTema registro = new NotaContableTema();
		registro.setCodigo(idReg);
		registro.setNumeroAsiento("" + asiento.getAsiento());
		registro.setNumeroApunte("" + asiento.getApunte());
		notaContableTemaDAO.update(con, registro, 0);
	}

	private void updateImpPartida(Connection con, AsientoSalida asiento, int idReg) throws Exception {
		NotaContableTemaImpuesto registro = new NotaContableTemaImpuesto();
		registro.setCodigo(idReg);
		registro.setNumeroAsiento("" + asiento.getAsiento());
		registro.setNumeroApunte("" + asiento.getApunte());
		notaContableTemaImpuestoDAO.update(con, registro, 0);
	}

	private void updateRegLibre(Connection con, AsientoSalida asiento, int idReg) throws Exception {
		NotaContableRegistroLibre registro = new NotaContableRegistroLibre();
		registro.setCodigo(idReg);
		registro.setNumeroAsiento("" + asiento.getAsiento());
		registro.setNumeroApunte("" + asiento.getApunte());
		notaContableRegistroLibreDAO.update(con, registro, 0);
	}

	private void updateCruceRef(Connection con, AsientoSalida asiento, int idReg) throws Exception {
		NotaContableCrucePartidaPendiente registro = new NotaContableCrucePartidaPendiente();
		registro.setCodigo(idReg);
		registro.setNumeroAsiento("" + asiento.getAsiento());
		registro.setNumeroApunte("" + asiento.getApunte());
		notaContableCrucePartidaPendienteDAO.update(con, registro, 0);
	}

	@Override
	public int siguienteActividad(Instancia instancia, Collection<NotaContableTema> temasNotaContableAux, Collection<NotaContableTotal> totalesNotaContableAux, int codigoUsuario, boolean aprobada, int codigoCausalDevolucion, String otraCausalDev)
			throws Exception {
		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);
			int codigoUsuarioAsignado = siguienteActividad(con, instancia, temasNotaContableAux, totalesNotaContableAux, codigoUsuario, aprobada, codigoCausalDevolucion, otraCausalDev);
			con.commit();
			return codigoUsuarioAsignado;
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}
	}

	@Override
	public int siguienteActividad(Instancia instancia, Collection<NotaContableTema> temasNotaContableAux, Collection<NotaContableTotal> totalesNotaContableAux, int codigoUsuario, boolean aprobada, int codigoCausalDevolucion, String otraCausalDev,
			boolean cheqeoReasignacion)
			throws Exception {
		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);
			int codigoUsuarioAsignado = siguienteActividad(con, instancia, temasNotaContableAux, totalesNotaContableAux, codigoUsuario, aprobada, codigoCausalDevolucion, otraCausalDev,cheqeoReasignacion);
			if(codigoUsuarioAsignado!=-1){
			con.commit();
			}
			return codigoUsuarioAsignado;
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}
	}
	
	public int siguienteActividad(Connection con, Instancia instancia, Collection<NotaContableTema> temasNotaContableAux, Collection<NotaContableTotal> totalesNotaContableAux, int codigoUsuario, boolean aprobada, int codigoCausalDevolucion,
			String otraCausalDev) throws Exception {
		CargaAltamiraSessionBean cargaAltamiraManager = new CargaAltamiraSessionBean();
		Instancia instanciaActualizada = new Instancia();

		NotaContable notaContable = new NotaContable();
		Collection<NotaContableTotal> totalesNotaContable;
		double totalNotaContable = 0;

		ArrayList<ActividadRealizada> actividadesRealizadas = new ArrayList<ActividadRealizada>();
		ActividadRealizada actividadRealizada = new ActividadRealizada();
		Concepto concepto = new Concepto();
		UnidadAnalisis unidadAnalisis = new UnidadAnalisis();

		ArrayList<Padrino> padrinos = new ArrayList<Padrino>();
		Padrino padrino = new Padrino();
		MontoAutorizado montoAutorizado = new MontoAutorizado();
		EnteAutorizador enteAutorizador = new EnteAutorizador();
		MontoAutorizadoGeneral montoAutorizadoGeneral = new MontoAutorizadoGeneral();
		int posicionMontoAutorizado = 0;
		Collection<Sucursal> sucursalAutorizacion = new ArrayList<Sucursal>();
		Iterator itAutorizacionMonto;

		Collection<UsuarioModulo> usuariosModulo = new ArrayList<UsuarioModulo>();
		Iterator itUsuarioModulo;
		UsuarioModulo usuarioModulo = new UsuarioModulo();
		int cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL = 6;
		int cs_CODIGO_ADMINISTRADOR_CONSOLIDACION = 2;
		int cs_CODIGO_PADRINOS = 4;
		@SuppressWarnings("unused")
		int cs_CODIGO_AUTORIZADOR = 5;
		int codigoUsuarioAsignado = 0;
		@SuppressWarnings("unused")
		int codigoUnidadAnalisisAsignada = 0;
		String siguienteEstado = "";

		boolean indEstadoEnteAutorizador = false;
		boolean indEstadoCentroSuperiorAutorizador = false;
		boolean indEstadoPrecierre = false;
		Iterator itSucursalAutorizacion;
		ArrayList<Sucursal> NuevaSucursalAutorizacion = new ArrayList<Sucursal>();
		ArrayList<Object> posicionMontosAutorizadosGenerales;
		MontoAutorizadoGeneralDAO objMontosAutorizadosGenerales = new MontoAutorizadoGeneralDAO();

		notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
		notaContable = getNotaContable(notaContable);

		concepto.setCodigo(notaContable.getCodigoConcepto());
		concepto = getConcepto(concepto);

		if (aprobada && (instancia.getEstado().equals("4") || instancia.getEstado().equals("5"))) {
			usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

			if (!usuariosModulo.isEmpty()) {
				usuarioModulo = usuariosModulo.iterator().next();

				siguienteEstado = "" + (Integer.valueOf(instancia.getEstado()) + 1);
				codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
				codigoUnidadAnalisisAsignada = 0;
			}
		} else {
			if (notaContable.getTipoNota().equals("R")) {
				if (aprobada) {
					if (instancia.getEstado().equals("0")) {
						if (concepto.getVistoBuenoAnalisis().equals("S")) {
							unidadAnalisis.setCodigo(concepto.getCodigoUnidadAnalisis());
							unidadAnalisis = getUnidadAnalisis(unidadAnalisis);

							padrino.setCodigoUnidadAnalisis(unidadAnalisis.getCodigo().intValue());
							padrino.setEstado("A");
							padrinos = new ArrayList<Padrino>(getPadrinosPorUnidadAnalisisYEstado(padrino));

							// Se debe balancear por carga
							if (!padrinos.isEmpty()) {
								siguienteEstado = "2";
								// codigoUsuarioAsignado = ((Padrino)padrinos.get(0)).getCodigoUsuario();

								try {
									codigoUsuarioAsignado = getUsuarioAsignadoPorBalanceo(siguienteEstado, unidadAnalisis.getCodigoSucursal(), cs_CODIGO_PADRINOS).getCodigo().intValue();
								} catch (Exception le_e) {
									codigoUsuarioAsignado = padrinos.get(0).getCodigoUsuario().intValue();
								}
								codigoUnidadAnalisisAsignada = 0;
							}
						} else {
							indEstadoEnteAutorizador = true;
						}
					}

					if (instancia.getEstado().equals("1")) {
						siguienteEstado = "0";
						codigoUsuarioAsignado = updateNotaContableRegistro(notaContable, temasNotaContableAux, totalesNotaContableAux, codigoUsuario);
					}

					if (indEstadoEnteAutorizador || instancia.getEstado().equals("2")) {
						if (concepto.getAutorizacionTercero().equals("S")) {
							totalesNotaContable = getTotalesPorNotaContable(notaContable.getCodigo().intValue());

							// El total de la Nota Contable no se esta calculando porque son diferentes Divisa. Se esta tomando el primer total
							for (NotaContableTotal notaContableTotal : totalesNotaContable) {
								if (totalNotaContable == 0 && notaContableTotal.getCodigoDivisa().equals("COP")) {
									totalNotaContable = notaContableTotal.getTotal();
								}
							}

							montoAutorizado.setCodigoTemaAutorizacion(concepto.getCodigoTemaAutorizacion());
							montoAutorizado.setCodigoTipoAutorizacion(notaContable.getCodigoTipoEvento());
							montoAutorizado.setEstado("A");
							montoAutorizado = getMontoAutorizadoPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizado);

							if (montoAutorizado.getCodigoEnteAutorizador().intValue() != 0) {
								enteAutorizador.setCodigo(montoAutorizado.getCodigoEnteAutorizador());
								enteAutorizador = getEnteAutorizador(enteAutorizador);

								if (enteAutorizador.getCodigoUsuarioModulo().intValue() != 0 && enteAutorizador.getEstado().equals("A")) {
									siguienteEstado = "3";
									codigoUsuarioAsignado = enteAutorizador.getCodigoUsuarioModulo().intValue();
									codigoUnidadAnalisisAsignada = 0;
								}
							}

							/* FLUJO ADICIONAL */
							montoAutorizadoGeneral.setCodigoTemaAutorizacion(concepto.getCodigoTemaAutorizacion());
							montoAutorizadoGeneral.setCodigoTipoAutorizacion(notaContable.getCodigoTipoEvento());
							montoAutorizadoGeneral.setEstado("A");
							montoAutorizadoGeneral = getMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizadoGeneral, totalNotaContable);
							posicionMontoAutorizado = getPosicionMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizadoGeneral, totalNotaContable);
							posicionMontosAutorizadosGenerales = objMontosAutorizadosGenerales.posicion(montoAutorizadoGeneral, totalNotaContable);
							/**COL514313I001449 MODIFICACION PARA VALIDAR LOS NIVELES DE AUTORIZACION
							 * SE CARGA EN UNA COLECCION LAS SUCURSALES AUTORIZADAS CON USUARIOS Y SE 
							 * COMPARA COMTRA LA POSICION DEL MONTO AUTORIZADO GENERAL SEGUN POSICION posicionMontoAutorizado  ****/

							if (montoAutorizadoGeneral.getCodigoRol().intValue() != 0) {
								Sucursal sucursal = new Sucursal();
								sucursal.setCodigo(notaContable.getCodigoSucursalOrigen());
								try {
									sucursalAutorizacion = cargaAltamiraManager.getCadenaAutorizacionSucursal(sucursal);
									indEstadoCentroSuperiorAutorizador = false;
									if (!sucursalAutorizacion.isEmpty()) {
										itSucursalAutorizacion = sucursalAutorizacion.iterator();
										itAutorizacionMonto = posicionMontosAutorizadosGenerales.iterator();
										montoAutorizadoGeneral =  (MontoAutorizadoGeneral) itAutorizacionMonto.next();

										for (Sucursal suc : sucursalAutorizacion) {
											sucursal = (Sucursal) itSucursalAutorizacion.next();
											usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(suc.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
											if (usuariosModulo.size() != 0) {
												NuevaSucursalAutorizacion.add(sucursal);
												if (itAutorizacionMonto.hasNext()){
													montoAutorizadoGeneral =  (MontoAutorizadoGeneral) itAutorizacionMonto.next();	
												}
											}
										}
										
										if (NuevaSucursalAutorizacion.size() == posicionMontosAutorizadosGenerales.size()){
											sucursal = NuevaSucursalAutorizacion.get(posicionMontoAutorizado);
											montoAutorizadoGeneral = (MontoAutorizadoGeneral) posicionMontosAutorizadosGenerales.get(posicionMontoAutorizado);
											usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(sucursal.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
											indEstadoCentroSuperiorAutorizador = true;
										}
											/**if (!indEstadoCentroSuperiorAutorizador) {
												usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(sucursal.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
												if (!usuariosModulo.isEmpty()) {
													usuarioModulo = usuariosModulo.iterator().next();

													if (contadorMontoAutorizadoEncontrado == posicionMontoAutorizado) {
														indEstadoCentroSuperiorAutorizador = true;
													}
												}
											}**/
									}

									if (indEstadoCentroSuperiorAutorizador) {
										siguienteEstado = "3";
										usuarioModulo = usuariosModulo.iterator().next();
										codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
										codigoUnidadAnalisisAsignada = 0;
									}
								} catch (Exception le_e) {
								}
							}
						} else {
							indEstadoPrecierre = true;
						}
					}

					if (indEstadoPrecierre || instancia.getEstado().equals("3")) {
						usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

						if (!usuariosModulo.isEmpty()) {
							usuarioModulo = usuariosModulo.iterator().next();

							siguienteEstado = "4";
							codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
							codigoUnidadAnalisisAsignada = 0;
						}
					}

				} else {
					if (instancia.getEstado().equals("0") || instancia.getEstado().equals("2") || instancia.getEstado().equals("3") || instancia.getEstado().equals("4") || instancia.getEstado().equals("5")) {
						actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
						actividadesRealizadas = new ArrayList<ActividadRealizada>(getActividadesRealizadasPorInstancia(actividadRealizada));

						siguienteEstado = "1";
						if (actividadesRealizadas.size() != 0) {
							codigoUsuarioAsignado = actividadesRealizadas.get(0).getCodigoUsuario().intValue();
						}
						codigoUnidadAnalisisAsignada = 0;
					}
				}
			} else if (notaContable.getTipoNota().equals("C")) {
				if (aprobada) {

					if (instancia.getEstado().equals("0")) {
						unidadAnalisis.setEstado("A");
						unidadAnalisis.setAutorizaReferenciaCruce("S");
						unidadAnalisis = getUnidadAnalisisPorAutorizaReferenciaCruceYEstado(unidadAnalisis);

						padrino.setCodigoUnidadAnalisis(unidadAnalisis.getCodigo().intValue());
						padrino.setEstado("A");
						padrinos = new ArrayList<Padrino>(getPadrinosPorUnidadAnalisisYEstado(padrino));

						// Se debe balancear por carga
						if (padrinos.size() != 0) {
							siguienteEstado = "2";
							// codigoUsuarioAsignado = ((Padrino)padrinos.get(0)).getCodigoUsuario();

							try {
								codigoUsuarioAsignado = getUsuarioAsignadoPorBalanceo(siguienteEstado, unidadAnalisis.getCodigoSucursal(), cs_CODIGO_PADRINOS).getCodigo().intValue();
							} catch (Exception le_e) {
								codigoUsuarioAsignado = padrinos.get(0).getCodigoUsuario().intValue();
							}
							codigoUnidadAnalisisAsignada = 0;	
						}
					}

					if (indEstadoPrecierre || instancia.getEstado().equals("2")) {
						usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

						if (!usuariosModulo.isEmpty()) {
							usuarioModulo = usuariosModulo.iterator().next();

							siguienteEstado = "4";
							codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
							codigoUnidadAnalisisAsignada = 0;
						}
					}

					if (instancia.getEstado().equals("1")) {
						siguienteEstado = "0";
						usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(notaContable.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");

						if (!usuariosModulo.isEmpty()) {
							usuarioModulo = usuariosModulo.iterator().next();
							codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
						}
					}

				} else {
					if (instancia.getEstado().equals("0") || instancia.getEstado().equals("2") || instancia.getEstado().equals("3") || instancia.getEstado().equals("4") || instancia.getEstado().equals("5")) {
						actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
						actividadesRealizadas = new ArrayList<ActividadRealizada>(getActividadesRealizadasPorInstancia(actividadRealizada));

						siguienteEstado = "1";
						if (actividadesRealizadas.size() != 0) {
							codigoUsuarioAsignado = actividadesRealizadas.get(0).getCodigoUsuario().intValue();
						}
						codigoUnidadAnalisisAsignada = 0;
					}
				}
			} else if (notaContable.getTipoNota().equals("L")) {
				if (aprobada) {

					if (indEstadoPrecierre || instancia.getEstado().equals("0")) {
						usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

						if (!usuariosModulo.isEmpty()) {
							usuarioModulo = usuariosModulo.iterator().next();
							siguienteEstado = "4";
							codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
							codigoUnidadAnalisisAsignada = 0;
						}
					}
					if (instancia.getEstado().equals("1")) {
						siguienteEstado = "0";
						usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(notaContable.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");

						if (!usuariosModulo.isEmpty()) {
							usuarioModulo = usuariosModulo.iterator().next();
							codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
						}
					}
				} else {
					if (instancia.getEstado().equals("0") || instancia.getEstado().equals("2") || instancia.getEstado().equals("3") || instancia.getEstado().equals("4") || instancia.getEstado().equals("5")) {
						actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
						actividadesRealizadas = new ArrayList<ActividadRealizada>(getActividadesRealizadasPorInstancia(actividadRealizada));

						siguienteEstado = "1";
						if (actividadesRealizadas.size() != 0) {
							codigoUsuarioAsignado = actividadesRealizadas.get(0).getCodigoUsuario().intValue();
						}
						codigoUnidadAnalisisAsignada = 0;
					}
				}
			}
		}

		Timestamp fecha = DateUtils.getTimestamp();

		// Actualiza Instancia
		instanciaActualizada = instancia;
		instanciaActualizada.setCodigoUsuarioActual(codigoUsuarioAsignado);
		instanciaActualizada.setEstado(siguienteEstado);
		instanciaActualizada.setUltimaActualizacionTs(fecha);

		updateInstancia(con, instanciaActualizada, codigoUsuario);

		// se consulta la actividad previa
		actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
		ActividadRealizada ultAcRe = actividadRealizadaDAO.getUltima(actividadRealizada);

		// se obtienen los festivos
		Collection<Festivo> festivos = cargaAltamiraManager.getFestivos();
		// se hace el calculo de duracion
		int cantFest = DateUtils.getFestivosEntre(new Date(ultAcRe.getFechaHoraCierreTs().getTime()), new Date(fecha.getTime()), new ArrayList<Festivo>(festivos));
		int duracion = (int) ((fecha.getTime() - ultAcRe.getFechaHoraCierreTs().getTime()) / 1000 / 60);
		duracion -= cantFest * 24 * 60;

		// Adiciona Actividad Realizada al historial
		ActividadRealizada actividadNueva = new ActividadRealizada();

		actividadNueva.setCodigoInstancia(instancia.getCodigo().intValue());
		actividadNueva.setCodigoUsuario(codigoUsuario);
		actividadNueva.setFechaHoraTs(ultAcRe.getFechaHoraCierreTs());
		actividadNueva.setFechaHoraCierreTs(fecha);
		actividadNueva.setDuracionActividad(duracion);
		actividadNueva.setEstado(instancia.getEstado());
		actividadNueva.setValor1(codigoCausalDevolucion != 0 ? String.valueOf(codigoCausalDevolucion) : "");
		actividadNueva.setValor2(otraCausalDev != null ? otraCausalDev : "");
		actividadNueva.setValor3("");
		addActividadRealizada(con, actividadNueva, codigoUsuario);
		return codigoUsuarioAsignado;
	}
	
	public int siguienteActividad(Connection con, Instancia instancia, Collection<NotaContableTema> temasNotaContableAux, Collection<NotaContableTotal> totalesNotaContableAux, int codigoUsuario, boolean aprobada, int codigoCausalDevolucion,
			String otraCausalDev,boolean chequeoReasignacion) throws Exception {
		CargaAltamiraSessionBean cargaAltamiraManager = new CargaAltamiraSessionBean();
		Instancia instanciaActualizada = new Instancia();

		NotaContable notaContable = new NotaContable();
		Collection<NotaContableTotal> totalesNotaContable;
		double totalNotaContable = 0;

		ArrayList<ActividadRealizada> actividadesRealizadas = new ArrayList<ActividadRealizada>();
		ActividadRealizada actividadRealizada = new ActividadRealizada();
		Concepto concepto = new Concepto();
		UnidadAnalisis unidadAnalisis = new UnidadAnalisis();

		ArrayList<Padrino> padrinos = new ArrayList<Padrino>();
		Padrino padrino = new Padrino();
		MontoAutorizado montoAutorizado = new MontoAutorizado();
		EnteAutorizador enteAutorizador = new EnteAutorizador();
		MontoAutorizadoGeneral montoAutorizadoGeneral = new MontoAutorizadoGeneral();
		int posicionMontoAutorizado = 0;
		Collection<Sucursal> sucursalAutorizacion = new ArrayList<Sucursal>();
		Iterator itAutorizacionMonto;

		Collection<UsuarioModulo> usuariosModulo = new ArrayList<UsuarioModulo>();
		Iterator itUsuarioModulo;
		UsuarioModulo usuarioModulo = new UsuarioModulo();
		int cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL = 6;
		int cs_CODIGO_ADMINISTRADOR_CONSOLIDACION = 2;
		int cs_CODIGO_PADRINOS = 4;
		@SuppressWarnings("unused")
		int cs_CODIGO_AUTORIZADOR = 5;
		int codigoUsuarioAsignado = 0;
		@SuppressWarnings("unused")
		int codigoUnidadAnalisisAsignada = 0;
		String siguienteEstado = "";

		boolean indEstadoEnteAutorizador = false;
		boolean indEstadoCentroSuperiorAutorizador = false;
		boolean indEstadoPrecierre = false;
		Iterator itSucursalAutorizacion;
		ArrayList<Sucursal> NuevaSucursalAutorizacion = new ArrayList<Sucursal>();
		ArrayList<Object> posicionMontosAutorizadosGenerales;
		MontoAutorizadoGeneralDAO objMontosAutorizadosGenerales = new MontoAutorizadoGeneralDAO();

		notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
		notaContable = getNotaContable(notaContable);

		concepto.setCodigo(notaContable.getCodigoConcepto());
		concepto = getConcepto(concepto);

		if (aprobada && (instancia.getEstado().equals("4") || instancia.getEstado().equals("5"))) {
			if(chequeoReasignacion && instancia.getEstado().equals("4")){
				usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

				if (!usuariosModulo.isEmpty()) {
					usuarioModulo = usuariosModulo.iterator().next();

					siguienteEstado = "" + (Integer.valueOf(instancia.getEstado()) + 1);
					codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
					codigoUnidadAnalisisAsignada = 0;
				}	
			}else
			{	
				if(instancia.getEstado().equals("5"))
				{
					usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

					if (!usuariosModulo.isEmpty()) {
						usuarioModulo = usuariosModulo.iterator().next();

						siguienteEstado = "" + (Integer.valueOf(instancia.getEstado()) + 1);
						codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
						codigoUnidadAnalisisAsignada = 0;
					}
				}else{
					return -1;
				}
			}
		} else {
			if (notaContable.getTipoNota().equals("R")) {
				if (aprobada) {
					if (instancia.getEstado().equals("0")) {
						if(chequeoReasignacion){
							if (concepto.getVistoBuenoAnalisis().equals("S")) {
								unidadAnalisis.setCodigo(concepto.getCodigoUnidadAnalisis());
								unidadAnalisis = getUnidadAnalisis(unidadAnalisis);
	
								padrino.setCodigoUnidadAnalisis(unidadAnalisis.getCodigo().intValue());
								padrino.setEstado("A");
								padrinos = new ArrayList<Padrino>(getPadrinosPorUnidadAnalisisYEstado(padrino));
	
								// Se debe balancear por carga
								if (!padrinos.isEmpty()) {
									siguienteEstado = "2";
									// codigoUsuarioAsignado = ((Padrino)padrinos.get(0)).getCodigoUsuario();
	
									try {
										codigoUsuarioAsignado = getUsuarioAsignadoPorBalanceo(siguienteEstado, unidadAnalisis.getCodigoSucursal(), cs_CODIGO_PADRINOS).getCodigo().intValue();
									} catch (Exception le_e) {
										codigoUsuarioAsignado = padrinos.get(0).getCodigoUsuario().intValue();
									}
									codigoUnidadAnalisisAsignada = 0;
								}
							} else {
								indEstadoEnteAutorizador = true;
							}
							}else
							{
								return -1;
							}
						}
	
						if (instancia.getEstado().equals("1")) {
							if(chequeoReasignacion){
							siguienteEstado = "0";
							codigoUsuarioAsignado = updateNotaContableRegistro(notaContable, temasNotaContableAux, totalesNotaContableAux, codigoUsuario);
							}else
							{
								return -1;
							}
						}
	
						if (indEstadoEnteAutorizador || instancia.getEstado().equals("2")) {
							if(chequeoReasignacion){
							if (concepto.getAutorizacionTercero().equals("S")) {
								totalesNotaContable = getTotalesPorNotaContable(notaContable.getCodigo().intValue());
	
								// El total de la Nota Contable no se esta calculando porque son diferentes Divisa. Se esta tomando el primer total
								for (NotaContableTotal notaContableTotal : totalesNotaContable) {
									if (totalNotaContable == 0 && notaContableTotal.getCodigoDivisa().equals("COP")) {
										totalNotaContable = notaContableTotal.getTotal();
									}
								}
	
								montoAutorizado.setCodigoTemaAutorizacion(concepto.getCodigoTemaAutorizacion());
								montoAutorizado.setCodigoTipoAutorizacion(notaContable.getCodigoTipoEvento());
								montoAutorizado.setEstado("A");
								montoAutorizado = getMontoAutorizadoPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizado);
	
								if (montoAutorizado.getCodigoEnteAutorizador().intValue() != 0) {
									enteAutorizador.setCodigo(montoAutorizado.getCodigoEnteAutorizador());
									enteAutorizador = getEnteAutorizador(enteAutorizador);
	
									if (enteAutorizador.getCodigoUsuarioModulo().intValue() != 0 && enteAutorizador.getEstado().equals("A")) {
										siguienteEstado = "3";
										codigoUsuarioAsignado = enteAutorizador.getCodigoUsuarioModulo().intValue();
										codigoUnidadAnalisisAsignada = 0;
									}
								}
	
								/* FLUJO ADICIONAL */
								montoAutorizadoGeneral.setCodigoTemaAutorizacion(concepto.getCodigoTemaAutorizacion());
								montoAutorizadoGeneral.setCodigoTipoAutorizacion(notaContable.getCodigoTipoEvento());
								montoAutorizadoGeneral.setEstado("A");
								montoAutorizadoGeneral = getMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizadoGeneral, totalNotaContable);
								posicionMontoAutorizado = getPosicionMontoAutorizadoGeneralPorTemaAutorizacionYTipoEventoNotaContableYEstado(montoAutorizadoGeneral, totalNotaContable);
								posicionMontosAutorizadosGenerales = objMontosAutorizadosGenerales.posicion(montoAutorizadoGeneral, totalNotaContable);
								/**COL514313I001449 MODIFICACION PARA VALIDAR LOS NIVELES DE AUTORIZACION
								 * SE CARGA EN UNA COLECCION LAS SUCURSALES AUTORIZADAS CON USUARIOS Y SE 
								 * COMPARA COMTRA LA POSICION DEL MONTO AUTORIZADO GENERAL SEGUN POSICION posicionMontoAutorizado  ****/
	
								if (montoAutorizadoGeneral.getCodigoRol().intValue() != 0) {
									Sucursal sucursal = new Sucursal();
									sucursal.setCodigo(notaContable.getCodigoSucursalOrigen());
									try {
										sucursalAutorizacion = cargaAltamiraManager.getCadenaAutorizacionSucursal(sucursal);
										indEstadoCentroSuperiorAutorizador = false;
										if (!sucursalAutorizacion.isEmpty()) {
											itSucursalAutorizacion = sucursalAutorizacion.iterator();
											itAutorizacionMonto = posicionMontosAutorizadosGenerales.iterator();
											montoAutorizadoGeneral =  (MontoAutorizadoGeneral) itAutorizacionMonto.next();
	
											for (Sucursal suc : sucursalAutorizacion) {
												sucursal = (Sucursal) itSucursalAutorizacion.next();
												usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(suc.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
												if (usuariosModulo.size() != 0) {
													NuevaSucursalAutorizacion.add(sucursal);
													if (itAutorizacionMonto.hasNext()){
														montoAutorizadoGeneral =  (MontoAutorizadoGeneral) itAutorizacionMonto.next();	
													}
												}
											}
											
											if (NuevaSucursalAutorizacion.size() == posicionMontosAutorizadosGenerales.size()){
												sucursal = NuevaSucursalAutorizacion.get(posicionMontoAutorizado);
												montoAutorizadoGeneral = (MontoAutorizadoGeneral) posicionMontosAutorizadosGenerales.get(posicionMontoAutorizado);
												usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(sucursal.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
												indEstadoCentroSuperiorAutorizador = true;
											}
												/**if (!indEstadoCentroSuperiorAutorizador) {
													usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(sucursal.getCodigo(), montoAutorizadoGeneral.getCodigoRol().intValue(), "A");
													if (!usuariosModulo.isEmpty()) {
														usuarioModulo = usuariosModulo.iterator().next();
	
														if (contadorMontoAutorizadoEncontrado == posicionMontoAutorizado) {
															indEstadoCentroSuperiorAutorizador = true;
														}
													}
												}**/
										}
	
										if (indEstadoCentroSuperiorAutorizador) {
											siguienteEstado = "3";
											usuarioModulo = usuariosModulo.iterator().next();
											codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
											codigoUnidadAnalisisAsignada = 0;
										}
									} catch (Exception le_e) {
									}
								}
							} else {
								indEstadoPrecierre = true;
							}
						}else
						{
							return -1;
						}
					}

					if (indEstadoPrecierre || instancia.getEstado().equals("3")) {
						if(chequeoReasignacion){
							usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");
	
							if (!usuariosModulo.isEmpty()) {
								usuarioModulo = usuariosModulo.iterator().next();
	
								siguienteEstado = "4";
								codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
								codigoUnidadAnalisisAsignada = 0;
							}
						}else
						{
							return -1;
						}
					}
				} else {
					if (instancia.getEstado().equals("0") || instancia.getEstado().equals("2") || instancia.getEstado().equals("3") || instancia.getEstado().equals("4") || instancia.getEstado().equals("5")) {
						actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
						actividadesRealizadas = new ArrayList<ActividadRealizada>(getActividadesRealizadasPorInstancia(actividadRealizada));

						siguienteEstado = "1";
						if (actividadesRealizadas.size() != 0) {
							codigoUsuarioAsignado = actividadesRealizadas.get(0).getCodigoUsuario().intValue();
						}
						codigoUnidadAnalisisAsignada = 0;
					}
				}
			} else if (notaContable.getTipoNota().equals("C")) {
				if (aprobada) {
					if (instancia.getEstado().equals("0")) {
						if(chequeoReasignacion){
						unidadAnalisis.setEstado("A");
						unidadAnalisis.setAutorizaReferenciaCruce("S");
						unidadAnalisis = getUnidadAnalisisPorAutorizaReferenciaCruceYEstado(unidadAnalisis);
						padrino.setCodigoUnidadAnalisis(unidadAnalisis.getCodigo().intValue());
						padrino.setEstado("A");
						padrinos = new ArrayList<Padrino>(getPadrinosPorUnidadAnalisisYEstado(padrino));

						// Se debe balancear por carga
						if (padrinos.size() != 0) {
							siguienteEstado = "2";
							// codigoUsuarioAsignado = ((Padrino)padrinos.get(0)).getCodigoUsuario();

							try {
								codigoUsuarioAsignado = getUsuarioAsignadoPorBalanceo(siguienteEstado, unidadAnalisis.getCodigoSucursal(), cs_CODIGO_PADRINOS).getCodigo().intValue();
							} catch (Exception le_e) {
								codigoUsuarioAsignado = padrinos.get(0).getCodigoUsuario().intValue();
							}
							codigoUnidadAnalisisAsignada = 0;	
						}
						}else
						{
							return -1;
						}
					}

					if (indEstadoPrecierre || instancia.getEstado().equals("2")) {
						if(chequeoReasignacion){
						usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

						if (!usuariosModulo.isEmpty()) {
							usuarioModulo = usuariosModulo.iterator().next();

							siguienteEstado = "4";
							codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
							codigoUnidadAnalisisAsignada = 0;
						}
						}else
						{
							return -1;
						}
					}

					if (instancia.getEstado().equals("1")) {
						if(chequeoReasignacion){
							siguienteEstado = "0";
							usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(notaContable.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");
	
							if (!usuariosModulo.isEmpty()) {
								usuarioModulo = usuariosModulo.iterator().next();
								codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
							}
						}else
						{
							return -1;
						}
				}

				} else {
					if (instancia.getEstado().equals("0") || instancia.getEstado().equals("2") || instancia.getEstado().equals("3") || instancia.getEstado().equals("4") || instancia.getEstado().equals("5")) {
						actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
						actividadesRealizadas = new ArrayList<ActividadRealizada>(getActividadesRealizadasPorInstancia(actividadRealizada));

						siguienteEstado = "1";
						if (actividadesRealizadas.size() != 0) {
							codigoUsuarioAsignado = actividadesRealizadas.get(0).getCodigoUsuario().intValue();
						}
						codigoUnidadAnalisisAsignada = 0;
					}
				}
			} else if (notaContable.getTipoNota().equals("L")) {
				if (aprobada) {
					if (indEstadoPrecierre || instancia.getEstado().equals("0")) {
						if(chequeoReasignacion){
						usuariosModulo = getUsuariosModuloPorRolYEstado(cs_CODIGO_ADMINISTRADOR_CONSOLIDACION, "A");

						if (!usuariosModulo.isEmpty()) {
							usuarioModulo = usuariosModulo.iterator().next();
							siguienteEstado = "4";
							codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
							codigoUnidadAnalisisAsignada = 0;
						}
						}else
						{
							return -1;
						}
					}
					if (instancia.getEstado().equals("1")) {
						if(chequeoReasignacion){
							siguienteEstado = "0";
							usuariosModulo = getUsuariosModuloPorAreaYRolYEstado(notaContable.getCodigoSucursalOrigen(), cs_CODIGO_SUBGERENTE_GERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL, "A");
	
							if (!usuariosModulo.isEmpty()) {
								usuarioModulo = usuariosModulo.iterator().next();
								codigoUsuarioAsignado = usuarioModulo.getCodigo().intValue();
							}
						}else
						{
							return -1;
						}
					}
				} else {
					if (instancia.getEstado().equals("0") || instancia.getEstado().equals("2") || instancia.getEstado().equals("3") || instancia.getEstado().equals("4") || instancia.getEstado().equals("5")) {
						actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
						actividadesRealizadas = new ArrayList<ActividadRealizada>(getActividadesRealizadasPorInstancia(actividadRealizada));

						siguienteEstado = "1";
						if (actividadesRealizadas.size() != 0) {
							codigoUsuarioAsignado = actividadesRealizadas.get(0).getCodigoUsuario().intValue();
						}
						codigoUnidadAnalisisAsignada = 0;
					}
				}
			}
		}

		Timestamp fecha = DateUtils.getTimestamp();

		// Actualiza Instancia
		instanciaActualizada = instancia;
		instanciaActualizada.setCodigoUsuarioActual(codigoUsuarioAsignado);
		instanciaActualizada.setEstado(siguienteEstado);
		instanciaActualizada.setUltimaActualizacionTs(fecha);

		updateInstancia(con, instanciaActualizada, codigoUsuario);

		// se consulta la actividad previa
		actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
		ActividadRealizada ultAcRe = actividadRealizadaDAO.getUltima(actividadRealizada);

		// se obtienen los festivos
		Collection<Festivo> festivos = cargaAltamiraManager.getFestivos();
		// se hace el calculo de duracion
		int cantFest = DateUtils.getFestivosEntre(new Date(ultAcRe.getFechaHoraCierreTs().getTime()), new Date(fecha.getTime()), new ArrayList<Festivo>(festivos));
		int duracion = (int) ((fecha.getTime() - ultAcRe.getFechaHoraCierreTs().getTime()) / 1000 / 60);
		duracion -= cantFest * 24 * 60;

		// Adiciona Actividad Realizada al historial
		ActividadRealizada actividadNueva = new ActividadRealizada();

		actividadNueva.setCodigoInstancia(instancia.getCodigo().intValue());
		actividadNueva.setCodigoUsuario(codigoUsuario);
		actividadNueva.setFechaHoraTs(ultAcRe.getFechaHoraCierreTs());
		actividadNueva.setFechaHoraCierreTs(fecha);
		actividadNueva.setDuracionActividad(duracion);
		actividadNueva.setEstado(instancia.getEstado());
		actividadNueva.setValor1(codigoCausalDevolucion != 0 ? String.valueOf(codigoCausalDevolucion) : "");
		actividadNueva.setValor2(otraCausalDev != null ? otraCausalDev : "");
		actividadNueva.setValor3("");
		addActividadRealizada(con, actividadNueva, codigoUsuario);
		return codigoUsuarioAsignado;
	}

	@Override
	public void anularNotaContable(Instancia instancia, int codigoUsuario) throws Exception {

		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);
			Collection<NotaContableCrucePartidaPendiente> temasNota = new ArrayList<NotaContableCrucePartidaPendiente>(notaContableCrucePartidaPendienteDAO.findByNotaContable(con, instancia.getCodigoNotaContable().intValue()));
			for (NotaContableCrucePartidaPendiente n : temasNota) {
				PartidaPendiente p = new PartidaPendiente();
				p.setCuenta(n.getCuentaContable());
				p.setSucursalDestino(n.getCodigoSucursalDestino());
				p.setNaturaleza(n.getNaturaleza());
				p.setDivisa(n.getDivisa());
				p.setReferenciaCruce(n.getReferenciaCruce());
				p.setImporte(n.getImporte());
				p.setConcepto(n.getConcepto());
				p.setFechaContable(n.getFechaContable());
				p.setUsada("N");
				partidaPendienteDAO.updateUsada(con, p, codigoUsuario);
			}

			Instancia instanciaActualizada = new Instancia();
			ActividadRealizada actividadRealizada = new ActividadRealizada();
			String siguienteEstado = "";

			if (instancia.getEstado().equals("0") || instancia.getEstado().equals("1") || instancia.getEstado().equals("2") || instancia.getEstado().equals("3") || instancia.getEstado().equals("4") || instancia.getEstado().equals("5")) {
				actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());

				siguienteEstado = "9";
			}
			
			
			// Actualiza Instancia
			instanciaActualizada = instancia;
			instanciaActualizada.setEstado(siguienteEstado);
			instanciaActualizada.setUltimaActualizacionTs(DateUtils.getTimestamp());

			updateInstancia(con, instanciaActualizada, codigoUsuario);


			CargaAltamiraSessionBean cargaAltamiraManager = new CargaAltamiraSessionBean();
			// se consulta la actividad previa
			ActividadRealizada actividadAnt=new ActividadRealizada();
			actividadAnt.setCodigoInstancia(instancia.getCodigo().intValue());
			actividadAnt = actividadRealizadaDAO.getUltima(actividadAnt);

			// se obtienen los festivos
			Collection<Festivo> festivos = cargaAltamiraManager.getFestivos();
			Timestamp fecha = DateUtils.getTimestamp();
			// se hace el calculo de duracion
			int cantFest = DateUtils.getFestivosEntre(new Date(actividadAnt.getFechaHoraCierreTs().getTime()), new Date(fecha.getTime()), new ArrayList<Festivo>(festivos));
			int duracion = (int) ((fecha.getTime() - actividadAnt.getFechaHoraCierreTs().getTime()) / 1000 / 60);
			duracion -= cantFest * 24 * 60;

			// Adiciona Actividad Realizada al historial
			actividadRealizada.setCodigoInstancia(instancia.getCodigo().intValue());
			actividadRealizada.setCodigoUsuario(codigoUsuario);
			actividadRealizada.setFechaHoraTs(fecha);
			actividadRealizada.setFechaHoraCierreTs(fecha);
			actividadRealizada.setDuracionActividad(duracion);
			actividadRealizada.setEstado(instancia.getEstado());
			actividadRealizada.setValor1("");
			actividadRealizada.setValor2("");
			actividadRealizada.setValor3("");

			addActividadRealizada(con, actividadRealizada, codigoUsuario);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}
	}

	/**
	 * 
	 * @param codigoUsuario
	 * @param operacion
	 * @param tipoRegistro
	 * @param codigoRegistro
	 * @return
	 * @throws Exception
	 * @{@link Deprecated} este metodo se mantiene por compatibilidad con versiones anteriores.
	 */
	@Override
	public void addRegistroAuditoriaIngreso(int codigoUsuario, String operacion, String tipoRegistro, String codigoRegistro) throws Exception {
		Connection con = null;
		try {
			con = actividadRealizadaDAO.getConexion(false);
			/** BLOQUEO BASE DE DATOS**/
			auditoriaDAO.addRegistroAuditoria(con, codigoUsuario, operacion, tipoRegistro, codigoRegistro);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			actividadRealizadaDAO.closeConnection(con);
		}
	}

	public int addRegistroAuditoria(Connection con, int codigoUsuario, String operacion, String tipoRegistro, String codigoRegistro) throws Exception {
		return auditoriaDAO.addRegistroAuditoria(con, codigoUsuario, operacion, tipoRegistro, codigoRegistro);
	}

	@Override
	public TreeMap<Menu, TreeSet<SubMenu>> getMenu(int codigo) throws RemoteException, NamingException, CreateException, Exception {
		Rol rol = new Rol();
		rol.setCodigo(codigo);
		Collection<SubMenuRol> smrs = findSubMenuRolByRol(rol);
		TreeMap<Menu, TreeSet<SubMenu>> ret = new TreeMap<Menu, TreeSet<SubMenu>>();

		for (SubMenuRol smr : smrs) {
			if (!ret.containsKey(smr.getSubMenu().getMenu())) {
				ret.put(smr.getSubMenu().getMenu(), new TreeSet<SubMenu>());
			}
			ret.get(smr.getSubMenu().getMenu()).add(smr.getSubMenu());
		}

		return ret;
	}

	@Override
	public Collection<NotaContableRegistroLibre> getRegistrosNotaContableLibre(Number codigoNotaContable) throws Exception {
		return notaContableRegistroLibreDAO.findByNotaContable(codigoNotaContable.intValue());
	}

	@Override
	public int getUltimoUsuarioActividadRealizadaPorRadicacion(String numRadicacion) throws Exception {
		return actividadRealizadaDAO.getUltimoUsuarioPorRadicacion(numRadicacion);
	}

	@Override
	public void updateNotaContable(Connection con, NotaContable nota, int codigoUsuario) throws Exception {
		notaContableDAO.update(con, nota, codigoUsuario);
	}

	@Override
	public void updateParametro(Parametro registro, int codigoUsuario) throws Exception {
		parametroDAO.update(registro, codigoUsuario);
	}

	@Override
	public void updateParametros(Collection<Parametro> registros, int codigoUsuario) throws Exception {
		for (Parametro registro : registros) {
			updateParametro(registro, codigoUsuario);
		}
	}

	@Override
	public void updateFechaNotaContableTema(NotaContableTema t, int codigoUsuario) throws Exception {
		notaContableTemaDAO.updateFecha(t, codigoUsuario);
	}

	@Override
	public void updateFechaNotaContableCruceRef(NotaContableCrucePartidaPendiente t, int codigoUsuario) throws Exception {
		notaContableCrucePartidaPendienteDAO.updateFecha(t, codigoUsuario);
	}

	@Override
	public void updateFechaNotaContableRegLibre(NotaContableRegistroLibre t, int codigoUsuario) throws Exception {
		notaContableRegistroLibreDAO.updateFecha(t, codigoUsuario);
	}

	@Override
	public ArrayList<NotaContableTotal> getDatosDeInstancias(Collection<Instancia> instancias, boolean totales) throws Exception {
		ArrayList<NotaContableTotal> totalesNotas = new ArrayList<NotaContableTotal>();
		int count = 0;

		for (Instancia ins : instancias) {
			NotaContable notaContable = ins.getNC();
			try {
				// Se mantiene para futuro soporte
				if (notaContable.getTipoNota().equals(NotaContable.NORMAL)) {
					Collection<NotaContableTema> temasNota = getTemasPorNotaContable(ins.getCodigoNotaContable().intValue());
					for (NotaContableTema t : temasNota) {
						Tema tema = new Tema();
						tema.setCodigo(t.getCodigoTema());
						t.setTema(getTema(tema));
					}
					notaContable.setTemas(new ArrayList<NotaContableTema>(temasNota));
				} else if (notaContable.getTipoNota().equals(NotaContable.CRUCE_REFERENCIA)) {
					Collection<NotaContableCrucePartidaPendiente> temasCruce = getCrucesPartidasPendientesPorNotaContable(ins.getCodigoNotaContable().intValue());
					notaContable.setTemasCruce(new ArrayList<NotaContableCrucePartidaPendiente>(temasCruce));
				} else if (notaContable.getTipoNota().equals(NotaContable.LIBRE)) {
					Collection<NotaContableRegistroLibre> temasLibre = getRegistrosLibresPorNotaContable(ins.getCodigoNotaContable().intValue());
					notaContable.setTemasLibre(new ArrayList<NotaContableRegistroLibre>(temasLibre));
				}
				if (totales) {
					getTotalesNota(totalesNotas, notaContable);
				}
			} catch (Exception le_e) {
			}
			count++;
		}

		return totalesNotas;
	}

	private ArrayList<NotaContableTotal> getTotalesNota(ArrayList<NotaContableTotal> totalesNotaAcumulado, NotaContable nota) {
		ArrayList<NotaContableTotal> totalesNota = new ArrayList<NotaContableTotal>(totalesNotaAcumulado);
		if (nota.getTipoNota().equals(NotaContable.NORMAL)) {
			for (NotaContableTema notaContableTema : nota.getTemas()) {
				totalesNota = addTotal(totalesNota, notaContableTema.getCodigoDivisa(), notaContableTema.getMonto().doubleValue());
			}
		} else if (nota.getTipoNota().equals(NotaContable.CRUCE_REFERENCIA)) {
			for (NotaContableCrucePartidaPendiente notaContableTema : nota.getTemasCruce()) {
				totalesNota = addTotal(totalesNota, notaContableTema.getDivisa(), notaContableTema.getImporte());
			}
		} else if (nota.getTipoNota().equals(NotaContable.LIBRE)) {
			for (NotaContableRegistroLibre notaContableTema : nota.getTemasLibre()) {
				totalesNota = addTotal(totalesNota, notaContableTema.getCodigoDivisa(), notaContableTema.getMonto());
			}
		}
		return totalesNota;
	}

	private ArrayList<NotaContableTotal> addTotal(ArrayList<NotaContableTotal> totalesNota, String divisa, double monto) {
		if (!divisa.equals("")) {
			ArrayList<NotaContableTotal> nuevosTotales = totalesNota;
			NotaContableTotal totalNotaContable = new NotaContableTotal();
			int count = 0;
			boolean indExisteDivisa = false;

			while (count < nuevosTotales.size() && !indExisteDivisa) {
				totalNotaContable = nuevosTotales.get(count);
				if (totalNotaContable.getCodigoDivisa().equals(divisa)) {
					indExisteDivisa = true;
				} else {
					count++;
				}
			}
			if (indExisteDivisa) {
				totalNotaContable.setTotal(totalNotaContable.getTotal() + monto);
				nuevosTotales.set(count, totalNotaContable);
			} else {
				totalNotaContable = new NotaContableTotal();
				totalNotaContable.setCodigoDivisa(divisa);
				totalNotaContable.setTotal(monto);
				nuevosTotales.add(totalNotaContable);
			}

			return nuevosTotales;
		}
		return totalesNota;
	}

	@Override
	public void sendMail(Instancia instancia, UsuarioLogueado usuarioLogueado) throws Exception {
		if (usuarioLogueado == null) {
			usuarioLogueado = new UsuarioLogueado(new UsuarioModulo());
		}
		UsuarioModulo usuarioModulo = new UsuarioModulo();
		EMailSender enviarEMail = new EMailSender();
		// se envia el correo correspondiente
		int codigoUsuarioAsignado = instancia.getCodigoUsuarioActual().intValue();
		usuarioModulo.setCodigo(codigoUsuarioAsignado);
		usuarioModulo = getUsuarioModulo(usuarioModulo);

		enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), usuarioLogueado.getUsuario().getEMailModificado(), "Mdulo Notas Contables - Registro para aprobar",
				"Por favor ingrese al mdulo de Notas Contables, se le ha asignado un registro que requiere su verificacin");
	}

	@Override
	public void processSendMail(List<Instancia> instancias, UsuarioLogueado usuarioLogueado) throws Exception {
		Map<Integer, List<Instancia>> instanciasMap = new TreeMap<Integer, List<Instancia>>();
		for (Instancia i : instancias) {
			if (!instanciasMap.containsKey(i.getCodigo().intValue())) {
				instanciasMap.put(i.getCodigo().intValue(), new ArrayList<Instancia>());
			}
			instanciasMap.get(i.getCodigo().intValue()).add(i);
		}

		for (List<Instancia> ins : instanciasMap.values()) {
			sendMail(ins, usuarioLogueado);
		}
	}

	private void sendMail(List<Instancia> instancias, UsuarioLogueado usuarioLogueado) throws Exception {
		if (usuarioLogueado == null) {
			usuarioLogueado = new UsuarioLogueado(new UsuarioModulo());
		}
		UsuarioModulo usuarioModulo = null;
		EMailSender enviarEMail = new EMailSender();
		int codigoUsuarioAsignado;
		String numerosRadicacion = "";
		for (Instancia instancia : instancias) {
			codigoUsuarioAsignado = instancia.getCodigoUsuarioActual().intValue();
			if (usuarioModulo == null) {
				usuarioModulo = new UsuarioModulo();
				usuarioModulo.setCodigo(codigoUsuarioAsignado);
				usuarioModulo = getUsuarioModulo(usuarioModulo);
			} else {
				if (usuarioModulo.getCodigo().intValue() != codigoUsuarioAsignado) {
					throw new Exception("El usuario no coincide en las notas contables");
				}
			}
			NotaContable notaContable = new NotaContable();
			notaContable.setCodigo(instancia.getCodigoNotaContable());
			numerosRadicacion += getNotaContable(notaContable).getNumeroRadicacion() + ", ";
		}
		if (usuarioModulo != null) {
			if (!numerosRadicacion.isEmpty()) {
				numerosRadicacion = numerosRadicacion.substring(0, numerosRadicacion.length() - 2);
			}
			enviarEMail.sendEmail(usuarioModulo.getEMailModificado(), usuarioLogueado.getUsuario().getEMailModificado(), "Mdulo Notas Contables - Registro para revisar", "Por favor ingrese al mdulo de Notas Contables, las notas "
					+ numerosRadicacion + " estn pendientes para revisin");
			//System.out.println("USUARIO " + usuarioModulo.getCodigoEmpleado() + "\nPor favor ingrese al mdulo de Notas Contables, las notas " + numerosRadicacion + " estn pendientes para revisin");
		}
	}

	@Override
	public Collection<UsuarioModulo> getTiemposPorUsuario(Timestamp desde) throws Exception {
		return usuarioModuloDAO.findPorTiempos(desde);
	}

	@Override
	public void addRechazoSalida(RechazoSalida salida, int codigoUsuario) throws Exception {
		String numRadicacion = salida.getNumNota();
		// se envia la nota asociada a la siguiente actividad
		NotaContable nota = new NotaContable();
		nota.setNumeroRadicacion(numRadicacion);
		nota = getNotaContable(nota);
		// se le asigna el consecutivo real dependiendo del numero de radicacion
		salida.setConsecutivo(nota.getCodigo());
		rechazoSalidaDAO.add(salida, codigoUsuario);
	}

	@Override
	public Map<?, String> findByProducto(String codProducto) throws Exception {
		return subProductoDAO.findByProducto(codProducto);
	}

	@Override
	public Map<?, String> findByCanal(String codCanal) throws Exception {
		return subCanalDAO.findByCanal(codCanal);
	}

	@Override
	public Map<?, String> findByClaseN2(String codClase) throws Exception {
		return claseRiesgoN2DAO.findByClaseRiesgoN2(codClase);
	}

	@Override
	public Map<?, String> findByClaseN3(String codClase, String codClaseN1) throws Exception {
		return claseRiesgoN3DAO.findByClaseRiesgoN3(codClase,codClaseN1);
	}

	@Override
	public Collection<String> getInformacionSiro() throws Exception {
		return notaContableDAO.getDataForFile();
	}

	public Collection<String> getDataConciliation(String fechaDesde , String fechaHasta) throws Exception {
		return notaContableDAO.getDataConciliationFile(fechaDesde , fechaHasta);
	}

	
}