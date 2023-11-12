package co.com.bbva.app.notas.contables.facade;

import co.com.bbva.app.notas.contables.carga.dto.*;
import co.com.bbva.app.notas.contables.dto.Concepto;
import co.com.bbva.app.notas.contables.dto.JerarquiaOperacionSFC_N2;
import co.com.bbva.app.notas.contables.dto.JerarquiaOperacionSFC_N3;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Remote interface for Enterprise Bean: CargaAltamiraSession
 */
public interface CargaAltamiraSession {

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateRegistroCarga(RegistroCarga row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteRegistroCarga(RegistroCarga row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public RegistroCarga getRegistroCarga(RegistroCarga row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<RegistroCarga> getRegistrosCarga() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateAuditoriaCarga(AuditoriaCarga row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteAuditoriaCarga(AuditoriaCarga row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public AuditoriaCarga getAuditoriaCarga(AuditoriaCarga row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<AuditoriaCarga> getAuditoriasCarga() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addClaseRiesgo(ClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateClaseRiesgo(ClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteClaseRiesgo(ClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public ClaseRiesgo getClaseRiesgo(ClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveClaseRiesgo(ClaseRiesgo row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<ClaseRiesgo> getClasesRiesgo() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addCliente(Cliente row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateCliente(Cliente row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteCliente(Cliente row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Cliente getCliente(Cliente row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveCliente(Cliente row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Cliente> getClientes() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addContrato(Contrato row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteContrato(Contrato row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Contrato getContrato(Contrato row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveContrato(Contrato row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Contrato> getContratos() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addPerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updatePerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deletePerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public PerdidaOperacionalClaseRiesgo getPerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void savePerdidaOperacionalClaseRiesgo(PerdidaOperacionalClaseRiesgo row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PerdidaOperacionalClaseRiesgo> getPerdidaOperacionalClaseRiesgo() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addPartidaPendiente(PartidaPendiente row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updatePartidaPendiente(PartidaPendiente row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deletePartidaPendiente(PartidaPendiente row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public PartidaPendiente getPartidaPendiente(PartidaPendiente row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void savePartidaPendiente(PartidaPendiente row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PartidaPendiente> getPartidasPendientes() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addPerdidaOperacional(PerdidaOperacional row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updatePerdidaOperacional(PerdidaOperacional row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deletePerdidaOperacional(PerdidaOperacional row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public PerdidaOperacional getPerdidaOperacional(PerdidaOperacional row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void savePerdidaOperacional(PerdidaOperacional row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PerdidaOperacional> getPerdidasOperacionales() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addUsuarioAltamira(UsuarioAltamira row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateUsuarioAltamira(UsuarioAltamira row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteUsuarioAltamira(UsuarioAltamira row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public UsuarioAltamira getUsuarioAltamira(UsuarioAltamira row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveUsuarioAltamira(UsuarioAltamira row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<UsuarioAltamira> getUsuariosAltamira() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addHADTAPL(HADTAPL row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateHADTAPL(HADTAPL row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteHADTAPL(HADTAPL row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public HADTAPL getHADTAPL(HADTAPL row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveHADTAPL(HADTAPL row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<HADTAPL> getHADTAPL() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addProducto(Producto row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateProducto(Producto row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteProducto(Producto row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Producto getProducto(Producto row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveProducto(Producto row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Producto> getProductos() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addSucursal(Sucursal row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateSucursal(Sucursal row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteSucursal(Sucursal row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Sucursal getSucursal(Sucursal row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveSucursal(Sucursal row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Sucursal> getSucursales() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addPUC(PUC row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updatePUC(PUC row, int codigoUsuario) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deletePUC(PUC row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public PUC getPUC(PUC row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void savePUC(PUC row, int codigoUsuario) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PUC> getPUCs() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addDivisa(Divisa row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateDivisa(Divisa row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteDivisa(Divisa row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Divisa getDivisa(Divisa row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveDivisa(Divisa row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Divisa> getDivisas() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateTercero(Tercero row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteTercero(Tercero row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Tercero getTercero(Tercero row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveTercero(Tercero row, int codigoUsuario) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Tercero> getTerceros() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addTipoIdentificacion(TipoIdentificacion row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateTipoIdentificacion(TipoIdentificacion row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteTipoIdentificacion(TipoIdentificacion row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public TipoIdentificacion getTipoIdentificacion(TipoIdentificacion row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveTipoIdentificacion(TipoIdentificacion row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<TipoIdentificacion> getTiposIdentificacion() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addTipoTelefono(TipoTelefono row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateTipoTelefono(TipoTelefono row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteTipoTelefono(TipoTelefono row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public TipoTelefono getTipoTelefono(TipoTelefono row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveTipoTelefono(TipoTelefono row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<TipoTelefono> getTiposTelefonos() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addTipoIndicativo(TipoIndicativo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateTipoIndicativo(TipoIndicativo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteTipoIndicativo(TipoIndicativo row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public TipoIndicativo getTipoIndicativo(TipoIndicativo row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveTipoIndicativo(TipoIndicativo row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<TipoIndicativo> getTiposIndicativos() throws Exception;

	public Collection<Pais> getPaises() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public RiesgoOperacionalProducto getRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveRiesgoOperacionalProducto(RiesgoOperacionalProducto row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<RiesgoOperacionalProducto> getRiesgosOperacionalesProducto() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public RiesgoOperacionalProceso getRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveRiesgoOperacionalProceso(RiesgoOperacionalProceso row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<RiesgoOperacionalProceso> getRiesgosOperacionalesProcesos() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public RiesgoOperacionalLineaOperativa getRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveRiesgoOperacionalLineaOperativa(RiesgoOperacionalLineaOperativa row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<RiesgoOperacionalLineaOperativa> getRiesgosOperacionalesLineasOperativas() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addMunicipio(Municipio row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateMunicipio(Municipio row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteMunicipio(Municipio row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Municipio getMunicipio(Municipio row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveMunicipio(Municipio row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Municipio> getMunicipios() throws Exception;

	public Collection<Municipio> getMunicipiosPorDepartamento(String codigoDepto) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addErrorValidacion(ErrorValidacion row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateErrorValidacion(ErrorValidacion row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteErrorValidacion(ErrorValidacion row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public ErrorValidacion getErrorValidacion(ErrorValidacion row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveErrorValidacion(ErrorValidacion row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<ErrorValidacion> getErroresValidacion() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addDepartamento(Departamento row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateDepartamento(Departamento row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteDepartamento(Departamento row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Departamento getDepartamento(Departamento row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveDepartamento(Departamento row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Departamento> getDepartamentos() throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addActividadEconomica(ActividadEconomica row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updateActividadEconomica(ActividadEconomica row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteActividadEconomica(ActividadEconomica row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public ActividadEconomica getActividadEconomica(ActividadEconomica row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void saveActividadEconomica(ActividadEconomica row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<ActividadEconomica> getActividadesEconomicas() throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadClaseRiesgo() throws Exception;

	public void beginLoad(String nombreTabla, boolean deleteAll) throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadClaseRiesgo(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadCliente() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadCliente(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadContrato() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadContrato(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadPerdidaOperacionalClaseRiesgo() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadPerdidaOperacionalClaseRiesgo(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadPartidaPendiente() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadPartidaPendiente(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadPerdidaOperacional() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadPerdidaOperacional(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadUsuarioAltamira() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadUsuarioAltamira(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadHADTAPL() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadHADTAPL(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadProducto() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadProducto(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadSucursal() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadSucursal(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadPUC() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadPUC(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadDivisa() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadDivisa(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadTercero() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadTercero(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadTipoIdentificacion() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadTipoIdentificacion(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadTipoTelefono() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadTipoTelefono(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadTipoIndicativo() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadTipoIndicativo(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadRiesgoOperacionalProducto() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadRiesgoOperacionalProducto(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadRiesgoOperacionalProceso() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadRiesgoOperacionalProceso(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadRiesgoOperacionalLineaOperativa() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadRiesgoOperacionalLineaOperativa(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadMunicipio() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadMunicipio(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadErrorValidacion() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadErrorValidacion(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadDepartamento() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadDepartamento(String nombreArchivo) throws Exception;

	/**
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void beginLoadActividadEconomica() throws Exception;

	/**
	 * @param nombreArchivo
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void endLoadActividadEconomica(String nombreArchivo) throws Exception;

	/**
	 * @param as_nombreTabla
	 * @param ls_linea
	 * @param estructurasCarga
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addRow(String as_nombreTabla, String ls_linea, EstructuraCarga[] estructurasCarga) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addRegistroCarga(RegistroCarga row) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<ActividadEconomica> searchActividadEconomica(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<ClaseRiesgo> searchClaseRiesgo(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Cliente> searchCliente(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Contrato> searchContrato(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Departamento> searchDepartamento(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Divisa> searchDivisa(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<ErrorValidacion> searchErrorValidacion(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<HADTAPL> searchHADTAPL(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Municipio> searchMunicipio(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PartidaPendiente> searchPartidaPendiente(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PerdidaOperacional> searchPerdidaOperacional(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PerdidaOperacionalClaseRiesgo> searchPerdidaOperacionalClaseRiesgo(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Producto> searchProducto(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PUC> searchPUC(String palabraClave) throws Exception;

	/**
	 * @param numCuenta
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PUC> findCentrosBy(String numCuenta) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<RiesgoOperacionalLineaOperativa> searchRiesgoOperacionalLineaOperativa(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<RiesgoOperacionalProceso> searchRiesgoOperacionalProceso(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<RiesgoOperacionalProducto> searchRiesgoOperacionalProducto(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Sucursal> searchSucursal(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Tercero> searchTercero(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<TipoIdentificacion> searchTipoIdentificacion(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<TipoIndicativo> searchTipoIndicativo(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<TipoTelefono> searchTipoTelefono(String palabraClave) throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<UsuarioAltamira> searchUsuarioAltamira(String palabraClave) throws Exception;

	/**
	 * @param pucs
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void updatePUCs(Collection<PUC> pucs, int codigoUsuario) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Perfil> getPerfiles() throws Exception;

	/**
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Perfil> searchPerfiles(String palabraClave) throws Exception;

	/**
	 * @param linea
	 * @return
	 * @throws RemoteException
	 */
	public int getNumeroDatos(String linea) throws RemoteException;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Cliente getClientePorTipoYNumeroIdentificacion(Cliente row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Tercero getTerceroPorTipoYNumeroIdentificacion(Tercero row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Contrato> getContratosPorCliente(Contrato row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addCierreMensual(CierreMensual row) throws Exception;
	
	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addOperacionSFCN2(JerarquiaOperacionSFC_N2 row) throws Exception;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addOperacionSFCN3(JerarquiaOperacionSFC_N3 row) throws Exception;
	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void deleteCierreMensual(CierreMensual row) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public CierreMensual getCierreMensual(CierreMensual row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<CierreMensual> getCierresMensuales() throws Exception;

	/**
	 * @param tiposCentro
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Sucursal> getSucursalesPorTiposCentro(String tiposCentro) throws Exception;

	/**
	 * @param concepto
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws CreateException
	 * @throws RemoteException
	 */
	public Collection<Sucursal> getSucursalesPorConcepto(Concepto concepto) throws Exception, RemoteException, NamingException, CreateException, RemoteException;

	/**
	 * @param row
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void addFestivo(Festivo row) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Festivo> getFestivos() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Date> getFestivosFecha() throws Exception;

	/**
	 * @param codigoSucursal
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PartidaPendiente> getPartidasPendientesPorSucursal(String codigoSucursal) throws Exception;

	/**
	 * @param codigoSucursal
	 * @param palabraClave
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PartidaPendiente> searchPartidaPendientePorSucursal(String codigoSucursal, String palabraClave) throws Exception;

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<Sucursal> getCadenaAutorizacionSucursal(Sucursal row) throws Exception;

	/**
	 * @param codigoSucursal
	 * @return
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection<PartidaPendiente> getPartidasPendientesCuentasPorSucursal(String codigoSucursal) throws Exception;

	/**
	 * @param sucursal
	 * @param cuenta
	 * @return
	 * @throws RemoteException
	 */
	public boolean isSucursalValidaPUCOrigen(Sucursal sucursal, PUC cuenta) throws RemoteException;

	/**
	 * @param sucursal
	 * @param cuenta
	 * @return
	 * @throws RemoteException
	 */
	public boolean isSucursalValidaPUCDestino(Sucursal sucursal, PUC cuenta) throws RemoteException;

	public Map<String, String> getCVSucursal() throws Exception, RemoteException;

	public Map<String, String> getCVUsuarioAltamira(Number codigoSucursal, int codPadrino, String estado) throws Exception, RemoteException;

	public Map<?, String> getCVClasRiesPorCuenta(String partidaContable) throws Exception;

	public Map<?, String> getCVPerdidaOper(String partidaContable, String codigoClaseRiesgo) throws Exception;

	public HADTAPL getHADTAPLPorCuenta(HADTAPL h) throws Exception;

	public Collection<String> getPUCArchAltamira() throws Exception;

	public Collection<String> getTercerosArch() throws Exception;

	public void addTercero(Tercero row, int codigoUsuario) throws Exception;

	public RegistroCarga getRegistroCargaPorNombreArchivo(String nombreArchivo) throws Exception;

	public Collection<PUC> searchPUCPorCuenta(String cuenta) throws Exception;

}
