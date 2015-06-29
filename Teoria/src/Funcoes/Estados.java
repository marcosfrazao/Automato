package Funcoes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import Funcoes.Automato;
import Funcoes.TransicaoAutomato;


public class Estados {
	private HashMap<String, ParDaTabela> map = new HashMap<String, ParDaTabela>();
//	public ArrayList<ParDaTabela> tabela = new ArrayList<ParDaTabela>();
	
	public Estados(ArrayList<String> estados){
		int n = estados.size();
		for(int i=0; i<n-1; i++){
			for(int j=i+1; j<n; j++){
//		
				map.put(estados.get(i)+"."+estados.get(j), new ParDaTabela(estados.get(i),estados.get(j)));
			}
		}
	}
	
	public void marcarFinalNaoFinal(ArrayList<String> estadosFinais){
		Set<String> chaves = map.keySet();
		for(String ch : chaves){
			if(estadosFinais.contains(ch.split("\\.")[0]) ^ estadosFinais.contains(ch.split("\\.")[1])){
				ParDaTabela par = map.get(ch);
				par.setMarcado(true);
				map.put(ch, par);
			}
		}
	}
	
	public void analiseNaoMarcados(Automato a){
		Set<String> chaves = map.keySet();
		for(String ch : chaves){
			if(map.get(ch).isMarcado() == false){
				int ns = a.getSimbolos().size();
				ArrayList<String> destino1;
				ArrayList<String> destino2;
				ArrayList<String> destinosSalvos = new ArrayList<String>();
				for(int s=0; s<ns; s++){
					destino1 = a.funcaoProgramaAFN(ch.split("\\.")[0], a.getSimbolos().get(s));
					destino2 = a.funcaoProgramaAFN(ch.split("\\.")[1], a.getSimbolos().get(s));
					//verifica se ja sao marcados, sendo marcados, marca o atual
					if(destino1.size()>1 || destino2.size()>1) System.out.println("Analise n�o marcados: Retornou mais de 1 destino p/ ch: "+"ch - "+destino1.toString()+"  <>  "+destino2.toString());
					if(verificaSeMarcados(destino1.get(0), destino2.get(0))){
						marcarRecursivamente(ch);
						break;//quebra o for dos simbolos
					}else{
						if( ! destino1.get(0).equals(destino2.get(0))){
							destinosSalvos.add(destino1.get(0)+"."+destino2.get(0));// tem repetido, ver depois
							destinosSalvos.add(destino2.get(0)+"."+destino1.get(0));
						}
					}
				}
				// Pendura o 'cara atual' nas listas
				int ndes = destinosSalvos.size();
				for(int d=0; d<ndes; d++){
					if(map.containsKey(destinosSalvos.get(d))){
						ParDaTabela par = map.get(destinosSalvos.get(d));
						par.listaPendurados.add(ch);
						map.put(destinosSalvos.get(d), par);
					}
				}
			}
		}
	}
	
	private boolean verificaSeMarcados(String a, String b){
		if( ! a.equals(b)){
			if(map.containsKey(a+"."+b)){
				return map.get(a+"."+b).isMarcado();
			}
			if(map.containsKey(b+"."+a)){
				return map.get(b+"."+a).isMarcado();
			}
		}
		return false;
	}
	
	private void marcarRecursivamente(String ch){
		// marca ele mesmo
		ParDaTabela par = map.get(ch);
		if(par.isMarcado() == false){
			par.setMarcado(true);
		}else{return;}
		map.put(ch, par);
		// marca os da lista que ele encabeca
		int n = par.listaPendurados.size();
		for(int i=0; i<n; i++){
			if(map.containsKey(par.listaPendurados.get(i))){
				marcarRecursivamente(par.listaPendurados.get(i));
			}
		}
	}
	
	public void unirEstados(Automato a){
		Set<String> chaves = map.keySet();
		for(String ch : chaves){
			if(map.get(ch).isMarcado() == false){
				System.out.println("Unir estados: "+ch);
				String estado1 = ch.split("\\.")[0], estado2 = ch.split("\\.")[1];
				String novoEstado = estado1+"-"+estado2;
				a.adicionarEstado(novoEstado);
				if(a.getEstadosFinais().contains(estado1)){
					a.adicionarEstadoFinal(novoEstado);
				}
				if(a.getEstadoInicial().equals(estado1) || a.getEstadoInicial().equals(estado2)){// um � estado inicial ent�o o novo ser� inicial
					a.setEstadoInicial(novoEstado);
				}
				ArrayList<String> simbolos = a.getSimbolos();
				int ns = simbolos.size();
				ArrayList<String> destino1;
				ArrayList<String> destino2;
				for(int i=0; i<ns; i++){// Faz para todos os terminais
					destino1 = a.funcaoProgramaAFN(estado1, simbolos.get(i));
					destino2 = a.funcaoProgramaAFN(estado2, simbolos.get(i));
					// a mesma transi��o que sai vai ser a mesma que sai no novo estado
					for(int w=0; w<destino1.size(); w++){
						a.setTransicao(novoEstado, simbolos.get(i), destino1.get(w));
					}
					for(int w=0; w<destino2.size(); w++){
						a.setTransicao(novoEstado, simbolos.get(i), destino2.get(w));
					}
					// descobrindo toda as trasi��es que chegam nos estados unidos
					ArrayList<String> estados = a.getEstados();
					int ne = estados.size();
					for(int k=0; k<ne; k++){
						ArrayList<String> valor = a.funcaoProgramaAFN(estados.get(k), simbolos.get(i));
						if(valor.size()>0 && ( estado1.equals(valor.get(0)) || estado2.equals(valor.get(0)) ) ){
							a.setTransicao(estados.get(k), simbolos.get(i), novoEstado);
						}
					}
				}
			}
		}
		
		
		// remover estados que foram unidos
		for(String ch : chaves){
			if(map.get(ch).isMarcado() == false){
				System.out.println("Remover estados: "+ch);
				String estado1 = ch.split("\\.")[0], estado2 = ch.split("\\.")[1];
				if(a.getEstadosFinais().contains(estado1)){// uni�o de estados finais
					a.removerEstadoFinal(estado1);
					a.removerEstadoFinal(estado2);
					a.removerEstado(estado1);
					a.removerEstado(estado2);
				}else{
					a.removerEstado(estado1);
					a.removerEstado(estado2);
				}
			}
		}
		
		// remover trasi��es repetidas
		HashMap<String, String> mapTras = new HashMap<String, String>();
		ArrayList<TransicaoAutomato> trasicoes = a.getTransicoes();
		int nt = trasicoes.size();
		for(int i=0; i<nt;i++){
			mapTras.put(trasicoes.get(i).getEstadoOrigem()+"="+trasicoes.get(i).getSimbolo()+"="+trasicoes.get(i).getEstadoDestino(), "v");
		}
		Set<String> chavesTras = mapTras.keySet();
		String origem;
		String simbolo;
		String destino;
		trasicoes.clear();
		a.limparTrasicoes();
		for(String chTr : chavesTras){
			origem = chTr.split("=")[0];
			simbolo = chTr.split("=")[1];
			destino = chTr.split("=")[2];
			a.setTransicao(origem, simbolo, destino);
		}
	}
}





