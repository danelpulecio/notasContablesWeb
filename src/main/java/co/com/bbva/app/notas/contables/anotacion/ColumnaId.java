package co.com.bbva.app.notas.contables.anotacion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnaId {

	String nombreSecuencia() default "";
}
