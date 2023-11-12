package co.com.bbva.app.notas.contables.carga.dto;

public interface ISalida {

//	String getConsecutivo();
//
//	void setConsecutivo(String consecutivo);

	String getNumNota();

	void setNumNota(String numNota);

	void produceFromString(String str) throws Exception;

	// public String getFecha();
	//
	// public void setFecha(String fecha);

}
