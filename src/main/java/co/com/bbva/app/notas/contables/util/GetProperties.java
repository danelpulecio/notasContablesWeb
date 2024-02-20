package co.com.bbva.app.notas.contables.util;

import co.com.bbva.app.notas.contables.jsf.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);

    public java.util.Properties readProperties() {
        java.util.Properties propiedades = new java.util.Properties();
        InputStream entrada = null;

        try {
            entrada = new FileInputStream("/app/properties/config.properties");
            propiedades.load(entrada);

        } catch (IOException e) {
            LOGGER.error("Error al cargar el archivo: " );
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException e) {
                    LOGGER.error("Error al cargar el archivo: " );
                }
            }
        }
        return propiedades;

    }
}
