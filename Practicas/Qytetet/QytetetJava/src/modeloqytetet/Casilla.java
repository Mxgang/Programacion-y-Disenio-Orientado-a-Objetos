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
public class Casilla {
// Se podría haber hecho con una clase abstracta también    
    private int numeroCasilla, coste;
    
    public Casilla(int numeroCasilla, int coste)
    {
        this.numeroCasilla = numeroCasilla;
        this.coste = coste;
    }
    
    public int getNumeroCasilla() {
        return numeroCasilla;
    }

    int getCoste() {
        return coste;
    }

    protected TipoCasilla getTipo() {
        return null;
    }

    protected TituloPropiedad getTitulo() {
        return null;
    }
    
    protected void setCoste(int coste)
    {
        this.coste = coste;
    }
    
    protected boolean soyEdificable(){
        return false;
    }
    
    @Override
    public String toString() {
        String salida = "Casilla{" + "numeroCasilla=" + numeroCasilla + ", coste=" + 
                coste;
        return salida;
    }
    
}
