package Funcoes;

import java.util.ArrayList;

import Funcoes.Automato;

public class MinimizarAFD {
	Automato auto;
	Automato minimo;
	Estados TabelaDeMin;

	public MinimizarAFD(Automato auto) {
		super();
		auto.operacaoTrim();
		this.auto = auto;
		this.minimo = new Automato(auto);
		
	}
	
	public Automato minimizar(){
		// Passo 0: Funcao programa total
		inserirEstadoD();
		
		// Passo 1: Montar a tabela 
		TabelaDeMin = new Estados(minimo.getEstados());
		
		// Passo 2. Marcacao dos pares { estado final, estado nao-final }
		TabelaDeMin.marcarFinalNaoFinal(minimo.getEstadosFinais());
		
		// Passo 3. Analise dos pares de estado nao-marcados
		TabelaDeMin.analiseNaoMarcados(minimo);
		
		// Passo 4. Unificacao dos estados
		TabelaDeMin.unirEstados(minimo);
		
		minimo.removerEstado("D");
		return minimo;
	}
	
	private void inserirEstadoD(){
		ArrayList<String> estados = minimo.getEstados();
		ArrayList<String> simbolos = minimo.getSimbolos();
		minimo.adicionarEstado("D");
		int ne = estados.size();
		int ns = simbolos.size();
		for(int s=0; s<ns; s++){
			minimo.setTransicao("D", simbolos.get(s), "D");
		}
		for(int e=0; e<ne; e++){
			for(int s=0; s<ns; s++){
				if(minimo.funcaoProgramaAFN(estados.get(e), simbolos.get(s)).size() == 0){
					minimo.setTransicao(estados.get(e), simbolos.get(s), "D");
				}
			}
		}
	}
}
