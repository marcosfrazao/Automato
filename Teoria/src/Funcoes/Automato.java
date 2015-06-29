package Funcoes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Automato {

    private ArrayList<String> estados;
    private ArrayList<String> simbolos;
    private ArrayList<TransicaoAutomato> transicoes;
    private String estadoInicial;
    private ArrayList<String> estadosFinais;

    public void setEstados(ArrayList<String> estados) {
        this.estados = estados;
    }

    public void setSimbolos(ArrayList<String> simbolos) {
        this.simbolos = simbolos;
    }

    public void setTransicoes(ArrayList<TransicaoAutomato> transicoes) {
        this.transicoes = transicoes;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public void setEstadosFinais(ArrayList<String> estadosFinais) {
        this.estadosFinais = estadosFinais;
    }
    
    

    public Automato() {
        estados = new ArrayList<String>();
        simbolos = new ArrayList<String>();
        transicoes = new ArrayList<TransicaoAutomato>();
        estadosFinais = new ArrayList<String>();
    }

    public Automato(String entrada) {

        estados = new ArrayList<String>();
        simbolos = new ArrayList<String>();
        transicoes = new ArrayList<TransicaoAutomato>();
        estadoInicial = null;
        estadosFinais = new ArrayList<String>();

        construirAFD(entrada);
    }

    public Automato(File arquivo) {
        super();

        estados = new ArrayList<String>();
        simbolos = new ArrayList<String>();
        transicoes = new ArrayList<TransicaoAutomato>();
        estadoInicial = null;
        estadosFinais = new ArrayList<String>();

        construirAFD(arquivo);
    }

    public Automato(Automato afd) {
        estados = new ArrayList<String>();
        estados.addAll(afd.estados);
        simbolos = new ArrayList<String>();
        simbolos.addAll(afd.simbolos);

        transicoes = new ArrayList<TransicaoAutomato>(afd.transicoes);

        estadoInicial = afd.estadoInicial;
        estadosFinais = new ArrayList<String>();
        estadosFinais.addAll(afd.estadosFinais);
    }

    public boolean adicionarEstado(String estado) {
        estado = estado.toUpperCase();
        if (!estados.contains(estado)) {
            estados.add(estado);
            return true;
        } else {
            return false;
        }
    }

    public boolean removerEstado(String estado) {
        ArrayList<String> remover = new ArrayList<String>();
        estado = estado.toUpperCase();
        if (estados.remove(estado)) {
            if (estado.equals(estadoInicial)) {
                estadoInicial = null;
            }
            ArrayList<TransicaoAutomato> aux = new ArrayList<TransicaoAutomato>(transicoes);

            for (int i = 0; i < transicoes.size(); i++) {
                if (estado.equals(transicoes.get(i).getEstadoOrigem()) || estado.equals(transicoes.get(i).getEstadoDestino())) {
                    aux.remove(transicoes.get(i));
                }
            }
            transicoes = aux;

            return true;
        } else {
            return false;
        }
    }

    public boolean adicionarEstadoFinal(String estadoFinal) {
        estadoFinal = estadoFinal.toUpperCase();
        if (estados.contains(estadoFinal) && !estadosFinais.contains(estadoFinal)) {
            estadosFinais.add(estadoFinal);
            return true;
        } else {
            return false;
        }
    }

    public boolean removerEstadoFinal(String estadoFinal) {
        estadoFinal = estadoFinal.toUpperCase();
        if (estadosFinais.remove(estadoFinal)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean adicionarSimbolo(String simbolo) {
        simbolo.toLowerCase();
        if (!simbolos.contains(simbolo)) {
            simbolos.add(simbolo);
            return true;
        } else {
            return false;
        }
    }

    public boolean removerSimbolo(String simbolo) {
        simbolo.toLowerCase();
        if (simbolos.remove(simbolo)) {
            return true;
        } else {
            return false;
        }
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }



    public void setTransicao(String estadoInicial, String simboloLido, String estadoFinal) {
        String chave;
        estadoInicial = estadoInicial.toUpperCase();
        simboloLido = simboloLido.toLowerCase();
        estadoFinal = estadoFinal.toUpperCase();

        if (!estados.contains(estadoInicial) && !estadosFinais.contains(estadoInicial)) {
            System.out.println(estadoInicial);
            throw new IllegalArgumentException("Não Existe Estado de Origem!");
        }
        if (!simbolos.contains(simboloLido)) {
            throw new IllegalArgumentException("Não Existe Simbolos!");
        }
        if (!estados.contains(estadoFinal)) {
            throw new IllegalArgumentException("Não Existe estado!");
        }
        TransicaoAutomato novaTransicao = new TransicaoAutomato();
        novaTransicao.setEstadoOrigem(estadoInicial);
        novaTransicao.setEstadoDestino(estadoFinal);
        novaTransicao.setSimbolo(simboloLido);
        transicoes.add(novaTransicao);
    }

    public String funcaoPrograma(String estadoInicial, String simboloLido) {
        String chave = estadoInicial + "." + simboloLido;
        for (TransicaoAutomato aux : transicoes) {
            if (aux.getEstadoOrigem().equals(estadoInicial) && aux.getSimbolo().equals(simboloLido)) {
                return aux.getEstadoDestino();
            }
        }
        return null;
    }

    public void construirAFD(String entrada) {
        String[] linhas = entrada.split("\n");
        String[] aux;

        limpar();
        for (String linha : linhas) {
            aux = linha.split(" = \\{| = |, |}|\\(|\\) = ");

            if (aux[0].equals("T")) {
                for (int i = 1; i < aux.length; i++) {
                    adicionarSimbolo(aux[i]);
                }
            } else if (aux[0].equals("Q")) {
                for (int i = 1; i < aux.length; i++) {
                    adicionarEstado(aux[i]);
                }
            } else if (aux[0].equals("P")) {
                setTransicao(aux[1], aux[2], aux[3]);
            } else if (aux[0].equals("q0")) {
                setEstadoInicial(aux[1]);
            } else if (aux[0].equals("F")) {
                for (int i = 1; i < aux.length; i++) {
                    adicionarEstadoFinal(aux[i]);
                }
            }
        }
    }

    public void construirAFD(File arquivo) {
        Reader fileReader;
        BufferedReader bufferReader;
        String[] aux;
        String linha;
        try {
            fileReader = new InputStreamReader(new FileInputStream(arquivo), "UTF-8");
            bufferReader = new BufferedReader(fileReader);

            limpar();
            while ((linha = bufferReader.readLine()) != null) {
                aux = linha.split(" = \\{| = |, |}|\\(|\\) = ");

                if (aux[0].equals("T")) {
                    for (int i = 1; i < aux.length; i++) {
                        adicionarSimbolo(aux[i]);
                    }
                } else if (aux[0].equals("Q")) {
                    for (int i = 1; i < aux.length; i++) {
                        adicionarEstado(aux[i]);
                    }
                } else if (aux[0].equals("P")) {
                    setTransicao(aux[1], aux[2], aux[3]);
                } else if (aux[0].equals("q0")) {
                    setEstadoInicial(aux[1]);
                } else if (aux[0].equals("F")) {
                    for (int i = 1; i < aux.length; i++) {
                        adicionarEstadoFinal(aux[i]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
	public void limparTrasicoes(){
        transicoes.clear();
	}

    public boolean limpar() {
        transicoes.clear();
        estadosFinais.clear();
        estados.clear();
        estadoInicial = null;
        simbolos.clear();

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("M = (Σ, Q, δ, q0, F)\n");
        int tam1, tam2;
        String aux;
        // Σ //////////
        builder.append("Σ = {");
        tam1 = simbolos.size();
        if (tam1 == 0) {
            builder.append("Ø");
        } else {
            builder.append(simbolos.get(0));
            for (int i = 1; i < tam1; i++) {
                builder.append(", ");
                builder.append(simbolos.get(i));
            }
        }
        builder.append("}\n");

        // Q //////////
        builder.append("Q = {");
        tam1 = estados.size();
        if (tam1 == 0) {
            builder.append("Ø");
        } else {
            builder.append(estados.get(0));
            for (int i = 1; i < tam1; i++) {
                builder.append(", ");
                builder.append(estados.get(i));
            }
        }
        builder.append("}\n");

        // δ //////////
        tam1 = transicoes.size();
        if (tam1 == 0) {
            builder.append("δ = Ø\n");
        } else {
            builder.append("\nδ     ");
            tam1 = simbolos.size();
            for (int i = 0; i < tam1; i++) {
                builder.append("| ");
                builder.append(String.format("%5s", simbolos.get(i)));
                builder.append(" ");
            }
            builder.append("\n");
            tam2 = estados.size();
            for (int i = 0; i < tam2; i++) {
                builder.append(String.format("%-5s", estados.get(i)));
                builder.append(" ");
                for (int j = 0; j < tam1; j++) {
                    aux = funcaoPrograma(estados.get(i), simbolos.get(j));
                    builder.append("| ");
                    if (aux != null) {
                        builder.append(String.format("%5s", aux));
                    } else {
                        builder.append("     ");
                    }
                    builder.append(" ");
                }
                builder.append("\n");
            }
            builder.append("\n");
        }

        // q0 /////////
        builder.append("q0 = ");
        if (estadoInicial != null) {
            builder.append(estadoInicial);
            builder.append("\n");
        } else {
            builder.append("Ø\n");
        }

        // F //////////
        builder.append("F = {");
        tam1 = estadosFinais.size();
        if (tam1 == 0) {
            builder.append("Ø");
        } else {
            builder.append(estadosFinais.get(0));
            for (int i = 1; i < tam1; i++) {
                builder.append(", ");
                builder.append(estadosFinais.get(i));
            }
        }
        builder.append("}\n");

        return builder.toString();
    }

    @SuppressWarnings("rawtypes")
    public Automato minimizar() throws UniaoDeEstado {
        this.adicionarDescarte();
        List<List<ArrayList<String>>> tabela = new ArrayList<List<ArrayList<String>>>();
        //um itenm da tabela deve apontar para "decartado" quando os estados nao puderem ser unidos
        ArrayList<String> descartado = new ArrayList<String>();
        descartado.add("0.0");

        //povoar a tabela BOTAR O X
        for (int i = 0; i < estados.size(); i++) {
            tabela.add(new ArrayList<ArrayList<String>>());
            for (int j = 0; j < i; j++) {
                tabela.get(i).add(new ArrayList<String>());

                //descartar unificação de estados finais com nao-finais
                if (estadosFinais.contains(estados.get(i)) ^ estadosFinais.contains(estados.get(j))) {
                    tabela.get(i).set(j, descartado);
                }
            }
        }

        //percorrer a tabela
        for (int i = 0; i < estados.size(); i++) {
            for (int j = 0; j < i; j++) {
				//analise do par
                //verificaar a possivel uniao dos estados

                String estadoDestino1;
                String estadoDestino2;

                if (tabela.get(i).get(j).equals(descartado)) {
                    continue;
                }

                for (String s : this.simbolos) {
                    estadoDestino1 = this.funcaoPrograma(estados.get(i), s);
                    estadoDestino2 = this.funcaoPrograma(estados.get(j), s);
                    if (!estadoDestino1.equals(estadoDestino2)) {

                        int indiceDestino1 = estados.indexOf(estadoDestino1);
                        int indiceDestino2 = estados.indexOf(estadoDestino2);

                        ArrayList<String> itemCorrespondenteAosEstadosResultantes;

                        if (indiceDestino1 > indiceDestino2) {
                            itemCorrespondenteAosEstadosResultantes = tabela.get(indiceDestino1).get(indiceDestino2);
                        } else {
                            itemCorrespondenteAosEstadosResultantes = tabela.get(indiceDestino2).get(indiceDestino1);
                        }

                        if (itemCorrespondenteAosEstadosResultantes.equals(descartado)) {

                            //descartar pares "pendurados"
                            for (String par : tabela.get(i).get(j)) {
                                if (par != "0.0") {
                                    tabela.get(estados.indexOf(par.split("\\.")[0])).
                                            set(estados.indexOf(par.split("\\.")[1]), descartado);
                                }
                            }
                            tabela.get(i).set(j, descartado);
                        } else {
                            //acrescenta o par atual no item correspondente ao par dos estados resultantes das transicoes
                            itemCorrespondenteAosEstadosResultantes.add(estados.get(i).concat("." + estados.get(j)));
                        }

                    }
                }
            }

        }

        //preparar o novo afd
        Automato afdMin = new Automato(this);
        this.removerEstado("D");
        afdMin.removerEstadosInalcansaveis();
        //armazenar os estados que serao unificados
        Map<String, String> estadosUnificados = new HashMap<String, String>();

        for (int i = 0; i < this.estados.size(); i++) {
            for (int j = 0; j < i; j++) {
                //verifica se o par nao foi marcado
                if (tabela.get(i).get(j) != descartado) {
                    String estado1 = this.estados.get(i);
                    String estado2 = this.estados.get(j);
                    String novoEstado = estado1.concat("-" + estado2);
                    estadosUnificados.put(estado1, novoEstado);
                    estadosUnificados.put(estado2, novoEstado);

                    //add novos estados
                    if (this.estadosFinais.contains(estado1)) {
                        afdMin.adicionarEstado(novoEstado);
                        if (this.estadosFinais.contains(estado2)) {
                            afdMin.adicionarEstadoFinal(novoEstado);
                        } else {
                            UniaoDeEstado exception = new UniaoDeEstado();
                            System.out.println("Uniao De Estado Final Com Estado Nao Final " + estado1 + " e " + estado2);
                            throw exception;
                        }
                    } else {
                        if (!(this.estadosFinais).contains(estado2)) {
                            afdMin.adicionarEstado(novoEstado);
                        } else {
                            UniaoDeEstado exception = new UniaoDeEstado();
                            System.out.println(estado1 + " e " + estado2);
                            throw exception;
                        }
                    }

                }
            }
        }

        for (int i = 0; i < this.estados.size(); i++) {
            for (int j = 0; j < i; j++) {
                //verifica se o par nao foi marcado
                if (tabela.get(i).get(j) != descartado) {
                    String estado1 = this.estados.get(i);
                    String estado2 = this.estados.get(j);
                    String novoEstado = estado1.concat("-" + estado2);

                    for (int k = 0; k < transicoes.size(); k++) {

                                                // chave = estadoOrigem + simboloLido
                        // valor = estadoDestino
                        String estadoOrigem = transicoes.get(k).getEstadoOrigem();
                        String estadoDestino = transicoes.get(k).getEstadoDestino();
                        String simboloLido = transicoes.get(k).getSimbolo();
                        //alterar transicao que chega aos estados equivalentes
                        if (estadoDestino.equals(estado1) || estadoDestino.equals(estado2)) {
                            //verifica se o estado de origem ainda é valido
                            if (!estadosUnificados.containsKey(estadoOrigem)) {
                                afdMin.setTransicao(estadoOrigem,
                                        simboloLido, novoEstado);
                            } else {
                                afdMin.setTransicao(estadosUnificados.get(estadoOrigem),
                                        simboloLido, novoEstado);
                            }
                        }
                        //alterar transicoes q partem do estado1 ou estado2
                        if (estadoOrigem.equals(estado1) || estadoOrigem.equals(estado2)) {
                            //verifica se o estado de destino ainda é valido, ou seja, se nao foi unificado
                            if (!estadosUnificados.containsKey(estadoDestino)) {
                                afdMin.setTransicao(novoEstado, simboloLido, estadoDestino);
                            } else {
                                afdMin.setTransicao(novoEstado, simboloLido, estadosUnificados.get(estadoDestino));
                            }
                        }
                    }
                    //excluir estado1 e estado2
                    afdMin.estados.remove(afdMin.estados.indexOf(estado1));
                    afdMin.estados.remove(afdMin.estados.indexOf(estado2));

                    if (afdMin.estadosFinais.contains(estado1)) {
                        afdMin.estadosFinais.remove(afdMin.estadosFinais.indexOf(estado1));
                        afdMin.estadosFinais.remove(afdMin.estadosFinais.indexOf(estado2));
                    }

                }

            }
        }

        afdMin.removerEstadosInvalidos();

        return afdMin;
    }

    public ArrayList<String> getEstados() {
        return estados;
    }

    public ArrayList<String> getSimbolos() {
        return simbolos;
    }

    public ArrayList<TransicaoAutomato> getTransicoes() {
        return transicoes;
    }

    public ArrayList<String> getEstadosFinais() {
        return estadosFinais;
    }

    public void removerEstadosInalcansaveis() {
        ArrayList<String> listaEstados = new ArrayList<String>();
        listaEstados.addAll(estados);
        Queue<String> fila = new LinkedList<String>();
        String estado;

        fila.add(estadoInicial);
        while (!fila.isEmpty()) {
            estado = fila.poll();
            if (listaEstados.remove(estado)) {
                for (String simbolo : simbolos) {
                    fila.offer(funcaoPrograma(estado, simbolo));
                }
            }
        }

        for (String estadoInaceddivel : listaEstados) {
            removerEstado(estadoInaceddivel);
        }
    }

    public void adicionarDescarte() {
        String descarte = "D";
        if (transicoes.size() < estados.size() * simbolos.size()) {
            adicionarEstado(descarte);
            for (String estado : estados) {
                for (String simbolo : simbolos) {
                    if (funcaoPrograma(estado, simbolo) == null) {
                        setTransicao(estado, simbolo, descarte);
                    }
                }
            }
        }
    }

    public void removerEstadosInvalidos() {
        ArrayList<String> estadosRemover = new ArrayList<String>();
        estadosRemover.addAll(estados);
        ArrayList<String> lista;
        Queue<String> fila;
        String atual, aux;
        for (String estado : estados) {
            if (estadosFinais.contains(estado)) {
                estadosRemover.remove(estado);
            } else {
                lista = new ArrayList<String>();
                fila = new LinkedList<String>();
                fila.offer(estado);
                while (!fila.isEmpty()) {
                    atual = fila.poll();
                    if (!lista.contains(atual)) {
                        lista.add(atual);
                        for (String simbolo : simbolos) {
                            aux = funcaoPrograma(atual, simbolo);
                            if (estadosFinais.contains(aux)) {
                                estadosRemover.remove(estado);
                                fila.clear();
                                break;
                            } else {
                                fila.offer(aux);
                            }
                        }
                    }
                }
            }
        }

        for (String estadoRemover : estadosRemover) {
            removerEstado(estadoRemover);
        }
    }
    
    public Automato operacaoTrim(){
        removerEstadosNaoAcessiveis();
        removerEstadosNaoCoAcessiveis();
        return this;
    }

        // CODIGOS INSERIDOS POR RAMON
    public void removerEstadosNaoAcessiveis() {
        ArrayList<String> estadosAcessiveis = new ArrayList<String>();
        ArrayList<String> estadosInacessiveis = new ArrayList<String>();
        Queue fila = new LinkedList();
        ArrayList<String> concorrentesAFila;
        estadosAcessiveis.add(estadoInicial);
        fila.add(estadoInicial);
        while (!fila.isEmpty()) {
            String atual = (String) fila.poll();
            concorrentesAFila = new ArrayList<String>();
            for (String string : simbolos) {
                concorrentesAFila.addAll(funcaoProgramaAFN(atual, string)); // verificar se a funcao programa retorna null
            }
            for (String string : concorrentesAFila) {
                if (!estadosAcessiveis.contains(string)) {
                    estadosAcessiveis.add(string);
                    fila.add(string);
                }
            }
        }

        for (String string : estados) {
            if (!estadosAcessiveis.contains(string)) {
                estadosInacessiveis.add(string);
            }
        }

        for (String string : estadosInacessiveis) {
            removerEstado(string);
        }
    }

    public void removerEstadosNaoCoAcessiveis(){
        ArrayList<String> estadosNaoCoAcessiveis = new ArrayList<String>();
        for (String string : estados) {
            if(!alcancarFinal(string)){
                estadosNaoCoAcessiveis.add(string);
            }
        }
        for (String string : estadosNaoCoAcessiveis) {
            removerEstado(string);
        }
    }

    public ArrayList<String> funcaoProgramaAFN(String estadoOrigem, String simbolo) {
        ArrayList<String> estadosAlcancados = new ArrayList<String>();
        for (TransicaoAutomato t : transicoes) {
            if (t.getEstadoOrigem().equals(estadoOrigem) && t.getSimbolo().equals(simbolo)) {
                estadosAlcancados.add(t.getEstadoDestino());
            }
        }
        return estadosAlcancados;
    }
    // dado um estado, verificar se consegue alcançar um estado final.
    private boolean alcancarFinal(String string) {
        if (estadosFinais.contains(string)) {
            return true;
        }
        Queue<String> fila = new LinkedList();
        ArrayList<String> estadosVisitados = new ArrayList<String>();
        ArrayList<String> concorrenteAFila;
        fila.add(string);
        estadosVisitados.add(string);
        while (!fila.isEmpty()) {            
            concorrenteAFila = new ArrayList<String>();
            String atual = fila.poll();
            for (String simbol : simbolos) {
                concorrenteAFila.addAll(funcaoProgramaAFN(atual, simbol));
            }
            for (String simbol : concorrenteAFila) {
                if (estadosFinais.contains(simbol)) {
                    return true;
                }
                if (!estadosVisitados.contains(simbol)){
                    estadosVisitados.add(simbol);
                    fila.add(simbol);
                }
            }
        }
        return false;
    }


}
