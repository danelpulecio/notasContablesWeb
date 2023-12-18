package co.com.bbva.app.notas.contables.config;

import co.com.bbva.app.notas.contables.jsf.consultas.ReporteGeneralPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        try (InputStream input = context.getResourceAsStream("/WEB-INF/classes/config.properties")) {
            LOGGER.info("input context {}", context.getResourceAsStream("/"));
            if (input != null) {
                LOGGER.info("CARGA DE ARCHIVO");
                Properties properties = new Properties();
                properties.load(input);

                String activarLdap = properties.getProperty("activar.ldap");
                LOGGER.info("activarLdap {}", activarLdap);
                context.setInitParameter("ACTIVAR_LDAP", activarLdap);
                LOGGER.info("context {}", context.toString());
            }else
                LOGGER.info("NO PUDO CARGAR EL ARCHIVO");
        } catch (Exception e) {
            e.printStackTrace(); // Manejo adecuado de errores en un entorno de producción
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Aquí puedes realizar tareas de limpieza si es necesario
    }
}
