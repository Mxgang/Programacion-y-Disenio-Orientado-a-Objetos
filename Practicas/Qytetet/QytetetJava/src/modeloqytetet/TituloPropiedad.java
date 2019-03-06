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
public class TituloPropiedad {
    
    private String nombre;
    private boolean hipotecada;
    private int precioCompra, alquilerBase, hipotecaBase, precioEdificar, numHoteles,
        numCasas;
    private double factorRevalorizacion;
    
    private Jugador propietario;
    
    public TituloPropiedad(String nombre, int precioCompra, int alquilerBase, 
            int hipotecaBase, int precioEdificar, double factorRevalorizacion){
        
        this.hipotecada = false;
        this.numHoteles = 0;
        this.numCasas = 0;
        
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.alquilerBase = alquilerBase;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
        this.factorRevalorizacion = factorRevalorizacion;
       
    }
    
    public TituloPropiedad(boolean hipotecada, int numHoteles, int numCasas,
            String nombre, int precioCompra, int alquilerBase, 
            int hipotecaBase, int precioEdificar, float factorRevalorizacion){
        
        this.hipotecada = hipotecada;
        this.numHoteles = numHoteles;
        this.numCasas = numCasas;
        
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.alquilerBase = alquilerBase;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
        this.factorRevalorizacion = factorRevalorizacion;
       
    }
    
    int calcularCosteCancelar(){
        return (int) (calcularCosteHipotecar() * 1.1);
    }
    
    int calcularCosteHipotecar(){
        return (int) (hipotecaBase + numCasas * 0.5 * hipotecaBase + numHoteles * hipotecaBase);
    }
    
    int calcularImporteAlquiler(){
        return alquilerBase + (int) ((numCasas*0.5 + numHoteles*2) * precioEdificar); // HE AÑADIDO LO DE PRECIOEDIFICAR!!!!!!!!!
    }
    
    int calcularPrecioVenta(){
        return (int) (precioCompra + (numCasas + numHoteles) * precioEdificar * factorRevalorizacion);
    }
    
    void cancelarHipoteca(){
        hipotecada = false;
    }
    
    void edificarCasa(){
        numCasas++;
    }
    
    void edificarHotel(){
        numCasas -= 4;
        numHoteles++;
    }
    
    public String getNombre() {
        return nombre;
    }

    public Boolean getHipotecada() {
        return hipotecada;
    }

    public int getPrecioCompra() {
        return precioCompra;
    }

    int getAlquilerBase() {
        return alquilerBase;
    }

    int getHipotecaBase() {
        return hipotecaBase;
    }

    int getPrecioEdificar() {
        return precioEdificar;
    }
    
    Jugador getPropietario() {
        return propietario;
    }

    int getNumHoteles() {
        return numHoteles;
    }

    int getNumCasas() {
        return numCasas;
    }

    double getFactorRevalorizacion() {
        return factorRevalorizacion;
    }
    
    int hipotecar(){
        int costeHipoteca = calcularCosteHipotecar();
        setHipotecada(true);
        return costeHipoteca;
    }
    
    int pagarAlquiler(){
        int costeAlquiler = calcularImporteAlquiler();
        propietario.modificarSaldo(costeAlquiler);
        return costeAlquiler;
    }
    
    boolean propietarioEncarcelado(){
        return propietario.getEncarcelado();
    }
    
    public void setHipotecada(Boolean hipotecada) {
        this.hipotecada = hipotecada;
    }
    
    void setPropietario(Jugador propietario){
        this.propietario = propietario;
    }
    
    boolean tengoPropietario(){
        return propietario != null;
    }
    @Override
    public String toString() {
        return "TituloPropiedad{" + "nombre=" + nombre + ", hipotecada=" + 
                hipotecada + ", precioCompra=" + precioCompra + 
                ", alquilerBase=" + alquilerBase + ", hipotecaBase=" 
                + hipotecaBase + ", precioEdificar=" + precioEdificar 
                + ", numHoteles=" + numHoteles + ", numCasas=" + numCasas + 
                ", factorRevalorizacion=" + factorRevalorizacion + '}';
    }
    
    
}
