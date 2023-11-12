package co.com.bbva.app.notas.contables.deletefilesiro;

import java.io.File;

/**
 *
 * @author pjimenez
* @version 1.0, 21/10/2021
* @since JDK1.8
 *
 */
public class DeleteTransmisionSiro {

	public void deleteFile(File file) {
		file.delete();
	}
}
