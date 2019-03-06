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
public class Especulador extends Jugador{
    private int fianza;
    
    Especulador(Jugador jugador, int fianza)
    {
        super(jugador);
        this.fianza = fianza;
    }
    
    @Override
    protected void pagarImpuesto()
    {
        int costeImpuesto = this.getCasillaActual().getCoste() / 2; 
        modificarSaldo(-costeImpuesto);
    }
    
    @Override
    protected boolean deboIrACarcel()
    {
        boolean debo = super.deboIrACarcel() && !pagarFianza();
        return debo;
    }
    
    @Override
    protected Especulador convertirme(int fianza)
    {
        return this;
    }
    
    private boolean pagarFianza()
    {
        boolean tengoSaldo = tengoSaldo(fianza);
        if(tengoSaldo)
        {
            modificarSaldo(-fianza);
            setEncarcelado(false);
        }
        return tengoSaldo;
    }
    
    @Override
    protected int getFactorEspeculador()
    {
        return fianza;
    }
    
    @Override
    protected boolean puedoEdificarCasa(TituloPropiedad titulo)
    {
        int numCasas = titulo.getNumCasas();
        // Hemos cambiado la visibilidad de esDeMiPropiedad de private a protected
        return ((numCasas < 8) && esDeMiPropiedad(titulo));
    }
    
    @Override
    protected boolean puedoEdificarHotel(TituloPropiedad titulo)
    {
        int numCasas = titulo.getNumCasas();
        int numHoteles = titulo.getNumHoteles();
        // Hemos cambiado la visibilidad de esDeMiPropiedad de private a protected
        return (numCasas >=4 && numHoteles < 8 && esDeMiPropiedad(titulo));
    }
    
    @Override
    public String toString() {
        return "ESPECULADOR (" + fianza + ") -> " + super.toString();
    }
}
