package co.com.bbva.app.notas.contables.anotacion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Columna {

	String nombreDB();

	String nombreApp();

	boolean esClave() default false;

	boolean esValor() default false;

	boolean esEstado() default false;
}
