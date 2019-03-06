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
public class Calle extends Casilla {
    private TituloPropiedad titulo;
    public Calle(int numeroCasilla, TituloPropiedad titulo)
    {
        super(numeroCasilla, titulo.getPrecioCompra());
        this.titulo = titulo;
    }
    
    public TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    }
    
    public void setTitulo(TituloPropiedad titulo) {
        this.titulo = titulo;
    }
    
    protected TituloPropiedad getTitulo() {
        return titulo;
    }
    
    public int pagarAlquiler(){
        int costeAlquiler = titulo.pagarAlquiler();
        return costeAlquiler;
    }
    
    boolean propietarioEncarcelado(){
        return titulo.propietarioEncarcelado();
    }
    
    @Override
    protected boolean soyEdificable(){
        return true;
    }
    
    public boolean tengoPropietario(){
        return titulo.tengoPropietario();
    }
    
    @Override
    public TipoCasilla getTipo()
    {
        return TipoCasilla.CALLE;
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", tipo=" + getTipo() + ", titulo=" + titulo + "}\n";
    }
}
