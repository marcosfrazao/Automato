package Funcoes;

public class UniaoDeEstado extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String detalhes  = "Tentativa de unir Estado final com Estado nao-final";
	
	@Override
	public String toString() {
		return detalhes;
	}
	
}
