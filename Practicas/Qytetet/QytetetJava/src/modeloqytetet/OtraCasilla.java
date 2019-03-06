/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author enrique
 */
public class OtraCasilla extends Casilla {
    private TipoCasilla tipo;
    private int coste;
    
    public OtraCasilla(int numeroCasilla, int coste, TipoCasilla tipo)
    {
        super(numeroCasilla, coste);
        this.tipo = tipo;
    }
    
    @Override
    public TipoCasilla getTipo()
    {
        return tipo;
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", tipo=" + tipo + "}\n";
    }
}
