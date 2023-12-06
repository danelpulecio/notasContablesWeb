package co.com.bbva.app.notas.contables.dto;

import java.util.ArrayList;

public class Menu implements java.io.Serializable, Comparable<Menu> {

	private static final long serialVersionUID = -4411745472093058822L;

	private int codigo = 0;
	private int ordenVisual = 0;
	private String nombre = "";

	private ArrayList<SubMenu> opciones;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getOrdenVisual() {
		return ordenVisual;
	}

	public void setOrdenVisual(int ordenVisual) {
		this.ordenVisual = ordenVisual;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int compareTo(Menu o) {
		return getOrdenVisual() - o.getOrdenVisual();
	}

	public ArrayList<SubMenu> getOpciones() {
		return opciones;
	}

	public void setOpciones(ArrayList<SubMenu> opciones) {
		this.opciones = opciones;
	}

	@Override
	public String toString() {
		return "Menu{" +
				"codigo=" + codigo +
				", ordenVisual=" + ordenVisual +
				", nombre='" + nombre + '\'' +
				", opciones=" + opciones +
				'}';
	}
}
