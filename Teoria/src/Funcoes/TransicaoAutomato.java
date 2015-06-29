/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcoes;

/**
 *
 * @author USUARIO
 */
public class TransicaoAutomato {
    private String estadoOrigem;
    private String simbolo;
    private String estadoDestino;

    public String getEstadoOrigem() {
        return estadoOrigem;
    }

    public void setEstadoOrigem(String estadoOrigem) {
        this.estadoOrigem = estadoOrigem;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(String estadoDestino) {
        this.estadoDestino = estadoDestino;
    }   
    
}
