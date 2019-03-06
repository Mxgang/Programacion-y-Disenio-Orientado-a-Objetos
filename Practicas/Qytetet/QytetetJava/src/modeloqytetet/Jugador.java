/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import java.util.ArrayList;

/**
 *
 * @author maxigang
 */
public class Jugador implements Comparable {
    
    private boolean encarcelado;
    private String nombre;
    private int saldo;
    private boolean tieneCartaLibertad;
    
    private Sorpresa cartaLibertad;
    private ArrayList<TituloPropiedad> propiedades;
    private Casilla casillaActual;
    
    public Jugador(String n, int s)
    {
        encarcelado = false;
        nombre = n;
        saldo = s;
        tieneCartaLibertad = false;
        propiedades = new ArrayList<>();
    }
  
    protected Jugador(Jugador otroJugador)
    {
        encarcelado = otroJugador.encarcelado;
        nombre = otroJugador.nombre;
        saldo = otroJugador.saldo;
        tieneCartaLibertad = otroJugador.tieneCartaLibertad;
        cartaLibertad = otroJugador.cartaLibertad;
        propiedades = otroJugador.propiedades;
        casillaActual = otroJugador.casillaActual;
    }
  
    @Override
    public int compareTo(Object otroJugador) {
        int otroCapital = ((Jugador) otroJugador).obtenerCapital();
        return otroCapital - obtenerCapital();
    }
    
    boolean cancelarHipoteca(TituloPropiedad titulo){
        int costeCancelarHipoteca = titulo.calcularCosteCancelar();
        boolean cancelada = false;
        if(tengoSaldo(costeCancelarHipoteca) && esDeMiPropiedadHipotecada(titulo,true))
        {
            modificarSaldo(-costeCancelarHipoteca);
            titulo.cancelarHipoteca();
            cancelada = true;
        }
        return cancelada;
    }
    
    boolean comprarTituloPropiedad(){
        boolean comprado = false;
        Calle calleActual = (Calle) casillaActual;
        int costeCompra = calleActual.getCoste();
        if(tengoSaldo(costeCompra))
        {
            TituloPropiedad titulo = calleActual.asignarPropietario(this);
            comprado = true;
            propiedades.add(titulo);
            modificarSaldo(-costeCompra);
        }
        return comprado;
    }
    
    ////
    
    protected Especulador convertirme(int fianza)
    {
        Especulador esp = new Especulador(this, fianza);
        return esp;
    }
    
    ////
    
    int cuantasCasasHotelesTengo(){
        int contador = 0;
        for(TituloPropiedad p:propiedades)
            contador += p.getNumCasas() + p.getNumHoteles();
        return contador;
    }
    
    ////
    
    protected boolean deboIrACarcel()
    {
        return !tengoCartaLibertad();
    }
    
    ////
    
    boolean deboPagarAlquiler(){
        Calle calleActual = (Calle) casillaActual;
        
        boolean esDeMiPropiedad = esDeMiPropiedad(calleActual.getTitulo());
        boolean tienePropietario = false;
        boolean estaEncarcelado = false;
        boolean estaHipotecada = false;
        
        if(!esDeMiPropiedad)
        {
            tienePropietario = calleActual.tengoPropietario();
            if(tienePropietario)
            {
                estaEncarcelado = calleActual.propietarioEncarcelado();
                estaHipotecada = calleActual.getTitulo().getHipotecada();
            }       
        }
        boolean deboPagar = !esDeMiPropiedad & tienePropietario & !estaEncarcelado & !estaHipotecada;
        
        return deboPagar;
    }
    
    Sorpresa devolverCartaLibertad(){ // RECORDAR QUE AL LLAMAR A ESTE METODO DEBEMOS ASEGURARNOS DE QUE EL JUGADOR TENGA UNA CARTA
        tieneCartaLibertad = false;
        return cartaLibertad;
    }
    
    boolean edificarCasa(TituloPropiedad titulo){
        boolean edificada = false;
        if(puedoEdificarCasa(titulo))
        {
            int costeEdificarCasa = titulo.getPrecioEdificar();
            boolean tengoSaldo = tengoSaldo(costeEdificarCasa);
            if(tengoSaldo)
            {
                titulo.edificarCasa();
                modificarSaldo(-costeEdificarCasa);
                edificada = true;
            }
        }
        return edificada;
    }
    
    boolean edificarHotel(TituloPropiedad titulo){
        boolean edificada = false;
        if(puedoEdificarHotel(titulo))
        {
            int costeEdificarHotel = titulo.getPrecioEdificar();
            boolean tengoSaldo = tengoSaldo(costeEdificarHotel);
            if(tengoSaldo)
            {
                titulo.edificarHotel();
                modificarSaldo(-costeEdificarHotel);
                edificada = true;
            }
        }
        return edificada;
    }
    
    private void eliminarDeMisPropiedades(TituloPropiedad titulo){
        propiedades.remove(titulo);
        titulo.setPropietario(null);
    }
    
    protected boolean esDeMiPropiedad(TituloPropiedad titulo){
        for(TituloPropiedad p:propiedades)
            if(p == titulo)
                return true;
        return false;
    }
    
    boolean esDeMiPropiedadHipotecada(TituloPropiedad propiedad, boolean hipotecada)
    {
        for(TituloPropiedad p:obtenerPropiedades(hipotecada))
            if(p==propiedad)
                return true;
        return false;
    }
    
    /*boolean estoyEnCalleLibre(){ // NO SE LE DA USO
        throw new UnsupportedOperationException("Sin implementar");
    }*/
    
    Sorpresa getCartaLibertad(){
        return cartaLibertad;
    }
    
    public Casilla getCasillaActual(){
        return casillaActual;
    }
    
    boolean getEncarcelado(){
        return encarcelado;
    }
    
    ////
    
    protected int getFactorEspeculador()
    {
        return 0;
    }
    
    ////
    
    String getNombre(){
        return nombre;
    }
    
    ArrayList<TituloPropiedad> getPropiedades(){
        return propiedades;
    }
    
    public int getSaldo(){
        return saldo;
    }
        
    boolean hipotecarPropiedad(TituloPropiedad titulo){
        boolean hipotecada = false;
        if(esDeMiPropiedadHipotecada(titulo,false))
        {
            int costeHipoteca = titulo.hipotecar();
            modificarSaldo(costeHipoteca);
            hipotecada = true;
        }
        return hipotecada;
    }
    
    void irACarcel(Casilla casilla){
        setCasillaActual(casilla);
        setEncarcelado(true);
    }
    
    int modificarSaldo(int cantidad){
        saldo += cantidad;
        return saldo;
    }
    
    int obtenerCapital(){
        int valor_propiedades = 0;
        for(TituloPropiedad p:propiedades)
        {
            valor_propiedades += p.getPrecioCompra() +
                                 p.getNumCasas() * p.getPrecioEdificar() +
                                 4 * p.getNumHoteles() * p.getPrecioEdificar();
            if(p.getHipotecada())
                valor_propiedades -= p.getHipotecaBase();
        }
        
        return valor_propiedades + saldo; 
    }
    
    ArrayList<TituloPropiedad> obtenerPropiedades(boolean hipotecada){
        ArrayList<TituloPropiedad> properties = new ArrayList<>();
        for(TituloPropiedad p:propiedades)
            if(p.getHipotecada()==hipotecada)
                properties.add(p);
        return properties;
    }
    
    void pagarAlquiler(){
        Calle calleActual = (Calle) casillaActual;
        int costeAlquiler = calleActual.pagarAlquiler();
        modificarSaldo(-costeAlquiler);
    }
    
    protected void pagarImpuesto(){
        int costeImpuesto = casillaActual.getCoste(); 
        modificarSaldo(-costeImpuesto);
    }
    
    void pagarLibertad(int cantidad){
        boolean tengoSaldo = tengoSaldo(cantidad);
        if(tengoSaldo)
        {
            setEncarcelado(false);
            modificarSaldo(-cantidad);
        }
    }
    
    ////
    
    protected boolean puedoEdificarCasa(TituloPropiedad titulo)
    {
        int numCasas = titulo.getNumCasas();
        return ((numCasas < 4) && esDeMiPropiedad(titulo));
    }
    
    protected boolean puedoEdificarHotel(TituloPropiedad titulo)
    {
        int numCasas = titulo.getNumCasas();
        int numHoteles = titulo.getNumHoteles();
        return (numCasas == 4 && numHoteles < 4 && esDeMiPropiedad(titulo));
    }
    
    ////
    
    void setCartaLibertad(Sorpresa carta){
        cartaLibertad = carta;
        tieneCartaLibertad = true;
    }
    
    void setCasillaActual(Casilla casilla){
        casillaActual = casilla;
    }
    
    void setEncarcelado(boolean encarcelado){
        this.encarcelado = encarcelado;
    }
    
    boolean tengoCartaLibertad(){
        return tieneCartaLibertad;
    }
    
    protected boolean tengoSaldo(int cantidad){
        return saldo > cantidad;
    }
    
    boolean venderPropiedad(Calle casilla)
    {
        TituloPropiedad titulo = casilla.getTitulo();
        if(titulo != null && esDeMiPropiedad(titulo))
        {
            eliminarDeMisPropiedades(titulo);
            int precioVenta = titulo.calcularPrecioVenta();
            modificarSaldo(precioVenta);
            casilla.setTitulo(null);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Jugador{" + "encarcelado=" + encarcelado + ", nombre=" + nombre + 
               ", saldo=" + saldo + ", capital=" + obtenerCapital() + 
               ", cartaLibertad=" + cartaLibertad +
               ", propiedades=" + propiedades +
               ", casillaActual=" + casillaActual + '}';
    }
}