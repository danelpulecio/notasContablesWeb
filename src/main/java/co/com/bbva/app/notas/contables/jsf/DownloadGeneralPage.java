package co.com.bbva.app.notas.contables.jsf;

import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

@SessionScoped
@Named
public class DownloadGeneralPage extends BasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadGeneralPage.class);
	static final int SOPORTE = 1;
	static final int RECEPCION_ALTAMIRA = 2;
	static final int TRANSMISION_ALTAMIRA = 3;
	static final int REPORTE_EXCEL = 4;
	static final int CARGA = 5;

	private String file;
	private Integer type;
	private Session session = getContablesSessionBean().getSessionTrace();

	public DownloadGeneralPage() {
		super();
	}

	public String download() throws IOException {
		LOGGER.info("{} Intentando realizar la descarga", session.getTraceLog());

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		response.reset(); // Limpiar cualquier configuracin anterior de la respuesta.

		// Establecer encabezados de respuesta para la descarga.
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + file);

		File fileToDownload = new File(getPath() + file);

		try (FileInputStream fis = new FileInputStream(fileToDownload);
			 OutputStream os = response.getOutputStream()) {

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// Manejar errores aqu
			LOGGER.error("Error al descargar el archivo: " + e.getMessage(), e);
		}

		facesContext.responseComplete();
		return null;
	}

//	public String download() {
//		LOGGER.info("{} Intentando realizar la descarga", session.getTraceLog());
//		HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
//		writeOutContent(response, new File(getPath() + file), file);
//		getFacesContext().responseComplete();
//		return null;
//	}

	void writeOutContent(final HttpServletResponse res, final File content, final String theFilename) {
		if (content == null) {
			return;
		}
		try {
			LOGGER.info("{} Descargando el archivo: {}", session.getTraceLog(), theFilename);
			res.setHeader("Pragma", "no-cache");
			res.setDateHeader("Expires", 0);
			res.setContentType("application/force-download");
			res.setHeader("Content-disposition", "attachment; filename=" + theFilename);
			fastChannelCopy(Channels.newChannel(new FileInputStream(content)), Channels.newChannel(res.getOutputStream()));
		} catch (final IOException e) {
			LOGGER.info("{} Error al descargar el archivo: {}", session.getTraceLog(), theFilename, e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema descargando el archivo: " + getPath() + theFilename);
		}
	}

	void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16384);// 16*1024
		while (src.read(buffer) != -1) {
			((Buffer)buffer).flip();
			dest.write(buffer);
			buffer.compact();
			((Buffer)buffer).clear();
		}
		((Buffer)buffer).flip();
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
		((Buffer)buffer).clear();
	}

	private String getPath() {

		switch (type) {
		case SOPORTE:
			return DIR_SOPORTES;
		case REPORTE_EXCEL:
			return DIR_REPORTES_EXCEL;
		case RECEPCION_ALTAMIRA:
			return DIR_RECEPCION_ALTAMIRA;
		case TRANSMISION_ALTAMIRA:
			return DIR_TRANSMISION_ALTAMIRA;
		case CARGA:
			return DIR_CARGA;
		}
		return "";
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getSOPORTE() {
		return SOPORTE;
	}

	public int getRECEPCION_ALTAMIRA() {
		return RECEPCION_ALTAMIRA;
	}

	public int getTRANSMISION_ALTAMIRA() {
		return TRANSMISION_ALTAMIRA;
	}

	public int getREPORTE_EXCEL() {
		return REPORTE_EXCEL;
	}

	public int getCARGA() {
		return CARGA;
	}

}
