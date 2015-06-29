package Funcoes;

import java.util.ArrayList;

public class ParDaTabela {
	private String estado1;
	private String estado2;
	private boolean marcado = false;
	public ArrayList<String> listaPendurados = new ArrayList<String>();
	
	public ParDaTabela(String estado1, String estado2) {
		super();
		this.estado1 = estado1;
		this.estado2 = estado2;
	}
	public String getEstado1() {
		return estado1;
	}
	public void setEstado1(String estado1) {
		this.estado1 = estado1;
	}
	public String getEstado2() {
		return estado2;
	}
	public void setEstado2(String estado2) {
		this.estado2 = estado2;
	}
	public boolean isMarcado() {
		return marcado;
	}
	public void setMarcado(boolean marcado) {
		this.marcado = marcado;
	}
}
