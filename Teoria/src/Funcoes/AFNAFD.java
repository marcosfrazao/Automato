package Funcoes;

import java.util.ArrayList;

public class AFNAFD {
	private Automato afn;
	private ArrayList<ArrayList<String>> estadosCompostos;
	
	public Automato toAFD(){
		if(verificaAFN()){
			criaEstadosCompostos();
			afn.removerEstadosNaoAcessiveis();
			return afn;
		}else{
			System.out.println("Nao e AFN");
			return null;
		}
		
		
		
	}
	
	public boolean verificaAFN(){
		//percorre as transições verificando se existe transição com mesma origem e mesmo simbolo e destinos diferentes 
		for(int i=0; i < afn.getTransicoes().size(); i++){
			for(int j=0; j < afn.getTransicoes().size(); j++){
				if(afn.getTransicoes().get(i).getEstadoOrigem().equals(afn.getTransicoes().get(j).getEstadoOrigem())){
					if(afn.getTransicoes().get(i).getSimbolo().equals(afn.getTransicoes().get(j).getSimbolo())){
						if(!afn.getTransicoes().get(i).getEstadoDestino().equals(afn.getTransicoes().get(j).getEstadoDestino())){
							return true;//Não é AFN
						}
					}
				}
			}
		}
		return false;
	}
	
	public void criaEstadosCompostos(){
		ArrayList<ArrayList<String>> novosEstados = new ArrayList<ArrayList<String>>();
//		ArrayList<String> novosDestinos = new ArrayList<String>();
		int count=0;
		String novoEstado ="";
		String estadoComponente ="";
		String origem = "";
			//cria os estados compostos
				for(int i=0; i < afn.getEstados().size(); i++){
					origem = afn.getEstados().get(i);
					for(int j=0; j < afn.getSimbolos().size(); j++){	
						//busca os destinos de todos os estados componentes
						for(int h=origem.length(); h > 0; h = h-2){
							estadoComponente = origem.substring((h-2), h);
							novosEstados.add(afn.funcaoProgramaAFN(estadoComponente, afn.getSimbolos().get(j)));
						}
						novoEstado = unificaEstados(novosEstados);
						if(!afn.getEstados().contains(novoEstado)&& novoEstado!=null){
							verificaInicial(novoEstado);
							verificaFinal(novoEstado);
							afn.getEstados().add(novoEstado);
						}
						if(novoEstado != "" & estadoComponente != ""){
							if(!possuiTransicao(origem, afn.getSimbolos().get(j), novoEstado)){
								afn.setTransicao(origem, afn.getSimbolos().get(j), novoEstado);
							}
						}
						limpaTransicoes(origem, afn.getSimbolos().get(j), novoEstado);
						novoEstado = "";
					}
				}
	}
	
	public String unificaEstados(ArrayList<ArrayList<String>> novosEstados){
		String junta="";
		for (int y = 0; y < novosEstados.size(); y++) {
			for(int t=0; t < novosEstados.get(y).size(); t++){
				if(!junta.contains(novosEstados.get(y).get(t))){
					junta += novosEstados.get(y).get(t);
				}
			}
		}
		novosEstados.clear();
		return junta;
	}
	
	public boolean possuiTransicao(String origem,String simbolo,String destino){
		for (int i = 0; i < afn.getTransicoes().size(); i++) {
			if(afn.getTransicoes().get(i).getEstadoOrigem().equals(origem)){
				if(afn.getTransicoes().get(i).getSimbolo().equals(simbolo)){
					if(afn.getTransicoes().get(i).getEstadoDestino().equals(destino)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void limpaTransicoes(String origem,String simbolo,String destino){
		String estadoComponente = "";
		if(destino.length() > 2){
			for(int h=destino.length(); h > 0; h = h-2){
				estadoComponente = destino.substring((h-2), h);	
				for (int i = 0; i < afn.getTransicoes().size(); i++) {
					if(afn.getTransicoes().get(i).getEstadoOrigem().equals(origem)){
						if(afn.getTransicoes().get(i).getSimbolo().equals(simbolo)){
							if(afn.getTransicoes().get(i).getEstadoDestino().equals(estadoComponente)){
									afn.getTransicoes().remove(afn.getTransicoes().get(i));
							}
						}
					}
				}
			}
		}
	}
	
	public void verificaInicial(String novoEstado){
		String estadoComponente = "";
		for(int h=novoEstado.length(); h > 0; h = h-2){
			estadoComponente = novoEstado.substring((h-2), h);
			if(afn.getEstadoInicial().equalsIgnoreCase(estadoComponente)){
				afn.setEstadoInicial(novoEstado);
				break;
			}
		}
	}
	
	public void verificaFinal(String novoEstado){
		String estadoComponente = "";
		if(contemFinal(novoEstado)){
			for(int h=novoEstado.length(); h > 0; h = h-2){
				estadoComponente = novoEstado.substring((h-2), h);
				for (int j = 0; j < afn.getEstadosFinais().size(); j++) {
					if(afn.getEstadosFinais().get(j).equalsIgnoreCase(estadoComponente)){
						afn.getEstadosFinais().remove(j);
					}
				}
			}
			afn.getEstadosFinais().add(novoEstado);
		}
	}
	
	public boolean contemFinal(String novoEstado){
		String estadoComponente = "";
		for(int h=novoEstado.length(); h > 0; h = h-2){
			estadoComponente = novoEstado.substring((h-2), h);
			for (int j = 0; j < afn.getEstadosFinais().size(); j++) {
				if(afn.getEstadosFinais().get(j).equalsIgnoreCase(estadoComponente)){
					return true;
				}
			}
		}
		return false;
	}
	
	public Automato getAfn() {
		return afn;
	}

	public void setAfn(Automato afn) {
		this.afn = afn;
	}

	public ArrayList<ArrayList<String>> getEstadosCompostos() {
		return estadosCompostos;
	}

	public void setEstadosCompostos(ArrayList<ArrayList<String>> estadosCompostos) {
		this.estadosCompostos = estadosCompostos;
	}

	public AFNAFD(Automato at){
		afn = new Automato(at);
		estadosCompostos = new ArrayList<ArrayList<String>>();
	}
	
}
