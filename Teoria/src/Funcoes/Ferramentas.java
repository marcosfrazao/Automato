package Funcoes;

import java.util.ArrayList;
import javax.swing.JFrame;

import Funcoes.MinimizarAFD;

import Funcoes.AFNAFD;
import Funcoes.Automato;
import Funcoes.DesenhoAutomato;
import Funcoes.TransicaoAutomato;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Ferramentas {
    public static ArrayList<String> marcado = new ArrayList<>();
    public static ArrayList<Transicoes> transicoes = new ArrayList<>();

    public static void comp_Paralela(String entrada1, String entrada2) {
        
        Automato afd1 = new Automato(entrada1);
        Automato afd2 = new Automato(entrada2);
        afd1.operacaoTrim();            
        afd2.operacaoTrim();            

        ArrayList<String> estadosCompostos = new ArrayList<String>();
        ArrayList<TransicaoAutomato> transicoesCompostas = new ArrayList<TransicaoAutomato>();
        ArrayList<String> simbolosCompostos = new ArrayList<String>();
        ArrayList<String> estadosFinaisCompostos = new ArrayList<String>();
        String estadoInicialComposto;


        estadoInicialComposto = afd1.getEstadoInicial().concat(",".concat(afd2.getEstadoInicial()));


        String temporaria;
        for (String estados1 : afd1.getEstados()) {
            for (String estados2 : afd2.getEstados()) {
                temporaria = estados1.concat(",".concat(estados2));
                estadosCompostos.add(temporaria);

            }
        }

        simbolosCompostos.addAll(afd1.getSimbolos());
        for (String string : afd2.getSimbolos()) {
            if (!simbolosCompostos.contains(string)) {
                simbolosCompostos.add(string);
            }
        }

        // transicões

        String[] aux;
        ArrayList<String> string1;
        ArrayList<String> string2;
        TransicaoAutomato transicao;
        String destino;
        for (String estado : estadosCompostos) {
            aux = estado.split(",");
            for (String simbolo : simbolosCompostos) {
                string1 = afd1.funcaoProgramaAFN(aux[0], simbolo);
                string2 = afd2.funcaoProgramaAFN(aux[1], simbolo);
                if (!string1.isEmpty() && !string2.isEmpty()) {
                    transicao = new TransicaoAutomato();
                    transicao.setEstadoOrigem(estado);
                    transicao.setSimbolo(simbolo);
                    destino = string1.get(0).concat(",".concat(string2.get(0)));
                    transicao.setEstadoDestino(destino);
                    transicoesCompostas.add(transicao);
                }
            }

        }

        // criar estados finais
        for (String string : estadosCompostos) {
            aux = string.split(",");
            if (afd1.getEstadosFinais().contains(aux[0]) && afd2.getEstadosFinais().contains(aux[1])) {
                estadosFinaisCompostos.add(string);
            }
        }

        Automato afdComposto = new Automato();
        afdComposto.setEstadoInicial(estadoInicialComposto);
        afdComposto.setEstados(estadosCompostos);
        afdComposto.setEstadosFinais(estadosFinaisCompostos);
        afdComposto.setSimbolos(simbolosCompostos);
        afdComposto.setTransicoes(transicoesCompostas);

        //afdComposto.operacaoTrim();


        try {
            DesenhoAutomato frame1 = new DesenhoAutomato(afdComposto, "Composição Paralela");
            frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame1.setVisible(true);
        } catch (Exception e) {
            System.err.println(e);
        }

    
        
    }
        
    public static void AFN_AFD(String entrada) {
        marcado = new ArrayList<>();
        transicoes = new ArrayList<>();
        ArrayList<ArrayList<String>> jaVisitados = new ArrayList<>();
        Automato afn = new Automato(entrada);
        ArrayList<ArrayList<String>> estadosAfd = new ArrayList<>();
        ArrayList<String> estadoInicial = criarEstadoInicial(afn.getEstadoInicial(), afn);
        
        jaVisitados = percorrer(jaVisitados, afn, estadoInicial);
        
        ArrayList<String> estadosResultantes = new ArrayList<>();
        ArrayList<String> estadosFinaisResultantes = new ArrayList<>();
        
        
        for (ArrayList<String> arrayList : jaVisitados) {
            boolean flag = false;
            String s = "";
            for (String string : arrayList) {
                if (afn.getEstadosFinais().contains(string)) {
                    flag = true;
                }
                s = s.concat(string);
            }
            if (flag) {
                estadosFinaisResultantes.add(s);
            }
            estadosResultantes.add(s);
        }
        
        ArrayList<TransicaoAutomato> transicoesResultantes = new ArrayList<>();
        
        for (Transicoes  transicoesAFN : transicoes) {
            String origem = "";
            for (String string : transicoesAFN.getOrigem()) {
                origem = origem.concat(string);
            }
            String token = transicoesAFN.getSimbolo();
            String destino = "";
            
            for (String string : transicoesAFN.getDestino()) {
                destino = destino.concat(string);
            }
            TransicaoAutomato t = new TransicaoAutomato();
            t.setEstadoDestino(destino);
            t.setEstadoOrigem(origem);
            t.setSimbolo(token);
            transicoesResultantes.add(t);
        }
        
        
        ArrayList<String> simbolosResultantes = afn.getSimbolos();
        
        String estadoInicialResultante = "";
        
        for (String s : estadoInicial) {
            estadoInicialResultante = estadoInicialResultante.concat(s);
        }
        
        
        Automato a = new Automato();
        a.setEstadoInicial(estadoInicialResultante);
        a.setEstados(estadosResultantes);
        a.setEstadosFinais(estadosFinaisResultantes);
        a.setSimbolos(simbolosResultantes);
        a.setTransicoes(transicoesResultantes);
        
        
        DesenhoAutomato frame2 = new DesenhoAutomato(a, "AFD");
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setVisible(true);
          
    }
    
    public static ArrayList<ArrayList<String>> percorrer(ArrayList<ArrayList<String>> visitados, Automato afn, ArrayList<String> estado){
        if (visitados.contains(estado)) {
            return visitados;
        }
        visitados.add(estado);
        for (String string : afn.getSimbolos()) {
            ArrayList<String> estadosAlcancados = new ArrayList<>();
            for (String string1 : estado) {
                ArrayList<String> aux = afn.funcaoProgramaAFN(string1, string);
                for (String string2 : aux) {
                    if (!estadosAlcancados.contains(string2)) {
                        estadosAlcancados.add(string2);
                    }
                }
            }
            ArrayList<String> fecho = new ArrayList<>();
            for (String string1 : estadosAlcancados) {
                ArrayList<String> temporario = clousure(string1, new ArrayList<String>(), afn);
                for (String string2 : temporario) {
                    if (!fecho.contains(string2)) {
                        fecho.add(string2);
                    }
                }
            }
            if(!fecho.isEmpty()){
                Transicoes transicaoAfns = new Transicoes();
                transicaoAfns.setOrigem(estado);
                transicaoAfns.setSimbolo(string);
                transicaoAfns.setDestino(fecho);
                transicoes.add(transicaoAfns);
                if (!visitados.contains(fecho)) {
                    visitados = percorrer(visitados, afn, fecho);
                }
            }
        }
        return visitados;
    }
    
    public static ArrayList<String> criarEstadoInicial(String inicial, Automato afn){
        return clousure(inicial, new ArrayList<String>(), afn);
    }
        
    public static ArrayList<String> clousure(String entrada, ArrayList<String> fechado, Automato afn){
        if (marcado.contains(entrada)) {
            return fechado;
        }
        fechado.add(entrada);
        ArrayList<String> estados = afn.funcaoProgramaAFN(entrada, "vazio");
        for (String string : estados) {
            fechado = clousure(string, fechado, afn);
        }
        return fechado;
    }

    public static void TRIM(String entrada) {
        Automato afn = new Automato(entrada);
        Automato afnTrim = afn.operacaoTrim();
        DesenhoAutomato frame2 = new DesenhoAutomato(afnTrim, "TRIM");
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setVisible(true);
    }

    public static void mimimizar(String entrada){
		Automato afd = new Automato(entrada);
		Automato afdMinimo = new MinimizarAFD(afd).minimizar();
		DesenhoAutomato frame2 = new DesenhoAutomato(afdMinimo, "Minimização");
		frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame2.setVisible(true);
	}

    public static void visualizarEntrada(String entrada1, String entrada2) {
        
        Automato afd1 = new Automato(entrada1);
        DesenhoAutomato frame1 = new DesenhoAutomato(afd1, "Automato1");
        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame1.setLocation((dim.width/2)-frame1.getSize().width, (dim.height/2)-(frame1.getSize().height/2));
        frame1.setVisible(true);
         
        
        Automato afd2 = new Automato(entrada2);
        DesenhoAutomato frame2 = new DesenhoAutomato(afd2, "Automato2");
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setLocation((dim.width/2), (dim.height/2)-(frame2.getSize().height/2));
        frame2.setVisible(true);
    }
    
    public static void produto(String entrada1, String entrada2) {
        Automato afd1 = new Automato(entrada1);
        Automato afd2 = new Automato(entrada2);
        afd1.operacaoTrim();            
        afd2.operacaoTrim();            

        ArrayList<String> estadosCompostos = new ArrayList<String>();
        ArrayList<TransicaoAutomato> transicoesCompostas = new ArrayList<TransicaoAutomato>();
        ArrayList<String> simbolosCompostos = new ArrayList<String>();
        ArrayList<String> estadosFinaisCompostos = new ArrayList<String>();
        String estadoInicialComposto;


        estadoInicialComposto = afd1.getEstadoInicial().concat(",".concat(afd2.getEstadoInicial()));


        String temporaria;
        for (String estados1 : afd1.getEstados()) {
            for (String estados2 : afd2.getEstados()) {
                temporaria = estados1.concat(",".concat(estados2));
                estadosCompostos.add(temporaria);

            }
        }

        simbolosCompostos.addAll(afd1.getSimbolos());
        for (String string : afd2.getSimbolos()) {
            if (!simbolosCompostos.contains(string)) {
                simbolosCompostos.add(string);
            }
        }

        // transicões

        String[] aux;
        ArrayList<String> string1;
        ArrayList<String> string2;
        TransicaoAutomato transicao;
        String destino;
        for (String estado : estadosCompostos) {
            aux = estado.split(",");
            for (String simbolo : simbolosCompostos) {
                string1 = afd1.funcaoProgramaAFN(aux[0], simbolo);
                string2 = afd2.funcaoProgramaAFN(aux[1], simbolo);
                if (!string1.isEmpty() && !string2.isEmpty()) {
                    transicao = new TransicaoAutomato();
                    transicao.setEstadoOrigem(estado);
                    transicao.setSimbolo(simbolo);
                    destino = string1.get(0).concat(",".concat(string2.get(0)));
                    transicao.setEstadoDestino(destino);
                    transicoesCompostas.add(transicao);
                }
            }

        }

        // criar estados finais
        for (String string : estadosCompostos) {
            aux = string.split(",");
            if (afd1.getEstadosFinais().contains(aux[0]) && afd2.getEstadosFinais().contains(aux[1])) {
                estadosFinaisCompostos.add(string);
            }
        }

        Automato afdComposto = new Automato();
        afdComposto.setEstadoInicial(estadoInicialComposto);
        afdComposto.setEstados(estadosCompostos);
        afdComposto.setEstadosFinais(estadosFinaisCompostos);
        afdComposto.setSimbolos(simbolosCompostos);
        afdComposto.setTransicoes(transicoesCompostas);

        afdComposto.operacaoTrim();


        try {
            DesenhoAutomato frame1 = new DesenhoAutomato(afdComposto, "PRODUTO");
            frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame1.setVisible(true);
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    
}
