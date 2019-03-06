/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author maxigang
 */
public class Sorpresa {
    
    private String texto;
    TipoSorpresa tipo_sorpresa;
    private int valor;
    
    public Sorpresa(String texto, int valor, TipoSorpresa tipo_sorpresa){
            
        this.texto = texto;
        this.valor = valor;
        this.tipo_sorpresa = tipo_sorpresa;
    }
    
    String getTexto(){
        return texto;
    }
    
    int getValor(){
        return valor;
    }
    
    TipoSorpresa getTipoSorpresa(){
        return tipo_sorpresa;
    }
    
    @Override
    public String toString() {
        return "Sorpresa{" + "texto=" + texto + ", valor=" + 
        Integer.toString(valor) + ", tipo=" + tipo_sorpresa + "}";
    }
    
    
    
}
